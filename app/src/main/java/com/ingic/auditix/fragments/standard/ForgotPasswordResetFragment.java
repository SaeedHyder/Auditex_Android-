package com.ingic.auditix.fragments.standard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.UserModel;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.TokenUpdater;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.PinEntryEditText;
import com.ingic.auditix.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 12/13/2017.
 */
public class ForgotPasswordResetFragment extends BaseFragment {
    @BindView(R.id.txt_pin_code)
    PinEntryEditText txtPinCode;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_resetcode)
    AnyTextView btnResetcode;
    Unbinder unbinder;
    private String userEmail;

    public static ForgotPasswordResetFragment newInstance() {
        Bundle args = new Bundle();

        ForgotPasswordResetFragment fragment = new ForgotPasswordResetFragment();
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
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.RESET_PASSWORD:
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.resend_code_message));
                break;
            case WebServiceConstants.RESET_PASSWORD_CODE:
                UserModel user = (UserModel) result;
                prefHelper.putUser(user);
                prefHelper.setUserToken(user.getToken().getTokenType() + " " + user.getToken().getAccessToken());
                prefHelper.setLoginStatus(true);
                prefHelper.setGuestStatus(false);
                TokenUpdater.getInstance().UpdateToken(getDockActivity(), user.getAccountID(), FirebaseInstanceId.getInstance().getToken(),
                        prefHelper.getUserToken(),prefHelper.isGuest());
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password_reset, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtPinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() >= 5) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @OnClick({R.id.btn_login, R.id.btn_resetcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                serviceHelper.enqueueCall(webService.ResetPassword(userEmail, txtPinCode.getText().toString()), WebServiceConstants.RESET_PASSWORD_CODE);

                break;
            case R.id.btn_resetcode:
                serviceHelper.enqueueCall(webService.ResetPassword(userEmail), WebServiceConstants.RESET_PASSWORD);
                break;
        }
    }
}