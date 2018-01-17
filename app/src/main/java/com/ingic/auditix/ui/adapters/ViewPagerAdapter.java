package com.ingic.auditix.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.WalkThroughItemFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<WalkThroughItemFragment> pagesArray;
    private String[] titles = new String[]{""};
    private ChangeBounds changeBoundsTransition;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<WalkThroughItemFragment> pagesArray) {

        super(fm);
        this.pagesArray = pagesArray;
        changeBoundsTransition = new ChangeBounds();
        changeBoundsTransition.setDuration(300);

    }

    public int getRealCount() {
        return pagesArray.size();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return pagesArray.size();
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        WalkThroughItemFragment fragment = pagesArray.get(position);
        fragment.setContent(position, pagesArray.size());
        return fragment;
    }


}
