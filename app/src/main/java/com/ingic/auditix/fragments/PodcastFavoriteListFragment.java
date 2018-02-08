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
public class PodcastFavoriteListFragment extends BaseFragment {
    public static final String TAG = "PodcastFavoriteListFragment";
    @BindView(R.id.rv_favorite)
    CustomRecyclerView rvFavorite;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    Unbinder unbinder;
    private int currentClickedItemPosition = 0;
    private RecyclerViewItemListener listener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            PodcastFavoriteEnt ent = (PodcastFavoriteEnt) Ent;
            serviceHelper.enqueueCall(webService.changeFavoriteStatus(ent.getTrackId(), false, prefHelper.getUserToken()), WebServiceConstants.REMOVE_FAVORITE_PODCAST);
            currentClickedItemPosition = position;
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {

        }
    };
    private ArrayList<PodcastFavoriteEnt> podcastFavoriteCollections;

    public static PodcastFavoriteListFragment newInstance() {
        Bundle args = new Bundle();

        PodcastFavoriteListFragment fragment = new PodcastFavoriteListFragment();
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
            case WebServiceConstants.GET_PODCAST_FAVORITE:
                BindPodcastFovoriteData((ArrayList<PodcastFavoriteEnt>) result);
                break;
            case WebServiceConstants.REMOVE_FAVORITE_PODCAST:
                podcastFavoriteCollections.remove(currentClickedItemPosition);
                rvFavorite.notifyDataSetChanged();
                currentClickedItemPosition = 0;
                if (podcastFavoriteCollections.size() <= 0) {
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

        serviceHelper.enqueueCall(webService.getAllFavorite(prefHelper.getUserToken()), WebServiceConstants.GET_PODCAST_FAVORITE);

    }


    private void BindPodcastFovoriteData(ArrayList<PodcastFavoriteEnt> result) {
        podcastFavoriteCollections = new ArrayList<>();
        podcastFavoriteCollections.addAll(result);
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
        rvFavorite.BindRecyclerView(new FavoriteItemBinder(options, listener), podcastFavoriteCollections, layoutManager, new DefaultItemAnimator());
    }
}