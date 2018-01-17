package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.UserModel;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.DatePickerHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.ImageSetter;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created on 1/5/2018.
 */
public class EditProfileFragment extends BaseFragment implements View.OnFocusChangeListener, ImageSetter {
    @BindView(R.id.user_image)
    CircleImageView userImage;
    @BindView(R.id.btn_image)
    ImageView btnImage;
    @BindView(R.id.edt_email_address)
    AppCompatEditText edtEmailAddress;
    @BindView(R.id.edt_name)
    AppCompatEditText edtName;
    @BindView(R.id.btn_calender)
    RelativeLayout btnCalender;
    @BindView(R.id.btn_save)
    Button btnSave;
    Unbinder unbinder;
    @BindView(R.id.lineemail)
    View lineemail;
    @BindView(R.id.linename)
    View linename;
    @BindView(R.id.txt_date)
    AnyTextView txtDate;
    @BindView(R.id.inputlayout_email)
    TextInputLayout inputlayoutEmail;
    @BindView(R.id.inputlayout_name)
    TextInputLayout inputlayoutName;
    private File profilePic;
    private String profilePath;
    private String selectedDate = "";

    public static EditProfileFragment newInstance() {
        Bundle args = new Bundle();

        EditProfileFragment fragment = new EditProfileFragment();
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
            case WebServiceConstants.EDIT_PROFILE:
                UserModel user = prefHelper.getUser();
                user.setEmailAddress(edtEmailAddress.getText().toString());
                user.setFullName(edtName.getText().toString());
                user.setdOB(selectedDate);
                UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.edit_profile_message));
                getMainActivity().refreshSideMenu();
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.showNotificationButton(0);
        titleBar.setSubHeading(getString(R.string.edit_profile));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewsData();
        setViewsControls();
    }

    private void setViewsControls() {
        edtName.setOnFocusChangeListener(this);
        setFocusColor(lineemail, edtEmailAddress);
        setUnFocusColor(linename, edtName);
        edtEmailAddress.setInputType(InputType.TYPE_NULL);
        getMainActivity().setImageSetter(this);
    }

    private void setViewsData() {
        UserModel user = prefHelper.getUser();
        edtEmailAddress.setText(user.getEmailAddress());
        edtName.setText(user.getFullName());
        txtDate.setText(user.getdOB());
        ImageLoader.getInstance().displayImage(WebServiceConstants.IMAGE_PATH + user.getImageName(), userImage);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setUnFocusColor(linename, edtName);
        switch (v.getId()) {
            case R.id.edt_name:
                if (hasFocus) {
                    setFocusColor(linename, edtName);
                } else {
                    edtName.clearFocus();
                    inputlayoutName.clearFocus();
                    UIHelper.hideSoftKeyboard(getDockActivity(), edtName);
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
                selectedDate = new SimpleDateFormat(AppConstants.SERVER_DATE_FORMAT, Locale.ENGLISH)
                        .format(date.getTime());
                textView.setText(new SimpleDateFormat(AppConstants.DATE_FORMAT_APP, Locale.ENGLISH)
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

    @OnClick({R.id.btn_image, R.id.btn_calender, R.id.btn_save, R.id.btn_change_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_image:
                getMainActivity().pickImageForUser();
                break;
            case R.id.btn_calender:
                inputlayoutName.clearFocus();
                initDatePicker(txtDate);
                break;
            case R.id.btn_save:
                if (isValidated()) {
                    saveProfileChanges();
                }
                break;
            case R.id.btn_change_password:
                getDockActivity().replaceDockableFragment(ChangePasswordFragment.newInstance(), ChangePasswordFragment.TAG);
                break;
        }
    }

    private void saveProfileChanges() {
        MultipartBody.Part filePart;
        if (profilePic != null) {
            filePart = MultipartBody.Part.createFormData("ProfileImage",
                    profilePic.getName(), RequestBody.create(MediaType.parse("image/*"), profilePic));
        } else {
            filePart = MultipartBody.Part.createFormData("ProfileImage", "",
                    RequestBody.create(MediaType.parse("*/*"), ""));
        }
        serviceHelper.enqueueCall(webService.EditProfile(
                RequestBody.create(MediaType.parse("text/plain"), edtName.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), txtDate.getText().toString()),
                filePart,
                prefHelper.getUserToken()), WebServiceConstants.EDIT_PROFILE);
    }

    private boolean isValidated() {
        if (edtName.getText() == null || (edtName.getText().toString().isEmpty())) {
            if (edtName.requestFocus()) {
                setEditTextFocus(edtName);
                setFocusColor(linename, edtName);
            }
            edtName.setError(getString(R.string.enter_FullName));
            return false;
        } else if (edtEmailAddress.getText() == null && (edtEmailAddress.getText().toString().isEmpty()) &&
                (!Patterns.EMAIL_ADDRESS.matcher(edtEmailAddress.getText().toString()).matches())) {
            if (edtEmailAddress.requestFocus()) {
                setEditTextFocus(edtEmailAddress);
            }
            edtEmailAddress.setError(getString(R.string.enter_valid_email));
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void setImage(String imagePath) {
        ImageLoader.getInstance().displayImage(AppConstants.FILE_PATH + imagePath, userImage);
        try {
            profilePic = new Compressor(getDockActivity()).compressToFile(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        profilePath = imagePath;

    }

    @Override
    public void setFilePath(String filePath) {

    }

    @Override
    public void setVideo(String videoPath, String VideoThumbail) {

    }


}