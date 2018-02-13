package com.ingic.auditix.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.facebook.FacebookSdk;
import com.ingic.auditix.R;
import com.ingic.auditix.fragments.BooksFilterFragment;
import com.ingic.auditix.fragments.FilterFragment;
import com.ingic.auditix.fragments.HomeFragment;
import com.ingic.auditix.fragments.LanguageSelectionFragment;
import com.ingic.auditix.fragments.NotificationsFragment;
import com.ingic.auditix.fragments.SideMenuFragment;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.SideMenuChooser;
import com.ingic.auditix.global.SideMenuDirection;
import com.ingic.auditix.helpers.Blur;
import com.ingic.auditix.helpers.ScreenHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.helpers.Utilities;
import com.ingic.auditix.interfaces.ImageSetter;
import com.ingic.auditix.residemenu.ResideMenu;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.Orientation;
import io.realm.Realm;


public class MainActivity extends DockActivity implements OnClickListener {
    private static final String TAG = "MainActivity";
    //region User Image Picker
    private static final int MAX_IMAGE_PICK_COUNT = 1;
    public TitleBar titleBar;
    @BindView(R.id.sideMneuFragmentContainer)
    public FrameLayout sideMneuFragmentContainer;
    @BindView(R.id.filterFragmentContainer)
    public FrameLayout filterFragmentContainer;
    public boolean isNavigationGravityRight = false;
    @BindView(R.id.header_main)
    TitleBar header_main;
    @BindView(R.id.mainFrameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.content_frame)
    RelativeLayout contentView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.imageBlur)
    ImageView imageBlur;
    private MainActivity mContext;
    private boolean loading;
    private ResideMenu resideMenu;
    private float lastTranslate = 0.0f;
    private String sideMenuType;
    private String sideMenuDirection;
    private ImageSetter imageSetter;
    //For Blurred Background
    private Bitmap mDownScaled;
    private String mBackgroundFilename;
    private Bitmap background;

    public View getDrawerView() {
        return getLayoutInflater().inflate(getSideMenuFrameLayoutId(), null);
    }

    public DisplayImageOptions getImageLoaderRoundCornerTransformation(int raduis) {
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.color.black)
                .showImageOnFail(R.color.black).resetViewBeforeLoading(true)
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .displayer(new RoundedBitmapDisplayer(raduis))
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    public void setBlurBackground() {

        if (mBackgroundFilename == null) {

            this.mDownScaled = Utilities.drawViewToBitmap(this.getContentView(), Color.parseColor("#fff5f5f5"));

            mBackgroundFilename = getBlurredBackgroundFilename();
            if (!TextUtils.isEmpty(mBackgroundFilename)) {
                //context.getMainContentFrame().setVisibility(View.VISIBLE);
                background = Utilities.loadBitmapFromFile(mBackgroundFilename);
                if (background != null) {
                    getBlurImage().setVisibility(View.VISIBLE);
                    getBlurImage().setImageBitmap(background);
                    getBlurImage().animate().alpha(1);
                }
            }
        } else {
            getBlurImage().setVisibility(View.VISIBLE);
            getBlurImage().setImageBitmap(background);
            getBlurImage().animate().alpha(1);
        }
    }

    private ImageView getBlurImage() {
        return imageBlur;
    }


    public String getBlurredBackgroundFilename() {
        Bitmap localBitmap = Blur.fastblur(this, this.mDownScaled, 20);
        String str = Utilities.saveBitmapToFile(this, localBitmap);
        this.mDownScaled.recycle();
        localBitmap.recycle();
        return str;
    }

    public void removeBlurImage() {
        getBlurImage().setVisibility(View.GONE);
    }

    public void setFlagKeepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void clearFlagKeepScreenOn() {
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void setRightSideDrawer() {
        DisplayMetrics matrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrics);
        Long longwidth = Math.round(matrics.widthPixels * 0.70);
        int drawerwidth = longwidth.intValue();
        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(drawerwidth, (int) DrawerLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;
        filterFragmentContainer.setLayoutParams(params);
        drawerLayout.setScrimColor(getResources().getColor(R.color.semi_tranparent));
    }

    public void setRightSideFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(getFilterFrameLayoutId(), fragment).commit();
    }

    private void settingSideMenu(String type, String direction) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (type.equals(SideMenuChooser.DRAWER.getValue())) {

            DisplayMetrics matrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(matrics);
            Long longwidth = Math.round(matrics.widthPixels * 0.75);
            int drawerwidth = longwidth.intValue();
            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(drawerwidth, (int) DrawerLayout.LayoutParams.MATCH_PARENT);


            if (direction.equals(SideMenuDirection.LEFT.getValue())) {
                params.gravity = Gravity.LEFT;
                sideMneuFragmentContainer.setLayoutParams(params);
            } else {
                params.gravity = Gravity.RIGHT;
                sideMneuFragmentContainer.setLayoutParams(params);
            }


            sideMenuFragment = SideMenuFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(getSideMenuFrameLayoutId(), sideMenuFragment).commit();
            setDrawerListeners();
            drawerLayout.closeDrawers();
        } else {
            resideMenu = new ResideMenu(this);
            resideMenu.attachToActivity(this);
            resideMenu.setMenuListener(getMenuListener());
            resideMenu.setScaleValue(0.35f);

            setMenuItemDirection(direction);
        }
    }

    private void setDrawerListeners() {
        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (!isNavigationGravityRight) {
                    contentView.setTranslationX(slideOffset * drawerView.getWidth());
                    drawerLayout.bringChildToFront(drawerView);
                    drawerLayout.requestLayout();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isNavigationGravityRight = false;
                drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (!isNavigationGravityRight) {
                    if (newState == DrawerLayout.STATE_SETTLING) {
                        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                            /*Blurry.with(MainActivity.this)
                                    .radius(15)
                                    .sampling(3)
                                    .async()
                                    .animate(500)
                                    .onto(contentView);*/
                            setBlurBackground();
                        } else {
                            // Blurry.delete(contentView);
                            removeBlurImage();
                        }
                    }
                }
            }
        });
    }

    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    private void setMenuItemDirection(String direction) {

        if (direction.equals(SideMenuDirection.LEFT.getValue())) {

            SideMenuFragment leftSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(leftSideMenuFragment, "LeftSideMenuFragment", SideMenuDirection.LEFT.getValue());

        } else if (direction.equals(SideMenuDirection.RIGHT.getValue())) {

            SideMenuFragment rightSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(rightSideMenuFragment, "RightSideMenuFragment", SideMenuDirection.RIGHT.getValue());

        }

    }

    private int getSideMenuFrameLayoutId() {
        return R.id.sideMneuFragmentContainer;

    }

    private int getFilterFrameLayoutId() {
        return R.id.filterFragmentContainer;

    }

    public void initFragment() {
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        if (prefHelper.isLogin()) {
            replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
        } else {
            replaceDockableFragment(LanguageSelectionFragment.newInstance(), "LanguageSelectionFragment");
        }
    }

    public void addMainframeBelowTitlebar() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, R.id.header_main);
        mainFrameLayout.setLayoutParams(params);
    }

    public void removeMainframeBelowTitlebar() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, 0);
        mainFrameLayout.setLayoutParams(params);
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    BaseFragment currFrag = (BaseFragment) manager.findFragmentById(getDockFrameLayoutId());
                    if (currFrag != null) {
                        currFrag.fragmentResume();
                    }
                }
            }
        };

        return result;
    }

    @Override
    public void onLoadingStarted() {

        if (mainFrameLayout != null) {
            mainFrameLayout.setVisibility(View.VISIBLE);
            if (progressBar != null) {
                progressBar.setVisibility(View.INVISIBLE);
            }
            loading = true;
        }
    }

    @Override
    public void onLoadingFinished() {
        mainFrameLayout.setVisibility(View.VISIBLE);

        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        loading = false;

    }

    @Override
    public void onProgressUpdated(int percentLoaded) {

    }

    @Override
    public int getDockFrameLayoutId() {
        return R.id.mainFrameLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_dock);
        ButterKnife.bind(this);
        titleBar = header_main;
        titleBar.setMainActivity(this);
        // setBehindContentView(R.layout.fragment_frame);
        mContext = this;
        Log.i("Screen Density", ScreenHelper.getDensity(this) + "");

        sideMenuType = SideMenuChooser.DRAWER.getValue();
        sideMenuDirection = SideMenuDirection.LEFT.getValue();

        settingSideMenu(sideMenuType, sideMenuDirection);
        setRightSideDrawer();
        initFiltersFragment();
        titleBar.setMenuButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sideMenuType.equals(SideMenuChooser.DRAWER.getValue()) && getDrawerLayout() != null) {
                    if (sideMenuDirection.equals(SideMenuDirection.LEFT.getValue())) {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                } else {
                    resideMenu.openMenu(SideMenuDirection.LEFT.getValue());


                }

            }
        });

        titleBar.setBackButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (loading) {
                    UIHelper.showLongToastInCenter(getApplicationContext(),
                            R.string.message_wait);
                } else {

                    popFragment();
                    UIHelper.hideSoftKeyboard(getApplicationContext(),
                            titleBar);
                }
            }
        });

        titleBar.setNotificationButtonListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceDockableFragment(NotificationsFragment.newInstance(), "NotificationsFragment");
            }
        });

        if (savedInstanceState == null)
            initFragment();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT) || drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeDrawer();
        } else {
            if (loading) {
                UIHelper.showLongToastInCenter(getApplicationContext(),
                        R.string.message_wait);
            } else
                super.onBackPressed();
        }
    }

    @Override
    public void onMenuItemActionCalled(int actionId, String data) {

    }

    @Override
    public void setSubHeading(String subHeadText) {

    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void hideHeaderButtons(boolean leftBtn, boolean rightBtn) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) { // guard against weird low-budget phones
            realm.close();
            realm = null;
        }
    }

    private void initFiltersFragment() {
        filterFragment = FilterFragment.newInstance();
        booksFilterFragment = BooksFilterFragment.newInstance();
    }

    public RelativeLayout getContentView() {
        return contentView;
    }

    private void notImplemented() {
        UIHelper.showLongToastInCenter(this, "Coming Soon");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        setCurrentLocale();
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            if (data != null) {
                imageSetter.setImage(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).get(0));
            }

        }

    }

    public void refreshSideMenu() {
        sideMenuFragment.refreshMenuData();
    }

    public void pickImageForUser() {
        FilePickerBuilder.getInstance().setMaxCount(MAX_IMAGE_PICK_COUNT)
                .enableCameraSupport(true)
                .enableVideoPicker(false)
                .enableDocSupport(false)
                .enableSelectAll(false)
                .showGifs(false)
                .withOrientation(Orientation.PORTRAIT_ONLY)
                .showFolderView(false)
                .setActivityTheme(R.style.AppTheme)
                .pickPhoto(this);
    }

    public void pickImageForUser(int count, BaseFragment fragment) {
        FilePickerBuilder.getInstance().setMaxCount(count)
                .enableCameraSupport(true)
                .enableVideoPicker(false)
                .enableDocSupport(false)
                .enableSelectAll(false)
                .showGifs(false)
                .withOrientation(Orientation.PORTRAIT_ONLY)
                .showFolderView(false)
                .setActivityTheme(R.style.AppTheme)
                .pickPhoto(fragment);
    }

    public void setImageSetter(ImageSetter imageSetter) {
        this.imageSetter = imageSetter;
    }

    @Override
    public void onClick(View view) {

    }


    //endregion

}
