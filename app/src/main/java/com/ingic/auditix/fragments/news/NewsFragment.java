package com.ingic.auditix.fragments.news;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.EnableFilterDataEnt;
import com.ingic.auditix.entities.NewItemDetailEnt;
import com.ingic.auditix.entities.NewsCategoryEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
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
    ArrayList<NewItemDetailEnt> subscriptionCollection;
    @BindView(R.id.MainContainer)
    LinearLayout MainContainer;
    @BindView(R.id.containerFragment)
    FrameLayout containerFragment;
    private boolean isFilterVisible = false;
    private EnableFilterDataEnt filterDataEnt;
    private RecyclerViewItemListener newSubscriptionListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            serviceHelper.enqueueCall(webService.unsubscribeNews(((NewItemDetailEnt) Ent).getNewsID(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_NEWS);
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            openChannelDetail((NewItemDetailEnt) Ent);
        }
    };
    private RecyclerViewItemListener newCategoriesListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            replaceFromParentFragment(NewListingByCategoryFragment.newInstance(((NewsCategoryEnt) Ent).getId()), NewsCategoryDetailFragment.TAG);
        }
    };

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void openChannelDetail(NewItemDetailEnt Ent) {
        replaceFromParentFragment(NewsChannelDetailFragment.newInstance(Ent), NewsCategoryDetailFragment.TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_ALL_NEWS_CATEGORIES:
                bindNewsCategories((ArrayList<NewsCategoryEnt>) result);
                break;
            case WebServiceConstants.GET_ALL_NEWS_SUBSCRIBE:
                getAllSubscribeNews((ArrayList<NewItemDetailEnt>) result);
                break;
            case WebServiceConstants.UNSUBSCRIBE_NEWS:
                serviceHelper.enqueueCall(webService.getAllSubscribeNews(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_SUBSCRIBE);
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        if (getMainActivity().newsFilterFragment != null) {
            getMainActivity().setRightSideFragment(getMainActivity().newsFilterFragment);
            getMainActivity().newsFilterFragment.setListener((filters, isClear) -> {
                if (isClear) {
                    filterDataEnt = null;
                    hideFilterList();
                } else {
                    filterDataEnt = filters;
                    showFilterList();
                }
            });
        }
        titleBar.addBackground();
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.news));
        titleBar.showMenuButton();
        titleBar.showFilterButton(v -> {
            getMainActivity().isNavigationGravityRight = true;
            getMainActivity().getDrawerLayout().openDrawer(Gravity.RIGHT);
        });
    }

    private void showFilterList() {
        MainContainer.setVisibility(View.GONE);
        containerFragment.setVisibility(View.VISIBLE);
        replaceFragment(NewsFilterListFragment.newInstance(filterDataEnt));
        isFilterVisible = true;
    }

    private void hideFilterList() {
        if (getChildFragmentManager().findFragmentById(R.id.containerFragment) != null) {
            getChildFragmentManager().beginTransaction().
                    remove(getChildFragmentManager().findFragmentById(R.id.containerFragment)).commit();
            getChildFragmentManager().popBackStack();
            getMainActivity().newsFilterFragment.clearFilters();
            MainContainer.setVisibility(View.VISIBLE);
            containerFragment.setVisibility(View.GONE);
            isFilterVisible = false;
        }

    }

    public void replaceFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        //transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.replace(R.id.containerFragment, fragment);
        transaction.commit();


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
        if (getMainActivity().newsFilterFragment != null) {
            getMainActivity().newsFilterFragment.clearFilters();
        }
        /*if (!getMainActivity().newsFilterFragment.isClear()) {
            showFilterList();
        } else {
            hideFilterList();
        }*/
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

    private void getAllSubscribeNews(ArrayList<NewItemDetailEnt> results) {
 /*       if (results.size() > 2)
            subscriptionCollection = new ArrayList<>(results.subList(0, 2));
        else*/
        subscriptionCollection = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            if (i == 4) {
                break;
            }
            subscriptionCollection.add(results.get(i));
        }
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
        replaceFromParentFragment(NewsSubscriptionLIstFragment.newInstance(), NewsSubscriptionLIstFragment.TAG);
    }

}