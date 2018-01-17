package com.ingic.auditix.services;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ingic.auditix.R;
import com.ingic.auditix.activities.MainActivity;
import com.ingic.auditix.entities.ResponseWrapper;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.helpers.NotificationHelper;
import com.ingic.auditix.retrofit.WebService;
import com.ingic.auditix.retrofit.WebServiceFactory;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private WebService webservice;
    private BasePreferenceHelper preferenceHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        preferenceHelper = new BasePreferenceHelper(getApplicationContext());
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            buildNotification(remoteMessage);


        }
    }

    private void getNotificationCount() {


        webservice = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(getApplicationContext(),
                WebServiceConstants.SERVICE_URL);
        preferenceHelper = new BasePreferenceHelper(getApplicationContext());
        Call<ResponseWrapper<Integer>> callback = webservice.getUnreadCount(preferenceHelper.getUserToken());
        callback.enqueue(new Callback<ResponseWrapper<Integer>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseWrapper<Integer>> call, @NonNull Response<ResponseWrapper<Integer>> response) {
                if (response.body() != null ) {
                    preferenceHelper.setNotificationCount(response.body().getResult());
                }
                Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", "");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);

            }

            @Override
            public void onFailure(Call<ResponseWrapper<Integer>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    private void buildNotification(RemoteMessage messageBody) {
        //getNotificaitonCount();
        String title = getString(R.string.app_name);
        String message = messageBody.getData().get("message");
        Log.e(TAG, "message: " + message);
        Intent resultIntent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra("message", message);
        resultIntent.putExtra("tapped", true);

        Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", message);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);
        showNotificationMessage(MyFirebaseMessagingService.this, title, message, "", resultIntent);
    }

    /*private void getNotificaitonCount() {
        webservice = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(this, WebServiceConstants.Local_SERVICE_URL);
        preferenceHelper = new BasePreferenceHelper(this);
        Call<ResponseWrapper<countEnt>> call = webservice.getNotificationCount(preferenceHelper.getMerchantId());
        call.enqueue(new Callback<ResponseWrapper<countEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<countEnt>> call, Response<ResponseWrapper<countEnt>> response) {
                preferenceHelper.setNotificationCount(response.body().getResult().getCount());
            }

            @Override
            public void onFailure(Call<ResponseWrapper<countEnt>> call, Throwable t) {

            }
        });
    }*/

    private void SendNotification(int count, JSONObject json) {

    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        NotificationHelper.getInstance().showNotification(context,
                R.drawable.ic_launcher,
                title,
                message,
                timeStamp,
                intent);
    }


}
