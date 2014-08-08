package com.hugelol.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.hugelol.R;
import com.hugelol.activity.LolPostActivity;
import com.hugelol.adapter.HugelolAdapter;
import com.hugelol.http.HTTPClient;
import com.hugelol.model.Hugelol;
import com.hugelol.parser.HTTPResponseParser;
import com.hugelol.parser.HugelolParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class Posts extends ListFragment implements OnScrollListener{
    
    private String url;
    private List<Hugelol> hugelols;
    private ProgressDialog progressDialog;
    private HugelolAdapter adapter;
    private Integer after;
    private boolean isVisible;
    private boolean isCreated;
    private View footer;
    private Parcelable listState;
    
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        this.url = this.getArguments().getString("url");
        setHasOptionsMenu(true);
        setListAdapter(null);
        footer = this.getLayoutInflater(savedInstance).inflate(R.layout.footer_list_layout, null);
        getListView().addFooterView(footer);
        this.setListShownNoAnimation(true);
        getListView().setOnScrollListener(this);
        getListView().setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                    long arg3) {
                Hugelol lolPost = hugelols.get(position);
                if(lolPost.getType() == 2){
                    final ImageView image = (ImageView)view.findViewById(R.id.image);
                    final ImageView gifImage = (ImageView)view.findViewById(R.id.gif_image);
                    ImageView gif = (ImageView)view.findViewById(R.id.image_gif);
                    gif.setVisibility(View.GONE);
                    final ProgressBar loading = (ProgressBar)view.findViewById(R.id.loading);
                    loading.setVisibility(View.VISIBLE);
                    Ion.with(gifImage).load(lolPost.getUrl()).setCallback(new FutureCallback<ImageView>(){
                        @Override
                        public void onCompleted(Exception arg0, ImageView view) {
                            image.setVisibility(View.GONE);
                            gifImage.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        }
                    });
                }else{
                    Intent intent = new Intent(getActivity(), LolPostActivity.class);
                    intent.putExtra("url", lolPost.getUrl());
                    intent.putExtra("title", lolPost.getTitle());
                    startActivity(intent);
                }
            }
            
        });
        Button loadMore = (Button)footer.findViewById(R.id.load_more_button);
        loadMore.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                loadPosts(true);
                
            }
            
        });
        if(isVisible && after == null){
            loadPosts(false);
        }
        isCreated = true;
    }
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = isVisibleToUser; 
            if(isCreated && after == null){
                loadPosts(false);
            }
        }
        else {  
            isVisible = isVisibleToUser;
        }
    }
    
    @Override
    public void onPause(){
        super.onPause();
        listState = getListView().onSaveInstanceState();
    }
    
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        isCreated = false; 
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
         super.onCreateOptionsMenu(menu, inflater);
         inflater.inflate(R.menu.hugelol, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        int id = menu.getItemId();
        if(id == R.id.refresh){
            loadPosts(false);
            return true;
        }
        return super.onOptionsItemSelected(menu);
        
    }
    
    @Override
    public void onResume(){
        super.onResume();
        if(adapter != null){
            setListAdapter(adapter);
            if(listState != null){
                ListView listView = getListView();
                listView.onRestoreInstanceState(listState);
                listView.setDividerHeight(0);
            }
        }
    }
    
    private void loadPosts(boolean after){
        LoadPosts loadFront = new LoadPosts();
        if(!after){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading posts...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        loadFront.execute(after);
    }

    private class LoadPosts extends AsyncTask<Boolean, Void, Void>{

        @Override
        protected Void doInBackground(Boolean... param) {            
            boolean loadAfter = param[0];
            if(loadAfter){
                url = url + "after="+after;
            }else{
                hugelols = new ArrayList<Hugelol>();
            }
            HTTPClient httpClient = new HTTPClient();
            try {
                String[] array = HTTPResponseParser.doParse(httpClient.getResponseAsString(url));
                
                for(int i = 0; i < array.length; i++){
                    Hugelol hugelol = HugelolParser.doParse(array[i], getActivity());
                    if(i == 9){
                        after = hugelol.getId();
                    }
                    hugelols.add(hugelol);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result){
            if(isAdded()){
                ListView listView = getListView();
                Parcelable state = listView.onSaveInstanceState();
                listView.setDividerHeight(0);
                adapter = new HugelolAdapter(getActivity(), R.layout.list_layout, hugelols);
                setListAdapter(adapter);
                adapter.notifyDataSetChanged();  
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                listView.onRestoreInstanceState(state);
            }
        }  
    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        if (listView.getLastVisiblePosition() >= listView.getCount() - 1) {
            loadPosts(true);
        }
    }

}
