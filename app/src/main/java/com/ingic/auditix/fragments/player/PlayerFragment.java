package com.ingic.auditix.fragments.player;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.devbrackets.android.exomedia.AudioPlayer;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.exoplayer2.Player;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.AdvertisementEnt;
import com.ingic.auditix.entities.BookChaptersEnt;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.entities.BooksChapterItemEnt;
import com.ingic.auditix.entities.NewsEpisodeEnt;
import com.ingic.auditix.entities.PlayerNewsEnt;
import com.ingic.auditix.entities.PlayerPodcatsEnt;
import com.ingic.auditix.entities.PodcastEpisodeEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.books.BookChaptersListingFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.DownloadListenerFragment;
import com.ingic.auditix.interfaces.FavoriteCheckChangeListener;
import com.ingic.auditix.interfaces.PlayerItemChangeListener;
import com.ingic.auditix.interfaces.TrackListItemListener;
import com.ingic.auditix.media.MediaPlayerHolder;
import com.ingic.auditix.media.PlayListModel;
import com.ingic.auditix.media.PlaybackInfoListener;
import com.ingic.auditix.media.PlayerAdapter;
import com.ingic.auditix.ui.slidinglayout.SlidingUpPanelLayout;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRatingBar;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 12/27/2017.
 */
public class PlayerFragment extends BaseFragment implements TrackListItemListener, FavoriteCheckChangeListener {

    //region Global Variables
    public static final String TAG = "PlayerFragment";
    @BindView(R.id.btn_volume)
    ImageView btnVolume;
    @BindView(R.id.txt_playing_item_album)
    AnyTextView txtPlayingItemAlbum;
    @BindView(R.id.txt_player_item_name)
    AnyTextView txtPlayingItemName;
    @BindView(R.id.txt_time_total)
    AnyTextView txtTimeTotal;
    @BindView(R.id.btn_backward)
    ImageView btnBackward;
    @BindView(R.id.btn_play)
    ImageView btnPlay;
    @BindView(R.id.btn_forward)
    ImageView btnForward;
    @BindView(R.id.txt_remaining_time)
    AnyTextView txtRemainingTime;
    @BindView(R.id.sb_progress)
    SeekBar sbProgress;
    @BindView(R.id.img_item_pic)
    ImageView imgItemPic;
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.txt_narrator_text)
    AnyTextView txtNarratorText;
    @BindView(R.id.rb_rating)
    CustomRatingBar rbRating;
    @BindView(R.id.txt_genre_text)
    AnyTextView txtGenreText;
    @BindView(R.id.txt_duration)
    AnyTextView txtDuration;
    @BindView(R.id.txt_duration_text)
    AnyTextView txtDurationText;
    @BindView(R.id.txt_location_text)
    AnyTextView txtLocationText;
    @BindView(R.id.img_item_cover)
    ImageView imgItemCover;
    Unbinder unbinder;
    @BindView(R.id.pb_buffering)
    ProgressBar pbBuffering;
    boolean hasItemPlayCompleted = false;
    @BindView(R.id.img_player_cover)
    ImageView imgPlayerCover;
    // @BindView(R.id.btn_player_play)
    ImageView btnPlayerPlay;
    @BindView(R.id.container_player)
    LinearLayout containerPlayer;
    @BindView(R.id.btnLeft)
    ImageView btnLeft;
    @BindView(R.id.txt_subHead)
    AnyTextView txtSubHead;
    @BindView(R.id.btn_favorite)
    CheckBox btnFavorite;
    @BindView(R.id.title_bar)
    RelativeLayout titleBarView;
    @BindView(R.id.txt_item_name)
    AnyTextView txtItemName;
    @BindView(R.id.txt_item_album)
    AnyTextView txtItemAlbum;
    @BindView(R.id.pb_bottom_buffering)
    ProgressBar pbBottomBuffering;
    @BindView(R.id.btnShare)
    ImageView btnShare;
    @BindView(R.id.btn_download_progress)
    CircleProgressBar btnDownloadProgress;
    @BindView(R.id.btn_download)
    ImageView btnDownload;


    private PlayerAdapter mPlayerAdapter;
    private boolean mUserIsSeeking = false;
    private boolean hasPlaylistComplete = false;
    private ArrayList<PlayListModel> mUserPlaylist;
    private PlayerPodcatsEnt podcastDetailEnt;
    private PlayerNewsEnt newsEpisodeEnt;
    private BookDetailEnt bookDetailEnt;
    private Integer ID;
    private String playerType = "";

    private int startingIndex = 0;
    private PlayerItemChangeListener itemChangeListener;
    private FavoriteCheckChangeListener checkChangeListener;
    private CompoundButton.OnCheckedChangeListener podcastFavoriteCheckListener = ((buttonView, isChecked) -> {
        if (prefHelper.isGuest()) {
            showGuestMessage();
            btnFavorite.setChecked(!isChecked);
        } else {
            serviceHelper.enqueueCall(webService.changeFavoriteStatus(ID, isChecked, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
            if (checkChangeListener != null) {
                checkChangeListener.onFavoriteCheckChange(isChecked, ID);
            }
        }
    });
    private CompoundButton.OnCheckedChangeListener bookFavoriteListener = ((buttonView, isChecked) -> {
        if (prefHelper.isGuest()) {
            showGuestMessage();
            btnFavorite.setChecked(!isChecked);
        } else {
            if (isChecked) {
                serviceHelper.enqueueCall(webService.AddBookToFavorite(ID, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
            } else {
                serviceHelper.enqueueCall(webService.RemoveBookFromFavorite(ID, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
            }
            if (checkChangeListener != null) {
                checkChangeListener.onFavoriteCheckChange(isChecked, ID);
            }
        }
    });
    private CompoundButton.OnCheckedChangeListener newsFavoriteListener = ((buttonView, isChecked) -> {
        if (prefHelper.isGuest()) {
            showGuestMessage();
            btnFavorite.setChecked(!isChecked);
        } else {
            if (isChecked) {
                serviceHelper.enqueueCall(webService.favoriteNews(ID, prefHelper.getUserToken()), WebServiceConstants.FAVORITE_NEWS);
            } else {
                serviceHelper.enqueueCall(webService.unFavoriteNews(ID, prefHelper.getUserToken()), WebServiceConstants.UNFAVORITE_NEWS);
            }
            if (checkChangeListener != null) {
                checkChangeListener.onFavoriteCheckChange(isChecked, ID);
            }
        }
    });
    private DownloadListenerFragment fileDownloadListener = new DownloadListenerFragment() {
        @Override
        public void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            findAndUpdateDownloadState(AppConstants.DownloadStates.PENDING, 0, (String) task.getTag());
        }

        @Override
        public void started(BaseDownloadTask task) {

        }

        @Override
        public void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {

        }

        @Override
        public void progress(BaseDownloadTask task, int progress) {
            findAndUpdateDownloadState(AppConstants.DownloadStates.DOWNLOADING, progress, (String) task.getTag());
        }

        @Override
        public void completed(BaseDownloadTask task) {
            findAndUpdateDownloadState(AppConstants.DownloadStates.COMPLETE, 0, (String) task.getTag());

        }

        @Override
        public void error(BaseDownloadTask task, Throwable e) {
            findAndUpdateDownloadState(AppConstants.DownloadStates.ERROR, 0, (String) task.getTag());

        }

        @Override
        public void warn(BaseDownloadTask task) {

        }
    };

    public static PlayerFragment newInstance(PlayerPodcatsEnt podcastDetail, Integer ID, String playerType,
                                             BookDetailEnt bookDetailEnt, PlayerNewsEnt newsEpisodeEnt, int startingIndex, FavoriteCheckChangeListener checkChangeListener) {
        Bundle args = new Bundle();

        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        fragment.setContent(podcastDetail, ID, playerType, bookDetailEnt, newsEpisodeEnt, startingIndex, checkChangeListener);
        return fragment;
    }

    //endregion
    private void findAndUpdateDownloadState(int State, int Progress, String id) {
        if (playerType.equalsIgnoreCase(AppConstants.TAB_NEWS)) {
            if (newsEpisodeEnt.getNewsepisodeslist().get(mPlayerAdapter.getCurrentItemIndex()).getNewsepisodeid().equalsIgnoreCase(id)) {
                switchDownloadStates(State, Progress);
            }
        } else if (playerType.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
            if (podcastDetailEnt.getPodcastEpisodeList().get(mPlayerAdapter.getCurrentItemIndex()).getPodcastEpisodeID().equalsIgnoreCase(id)) {
                switchDownloadStates(State, Progress);
            }

        } else if (playerType.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
            if (bookDetailEnt.getChapters().getChapter().get(mPlayerAdapter.getCurrentItemIndex()).getChapterID().equalsIgnoreCase(id)) {
                switchDownloadStates(State, Progress);
            }
        }
    }

    private void switchDownloadStates(int State, int Progress) {
        switch (State) {
            case AppConstants.DownloadStates.ERROR:
                btnDownload.setEnabled(true);
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
                btnDownloadProgress.setProgress(Progress);
                break;
            case AppConstants.DownloadStates.PENDING:
                btnDownload.setVisibility(View.GONE);
                btnDownloadProgress.setVisibility(View.VISIBLE);
                break;
            case AppConstants.DownloadStates.COMPLETE:
                btnDownload.setVisibility(View.VISIBLE);
                btnDownloadProgress.setVisibility(View.GONE);
                btnDownload.setEnabled(false);
                btnDownload.setAlpha(.5f);
                break;
            default:

                break;
        }
    }

    public void setContent(PlayerPodcatsEnt podcastDetail, Integer trackID, String playerType, BookDetailEnt bookDetailEnt, PlayerNewsEnt newsEpisodeEnt, int startingIndex, FavoriteCheckChangeListener checkChangeListener) {
        this.podcastDetailEnt = podcastDetail;
        this.ID = trackID;
        this.playerType = playerType;
        this.bookDetailEnt = bookDetailEnt;
        this.newsEpisodeEnt = newsEpisodeEnt;
        this.startingIndex = startingIndex;
        this.checkChangeListener = checkChangeListener;
    }

    private void getDetails(String type) {
        if (type.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
            setupUIViewsPodcast(podcastDetailEnt);
        } else if (type.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
            setupUIViewsBook(bookDetailEnt);
        } else if (type.equalsIgnoreCase(AppConstants.TAB_NEWS)) {
            setupUIViewsNews(newsEpisodeEnt);
        }
        hasPlaylistComplete = false;
        txtPlayingItemAlbum.setSelected(true);
        txtPlayingItemName.setSelected(true);
        txtItemName.setSelected(true);
        txtItemAlbum.setSelected(true);

    }

    private void setupUIViewsNews(PlayerNewsEnt ent) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        ImageLoader.getInstance().displayImage(ent.getDetailEnt().getImageUrl(), imgItemCover, options);
        ImageLoader.getInstance().displayImage(ent.getDetailEnt().getImageUrl(), imgItemPic, options);
        ImageLoader.getInstance().displayImage(ent.getDetailEnt().getImageUrl(), imgPlayerCover, options);
        txtTitle.setText(ent.getDetailEnt() + "");
        sbProgress.setPadding(0, 0, 0, 0);
        txtDuration.setVisibility(View.INVISIBLE);
        txtDurationText.setVisibility(View.INVISIBLE);
        // txtDurationText.setText(getBookDurationText(ent.getDuration()));
        bindNewsPlaylist(newsEpisodeEnt.getNewsepisodeslist());

    }

    private void bindNewsPlaylist(ArrayList<NewsEpisodeEnt> tracklist) {
        initializePlaybackController();
        initializeSeekbar();
        mUserPlaylist = new ArrayList<>();
        for (NewsEpisodeEnt tracks : tracklist
                ) {
            String path = getDownloadPath(ID, tracks.getNewsepisodeid(), AppConstants.TAB_NEWS);
            if (new File(path).exists()) {
                mUserPlaylist.add(new PlayListModel(AppConstants.FILE_PATH + path, "", true));

            } else {
                mUserPlaylist.add(new PlayListModel("", tracks.getFilepath(), false));
            }
        }
        mPlayerAdapter.loadPlayList(mUserPlaylist, startingIndex);
        setItemName(startingIndex);

    }

    private String getDownloadPath(int parentID, String episodeID, String parentTag) {
        File directory = new File(String.valueOf(AppConstants.DOWNLOAD_PATH + File.separator + parentTag + File.separator + parentID));
        if(!directory.exists()){
            directory.mkdir();
        }
        return AppConstants.DOWNLOAD_PATH + File.separator + parentTag + File.separator + parentID + File.separator + episodeID;

    }


    private void setupUIViewsPodcast(PlayerPodcatsEnt ent) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        ImageLoader.getInstance().displayImage(ent.getPodcastDetail().getImageUrl(), imgItemCover, options);
        ImageLoader.getInstance().displayImage(ent.getPodcastDetail().getImageUrl(), imgItemPic, options);
        ImageLoader.getInstance().displayImage(ent.getPodcastDetail().getImageUrl(), imgPlayerCover, options);
        if (ent.getPodcastDetail().getRating() == -1) {
            rbRating.setScore(0);
        } else {
            rbRating.setScore((float) ent.getPodcastDetail().getRating());
        }
        txtTitle.setText(ent.getPodcastDetail().getName() + "");
        txtGenreText.setText(ent.getPodcastDetail().getGenre() + "");
        txtNarratorText.setText(ent.getPodcastDetail().getArtistName() + "");
        sbProgress.setPadding(0, 0, 0, 0);
        bindPodcastPlaylist(ent.getPodcastEpisodeList());
    }

    private void setupUIViewsBook(BookDetailEnt ent) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        ImageLoader.getInstance().displayImage(ent.getImageUrl(), imgItemCover, options);
        ImageLoader.getInstance().displayImage(ent.getImageUrl(), imgItemPic, options);
        ImageLoader.getInstance().displayImage(ent.getImageUrl(), imgPlayerCover, options);

        if (ent.getRating() == -1) {
            rbRating.setScore(0);
        } else {
            rbRating.setScore((float) ent.getRating());
        }
        txtTitle.setText(ent.getBookName() + "");
        txtGenreText.setText(ent.getGenre() + "");
        txtNarratorText.setText(ent.getNarratorName() + "");
        sbProgress.setPadding(0, 0, 0, 0);
        txtDuration.setVisibility(View.VISIBLE);
        txtDurationText.setVisibility(View.VISIBLE);
        txtDurationText.setText(getBookDurationText(ent.getDuration()));
        bindBookPlaylist(ent.getChapters());
    }

    private void bindBookPlaylist(BookChaptersEnt chapters) {
        initializePlaybackController();
        initializeSeekbar();
        mUserPlaylist = new ArrayList<>();
        if (bookDetailEnt.getIsPurchased() && chapters.getChapter().size() > 1) {
            // chapters.getChapter().remove(0);
        }
        for (BooksChapterItemEnt tracks : chapters.getChapter()
                ) {
            String path = AppConstants.DOWNLOAD_PATH + File.separator + bookDetailEnt.getBookName() + File.separator + tracks.getChapterID().replaceAll("\\s+", "");
            if (new File(path).exists()) {
                mUserPlaylist.add(new PlayListModel(AppConstants.FILE_PATH + path, "", true));


            } else {
                mUserPlaylist.add(new PlayListModel("", tracks.getFilePath(), false));
            }

        }
        mPlayerAdapter.loadPlayList(mUserPlaylist, startingIndex);
        setItemName(startingIndex);
    }

    private void bindPodcastPlaylist(ArrayList<PodcastEpisodeEnt> trackList) {
        initializePlaybackController();
        initializeSeekbar();
        mUserPlaylist = new ArrayList<>();
        for (PodcastEpisodeEnt tracks : trackList
                ) {
            String path = getDownloadPath(tracks.getPodcastId(), tracks.getPodcastEpisodeID(), AppConstants.TAB_PODCAST);
            if (new File(path).exists()) {
                mUserPlaylist.add(new PlayListModel(AppConstants.FILE_PATH + path, "", true));
            } else {
             /*   if (podcastDetailEnt.getPodcastDetail().getEpisodesAdded()) {
                    mUserPlaylist.add(new PlayListModel("", String.format("%s:%s/%s/mp3:%s/playlist.m3u8", podcastDetailEnt..WowzaURL, podcastDetailEnt.getWowzaPort(),
                            podcastDetailEnt.getWowzaAppName(), tracks.getFilePath()), false));
                } else {
                    mUserPlaylist.add(new PlayListModel("", tracks.getFilePath(), false));
                }*/
                mUserPlaylist.add(new PlayListModel("", tracks.getFilePath(), false));
            }
        }
        mPlayerAdapter.loadPlayList(mUserPlaylist, startingIndex);
        setItemName(startingIndex);

    }

    private void setNameOnTextViews(String itemName, String albumName) {
        txtItemName.setText(itemName + "");
        txtPlayingItemName.setText(itemName + "");
        txtItemAlbum.setText(albumName + "");
        txtPlayingItemAlbum.setText(albumName + "");
    }

    private void setItemName(int index) {
        String name = "";
        if (playerType.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
            if (index < mUserPlaylist.size()) {
                name = podcastDetailEnt.getPodcastEpisodeList().get(index).getEpisodeTitle();
                if (name == null)
                    name = getResString(R.string.episode_no) + " " + (mPlayerAdapter.getCurrentItemIndex() + 1);
                setNameOnTextViews(name, podcastDetailEnt.getPodcastDetail().getName());
                if (itemChangeListener != null) {
                    itemChangeListener.onItemChanged(index);
                }
            }
        } else if (playerType.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
            if (index < mUserPlaylist.size()) {

                if (!bookDetailEnt.getIsPurchased()) {
                    name = getResString(R.string.preview);
                } else {
                    name = getResString(R.string.chapters) + " " + (index + 1);
                }
                if (name == null)
                    name = getResString(R.string.chapters) + " " + (mPlayerAdapter.getCurrentItemIndex() + 1);
                setNameOnTextViews(name, bookDetailEnt.getBookName());
                if (itemChangeListener != null) {
                    itemChangeListener.onItemChanged(index);
                }
            }
        } else if (playerType.equalsIgnoreCase(AppConstants.TAB_NEWS)) {
            if (index < mUserPlaylist.size()) {
                name = newsEpisodeEnt.getNewsepisodeslist().get(index).getEpisodetitle();
                if (name == null)
                    name = getResString(R.string.episode_no) + " " + (mPlayerAdapter.getCurrentItemIndex() + 1);
                setNameOnTextViews(name, newsEpisodeEnt.getDetailEnt().getName());
                if (itemChangeListener != null) {
                    itemChangeListener.onItemChanged(index);
                }
            }
        }
        changeDownloadButtonState(index);
    }

    private void changeDownloadButtonState(int index) {
        if (mUserPlaylist.get(index).isFromPath()) {
            btnDownload.setEnabled(false);
            btnDownload.setAlpha(.5f);
        } else {
            btnDownload.setEnabled(true);
            btnDownload.setAlpha(1f);
        }
    }


    private void setEpisodeFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.replace(R.id.items, fragment);
        transaction.commit();
    }


    //region Common Item that do not need Change
    private void setItemChangeListener(PlayerItemChangeListener itemChangeListener) {
        this.itemChangeListener = itemChangeListener;
    }

    public void setCheckChangeListener(FavoriteCheckChangeListener checkChangeListener) {
        this.checkChangeListener = checkChangeListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void ResponseSuccess(final Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_ADVERTISEMENT:
                final ArrayList<AdvertisementEnt> results = (ArrayList<AdvertisementEnt>) result;
                if (results != null && results.size() > 0) {
                    getMainActivity().showAdvertisementImage();
                    final AudioPlayer mMediaPlayer = new AudioPlayer(getContext());
                    mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                        @Override
                        public void onCompletion() {
                            getMainActivity().hideAdvertisementImage();
                            mMediaPlayer.release();
                            bindScreenData();

                        }
                    });

                    mMediaPlayer.setOnPreparedListener(mMediaPlayer::start);
                    AdvertisementEnt ent = results.get(0);
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.setDataSource(Uri.parse(ent.getAudioUrl() + ent.getAudioPath()));
                    mMediaPlayer.prepareAsync();
                } else {
                    bindScreenData();
                }
                break;
        }
    }

    private void setHeader() {
        if (getChildFragmentManager().findFragmentById(R.id.items) != null) {
            getChildFragmentManager().beginTransaction().
                    remove(getChildFragmentManager().findFragmentById(R.id.items)).commit();
        }
        if (playerType.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
            btnFavorite.setOnCheckedChangeListener(null);
            btnFavorite.setChecked(podcastDetailEnt.getPodcastDetail().getFavorite());
            btnFavorite.setOnCheckedChangeListener(podcastFavoriteCheckListener);
           /* EpisodeListingFragment fragment = new EpisodeListingFragment();
            fragment.setListItemListener(this);
            setItemChangeListener(fragment);
           // fragment.setTrackList(podcastDetailEnt.getPodcastEpisodeList());
            setEpisodeFragment(fragment);
            if (itemChangeListener != null) {
                callForItemChange();
            }*/
        } else if (playerType.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
            if (bookDetailEnt.getIsPurchased()) {
                BookChaptersListingFragment fragment = new BookChaptersListingFragment();
                fragment.setListItemListener(this);
                setItemChangeListener(fragment);
                fragment.setTrackList(new ArrayList<>(bookDetailEnt.getChapters().getChapter().subList(0, bookDetailEnt.getChapters().getChapter().size())));
                setEpisodeFragment(fragment);
                if (itemChangeListener != null) {
                    callForItemChange();
                }
            }
            btnFavorite.setChecked(bookDetailEnt.getIsFavorite());
            btnFavorite.setOnCheckedChangeListener(bookFavoriteListener);
        } else if (playerType.equalsIgnoreCase(AppConstants.TAB_NEWS)) {

            //  btnFavorite.setChecked(newsEpisodeEnt.isFavourite());
            btnFavorite.setOnCheckedChangeListener(newsFavoriteListener);
        }
    }

    private void callForItemChange() {
        Handler handler = new Handler();
        handler.postDelayed(() -> itemChangeListener.onItemChanged(startingIndex), 1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnPlayerPlay = (ImageView) view.findViewById(R.id.btn_player_play);
        addSlidingListener();
    }


    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getMainActivity().clearFlagKeepScreenOn();
        if (mPlayerAdapter != null)
            mPlayerAdapter.release();
        unbinder.unbind();
    }

    public void resetPlayer() {
        getMainActivity().clearFlagKeepScreenOn();
        if (mPlayerAdapter != null) {
            mPlayerAdapter.release();
        }
    }

    public void startPlaying() {
        setHeader();
        getMainActivity().setFlagKeepScreenOn();
       /* if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            playAdvertisement();
        } else {*/
        bindScreenData();
        //}

    }

    private void bindScreenData() {
        getDetails(playerType);
    }

    private void playAdvertisement() {
        serviceHelper.enqueueCall(webService.getAllAdvertisement(prefHelper.getUserToken()), WebServiceConstants.GET_ADVERTISEMENT);
    }

    private void addSlidingListener() {
        getMainActivity().mSlidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                float offset = Math.abs((slideOffset - 1));
                containerPlayer.setAlpha(offset);
                titleBarView.setAlpha(slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                switch (newState) {
                    case HIDDEN:
                        break;
                    case DRAGGING:
                        break;
                    case EXPANDED:
                        getDockActivity().setFileDownloadListener(fileDownloadListener);
                        btnFavorite.setVisibility(View.INVISIBLE);
                      /*  btnPlayerPlay.setVisibility(View.GONE);
                        pbBottomBuffering.setVisibility(View.GONE);*/
                        break;
                    case ANCHORED:
                        getMainActivity().mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    case COLLAPSED:
                        btnFavorite.setVisibility(View.GONE);
                       /* btnPlayerPlay.setVisibility(View.VISIBLE);
                        pbBottomBuffering.setVisibility(View.GONE);*/
                        break;
                }
            }
        });

    }

    @OnClick({R.id.btn_volume, R.id.btn_backward, R.id.btn_play, R.id.btn_forward, R.id.btnLeft, R.id.btn_player_play, R.id.btn_close, R.id.container_player, R.id.background, R.id.btnShare, R.id.btn_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_volume:
                AudioManager audioManager = (AudioManager) getDockActivity().getSystemService(Context.AUDIO_SERVICE);
                if (audioManager != null) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
                }
                break;
            case R.id.btn_backward:
                hasPlaylistComplete = false;
                mPlayerAdapter.playPrevious();
                break;
            case R.id.btn_play:
                checkCanPlay();
                break;
            case R.id.btn_forward:
                if (!prefHelper.isContinous()) {
                    hasPlaylistComplete = false;
                }
                mPlayerAdapter.playNext();
                break;
            case R.id.btnLeft:
                getMainActivity().mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                break;
            case R.id.btn_player_play:
                checkCanPlay();
                break;
            case R.id.btn_close:
                getMainActivity().hideBottomPlayer();
                break;

            case R.id.btnShare:
                break;
            case R.id.btn_download:
                // TODO: 7/24/18 Download for Items
                if (playerType.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
                    BooksChapterItemEnt ent = bookDetailEnt.getChapters().getChapter().get(mPlayerAdapter.getCurrentItemIndex());
                    assert ent != null;
                    String path = AppConstants.DOWNLOAD_PATH + File.separator + bookDetailEnt.getBookName() + File.separator +
                            ent.getChapterID().replaceAll("\\s+", "");
                    if (!new File(path).exists()) {
                        getDockActivity().addDownload(ent.getFilePath(), ent.getChapterID(), getBookDownloadName(ent.getChapterNumber()), bookDetailEnt.getBookName(), bookDetailEnt);
                    }

                } else if (playerType.equalsIgnoreCase(AppConstants.TAB_NEWS)) {
                    NewsEpisodeEnt item = newsEpisodeEnt.getNewsepisodeslist().get(mPlayerAdapter.getCurrentItemIndex());
                    getDockActivity().addDownload(item.getFilepath(),
                            getDownloadPath(newsEpisodeEnt.getDetailEnt().getNewsID(), item.getNewsepisodeid(), AppConstants.TAB_NEWS),
                            item.getNewsepisodeid() + "",
                            item.getEpisodetitle(),
                            newsEpisodeEnt.getDetailEnt().getName(),
                            item
                    );

                } else if (playerType.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
                    PodcastEpisodeEnt item = podcastDetailEnt.getPodcastEpisodeList().get(mPlayerAdapter.getCurrentItemIndex());
                    getDockActivity().addDownload(item.getFilePath(),
                            getDownloadPath(item.getPodcastId(), item.getPodcastEpisodeID(), AppConstants.TAB_PODCAST),
                            item.getPodcastEpisodeID(),
                            item.getEpisodeTitle(),
                            podcastDetailEnt.getPodcastDetail().getName(),
                            item
                    );
                }
                break;

        }
    }

    private String getBookDownloadName(Integer number) {
        return String.format(Locale.ENGLISH, "%s %s %d", bookDetailEnt.getBookName(), getResString(R.string.chapter), number);
    }

    private void checkCanPlay() {
        if (mPlayerAdapter.isReadyForPlay()) {
            if (!mPlayerAdapter.isPlaying() && !hasPlaylistComplete) {
                performPlayClick(hasItemPlayCompleted);
            } else {
                performPauseClick();
            }
        } else {
            if (hasPlaylistComplete) {

            } else {
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.buffering_text));
            }
        }
    }

    private String getTotalDuaration(long duration) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        Long elapsedMinutes = duration / minutesInMilli;
        duration = duration % minutesInMilli;

        Long elapsedSeconds = duration / secondsInMilli;
        return elapsedMinutes == 0 ? elapsedSeconds.intValue() + "s" : elapsedMinutes.intValue() + "m";
    }

    private String getBookDurationText(Integer secound) {
        int minutes = (secound % 3600) / 60;
        int hours = secound / 3600;
        if (minutes > 0) {
            return (minutes) + " mins";
        } else if (secound > 0) {
            return (secound) + " sec";
        } else {
            return "-";
        }

    }

    private void initializePlaybackController() {
        MediaPlayerHolder mMediaPlayerHolder = new MediaPlayerHolder(getDockActivity());
        Log.d(TAG, "initializePlaybackController: created MediaPlayerHolder");
        mMediaPlayerHolder.setPlaybackInfoListener(new PlaybackListener());
        mPlayerAdapter = mMediaPlayerHolder;
        Log.d(TAG, "initializePlaybackController: MediaPlayerHolder progress callback set");
    }

    private void showPlayButton() {
        btnPlay.setVisibility(View.VISIBLE);
        btnPlayerPlay.setVisibility(View.VISIBLE);
    }

    private void hidePlayButton() {
        btnPlay.setVisibility(View.GONE);
        btnPlayerPlay.setVisibility(View.GONE);
    }

    private void setPlayButtonResource() {
        btnPlay.setImageResource(R.drawable.play_icon_white);
        btnPlayerPlay.setImageResource(R.drawable.ic_play_arrow);
    }

    private void setPauseButtonResource() {
        btnPlay.setImageResource(R.drawable.pause_icon_big);
        btnPlayerPlay.setImageResource(R.drawable.pause_icon_small);
    }

    private void initializeSeekbar() {
        sbProgress.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int userSelectedPosition = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = false;
                        mPlayerAdapter.seekTo(userSelectedPosition);
                    }
                });
    }

    private void performPlayClick(boolean isReplay) {
        hasPlaylistComplete = false;
        hasItemPlayCompleted = false;
        if (isReplay) {
            mPlayerAdapter.replay();
        } else {
            mPlayerAdapter.play();
        }
        pbBuffering.setVisibility(View.GONE);
        pbBottomBuffering.setVisibility(View.GONE);
        showPlayButton();
        setPauseButtonResource();
       /* btnPlay.setVisibility(View.VISIBLE);
        btnPlay.setImageResource(R.drawable.pause_icon_big);*/

    }

    private void performPauseClick() {
        mPlayerAdapter.pause();
        /*        btnPlay.setImageResource(R.drawable.play_icon_white);*/
        setPlayButtonResource();

    }

    @Override
    public void onTrackSelected(int index) {
        if (mPlayerAdapter != null) {
            hasPlaylistComplete = false;
            mPlayerAdapter.playIndex(index);
            //  getMainActivity().closeDrawer();
            setItemName(index);
        }
    }


    @Override
    public void onFavoriteCheckChange(boolean check, int ID) {
        if (this.ID != null && ID == this.ID) {
            if (playerType.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
                btnFavorite.setOnCheckedChangeListener(null);
                btnFavorite.setChecked(check);
                btnFavorite.setOnCheckedChangeListener(podcastFavoriteCheckListener);
            } else if (playerType.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
                btnFavorite.setOnCheckedChangeListener(null);
                btnFavorite.setChecked(check);
                btnFavorite.setOnCheckedChangeListener(bookFavoriteListener);
            } else if (playerType.equalsIgnoreCase(AppConstants.TAB_NEWS)) {
                btnFavorite.setOnCheckedChangeListener(null);
                btnFavorite.setChecked(check);
                Log.e(TAG, "onFavoriteCheckChange: Fron Players");
                btnFavorite.setOnCheckedChangeListener(newsFavoriteListener);
            }
        }
    }


    public class PlaybackListener extends PlaybackInfoListener {
        @Override
        public void onLogUpdated(String message) {
            Log.e(TAG, message);
        }

        @Override
        public void onDurationChanged(int duration) {
            sbProgress.setMax(duration);
            txtTimeTotal.setText(getTotalDuaration(duration));
            Log.d(TAG, String.format("setPlaybackDuration: setMax(%d)", duration));
        }

        @Override
        public void onPositionChanged(int position, long remainingMinute, long remainingSecond) {
            if (!mUserIsSeeking) {
               /* if (position == 0) {
                    sbProgress.setProgress(position);
                } else*/
                {
                    ObjectAnimator animation = ObjectAnimator.ofInt(sbProgress, "progress", sbProgress.getProgress(), position);
                    animation.setDuration(990);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.start();
                }
                if (remainingMinute <= 0 && remainingSecond <= 0) {
                    txtRemainingTime.setText(String.format(Locale.ENGLISH, "%02d:%02d", remainingMinute, remainingSecond));
                } else {
                    txtRemainingTime.setText(String.format(Locale.ENGLISH, "-%02d:%02d", remainingMinute, remainingSecond));
                }
                Log.d(TAG, String.format("setPlaybackPosition: setProgress(%d)", position));
            }

        }

        @Override
        public void onStateChanged(int state) {
            String stateToString = PlaybackInfoListener.convertStateToString(state);
            onLogUpdated(String.format("onStateChanged(%s)", stateToString));
        }

        @Override
        public void onPlaybackCompleted() {
            pbBuffering.setVisibility(View.GONE);
            pbBottomBuffering.setVisibility(View.GONE);
            showPlayButton();
            setPlayButtonResource();
            onPositionChanged(0, 0, 0);
            if (prefHelper.isContinous()) {
                mPlayerAdapter.playNext();
            } else {
                hasItemPlayCompleted = true;
            }

        }

        @Override
        public void onPlayListCompleted() {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.playlist_complete));
            mPlayerAdapter.reset();
            sbProgress.setProgress(0);
            hasPlaylistComplete = true;
            pbBuffering.setVisibility(View.GONE);
            pbBottomBuffering.setVisibility(View.GONE);
            showPlayButton();
            setPlayButtonResource();
//            getMainActivity().clearFlagKeepScreenOn();
        }

        @Override
        public void isReadyToPlay(int currentItem) {
            if (hasPlaylistComplete) {
                return;
            } else {
                performPlayClick(hasItemPlayCompleted);
            }
        }

        @Override
        public void onPlaybackInfo(int what, int extra) {
            switch (what) {
                case Player.STATE_BUFFERING:
                    pbBuffering.setVisibility(View.VISIBLE);
                    pbBottomBuffering.setVisibility(View.VISIBLE);
                    hidePlayButton();
                    break;
                case Player.STATE_READY:
                    pbBuffering.setVisibility(View.GONE);
                    pbBottomBuffering.setVisibility(View.GONE);
                    showPlayButton();
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    pbBuffering.setVisibility(View.VISIBLE);
                    pbBottomBuffering.setVisibility(View.VISIBLE);
                    hidePlayButton();
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    pbBuffering.setVisibility(View.GONE);
                    pbBottomBuffering.setVisibility(View.GONE);
                    showPlayButton();
                    break;
                case State.NEXT:
                    pbBuffering.setVisibility(View.VISIBLE);
                    pbBottomBuffering.setVisibility(View.VISIBLE);
                    hidePlayButton();
                    setItemName(mPlayerAdapter.getCurrentItemIndex());
                    //  onPositionChanged(0,0,0);
                    break;
                case State.PREVIOUS:
                    pbBuffering.setVisibility(View.VISIBLE);
                    pbBottomBuffering.setVisibility(View.VISIBLE);
                    hidePlayButton();
                    setItemName(mPlayerAdapter.getCurrentItemIndex());
                    //onPositionChanged(0,0,0);
                    break;
            }
        }
    }
//endregion
}