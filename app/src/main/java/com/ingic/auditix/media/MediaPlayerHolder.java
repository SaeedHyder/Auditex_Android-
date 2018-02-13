/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ingic.auditix.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import com.devbrackets.android.exomedia.AudioPlayer;
import com.devbrackets.android.exomedia.core.listener.InfoUpdateListener;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.ingic.auditix.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Exposes the functionality of the {@link MediaPlayer} and implements the {@link PlayerAdapter}
 * so that {@link com.ingic.auditix.activities.MainActivity} can control music playback.
 */
public final class MediaPlayerHolder implements PlayerAdapter, OnPreparedListener, InfoUpdateListener {

    //region Global Variables
    public static final int PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 1000;

    private final Context mContext;
    private AudioPlayer mMediaPlayer;
    private String mResource;
    private String mResouceFilePath, mResourceURL;
    private boolean isFromPath;
    private PlaybackInfoListener mPlaybackInfoListener;
    private Handler mExecutor;
    private Runnable mSeekbarPositionUpdateTask;
    private ArrayList<PlayListModel> mPlayList;
    private int currentPlayingItem;
    private boolean isReadyForPlay = false;
    //endregion

    //region Player Initializing
    public MediaPlayerHolder(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * Once the {@link MediaPlayer} is released, it can't be used again, and another one has to be
     * created. In the onStop() method of the {@link com.ingic.auditix.activities.MainActivity} the {@link MediaPlayer} is
     * released. Then in the onStart() of the {@link com.ingic.auditix.activities.MainActivity} a new {@link MediaPlayer}
     * object has to be created. That's why this method is private, and called by load(int) and
     * not the constructor.
     */
    private void initializeMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new AudioPlayer(mContext);
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion() {
                    logToUI("MediaPlayer playback completed");
                    if (mPlaybackInfoListener != null) {
                        mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.COMPLETED);
                        mPlaybackInfoListener.onPlaybackCompleted();
                    } else {
                        playNext();
                    }
                }
            });
            mMediaPlayer.setInfoUpdateListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            logToUI("mMediaPlayer = new MediaPlayer()");

        }
    }

    public void setPlaybackInfoListener(PlaybackInfoListener listener) {
        mPlaybackInfoListener = listener;
    }

  /*  @Override
    public void onPrepared(MediaPlayer mp) {
        initializeProgressCallback();
        isReadyForPlay = true;
        if (mPlaybackInfoListener != null)
            mPlaybackInfoListener.isReadyToPlay(currentPlayingItem);
        logToUI("initializeProgressCallback()");

    }
*/
  /*  @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        onPlaybackInfo(what, extra);
        return false;
    }*/
    //endregion

    //region Player Controls
    // Implements PlaybackControl.
    private void loadMedia(String mResouceURI) {
        isReadyForPlay = false;
        stopUpdatingCallbackWithPosition(true);
//        initializeMediaPlayer();

        try {
            logToUI("load() {1. setDataSource}");
            AssetFileDescriptor assetFileDescriptor =
                    mContext.getResources().openRawResourceFd(R.raw.jazz_in_paris);
           /* mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());*/
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(Uri.parse(mResouceURI));
        } catch (Exception e) {
            logToUI(e.toString());
        }

        try {
            logToUI("load() {2. prepare}");
            mMediaPlayer.prepareAsync();

        } catch (Exception e) {
            logToUI(e.toString());
        }

        //initializeProgressCallback();

    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            logToUI("release() and mMediaPlayer = null");
            mMediaPlayer.release();
            mMediaPlayer = null;
            currentPlayingItem = 0;
            loadMedia(getResouceURI(currentPlayingItem));
        }
    }

    @Override
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public boolean isReadyForPlay() {
        return isReadyForPlay;
    }

    @Override
    public void play() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            logToUI("playbackStart()");
            mMediaPlayer.start();
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PLAYING);
            }
            startUpdatingCallbackWithPosition();
        }
    }

    @Override
    public void reset() {
        if (mMediaPlayer != null) {
            logToUI("playbackReset()");
            mMediaPlayer.reset();
           /* release();
            currentPlayingItem = 0;
            loadMedia(getResouceURI(currentPlayingItem));*/
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.RESET);
            }
            stopUpdatingCallbackWithPosition(true);
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PAUSED);
            }
            logToUI("playbackPause()");
        }
    }

    @Override
    public void playNext() {
        if (mMediaPlayer != null) {
            int nextIndex = getNextIndex();
            if (nextIndex == -1) {
                if (mPlaybackInfoListener != null) {
                    mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.PLAYLISTCOMPLETED);
                    mPlaybackInfoListener.onPlayListCompleted();
                    stopUpdatingCallbackWithPosition(true);
                }
            } else {
//                release();
                reset();
                loadMedia(getResouceURI(nextIndex));
                onPlaybackInfo(PlaybackInfoListener.State.NEXT, 0);
                logToUI("playingNextWithIndex(" + currentPlayingItem + ")");
            }
        }
    }

    @Override
    public void playPrevious() {
        if (mMediaPlayer != null) {
//            release();
            reset();
            loadMedia(getResouceURI(getPreviousIndex()));
            onPlaybackInfo(PlaybackInfoListener.State.PREVIOUS, 0);
            logToUI("playingPreviousWithIndex(" + currentPlayingItem + ")");
        }
    }

    @Override
    public void playIndex(int index) {
        if (index < mPlayList.size()) {
            reset();
            onPlaybackInfo(PlaybackInfoListener.State.NEXT, 0);
            logToUI("playingNextWithIndex(" + currentPlayingItem + ")");
            currentPlayingItem = index;
            loadMedia(getResouceURI(currentPlayingItem));
        }
    }

    @Override
    public int getCurrentItemIndex() {
        return currentPlayingItem;
    }

    @Override
    public void loadPlayList(ArrayList<PlayListModel> mPlayList,int currentPlayingItem) {
        if (this.mPlayList == null) {
            this.mPlayList = new ArrayList<>();
            this.mPlayList.addAll(mPlayList);
        } else {
            this.mPlayList.addAll(mPlayList);
        }
        initializeMediaPlayer();
       this. currentPlayingItem = currentPlayingItem;
        loadMedia(getResouceURI(currentPlayingItem));
        logToUI("LoadPlayList(" + this.mPlayList.size() + ")");
    }

    //region Player Progress Updater
    @Override
    public void initializeProgressCallback() {
        final int duration = ((Long) mMediaPlayer.getDuration()).intValue();
        if (mPlaybackInfoListener != null) {
            mPlaybackInfoListener.onDurationChanged(duration);
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
        /*Long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;*/
            long different = mMediaPlayer.getDuration();
            Long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            Long elapsedSeconds = different / secondsInMilli;
            mPlaybackInfoListener.onPositionChanged(0,
                    elapsedMinutes,
                    elapsedSeconds);
            logToUI(String.format("firing setPlaybackDuration(%d sec)",
                    TimeUnit.MILLISECONDS.toSeconds(duration)));
            logToUI("firing setPlaybackPosition(0)");
        }
    }

    //endregion

    @Override
    public void seekTo(int position) {
        if (mMediaPlayer != null) {
            logToUI(String.format("seekTo() %d ms", position));
            mMediaPlayer.seekTo(position);
        }
    }

    @Override
    public void onPlaybackInfo(int what, int extra) {
        if (mPlaybackInfoListener != null) {
            mPlaybackInfoListener.onPlaybackInfo(what, extra);
        }
    }

    @Override
    public boolean canPlayNext() {
        return isNextAvailable();
    }

    /**
     * Syncs the mMediaPlayer position with mPlaybackProgressCallback via recurring task.
     */
    private void startUpdatingCallbackWithPosition() {
     /*   if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
        } */
        if (mExecutor == null) {
            mExecutor = new Handler();
        }
        if (mSeekbarPositionUpdateTask == null) {
            mSeekbarPositionUpdateTask = new Runnable() {
                @Override
                public void run() {
                    updateProgressCallbackTask();
                }
            };
        }
        mExecutor.postDelayed(mSeekbarPositionUpdateTask, PLAYBACK_POSITION_REFRESH_INTERVAL_MS);
       /* mExecutor.scheduleAtFixedRate(
                mSeekbarPositionUpdateTask,
                0,
                PLAYBACK_POSITION_REFRESH_INTERVAL_MS,
                TimeUnit.MILLISECONDS
        );*/
    }

    // Reports media playback position to mPlaybackProgressCallback.
    private void stopUpdatingCallbackWithPosition(boolean resetUIPlaybackPosition) {
        if (mExecutor != null) {
//            mExecutor.shutdownNow();
            mExecutor.removeCallbacks(mSeekbarPositionUpdateTask);
            mExecutor = null;
            mSeekbarPositionUpdateTask = null;
            if (resetUIPlaybackPosition && mPlaybackInfoListener != null) {
                mPlaybackInfoListener.onPositionChanged(0, 0, 0);
            }
        }
    }

    private void updateProgressCallbackTask() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            long duration = mMediaPlayer.getDuration();
            long currentPosition = mMediaPlayer.getCurrentPosition();
            long different = (duration - currentPosition);
            long remainingMinute = TimeUnit.MILLISECONDS.toMinutes((duration - currentPosition));
            long remainingSecond = TimeUnit.MILLISECONDS.toSeconds((duration - currentPosition));
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
        /*Long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;*/

            Long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            Long elapsedSeconds = different / secondsInMilli;
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener.onPositionChanged(((Long) mMediaPlayer.getCurrentPosition()).intValue(), elapsedMinutes, elapsedSeconds);
                startUpdatingCallbackWithPosition();
            }
        }
    }

    //endregion

    //region Player Resource Controls
    private String getResouceURI(int index) {
        if (mPlayList.size() > index) {
            PlayListModel entity = mPlayList.get(index);
            return entity.isFromPath() ? entity.getmResouceFilePath() : entity.getmResouceURL();
        } else {
            return "";
        }
    }

    private int getNextIndex() {
        if (mPlayList != null && mPlayList.size() > (currentPlayingItem + 1)) {
            currentPlayingItem = currentPlayingItem + 1;
            return currentPlayingItem;
        }
        return -1;
    }

    private boolean isNextAvailable() {
        return mPlayList != null && mPlayList.size() > (currentPlayingItem + 1);
    }

    private int getPreviousIndex() {
        if (mPlayList != null && (currentPlayingItem - 1) >= 0) {
            currentPlayingItem = currentPlayingItem - 1;
            return currentPlayingItem;
        }
        return currentPlayingItem;
    }

    //endregion

    //region Log Controls
    private void logToUI(String message) {
        if (mPlaybackInfoListener != null) {
            mPlaybackInfoListener.onLogUpdated(message);
        }
    }

    @Override
    public void onInfoUpdate(int what, int extra) {
        onPlaybackInfo(what, extra);
    }

    @Override
    public void onPrepared() {
        initializeProgressCallback();
        isReadyForPlay = true;
        if (mPlaybackInfoListener != null)
            mPlaybackInfoListener.isReadyToPlay(currentPlayingItem);
        logToUI("initializeProgressCallback()");
    }

    //endregion


}
