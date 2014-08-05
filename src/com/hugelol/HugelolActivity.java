package com.hugelol;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.hugelol.adapter.TabAdapter;

public class HugelolActivity extends FragmentActivity implements TabListener {
    
    //Tab names
    private String[] tabNames = {"Front", "Rising", "Fresh"};
    private TabAdapter tabAdapter;
    private ViewPager tabViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hugelol);
        //Set action mode to tabs
        final ActionBar actionBar = this.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //Add tabs, set tab names and tab listener
        for(String tabName : tabNames){
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(tabName)
                            .setTabListener(this));
        }
        
        this.tabAdapter = new TabAdapter(this.getSupportFragmentManager());
        this.tabViewPager = (ViewPager)findViewById(R.id.tab_pager);
        this.tabViewPager.setAdapter(tabAdapter);
        this.tabViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        if(this.tabViewPager != null){
            this.tabViewPager.setCurrentItem(tab.getPosition());
        }
        
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        
    }
}
