package com.ingic.auditix.fragments.podcast;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastTrackEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.interfaces.PlayerItemChangeListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.interfaces.TrackListItemListener;
import com.ingic.auditix.ui.binders.podcast.PodcastEpisodeListingBinder;
import com.ingic.auditix.ui.views.CustomRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;

/**
 * Created on 1/17/2018.
 */
public class EpisodeListingFragment extends BaseFragment implements PlayerItemChangeListener {
    public static final String TAG = "EpisodeListingFragment";
    @BindView(R.id.rv_episode)
    CustomRecyclerView rvEpisode;
    Unbinder unbinder;
    @BindView(R.id.container)
    LinearLayout container;
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
        public void onRecyclerItemClicked(final Object Ent, int position) {
            if (listItemListener != null) {
                setTrackSelected(previousSelected,false);
                rvEpisode.notifyItemChanged(previousSelected);
                getMainActivity().realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        PodcastTrackEnt ent = (PodcastTrackEnt) Ent;
                        ent.setSelected(true);
                    }
                });
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
        setTrackSelected(previousSelected,true);
        getMainActivity().mSlidingLayout.setScrollableView(rvEpisode);
        rvEpisode.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        rvEpisode.BindRecyclerView(new PodcastEpisodeListingBinder(episodeItemListener), trackList, layoutManager, new DefaultItemAnimator());

    }
    private void setTrackSelected(final int position, final boolean isSelected){
        getMainActivity().realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                trackList.get(position).setSelected(isSelected);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemChanged(int position) {
        if (rvEpisode != null) {
            setTrackSelected(previousSelected,false);
            rvEpisode.notifyItemChanged(previousSelected);
            setTrackSelected(position,true);
            rvEpisode.notifyItemChanged(position);
            previousSelected = position;
        }
    }

    @OnClick(R.id.container)
    public void onViewClicked() {
    }
}