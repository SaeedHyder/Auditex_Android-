package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastFavoriteEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.FavoriteItemBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 1/12/2018.
 */
public class FavoriteListFragment extends BaseFragment {
    public static final String TAG = "FavoriteListFragment";
    @BindView(R.id.rv_favorite)
    CustomRecyclerView rvFavorite;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    Unbinder unbinder;
    private String typeFavorite;
    private int currentClickedItemPosition = 0;
    private RecyclerViewItemListener listener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            PodcastFavoriteEnt ent = (PodcastFavoriteEnt) Ent;
            serviceHelper.enqueueCall(webService.changeFavoriteStatus(ent.getTrackId(), false, prefHelper.getUserToken()), WebServiceConstants.REMOVE_FAVORITE);
            currentClickedItemPosition = position;
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {

        }
    };
    private ArrayList<PodcastFavoriteEnt> favoriteCollections;

    public static FavoriteListFragment newInstance(String typeFavorite) {
        Bundle args = new Bundle();

        FavoriteListFragment fragment = new FavoriteListFragment();
        fragment.setArguments(args);
        fragment.setTypeFavorite(typeFavorite);
        return fragment;
    }

    public void setTypeFavorite(String typeFavorite) {
        this.typeFavorite = typeFavorite;
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
            case WebServiceConstants.GET_PODCAST_FAVORITE:
                BindData((ArrayList<PodcastFavoriteEnt>) result);
                break;
            case WebServiceConstants.REMOVE_FAVORITE:
                favoriteCollections.remove(currentClickedItemPosition);
                rvFavorite.notifyDataSetChanged();
                currentClickedItemPosition = 0;
                if (favoriteCollections.size() <= 0) {
                    txtNoData.setVisibility(View.VISIBLE);
                    rvFavorite.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (typeFavorite.equalsIgnoreCase(AppConstants.TAB_NEWS)) {
            BindData(new ArrayList<PodcastFavoriteEnt>());
        } else if (typeFavorite.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
            serviceHelper.enqueueCall(webService.getAllFavorite(prefHelper.getUserToken()), WebServiceConstants.GET_PODCAST_FAVORITE);
        } else if (typeFavorite.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
            BindData(new ArrayList<PodcastFavoriteEnt>());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void BindData(ArrayList<PodcastFavoriteEnt> result) {
        favoriteCollections = new ArrayList<>();
        favoriteCollections.addAll(result);
        if (result.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvFavorite.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvFavorite.setVisibility(View.VISIBLE);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        rvFavorite.BindRecyclerView(new FavoriteItemBinder(options, listener), favoriteCollections, layoutManager, new DefaultItemAnimator());
    }
}