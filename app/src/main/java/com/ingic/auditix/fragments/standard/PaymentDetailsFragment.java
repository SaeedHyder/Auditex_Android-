package com.ingic.auditix.fragments.standard;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.helpers.DatePickerHelper;
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
 * Created on 1/29/2018.
 */
public class PaymentDetailsFragment extends BaseFragment implements View.OnFocusChangeListener {
    public static final String TAG = "PaymentDetailsFragment";
    @BindView(R.id.edt_cc_number)
    AppCompatEditText edtCcNumber;
    @BindView(R.id.inputlayout_cc_number)
    TextInputLayout inputlayoutCcNumber;
    @BindView(R.id.linecc_number)
    View lineccNumber;
    @BindView(R.id.edt_ccv)
    AppCompatEditText edtCcv;
    @BindView(R.id.inputlayout_ccv)
    TextInputLayout inputlayoutCcv;
    @BindView(R.id.lineccv)
    View lineccv;
    @BindView(R.id.txt_date)
    AnyTextView txtDate;
    @BindView(R.id.img_calender)
    ImageView imgCalender;
    @BindView(R.id.btn_calender)
    RelativeLayout btnCalender;
    @BindView(R.id.edt_name)
    AppCompatEditText edtName;
    @BindView(R.id.inputlayout_name)
    TextInputLayout inputlayoutName;
    @BindView(R.id.linename)
    View linename;
    @BindView(R.id.btn_buy_now)
    Button btnBuyNow;
    Unbinder unbinder;

    public static PaymentDetailsFragment newInstance() {
        Bundle args = new Bundle();

        PaymentDetailsFragment fragment = new PaymentDetailsFragment();
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
        titleBar.showBackButton();
        titleBar.addBackground();
        titleBar.setSubHeading(getResString(R.string.billing_details));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_detail, container, false);
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

    private void setFocusColor(View line, AppCompatEditText editText) {
        editText.setTextColor(getResources().getColor(R.color.app_orange));
        line.setBackgroundColor(getResources().getColor(R.color.app_orange));

    }

    private void setUnFocusColor(View line, AppCompatEditText editText) {
        editText.setTextColor(getResources().getColor(R.color.app_gray));
        line.setBackgroundColor(getResources().getColor(R.color.app_gray));

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        setUnFocusColor(lineccNumber, edtCcNumber);
        setUnFocusColor(linename, edtName);
        setUnFocusColor(lineccv, edtCcv);
        switch (view.getId()) {
            case R.id.edt_cc_number:
                if (hasFocus) {
                    setFocusColor(lineccNumber, edtCcNumber);
                }
                break;
            case R.id.edt_name:
                if (hasFocus) {
                    setFocusColor(linename, edtName);
                }
                break;
            case R.id.edt_ccv:
                if (hasFocus) {
                    setFocusColor(lineccv, edtCcv);
                }
                break;

        }
    }


    @OnClick({R.id.txt_date, R.id.btn_buy_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_date:
                initDatePicker(txtDate);
                break;
            case R.id.btn_buy_now:
                if (isValidated()) {
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
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
                textView.setText(new SimpleDateFormat("MMM yyyy", Locale.ENGLISH)
                        .format(date.getTime()));
            }
        });
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
        datePickerHelper.setmaxDate(c.getTime().getTime());

        datePickerHelper.showDate();
    }

    private boolean isValidated() {
        if (edtCcNumber.getText().toString().isEmpty()) {
            edtCcNumber.setError(getString(R.string.enter_correct_cc));
            if (edtCcNumber.requestFocus()) {
                setEditTextFocus(edtCcNumber);
            }
            return false;
        } else if (edtCcNumber.getText().toString().length() < 14) {
            edtCcNumber.setError(getString(R.string.cclenght));
            if (edtCcNumber.requestFocus()) {
                setEditTextFocus(edtCcNumber);
            }
            return false;
        } else if (edtCcv.getText().toString().length() < 4 || edtCcv.getText().toString().equalsIgnoreCase("")) {
            edtCcv.setError(getString(R.string.ccv_error));
            if (edtCcv.requestFocus()) {
                setEditTextFocus(edtCcv);
            }
            return false;
        } else if (txtDate.getText() == null || (txtDate.getText().toString().isEmpty())) {
            UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.expire_error_message));
            return false;
        } else if (edtName.getText() == null || (edtName.getText().toString().isEmpty())) {
            if (edtName.requestFocus()) {
                setEditTextFocus(edtName);
            }
            edtName.setError(getString(R.string.enter_FullName));
            return false;
        } else {
            return true;
        }

    }


}