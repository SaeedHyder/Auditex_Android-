package com.ingic.auditix.fragments.podcast;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.EnableFilterDataEnt;
import com.ingic.auditix.entities.PodcastDetailHomeEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.books.BooksFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.podcast.PodcastDefaultCategoryBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 7/23/18.
 */
public class PodcastFilterListFragment extends BaseFragment {
    public static final String TAG = "PodcastFilterListFragment";


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
            PodcastDetailHomeEnt ent = (PodcastDetailHomeEnt) Ent;
            if (!ent.getSubscribed()) {
                serviceHelper.enqueueCall(webService.subscribePodcast(ent.getTrackId(),ent.getCategoryId(), prefHelper.getUserToken()), WebServiceConstants.SUBSCRIBE_PODCAST);
                podcastDefaultCollections.get(position).setSubscribed(true);
                rvListing.notifyItemChanged(position);
            } else {
                serviceHelper.enqueueCall(webService.unSubscribePodcast(ent.getTrackId(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_PODCAST);
                podcastDefaultCollections.get(position).setSubscribed(false);
                rvListing.notifyItemChanged(position);
            }
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            PodcastDetailHomeEnt ent = (PodcastDetailHomeEnt) Ent;
            assert getParentFragment() != null;
            ((PodcastFragmentNew)getParentFragment()).openPodcastDetail((PodcastDetailHomeEnt) Ent);
        }
    };
    private ArrayList<PodcastDetailHomeEnt> podcastDefaultCollections;
    private LinearLayoutManager defaultLayoutManager;

    public static PodcastFilterListFragment newInstance(EnableFilterDataEnt filters) {
        Bundle args = new Bundle();

        PodcastFilterListFragment fragment = new PodcastFilterListFragment();
        fragment.setArguments(args);
        fragment.setFilters(filters);
        return fragment;
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
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.addBackground();
    }
    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_FILTER_DATA:
                bindData((ArrayList<PodcastDetailHomeEnt>) result);
                break;
            case WebServiceConstants.GET_PAGED_DATA:
                isOnCall = false;
                bindPagedPodcastList((ArrayList<PodcastDetailHomeEnt>) result);
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
        getFilterData(WebServiceConstants.GET_FILTER_DATA);

    }

    private void getFilterData(String TAG) {
        serviceHelper.enqueueCall(webService.getPodcastsByfilter(filters.getMinDuration(),
                filters.getMaxDuration(),
                filters.getMinSubscriber(),
                filters.getMaxSubscriber(), 1,
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

    private void bindPagedPodcastList(ArrayList<PodcastDetailHomeEnt> result) {
        if (result.size() <= 0) {
            canCallForMore = false;
        } else if (podcastDefaultCollections != null && rvListing != null && defaultLayoutManager != null) {
            podcastDefaultCollections.addAll(result);
            int defaultCount = 0;
            defaultCount = defaultCount + podcastDefaultCollections.size();
            rvListing.notifyItemRangeChanged(defaultLayoutManager.findLastVisibleItemPosition(), defaultCount);
        }
    }

    private void bindData(ArrayList<PodcastDetailHomeEnt> result) {
        defaultLayoutManager = new LinearLayoutManager(getDockActivity());
        podcastDefaultCollections = new ArrayList<>(result);
        txtSubscriptionNoData.setVisibility(result.size() <= 0 ? View.VISIBLE : View.GONE);
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        rvListing.BindRecyclerView(new PodcastDefaultCategoryBinder(options, itemClickListner,prefHelper), podcastDefaultCollections, defaultLayoutManager, new DefaultItemAnimator());
        rvListing.setNestedScrollingEnabled(false);
        //rvListing.getAdapter().setOnLoadMoreListener(position -> getPagedPodcast());
    }
}