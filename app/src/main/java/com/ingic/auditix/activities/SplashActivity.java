package com.ingic.auditix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.global.AppConstants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends AppCompatActivity {

    final int TIME_INTERVAL_TO_CHECK = 500;// in millis
    final int MIN_TIME_INTERVAL_FOR_SPLASH = 2500; // in millis
    boolean workComplete = false;
    Timer checkWorkTimer;
    @BindView(R.id.img_splash)
    ImageView imageView;
    Runnable backgroundWork = new Runnable() {

        @Override
        public void run() {

            // This area can be used in Splash to do tasks that do not delay
            // Splash as well as do not extend its time if there processing time
            // is less than splash
            // Check Internet Connection and meet necessary conditions
            // to start the app. E.g. Disk Checks.
            // Check preferences and availability of certain data.
            workComplete = true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        ImageLoader.getInstance().displayImage(AppConstants.DRAWABLE_PATH + R.drawable.splash_logo, imageView);
    }

    @Override
    public void onResume() {
        super.onResume();

        launchTimerAndTask();
    }

    private void launchTimerAndTask() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMainActivity();
            }
        }, MIN_TIME_INTERVAL_FOR_SPLASH);
        // Launch timer to test image changing and background threads work
       /* checkWorkTimer = new Timer();
        checkWorkTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (workComplete) {
                    initNextActivity();
                }
            }

        }, MIN_TIME_INTERVAL_FOR_SPLASH, TIME_INTERVAL_TO_CHECK);

        new Thread(backgroundWork).start();*/
    }

    private void initNextActivity() {
        checkWorkTimer.cancel();
        showMainActivity();

    }

    private void showMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}