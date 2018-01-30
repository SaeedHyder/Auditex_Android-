package com.ingic.auditix.fragments;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.views.TitleBar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 12/23/2017.
 */
public class SearchFragment extends BaseFragment implements ViewPagerFragmentLifecycleListener {
    public static SearchFragment newInstance() {
        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setTitleBar(((HomeTabFragment) getParentFragment()).getTitleBar());
    } @Override
    public void onResume() {
        super.onResume();
        setTitleBar(((HomeTabFragment) getParentFragment()).getTitleBar());
    }

    public void setTitleBar(TitleBar titleBar) {
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.search));
        titleBar.showBackButton();

    }
    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper) {

    }
}