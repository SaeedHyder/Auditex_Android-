package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 1/29/2018.
 */
public class SettingsFragment extends BaseFragment {
    public static final String TAG = "SettingsFragment";
    @BindView(R.id.btn_account)
    FrameLayout btnAccount;
    @BindView(R.id.btn_payment_details)
    FrameLayout btnPaymentDetails;
    @BindView(R.id.swt_continous_play)
    Switch swtContinousPlay;
    @BindView(R.id.btn_new_episodes)
    FrameLayout btnNewEpisodes;
    @BindView(R.id.btn_played_episodes)
    FrameLayout btnPlayedEpisodes;
    @BindView(R.id.btn_notifications)
    FrameLayout btnNotifications;
    @BindView(R.id.btn_privacy_policy)
    FrameLayout btnPrivacyPolicy;
    @BindView(R.id.btn_term_conditions)
    FrameLayout btnTermConditions;
    Unbinder unbinder;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
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
        titleBar.hideButtons();
        titleBar.addBackground();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.settings));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swtContinousPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_account, R.id.btn_payment_details, R.id.btn_new_episodes, R.id.btn_played_episodes, R.id.btn_notifications, R.id.btn_privacy_policy, R.id.btn_term_conditions})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_account:
                getDockActivity().replaceDockableFragment(EditProfileFragment.newInstance(), EditProfileFragment.TAG);
                break;
            case R.id.btn_payment_details:
                getDockActivity().replaceDockableFragment(PaymentDetailsFragment.newInstance(), PaymentDetailsFragment.TAG);
                break;
            case R.id.btn_new_episodes:
                getDockActivity().replaceDockableFragment(NewEpisodesFragment.newInstance(), NewEpisodesFragment.TAG);
                break;
            case R.id.btn_played_episodes:
                getDockActivity().replaceDockableFragment(PlayedEpisodesFragment.newInstance(), PlayedEpisodesFragment.TAG);
                break;
            case R.id.btn_notifications:
                getDockActivity().replaceDockableFragment(NotificationsFragment.newInstance(), NotificationsFragment.TAG);
                break;
            case R.id.btn_privacy_policy:
                getDockActivity().replaceDockableFragment(PrivacyPolicyFragment.newInstance(), PrivacyPolicyFragment.TAG);
                break;
            case R.id.btn_term_conditions:
                getDockActivity().replaceDockableFragment(TermAndConditionFragment.newInstance(), TermAndConditionFragment.TAG);
                break;
        }
    }
}