package com.ingic.auditix.fragments.settings;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 1/5/2018.
 */
public class ChangePasswordFragment extends BaseFragment implements View.OnFocusChangeListener {
    public static final String TAG = "ChangePasswordFragment";
    @BindView(R.id.edt_old_password)
    AppCompatEditText edtOldPassword;
    @BindView(R.id.inputlayout_old_password)
    TextInputLayout inputlayoutOldPassword;
    @BindView(R.id.lineoldpassword)
    View lineoldpassword;
    @BindView(R.id.edt_password)
    AppCompatEditText edtPassword;
    @BindView(R.id.inputlayout_password)
    TextInputLayout inputlayoutPassword;
    @BindView(R.id.linepassword)
    View linepassword;
    @BindView(R.id.edt_confirm_password)
    AppCompatEditText edtConfirmPassword;
    @BindView(R.id.inputlayout_confirm_password)
    TextInputLayout inputlayoutConfirmPassword;
    @BindView(R.id.lineconfirmpassword)
    View lineconfirmpassword;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;

    public static ChangePasswordFragment newInstance() {
        Bundle args = new Bundle();

        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
            case WebServiceConstants.CHANGE_PASSWORD:
                UIHelper.showShortToastInCenter(getDockActivity(),getString(R.string.edit_profile_message));
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
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
    public void onFocusChange(View v, boolean hasFocus) {
        setUnFocusColor(lineoldpassword, edtOldPassword);
        setUnFocusColor(linepassword, edtPassword);
        setUnFocusColor(lineconfirmpassword, edtConfirmPassword);
        switch (v.getId()) {
            case R.id.edt_old_password:
                if (hasFocus) {
                    setFocusColor(lineoldpassword, edtOldPassword);
                }
                break;
            case R.id.edt_password:
                if (hasFocus) {
                    setFocusColor(linepassword, edtPassword);
                }
                break;
            case R.id.edt_confirm_password:
                if (hasFocus) {
                    setFocusColor(lineconfirmpassword, edtConfirmPassword);
                }
                break;
        }
    }

    private void setFocusColor(View line, AppCompatEditText editText) {
        editText.setTextColor(getResources().getColor(R.color.app_orange));
        line.setBackgroundColor(getResources().getColor(R.color.app_orange));

    }

    private void setUnFocusColor(View line, AppCompatEditText editText) {
        editText.setTextColor(getResources().getColor(R.color.app_gray));
        line.setBackgroundColor(getResources().getColor(R.color.app_gray));

    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (isValidated()) {
            serviceHelper.enqueueCall(webService.ChangePassword(edtOldPassword.getText().toString()
                    , edtPassword.getText().toString(), edtConfirmPassword.getText().toString(), prefHelper.getUserToken()), WebServiceConstants.CHANGE_PASSWORD);
        }
    }

    private boolean isValidated() {
        if (edtOldPassword.getText().toString().isEmpty()) {
            edtOldPassword.setError(getString(R.string.enter_password));
            if (edtOldPassword.requestFocus()) {
                setEditTextFocus(edtOldPassword);
            }
            return false;
        } else if (edtPassword.getText().toString().isEmpty()) {
            edtPassword.setError(getString(R.string.enter_password));
            if (edtPassword.requestFocus()) {
                setEditTextFocus(edtPassword);
            }
            return false;
        } else if (edtPassword.getText().toString().length() < 6) {
            edtPassword.setError(getString(R.string.passwordLength));
            if (edtPassword.requestFocus()) {
                setEditTextFocus(edtPassword);
            }
            return false;
        } else if (!edtConfirmPassword.getText().toString().equals(edtPassword.getText().toString())) {
            edtConfirmPassword.setError(getString(R.string.confirm_password_error));
            if (edtConfirmPassword.requestFocus()) {
                setEditTextFocus(edtConfirmPassword);
            }
            return false;
        } else {
            return true;
        }
    }
}