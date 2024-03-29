package com.ingic.auditix.fragments.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.DownloadDetailEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.views.TitleBar;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 12/23/2017.
 */
public class ProfileFragment extends BaseFragment implements TabLayout.OnTabSelectedListener, ViewPagerFragmentLifecycleListener {
    public static final String TAG = "ProfileFragment";

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    FrameLayout viewpager;
    Unbinder unbinder;
    ArrayList<String> newsAndPodcastsEpisodesID = new ArrayList<>();
    private ArrayList<BaseFragment> fragmentList;
    private int startingWithIndex = 0;
    private DownloadDetailEnt downloadDetailEnt;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setStartingWithIndex(String tag) {
        switch (tag) {
            case AppConstants.TAB_NEWS:
                this.startingWithIndex = 0;

                break;
            case AppConstants.TAB_PODCAST:
                this.startingWithIndex = 1;

                break;
            case AppConstants.TAB_BOOKS:
                this.startingWithIndex = 2;

                break;
            default:
                this.startingWithIndex = 0;
                break;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_ALL_DOWNLAOD_DETAILS:
                downloadDetailEnt = (DownloadDetailEnt) result;
                bindTabs();
                break;
        }
    }

    public void setTitleBar(TitleBar titleBar) {
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.profile));
        titleBar.showMenuButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serviceHelper.enqueueCall(webService.getAllDownloadDetails(getAllDownloadIDS(AppConstants.TAB_PODCAST), getAllDownloadIDS(AppConstants.TAB_NEWS), prefHelper.getUserToken()), WebServiceConstants.GET_ALL_DOWNLAOD_DETAILS);

    }

    public DownloadDetailEnt getDownloadDetailEnt() {
        return downloadDetailEnt;
    }

    public String getAllDownloadIDS(String tag) {
        ArrayList<String> idList = new ArrayList<>();
        String path = AppConstants.DOWNLOAD_PATH + File.separator + tag;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();

            for (File file : files
                    ) {
                String childPath = AppConstants.DOWNLOAD_PATH + File.separator + tag + File.separator + file.getName();
                File childdirectory = new File(childPath);
                File[] childfiles = childdirectory.listFiles();
                for (File childFile : childfiles
                        ) {
                    if (!childFile.getName().contains(".temp"))
                        idList.add(childFile.getName());
                }

            }
            return android.text.TextUtils.join(",", idList);
        }
        return "";
    }

    private void replaceTab(int position) {
        android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.replace(R.id.viewpager, fragmentList.get(position));
        transaction.commitAllowingStateLoss();


    }

    private void bindTabs() {

        fragmentList = new ArrayList<>(3);
        fragmentList.add(new ProfileNewsFragment());
        fragmentList.add(new ProfilePodcastFragment());
        fragmentList.add(new ProfileBooksFragment());
        tabLayout.addTab(tabLayout.newTab().setText(R.string.news), true);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.podcast), false);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.books), false);
        replaceTab(startingWithIndex);
        tabLayout.addOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        replaceTab(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper) {

    }
}
