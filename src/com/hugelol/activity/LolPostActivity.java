package com.hugelol.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.hugelol.R;
import com.koushikdutta.ion.Ion;

public class LolPostActivity extends Activity {
    
    private TextView title;
    private ImageView gif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        
        //Set title 
        title = (TextView)findViewById(R.id.gif_title);
        String titleString = getIntent().getExtras().getString("title");
        title.setText(titleString);

        String url = getIntent().getExtras().getString("url");
        if(url != null){
            gif = (ImageView)findViewById(R.id.gif);
            Ion.with(gif).load(url);
        }
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
