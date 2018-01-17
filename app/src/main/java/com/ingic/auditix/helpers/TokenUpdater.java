package com.ingic.auditix.helpers;

import android.content.Context;
import android.util.Log;

import com.ingic.auditix.entities.ResponseWrapper;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.retrofit.WebService;
import com.ingic.auditix.retrofit.WebServiceFactory;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 5/15/2017.
 */

public class TokenUpdater {
    private static final TokenUpdater tokenUpdater = new TokenUpdater();
    private WebService webservice;

    private TokenUpdater() {
    }

    public static TokenUpdater getInstance() {
        return tokenUpdater;
    }

    public void UpdateToken(Context context, final Integer userid, String fireBaseToken, String userAccessToken) {
        if (fireBaseToken.isEmpty()) {
            Log.e("Token Updater", "Token is Empty");
        }
        webservice = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(context,
                WebServiceConstants.Local_SERVICE_URL);
        String android_id = UUID.randomUUID().toString();
        ;
        Call<ResponseWrapper> call = webservice.updateToken(AppConstants.ANDROID, android_id,
                true, false, userid, false, fireBaseToken, userAccessToken);
        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response.body() != null)
                    Log.i("UPDATETOKEN", response.body().getStatusCode() + "" + userid);
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                Log.e("UPDATETOKEN", t.toString());
            }
        });
    }

}
