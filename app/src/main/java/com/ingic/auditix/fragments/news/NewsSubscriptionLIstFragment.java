package com.ingic.auditix.fragments.news;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NewItemDetailEnt;
import com.ingic.auditix.entities.NewsCategoryEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.news.NewsSubscriptionListBinder;
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
import droidninja.filepicker.utils.GridSpacingItemDecoration;

/**
 * Created on 4/13/18.
 */
public class NewsSubscriptionLIstFragment extends BaseFragment {
    public static final String TAG = "NewsSubscriptionLIstFragment";
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.rv_news)
    CustomRecyclerView rvNews;
    Unbinder unbinder;
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

    public static NewsSubscriptionLIstFragment newInstance() {
        Bundle args = new Bundle();

        NewsSubscriptionLIstFragment fragment = new NewsSubscriptionLIstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    public void setTitleBar(TitleBar titleBar) {
        titleBar.hideButtons();
        titleBar.addBackground();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.my_subscriptions));
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
    private void bindNewsCategories(ArrayList<NewItemDetailEnt> result) {
        if (result.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvNews.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvNews.setVisibility(View.VISIBLE);
        }
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL,false);
        rvNews.BindRecyclerView(new NewsSubscriptionListBinder(options, newSubscriptionListener, prefHelper), result, gridLayoutManager, new DefaultItemAnimator());
        rvNews.setHasFixedSize(true);
        rvNews.addItemDecoration(new GridSpacingItemDecoration(2, Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)), true));
        rvNews.setNestedScrollingEnabled(false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_subscription_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serviceHelper.enqueueCall(webService.getAllSubscribeNews(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_SUBSCRIBE);
    }
}