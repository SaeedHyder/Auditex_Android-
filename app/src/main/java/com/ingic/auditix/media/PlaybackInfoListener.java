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

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Allows {@link MediaPlayerHolder} to report media playback duration and progress updates to
 * the {@link com.ingic.auditix.activities.MainActivity}.
 */
public abstract class PlaybackInfoListener {

    public static String convertStateToString(@State int state) {
        String stateString;
        switch (state) {
            case State.PLAYLISTCOMPLETED:
                stateString = "PLAYLISTCOMPLETED";
                break;
            case State.COMPLETED:
                stateString = "COMPLETED";
                break;
            case State.INVALID:
                stateString = "INVALID";
                break;
            case State.PAUSED:
                stateString = "PAUSED";
                break;
            case State.PLAYING:
                stateString = "PLAYING";
                break;
            case State.RESET:
                stateString = "RESET";
                break;
            default:
                stateString = "N/A";
        }
        return stateString;
    }

    public abstract void onLogUpdated(String formattedMessage);

    public abstract void onDurationChanged(int duration);

    public abstract void onPositionChanged(int position, long remainingMinute, long remainingSecond);

    public abstract void onStateChanged(@State int state);

    public abstract void onPlaybackCompleted();

    public abstract void onPlayListCompleted();

    public abstract void isReadyToPlay(int currentItem);


    public abstract void onPlaybackInfo(int what, int extra);

    @IntDef({State.INVALID, State.PLAYING, State.PAUSED, State.RESET, State.COMPLETED, State.PLAYLISTCOMPLETED, State.NEXT, State.PREVIOUS})
    @Retention(RetentionPolicy.SOURCE)
    protected @interface State {

        int INVALID = -1;
        int PLAYING = 0;
        int PAUSED = 1;
        int RESET = 2;
        int COMPLETED = 3;
        int PLAYLISTCOMPLETED = 4;
        int NEXT = 12;
        int PREVIOUS = 15;
    }

}