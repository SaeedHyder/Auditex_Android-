package com.ingic.auditix.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.google.android.exoplayer2.Player;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookChaptersEnt;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.entities.BooksChapterItemEnt;
import com.ingic.auditix.entities.PodcastDetailEnt;
import com.ingic.auditix.entities.PodcastTrackEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.PlayerItemChangeListener;
import com.ingic.auditix.interfaces.TrackListItemListener;
import com.ingic.auditix.media.MediaPlayerHolder;
import com.ingic.auditix.media.PlayListModel;
import com.ingic.auditix.media.PlaybackInfoListener;
import com.ingic.auditix.media.PlayerAdapter;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRatingBar;
import com.ingic.auditix.ui.views.TitleBar;
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
    private static final String TAG = "PlayerFragment";
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
    private PlayerAdapter mPlayerAdapter;
    private boolean mUserIsSeeking = false;
    private boolean hasPlaylistComplete = false;
    private ArrayList<PlayListModel> mUserPlaylist;
    private PodcastDetailEnt podcastDetailEnt;
    private BookDetailEnt bookDetailEnt;
    private Integer ID;
    private String playerType = "";
    private int startingIndex = 0;
    //endregion

    //region Fragment Lifecycles
    public static PlayerFragment newInstance(PodcastDetailEnt podcastDetail, Integer ID, String playerType, BookDetailEnt bookDetailEnt, int startingIndex) {
        Bundle args = new Bundle();

        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        fragment.setContent(podcastDetail, ID, playerType, bookDetailEnt, startingIndex);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    public void setItemChangeListener(PlayerItemChangeListener itemChangeListener) {
        this.itemChangeListener = itemChangeListener;
    }

    private PlayerItemChangeListener itemChangeListener;
    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.playing_now));
        titleBar.showBackButton();
        titleBar.addBackground();
        if (playerType.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
            getMainActivity().titleBar.showFavoriteButton(podcastDetailEnt.isFavorite(), new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    serviceHelper.enqueueCall(webService.changeFavoriteStatus(ID, isChecked, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
                }
            });
            EpisodeListingFragment fragment = new EpisodeListingFragment();
            fragment.setListItemListener(this);
            setItemChangeListener(fragment);
            fragment.setTrackList(podcastDetailEnt.getTrackList());
            getMainActivity().setRightSideFragment(fragment);
            titleBar.showListingFragment(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMainActivity().isNavigationGravityRight = true;
                    getMainActivity().getDrawerLayout().openDrawer(Gravity.RIGHT);
                }
            });
            if (itemChangeListener!=null){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        itemChangeListener.onItemChanged(startingIndex);
                    }
                },1000);
            }
        } else if (playerType.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
            if (bookDetailEnt.getIsPurchased()) {
                BookChaptersListingFragment fragment = new BookChaptersListingFragment();
                fragment.setListItemListener(this);
                setItemChangeListener(fragment);
                fragment.setTrackList(new ArrayList<>(bookDetailEnt.getChapters().getChapter().subList(0, bookDetailEnt.getChapters().getChapter().size())));
                getMainActivity().setRightSideFragment(fragment);
                titleBar.showListingFragment(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getMainActivity().isNavigationGravityRight = true;
                        getMainActivity().getDrawerLayout().openDrawer(Gravity.RIGHT);
                    }
                });
                if (itemChangeListener!=null){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            itemChangeListener.onItemChanged(startingIndex);
                        }
                    },1000);
                }
            }
            getMainActivity().titleBar.showFavoriteButton(bookDetailEnt.getIsFavorite(), new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        serviceHelper.enqueueCall(webService.AddBookToFavorite(ID, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
                    } else {
                        serviceHelper.enqueueCall(webService.RemoveBookFromFavorite(ID, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
                    }
                }
            });
        }
    }

    private void setContent(PodcastDetailEnt podcastDetail, Integer trackID, String playerType, BookDetailEnt bookDetailEnt, int startingIndex) {
        this.podcastDetailEnt = podcastDetail;
        this.ID = trackID;
        this.playerType = playerType;
        this.bookDetailEnt = bookDetailEnt;
        this.startingIndex = startingIndex;
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
        getDetails(playerType);

        getMainActivity().setFlagKeepScreenOn();
    }
    //endregion

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

    private void getDetails(String type) {
        if (type.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
            setupUIViewsPodcast(podcastDetailEnt);
        } else if (type.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
            setupUIViewsBook(bookDetailEnt);
        }

    }

    private void setupUIViewsPodcast(PodcastDetailEnt ent) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        ImageLoader.getInstance().displayImage(ent.getImage(), imgItemCover, options);
        ImageLoader.getInstance().displayImage(ent.getImage(), imgItemPic, options);
        txtPlayingItemAlbum.setSelected(true);
        txtPlayingItemName.setSelected(true);
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
        txtPlayingItemAlbum.setSelected(true);
        txtPlayingItemName.setSelected(true);
        if (ent.getRating() == -1) {
            rbRating.setScore(0);
        } else {
            rbRating.setScore((float) ent.getRating());
        }
        txtTitle.setText(ent.getBookName() + "");
        txtGenreText.setText(ent.getGenre() + "");
        txtNarratorText.setText(ent.getAuthorName() + "");
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
            String path = AppConstants.DOWNLOAD_PATH + File.separator + tracks.getAudioUrl().replaceAll("\\s+", "");
            if (new File(path).exists()) {
                mUserPlaylist.add(new PlayListModel("", AppConstants.FILE_PATH + path, false));

            } else {
                mUserPlaylist.add(new PlayListModel("", String.format("%s:%s/%s/mp3:%s/playlist.m3u8", chapters.getWowzaURL(), chapters.getWowzaPort(),
                        chapters.getWowzaAppName(), tracks.getAudioUrl()), false));
            }

        }
        mPlayerAdapter.loadPlayList(mUserPlaylist,startingIndex);
        setItemName(startingIndex);
    }

    private void bindPodcastPlaylist(ArrayList<PodcastTrackEnt> trackList) {
        initializePlaybackController();
        initializeSeekbar();
        mUserPlaylist = new ArrayList<>();
        for (PodcastTrackEnt tracks : trackList
                ) {
            String path = AppConstants.DOWNLOAD_PATH + File.separator + tracks.getName().replaceAll("\\s+", "") + ".mp3";
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
        mPlayerAdapter.loadPlayList(mUserPlaylist,startingIndex);
        setItemName(startingIndex);

    }

    @OnClick({R.id.btn_volume, R.id.btn_backward, R.id.btn_play, R.id.btn_forward})
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
                if (mPlayerAdapter.isReadyForPlay()) {
                    if (!mPlayerAdapter.isPlaying() && !hasPlaylistComplete) {
                        performPlayClick();
                    } else {
                        performPauseClick();
                    }
                } else {
                    if (hasPlaylistComplete) {

                    } else {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.buffering_text));
                    }
                }
                break;
            case R.id.btn_forward:
                mPlayerAdapter.playNext();
                break;
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

    public String getBookDurationText(Integer secound) {
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

    private void performPlayClick() {
        hasPlaylistComplete = false;
        mPlayerAdapter.play();
        pbBuffering.setVisibility(View.GONE);
        btnPlay.setVisibility(View.VISIBLE);
        btnPlay.setImageResource(R.drawable.pause_icon_big);

    }

    private void performPauseClick() {
        mPlayerAdapter.pause();
        btnPlay.setImageResource(R.drawable.play_icon_white);

    }

    @Override
    public void onTrackSelected(int index) {
        if (mPlayerAdapter != null) {
            hasPlaylistComplete = false;
            mPlayerAdapter.playIndex(index);
            getMainActivity().closeDrawer();
            setItemName(index);
        }
    }

    private void setItemName(int index) {
        if (playerType.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
            if (index < mUserPlaylist.size()) {
                txtPlayingItemName.setText(podcastDetailEnt.getTrackList().get(index).getName());
                txtPlayingItemAlbum.setText(podcastDetailEnt.getTitle());
                if (itemChangeListener!=null){
                    itemChangeListener.onItemChanged(index);
                }
            }
        } else if (playerType.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
            if (index < mUserPlaylist.size()) {
                if (!bookDetailEnt.getIsPurchased()) {
                    txtPlayingItemName.setText(R.string.preview);
                } else {
                    txtPlayingItemName.setText(getDockActivity().getResources().getString(R.string.chapters) + " " + (index + 1));
                }
                txtPlayingItemAlbum.setText(bookDetailEnt.getBookName());
                if (itemChangeListener!=null){
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
            if (playerType.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
                txtTimeTotal.setText(getTotalDuaration(duration));
            } else if (playerType.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {
                txtTimeTotal.setText(getTotalDuaration(duration));
            }
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
                if (remainingMinute == 0 && remainingSecond == 0) {
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
            btnPlay.setVisibility(View.VISIBLE);
            btnPlay.setImageResource(R.drawable.play_icon_white);
            mPlayerAdapter.playNext();
        }

        @Override
        public void onPlayListCompleted() {
            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.playlist_complete));
            mPlayerAdapter.reset();
            sbProgress.setProgress(0);
            hasPlaylistComplete = true;
            pbBuffering.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
            btnPlay.setImageResource(R.drawable.play_icon_white);
//            getMainActivity().clearFlagKeepScreenOn();
        }

        @Override
        public void isReadyToPlay(int currentItem) {
            if (hasPlaylistComplete) {
                return;
            } else {
                performPlayClick();
            }
        }

        @Override
        public void onPlaybackInfo(int what, int extra) {
            switch (what) {
                case Player.STATE_BUFFERING:
                    pbBuffering.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.GONE);
                    break;
                case Player.STATE_READY:
                    pbBuffering.setVisibility(View.GONE);
                    btnPlay.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    pbBuffering.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.GONE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    pbBuffering.setVisibility(View.GONE);
                    btnPlay.setVisibility(View.VISIBLE);
                    break;
                case State.NEXT:
                    pbBuffering.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.GONE);
                    setItemName(mPlayerAdapter.getCurrentItemIndex());
                    //  onPositionChanged(0,0,0);
                    break;
                case State.PREVIOUS:
                    pbBuffering.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.GONE);
                    setItemName(mPlayerAdapter.getCurrentItemIndex());
                    //onPositionChanged(0,0,0);
                    break;
            }
        }
    }

}