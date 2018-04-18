package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.SubscribePodcastEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.MySubscriptionsBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 12/27/2017.
 */
public class SubscriptionsFragment extends BaseFragment implements RecyclerViewItemListener {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.rv_subscription)
    CustomRecyclerView rvSubscription;
    Unbinder unbinder;
    private ArrayList<SubscribePodcastEnt> subscriptionsEnts;
    private String titleHeading;
    private int currentClickedItemPosition = 0;
    public static SubscriptionsFragment newInstance() {
        Bundle args = new Bundle();

        SubscriptionsFragment fragment = new SubscriptionsFragment();
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
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_SUBSCRIBE_PODCASTS:
                bindData((ArrayList<SubscribePodcastEnt>) result);
                break;
            case WebServiceConstants.UNSUBSCRIBE_PODCAST:
                subscriptionsEnts.remove(currentClickedItemPosition);
                rvSubscription.notifyDataSetChanged();
                currentClickedItemPosition = 0;
//                bindData((ArrayList<SubscribePodcastEnt>) result);
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.addBackground();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.my_subscriptions));
//        titleBar.setSubHeading(titleHeading);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_subscription, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serviceHelper.enqueueCall(webService.getSubscribePodcasts(prefHelper.getUserToken()), WebServiceConstants.GET_SUBSCRIBE_PODCASTS);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void bindData(ArrayList<SubscribePodcastEnt> result) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        subscriptionsEnts = new ArrayList<>();
        subscriptionsEnts.addAll(result);
        if (subscriptionsEnts.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvSubscription.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvSubscription.setVisibility(View.VISIBLE);

        }
        rvSubscription.BindRecyclerView(new MySubscriptionsBinder(options, this, prefHelper), subscriptionsEnts,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false), new DefaultItemAnimator());

    }

    @Override
    public void onRecyclerItemButtonClicked(Object Ent, int position) {
        SubscribePodcastEnt ent = (SubscribePodcastEnt) Ent;
        serviceHelper.enqueueCall(webService.unSubscribePodcast(ent.getTrackId(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_PODCAST);
        currentClickedItemPosition = position;

    }

    @Override
    public void onRecyclerItemClicked(Object Ent, int position) {
        SubscribePodcastEnt ent = (SubscribePodcastEnt) Ent;
        getDockActivity().replaceDockableFragment(PodcastDetailFragment.newInstance(ent.getTrackId()), "PodcastDetailFragment");
    }


    public void setTitleHeading(String titleHeading) {
        this.titleHeading = titleHeading;
    }
}