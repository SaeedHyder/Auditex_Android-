package com.ingic.auditix.fragments;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 1/30/2018.
 */
public class NewsFavoriteListFragment extends BaseFragment {
    public static final String TAG = "NewsFavoriteListFragment";

    public static NewsFavoriteListFragment newInstance() {
        Bundle args = new Bundle();

        NewsFavoriteListFragment fragment = new NewsFavoriteListFragment();
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
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}