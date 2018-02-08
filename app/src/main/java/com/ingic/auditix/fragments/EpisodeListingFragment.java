package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastTrackEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.interfaces.TrackListItemListener;
import com.ingic.auditix.ui.binders.PodcastEpisodeListingBinder;
import com.ingic.auditix.ui.views.CustomRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 1/17/2018.
 */
public class EpisodeListingFragment extends BaseFragment {
    public static final String TAG = "EpisodeListingFragment";
    @BindView(R.id.rv_episode)
    CustomRecyclerView rvEpisode;
    Unbinder unbinder;
    private TrackListItemListener listItemListener;
    private ArrayList<PodcastTrackEnt> trackList;
    private int previousSelected = 0;
    private RecyclerViewItemListener episodeItemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            PodcastTrackEnt ent = (PodcastTrackEnt) Ent;
            willbeimplementedinfuture();
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            if (listItemListener != null) {
                trackList.get(previousSelected).setSelected(false);
                rvEpisode.notifyItemChanged(previousSelected);
                PodcastTrackEnt ent= (PodcastTrackEnt)Ent;
                ent.setSelected(true);
                rvEpisode.notifyItemChanged(position);
                previousSelected = position;
                listItemListener.onTrackSelected(position);
            }
        }
    };

    public static EpisodeListingFragment newInstance(ArrayList<PodcastTrackEnt> trackList) {
        Bundle args = new Bundle();

        EpisodeListingFragment fragment = new EpisodeListingFragment();
        fragment.setArguments(args);
        fragment.setTrackList(trackList);
        return fragment;
    }

    public TrackListItemListener getListItemListener() {
        return listItemListener;
    }

    public void setListItemListener(TrackListItemListener listItemListener) {
        this.listItemListener = listItemListener;
    }

    public void setTrackList(ArrayList<PodcastTrackEnt> trackList) {
        this.trackList = trackList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode_listing, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trackList.get(previousSelected).setSelected(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        rvEpisode.BindRecyclerView(new PodcastEpisodeListingBinder(episodeItemListener), trackList, layoutManager, new DefaultItemAnimator());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}