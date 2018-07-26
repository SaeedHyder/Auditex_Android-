package com.ingic.auditix.fragments.podcast;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.PlayerPodcatsEnt;
import com.ingic.auditix.entities.PodcastEpisodeEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.DownloadListenerFragment;
import com.ingic.auditix.interfaces.FavoriteCheckChangeListener;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.TitleBar;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
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
    @BindView(R.id.btn_download_progress)
    CircleProgressBar btnDownloadProgress;
    private PodcastEpisodeEnt podcastEpisodeEnt;
    private ArrayList<PodcastEpisodeEnt> podcastEpisodeCollection;
    private int startingIndex = 0;
    private String episodeID;
    private FavoriteCheckChangeListener favoritePlayerCheckChangeListener = (check, ID) -> {

    };
    private DownloadListenerFragment fileDownlaodListener = new DownloadListenerFragment() {
        @Override
        public void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            episodeID = (String) task.getTag();
            findAndUpdateDownloadState(AppConstants.DownloadStates.PENDING, 0);
        }

        @Override
        public void started(BaseDownloadTask task) {

        }

        @Override
        public void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {

        }

        @Override
        public void progress(BaseDownloadTask task, int progress) {
            episodeID = (String) task.getTag();
            findAndUpdateDownloadState(AppConstants.DownloadStates.DOWNLOADING, progress);
        }

        @Override
        public void completed(BaseDownloadTask task) {
            episodeID = (String) task.getTag();
            findAndUpdateDownloadState(AppConstants.DownloadStates.COMPLETE, 0);

        }

        @Override
        public void error(BaseDownloadTask task, Throwable e) {
            episodeID = (String) task.getTag();
            findAndUpdateDownloadState(AppConstants.DownloadStates.ERROR, 0);

        }

        @Override
        public void warn(BaseDownloadTask task) {

        }
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

    private void findAndUpdateDownloadState(int State, int Progress) {
        if (podcastEpisodeEnt.getPodcastEpisodeID().equalsIgnoreCase(episodeID)) {
            switch (State) {
                case AppConstants.DownloadStates.ERROR:
                    btnDownload.setEnabled(true);
                    btnDownloadEpisode.setEnabled(true);
                    btnDownload.setVisibility(View.VISIBLE);
                    btnDownloadProgress.setVisibility(View.GONE);
                    break;
                case AppConstants.DownloadStates.STARTED:
              /*  holder.btnDownload.setVisibility(View.GONE);
                holder.btnPlay.setVisibility(View.GONE);
                holder.downloadProgress.setVisibility(View.VISIBLE);*/
                    break;
                case AppConstants.DownloadStates.DOWNLOADING:
                    btnDownloadEpisode.setEnabled(false);
                    btnDownload.setVisibility(View.GONE);
                    btnDownloadProgress.setVisibility(View.VISIBLE);
                    btnDownloadProgress.setProgress(Progress);
                    break;
                case AppConstants.DownloadStates.PENDING:
                    btnDownloadEpisode.setEnabled(false);
                    btnDownload.setVisibility(View.GONE);
                    btnDownloadProgress.setVisibility(View.VISIBLE);
                    break;
                case AppConstants.DownloadStates.COMPLETE:
                    btnDownload.setVisibility(View.VISIBLE);
                    btnDownloadProgress.setVisibility(View.GONE);
                    btnDownload.setEnabled(false);
                    btnDownload.setAlpha(.5f);
                    btnDownloadEpisode.setEnabled(false);
                    break;
                default:

                    break;
            }
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
        getDockActivity().setFileDownloadListener(fileDownlaodListener);
        bindScreenData();
    }

    private void bindScreenData() {
        serviceHelper.enqueueCall(webService.addListeningEventPodcast(podcastEpisodeEnt.getPodcastId(), podcastEpisodeEnt.getPodcastEpisodeID(), prefHelper.getUserToken()), WebServiceConstants.ADD_TO_LISTENING_LIBRARY);
        txtAboutText.setMovementMethod(new ScrollingMovementMethod());
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        if (podcastEpisodeEnt.getPodcast() != null) {
            ImageLoader.getInstance().displayImage(podcastEpisodeEnt.getPodcast().getImageUrl(), imgItemPic, options);
            txtNarratorText.setText(podcastEpisodeEnt.getPodcast().getArtistName());
            txtAboutText.setText(podcastEpisodeEnt.getPodcast().getDescription());

        }
        txtTitle.setText(podcastEpisodeEnt.getEpisodeTitle());
        txtDurationText.setText(podcastEpisodeEnt.getFileDuration() + "" + getResString(R.string.seconds));
        if (isAlreadyDownloaded(podcastEpisodeEnt.getPodcastId(), podcastEpisodeEnt.getPodcastEpisodeID())) {
            btnDownload.setEnabled(false);
            btnDownload.setAlpha(0.5f);
            btnDownloadEpisode.setEnabled(false);
        }
    }

    private boolean isAlreadyDownloaded(int newsID, String episodeID) {
        return new File(getPodcastDownloadPath(newsID, episodeID)).exists();
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
                getDockActivity().addDownload(podcastEpisodeEnt.getFilePath(),
                        getPodcastDownloadPath(podcastEpisodeEnt.getPodcastId(), podcastEpisodeEnt.getPodcastEpisodeID()),
                        podcastEpisodeEnt.getPodcastEpisodeID(),
                        podcastEpisodeEnt.getEpisodeTitle(),
                        podcastEpisodeEnt.getPodcast().getName(),
                        podcastEpisodeEnt
                );
                break;
            case R.id.btnShare:
                break;
        }
    }



    private String getPodcastDownloadPath(int podcastID, String episodeID) {
        File directory = new File(String.valueOf(AppConstants.DOWNLOAD_PATH + File.separator + AppConstants.TAB_PODCAST + File.separator + podcastID));
        if(!directory.exists()){
            directory.mkdir();
        }
        return AppConstants.DOWNLOAD_PATH + File.separator + AppConstants.TAB_PODCAST + File.separator + podcastID + File.separator + episodeID;

    }

    private void openPlayer(PlayerPodcatsEnt playerPodcatsEnt, int startingIndex) {
        if (getMainActivity().filterFragment != null) {
            getMainActivity().filterFragment.clearFilters();
        }
        getMainActivity().showBottomPlayer(playerPodcatsEnt, podcastEpisodeEnt.getPodcast().getTrackId(), AppConstants.TAB_PODCAST, null, null,
                startingIndex, favoritePlayerCheckChangeListener);
    }


}