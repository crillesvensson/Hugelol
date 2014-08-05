package com.hugelol.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.hugelol.R;
import com.hugelol.activity.LolPostActivity;
import com.hugelol.adapter.HugelolAdapter;
import com.hugelol.http.HTTPClient;
import com.hugelol.model.Hugelol;
import com.hugelol.parser.HTTPResponseParser;
import com.hugelol.parser.HugelolParser;

public class Rising extends ListFragment{
    
    private List<Hugelol> hugelols;
    private ProgressDialog progressDialog;
    private Integer after;
    private boolean isVisible;
    private boolean isCreated;
    
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        setHasOptionsMenu(true);
        View footer = getLayoutInflater(savedInstance).inflate(R.layout.footer_list_layout, null);
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
                loadRisingPosts(true);
            }
        });
        if(isVisible && after == null){
            loadRisingPosts(false);
        }
        isCreated = true;
        
    }
    
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        isCreated = false;
    }
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = isVisibleToUser;
            if(isCreated && after == null){
                loadRisingPosts(false);
            }
        }
        else {  
            isVisible = isVisibleToUser;
        }
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
            loadRisingPosts(false);
            return true;
        }
        return super.onOptionsItemSelected(menu);
    }
    
    @Override
    public void onResume(){
        super.onResume();
        
    }
    
    private void loadRisingPosts(boolean after){
        LoadRising loadRising = new LoadRising();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading posts..");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        loadRising.execute(after);
    }
    
    private class LoadRising extends AsyncTask<Boolean, Void, Void>{
        @Override
        protected Void doInBackground(Boolean... params) {
            String url = "http://hugelol.com/api/rising.php?";
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
                HugelolAdapter adapter = new HugelolAdapter(getActivity(), R.layout.list_layout, hugelols);
                setListAdapter(adapter);
                adapter.notifyDataSetChanged(); 
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        }
    }
}
