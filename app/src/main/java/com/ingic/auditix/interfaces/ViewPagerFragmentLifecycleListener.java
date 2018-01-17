package com.ingic.auditix.interfaces;

import android.content.Context;

import com.ingic.auditix.helpers.BasePreferenceHelper;

/**
 * Created on 1/15/2018.
 */

public interface ViewPagerFragmentLifecycleListener {

    public void onPauseFragment();

    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper);
}
