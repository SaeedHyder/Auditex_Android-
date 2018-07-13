package com.ingic.auditix.helpers;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ingic.auditix.R;
import com.ingic.auditix.activities.DockActivity;
import com.ingic.auditix.entities.ResponseWrapper;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.webServiceResponseLisener;
import com.ingic.auditix.retrofit.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 7/17/2017.
 */

public class ServiceHelper<T> {
    private final ProgressDialog progressDialog;
    private webServiceResponseLisener serviceResponseLisener;
    private DockActivity context;

    public ServiceHelper(webServiceResponseLisener serviceResponseLisener, DockActivity conttext) {
        this.serviceResponseLisener = serviceResponseLisener;
        this.context = conttext;
        progressDialog = new ProgressDialog(conttext);
        progressDialog.setMessage(conttext.getString(R.string.com_facebook_loading));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

    }

    public void enqueueCall(Call<ResponseWrapper<T>> call, final String tag) {
        if (InternetHelper.CheckInternetConectivityandShowToast(context)) {
            context.onLoadingStarted();
            if (progressDialog != null) {
                progressDialog.show();
            }
            call.enqueue(new Callback<ResponseWrapper<T>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseWrapper<T>> call, @NonNull Response<ResponseWrapper<T>> response) {
                    context.onLoadingFinished();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.body() != null) {
                        if (response.body().getStatusCode() == WebServiceConstants.SUCCESS_RESPONSE_CODE) {
                            serviceResponseLisener.ResponseSuccess(response.body().getResult(), tag);
                        } else {
                            serviceResponseLisener.ResponseFailure(tag);
                            UIHelper.showShortToastInCenter(context, response.body().getMessage());
                        }
                    } else {
                        serviceResponseLisener.ResponseFailure(tag);
                        UIHelper.showShortToastInCenter(context, response.message()+"");
//                        UIHelper.showShortToastInCenter(context, context.getResources().getString(R.string.server_response_error));
                    }

                }

                @Override
                public void onFailure(Call<ResponseWrapper<T>> call, Throwable t) {
                    context.onLoadingFinished();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    serviceResponseLisener.ResponseFailure(tag);
                    t.printStackTrace();
                    Log.e(ServiceHelper.class.getSimpleName() + " by tag: " + tag, t.toString());
                }
            });
        }else {
            serviceResponseLisener.ResponseFailure(tag);
        }
    }

    public void enqueueCall(Call<ResponseWrapper<T>> call, final String tag, boolean showLoader) {
        if (InternetHelper.CheckInternetConectivityandShowToast(context)) {
            if (showLoader) {
                context.onLoadingStarted();
                if (progressDialog != null) {
                    progressDialog.show();
                }
            }
            call.enqueue(new Callback<ResponseWrapper<T>>() {
                @Override
                public void onResponse(@NonNull Call<ResponseWrapper<T>> call, @NonNull Response<ResponseWrapper<T>> response) {
                    context.onLoadingFinished();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.body() != null) {
                        if (response.body().getStatusCode() == WebServiceConstants.SUCCESS_RESPONSE_CODE) {
                            serviceResponseLisener.ResponseSuccess(response.body().getResult(), tag);
                        } else {
                            serviceResponseLisener.ResponseFailure(tag);
                            UIHelper.showShortToastInCenter(context, response.body().getMessage());
                        }
                    } else {
                        serviceResponseLisener.ResponseFailure(tag);
                        UIHelper.showShortToastInCenter(context, response.message()+"");
                        //   UIHelper.showShortToastInCenter(context, context.getResources().getString(R.string.server_response_error));
                    }

                }

                @Override
                public void onFailure(Call<ResponseWrapper<T>> call, Throwable t) {
                    context.onLoadingFinished();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    serviceResponseLisener.ResponseFailure(tag);
                    t.printStackTrace();
                    Log.e(ServiceHelper.class.getSimpleName() + " by tag: " + tag, t.toString());
                }
            });
        }else {
            serviceResponseLisener.ResponseFailure(tag);
        }
    }
}
