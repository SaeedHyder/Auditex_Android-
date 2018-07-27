package com.ingic.auditix.fragments.abstracts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatEditText;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;
import com.ingic.auditix.R;
import com.ingic.auditix.activities.DockActivity;
import com.ingic.auditix.activities.MainActivity;
import com.ingic.auditix.fragments.books.BooksMainFragment;
import com.ingic.auditix.fragments.news.NewsMainFragment;
import com.ingic.auditix.fragments.podcast.PodcastMainFragment;
import com.ingic.auditix.fragments.profile.ProfileMainFragment;
import com.ingic.auditix.fragments.search.SearchMainFragment;
import com.ingic.auditix.fragments.standard.HomeTabFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.helpers.ChildBackPressHelper;
import com.ingic.auditix.helpers.GPSTracker;
import com.ingic.auditix.helpers.ServiceHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.LoadingListener;
import com.ingic.auditix.interfaces.OnBackPressListener;
import com.ingic.auditix.interfaces.webServiceResponseLisener;
import com.ingic.auditix.retrofit.WebService;
import com.ingic.auditix.retrofit.WebServiceFactory;
import com.ingic.auditix.ui.views.AnyEditTextView;
import com.ingic.auditix.ui.views.TitleBar;


public abstract class BaseFragment extends Fragment implements webServiceResponseLisener, OnBackPressListener {

    protected Handler handler = new Handler();


    protected BasePreferenceHelper prefHelper;

    protected WebService webService;
    protected ServiceHelper serviceHelper;

    protected GPSTracker mGpsTracker;

    protected DockActivity myDockActivity;
    //private DockActivity activity;
    /**
     * Trigger when receives broadcasts from device to check wifi connectivity
     * using connectivity manager
     * <p>
     * Usage : registerBroadcastReceiver() on resume of activity to receive
     * notifications where needed and unregisterBroadcastReceiver() when not
     * needed.
     *
     * @return The connectivity of wifi/mobile carrier connectivity.
     */

    protected BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            boolean isWifiConnected = false;
            boolean isMobileConnected = false;

            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (networkInfo != null)
                isWifiConnected = networkInfo.isConnected();

            networkInfo = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (networkInfo != null)
                isMobileConnected = networkInfo.isConnected();

            Log.d("NETWORK STATUS", "wifi==" + isWifiConnected + " & mobile=="
                    + isMobileConnected);
        }
    };
    private boolean isLoading;
    private BroadcastReceiver broadcastReceiver;

    protected void willbeimplementedinBeta() {
        UIHelper.showShortToastInCenter(getDockActivity(), "Will be implemented in Beta Version");
    }

    protected void willbeimplementedinfuture() {
        UIHelper.showShortToastInCenter(getDockActivity(), "Will be implemented in Next Module");
    }

    public void fragmentResume() {
        setTitleBar(((MainActivity) getDockActivity()).titleBar);

    }

    @Override
    public boolean onBackPressed() {
        return isAdded() && new ChildBackPressHelper(this).onBackPressed();
    }

    private void onNotificationReceived() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction() != null) {
                    if (intent.getAction().equals(AppConstants.NOTIFICATION_COUNT_RECIEVED)) {
                        getMainActivity().refreshSideMenu();

                    } else if (intent.getAction().equals(AppConstants.PUSH_NOTIFICATION)) {
                        // new push notification is received
                        Bundle bundle = intent.getExtras();
                        if (bundle != null) {

                        }
                        onPushAction(intent);
                        System.out.println(prefHelper.getFirebase_TOKEN());
                    }
                }
            }
        };
    }

    public HomeTabFragment getParentfragment() {
        return (HomeTabFragment) getParentFragment();
    }

    protected void onPushAction(Intent intent) {
    }

    protected void createClient() {
        // webService = WebServiceFactory.getInstanceWithBasicGsonConversion();

    }

    public NewsMainFragment getNewsMainFragment() {
        return (NewsMainFragment) getParentFragment();
    }

    public PodcastMainFragment getPodcastMainFragment() {
        return (PodcastMainFragment) getParentFragment();
    }

    public BooksMainFragment getBooksMainFragment() {
        return (BooksMainFragment) getParentFragment();
    }

    public SearchMainFragment getSearchMainFragment() {
        return (SearchMainFragment) getParentFragment();
    }

    public ProfileMainFragment getProfileMainFragment() {
        return (ProfileMainFragment) getParentFragment();
    }

    protected void setEditTextFocus(AppCompatEditText textFocus) {
        InputMethodManager imm = (InputMethodManager) getDockActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(textFocus, InputMethodManager.SHOW_IMPLICIT);
    }

    protected void loadingStarted() {

        if (getParentFragment() != null)
            ((LoadingListener) getParentFragment()).onLoadingStarted();
        else
            getDockActivity().onLoadingStarted();

        isLoading = true;
    }

    public void showGuestMessage() {
        UIHelper.showShortToastInCenter(getDockActivity(), getResString(R.string.guest_message));
    }

    public void loadingFinished() {

        if (getParentFragment() != null)
            ((LoadingListener) getParentFragment()).onLoadingFinished();
        else if (getDockActivity() != null)
            getDockActivity().onLoadingFinished();

        isLoading = false;
        // else
        // ( (LoadingListener) super.getParentFragment() ).onLoadingFinished();
    }

    //it will gives us instance of DockActivity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myDockActivity = (DockActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefHelper = new BasePreferenceHelper(getContext());
        if (getMainActivity().getDrawerLayout() != null) {
            getMainActivity().lockDrawer();
        }

        mGpsTracker = new GPSTracker(getDockActivity());

        if (webService == null) {
            webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(getDockActivity(),
                    WebServiceConstants.SERVICE_URL);
        }
        if (serviceHelper == null) {
            serviceHelper = new ServiceHelper(this, getDockActivity());
        }

        myDockActivity = getDockActivity();
        onNotificationReceived();
    }

    @Override
    public void onResume() {
        super.onResume();
        //	setTitleBar( ((MainActivity) getDockActivity()).titleBar );
        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver, new IntentFilter(AppConstants.NOTIFICATION_COUNT_RECIEVED));
        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver, new IntentFilter(AppConstants.PUSH_NOTIFICATION));
        if (getMainActivity().getDrawerLayout() != null) {
            getMainActivity().lockDrawer();
        }
        if (getParentFragment() instanceof NewsMainFragment) {
            setTitleBar(getNewsMainFragment().getTitleBar());
        }
        if (getParentFragment() instanceof ProfileMainFragment) {
            setTitleBar(getProfileMainFragment().getTitleBar());
        }
        if (getParentFragment() instanceof PodcastMainFragment) {
            setTitleBar(getPodcastMainFragment().getTitleBar());
        }
        if (getParentFragment() instanceof SearchMainFragment) {
            setTitleBar(getSearchMainFragment().getTitleBar());
        }
        if (getParentFragment() instanceof BooksMainFragment) {
            setTitleBar(getBooksMainFragment().getTitleBar());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getDockActivity()).unregisterReceiver(broadcastReceiver);
        if (getDockActivity().getWindow() != null)
            if (getDockActivity().getWindow().getDecorView() != null)
                UIHelper.hideSoftKeyboard(getDockActivity(), getDockActivity()
                        .getWindow().getDecorView());

    }

    public void replaceFromParentFragment(BaseFragment fragment, String tag) {
        if (getParentFragment() instanceof NewsMainFragment) {
            getNewsMainFragment().replaceFragment(fragment, tag);
        }
        if (getParentFragment() instanceof ProfileMainFragment) {
            getProfileMainFragment().replaceFragment(fragment, tag);

        }
        if (getParentFragment() instanceof PodcastMainFragment) {
            getPodcastMainFragment().replaceFragment(fragment, tag);

        }
        if (getParentFragment() instanceof SearchMainFragment) {
            getSearchMainFragment().replaceFragment(fragment, tag);

        }
        if (getParentFragment() instanceof BooksMainFragment) {
            getBooksMainFragment().replaceFragment(fragment, tag);

        }


    }

    public void popFromParentFragment() {
        if (getParentFragment() instanceof NewsMainFragment) {
            getNewsMainFragment().popFragment();
        }
        if (getParentFragment() instanceof ProfileMainFragment) {
            getProfileMainFragment().popFragment();

        }
        if (getParentFragment() instanceof PodcastMainFragment) {
            getPodcastMainFragment().popFragment();

        }
        if (getParentFragment() instanceof SearchMainFragment) {
            getSearchMainFragment().popFragment();

        }
        if (getParentFragment() instanceof BooksMainFragment) {
            getBooksMainFragment().popFragment();

        }
    }

    public String getResString(int stringResouseID) {
        return getDockActivity().getResources().getString(stringResouseID);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {

    }

    @Override
    public void ResponseFailure(String tag) {

    }

    protected DockActivity getDockActivity() {

		/*DockActivity activity = (DockActivity) getActivity();
        while ( activity == null ) {
			activity = (DockActivity) getActivity();
			try {
				Thread.sleep( 50 );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}*/
        return myDockActivity;

    }

    protected MainActivity getMainActivity() {
        return (MainActivity) myDockActivity;
    }

    protected TitleBar getTitleBar() {
        return getMainActivity().titleBar;
    }

    /**
     * This is called in the end to modify titlebar. after all changes.
     *
     * @param
     */
    public void setTitleBar(TitleBar titleBar) {
        titleBar.showTitleBar();
        titleBar.setBackgroundColor(getResources().getColor(R.color.transparent));
        getMainActivity().removeMainframeBelowTitlebar();
        // titleBar.refreshListener();
    }

    public String getTitleName() {
        return "";
    }

    /**
     * Gets the preferred height for each item in the ListView, in pixels, after
     * accounting for screen density. ImageLoader uses this value to resize
     * thumbnail images to match the ListView item height.
     *
     * @return The preferred height in pixels, based on the current theme.
     */
    protected int getListPreferredItemHeight() {
        final TypedValue typedValue = new TypedValue();

        // Resolve list item preferred height theme attribute into typedValue
        getActivity().getTheme().resolveAttribute(
                android.R.attr.listPreferredItemHeight, typedValue, true);

        // Create a new DisplayMetrics object
        final DisplayMetrics metrics = new android.util.DisplayMetrics();

        // Populate the DisplayMetrics
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);

        // Return theme value based on DisplayMetrics
        return (int) typedValue.getDimension(metrics);
    }

    protected String getStringTrimed(AnyEditTextView edtView) {
        return edtView.getText().toString().trim();
    }

    /**
     * This generic method to add validator to a text view should be used
     * FormEditText
     * <p>
     * Usage : Takes Array of AnyEditTextView ;
     *
     * @return void
     */
    protected void addEmptyStringValidator(AnyEditTextView... allFields) {

        for (AnyEditTextView field : allFields) {
            field.addValidator(new EmptyStringValidator());
        }

    }

    protected void notImplemented() {
        UIHelper.showLongToastInCenter(getActivity(), "Coming Soon");
    }

    protected void serverNotFound() {
        UIHelper.showLongToastInCenter(getActivity(),
                "Unable to connect to the server, "
                        + "are you connected to the internet?");
    }

    protected void finishLoading() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                loadingFinished();
            }
        });
    }

    protected boolean checkLoading() {
        if (isLoading) {
            UIHelper.showLongToastInCenter(getActivity(),
                    R.string.message_wait);
            return false;
        } else {
            return true;
        }

    }

    /**
     * This generic null string validator to be used FormEditText
     * <p>
     * Usage : formEditText.addValicator(new EmptyStringValidator);
     *
     * @return Boolean and setError on respective field.
     */
    protected class EmptyStringValidator extends Validator {

        public EmptyStringValidator() {
            super("The field must not be empty");
        }

        @Override
        public boolean isValid(EditText et) {
            return et.getText().toString().trim().length() >= 1;
        }

    }

}
