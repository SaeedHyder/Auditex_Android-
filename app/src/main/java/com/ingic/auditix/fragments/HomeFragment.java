package com.ingic.auditix.fragments;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.ui.views.TitleBar;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.btn_poscast)
    ImageView btnPoscast;
    @BindView(R.id.btn_books)
    ImageView btnBooks;
    @BindView(R.id.btn_news)
    ImageView btnNews;
    @BindView(R.id.btn_profile)
    ImageView btnProfile;
    HomeTabFragment tabFragment;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabFragment = new HomeTabFragment();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showMenuButton();
        titleBar.addBackground();
        titleBar.setSubHeading(getString(R.string.home));
        if (!prefHelper.isGuest()) {
            titleBar.showNotificationButton(0);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().refreshSideMenu();
        if (getMainActivity().filterFragment != null) {
            getMainActivity().filterFragment.clearFilters();
        }
        if (getMainActivity().booksFilterFragment != null) {
            getMainActivity().booksFilterFragment.clearFilters();
        }
        setListeners();
        getDownloadFilePath(prefHelper.getUser().getAccountID() + "");
    }

    private void setListeners() {
        btnBooks.setOnClickListener(this);
        btnNews.setOnClickListener(this);
        btnPoscast.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
    }

    private void getDownloadFilePath(String userID) {
        File direct = new File(Environment.getExternalStorageDirectory() + File.separator
                + "audtix" + File.separator
                + userID);

        if (!direct.exists()) {
            if (direct.mkdirs()) {
                AppConstants.DOWNLOAD_PATH = direct.getPath();
            }
        } else {
            AppConstants.DOWNLOAD_PATH = direct.getPath();
        }
    }

  /*  @OnClick({R.id.btn_poscast, R.id.btn_books, R.id.btn_news})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_poscast:
                tabFragment.setTag(AppConstants.TAB_PODCAST);
                getDockActivity().replaceDockableFragment(tabFragment, "HomeTabFragment");
                break;
            case R.id.btn_books:
                tabFragment.setTag(AppConstants.TAB_BOOKS);
                getDockActivity().replaceDockableFragment(tabFragment, "HomeTabFragment");
            case R.id.btn_news:
               tabFragment.setTag(AppConstants.TAB_NEWS);
                getDockActivity().replaceDockableFragment(tabFragment, "HomeTabFragment");
                //willbeimplementedinfuture();
                break;
        }
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_poscast:
                tabFragment.setTag(AppConstants.TAB_PODCAST);
                getDockActivity().replaceDockableFragment(tabFragment, "HomeTabFragment");
                break;
            case R.id.btn_books:
                tabFragment.setTag(AppConstants.TAB_BOOKS);
                getDockActivity().replaceDockableFragment(tabFragment, "HomeTabFragment");
                break;
            case R.id.btn_news:
                tabFragment.setTag(AppConstants.TAB_NEWS);
                getDockActivity().replaceDockableFragment(tabFragment, "HomeTabFragment");
                //willbeimplementedinfuture();
                break;
                case R.id.btn_profile:
                    if (prefHelper.isGuest()) {
                        showGuestMessage();
                    }else {
                        tabFragment.setTag(AppConstants.TAB_PROFILE);
                        getDockActivity().replaceDockableFragment(tabFragment, "HomeTabFragment");
                    }
                //willbeimplementedinfuture();
                break;
        }
    }


}

