package com.ingic.auditix.ui.adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.ui.views.WrappingViewPager;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

/**
 * Created on 1/6/2018.
 */

public class TitleViewpagerAdapter extends FragmentStatePagerAdapter {
    private final List<BaseFragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private int mCurrentPosition = -1;
    public TitleViewpagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public String getItemTitle(int position){
        return mFragmentTitleList.get(position);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(BaseFragment fragment, String title) {

        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

    }
   /* @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != mCurrentPosition) {
            Fragment fragment = (Fragment) object;
            WrappingViewPager pager = (WrappingViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }*/

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void clearList() {
        mFragmentList.clear();
        mFragmentTitleList.clear();
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return mFragmentList.get(position);
    }
}
