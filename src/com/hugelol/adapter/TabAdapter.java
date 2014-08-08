package com.hugelol.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.hugelol.fragment.Posts;

public class TabAdapter extends FragmentPagerAdapter{

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int selected) {
        Posts posts = new Posts();
        Bundle args = new Bundle();
        switch(selected){
            case 0:
                args.putString("url", "http://hugelol.com/api/front.php?");
                break;
            case 1:
                args.putString("url", "http://hugelol.com/api/rising.php?");
                break;
            case 2:
                args.putString("url", "http://hugelol.com/api/fresh.php?");
                break;
            default:
                args.putString("url", "http://hugelol.com/api/front.php?");
        }
        posts.setArguments(args);
        return posts;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
