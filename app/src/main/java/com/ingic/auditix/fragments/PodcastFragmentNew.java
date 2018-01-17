package com.ingic.auditix.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastCategoryHomeEnt;
import com.ingic.auditix.entities.PodcastDetailHomeEnt;
import com.ingic.auditix.entities.PodcastHomeEnt;
import com.ingic.auditix.entities.SubscribePodcastEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.helpers.InternetHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.FilterDoneClickListener;
import com.ingic.auditix.interfaces.LoadMoreListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.binders.PodcastDefaultCategoryBinder;
import com.ingic.auditix.ui.binders.PodcastRecommendedBinder;
import com.ingic.auditix.ui.binders.PodcastSubscriptionBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 1/10/2018.
 */
public class PodcastFragmentNew extends BaseFragment implements ViewPagerFragmentLifecycleListener, FilterDoneClickListener {
    public static final String TAG = "PodcastFragmentNew";
    @BindView(R.id.txt_subscription)
    AnyTextView txtSubscription;
    @BindView(R.id.btn_subscription_seeall)
    AnyTextView btnSubscriptionSeeall;
    @BindView(R.id.rv_subscribe)
    CustomRecyclerView rvSubscribe;
    @BindView(R.id.txt_recommended)
    AnyTextView txtRecommended;
    @BindView(R.id.rv_recommended)
    CustomRecyclerView rvRecommended;
    @BindView(R.id.rv_podcast_default)
    CustomRecyclerView rvPodcastDefault;
    @BindView(R.id.parent_scroll)
    NestedScrollView nestedScrollView;
    Unbinder unbinder;
    DisplayImageOptions options;
    String categoriesIds;
    @BindView(R.id.txt_subscription_no_data)
    AnyTextView txtSubscriptionNoData;
    @BindView(R.id.txt_recommended_no_data)
    AnyTextView txtRecommendedNoData;
    @BindView(R.id.txt_default_no_data)
    AnyTextView txtDefaultNoData;
    private LinearLayoutManager defaultLayoutManager;
    private LinearLayoutManager recommendedLayoutManager;
    private ArrayList<PodcastDetailHomeEnt> podcastRecommendedCollections;
    private ArrayList<SubscribePodcastEnt> podcastSubscribeCollections;
    private ArrayList<PodcastDetailHomeEnt> podcastDefaultCollections;
    private RecyclerViewItemListener subscriptionItemLister = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            SubscribePodcastEnt ent = (SubscribePodcastEnt) Ent;
            serviceHelper.enqueueCall(webService.unSubscribePodcast(ent.getTrackId(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_PODCAST);
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            SubscribePodcastEnt ent = (SubscribePodcastEnt) Ent;
            openPodcastDetail(ent.getTrackId());

        }
    };
    private RecyclerViewItemListener recommendedItemLister = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            UIHelper.showShortToastInCenter(getDockActivity(), "recommend");

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            PodcastDetailHomeEnt ent = (PodcastDetailHomeEnt) Ent;
            openPodcastDetail(ent.getTrackId());

        }
    };
    private RecyclerViewItemListener defaultCategoryItemLister = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            PodcastDetailHomeEnt ent = (PodcastDetailHomeEnt) Ent;
            if (!ent.isSubscribed()) {
                serviceHelper.enqueueCall(webService.subscribePodcast(ent.getTrackId(), prefHelper.getUserToken()), WebServiceConstants.SUBSCRIBE_PODCAST);
                podcastDefaultCollections.get(position).setSubscribed(true);
                rvPodcastDefault.notifyItemChanged(position);
            } else {
                serviceHelper.enqueueCall(webService.unSubscribePodcast(ent.getTrackId(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_PODCAST);
                podcastDefaultCollections.get(position).setSubscribed(false);
                rvPodcastDefault.notifyItemChanged(position);
            }
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            PodcastDetailHomeEnt ent = (PodcastDetailHomeEnt) Ent;
            openPodcastDetail(ent.getTrackId());

        }
    };
    private Integer currentPageNumber = 1;
    private Integer totalCount = 5;
    private boolean canCallForMore = true;
    private boolean isOnCall = false;
    private ProgressDialog progressDialog;
    private boolean isFirstTime = false;

    public static PodcastFragmentNew newInstance() {
        Bundle args = new Bundle();

        PodcastFragmentNew fragment = new PodcastFragmentNew();
        fragment.setArguments(args);
        return fragment;
    }

    private void openPodcastDetail(Integer trackid) {
        getDockActivity().replaceDockableFragment(PodcastDetailFragment.newInstance(trackid), "PodcastDetailFragment");
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

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_SUBSCRIBE_PODCASTS:
                bindSubscriptionList((ArrayList<SubscribePodcastEnt>) result);
                // getDockActivity().onLoadingStarted();
                break;
            case WebServiceConstants.GET_DEFAULT_PODCASTS:
                getDockActivity().onLoadingFinished();
                bindPodcastList((PodcastHomeEnt) result);
                break;
            case WebServiceConstants.GET_PAGED_PODCAST:
                isOnCall = false;
                bindPagedPodcastList((PodcastHomeEnt) result);
                break;
            case WebServiceConstants.SUBSCRIBE_PODCAST:
                serviceHelper.enqueueCall(webService.getSubscribePodcasts(prefHelper.getUserToken()), WebServiceConstants.GET_SUBSCRIBE_PODCASTS);
                break;
            case WebServiceConstants.UNSUBSCRIBE_PODCAST:
                serviceHelper.enqueueCall(webService.getSubscribePodcasts(prefHelper.getUserToken()), WebServiceConstants.GET_SUBSCRIBE_PODCASTS);
                break;
        }
    }

    @Override
    public void ResponseFailure(String tag) {
        hideProgressDialog();
    }

    public void setTitleBar(TitleBar titleBar) {
        getMainActivity().settingFilterMenu();
        if (getMainActivity().filterFragment != null) {
            getMainActivity().filterFragment.setListener(this);
        }
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.podcast));
        titleBar.showBackButton();
        titleBar.showFilterButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().isNavigationGravityRight = true;
                getMainActivity().getDrawerLayout().openDrawer(Gravity.RIGHT);
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void bindPagedPodcastList(PodcastHomeEnt result) {
        if (result.getDefaultCategories().size() <= 0 && result.getFeaturedCategories().size() <= 0) {
            canCallForMore = false;
        } else {
            ArrayList<PodcastCategoryHomeEnt> featureList = new ArrayList<>();
            featureList.addAll(result.getFeaturedCategories());
            ArrayList<PodcastCategoryHomeEnt> defaultList = new ArrayList<>();
            defaultList.addAll(result.getDefaultCategories());
            int recommenedCount = 0;
            int defaultCount = 0;
            for (PodcastCategoryHomeEnt ent : featureList) {
                podcastRecommendedCollections.addAll(ent.getPodcastdetails());
                recommenedCount = recommenedCount + ent.getPodcastdetails().size();

            }
            rvRecommended.notifyItemRangeChanged(recommendedLayoutManager.findLastVisibleItemPosition(), recommenedCount);
            for (PodcastCategoryHomeEnt ent : defaultList) {
                podcastDefaultCollections.addAll(ent.getPodcastdetails());
                defaultCount = defaultCount + ent.getPodcastdetails().size();
            }
            rvPodcastDefault.notifyItemRangeChanged(defaultLayoutManager.findLastVisibleItemPosition(), defaultCount);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast_new, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleBar(((HomeTabFragment) getParentFragment()).getTitleBar());
        options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        if (isFirstTime) {
            initProgressDialog();
            getFirstTimeData();
        }
//        getFirstTimeData();
    }


    private void clearFragmentResources() {
        currentPageNumber = 1;
        if (rvRecommended != null && rvPodcastDefault != null && rvSubscribe != null) {
            rvRecommended.clearList();
            rvPodcastDefault.clearList();
            rvSubscribe.clearList();
        }
        if (getMainActivity() != null && getMainActivity().filterFragment != null) {
            getMainActivity().filterFragment.clearFilters();
        }
        canCallForMore = true;
        isOnCall = false;
        categoriesIds = "";
        hideProgressDialog();
    }

    private void getFirstTimeData() {
        if (prefHelper == null) {
            isFirstTime = true;
        } else {
            showProgressDialog();
            serviceHelper.enqueueCall(webService.getSubscribePodcasts(prefHelper.getUserToken()), WebServiceConstants.GET_SUBSCRIBE_PODCASTS, false);
            // bindPodcastList();
            serviceHelper.enqueueCall(webService.getDefaultPodcast(currentPageNumber, totalCount, categoriesIds, prefHelper.getUserToken()),
                    WebServiceConstants.GET_DEFAULT_PODCASTS, false);
            isFirstTime = false;
        }
    }

    private void initProgressDialog() {
        if (getDockActivity() == null) {
            isFirstTime = true;
        } else {
            if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                progressDialog = new ProgressDialog(getDockActivity());
                progressDialog.setMessage(getDockActivity().getResources().getString(R.string.com_facebook_loading));
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                isFirstTime = false;
            }
        }
    }

    private void bindSubscriptionList(ArrayList<SubscribePodcastEnt> result) {

        rvSubscribe.setNestedScrollingEnabled(false);
        podcastSubscribeCollections = new ArrayList<>(3);
        for (int i = 0; i < result.size(); i++) {
            if (i == 2) {
                break;
            }
            podcastSubscribeCollections.add(result.get(i));
        }
        rvSubscribe.BindRecyclerView(new PodcastSubscriptionBinder(options, subscriptionItemLister), podcastSubscribeCollections,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false),
                new DefaultItemAnimator());
        if (podcastSubscribeCollections.size() <= 0) {
            txtSubscriptionNoData.setVisibility(View.VISIBLE);
            rvSubscribe.setVisibility(View.GONE);
        } else {
            txtSubscriptionNoData.setVisibility(View.GONE);
            rvSubscribe.setVisibility(View.VISIBLE);
        }
    }

    private void bindPodcastList(PodcastHomeEnt podcastList) {
        rvRecommended.setNestedScrollingEnabled(false);
        rvPodcastDefault.setNestedScrollingEnabled(false);
        hideProgressDialog();
        podcastDefaultCollections = new ArrayList<>();
        podcastRecommendedCollections = new ArrayList<>();
        currentPageNumber = 1;
        ArrayList<PodcastCategoryHomeEnt> featureList = new ArrayList<>();
        featureList.addAll(podcastList.getFeaturedCategories());
        ArrayList<PodcastCategoryHomeEnt> defaultList = new ArrayList<>();
        defaultList.addAll(podcastList.getDefaultCategories());
        for (PodcastCategoryHomeEnt ent : featureList) {
            podcastRecommendedCollections.addAll(ent.getPodcastdetails());
        }
        for (PodcastCategoryHomeEnt ent : defaultList) {
            podcastDefaultCollections.addAll(ent.getPodcastdetails());
        }
        bindRecommendedPodcastView();
        bindDefaultPodcastView();
    }

    private void bindDefaultPodcastView() {
        defaultLayoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        rvPodcastDefault.BindRecyclerView(new PodcastDefaultCategoryBinder(options, defaultCategoryItemLister), podcastDefaultCollections,
                defaultLayoutManager, new DefaultItemAnimator());
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() - 500)) &&
                            scrollY > oldScrollY) {
                        if (podcastDefaultCollections.size() > 0)
                            getPagedPodcast();
                        //code to fetch more data for endless scrolling
                    }
                }
            }
        });
        if (podcastDefaultCollections.size() <= 0) {
            txtDefaultNoData.setVisibility(View.VISIBLE);
            rvPodcastDefault.setVisibility(View.GONE);
        } else {
            txtDefaultNoData.setVisibility(View.GONE);
            rvPodcastDefault.setVisibility(View.VISIBLE);
        }
    }

    private void bindRecommendedPodcastView() {
        recommendedLayoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvRecommended.BindRecyclerView(new PodcastRecommendedBinder(options, recommendedItemLister), podcastRecommendedCollections,
                recommendedLayoutManager,
                new DefaultItemAnimator());
        // rvRecommended.addOnScrollListener(scrollListener);
        rvRecommended.getAdapter().setOnLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMoreItem(int position) {
                getPagedPodcast();
            }
        });
        if (podcastRecommendedCollections.size() <= 0) {
            txtRecommendedNoData.setVisibility(View.VISIBLE);
            rvRecommended.setVisibility(View.GONE);
        } else {
            txtRecommendedNoData.setVisibility(View.GONE);
            rvRecommended.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void openSubscriptionFragment(String titleHeading) {
        SubscriptionsFragment fragment = new SubscriptionsFragment();
        fragment.setTitleHeading(titleHeading);
        getDockActivity().replaceDockableFragment(fragment, "SubscriptionsFragment");
    }

    @OnClick(R.id.btn_subscription_seeall)
    public void onViewClicked() {
        openSubscriptionFragment(getString(R.string.my_subscriptions));
    }

    private void getPagedPodcast() {
        if (canCallForMore) {
            if (!isOnCall) {
                currentPageNumber = currentPageNumber + 1;
                //  progressBar.setVisibility(View.VISIBLE);
                isOnCall = true;
                serviceHelper.enqueueCall(webService.getDefaultPodcast(currentPageNumber, totalCount, categoriesIds, prefHelper.getUserToken()), WebServiceConstants.GET_PAGED_PODCAST);
            }
        }
    }

    @Override
    public void onPauseFragment() {
        clearFragmentResources();
    }

    @Override
    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper) {
        initProgressDialog();
        getFirstTimeData();
    }

    @Override
    public void onDoneFiltering(String filterIDs) {
        categoriesIds = filterIDs;
        serviceHelper.enqueueCall(webService.getDefaultPodcast(currentPageNumber, totalCount, categoriesIds, prefHelper.getUserToken()),
                WebServiceConstants.GET_DEFAULT_PODCASTS);
    }

    class PageScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
//                int lastDefaultVisibleItem = defaultLayoutManager.findLastVisibleItemPosition();&& (lastDefaultVisibleItem == rvPodcastDefault.getAdapter().getItemCount() - 1)
                int lastRecommendedItem = recommendedLayoutManager.findLastVisibleItemPosition();
                if ((lastRecommendedItem == rvRecommended.getAdapter().getItemCount() - 1))
                    getPagedPodcast();
            }
        }
    }
}