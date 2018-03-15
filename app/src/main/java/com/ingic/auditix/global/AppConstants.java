package com.ingic.auditix.global;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AppConstants {

    public static final Object FILTER_VIEW_1 = "filter_view_1";
    public static final Object FILTER_VIEW_2 = "filter_view_2";
    public static final String FILE_PATH = "file://";
    public static final String ANDROID = "android";
    public static final String NOTIFICATION_COUNT_RECIEVED = "notificationCountRecieved";
    public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_APP = "MMM dd yyyy";
    public static final String Device_Type = "android";
    public static final String SOCIAL_MEDIA_TYPE = "facebook";
    //Firebase Constants
    // broadcast receiver intent filters
    public static final String TOPIC_GLOBAL = "global";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    //Tab Tag for TabLayout
    public static final String TAB_BOOKS = "books";
    public static final String TAB_PODCAST = "podcast";
    public static final String TAB_NEWS = "news";
    public static final String TAB_SEARCH = "search";
    public static final String TAB_PROFILE = "profile";
    //Path to Drawable
    public static final String DRAWABLE_PATH = "drawable://";
    public static final String KEY_CMS_TYPE = "Key";
    public static String twitter = "twitter";
    public static int INTENT_ID = 100;
    // id to handle the notification in the notification tray
    public static int NOTIFICATION_ID = 100;
    public static String DateFormat_DMY2 = "dd-MM-yy";
    public static String DateFormat_HM = "HH:mm";
    public static String DateFormat_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    //Path to Downloads
    public static String DOWNLOAD_PATH = "";

    //Download States
    @IntDef({DownloadStates.ERROR, DownloadStates.STARTED, DownloadStates.DOWNLOADING, DownloadStates.PENDING, DownloadStates.COMPLETE,DownloadStates.PAUSED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DownloadStates {
        int ERROR = 2000;
        int STARTED = 2001;
        int DOWNLOADING = 2002;
        int PENDING = 2003;
        int COMPLETE = 2004;
        int PAUSED = 2005;
    }

}
