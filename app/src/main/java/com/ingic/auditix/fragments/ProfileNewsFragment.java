package com.ingic.auditix.fragments;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.ui.views.TitleBar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 1/6/2018.
 */
public class ProfileNewsFragment extends BaseFragment {
    public static final String TAG = "ProfileNewsFragment";

    public static ProfileNewsFragment newInstance() {
        Bundle args = new Bundle();

        ProfileNewsFragment fragment = new ProfileNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }
    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_news, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}