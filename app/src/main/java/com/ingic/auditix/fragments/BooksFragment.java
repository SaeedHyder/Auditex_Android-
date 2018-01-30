package com.ingic.auditix.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.views.TitleBar;

/**
 * Created on 12/23/2017.
 */
public class BooksFragment extends BaseFragment implements ViewPagerFragmentLifecycleListener {
    public static BooksFragment newInstance() {
        Bundle args = new Bundle();

        BooksFragment fragment = new BooksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    public void setTitleBar(TitleBar titleBar) {
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.books));
        titleBar.showBackButton();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleBar(((HomeTabFragment) getParentFragment()).getTitleBar());
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        setTitleBar(((HomeTabFragment) getParentFragment()).getTitleBar());
    }

    @Override
    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper) {

    }
}