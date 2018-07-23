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
    private int indexToStart = 0;
    private DownloadListenerFragment fileDownlaodListener = new DownloadListenerFragment() {
        @Override
        public void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        public void started(BaseDownloadTask task) {

        }

        @Override
        public void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {

        }

        @Override
        public void progress(BaseDownloadTask task, int progress) {

        }

        @Override
        public void completed(BaseDownloadTask task) {

        }

        @Override
        public void error(BaseDownloadTask task, Throwable e) {

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

    private void bindData() {
        NewsEpisodeEnt episodeEnt = episodeData.getNewsepisodeslist().get(indexToStart);
        ImageLoader.getInstance().displayImage(episodeData.getDetailEnt().getImageUrl(), imgItemPic, options);
        txtDurationText.setText(episodeEnt.getFileduration() + " " + getResString(R.string.seconds));
        txtNarratorText.setText(episodeData.getDetailEnt().getNarratorName() + "");
        txtTitle.setEllipsize(TextUtils.TruncateAt.END);
        txtTitle.setMaxLines(3);
        txtTitle.setText(episodeEnt.getEpisodetitle() + "");
        txtAboutText.setText(episodeData.getDetailEnt().getDescription() + "");
        txtAboutText.setMovementMethod(new ScrollingMovementMethod());
    }

    @OnClick(R.id.btn_play)
    public void onViewClicked() {
      /*  getMainActivity().showBottomPlayer(null, newsCategoryID,
                AppConstants.TAB_NEWS, null, episodeData, startingIndex,null);*/
        /*getDockActivity().replaceDockableFragment(PlayerFragment.newInstance(null, 0,
                AppConstants.TAB_NEWS, null, episodeData, startingIndex), PlayerFragment.TAG);*/

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
                getDockActivity().addDownload(episodeData.getNewsepisodeslist().get(indexToStart).getFilepath(), episodeData.getDetailEnt().getNewsID() + "",
                        episodeData.getNewsepisodeslist().get(indexToStart).getEpisodetitle() + "" + episodeData.getNewsepisodeslist().get(indexToStart).getNewsepisodeid(), episodeData.getDetailEnt().getName(), episodeData.getDetailEnt());
                break;
            case R.id.btnDownload:
                break;
            case R.id.btnShareEpisode:
                break;
            case R.id.btnShare:
                break;
        }
    }

  //region Downlaod Methods
    void updateDownloadStatus(NewsEpisodeEnt entity){
        if (isAlreadyDownloaded(entity.getFilepath())) {
            btnDownload.setVisibility(View.GONE);
            btnDownloadProgress.setVisibility(View.GONE);
        } else if (entity.getStatusState() != AppConstants.DownloadStates.DOWNLOADING) {
            DownloadItemModel downloadItem = getObjectfromRealm(entity.getNewsepisodeid()+"");
            if (downloadItem != null) {
                entity.setStatusState(downloadItem.getDownloadState());
                entity.setDownloadProgress(downloadItem.getDownloadProgress());
            }
        }
        int status = entity.getStatusState();
        switch (status) {
            case AppConstants.DownloadStates.ERROR:
                btnDownload.setVisibility(View.VISIBLE);
                btnDownloadProgress.setVisibility(View.GONE);
                break;
            case AppConstants.DownloadStates.STARTED:
              /*  holder.btnDownload.setVisibility(View.GONE);
                holder.btnPlay.setVisibility(View.GONE);
                holder.downloadProgress.setVisibility(View.VISIBLE);*/
                break;
            case AppConstants.DownloadStates.DOWNLOADING:
                btnDownload.setVisibility(View.GONE);
                btnDownloadProgress.setVisibility(View.VISIBLE);
                // entity.setDownloadProgress(entity.getDownloadProgress() + 1);
                btnDownloadProgress.setProgress(entity.getDownloadProgress());
                break;
            case AppConstants.DownloadStates.PENDING:
                btnDownload.setVisibility(View.GONE);
                btnDownloadProgress.setVisibility(View.VISIBLE);
                break;
            case AppConstants.DownloadStates.COMPLETE:
                btnDownload.setVisibility(View.GONE);
                btnDownloadProgress.setVisibility(View.GONE);
                break;
            default:

                break;
        }


    }

    private DownloadItemModel getObjectfromRealm(String chapterID) {
        return getMainActivity().realm
                .where(DownloadItemModel.class)
                .equalTo("downloadTag", chapterID).findFirst();
    }

    private boolean isAlreadyDownloaded(String audioUrl) {
        return new File(AppConstants.DOWNLOAD_PATH + File.separator + "Enter Parent Folder" + File.separator + audioUrl.
                replaceAll("\\s+", "")
                .replaceAll("\\\\", "")
                .replaceAll("/", "")).exists();
    }
    //endregion
}