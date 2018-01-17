package com.ingic.auditix;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		MultiDex.install(this);
		Fabric.with(this, new Crashlytics());
		initImageLoader();
		initRealm();
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

	public void initImageLoader() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(com.facebook.R.drawable.com_facebook_profile_picture_blank_square)
				.showImageOnFail( com.facebook.R.drawable.com_facebook_profile_picture_blank_square )
				.resetViewBeforeLoading( true )
				.cacheInMemory( true ).cacheOnDisc( true )
				.imageScaleType( ImageScaleType.IN_SAMPLE_POWER_OF_2 )
				.displayer( new FadeInBitmapDisplayer( 850 ) )
				.bitmapConfig( Bitmap.Config.RGB_565 ).build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext() ).defaultDisplayImageOptions( options )
				.build();
		ImageLoader.getInstance().init( config );
		L.disableLogging();
	}
	
}
