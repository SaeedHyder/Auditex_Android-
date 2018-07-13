package com.ingic.auditix.fragments.standard;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.UserModel;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.DatePickerHelper;
import com.ingic.auditix.helpers.TokenUpdater;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.TitleBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 12/13/2017.
 */
public class SignUpFragment extends BaseFragment implements View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.edt_email_address)
    AppCompatEditText edtEmailAddress;
    @BindView(R.id.inputlayout_email)
    TextInputLayout inputlayoutEmail;
    @BindView(R.id.lineemail)
    View lineemail;
    @BindView(R.id.edt_name)
    AppCompatEditText edtName;
    @BindView(R.id.inputlayout_name)
    TextInputLayout inputlayoutName;
    @BindView(R.id.linename)
    View linename;
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;
    @BindView(R.id.img_calender)
    ImageView imgCalender;
    @BindView(R.id.btn_calender)
    RelativeLayout btnCalender;
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
    @BindView(R.id.btn_signup)
    Button btnSignup;
    Unbinder unbinder;
    @BindView(R.id.txt_date)
    AnyTextView txtDate;
    @BindView(R.id.phoneCountryPicker)
    CountryCodePicker phoneCountryPicker;
    @BindView(R.id.edt_phone)
    AppCompatEditText edtPhone;
    @BindView(R.id.inputlayout_phone)
    TextInputLayout inputlayoutPhone;
    @BindView(R.id.linephone)
    View linephone;
    @BindView(R.id.Countrypicker)
    CountryCodePicker spnCountryPicker;
    @BindView(R.id.txtCountry)
    AnyTextView txtCountry;
    @BindView(R.id.imgCountry)
    ImageView imgCountry;
    @BindView(R.id.btnCountry)
    RelativeLayout btnCountry;
    @BindView(R.id.btnPhoneCountryPicker)
    AnyTextView btnPhoneCountryPicker;
    private int genderType = WebServiceConstants.GENDER_MALE;
    private CountryCodePicker.OnCountryChangeListener countrySelectListener = new CountryCodePicker.OnCountryChangeListener() {
        @Override
        public void onCountrySelected() {
            setCountryNames();

        }
    };

    public static SignUpFragment newInstance() {
        Bundle args = new Bundle();

        SignUpFragment fragment = new SignUpFragment();
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
            case WebServiceConstants.REGISTER:
                UserModel user = (UserModel) result;
                prefHelper.putUser(user);
                prefHelper.setUserToken(user.getToken().getTokenType() + " " + user.getToken().getAccessToken());
                prefHelper.setLoginStatus(true);
                prefHelper.setGuestStatus(false);
                TokenUpdater.getInstance().UpdateToken(getDockActivity(), user.getAccountID(), FirebaseInstanceId.getInstance().getToken(),
                        prefHelper.getUserToken(), prefHelper.isGuest());

                getDockActivity().replaceDockableFragment(VerifyMobileFragment.newInstance(), VerifyMobileFragment.TAG);
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
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();

    }

    private void setListeners() {
        edtName.setOnFocusChangeListener(this);
        edtEmailAddress.setOnFocusChangeListener(this);
        edtPassword.setOnFocusChangeListener(this);
        edtConfirmPassword.setOnFocusChangeListener(this);
        edtPhone.setOnFocusChangeListener(this);
        rbMale.setOnCheckedChangeListener(this);
        spnCountryPicker.setOnCountryChangeListener(countrySelectListener);
        phoneCountryPicker.setOnCountryChangeListener(countrySelectListener);
        phoneCountryPicker.registerCarrierNumberEditText(edtPhone);
        setCountryNames();
    }

    private void setCountryNames() {
        txtCountry.setText(spnCountryPicker.getSelectedCountryName());
        btnPhoneCountryPicker.setText(String.format("%s ( %s ) ", phoneCountryPicker.getSelectedCountryNameCode(), phoneCountryPicker.getSelectedCountryCodeWithPlus()));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setUnFocusColor(lineemail, edtEmailAddress);
        setUnFocusColor(linename, edtName);
        setUnFocusColor(linepassword, edtPassword);
        setUnFocusColor(lineconfirmpassword, edtConfirmPassword);
        setUnFocusColor(linephone, edtPhone);
        switch (v.getId()) {
            case R.id.edt_email_address:
                if (hasFocus) {
                    setFocusColor(lineemail, edtEmailAddress);
                }
                break;
            case R.id.edt_phone:
                if (hasFocus) {
                    setFocusColor(linephone, edtPhone);
                }
                break;
            case R.id.edt_name:
                if (hasFocus) {
                    setFocusColor(linename, edtName);
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

    private void initDatePicker(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        final DatePickerHelper datePickerHelper = new DatePickerHelper();
        datePickerHelper.initDateDialog(
                getDockActivity(),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
                , "PreferredDate");
        datePickerHelper.setListener(new DatePickerHelper.OnDateSelectedListener() {
            @Override
            public void onDatePicked(Calendar date) {
                textView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                        .format(date.getTime()));
            }
        });
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
        datePickerHelper.setmaxDate(c.getTime().getTime());
        datePickerHelper.showDate();
    }

    private void setFocusColor(View line, AppCompatEditText editText) {
        editText.setTextColor(getResources().getColor(R.color.app_orange));
        line.setBackgroundColor(getResources().getColor(R.color.app_orange));

    }

    private void setUnFocusColor(View line, AppCompatEditText editText) {
        editText.setTextColor(getResources().getColor(R.color.app_gray));
        line.setBackgroundColor(getResources().getColor(R.color.app_gray));

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_male:
                if (isChecked) {
                    genderType = WebServiceConstants.GENDER_MALE;
                    rbMale.setTextColor(getResources().getColor(R.color.app_orange));
                    rbFemale.setTextColor(getResources().getColor(R.color.app_gray));
                } else {
                    genderType = WebServiceConstants.GENDER_FEMALE;
                    rbMale.setTextColor(getResources().getColor(R.color.app_gray));
                    rbFemale.setTextColor(getResources().getColor(R.color.app_orange));
                }
                break;
        }
    }

    private boolean isValidated() {
        if (edtName.getText() == null || (edtName.getText().toString().isEmpty())) {
            if (edtName.requestFocus()) {
                setEditTextFocus(edtName);
            }
            edtName.setError(getString(R.string.enter_FullName));
            return false;
        } else if (edtEmailAddress.getText() == null || (edtEmailAddress.getText().toString().isEmpty()) ||
                (!Patterns.EMAIL_ADDRESS.matcher(edtEmailAddress.getText().toString()).matches())) {
            if (edtEmailAddress.requestFocus()) {
                setEditTextFocus(edtEmailAddress);
            }
            edtEmailAddress.setError(getString(R.string.enter_valid_email));
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
        } else if (!phoneCountryPicker.isValidFullNumber()) {
            edtPhone.setError(getString(R.string.invalid_phone_number));
            if (edtPhone.requestFocus()) {
                setEditTextFocus(edtPhone);
            }
            return false;
        } else if (txtDate.getText() == null || (txtDate.getText().toString().isEmpty())) {
            UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.dob_error_message));
            return false;
        } else {
            return true;
        }

    }

    @OnClick({R.id.txt_date, R.id.btn_signup, R.id.btnCountry, R.id.btnPhoneCountryPicker})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_date:
                initDatePicker(txtDate);
                break;
            case R.id.btnCountry:
                spnCountryPicker.launchCountrySelectionDialog();
                break;
            case R.id.btnPhoneCountryPicker:
                phoneCountryPicker.launchCountrySelectionDialog();
                break;
            case R.id.btn_signup:
                if (isValidated()) {
                    serviceHelper.enqueueCall(webService.registerUser(edtName.getText().toString(), edtEmailAddress.getText().toString(), edtPassword.getText().toString(), edtConfirmPassword.getText().toString(),
                            txtDate.getText().toString(), genderType, txtCountry.getText().toString(), phoneCountryPicker.getFullNumberWithPlus()), WebServiceConstants.REGISTER);
                }
                break;
        }
    }
}