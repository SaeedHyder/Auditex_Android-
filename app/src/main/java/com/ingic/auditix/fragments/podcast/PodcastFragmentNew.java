package com.ingic.auditix.fragments.podcast;

import android.app.ProgressDialog;
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
import com.ingic.auditix.entities.PodcastCategoriesEnt;
import com.ingic.auditix.entities.PodcastCategoryHomeEnt;
import com.ingic.auditix.entities.PodcastDetailHomeEnt;
import com.ingic.auditix.entities.PodcastEpisodeEnt;
import com.ingic.auditix.entities.PodcastHomeEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.standard.HomeTabFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.helpers.InternetHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.LoadMoreListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.binders.podcast.PodcastCategoryBinder;
import com.ingic.auditix.ui.binders.podcast.PodcastNewNoteworthyBinder;
import com.ingic.auditix.ui.binders.podcast.PodcastRecommendedBinder;
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
 * Created on 1/10/2018.
 */
public class PodcastFragmentNew extends BaseFragment implements ViewPagerFragmentLifecycleListener {

    //region Global Variables
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
    @BindView(R.id.MainContainer)
    LinearLayout MainContainer;
    @BindView(R.id.containerFragment)
    FrameLayout containerFragment;
    private boolean isFilterVisible = false;
    private EnableFilterDataEnt filterDataEnt;
    private LinearLayoutManager defaultLayoutManager;
    private LinearLayoutManager recommendedLayoutManager;
    private ArrayList<PodcastDetailHomeEnt> podcastRecommendedCollections;
    private ArrayList<PodcastEpisodeEnt> podcastEpisodesCollections;

    private RecyclerViewItemListener newAndNoteworthyitemlistener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            PodcastEpisodeEnt ent = (PodcastEpisodeEnt) Ent;
            if (ent.getPodcast().getSubscribed()) {
                serviceHelper.enqueueCall(webService.unSubscribePodcast(ent.getPodcast().getTrackId(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_PODCAST);
            } else {
                serviceHelper.enqueueCall(webService.subscribePodcast(ent.getPodcast().getTrackId(), ent.getPodcast().getCategoryId(), prefHelper.getUserToken()), WebServiceConstants.SUBSCRIBE_PODCAST);
            }
            ent.getPodcast().setSubscribed(!ent.getPodcast().getSubscribed());
            rvSubscribe.notifyItemChanged(position);
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            PodcastEpisodeEnt ent = (PodcastEpisodeEnt) Ent;
            openPodcastEpisodeDetail(ent);

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
            openPodcastDetail(ent);

        }
    };
    private RecyclerViewItemListener categoryListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            getDockActivity().replaceDockableFragment(PodcastListByCategoryFragment.newInstance(((PodcastCategoriesEnt) Ent).getCategoryId()), PodcastListByCategoryFragment.TAG);
        }
    };

    private Integer currentPageNumber = 1;
    private Integer totalCount = 10;
    private boolean canCallForMore = true;
    private boolean isOnCall = false;
    private ProgressDialog progressDialog;
    private boolean isFirstTime = false;


    //region Lifecycle Methods
    public static PodcastFragmentNew newInstance() {
        Bundle args = new Bundle();

        PodcastFragmentNew fragment = new PodcastFragmentNew();
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

    //endregion
    //region Service Response Helpers
    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_NEW_NOTEWORTHY_PODCASTS_EPISODES:
                bindEpisodesNewAndNoteworthy((ArrayList<PodcastEpisodeEnt>) result);
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
         /*   case WebServiceConstants.SUBSCRIBE_PODCAST:
                serviceHelper.enqueueCall(webService.getSubscribePodcasts(prefHelper.getUserToken()), WebServiceConstants.GET_SUBSCRIBE_PODCASTS);
                break;
            case WebServiceConstants.UNSUBSCRIBE_PODCAST:
                serviceHelper.enqueueCall(webService.getSubscribePodcasts(prefHelper.getUserToken()), WebServiceConstants.GET_SUBSCRIBE_PODCASTS);
                break;*/
            case WebServiceConstants.GET_ALL_PODCAST_CATEGORIES:
                bindDefaultPodcastView((ArrayList<PodcastCategoriesEnt>) result);
                break;
        }
    }

    @Override
    public void ResponseFailure(String tag) {
        hideProgressDialog();
    }

    public void setTitleBar(TitleBar titleBar) {
        if (getMainActivity().filterFragment != null) {
            getMainActivity().setRightSideFragment(getMainActivity().filterFragment);
            getMainActivity().filterFragment.setListener((filters, isClear) -> {
                if (isClear) {
                    filterDataEnt = null;
                    hideFilterList();
                } else {
                    filterDataEnt = filters;
                    showFilterList();
                }
            });
        }
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.podcast));
        titleBar.showMenuButton();
        titleBar.showFilterButton(v -> {
            getMainActivity().isNavigationGravityRight = true;
            getMainActivity().getDrawerLayout().openDrawer(Gravity.RIGHT);
        });
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
        options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        initProgressDialog();
        getFirstTimeData();

//        getFirstTimeData();
    }

    private void getFirstTimeData() {
        if (prefHelper == null) {
            isFirstTime = true;
        } else {
            //categoriesIds = getMainActivity().filterFragment.getFiltersList();
            showProgressDialog();
            serviceHelper.enqueueCall(webService.getNewAndNoteworthy(prefHelper.getUserToken()), WebServiceConstants.GET_NEW_NOTEWORTHY_PODCASTS_EPISODES, false);
            // bindPodcastList();
            serviceHelper.enqueueCall(webService.getDefaultPodcast(currentPageNumber, totalCount, categoriesIds, prefHelper.getUserToken()),
                    WebServiceConstants.GET_DEFAULT_PODCASTS, false);
            serviceHelper.enqueueCall(webService.getAllPodcastCategories(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_PODCAST_CATEGORIES, false);
            isFirstTime = false;
            if (isFilterVisible) {
                showFilterList();
            } else {
                hideFilterList();
            }
        }
    }
    private void showFilterList() {
        MainContainer.setVisibility(View.GONE);
        containerFragment.setVisibility(View.VISIBLE);
        replaceFragment(PodcastFilterListFragment.newInstance(filterDataEnt));
        isFilterVisible = true;
    }

    private void hideFilterList() {
        if (getChildFragmentManager().findFragmentById(R.id.containerFragment) != null) {
            getChildFragmentManager().beginTransaction().
                    remove(getChildFragmentManager().findFragmentById(R.id.containerFragment)).commit();
            getChildFragmentManager().popBackStack();
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
    private void clearFragmentResources() {
        currentPageNumber = 1;
        if (rvRecommended != null && rvPodcastDefault != null && rvSubscribe != null) {
            rvRecommended.clearList();
            rvPodcastDefault.clearList();
            rvSubscribe.clearList();
        }
        canCallForMore = true;
        isOnCall = false;
        categoriesIds = "";
        hideProgressDialog();
    }
    //endregion

    @Override
    public void onPauseFragment() {
        clearFragmentResources();
    }

    @Override
    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper) {
        //initProgressDialog();
        //getFirstTimeData();
    }

    //endregion

    //region Progress Dialog
    private void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
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

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    //endregion

    //region Data Bindings
    private void bindPagedPodcastList(PodcastHomeEnt result) {
        if (result.getFeaturedCategories().size() <= 0) {
            canCallForMore = false;
        } else {
            ArrayList<PodcastCategoryHomeEnt> featureList = new ArrayList<>(result.getFeaturedCategories());
            ArrayList<PodcastCategoryHomeEnt> defaultList = new ArrayList<>(result.getDefaultCategories());
            int recommenedCount = 0;
            for (PodcastCategoryHomeEnt ent : featureList) {
                for (PodcastDetailHomeEnt homeEnt : ent.getPodcastdetails()
                        ) {
                    if (!podcastRecommendedCollections.contains(homeEnt)) {
                        podcastRecommendedCollections.add(homeEnt);
                        recommenedCount = recommenedCount + 1;
                    }
                }

                //   recommenedCount = recommenedCount + ent.getPodcastdetails().size();

            }
            rvRecommended.notifyItemRangeChanged(recommendedLayoutManager.findLastVisibleItemPosition(), recommenedCount);
        }
    }


    private void bindEpisodesNewAndNoteworthy(ArrayList<PodcastEpisodeEnt> result) {

        rvSubscribe.setNestedScrollingEnabled(false);
        podcastEpisodesCollections = new ArrayList<>(3);
        for (int i = 0; i < result.size(); i++) {
            if (i == 4) {
                break;
            }
            podcastEpisodesCollections.add(result.get(i));
        }
        rvSubscribe.BindRecyclerView(new PodcastNewNoteworthyBinder(options, newAndNoteworthyitemlistener, prefHelper), podcastEpisodesCollections,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false),
                new DefaultItemAnimator());
        if (podcastEpisodesCollections.size() <= 0) {
            txtSubscriptionNoData.setVisibility(View.VISIBLE);
            rvSubscribe.setVisibility(View.GONE);
            btnSubscriptionSeeall.setVisibility(View.INVISIBLE);
        } else {
            txtSubscriptionNoData.setVisibility(View.GONE);
            btnSubscriptionSeeall.setVisibility(View.VISIBLE);
            rvSubscribe.setVisibility(View.VISIBLE);
        }
    }

    private void bindPodcastList(PodcastHomeEnt podcastList) {
        rvRecommended.setNestedScrollingEnabled(false);
        rvPodcastDefault.setNestedScrollingEnabled(false);
        hideProgressDialog();
        podcastRecommendedCollections = new ArrayList<>();
        currentPageNumber = 1;
        ArrayList<PodcastCategoryHomeEnt> featureList = new ArrayList<>();
        featureList.addAll(podcastList.getFeaturedCategories());
        ArrayList<PodcastCategoryHomeEnt> defaultList = new ArrayList<>();
        defaultList.addAll(podcastList.getDefaultCategories());
        for (PodcastCategoryHomeEnt ent : featureList) {
            podcastRecommendedCollections.addAll(ent.getPodcastdetails());
        }
        bindRecommendedPodcastView();
        // bindDefaultPodcastView();
    }

    private void bindDefaultPodcastView(ArrayList<PodcastCategoriesEnt> results) {
        defaultLayoutManager = new GridLayoutManager(getDockActivity(), 3, GridLayoutManager.VERTICAL, false);
        rvPodcastDefault.BindRecyclerView(new PodcastCategoryBinder(options, categoryListener), results,
                defaultLayoutManager, new DefaultItemAnimator());
        rvPodcastDefault.setHasFixedSize(true);
        rvPodcastDefault.addItemDecoration(new GridSpacingItemDecoration(3, Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)), true));
        if (results.size() <= 0) {
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

    //endregion

    //region Action Events
    private void openPodcastEpisodeDetail(PodcastEpisodeEnt ent) {
        getDockActivity().replaceDockableFragment(PodcastEpisodeDetailFragment.newInstance(ent), PodcastEpisodeDetailFragment.TAG);
    }

    @OnClick(R.id.btn_subscription_seeall)
    public void onViewClicked() {
        getDockActivity().replaceDockableFragment(NewAndNoteworthyListingFragment.newInstance(podcastEpisodesCollections), NewAndNoteworthyListingFragment.TAG);
    }

    public void openPodcastDetail(PodcastDetailHomeEnt ent) {
        getDockActivity().replaceDockableFragment(PodcastDetailFragment.newInstance(ent), "PodcastDetailFragment");
    }



   /* @Override
    public void onDoneFiltering(String filterIDs) {
        categoriesIds = filterIDs;
        serviceHelper.enqueueCall(webService.getDefaultPodcast(currentPageNumber, totalCount, categoriesIds, prefHelper.getUserToken()),
                WebServiceConstants.GET_DEFAULT_PODCASTS);
    }*/
    //endregion

}
