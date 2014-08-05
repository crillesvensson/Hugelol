package com.hugelol.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hugelol.fragment.Fresh;
import com.hugelol.fragment.Front;
import com.hugelol.fragment.Rising;

public class TabAdapter extends FragmentPagerAdapter{

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int selected) {
        switch(selected){
            case 0:
                return new Front();
            case 1:
                return new Rising();
            case 2:
                return new Fresh();
            default:
                return new Front();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
