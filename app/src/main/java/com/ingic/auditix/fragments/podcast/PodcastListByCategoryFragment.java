package com.ingic.auditix.fragments.podcast;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastDetailHomeEnt;
import com.ingic.auditix.entities.PodcastCategoryListEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.FilterDoneClickListener;
import com.ingic.auditix.interfaces.LoadMoreListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.podcast.PodcastDefaultCategoryBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 4/11/18.
 */
public class PodcastListByCategoryFragment extends BaseFragment  {
    public static final String TAG = "PodcastListByCategoryFragment";
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.rv_podcast)
    CustomRecyclerView rvPodcast;
    Unbinder unbinder;
    private String categoriesIds = "US";
    private int mCategoryID;
    private int totalCount = 10;
    private int currentOffset = 1;
    private LinearLayoutManager defaultLayoutManager;
    private ArrayList<PodcastDetailHomeEnt> podcastDefaultCollections;
    private boolean canCallForMore = true;
    private boolean isOnCall = false;
    private RecyclerViewItemListener defaultCategoryItemLister = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            PodcastDetailHomeEnt ent = (PodcastDetailHomeEnt) Ent;
            if (!ent.getSubscribed()) {
                serviceHelper.enqueueCall(webService.subscribePodcast(ent.getTrackId(),ent.getCategoryId(), prefHelper.getUserToken()), WebServiceConstants.SUBSCRIBE_PODCAST);
                podcastDefaultCollections.get(position).setSubscribed(true);
                rvPodcast.notifyItemChanged(position);
            } else {
                serviceHelper.enqueueCall(webService.unSubscribePodcast(ent.getTrackId(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_PODCAST);
                podcastDefaultCollections.get(position).setSubscribed(false);
                rvPodcast.notifyItemChanged(position);
            }
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            PodcastDetailHomeEnt ent = (PodcastDetailHomeEnt) Ent;
            openPodcastDetail(ent);
        }
    };

    public static PodcastListByCategoryFragment newInstance(int categorID) {
        Bundle args = new Bundle();

        PodcastListByCategoryFragment fragment = new PodcastListByCategoryFragment();
        fragment.setArguments(args);
        fragment.setmCategoryID(categorID);
        return fragment;
    }

    private void openPodcastDetail(PodcastDetailHomeEnt ent) {
        getDockActivity().replaceDockableFragment(PodcastDetailFragment.newInstance(ent), "PodcastDetailFragment");
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
            case WebServiceConstants.GET_ALL_PODCAST_BY_CATEGORIES:
                bindDefaultPodcastView(((PodcastCategoryListEnt) result).getResults());
                break;
            case WebServiceConstants.GET_PAGED_PODCAST:
                isOnCall = false;
                bindPagedPodcastList(((PodcastCategoryListEnt) result).getResults());
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);

        titleBar.hideButtons();
        titleBar.addBackground();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.podcast));
        titleBar.showBackButton();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast_by_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentOffset = 1;
        serviceHelper.enqueueCall(webService.getPodcastsByCategory(mCategoryID, totalCount, categoriesIds, currentOffset, prefHelper.getUserToken()),
                WebServiceConstants.GET_ALL_PODCAST_BY_CATEGORIES);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getPagedPodcast() {
        if (canCallForMore) {
            if (!isOnCall) {
                currentOffset = currentOffset + totalCount + 1;
                //  progressBar.setVisibility(View.VISIBLE);
                isOnCall = true;
                serviceHelper.enqueueCall(webService.getPodcastsByCategory(mCategoryID, totalCount, categoriesIds, currentOffset, prefHelper.getUserToken()),
                        WebServiceConstants.GET_PAGED_PODCAST);
            }
        }
    }

    private void bindPagedPodcastList(ArrayList<PodcastDetailHomeEnt> result) {
        if (result.size() <= 0) {
            canCallForMore = false;
        } else {
            ArrayList<PodcastDetailHomeEnt> defaultList = new ArrayList<>(result);
            totalCount = result.size();
            podcastDefaultCollections.addAll(defaultList);
            int defaultCount = 0;
            defaultCount = defaultCount + podcastDefaultCollections.size();
            rvPodcast.notifyItemRangeChanged(defaultLayoutManager.findLastVisibleItemPosition(), defaultCount);
        }
    }

    private void bindDefaultPodcastView(ArrayList<PodcastDetailHomeEnt> result) {
        podcastDefaultCollections = result;
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        defaultLayoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        rvPodcast.BindRecyclerView(new PodcastDefaultCategoryBinder(options, defaultCategoryItemLister, prefHelper), podcastDefaultCollections,
                defaultLayoutManager, new DefaultItemAnimator());
        rvPodcast.getAdapter().setOnLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMoreItem(int position) {
                getPagedPodcast();
            }
        });
        if (podcastDefaultCollections.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvPodcast.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvPodcast.setVisibility(View.VISIBLE);
        }
    }

    /*@Override
    public void onDoneFiltering(String filterIDs) {
        if (filterIDs.equalsIgnoreCase("")) {
            categoriesIds = "US";
        } else
            categoriesIds = filterIDs;
        serviceHelper.enqueueCall(webService.getPodcastsByCategory(mCategoryID, totalCount, categoriesIds, currentOffset, prefHelper.getUserToken()),
                WebServiceConstants.GET_ALL_PODCAST_BY_CATEGORIES);
    }*/

    public void setmCategoryID(int mCategoryID) {
        this.mCategoryID = mCategoryID;
    }
}