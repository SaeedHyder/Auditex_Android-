package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
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
import com.ingic.auditix.entities.SubscribePodcastEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.DownloadBinder;
import com.ingic.auditix.ui.binders.PodcastSubscriptionBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 1/6/2018.
 */
public class ProfilePodcastFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
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

    public static ProfilePodcastFragment newInstance() {
        Bundle args = new Bundle();

        ProfilePodcastFragment fragment = new ProfilePodcastFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void openPodcastDetail(Integer trackId) {
        getDockActivity().replaceDockableFragment(PodcastDetailFragment.newInstance(trackId), "PodcastDetailFragment");
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
            if (i == 2) {
                break;
            }
            subscribePodcastcollection.add(result.get(i));
        }
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        if (rvSubscribe != null) {
            rvSubscribe.BindRecyclerView(new PodcastSubscriptionBinder(options, subscriptionItemLister), subscribePodcastcollection
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
        getDockActivity().replaceDockableFragment(fragment, "SubscriptionsFragment");
    }

    private void bindTabs() {
        bindData();
        fragmentList = new ArrayList<>(3);
        fragmentList.add(new MyDownloadsFragment());
        fragmentList.add(new MyDownloadsFragment());
        fragmentList.add(new MyDownloadsFragment());
        tabLayout.addTab(tabLayout.newTab().setText(R.string.today), true);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.this_month), false);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.older), false);
//        replaceTab(startingWithIndex);
        bindViewWithPosition(startingWithIndex);
        tabLayout.addOnTabSelectedListener(this);

    }

    private void replaceTab(int position) {
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.replace(R.id.viewpager, fragmentList.get(position));
        transaction.commit();


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
        bindTabs();
        rvDownloads.setVisibility(View.GONE);
        txtDownloadNoData.setVisibility(View.VISIBLE);


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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //replaceTab(tab.getPosition());
        replaceList(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void bindData() {
        todayDownloadsCollections = new ArrayList<>();
        monthDownloadsCollections = new ArrayList<>();
        olderDownloadsCollections = new ArrayList<>();
       /* todayDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        todayDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        todayDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        todayDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        todayDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        todayDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        todayDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        monthDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        monthDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        monthDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        monthDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        monthDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        olderDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        olderDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        olderDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        olderDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        olderDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        olderDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        olderDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        olderDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        olderDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        olderDownloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));*/
    }

    private void bindViewWithPosition(int Position) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        if (Position == 0) {
            rvDownloads.BindRecyclerView(new DownloadBinder(), todayDownloadsCollections, layoutManager, new DefaultItemAnimator());
        } else if (Position == 1) {
            rvDownloads.BindRecyclerView(new DownloadBinder(), monthDownloadsCollections, layoutManager, new DefaultItemAnimator());
        } else if (Position == 2) {
            rvDownloads.BindRecyclerView(new DownloadBinder(), olderDownloadsCollections, layoutManager, new DefaultItemAnimator());
        }
        rvDownloads.setNestedScrollingEnabled(false);

    }

    private void replaceList(int position) {
        willbeimplementedinfuture();
        rvDownloads.clearList();
        if (position == 0) {
            rvDownloads.addAll(todayDownloadsCollections);
        } else if (position == 1) {
            rvDownloads.addAll(monthDownloadsCollections);
        } else if (position == 2) {
            rvDownloads.addAll(olderDownloadsCollections);

        }
        rvDownloads.notifyDataSetChanged();

    }
}