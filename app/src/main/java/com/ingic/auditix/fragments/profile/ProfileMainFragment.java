package com.ingic.auditix.fragments.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.books.BooksFragment;
import com.ingic.auditix.fragments.standard.HomeTabFragment;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.views.TitleBar;

/**
 * Created on 7/23/18.
 */
public class ProfileMainFragment extends BaseFragment implements ViewPagerFragmentLifecycleListener {
    public static final String TAG = "NewsMainFragment";

    public static ProfileMainFragment newInstance() {
        Bundle args = new Bundle();

        ProfileMainFragment fragment = new ProfileMainFragment();
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
    public void onResume() {
        super.onResume();
    }

    public TitleBar getTitleBar() {
        return ((HomeTabFragment) getParentFragment()).getTitleBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_holder, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null)
            replaceFragment(ProfileFragment.newInstance(), "ProfileFragment");
    }

    public void replaceFragment(BaseFragment fragment, String tag) {
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_popenter, R.anim.fragment_pop_exit);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();


    }
    public void popFragment() {
        if (getChildFragmentManager() == null)
            return;
        getChildFragmentManager().popBackStack();
    }
    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper) {

    }
}