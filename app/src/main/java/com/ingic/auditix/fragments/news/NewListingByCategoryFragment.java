package com.ingic.auditix.fragments.news;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
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

import static com.ingic.auditix.global.WebServiceConstants.GET_ALL_NEWS_BY_CATEGORIES;

/**
 * Created on 7/14/18.
 */
public class NewListingByCategoryFragment extends BaseFragment {
    public static final String TAG = "NewListingByCategoryFragment";
    @BindView(R.id.rvListing)
    CustomRecyclerView rvListing;
    Unbinder unbinder;
    @BindView(R.id.txt_subscription_no_data)
    AnyTextView txtSubscriptionNoData;
    private Integer categoryID;
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
            replaceFromParentFragment(NewsChannelDetailFragment.newInstance((NewItemDetailEnt) Ent), NewsCategoryDetailFragment.TAG);

        }
    };

    public static NewListingByCategoryFragment newInstance(Integer categoryID) {
        Bundle args = new Bundle();

        NewListingByCategoryFragment fragment = new NewListingByCategoryFragment();
        fragment.setArguments(args);
        fragment.setCategoryID(categoryID);
        return fragment;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
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
            case GET_ALL_NEWS_BY_CATEGORIES:
                bindData((ArrayList<NewItemDetailEnt>) result);
                break;
        }
    }
    public void setTitleBar(TitleBar titleBar) {

        titleBar.addBackground();
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.addBackground();
        titleBar.setSubHeading(getResString(R.string.news_detail));
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
        serviceHelper.enqueueCall(webService.getAllNewsByCategory(categoryID, prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_BY_CATEGORIES);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void bindData(ArrayList<NewItemDetailEnt> result) {
        txtSubscriptionNoData.setVisibility(result.size() <= 0 ? View.VISIBLE : View.GONE);
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        rvListing.BindRecyclerView(new NewsCategoryListingBinder(options, itemClickListner, prefHelper), result, new LinearLayoutManager(getDockActivity()), new DefaultItemAnimator());
    }
}