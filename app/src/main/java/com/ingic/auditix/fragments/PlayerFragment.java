package com.ingic.auditix.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastDetailEnt;
import com.ingic.auditix.entities.PodcastTrackEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.UIHelper;
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
    private Integer trackID;
    private String playerType = "";
    private int startingIndex = 0;
    //endregion

    //region Fragment Lifecycles
    public static PlayerFragment newInstance(PodcastDetailEnt podcastDetail, Integer trackID, String playerType) {
        Bundle args = new Bundle();

        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        fragment.setContent(podcastDetail, trackID, playerType);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_PODCAST_DETAIL:
                PodcastDetailEnt ent = (PodcastDetailEnt) result;
                setupUIViews(ent);
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.playing_now));
        titleBar.showBackButton();
        titleBar.addBackground();
        EpisodeListingFragment fragment = new EpisodeListingFragment();
        fragment.setListItemListener(this);
        fragment.setTrackList(podcastDetailEnt.getTrackList());
        getMainActivity().setRightSideFragment(fragment);
        titleBar.showListingFragment(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().isNavigationGravityRight = true;
                getMainActivity().getDrawerLayout().openDrawer(Gravity.RIGHT);
            }
        });
        getMainActivity().titleBar.showFavoriteButton(podcastDetailEnt.isFavorite(), new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                serviceHelper.enqueueCall(webService.changeFavoriteStatus(trackID, isChecked, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
            }
        });
      /*  titleBar.showFavoriteButton(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                serviceHelper.enqueueCall(webService.changeFavoriteStatus(trackID, isChecked, prefHelper.getUserToken()), WebServiceConstants.ADD_FAVORITE);
            }
        });*/
    }

    private void setContent(PodcastDetailEnt podcastDetail, Integer trackID, String playerType) {
        this.podcastDetailEnt = podcastDetail;
        this.trackID = trackID;
        this.playerType = playerType;
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
        //setupMediaPlayer(musicList);
        getDetails(playerType);


    }
    //endregion

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlayerAdapter.release();
        unbinder.unbind();
    }

    private void getDetails(String type) {
        if (type.equalsIgnoreCase(AppConstants.TAB_PODCAST)) {
//            serviceHelper.enqueueCall(webService.getPodcastDetailByTrack(trackID, prefHelper.getUserToken()), WebServiceConstants.GET_PODCAST_DETAIL);
            setupUIViews(podcastDetailEnt);
        } else if (type.equalsIgnoreCase(AppConstants.TAB_BOOKS)) {

        }

    }

    private void setupUIViews(PodcastDetailEnt ent) {
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
        sbProgress.setPadding(0, 0, 0, 0);
        bindPlaylist(ent.getTrackList());

        // sbProgress.setOnTouchListener(this);
    }

    private void bindPlaylist(ArrayList<PodcastTrackEnt> trackList) {
        initializePlaybackController();
        initializeSeekbar();
        mUserPlaylist = new ArrayList<>();
        for (PodcastTrackEnt tracks : trackList
                ) {
            if (podcastDetailEnt.isEpisodeAdded()) {
                mUserPlaylist.add(new PlayListModel("", String.format("%s:%s/%s/%s", podcastDetailEnt.WowzaURL, podcastDetailEnt.getWowzaPort(),
                        podcastDetailEnt.getWowzaAppName(), tracks.getFileUrl()), false));
            } else {
                mUserPlaylist.add(new PlayListModel("", tracks.getFileUrl(), false));
            }
        }
        mPlayerAdapter.loadPlayList(mUserPlaylist);
        if (startingIndex < mUserPlaylist.size()) {
            txtPlayingItemName.setText(podcastDetailEnt.getTrackList().get(startingIndex).getName());
            txtPlayingItemAlbum.setText(podcastDetailEnt.getTrackList().get(startingIndex).getDescription());
        }

    }

    @OnClick({R.id.btn_volume, R.id.btn_backward, R.id.btn_play, R.id.btn_forward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_volume:
                AudioManager audioManager = (AudioManager) getDockActivity().getSystemService(Context.AUDIO_SERVICE);
                if (audioManager != null) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
                }
                //openTracklist();
                break;
            case R.id.btn_backward:
                mPlayerAdapter.playPrevious();
                break;
            case R.id.btn_play:
                if (mPlayerAdapter.isReadyForPlay()) {
                    if (!mPlayerAdapter.isPlaying()) {
                        performPlayClick();
                    } else {
                        performPauseClick();
                    }
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.buffering_text));
                }
                break;
            case R.id.btn_forward:
                mPlayerAdapter.playNext();
                break;
        }
    }

    private void openTracklist() {
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.replace(R.id.episode_fragment, EpisodeListingFragment.newInstance(podcastDetailEnt.getTrackList()));
        transaction.addToBackStack("EpisodeListingFragment");
        transaction.commit();


    }

    private String getTotalDuaration(long duration) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        Long elapsedMinutes = duration / minutesInMilli;
        duration = duration % minutesInMilli;

        Long elapsedSeconds = duration / secondsInMilli;
        return elapsedMinutes == 0 ? elapsedSeconds.intValue() + "s" : elapsedMinutes.intValue() + "m";
    }

    private String getReamainingDuration(long totalDuration, long currentTime) {
        long different = currentTime - totalDuration;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        /*Long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;*/

        Long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        Long elapsedSeconds = different / secondsInMilli;

        return elapsedMinutes == 0 && elapsedSeconds == 0 ? "00:00" : "-" + elapsedMinutes.intValue() + ":" + elapsedSeconds.intValue();

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
        getMainActivity().setFlagKeepScreenOn();
    }

    private void performPauseClick() {
        mPlayerAdapter.pause();
        btnPlay.setImageResource(R.drawable.play_icon_white);
        getMainActivity().clearFlagKeepScreenOn();
    }

    @Override
    public void onTrackSelected(int index) {
        if (mPlayerAdapter != null) {
            mPlayerAdapter.playIndex(index);
            getMainActivity().closeDrawer();
            if (index < mUserPlaylist.size()) {
                txtPlayingItemName.setText(podcastDetailEnt.getTrackList().get(index).getName());
                txtPlayingItemAlbum.setText(podcastDetailEnt.getTrackList().get(index).getDescription());
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
        }

        @Override
        public void onPlayListCompleted() {
            mPlayerAdapter.release();
            sbProgress.setProgress(0);
            hasPlaylistComplete = true;
            pbBuffering.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
            btnPlay.setImageResource(R.drawable.play_icon_white);
            getMainActivity().clearFlagKeepScreenOn();
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
                    //  onPositionChanged(0,0,0);
                    break;
                case State.PREVIOUS:
                    pbBuffering.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.GONE);
                    //onPositionChanged(0,0,0);
                    break;
            }
        }
    }

}