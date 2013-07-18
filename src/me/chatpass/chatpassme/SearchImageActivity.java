package com.bignerdranch.android.photogallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class SearchImageActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        
        ViewPager pager = (ViewPager)findViewById(R.id.viewPager);
        FragmentManager fm = getSupportFragmentManager();
        
        pager.setAdapter(new FragmentStatePagerAdapter(fm) {
            
            @Override
            public int getCount() {
                return 2;
            }
            
            @Override
            public Fragment getItem(int pos) {
				return new SearchImageFragment();
            }
        });
    }
}
