package com.ingic.auditix.fragments.profile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.CartEnt;
import com.ingic.auditix.entities.PodcastEpisodeEnt;
import com.ingic.auditix.entities.SubscribePodcastEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.podcast.PodcastDetailFragment;
import com.ingic.auditix.fragments.podcast.PodcastEpisodeDetailFragment;
import com.ingic.auditix.fragments.podcast.SubscriptionsFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.FileHelper;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.podcast.PodcastDownloadBinder;
import com.ingic.auditix.ui.binders.podcast.PodcastSubscriptionBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 1/6/2018.
 */
public class ProfilePodcastFragment extends BaseFragment {
    public static final String TAG = "ProfilePodcastFragment";
    @BindView(R.id.txt_subscription)
    AnyTextView txtSubscription;
    @BindView(R.id.btn_subscription_seeall)
    AnyTextView btnSubscriptionSeeall;
    @BindView(R.id.si1_image)
    ImageView si1Image;
    @BindView(R.id.si1_text)
    AnyTextView si1Text;
    @BindView(R.id.si1_btn)
    Button si1Btn;
    @BindView(R.id.si2_image)
    ImageView si2Image;
    @BindView(R.id.si2text)
    AnyTextView si2text;
    @BindView(R.id.si2_btn)
    Button si2Btn;
    @BindView(R.id.txt_downloads)
    AnyTextView txtDownloads;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    /*@BindView(R.id.viewpager)
    FrameLayout viewpager;*/
    Unbinder unbinder;
    @BindView(R.id.lv_downloads)
    CustomRecyclerView rvDownloads;
    @BindView(R.id.rv_subscribe)
    CustomRecyclerView rvSubscribe;
    @BindView(R.id.txt_subscription_no_data)
    AnyTextView txtSubscriptionNoData;
    @BindView(R.id.txt_download_no_data)
    AnyTextView txtDownloadNoData;
    private ArrayList<CartEnt> todayDownloadsCollections;
    private ArrayList<CartEnt> monthDownloadsCollections;
    private ArrayList<CartEnt> olderDownloadsCollections;
    private ArrayList<SubscribePodcastEnt> subscribePodcastcollection;
    //    private TitleViewpagerAdapter adapter;
    private ArrayList<MyDownloadsFragment> fragmentList;
    private int startingWithIndex = 0;
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
    private RecyclerViewItemListener downloadListner = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            PodcastEpisodeEnt ent = (PodcastEpisodeEnt) Ent;
            getMainActivity().onLoadingStarted();
            if (new File(getPodcastDownloadPath(ent.getPodcastId(), ent.getPodcastEpisodeID())).delete()) {
                assert getParentFragment() != null;
                ((ProfileFragment) getParentFragment()).getDownloadDetailEnt().getPodcastEpisode().remove(position);
                rvDownloads.notifyDataSetChanged();

            }
            getMainActivity().onLoadingFinished();


        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            ((ProfileFragment)getParentFragment()). replaceFromParentFragment(PodcastEpisodeDetailFragment.newInstance((PodcastEpisodeEnt) Ent), PodcastEpisodeDetailFragment.TAG);

        }
    };
    private DisplayImageOptions options;

    public static ProfilePodcastFragment newInstance() {
        Bundle args = new Bundle();

        ProfilePodcastFragment fragment = new ProfilePodcastFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String getPodcastDownloadPath(int podcastID, String episodeID) {
        File directory = new File(String.valueOf(AppConstants.DOWNLOAD_PATH + File.separator + AppConstants.TAB_PODCAST + File.separator + podcastID));
        if (!directory.exists()) {
            directory.mkdir();
        }
        return AppConstants.DOWNLOAD_PATH + File.separator + AppConstants.TAB_PODCAST + File.separator + podcastID + File.separator + episodeID;

    }

    private void openPodcastDetail(Integer trackId) {
        //((ProfileFragment)getParentFragment()). replaceFromParentFragment(PodcastDetailFragment.newInstance(trackId), "PodcastDetailFragment");
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
            case WebServiceConstants.GET_SUBSCRIBE_PODCASTS:
                bindSubscriptionList((ArrayList<SubscribePodcastEnt>) result);
                break;
            case WebServiceConstants.UNSUBSCRIBE_PODCAST:
                serviceHelper.enqueueCall(webService.getSubscribePodcasts(prefHelper.getUserToken()), WebServiceConstants.GET_SUBSCRIBE_PODCASTS);
                break;
        }
    }

    private void bindSubscriptionList(ArrayList<SubscribePodcastEnt> result) {
        subscribePodcastcollection = new ArrayList<>(3);
        for (int i = 0; i < result.size(); i++) {
            if (i == 4) {
                break;
            }
            subscribePodcastcollection.add(result.get(i));
        }

        if (rvSubscribe != null) {
            rvSubscribe.BindRecyclerView(new PodcastSubscriptionBinder(options, subscriptionItemLister, prefHelper), subscribePodcastcollection
                    , new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false), new DefaultItemAnimator());
            if (subscribePodcastcollection.size() <= 0) {
                txtSubscriptionNoData.setVisibility(View.VISIBLE);
                rvSubscribe.setVisibility(View.GONE);
                btnSubscriptionSeeall.setVisibility(View.INVISIBLE);
            } else {
                txtSubscriptionNoData.setVisibility(View.GONE);
                rvSubscribe.setVisibility(View.VISIBLE);
                btnSubscriptionSeeall.setVisibility(View.VISIBLE);

            }
        }
    }

    @OnClick(R.id.btn_subscription_seeall)
    public void onViewClicked() {
        openSubscriptionFragment(getString(R.string.my_subscriptions));
    }

    private void openSubscriptionFragment(String titleHeading) {
        SubscriptionsFragment fragment = new SubscriptionsFragment();
        fragment.setTitleHeading(titleHeading);
        ((ProfileFragment)getParentFragment()). replaceFromParentFragment(fragment, "SubscriptionsFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_podcast, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //bindTabs();
        options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        assert getParentFragment() != null;
        bindSingleDownloadList(((ProfileFragment) getParentFragment()).getDownloadDetailEnt().getPodcastEpisode());

    }

    @Override
    public void onStart() {
        super.onStart();
        serviceHelper.enqueueCall(webService.getSubscribePodcasts(prefHelper.getUserToken()), WebServiceConstants.GET_SUBSCRIBE_PODCASTS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void bindSingleDownloadList(ArrayList<PodcastEpisodeEnt> result) {
        if (result.size() > 0) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
            rvDownloads.BindRecyclerView(new PodcastDownloadBinder(downloadListner, options), result, layoutManager, new DefaultItemAnimator());
            rvDownloads.setNestedScrollingEnabled(false);
        } else {
            rvDownloads.setVisibility(View.GONE);
            txtDownloadNoData.setVisibility(View.VISIBLE);

        }

    }


}