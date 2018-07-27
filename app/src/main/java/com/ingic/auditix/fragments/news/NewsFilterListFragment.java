package com.ingic.auditix.fragments.news;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.EnableFilterDataEnt;
import com.ingic.auditix.entities.NewItemDetailEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.news.NewsCategoryListingBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 7/20/18.
 */
public class NewsFilterListFragment extends BaseFragment {

    public static final String TAG = "NewsFilterListFragment";
    @BindView(R.id.txt_subscription_no_data)
    AnyTextView txtSubscriptionNoData;
    @BindView(R.id.rvListing)
    CustomRecyclerView rvListing;
    Unbinder unbinder;
    private EnableFilterDataEnt filters;
    private int totalCount = 20;
    private int currentPageNumber = 1;
    private boolean canCallForMore = true;
    private boolean isOnCall = false;
    private RecyclerViewItemListener itemClickListner = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            NewItemDetailEnt ent = (NewItemDetailEnt) Ent;
            if (ent.getSubscribed()) {
                serviceHelper.enqueueCall(webService.unsubscribeNews(ent.getNewsID(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_NEWS);
            } else {
                serviceHelper.enqueueCall(webService.subscribeNews(ent.getNewsID(), prefHelper.getUserToken()), WebServiceConstants.SUBSCRIBE_NEWS);
            }
            ent.setSubscribed(!ent.getSubscribed());
            rvListing.notifyItemChanged(position);
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            if (getParentFragment() != null) {
                ((NewsFragment)getParentFragment()).openChannelDetail((NewItemDetailEnt) Ent);
            }
            //replaceFromParentFragment(NewsChannelDetailFragment.newInstance(), NewsCategoryDetailFragment.TAG);

        }
    };
    private ArrayList<NewItemDetailEnt> newsCollection;
    private LinearLayoutManager defaultLayoutManager;

    public static NewsFilterListFragment newInstance(EnableFilterDataEnt filters) {
        Bundle args = new Bundle();

        NewsFilterListFragment fragment = new NewsFilterListFragment();
        fragment.setArguments(args);
        fragment.setFilters(filters);
        return fragment;
    }
    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.addBackground();
    }
    public void setFilters(EnableFilterDataEnt filters) {
        this.filters = filters;
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
            case WebServiceConstants.GET_FILTER_DATA:
                bindData((ArrayList<NewItemDetailEnt>) result);
                break;
            case WebServiceConstants.GET_PAGED_DATA:
                isOnCall = false;
                bindPagedPodcastList((ArrayList<NewItemDetailEnt>) result);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_see_all, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // this.filters = getMainActivity().newsFilterFragment.getUserEnableFilters();
        getFilterData(WebServiceConstants.GET_FILTER_DATA);

    }

    private void getFilterData(String TAG) {
        serviceHelper.enqueueCall(webService.getNewsByfilter(filters.getMinDuration(),
                filters.getMaxDuration(),
                filters.getMinSubscriber(),
                filters.getMaxSubscriber(), 3,
                currentPageNumber,
                totalCount,
                filters.getCountryIDS(),
                prefHelper.getUserToken()), TAG);

    }

    private void getPagedPodcast() {
        if (canCallForMore) {
            if (!isOnCall) {
                currentPageNumber = currentPageNumber + 1;
                //  progressBar.setVisibility(View.VISIBLE);
                isOnCall = true;
                getFilterData(WebServiceConstants.GET_PAGED_DATA);
            }
        }
    }

    private void bindPagedPodcastList(ArrayList<NewItemDetailEnt> result) {
        if (result.size() <= 0) {
            canCallForMore = false;
        } else if (newsCollection != null && rvListing != null && defaultLayoutManager != null) {
            newsCollection.addAll(result);
            int defaultCount = 0;
            defaultCount = defaultCount + newsCollection.size();
            rvListing.notifyItemRangeChanged(defaultLayoutManager.findLastVisibleItemPosition(), defaultCount);
        }
    }

    private void bindData(ArrayList<NewItemDetailEnt> result) {
        defaultLayoutManager = new LinearLayoutManager(getDockActivity());
        newsCollection = new ArrayList<>(result);
        txtSubscriptionNoData.setVisibility(result.size() <= 0 ? View.VISIBLE : View.GONE);
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        rvListing.BindRecyclerView(new NewsCategoryListingBinder(options, itemClickListner, prefHelper), newsCollection, defaultLayoutManager, new DefaultItemAnimator());
        rvListing.setNestedScrollingEnabled(false);
        //rvListing.getAdapter().setOnLoadMoreListener(position -> getPagedPodcast());
    }
}