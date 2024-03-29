package com.ingic.auditix.fragments.standard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NavigationEnt;
import com.ingic.auditix.entities.UserModel;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.dashboard.DashboardFragment;
import com.ingic.auditix.fragments.dashboard.NotificationsFragment;
import com.ingic.auditix.fragments.settings.SettingsFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.DialogHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.LanguageInterface;
import com.ingic.auditix.ui.adapters.ArrayListAdapter;
import com.ingic.auditix.ui.binders.NavigationBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class SideMenuFragment extends BaseFragment implements LanguageInterface {

    @BindView(R.id.img_profile)
    CircleImageView imgProfile;
    @BindView(R.id.txt_username)
    AnyTextView txtUsername;
    @BindView(R.id.txt_useraddress)
    AnyTextView txtUseraddress;
    @BindView(R.id.sideoptions)
    ListView sideoptions;
    Unbinder unbinder;
    @BindView(R.id.img_user_blur)
    ImageView imgUserBlur;
    private ArrayList<NavigationEnt> navigationEnts;
    private ArrayListAdapter<NavigationEnt> adapter;

    public static SideMenuFragment newInstance() {
        return new SideMenuFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<NavigationEnt>(getDockActivity(), new NavigationBinder(prefHelper, getDockActivity(), this));
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sidemenu, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachDataToViews();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    private void attachDataToViews() {
        initView();
        UserModel user = prefHelper.getUser();
        if (user != null) {
            txtUsername.setText(user.getFullName());
            txtUseraddress.setText(user.getEmailAddress());
            ImageLoader.getInstance().displayImage(WebServiceConstants.IMAGE_PATH + user.getImageName(), imgProfile);
            ImageLoader.getInstance().loadImage(WebServiceConstants.IMAGE_PATH + user.getImageName(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    loadBlurBitmap(BitmapFactory.decodeResource(getMainActivity().getResources(),
                            com.facebook.R.drawable.com_facebook_profile_picture_blank_portrait));
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    loadBlurBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    loadBlurBitmap(BitmapFactory.decodeResource(getMainActivity().getResources(),
                            com.facebook.R.drawable.com_facebook_profile_picture_blank_portrait));
                }
            });
            imgProfile.invalidate();
            txtUseraddress.invalidate();
            txtUsername.invalidate();
            adapter.notifyDataSetChanged();
        }
    }

    private void loadBlurBitmap(Bitmap image) {
      /*  Blurry.with(getMainActivity())
                .radius(10)
                .sampling(10)
                .async()
                .from(image)
                .into(imgUserBlur);*/
        if (imgUserBlur != null)
            imgUserBlur.setImageBitmap(image);
    }

    public void refreshMenuData() {
        attachDataToViews();
    }

    private void initView() {
        navigationEnts = new ArrayList<>();
        navigationEnts.add(new NavigationEnt(R.drawable.home_grey, getDockActivity().getResources().getString(R.string.home), 0));
        //  navigationEnts.add(new NavigationEnt(R.drawable.dashboard_grey, getDockActivity().getResources().getString(R.string.dashboard), 0));
        //  navigationEnts.add(new NavigationEnt(R.drawable.notifications_grey, getDockActivity().getResources().getString(R.string.notification), prefHelper.getNotificationCount()));
        navigationEnts.add(new NavigationEnt(R.drawable.settings, getDockActivity().getResources().getString(R.string.settings), 0));
        navigationEnts.add(new NavigationEnt(R.drawable.about, getDockActivity().getResources().getString(R.string.about), 0));
        navigationEnts.add(new NavigationEnt(R.drawable.about, getDockActivity().getResources().getString(R.string.contact_us), 0));
        navigationEnts.add(new NavigationEnt(R.drawable.about, getDockActivity().getResources().getString(R.string.language), 0));
        navigationEnts.add(new NavigationEnt(R.drawable.logout_grey, prefHelper.isGuest() ? getResString(R.string.login) : getResString(R.string.logout), 0));
        bindview();
    }

    private void bindview() {
        adapter.clearList();
        sideoptions.setAdapter(adapter);
        adapter.addAll(navigationEnts);
        adapter.notifyDataSetChanged();
        sideoptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (navigationEnts.get(position).getTitle().equals(getString(R.string.home))) {
                    getMainActivity().closeDrawer();
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.dashboard))) {
                    if (prefHelper.isGuest()) {
                        showGuestMessage();
                        return;
                    }
                    getDockActivity().replaceDockableFragment(DashboardFragment.newInstance(), "DashboardFragment");
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.notification))) {
                    if (prefHelper.isGuest()) {
                        showGuestMessage();
                        return;
                    }
                    getDockActivity().replaceDockableFragment(NotificationsFragment.newInstance(), NotificationsFragment.class.getSimpleName());
                } else if (navigationEnts.get(position).getTitle().equals(getResources().getString(R.string.settings))) {
                    getDockActivity().replaceDockableFragment(SettingsFragment.newInstance(), SettingsFragment.TAG);
                } else if (navigationEnts.get(position).getTitle().equals(getResources().getString(R.string.about))) {
                    getDockActivity().replaceDockableFragment(AboutUsFragment.newInstance(), AboutUsFragment.TAG);
                } else if (navigationEnts.get(position).getTitle().equals(getResources().getString(R.string.contact_us))) {
                    getDockActivity().replaceDockableFragment(ContactUsFragment.newInstance(), ContactUsFragment.TAG);
                } else if (navigationEnts.get(position).getTitle().equals(getString(R.string.logout))) {
                    final DialogHelper logoutdialog = new DialogHelper(getDockActivity());
                    logoutdialog.initlogout(R.layout.dialog_logout, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // File dir = new File(AppConstants.DOWNLOAD_PATH);
                            // DeleteRecursive(dir);
                            getMainActivity().hideBottomPlayer();
                            prefHelper.setLoginStatus(false);
                            getDockActivity().popBackStackTillEntry(0);
                            getDockActivity().replaceDockableFragment(AccessSelectionFragment.newInstance(), "AccessSelectionFragment");
                            logoutdialog.hideDialog();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            logoutdialog.hideDialog();
                        }
                    });
                    logoutdialog.setCancelable(false);
                    logoutdialog.showDialog();
                } else if (navigationEnts.get(position).getTitle().equals(getResources().getString(R.string.login))) {
                    getMainActivity().hideBottomPlayer();
                    prefHelper.setLoginStatus(false);
                    prefHelper.setGuestStatus(false);
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(AccessSelectionFragment.newInstance(), "AccessSelectionFragment");
                }
            }
        });
    }

    void DeleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();

    }

    @Override
    public void changeListner(NavigationEnt entity, int position, boolean b) {
        UIHelper.showShortToastInCenter(getDockActivity(), "will be implemented in future version");
    }
}
