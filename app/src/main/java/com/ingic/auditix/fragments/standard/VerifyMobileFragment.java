package com.ingic.auditix.fragments.standard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.PinEntryEditText;
import com.ingic.auditix.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 7/5/18.
 */
public class VerifyMobileFragment extends BaseFragment {
    public static final String TAG = "VerifyMobileFragment";
    @BindView(R.id.txt_pin_code)
    PinEntryEditText txtPinCode;
    Unbinder unbinder;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.btnResentCode)
    AnyTextView btnResentCode;

    public static VerifyMobileFragment newInstance() {
        Bundle args = new Bundle();

        VerifyMobileFragment fragment = new VerifyMobileFragment();
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
            case WebServiceConstants.SUBMIT_SMS_CODE:
                prefHelper.setLoginStatus(true);
                prefHelper.setGuestStatus(false);
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragmnet");
                break;
            case WebServiceConstants.RESENT_SMS_CODE:
                UIHelper.showShortToastInCenter(getDockActivity(), getResString(R.string.resend_code_message_mobile));
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
        View view = inflater.inflate(R.layout.fragment_verify_mobile, container, false);
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
                if (s.toString().length() >= 4) {
                    btnSubmit.setEnabled(true);
                } else {
                    btnSubmit.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.btnSubmit, R.id.btnResentCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                serviceHelper.enqueueCall(webService.verifySMSCode(txtPinCode.getText().toString(), prefHelper.getUserToken()), WebServiceConstants.SUBMIT_SMS_CODE);
                break;
            case R.id.btnResentCode:
                serviceHelper.enqueueCall(webService.resendSMSCode(prefHelper.getUser().getPhoneNo(), prefHelper.getUserToken()), WebServiceConstants.RESENT_SMS_CODE);
                break;
        }
    }
}