package com.hugelol.fragment;

import java.util.ArrayList;
import java.util.List;
import com.hugelol.R;
import com.hugelol.activity.LolPostActivity;
import com.hugelol.adapter.HugelolAdapter;
import com.hugelol.http.HTTPClient;
import com.hugelol.model.Hugelol;
import com.hugelol.parser.HTTPResponseParser;
import com.hugelol.parser.HugelolParser;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Fresh extends ListFragment{
    
    private List<Hugelol> hugelols;
    private ProgressDialog progressDialog;
    private HugelolAdapter adapter;
    private Integer after;
    private boolean isVisible;
    private boolean isCreated;
    private Parcelable listState;
    
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        setHasOptionsMenu(true);
        View footer = getLayoutInflater(savedInstance).inflate(R.layout.footer_list_layout, null);
        setListAdapter(null);
        getListView().addFooterView(footer);
        this.setListShownNoAnimation(true);
        getListView().setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                    long arg3) {
                Intent intent = new Intent(getActivity(), LolPostActivity.class);
                Hugelol hugelol = hugelols.get(position);
                intent.putExtra("url", hugelol.getUrl());
                intent.putExtra("title", hugelol.getTitle());
                startActivity(intent);
            }
        });
        
        Button loadMore = (Button)footer.findViewById(R.id.load_more_button);
        loadMore.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                loadFreshPosts(true);
            }
        }); 
        if(isVisible && after == null){
            loadFreshPosts(false);
        }
        isCreated = true;
    }
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = isVisibleToUser;
            if(isCreated && after == null){
                loadFreshPosts(false);
            }
        }
        else {  
            isVisible = isVisibleToUser;
        }
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
            loadFreshPosts(false);
            return true;
        }
        return super.onOptionsItemSelected(menu);
    }
    
    @Override
    public void onPause(){
        super.onPause();
        listState = getListView().onSaveInstanceState();
    }
    
    @Override
    public void onResume(){
        super.onResume();
        if(adapter != null){
            setListAdapter(adapter);
            if(listState != null){
                getListView().onRestoreInstanceState(listState);
            }
        }
    }
    
    private void loadFreshPosts(boolean after){
        LoadFresh loadFresh = new LoadFresh();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading posts..");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        loadFresh.execute(after);
    }
    
    private class LoadFresh extends AsyncTask<Boolean, Void, Void>{
        @Override
        protected Void doInBackground(Boolean... params) {
            String url = "http://hugelol.com/api/fresh.php?";
            boolean loadAfter = params[0];
            if(loadAfter){
                url += "after=" + after;
            }
            HTTPClient httpClient = new HTTPClient();
            try {
                String[] array = HTTPResponseParser.doParse(httpClient.getResponseAsString(url));
                hugelols = new ArrayList<Hugelol>();
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
                listView.setDividerHeight(0);
                adapter = new HugelolAdapter(getActivity(), R.layout.list_layout, hugelols);
                setListAdapter(adapter);
                adapter.notifyDataSetChanged(); 
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        }
    }
    
}
