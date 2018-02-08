package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.ui.adapters.TitleViewpagerAdapter;
import com.ingic.auditix.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 1/12/2018.
 */
public class FavoriteTabFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    public static final String TAG = "FavoriteTabFragment";
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    FrameLayout viewpager;
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
    private ArrayList<BaseFragment> fragmentList;
    private int startingWithIndex = 0;

    private void setViewPager() {
        adapter = new TitleViewpagerAdapter(getChildFragmentManager());

        if (adapter.getCount() > 0) {
            adapter.clearList();
        }
        fragmentList = new ArrayList<>(3);
        fragmentList.add(NewsFavoriteListFragment.newInstance());
        fragmentList.add(PodcastFavoriteListFragment.newInstance());
        fragmentList.add(BookFavoriteListFragment.newInstance());
        tabLayout.addTab(tabLayout.newTab().setText(R.string.news), true);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.podcast), false);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.books), false);
        replaceTab(startingWithIndex);
        tabLayout.addOnTabSelectedListener(this);

    }
    private void replaceTab(int position) {
        android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.replace(R.id.viewpager, fragmentList.get(position));
        transaction.commit();


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        replaceTab(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}