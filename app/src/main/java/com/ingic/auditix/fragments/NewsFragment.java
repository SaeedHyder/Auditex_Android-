package com.ingic.auditix.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NewsCategoryEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.binders.NewsCategoryBinder;
import com.ingic.auditix.ui.binders.NewsSubscriptionBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import droidninja.filepicker.utils.GridSpacingItemDecoration;

/**
 * Created on 12/23/2017.
 */
public class NewsFragment extends BaseFragment implements ViewPagerFragmentLifecycleListener {
    @BindView(R.id.txt_subscription)
    AnyTextView txtSubscription;
    @BindView(R.id.btn_subscription_seeall)
    AnyTextView btnSubscriptionSeeall;
    @BindView(R.id.rv_subscribe)
    CustomRecyclerView rvSubscribe;
    @BindView(R.id.txt_subscription_no_data)
    AnyTextView txtSubscriptionNoData;
    @BindView(R.id.txt_news)
    AnyTextView txtNews;
    @BindView(R.id.rv_categories)
    CustomRecyclerView rvCategories;
    @BindView(R.id.txt_news_no_data)
    AnyTextView txtNewsNoData;
    @BindView(R.id.parent_scroll)
    NestedScrollView parentScroll;
    Unbinder unbinder;
    DisplayImageOptions options;
    private RecyclerViewItemListener newSubscriptionListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {

        }
    };
    private RecyclerViewItemListener newCategoriesListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            getDockActivity().replaceDockableFragment(NewsCategoryDetailFragment.newInstance((NewsCategoryEnt) Ent),NewsCategoryDetailFragment.TAG);
        }
    };

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();

        NewsFragment fragment = new NewsFragment();
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
    public void onResume() {
        super.onResume();
        setTitleBar(((HomeTabFragment) getParentFragment()).getTitleBar());
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_ALL_NEWS_CATEGORIES:
                bindNewsCategories((ArrayList<NewsCategoryEnt>) result);
                break;
        }
    }

    public void setTitleBar(TitleBar titleBar) {
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.news));
        titleBar.showBackButton();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        getAllSubscribeNews();
        getNewsCategories();
//        setTitleBar(((HomeTabFragment) getParentFragment()).getTitleBar());

    }
    private void bindNewsCategories(ArrayList<NewsCategoryEnt> result) {
        if (result.size() <= 0) {
            txtNewsNoData.setVisibility(View.VISIBLE);
            rvCategories.setVisibility(View.GONE);
        } else {
            txtNewsNoData.setVisibility(View.GONE);
            rvCategories.setVisibility(View.VISIBLE);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getDockActivity(), 2, GridLayoutManager.VERTICAL, false);
        rvCategories.BindRecyclerView(new NewsCategoryBinder(options, newCategoriesListener), result, gridLayoutManager, new DefaultItemAnimator());
        rvCategories.setHasFixedSize(true);
        rvCategories.addItemDecoration(new GridSpacingItemDecoration(2, Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)), true));
        rvCategories.setNestedScrollingEnabled(false);
    }

    private void getAllSubscribeNews() {
        ArrayList<String> dummyCollection = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dummyCollection.add("");
        }
        rvSubscribe.BindRecyclerView(new NewsSubscriptionBinder(options,
                        newSubscriptionListener), dummyCollection, new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false)
                , new DefaultItemAnimator());
        if (dummyCollection.size() <= 0) {
            txtSubscriptionNoData.setVisibility(View.VISIBLE);
            rvSubscribe.setVisibility(View.GONE);
        } else {
            txtSubscriptionNoData.setVisibility(View.GONE);
            rvSubscribe.setVisibility(View.VISIBLE);
        }
    }

    private void getNewsCategories() {
        serviceHelper.enqueueCall(webService.getAllNewsCategories(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_CATEGORIES);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper) {
    }
}