package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BooksChapterItemEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.interfaces.PlayerItemChangeListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.interfaces.TrackListItemListener;
import com.ingic.auditix.ui.binders.ChapterListBinder;
import com.ingic.auditix.ui.views.CustomRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 2/3/2018.
 */
public class BookChaptersListingFragment extends BaseFragment implements PlayerItemChangeListener {
    public static final String TAG = "BookChaptersListingFragment";
    @BindView(R.id.rv_episode)
    CustomRecyclerView rvEpisode;
    Unbinder unbinder;
    @BindView(R.id.container)
    LinearLayout container;
    private TrackListItemListener listItemListener;
    private ArrayList<BooksChapterItemEnt> trackList;
    private int previousSelected = 0;
    private RecyclerViewItemListener episodeItemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            willbeimplementedinfuture();
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            if (listItemListener != null) {
                trackList.get(previousSelected).setSelected(false);
                rvEpisode.notifyItemChanged(previousSelected);
                BooksChapterItemEnt ent = (BooksChapterItemEnt) Ent;
                ent.setSelected(true);
                rvEpisode.notifyItemChanged(position);
                previousSelected = position;
                listItemListener.onTrackSelected(position);
            }
        }
    };

    public static BookChaptersListingFragment newInstance() {
        Bundle args = new Bundle();

        BookChaptersListingFragment fragment = new BookChaptersListingFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_chapter_listing, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void setListItemListener(TrackListItemListener listItemListener) {
        this.listItemListener = listItemListener;
    }

    public void setTrackList(ArrayList<BooksChapterItemEnt> trackList) {
        this.trackList = trackList;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        trackList.get(previousSelected).setSelected(true);
        rvEpisode.BindRecyclerView(new ChapterListBinder(episodeItemListener), trackList, layoutManager, new DefaultItemAnimator());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemChanged(int position) {
        trackList.get(previousSelected).setSelected(false);
        rvEpisode.notifyItemChanged(previousSelected);
        trackList.get(position).setSelected(true);
        rvEpisode.notifyItemChanged(position);
        previousSelected = position;
    }

    @OnClick(R.id.container)
    public void onViewClicked() {
    }
}