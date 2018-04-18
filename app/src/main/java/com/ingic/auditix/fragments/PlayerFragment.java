package com.ingic.auditix.fragments;

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
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.google.android.exoplayer2.Player;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.AdvertisementEnt;
import com.ingic.auditix.entities.BookChaptersEnt;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.entities.BooksChapterItemEnt;
import com.ingic.auditix.entities.NewsEpisodeEnt;
import com.ingic.auditix.entities.PodcastDetailEnt;
import com.ingic.auditix.entities.PodcastTrackEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.InternetHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.PlayerItemChangeListener;
import com.ingic.auditix.interfaces.TrackListItemListener;
import com.ingic.auditix.media.MediaPlayerHolder;
import com.ingic.auditix.media.PlayListModel;
import com.ingic.auditix.media.PlaybackInfoListener;
import com.ingic.auditix.media.PlayerAdapter;
import com.ingic.auditix.ui.slidinglayout.SlidingUpPanelLayout;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRatingBar;
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
public class PlayerFragment extends BaseFragment implements TrackListItemListener {

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
    private PlayerAdapter mPlayerAdapter;
    private boolean mUserIsSeeking = false;
    private boolean hasPlaylistComplete = false;
    private ArrayList<PlayListModel> mUserPlaylist;
    private PodcastDetailEnt podcastDetailEnt;
    private NewsEpisodeEnt newsEpisodeEnt;
    private BookDetailEnt bookDetailEnt;
    private Integer ID;
    private String playerType = "";

    private int startingIndex = 0;
    private PlayerItemChangeListener itemChangeListener;

    //endregion
    //region Fragment Lifecycles
    public static PlayerFragment newInstance(PodcastDetailEnt podcastDetail, Integer ID, String playerType,
                                             BookDetailEnt bookDetailEnt, NewsEpisodeEnt newsEpisodeEnt, int startingIndex) {
        Bundle args = new Bundle();

        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        fragment.setContent(podcastDetail, ID, playerType, bookDetailEnt, newsEpisodeEnt, startingIndex);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
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

                    mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                        @Override
                        public void onPrepared() {
                            mMediaPlayer.start();
                        }
                    });
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

    private void setHeadBar() {
        if (playerType.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
            btnFavorite.setChecked(podcastDetailEnt.isFavorite());

            btnFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (prefHelper.isGuest()) {
                        showGuestMessage();
                    } else
                        serviceHelper.enqueueCall(webService.changeFavoriteStatus(ID, isChecked, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
                }
            });


            EpisodeListingFragment fragment = new EpisodeListingFragment();
            fragment.setListItemListener(this);
            setItemChangeListener(fragment);
            fragment.setTrackList(podcastDetailEnt.getTrackList());
            setEpisodeFragment(fragment);
            /*titleBar.showListingFragment(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMainActivity().isNavigationGravityRight = true;
                    getMainActivity().getDrawerLayout().openDrawer(Gravity.RIGHT);
                }
            });*/
            if (itemChangeListener != null) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        itemChangeListener.onItemChanged(startingIndex);
                    }
                }, 1000);
            }
        } else if (playerType.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
            if (bookDetailEnt.getIsPurchased()) {
                BookChaptersListingFragment fragment = new BookChaptersListingFragment();
                fragment.setListItemListener(this);
                setItemChangeListener(fragment);
                fragment.setTrackList(new ArrayList<>(bookDetailEnt.getChapters().getChapter().subList(0, bookDetailEnt.getChapters().getChapter().size())));
                setEpisodeFragment(fragment);
                /*titleBar.showListingFragment(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getMainActivity().isNavigationGravityRight = true;
                        getMainActivity().getDrawerLayout().openDrawer(Gravity.RIGHT);
                    }
                });*/
                if (itemChangeListener != null) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            itemChangeListener.onItemChanged(startingIndex);
                        }
                    }, 1000);
                }
            }
            btnFavorite.setChecked(bookDetailEnt.getIsFavorite());
            btnFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (prefHelper.isGuest()) {
                        showGuestMessage();
                    } else {
                        if (isChecked) {
                            serviceHelper.enqueueCall(webService.AddBookToFavorite(ID, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
                        } else {
                            serviceHelper.enqueueCall(webService.RemoveBookFromFavorite(ID, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
                        }
                    }
                }
            });
        } else if (playerType.equalsIgnoreCase(AppConstants.TAB_NEWS)) {
            if (getChildFragmentManager().findFragmentById(R.id.items) != null) {
                getChildFragmentManager().beginTransaction().
                        remove(getChildFragmentManager().findFragmentById(R.id.items)).commit();
            }
            btnFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (prefHelper.isGuest()) {
                        showGuestMessage();
                    } else {
                        if (isChecked) {
                            serviceHelper.enqueueCall(webService.favoriteNews(ID, prefHelper.getUserToken()), WebServiceConstants.FAVORITE_NEWS);
                        } else {
                            serviceHelper.enqueueCall(webService.unFavoriteNews(ID, prefHelper.getUserToken()), WebServiceConstants.UNFAVORITE_NEWS);
                        }
                    }
                }
            });
        }
    }

    private void setEpisodeFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.replace(R.id.items, fragment);
        transaction.commit();
    }

    private void setItemChangeListener(PlayerItemChangeListener itemChangeListener) {
        this.itemChangeListener = itemChangeListener;
    }

    public void setContent(PodcastDetailEnt podcastDetail, Integer trackID, String playerType, BookDetailEnt bookDetailEnt, NewsEpisodeEnt newsEpisodeEnt, int startingIndex) {
        this.podcastDetailEnt = podcastDetail;
        this.ID = trackID;
        this.playerType = playerType;
        this.bookDetailEnt = bookDetailEnt;
        this.newsEpisodeEnt = newsEpisodeEnt;
        this.startingIndex = startingIndex;
    }
    //endregion

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
        setHeadBar();
        getMainActivity().setFlagKeepScreenOn();
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            playAdvertisement();
        } else {
            bindScreenData();
        }

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
                Log.e(TAG, "onPanelSlide: offset :  " + offset);
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
                        btnFavorite.setVisibility(View.VISIBLE);
                      /*  btnPlayerPlay.setVisibility(View.GONE);
                        pbBottomBuffering.setVisibility(View.GONE);*/
                        break;
                    case ANCHORED:
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

    private void getDetails(String type) {
        if (type.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
            setupUIViewsPodcast(podcastDetailEnt);
        } else if (type.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
            setupUIViewsBook(bookDetailEnt);
        } else if (type.equalsIgnoreCase(AppConstants.TAB_NEWS)) {
            setupUIViewsNews(newsEpisodeEnt);
        }

        txtPlayingItemAlbum.setSelected(true);
        txtPlayingItemName.setSelected(true);
        txtItemName.setSelected(true);
        txtItemAlbum.setSelected(true);

    }

    private void setupUIViewsNews(NewsEpisodeEnt ent) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        ImageLoader.getInstance().displayImage(ent.getImage_url(), imgItemCover, options);
        ImageLoader.getInstance().displayImage(ent.getImage_url(), imgItemPic, options);
        ImageLoader.getInstance().displayImage(ent.getImage_url(), imgPlayerCover, options);
        txtTitle.setText(ent.getTitle() + "");
        sbProgress.setPadding(0, 0, 0, 0);
        txtDuration.setVisibility(View.VISIBLE);
        txtDurationText.setVisibility(View.VISIBLE);
        txtDurationText.setText(getBookDurationText(ent.getDuration()));
        initializePlaybackController();
        initializeSeekbar();
        mUserPlaylist = new ArrayList<>();
        String path = AppConstants.DOWNLOAD_PATH + File.separator + ent.get_id() + File.separator + ent.getTitle().replaceAll("\\s+", "") + ".mp3";
        if (new File(path).exists()) {
            mUserPlaylist.add(new PlayListModel("", AppConstants.FILE_PATH + path, false));
        } else {
            mUserPlaylist.add(new PlayListModel("", ent.getAudio_link(), false));
        }
        mPlayerAdapter.loadPlayList(mUserPlaylist, startingIndex);
        setItemName(startingIndex);


    }

    private void setupUIViewsPodcast(PodcastDetailEnt ent) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        ImageLoader.getInstance().displayImage(ent.getImage(), imgItemCover, options);
        ImageLoader.getInstance().displayImage(ent.getImage(), imgItemPic, options);
        ImageLoader.getInstance().displayImage(ent.getImage(), imgPlayerCover, options);
        if (podcastDetailEnt.getRating() == -1) {
            rbRating.setScore(0);
        } else {
            rbRating.setScore((float) podcastDetailEnt.getRating());
        }
        txtTitle.setText(podcastDetailEnt.getTitle() + "");
        txtGenreText.setText(podcastDetailEnt.getGenre() + "");
        txtNarratorText.setText(podcastDetailEnt.getAuthor() + "");
        sbProgress.setPadding(0, 0, 0, 0);
        bindPodcastPlaylist(ent.getTrackList());
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
            String path = AppConstants.DOWNLOAD_PATH + File.separator + bookDetailEnt.getBookName() + File.separator + tracks.getAudioUrl().replaceAll("\\s+", "");
            if (new File(path).exists()) {
                mUserPlaylist.add(new PlayListModel("", AppConstants.FILE_PATH + path, false));

            } else {
                mUserPlaylist.add(new PlayListModel("", String.format("%s:%s/%s/mp3:%s/playlist.m3u8", chapters.getWowzaURL(), chapters.getWowzaPort(),
                        chapters.getWowzaAppName(), tracks.getAudioUrl()), false));
            }

        }
        mPlayerAdapter.loadPlayList(mUserPlaylist, startingIndex);
        setItemName(startingIndex);
    }

    private void bindPodcastPlaylist(ArrayList<PodcastTrackEnt> trackList) {
        initializePlaybackController();
        initializeSeekbar();
        mUserPlaylist = new ArrayList<>();
        for (PodcastTrackEnt tracks : trackList
                ) {
            String path = AppConstants.DOWNLOAD_PATH + File.separator + podcastDetailEnt.getTitle() + File.separator + tracks.getName().replaceAll("\\s+", "") + ".mp3";
            if (new File(path).exists()) {
                mUserPlaylist.add(new PlayListModel("", AppConstants.FILE_PATH + path, false));
            } else {
                if (podcastDetailEnt.isEpisodeAdded()) {
                    mUserPlaylist.add(new PlayListModel("", String.format("%s:%s/%s/mp3:%s/playlist.m3u8", podcastDetailEnt.WowzaURL, podcastDetailEnt.getWowzaPort(),
                            podcastDetailEnt.getWowzaAppName(), tracks.getFileUrl()), false));
                } else {
                    mUserPlaylist.add(new PlayListModel("", tracks.getFileUrl(), false));
                }
            }
        }
        mPlayerAdapter.loadPlayList(mUserPlaylist, startingIndex);
        setItemName(startingIndex);

    }

    @OnClick({R.id.btn_volume, R.id.btn_backward, R.id.btn_play, R.id.btn_forward, R.id.btnLeft, R.id.btn_player_play, R.id.btn_close})
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
        }
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

    private void setNameOnTextViews(String itemName, String albumName) {
        txtItemName.setText(itemName + "");
        txtPlayingItemName.setText(itemName + "");
        txtItemAlbum.setText(albumName + "");
        txtPlayingItemAlbum.setText(albumName + "");
    }

    private void setItemName(int index) {
        if (playerType.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
            if (index < mUserPlaylist.size()) {
                setNameOnTextViews(podcastDetailEnt.getTrackList().get(index).getName(), podcastDetailEnt.getTitle());
                if (itemChangeListener != null) {
                    itemChangeListener.onItemChanged(index);
                }
            }
        } else if (playerType.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
            if (index < mUserPlaylist.size()) {
                String name = "";
                if (!bookDetailEnt.getIsPurchased()) {
                    name = getResString(R.string.preview);
                } else {
                    name = getResString(R.string.chapters) + " " + (index + 1);
                }
                setNameOnTextViews(name, bookDetailEnt.getBookName());
                if (itemChangeListener != null) {
                    itemChangeListener.onItemChanged(index);
                }
            }
        } else if (playerType.equalsIgnoreCase(AppConstants.TAB_NEWS)) {
            if (index < mUserPlaylist.size()) {
                setNameOnTextViews(newsEpisodeEnt.getTitle(), newsEpisodeEnt.getSource_name());
                if (itemChangeListener != null) {
                    itemChangeListener.onItemChanged(index);
                }
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

}