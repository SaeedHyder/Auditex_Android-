package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.UserModel;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.TokenUpdater;
import com.ingic.auditix.ui.views.TitleBar;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 12/12/2017.
 */
public class AccessSelectionFragment extends BaseFragment {
    @BindView(R.id.btn_signin)
    LinearLayout btnSignin;
    @BindView(R.id.btn_signup)
    LinearLayout btnSignup;
    @BindView(R.id.btn_skip)
    Button btnSkip;
    Unbinder unbinder;

    public static AccessSelectionFragment newInstance() {
        Bundle args = new Bundle();

        AccessSelectionFragment fragment = new AccessSelectionFragment();
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
        View view = inflater.inflate(R.layout.fragment_access_selection, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag){
            case WebServiceConstants.GUEST_LOGIN:
                UserModel user = (UserModel) result;
                prefHelper.putUser(user);
                prefHelper.setUserToken(user.getToken().getTokenType() + " " + user.getToken().getAccessToken());
                prefHelper.setLoginStatus(true);
                prefHelper.setGuestStatus(true);
                TokenUpdater.getInstance().UpdateToken(getDockActivity(), user.getAccountID(), FirebaseInstanceId.getInstance().getToken(),
                        prefHelper.getUserToken(),prefHelper.isGuest());
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragmnet");
                break;
        }
    }

    @OnClick({R.id.btn_signin, R.id.btn_signup, R.id.btn_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                break;
            case R.id.btn_signup:
                getDockActivity().replaceDockableFragment(SignUpFragment.newInstance(), "SignUpFragment");
                break;
            case R.id.btn_skip:
                /*getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");*/
                serviceHelper.enqueueCall(webService.guestRegistration(AppConstants.ANDROID, FirebaseInstanceId.getInstance().getToken(),
                        true, false, false, FirebaseInstanceId.getInstance().getToken()), WebServiceConstants.GUEST_LOGIN);
                break;
        }
    }
}