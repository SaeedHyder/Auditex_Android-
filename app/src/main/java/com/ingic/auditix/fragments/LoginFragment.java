package com.ingic.auditix.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.UserModel;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.FacebookLoginHelper;
import com.ingic.auditix.helpers.GoogleHelper;
import com.ingic.auditix.helpers.TokenUpdater;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginFragment extends BaseFragment implements View.OnFocusChangeListener, FacebookLoginHelper.FacebookLoginListener, GoogleHelper.GoogleHelperInterfce {
    private static final int RC_SIGN_IN = 7;


    @BindView(R.id.edt_email_address)
    AppCompatEditText edtEmailAddress;
    @BindView(R.id.lineemail)
    View lineemail;
    @BindView(R.id.edt_password)
    AppCompatEditText edtPassword;
    @BindView(R.id.btn_forgot)
    AnyTextView btnForgot;
    @BindView(R.id.linepassword)
    View linepassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.inputlayout_email)
    TextInputLayout inputlayoutEmail;
    @BindView(R.id.inputlayout_password)
    TextInputLayout inputlayoutPassword;
    private CallbackManager callbackManager;
    private FacebookLoginHelper facebookLoginHelper;
    private FacebookLoginHelper.FacebookLoginEnt facebookLoginEnt;
    private GoogleHelper googleHelper;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private void setFocusListener() {
        edtEmailAddress.setOnFocusChangeListener(this);
        edtPassword.setOnFocusChangeListener(this);
    }

    private void setupGoogleSignup() {
        googleHelper = GoogleHelper.getInstance();
        googleHelper.setGoogleHelperInterface(this);
        googleHelper.configGoogleApiClient(this);
    }

    private void setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        // btnfbLogin.setFragment(this);
        facebookLoginHelper = new FacebookLoginHelper(getDockActivity(), this, this);
        LoginManager.getInstance().registerCallback(callbackManager, facebookLoginHelper);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            googleHelper.handleGoogleResult(requestCode, resultCode, data);
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        setFocusListener();
        setupGoogleSignup();
        setupFacebookLogin();
    }

    @Override
    public void onStart() {
        super.onStart();
        googleHelper.ConnectGoogleAPi();
        // googleHelper.checkGoogleSeesion();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleHelper.DisconnectGoogleApi();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edt_email_address:
                if (hasFocus) {
                    edtEmailAddress.setTextColor(getResources().getColor(R.color.app_orange));
                    edtPassword.setTextColor(getResources().getColor(R.color.app_gray));
                    lineemail.setBackgroundColor(getResources().getColor(R.color.app_orange));
                    linepassword.setBackgroundColor(getResources().getColor(R.color.app_gray));
                }
                break;
            case R.id.edt_password:
                if (hasFocus) {
                    edtEmailAddress.setTextColor(getResources().getColor(R.color.app_gray));
                    edtPassword.setTextColor(getResources().getColor(R.color.app_orange));
                    lineemail.setBackgroundColor(getResources().getColor(R.color.app_gray));
                    linepassword.setBackgroundColor(getResources().getColor(R.color.app_orange));
                }
                break;
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.LOGIN:
                UserModel user = (UserModel) result;
                prefHelper.putUser(user);
                prefHelper.setUserToken(user.getToken().getTokenType() + " " + user.getToken().getAccessToken());
                prefHelper.setLoginStatus(true);
                TokenUpdater.getInstance().UpdateToken(getDockActivity(), user.getAccountID(), FirebaseInstanceId.getInstance().getToken(),
                        prefHelper.getUserToken());
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragmnet");
                break;
        }

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
    }

    @OnClick({R.id.btn_forgot, R.id.btn_login, R.id.btn_fb_login, R.id.btn_gp_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_forgot:
                getDockActivity().replaceDockableFragment(ForgotPasswordEmailFragment.newInstance(), "ForgotPasswordEmailFragment");
                break;
            case R.id.btn_login:
                if (isvalidated()) {
                    serviceHelper.enqueueCall(webService.loginUser(edtEmailAddress.getText().toString(), edtPassword.getText().toString()),
                            WebServiceConstants.LOGIN);
                }
                break;
            case R.id.btn_fb_login:
                LoginManager.getInstance().logInWithReadPermissions(LoginFragment.this, facebookLoginHelper.getPermissionNeeds());
                break;
            case R.id.btn_gp_login:
                googleHelper.intentGoogleSign();
                break;

        }
    }

    private boolean isvalidated() {
        if (edtEmailAddress.getText() == null || (edtEmailAddress.getText().toString().isEmpty()) ||
                !(Patterns.EMAIL_ADDRESS.matcher(edtEmailAddress.getText().toString()).matches())) {
            edtEmailAddress.setError(getString(R.string.enter_valid_email));
            return false;
        } else if (edtPassword.getText().toString().isEmpty()) {
            edtPassword.setError(getString(R.string.enter_password));
            return false;
        } else if (edtPassword.getText().toString().length() < 6) {
            edtPassword.setError(getString(R.string.passwordLength));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onSuccessfulFacebookLogin(FacebookLoginHelper.FacebookLoginEnt loginEnt) {
        if (loginEnt != null) {
            facebookLoginEnt = loginEnt;
            serviceHelper.enqueueCall(webService.socialLoginUser(loginEnt.getFacebookFullName(), loginEnt.getFacebookUID(),
                    loginEnt.getFacebookBirthday(), getGenderConstants(loginEnt.getFacebookGender()), WebServiceConstants.REGISTER_TYPE_FACEBOOK,
                    loginEnt.getFacebookUProfilePicture()), WebServiceConstants.LOGIN);
        }
    }

    private int getGenderConstants(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return WebServiceConstants.GENDER_MALE;
        } else if (gender.equalsIgnoreCase("female")) {
            return WebServiceConstants.GENDER_FEMALE;
        } else {
            return WebServiceConstants.GENDER_MALE;
        }
    }

    @Override
    public void onGoogleSignInResult(GoogleHelper.GoogleLoginEnt result) {
        if (result != null) {
            serviceHelper.enqueueCall(webService.socialLoginUser(result.getGoogleFullName(), result.getGoogleUID(),
                    result.getGoogleBirthday(), getGenderConstants(result.getGoogleGender()),
                    WebServiceConstants.REGISTER_TYPE_GMAIL, result.getGoogleUProfilePicture()), WebServiceConstants.LOGIN);
        }
    }
}
