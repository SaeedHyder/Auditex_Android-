package com.ingic.auditix.fragments.profile;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NewItemDetailEnt;
import com.ingic.auditix.entities.NewsCategoryEnt;
import com.ingic.auditix.entities.NewsEpisodeEnt;
import com.ingic.auditix.entities.PlayerNewsEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.news.NewsCategoryDetailFragment;
import com.ingic.auditix.fragments.news.NewsChannelDetailFragment;
import com.ingic.auditix.fragments.news.NewsEpisodeDetailFragment;
import com.ingic.auditix.fragments.news.NewsSubscriptionLIstFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.news.NewsDownloadBinder;
import com.ingic.auditix.ui.binders.news.NewsSubscriptionBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 1/6/2018.
 */
public class ProfileNewsFragment extends BaseFragment {
    public static final String TAG = "ProfileNewsFragment";
    @BindView(R.id.rvBooksFavourite)
    CustomRecyclerView rvNewsSubscribe;
    @BindView(R.id.txt_favourite_no_data)
    AnyTextView txtFavouriteNoData;
    @BindView(R.id.txt_recommended)
    AnyTextView txtRecommended;
    @BindView(R.id.btn_recommne_seeall)
    AnyTextView btnRecommneSeeall;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.rv_news)
    CustomRecyclerView rvNews;
    private RecyclerViewItemListener newSubscriptionListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            serviceHelper.enqueueCall(webService.unsubscribeNews(((NewItemDetailEnt) Ent).getNewsID(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_NEWS);
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            replaceFromParentFragment(NewsChannelDetailFragment.newInstance((NewItemDetailEnt) Ent), NewsCategoryDetailFragment.TAG);
        }
    };
    private RecyclerViewItemListener downloadListner = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {



        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
           // replaceFromParentFragment(NewsEpisodeDetailFragment.newInstance(new PlayerNewsEnt()), NewsCategoryDetailFragment.TAG);

        }
    };
    private DisplayImageOptions options;
    public static ProfileNewsFragment newInstance() {
        Bundle args = new Bundle();

        ProfileNewsFragment fragment = new ProfileNewsFragment();
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
            case WebServiceConstants.GET_ALL_NEWS_SUBSCRIBE:
                bindNewsCategories((ArrayList<NewItemDetailEnt>) result);
                break;
            case WebServiceConstants.UNSUBSCRIBE_NEWS:
                serviceHelper.enqueueCall(webService.getAllSubscribeNews(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_SUBSCRIBE);
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_news, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        serviceHelper.enqueueCall(webService.getAllSubscribeNews(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_SUBSCRIBE);
        assert getParentFragment() != null;
        bindSingleDownloadList(((ProfileFragment)getParentFragment()).getDownloadDetailEnt().getNewsEpisode());
    }

    private void bindNewsCategories(ArrayList<NewItemDetailEnt> result) {
        ArrayList<NewItemDetailEnt> subscriptionCollection = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if (i == 4) {
                break;
            }
            subscriptionCollection.add(result.get(i));
        }
        if (txtNoData != null && rvNewsSubscribe != null) {
            if (result.size() <= 0) {
                txtNoData.setVisibility(View.VISIBLE);
                rvNewsSubscribe.setVisibility(View.GONE);
            } else {
                txtNoData.setVisibility(View.GONE);
                rvNewsSubscribe.setVisibility(View.VISIBLE);
            }
            DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false);
            rvNewsSubscribe.BindRecyclerView(new NewsSubscriptionBinder(options, newSubscriptionListener, prefHelper), subscriptionCollection, gridLayoutManager, new DefaultItemAnimator());
            rvNewsSubscribe.setHasFixedSize(true);
            rvNewsSubscribe.setNestedScrollingEnabled(false);
        }
    }

    private void bindSingleDownloadList(ArrayList<NewsEpisodeEnt> result) {
        if (result.size() > 0) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
            layoutManager.setAutoMeasureEnabled(true);
            rvNews.BindRecyclerView(new NewsDownloadBinder(downloadListner, options), result, layoutManager, new DefaultItemAnimator());
            rvNews.setNestedScrollingEnabled(false);
        } else {
            rvNews.setVisibility(View.GONE);
            rvNews.setVisibility(View.VISIBLE);

        }

    }

    @OnClick(R.id.btn_recommne_seeall)
    public void onViewClicked() {
        replaceFromParentFragment(NewsSubscriptionLIstFragment.newInstance(), NewsSubscriptionLIstFragment.TAG);
    }
}
