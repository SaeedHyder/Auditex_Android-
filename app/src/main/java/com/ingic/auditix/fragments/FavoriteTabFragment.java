package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.ui.adapters.TitleViewpagerAdapter;
import com.ingic.auditix.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 1/12/2018.
 */
public class FavoriteTabFragment extends BaseFragment {
    public static final String TAG = "FavoriteTabFragment";
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    private TitleViewpagerAdapter adapter;

    public static FavoriteTabFragment newInstance() {
        Bundle args = new Bundle();

        FavoriteTabFragment fragment = new FavoriteTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.addBackground();
        titleBar.setSubHeading(getString(R.string.my_favourite));
        titleBar.showBackButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_tab, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewPager();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setViewPager() {
        adapter = new TitleViewpagerAdapter(getChildFragmentManager());

        if (adapter.getCount() > 0) {
            adapter.clearList();
        }

        adapter.addFragment(FavoriteListFragment.newInstance(AppConstants.TAB_NEWS), getString(R.string.news));
        adapter.addFragment(FavoriteListFragment.newInstance(AppConstants.TAB_PODCAST), getString(R.string.podcast));
        adapter.addFragment(FavoriteListFragment.newInstance(AppConstants.TAB_BOOKS), getString(R.string.books));
        viewpager.setAdapter(adapter);
        viewpager.getAdapter().notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewpager);
    }
}