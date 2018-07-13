package com.ingic.auditix.fragments.standard;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 12/13/2017.
 */
public class ForgotPasswordEmailFragment extends BaseFragment {
    @BindView(R.id.btn_forgot)
    AnyTextView btnForgot;
    @BindView(R.id.edt_email_address)
    AppCompatEditText edtEmailAddress;
    @BindView(R.id.inputlayout_email)
    TextInputLayout inputlayoutEmail;
    @BindView(R.id.lineemail)
    View lineemail;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;

    public static ForgotPasswordEmailFragment newInstance() {
        Bundle args = new Bundle();

        ForgotPasswordEmailFragment fragment = new ForgotPasswordEmailFragment();
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
                ForgotPasswordResetFragment forgotPasswordResetFragment = new ForgotPasswordResetFragment();
                forgotPasswordResetFragment.setUserEmail(edtEmailAddress.getText().toString());
                getDockActivity().replaceDockableFragment(forgotPasswordResetFragment, "ForgotPasswordResetFragment");
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
        View view = inflater.inflate(R.layout.fragment_forgot_password_email, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setlistener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setlistener() {
        edtEmailAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edtEmailAddress.setTextColor(getResources().getColor(R.color.app_orange));
                    lineemail.setBackgroundColor(getResources().getColor(R.color.app_orange));
                }
            }
        });
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (isvalidated()) {
            serviceHelper.enqueueCall(webService.ResetPassword(edtEmailAddress.getText().toString()), WebServiceConstants.RESET_PASSWORD);
        }
    }

    private boolean isvalidated() {
        if (edtEmailAddress.getText() == null || (edtEmailAddress.getText().toString().isEmpty()) ||
                !(Patterns.EMAIL_ADDRESS.matcher(edtEmailAddress.getText().toString()).matches())) {
            edtEmailAddress.setError(getString(R.string.enter_valid_email));
            return false;
        } else {
            return true;
        }
    }
}