package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NewsCategoryEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.NewsFavoriteBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 1/30/2018.
 */
public class NewsFavoriteListFragment extends BaseFragment {
    public static final String TAG = "NewsFavoriteListFragment";
    @BindView(R.id.rv_favorite)
    CustomRecyclerView rvFavorite;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    Unbinder unbinder;
    private int currentClickedItemPosition = 0;
    private RecyclerViewItemListener listener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            NewsCategoryEnt ent = (NewsCategoryEnt) Ent;
            serviceHelper.enqueueCall(webService.unFavoriteNews(ent.getNewsCategoryId(),  prefHelper.getUserToken()), WebServiceConstants.UNFAVORITE_NEWS);
            currentClickedItemPosition = position;
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {

        }
    };
    private ArrayList<NewsCategoryEnt> newsFavoriteCollections;
    public static NewsFavoriteListFragment newInstance() {
        Bundle args = new Bundle();

        NewsFavoriteListFragment fragment = new NewsFavoriteListFragment();
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
            case WebServiceConstants.GET_ALL_FAVORITE:
                BindNewsFovoriteData((ArrayList<NewsCategoryEnt>) result);
                break;
            case WebServiceConstants.UNFAVORITE_NEWS:
                newsFavoriteCollections.remove(currentClickedItemPosition);
                rvFavorite.notifyDataSetChanged();
                currentClickedItemPosition = 0;
                if (newsFavoriteCollections.size() <= 0) {
                    txtNoData.setVisibility(View.VISIBLE);
                    rvFavorite.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void BindNewsFovoriteData(ArrayList<NewsCategoryEnt> result) {
        newsFavoriteCollections = new ArrayList<>();
        newsFavoriteCollections.addAll(result);
        if (result.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvFavorite.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvFavorite.setVisibility(View.VISIBLE);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        rvFavorite.setNestedScrollingEnabled(false);
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        rvFavorite.BindRecyclerView(new NewsFavoriteBinder(options, listener), newsFavoriteCollections, layoutManager, new DefaultItemAnimator());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serviceHelper.enqueueCall(webService.getAllFavoriteNews(prefHelper.getUserToken()),WebServiceConstants.GET_ALL_FAVORITE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}