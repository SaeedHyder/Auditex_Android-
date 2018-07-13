package com.ingic.auditix.fragments.news;

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
import com.ingic.auditix.fragments.standard.HomeTabFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.binders.news.NewsCategoryBinder;
import com.ingic.auditix.ui.binders.news.NewsSubscriptionBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    ArrayList<NewsCategoryEnt> subscriptionCollection;
    private RecyclerViewItemListener newSubscriptionListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            serviceHelper.enqueueCall(webService.unsubscribeNews(((NewsCategoryEnt) Ent).getNewsCategoryId(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_NEWS);
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            getDockActivity().replaceDockableFragment(NewsCategoryDetailFragment.newInstance((NewsCategoryEnt) Ent), NewsCategoryDetailFragment.TAG);
        }
    };
    private RecyclerViewItemListener newCategoriesListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            getDockActivity().replaceDockableFragment(NewsCategoryDetailFragment.newInstance((NewsCategoryEnt) Ent), NewsCategoryDetailFragment.TAG);
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
            case WebServiceConstants.GET_ALL_NEWS_SUBSCRIBE:
                getAllSubscribeNews((ArrayList<NewsCategoryEnt>) result);
                break;
            case WebServiceConstants.UNSUBSCRIBE_NEWS:
                serviceHelper.enqueueCall(webService.getAllSubscribeNews(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_SUBSCRIBE);
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getDockActivity(), 3, GridLayoutManager.VERTICAL, false);
        rvCategories.BindRecyclerView(new NewsCategoryBinder(options, newCategoriesListener), result, gridLayoutManager, new DefaultItemAnimator());
        rvCategories.setHasFixedSize(true);
        rvCategories.addItemDecoration(new GridSpacingItemDecoration(3, Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)), true));
        rvCategories.setNestedScrollingEnabled(false);
    }

    private void getAllSubscribeNews(ArrayList<NewsCategoryEnt> results) {
 /*       if (results.size() > 2)
            subscriptionCollection = new ArrayList<>(results.subList(0, 2));
        else*/
            subscriptionCollection = new ArrayList<>(results);

        rvSubscribe.BindRecyclerView(new NewsSubscriptionBinder(options,
                        newSubscriptionListener, prefHelper), subscriptionCollection, new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false)
                , new DefaultItemAnimator());
        if (subscriptionCollection.size() <= 0) {
            txtSubscriptionNoData.setVisibility(View.VISIBLE);
            rvSubscribe.setVisibility(View.GONE);
            btnSubscriptionSeeall.setVisibility(View.INVISIBLE);
        } else {
            txtSubscriptionNoData.setVisibility(View.GONE);
            rvSubscribe.setVisibility(View.VISIBLE);
            btnSubscriptionSeeall.setVisibility(View.VISIBLE);
        }
    }

    private void getNewsCategories() {
        serviceHelper.enqueueCall(webService.getAllSubscribeNews(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_SUBSCRIBE);
        serviceHelper.enqueueCall(webService.getAllNewsCategories(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_CATEGORIES);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper) {
    }


    @OnClick(R.id.btn_subscription_seeall)
    public void onViewClicked() {
        getDockActivity().replaceDockableFragment(NewsSubscriptionLIstFragment.newInstance(), NewsSubscriptionLIstFragment.TAG);
    }
}