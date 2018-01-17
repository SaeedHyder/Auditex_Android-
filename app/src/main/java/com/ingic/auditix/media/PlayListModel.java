package com.ingic.auditix.media;

/**
 * Created on 1/3/2018.
 */

public class PlayListModel {
    private String mResouceFilePath;
    private String mResouceURL;
    private boolean isFromPath;

    public PlayListModel(String mResouceFilePath, String mResouceURL, boolean isFromPath) {
        this.mResouceFilePath = mResouceFilePath;
        this.mResouceURL = mResouceURL;
        this.isFromPath = isFromPath;
    }

    public String getmResouceFilePath() {
        return mResouceFilePath;
    }

    public void setmResouceFilePath(String mResouceFilePath) {
        this.mResouceFilePath = mResouceFilePath;
    }

    public String getmResouceURL() {
        return mResouceURL;
    }

    public void setmResouceURL(String mResouceURL) {
        this.mResouceURL = mResouceURL;
    }

    public boolean isFromPath() {
        return isFromPath;
    }

    public void setFromPath(boolean fromPath) {
        isFromPath = fromPath;
    }
}
