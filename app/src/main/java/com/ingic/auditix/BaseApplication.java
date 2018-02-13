package com.ingic.auditix;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.devbrackets.android.exomedia.ExoMedia;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.retrofit.CustomInterceptor;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;
/*
import io.realm.Realm;
import io.realm.RealmConfiguration;*/
import java.io.File;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import cn.dreamtobe.filedownloader.OkHttp3Connection;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
//        MultiDex.install(this);
       Fabric.with(this, new Crashlytics());
        initImageLoader();
        initRealm();
        configureExoMedia();
        getDownloadFilePath();
        initDownloadManager();
    }

    private void initDownloadManager() {
        /*FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();*/
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.build();
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new OkHttp3Connection.Creator(builder))
                .commit();
    }

    private void getDownloadFilePath() {
        File direct = new File(Environment.getExternalStorageDirectory()+File.separator
                + "audtix");

        if (!direct.exists()) {
            if (direct.mkdirs()) {
                AppConstants.DOWNLOAD_PATH = direct.getPath();
            }
        }else {
            AppConstants.DOWNLOAD_PATH = direct.getPath();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    private void initRealm() {
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
    private void configureExoMedia() {
        // Registers the media sources to use the OkHttp client instead of the standard Apache one
        // Note: the OkHttpDataSourceFactory can be found in the ExoPlayer extension library `extension-okhttp`
        ExoMedia.setDataSourceFactoryProvider(new ExoMedia.DataSourceFactoryProvider() {
            @NonNull
            @Override
            public DataSource.Factory provide(@NonNull String userAgent, @Nullable TransferListener<? super DataSource> listener) {
                // Updates the network data source to use the OKHttp implementation
                DataSource.Factory upstreamFactory = new OkHttpDataSourceFactory(new OkHttpClient(), userAgent, listener);

                // Adds a cache around the upstreamFactory
                Cache cache = new SimpleCache(getCacheDir(), new LeastRecentlyUsedCacheEvictor(50 * 1024 * 1024));
                return new CacheDataSourceFactory(cache, upstreamFactory, CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
            }
        });
    }
    public void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(com.facebook.R.drawable.com_facebook_profile_picture_blank_square)
                .showImageOnFail(com.facebook.R.drawable.com_facebook_profile_picture_blank_square)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .displayer(new FadeInBitmapDisplayer(850))
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
        L.disableLogging();
    }

}
