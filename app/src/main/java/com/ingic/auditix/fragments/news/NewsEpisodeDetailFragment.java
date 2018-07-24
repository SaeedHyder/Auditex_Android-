package com.ingic.auditix.fragments.news;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.DownloadItemModel;
import com.ingic.auditix.entities.NewsEpisodeEnt;
import com.ingic.auditix.entities.PlayerNewsEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.interfaces.DownloadListenerFragment;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.TitleBar;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 3/15/2018.
 */
public class NewsEpisodeDetailFragment extends BaseFragment {
    public static final String TAG = "NewsEpisodeDetailFragment";

    PlayerNewsEnt episodeData;
    DisplayImageOptions options;
    @BindView(R.id.img_item_pic)
    ImageView imgItemPic;
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.txt_narrator_text)
    AnyTextView txtNarratorText;
    @BindView(R.id.txt_duration_text)
    AnyTextView txtDurationText;
    @BindView(R.id.txt_about_text)
    AnyTextView txtAboutText;
    @BindView(R.id.btn_download_progress)
    CircleProgressBar btnDownloadProgress;
    @BindView(R.id.btnDownload)
    ImageView btnDownload;
    @BindView(R.id.btnDownloadEpisode)
    AnyTextView btnDownloadEpisode;
    private int indexToStart = 0;
    private String episodeID;
    private DownloadListenerFragment fileDownlaodListener = new DownloadListenerFragment() {
        @Override
        public void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            episodeID=(String) task.getTag();
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
            episodeID=(String) task.getTag();
            findAndUpdateDownloadState(AppConstants.DownloadStates.DOWNLOADING, progress);
        }

        @Override
        public void completed(BaseDownloadTask task) {
            episodeID=(String) task.getTag();
            findAndUpdateDownloadState(AppConstants.DownloadStates.COMPLETE, 0);

        }

        @Override
        public void error(BaseDownloadTask task, Throwable e) {
            episodeID=(String) task.getTag();
            findAndUpdateDownloadState(AppConstants.DownloadStates.ERROR, 0);

        }

        @Override
        public void warn(BaseDownloadTask task) {

        }
    };

    public static NewsEpisodeDetailFragment newInstance(PlayerNewsEnt data, int indexToStart) {
        Bundle args = new Bundle();

        NewsEpisodeDetailFragment fragment = new NewsEpisodeDetailFragment();
        fragment.setArguments(args);
        fragment.setEpisodeData(data);
        fragment.setIndexToStart(indexToStart);
        return fragment;
    }

    private void findAndUpdateDownloadState(int State, int Progress) {
        NewsEpisodeEnt ent = new NewsEpisodeEnt();
        ent.setNewsepisodeid(episodeID);
        if (episodeData.getNewsepisodeslist().get(indexToStart).getNewsepisodeid().equalsIgnoreCase(episodeID)) {
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

    public void setIndexToStart(int indexToStart) {
        this.indexToStart = indexToStart;
    }

    public void setEpisodeData(PlayerNewsEnt episodeData) {
        this.episodeData = episodeData;
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
        if (episodeData.getDetailEnt() == null)
            titleBar.setSubHeading(getResString(R.string.news_detail));
        else
            titleBar.setSubHeading(episodeData.getDetailEnt().getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_episode_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        getDockActivity().setFileDownloadListener(fileDownlaodListener);
        bindData();
    }


    public String getDurationText(Integer secound) {
        int minutes = (secound % 3600) / 60;
        int hours = secound / 3600;

        if (hours > 1) {
            return (hours) + " Hr " + (minutes) + " Mins";
        } else if (minutes > 0) {
            return (minutes) + " Mins";
        } else if (secound > 0) {
            return (secound) + " Sec";
        } else {
            return "-";
        }

    }

    private boolean isAlreadyDownloaded(int newsID, String episodeID) {
        return new File(getNewsDownloadPath(newsID, episodeID)).exists();
    }

    private void bindData() {
        NewsEpisodeEnt episodeEnt = episodeData.getNewsepisodeslist().get(indexToStart);
        ImageLoader.getInstance().displayImage(episodeData.getDetailEnt().getImageUrl(), imgItemPic, options);
        txtDurationText.setText(episodeEnt.getFileduration() + " " + getResString(R.string.seconds));
        txtNarratorText.setText(episodeData.getDetailEnt().getNarratorName() + "");
        txtTitle.setEllipsize(TextUtils.TruncateAt.END);
        txtTitle.setMaxLines(3);
        txtTitle.setText(episodeData.getDetailEnt().getName() + "");
        txtAboutText.setText(episodeData.getDetailEnt().getDescription() + "");
        txtAboutText.setMovementMethod(new ScrollingMovementMethod());

        if (isAlreadyDownloaded(episodeData.getDetailEnt().getNewsID(), episodeEnt.getNewsepisodeid())) {
            btnDownload.setEnabled(false);
            btnDownload.setAlpha(0.5f);
            btnDownloadEpisode.setEnabled(false);
        }
    }



    private void openPlayer(PlayerNewsEnt playerNewsEnt, int startingIndex) {
        if (getMainActivity().newsFilterFragment != null) {
            getMainActivity().newsFilterFragment.clearFilters();
        }
        getMainActivity().showBottomPlayer(null, playerNewsEnt.getDetailEnt().getNewsID(), AppConstants.TAB_NEWS, null, playerNewsEnt,
                startingIndex, null);
    }

    @OnClick({R.id.btnPlay, R.id.btn_play, R.id.btnDownloadEpisode, R.id.btnDownload, R.id.btnShareEpisode, R.id.btnShare})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnPlay:
                openPlayer(episodeData, indexToStart);
                break;
            case R.id.btn_play:
                openPlayer(episodeData, indexToStart);
                break;
            case R.id.btnDownloadEpisode:
                NewsEpisodeEnt item = episodeData.getNewsepisodeslist().get(indexToStart);
                getDockActivity().addDownload(episodeData.getNewsepisodeslist().get(indexToStart).getFilepath(),
                        getNewsDownloadPath(episodeData.getDetailEnt().getNewsID(), item.getNewsepisodeid()),
                        item.getNewsepisodeid() + "",
                        item.getEpisodetitle(),
                        episodeData.getDetailEnt().getName(),
                        item
                );
                break;
            case R.id.btnDownload:

                break;
            case R.id.btnShareEpisode:
                break;
            case R.id.btnShare:
                break;
        }
    }

    private String getNewsDownloadPath(int newsID, String episodeID) {
        return AppConstants.DOWNLOAD_PATH + File.separator + AppConstants.TAB_NEWS + File.separator + newsID + File.separator + episodeID;

    }


}