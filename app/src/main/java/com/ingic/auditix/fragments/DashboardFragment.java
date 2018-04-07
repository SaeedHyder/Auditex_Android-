package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.UserModel;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created on 12/18/2017.
 */
public class DashboardFragment extends BaseFragment {

    @BindView(R.id.img_background_user_image)
    ImageView imgBackgroundUserImage;
    @BindView(R.id.img_profile)
    CircleImageView imgProfile;
    @BindView(R.id.txt_username)
    AnyTextView txtUsername;
    @BindView(R.id.btn_cart)
    ImageView btnCart;
    @BindView(R.id.txt_cart)
    AnyTextView txtCart;
    @BindView(R.id.btn_cc)
    ImageView btnCc;
    @BindView(R.id.txt_cc)
    AnyTextView txtCc;
    @BindView(R.id.btn_favourite)
    ImageView btnFavourite;
    @BindView(R.id.txt_favourite)
    AnyTextView txtFavourite;
    @BindView(R.id.btn_edit_profile)
    ImageView btnEditProfile;
    @BindView(R.id.txt_edit_profile)
    AnyTextView txtEditProfile;
    Unbinder unbinder;

    public static DashboardFragment newInstance() {
        Bundle args = new Bundle();

        DashboardFragment fragment = new DashboardFragment();
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
        titleBar.setSubHeading(getString(R.string.dashboard));
        titleBar.showBackButton();
       // titleBar.showNotificationButton(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindUserData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void bindUserData() {
        UserModel user = prefHelper.getUser();
        if (user != null) {
            txtUsername.setText(user.getFullName());
            ImageLoader.getInstance().displayImage(WebServiceConstants.IMAGE_PATH + user.getImageName(), imgProfile);
            ImageLoader.getInstance().displayImage(WebServiceConstants.IMAGE_PATH + user.getImageName(), imgBackgroundUserImage);
        }
    }

    @OnClick({R.id.btn_cart, R.id.btn_cc, R.id.btn_favourite, R.id.btn_edit_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cart:
                getDockActivity().replaceDockableFragment(CartListFragment.newInstance(), "CartListFragment");
                break;
            case R.id.btn_cc:
               getDockActivity().replaceDockableFragment(NotificationsFragment.newInstance(),NotificationsFragment.TAG);
                break;
            case R.id.btn_favourite:
                getDockActivity().replaceDockableFragment(FavoriteTabFragment.newInstance(), FavoriteTabFragment.TAG);
                break;
            case R.id.btn_edit_profile:
                getDockActivity().replaceDockableFragment(EditProfileFragment.newInstance(), "EditProfileFragment");
                break;
        }
    }
}