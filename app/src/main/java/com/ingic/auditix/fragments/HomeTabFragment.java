package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.adapters.TabViewPagerAdapter;
import com.ingic.auditix.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 12/23/2017.
 */
public class HomeTabFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    Unbinder unbinder;
    private TabViewPagerAdapter adapter;
    private ArrayList<Integer> selectedTabsIcons;
    private ArrayList<Integer> unselectedTabsIcons;
    private ArrayList<String> tabTexts;
    private String tag = AppConstants.TAB_NEWS;
    private int INDEX_NEWS = 0;
    private int INDEX_PODCAST = 1;
    private int INDEX_BOOKS = 2;
    private int INDEX_SEARCH = 3;
    private int INDEX_PROFILE = 4;
    private TitleBar titleBar;

    public static HomeTabFragment newInstance() {
        Bundle args = new Bundle();

        HomeTabFragment fragment = new HomeTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TabViewPagerAdapter(getChildFragmentManager());
        selectedTabsIcons = new ArrayList<>(5);
        unselectedTabsIcons = new ArrayList<>(5);
        tabTexts = new ArrayList<>(5);
        if (getArguments() != null) {
        }

    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.addBackground();
        this.titleBar = titleBar;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs_home, container, false);
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

    public void setTag(String tag) {
        this.tag = tag;
    }

    private void bindTabsResources(BaseFragment fragment, Integer selectedTabIcon, Integer unselectedTabIcon, Integer tabText) {
        adapter.addFragment(fragment);
        selectedTabsIcons.add(selectedTabIcon);
        unselectedTabsIcons.add(unselectedTabIcon);
        tabTexts.add(getString(tabText));
    }

    private void bindSelectedTabView(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        if (view != null) {
            ImageView iv = (ImageView) view.findViewById(R.id.tab_icon);
            TextView tv = (TextView) view.findViewById(R.id.tab_text);
            iv.setImageDrawable(ContextCompat.getDrawable(getDockActivity(), selectedTabsIcons.get(tab.getPosition())));
            tv.setText(tabTexts.get(tab.getPosition()));
            tv.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.app_title_orange));
        }
        ViewPagerFragmentLifecycleListener listener =
                (ViewPagerFragmentLifecycleListener) adapter.getItem(tab.getPosition());
        if (listener!=null){
            listener.onResumeFragment(getDockActivity(),prefHelper);
        }
    }

    private void bindUnselectedTabView(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        if (view != null) {
            ImageView iv = (ImageView) view.findViewById(R.id.tab_icon);
            TextView tv = (TextView) view.findViewById(R.id.tab_text);
            iv.setImageDrawable(ContextCompat.getDrawable(getDockActivity(), unselectedTabsIcons.get(tab.getPosition())));
            tv.setText(tabTexts.get(tab.getPosition()));
            tv.setTextColor(ContextCompat.getColor(getDockActivity(), R.color.app_font_gray));
        }
        ViewPagerFragmentLifecycleListener listener =
                (ViewPagerFragmentLifecycleListener) adapter.getItem(tab.getPosition());
        if (listener!=null){
            listener.onPauseFragment();
        }
    }

    private void setViewPager() {


        if (adapter.getCount() > 0) {
            adapter.clearList();
        }
        bindTabsResources(new NewsFragment(), R.drawable.news_orange_small, R.drawable.news_grey, R.string.news);
        bindTabsResources(new PodcastFragmentNew(), R.drawable.podcast_orange_small, R.drawable.podcast_icon_grey, R.string.podcast);
        bindTabsResources(new BooksFragment(), R.drawable.books_orange, R.drawable.books_grey, R.string.books);
        bindTabsResources(new SearchFragment(), R.drawable.search_orange, R.drawable.search_grey, R.string.search);
        bindTabsResources(new ProfileFragment(), R.drawable.profile_icon_orange, R.drawable.profile_icon_grey, R.string.my_profile);
        pager.setAdapter(adapter);
        pager.getAdapter().notifyDataSetChanged();
        tabLayout.setupWithViewPager(pager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab_item_home);
                bindUnselectedTabView(tab);
            }
        }
        selectTabBasedOnTag(tag);
        tabLayout.addOnTabSelectedListener(this);
    }

    private void selectTabBasedOnTag(String tag) {
        switch (tag) {
            case AppConstants.TAB_NEWS:
                pager.setCurrentItem(INDEX_NEWS);
                bindSelectedTabView(tabLayout.getTabAt(INDEX_NEWS));
                break;
            case AppConstants.TAB_PODCAST:
                pager.setCurrentItem(INDEX_PODCAST);
                bindSelectedTabView(tabLayout.getTabAt(INDEX_PODCAST));
                break;
            case AppConstants.TAB_BOOKS:
                pager.setCurrentItem(INDEX_BOOKS);
                bindSelectedTabView(tabLayout.getTabAt(INDEX_BOOKS));
                break;
            case AppConstants.TAB_SEARCH:
                pager.setCurrentItem(INDEX_SEARCH);
                bindSelectedTabView(tabLayout.getTabAt(INDEX_SEARCH));
                break;
            case AppConstants.TAB_PROFILE:
                pager.setCurrentItem(INDEX_PROFILE);
                bindSelectedTabView(tabLayout.getTabAt(INDEX_PROFILE));
                break;
            default:
                pager.setCurrentItem(INDEX_NEWS);
                bindSelectedTabView(tabLayout.getTabAt(INDEX_NEWS));
                break;

        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        bindSelectedTabView(tab);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        bindUnselectedTabView(tab);

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}