package com.ingic.auditix.fragments.podcast;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PlayerPodcatsEnt;
import com.ingic.auditix.entities.PodcastEpisodeEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.FavoriteCheckChangeListener;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 7/10/18.
 */
public class PodcastEpisodeDetailFragment extends BaseFragment {
    public static final String TAG = "PodcastEpisodeDetailFragment";
    @BindView(R.id.img_item_pic)
    ImageView imgItemPic;
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.txt_narrator)
    AnyTextView txtNarrator;
    @BindView(R.id.txt_narrator_text)
    AnyTextView txtNarratorText;
    @BindView(R.id.txt_duration)
    AnyTextView txtDuration;
    @BindView(R.id.txt_duration_text)
    AnyTextView txtDurationText;
    @BindView(R.id.btnPlay)
    AnyTextView btnPlayText;
    @BindView(R.id.btn_play)
    ImageView btnPlay;
    @BindView(R.id.btnDownloadEpisode)
    AnyTextView btnDownloadEpisode;
    @BindView(R.id.btnDownload)
    ImageView btnDownload;
    @BindView(R.id.btnShareEpisode)
    AnyTextView btnShareEpisode;
    @BindView(R.id.btnShare)
    ImageView btnShare;
    @BindView(R.id.txt_about)
    AnyTextView txtAbout;
    @BindView(R.id.txt_about_text)
    AnyTextView txtAboutText;
    Unbinder unbinder;
    boolean isFromChannel = false;
    private PodcastEpisodeEnt podcastEpisodeEnt;
    private ArrayList<PodcastEpisodeEnt> podcastEpisodeCollection;
    private int startingIndex = 0;
    private FavoriteCheckChangeListener favoritePlayerCheckChangeListener = (check, ID) -> {

    };

    public static PodcastEpisodeDetailFragment newInstance(PodcastEpisodeEnt podcastEpisodeEnt) {
        Bundle args = new Bundle();

        PodcastEpisodeDetailFragment fragment = new PodcastEpisodeDetailFragment();
        fragment.setArguments(args);
        fragment.setPodcastEpisodeEnt(podcastEpisodeEnt);
        fragment.isFromChannel = false;
        return fragment;
    }

    public static PodcastEpisodeDetailFragment newInstance(PodcastEpisodeEnt podcastEpisodeEnt, ArrayList<PodcastEpisodeEnt> podcastEpisodeCollection) {
        Bundle args = new Bundle();

        PodcastEpisodeDetailFragment fragment = new PodcastEpisodeDetailFragment();
        fragment.setArguments(args);
        fragment.setPodcastEpisodeCollection(podcastEpisodeCollection);
        fragment.setPodcastEpisodeEnt(podcastEpisodeEnt);
        fragment.isFromChannel = true;
        return fragment;
    }

    public void setPodcastEpisodeCollection(ArrayList<PodcastEpisodeEnt> podcastEpisodeCollection) {
        this.podcastEpisodeCollection = podcastEpisodeCollection;
    }

    public PodcastEpisodeEnt getPodcastEpisodeEnt() {
        return podcastEpisodeEnt;
    }

    public void setPodcastEpisodeEnt(PodcastEpisodeEnt podcastEpisodeEnt) {
        this.podcastEpisodeEnt = podcastEpisodeEnt;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.addBackground();
        titleBar.setSubHeading(podcastEpisodeEnt.getEpisodeTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_podcast_episode_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serviceHelper.enqueueCall(webService.addListeningEventPodcast(podcastEpisodeEnt.getPodcastId(),podcastEpisodeEnt.getPodcastEpisodeID(),prefHelper.getUserToken()), WebServiceConstants.ADD_TO_LISTENING_LIBRARY);
        txtAboutText.setMovementMethod(new ScrollingMovementMethod());
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        if (podcastEpisodeEnt.getPodcast() != null) {
            ImageLoader.getInstance().displayImage(podcastEpisodeEnt.getPodcast().getImageUrl(), imgItemPic, options);
            txtNarratorText.setText(podcastEpisodeEnt.getPodcast().getArtistName());
            txtAboutText.setText(podcastEpisodeEnt.getPodcast().getDescription());

        }
        txtTitle.setText(podcastEpisodeEnt.getEpisodeTitle());
        txtDurationText.setText(podcastEpisodeEnt.getFileDuration() + "" + getResString(R.string.seconds));
    }

    @OnClick({R.id.btn_play, R.id.btnDownload, R.id.btnShare})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                if (podcastEpisodeEnt != null) {
                    if (isFromChannel) {
                        if (podcastEpisodeCollection != null) {
                            startingIndex = podcastEpisodeCollection.indexOf(podcastEpisodeEnt);
                            openPlayer(new PlayerPodcatsEnt(podcastEpisodeEnt.getPodcast(), podcastEpisodeCollection), startingIndex);
                        }
                    } else {
                        ArrayList<PodcastEpisodeEnt> podcastEpisodeEntArrayList = new ArrayList<>();
                        podcastEpisodeEntArrayList.add(podcastEpisodeEnt);
                        openPlayer(new PlayerPodcatsEnt(podcastEpisodeEnt.getPodcast(), podcastEpisodeEntArrayList), startingIndex);
                    }
                }
                break;
            case R.id.btnDownload:
                break;
            case R.id.btnShare:
                break;
        }
    }

    private void openPlayer(PlayerPodcatsEnt playerPodcatsEnt, int startingIndex) {
        if (getMainActivity().filterFragment != null) {
            getMainActivity().filterFragment.clearFilters();
        }
        getMainActivity().showBottomPlayer(playerPodcatsEnt, podcastEpisodeEnt.getPodcast().getTrackId(), AppConstants.TAB_PODCAST, null, null,
                startingIndex, favoritePlayerCheckChangeListener);
    }
}