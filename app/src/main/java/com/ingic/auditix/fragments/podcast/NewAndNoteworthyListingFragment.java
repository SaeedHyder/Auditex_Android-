package com.ingic.auditix.fragments.podcast;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastEpisodeEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.podcast.PodcastNewAndNoteworthyListingBinder;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 7/11/18.
 */
public class NewAndNoteworthyListingFragment extends BaseFragment {
    public static final String TAG = "NewAndNoteworthyListingFragment";
    @BindView(R.id.rvListing)
    CustomRecyclerView rvListing;
    Unbinder unbinder;

    ArrayList<PodcastEpisodeEnt> episodesCollection;
    private RecyclerViewItemListener subscriptionItemLister = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            PodcastEpisodeEnt ent = (PodcastEpisodeEnt) Ent;
            if (ent.getPodcast().getSubscribed()) {
                serviceHelper.enqueueCall(webService.unSubscribePodcast(ent.getPodcast().getTrackId(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_PODCAST);
            } else {
                serviceHelper.enqueueCall(webService.subscribePodcast(ent.getPodcast().getTrackId(), ent.getPodcast().getCategoryId(), prefHelper.getUserToken()), WebServiceConstants.SUBSCRIBE_PODCAST);
            }
            ent.getPodcast().setSubscribed(!ent.getPodcast().getSubscribed());
            rvListing.notifyItemChanged(position);

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            PodcastEpisodeEnt ent = (PodcastEpisodeEnt) Ent;
            openPodcastEpisodeDetail(ent);

        }
    };

    public static NewAndNoteworthyListingFragment newInstance(ArrayList<PodcastEpisodeEnt> episodesCollection) {
        Bundle args = new Bundle();

        NewAndNoteworthyListingFragment fragment = new NewAndNoteworthyListingFragment();
        fragment.setArguments(args);
        fragment.setEpisodesCollection(episodesCollection);
        return fragment;
    }

    private void openPodcastEpisodeDetail(PodcastEpisodeEnt ent) {
        getDockActivity().replaceDockableFragment(PodcastEpisodeDetailFragment.newInstance(ent), PodcastEpisodeDetailFragment.TAG);
    }

    public void setEpisodesCollection(ArrayList<PodcastEpisodeEnt> episodesCollection) {
        this.episodesCollection = episodesCollection;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_see_all, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    public void setTitleBar(TitleBar titleBar) {
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.podcast));
        titleBar.showBackButton();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));

        rvListing.BindRecyclerView(new PodcastNewAndNoteworthyListingBinder(options, subscriptionItemLister, prefHelper), episodesCollection,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false),
                new DefaultItemAnimator());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}