package com.ingic.auditix.entities;

import com.ingic.auditix.helpers.DateHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by saeedhyder on 9/15/2017.
 */

public class NotificationEnt {

    @SerializedName("UserNotificationID")
    @Expose
    private Integer userNotificationID;
    @SerializedName("NotificationID")
    @Expose
    private Integer notificationID;
    @SerializedName("AccountID")
    @Expose
    private Integer accountID;
    @SerializedName("AccountDeviceID")
    @Expose
    private Integer accountDeviceID;
    @SerializedName("NotificationTitle")
    @Expose
    private String notificationTitle;
    @SerializedName("NotificationMessage")
    @Expose
    private String notificationMessage;

    public Integer getUserNotificationID() {
        return userNotificationID;
    }

    public void setUserNotificationID(Integer userNotificationID) {
        this.userNotificationID = userNotificationID;
    }

    public Integer getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(Integer notificationID) {
        this.notificationID = notificationID;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getAccountDeviceID() {
        return accountDeviceID;
    }

    public void setAccountDeviceID(Integer accountDeviceID) {
        this.accountDeviceID = accountDeviceID;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }
}