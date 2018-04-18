package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.adapters.TabViewPagerAdapter;
import com.ingic.auditix.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 12/23/2017.
 */
public class HomeTabFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    private final int INDEX_NEWS = 0;
    private final int INDEX_PODCAST = 1;
    private final int INDEX_SEARCH = 2;
    private final int INDEX_BOOKS = 3;
    private final int INDEX_PROFILE = 4;
    @BindView(R.id.pager)
    FrameLayout pager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    Unbinder unbinder;
    private TabViewPagerAdapter adapter;
    private ArrayList<Integer> selectedTabsIcons;
    private ArrayList<Integer> unselectedTabsIcons;
    private ArrayList<String> tabTexts;
    private String tag = AppConstants.TAB_NEWS;
    private TitleBar titleBar;
    private View viewParent;

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
        setViewPager();
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

    private void assignTabTag(int position) {
        switch (position) {
            case INDEX_NEWS:
                tag = AppConstants.TAB_NEWS;
                break;
            case INDEX_PODCAST:
                tag = AppConstants.TAB_PODCAST;
                break;
            case INDEX_BOOKS:
                tag = AppConstants.TAB_BOOKS;
                break;
            case INDEX_SEARCH:
                tag = AppConstants.TAB_SEARCH;
                break;
            case INDEX_PROFILE:
                tag = AppConstants.TAB_PROFILE;
                break;
            default:
                tag = AppConstants.TAB_NEWS;
                break;
        }
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
        assignTabTag(tab.getPosition());
        replaceTab(tab.getPosition());
        ViewPagerFragmentLifecycleListener listener =
                (ViewPagerFragmentLifecycleListener) adapter.getItem(tab.getPosition());
        if (listener != null) {
            listener.onResumeFragment(getDockActivity(), prefHelper);
        }
    }

    private void replaceTab(int position) {
        android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.replace(R.id.pager, adapter.getItem(position));
        transaction.commit();


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
        if (listener != null) {
            listener.onPauseFragment();
        }
    }

    private void setViewPager() {


        if (adapter.getCount() > 0) {
            adapter.clearList();
            tabLayout.removeAllTabs();
        }
        bindTabsResources(new NewsFragment(), R.drawable.news_orange_small, R.drawable.news_grey, R.string.news);
        bindTabsResources(new PodcastFragmentNew(), R.drawable.podcast_orange_small, R.drawable.podcast_icon_grey, R.string.podcast);
        bindTabsResources(new SearchFragment(), R.drawable.search_orange, R.drawable.search_grey, R.string.search);
        bindTabsResources(new BooksFragment(), R.drawable.books_orange, R.drawable.books_grey, R.string.books);
        bindTabsResources(new ProfileFragment(), R.drawable.profile_icon_orange, R.drawable.profile_icon_grey, R.string.profile);
        for (int i = 0; i < adapter.getCount(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(R.layout.tab_item_home);
            tabLayout.addTab(tab, false);
            bindUnselectedTabView(tab);
        }
        if (prefHelper.isGuest()) {
          /*  ViewGroup vgTab = (ViewGroup) tabLayout.getChildAt(INDEX_PROFILE);
            if (vgTab != null)
                vgTab.setEnabled(false);*/
          Objects.requireNonNull(tabLayout.getTabAt(INDEX_PROFILE)).getCustomView().setOnTouchListener(new View.OnTouchListener() {
              @Override
              public boolean onTouch(View v, MotionEvent event) {
                  showGuestMessage();
                  return true;
              }
          });
        }
        tabLayout.addOnTabSelectedListener(this);
        selectTabBasedOnTag(tag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (viewParent != null) {
            ViewGroup parent = (ViewGroup) viewParent.getParent();
            if (parent != null)
                parent.removeView(viewParent);
        }
        try {
            viewParent = inflater.inflate(R.layout.fragment_tabs_home, container, false);

        } catch (InflateException e) {
            e.printStackTrace();
        }
        if (viewParent != null)
            ButterKnife.bind(this, viewParent);

        return viewParent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void selectTabBasedOnTag(String tag) {
        TabLayout.Tab tab;
        switch (tag) {
            case AppConstants.TAB_NEWS:
                tab = tabLayout.getTabAt(INDEX_NEWS);
                tab.select();
                break;
            case AppConstants.TAB_PODCAST:
                tab = tabLayout.getTabAt(INDEX_PODCAST);
                tab.select();
                break;
            case AppConstants.TAB_BOOKS:
                tab = tabLayout.getTabAt(INDEX_BOOKS);
                tab.select();
                break;
            case AppConstants.TAB_SEARCH:
                tab = tabLayout.getTabAt(INDEX_SEARCH);
                tab.select();
                break;
            case AppConstants.TAB_PROFILE:
                tab = tabLayout.getTabAt(INDEX_PROFILE);
                tab.select();
                break;
            default:
                tab = tabLayout.getTabAt(INDEX_NEWS);
                tab.select();
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
        //  bindSelectedTabView(tab);
    }
}