package com.ingic.auditix.ui.adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ingic.auditix.fragments.abstracts.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muniyemiftikhar on 4/24/2017.
 */


public class TabViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<BaseFragment> mFragmentList = new ArrayList<>();

    public TabViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(BaseFragment fragment) {

        mFragmentList.add(fragment);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void clearList() {
        mFragmentList.clear();
        notifyDataSetChanged();
    }


    public Fragment getRegisteredFragment(int position) {
        return mFragmentList.get(position);
    }
}