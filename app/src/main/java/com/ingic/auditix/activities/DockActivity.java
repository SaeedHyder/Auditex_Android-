package com.ingic.auditix.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.ingic.auditix.BaseApplication;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.DownloadItemModel;
import com.ingic.auditix.fragments.BooksFilterFragment;
import com.ingic.auditix.fragments.FilterFragment;
import com.ingic.auditix.fragments.HomeFragment;
import com.ingic.auditix.fragments.SideMenuFragment;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.interfaces.DownloadListener;
import com.ingic.auditix.interfaces.DownloadListenerFragment;
import com.ingic.auditix.interfaces.LoadingListener;
import com.ingic.auditix.residemenu.ResideMenu;
import com.ingic.auditix.ui.dialogs.DialogFactory;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.event.DownloadServiceConnectChangedEvent;
import com.liulishuo.filedownloader.event.IDownloadEvent;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * This class is marked abstract so that it can pair with Dockable Fragments
 * only. All Classes extending this will inherit this functionality of
 * interaction with menus.
 */
public abstract class DockActivity extends AppCompatActivity implements
        LoadingListener {
    public static final String KEY_FRAG_FIRST = "firstFrag";
    public Realm realm;
    public SideMenuFragment sideMenuFragment;
    public FilterFragment filterFragment;
    public BooksFilterFragment booksFilterFragment;
    protected BasePreferenceHelper prefHelper;
    //For side menu
    protected DrawerLayout drawerLayout;
    BaseFragment baseFragment;
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };
    private DownloadListener fileDownloadListener;

    public abstract int getDockFrameLayoutId();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        prefHelper = new BasePreferenceHelper(this);
        fileDownloadListener = new DownloadListener(realm);
        FileDownloader.getImpl().bindService();
        FileDownloader.getImpl().addServiceConnectListener(new FileDownloadConnectListener() {
            @Override
            public boolean callback(IDownloadEvent event) {
                return super.callback(event);
            }

            @Override
            public void connected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RealmResults<DownloadItemModel> object = realm
                                .where(DownloadItemModel.class).findAll();
                        for (DownloadItemModel items : object
                                ) {
                            int status = FileDownloader.getImpl().replaceListener(items.getDownloadID(), fileDownloadListener);
                            if (status == 0) {
                                if (!realm.isInTransaction()) {
                                    realm.beginTransaction();
                                    items.deleteFromRealm();
                                    realm.commitTransaction();
                                }
                            }
                        }
                        realm.close();
                    }
                });
            }

            @Override
            public void disconnected() {

            }

            @Override
            public DownloadServiceConnectChangedEvent.ConnectStatus getConnectStatus() {
                return super.getConnectStatus();
            }
        });

    }

    public void replaceDockableFragment(BaseFragment frag) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();


    }

    public void replaceDockableFragment(BaseFragment frag, String Tag) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_popenter, R.anim.fragment_pop_exit);
        transaction.replace(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();


    }

    public void replaceDockableFragment(BaseFragment frag, boolean isAnimate) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        // if ( isAnimate )
        // if ( !(frag instanceof DashboardFragment) ) {
        // // transaction.setCustomAnimations( R.anim.push_right_in,
        // // R.anim.push_right_out, R.anim.push_left_in,
        // // R.anim.push_left_out );
        // }

        transaction.replace(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();
    }

    public void addDockableFragment(BaseFragment frag, String Tag) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.add(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();


    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();

    }

    public void lockDrawer() {
        try {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void addAndShowDialogFragment(
            android.support.v4.app.DialogFragment dialog) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        dialog.show(transaction, "tag");

    }

    public void prepareAndShowDialog(DialogFragment frag, String TAG,
                                     BaseFragment fragment) {
        FragmentTransaction transaction = fragment.getChildFragmentManager()
                .beginTransaction();
        Fragment prev = fragment.getChildFragmentManager().findFragmentByTag(
                TAG);

        if (prev != null)
            transaction.remove(prev);

        transaction.addToBackStack(null);
        frag.show(transaction, TAG);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            super.onBackPressed();
        else
            DialogFactory.createQuitDialog(this,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DockActivity.this.finish();

                        }
                    }, R.string.message_quit).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    public BaseFragment getLastAddedFragment() {
        return baseFragment;
    }

    public void emptyBackStack() {
        //popBackStackTillEntry( 0 );
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(i);
            if (entry != null && (!(entry instanceof HomeFragment)) && (!(entry instanceof SideMenuFragment))) {
                getSupportFragmentManager().popBackStack(entry.getId(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

        }
    }

    /**
     * @param entryIndex is the index of fragment to be popped to, for example the
     *                   first fragment will have a index 0;
     */
    public void popBackStackTillEntry(int entryIndex) {
        if (getSupportFragmentManager() == null)
            return;
        if (getSupportFragmentManager().getBackStackEntryCount() <= entryIndex)
            return;
        BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(
                entryIndex);
        if (entry != null) {
            getSupportFragmentManager().popBackStack(entry.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void popFragment() {
        if (getSupportFragmentManager() == null)
            return;
        getSupportFragmentManager().popBackStack();
    }

    public abstract void onMenuItemActionCalled(int actionId, String data);

    public abstract void setSubHeading(String subHeadText);

    public abstract boolean isLoggedIn();

    public abstract void hideHeaderButtons(boolean leftBtn, boolean rightBtn);

    public BaseApplication getMainApplication() {
        return (BaseApplication) getApplication();
    }

    public ResideMenu.OnMenuListener getMenuListener() {
        return menuListener;
    }

    public void setFileDownloadListener(DownloadListenerFragment listener) {
        if (fileDownloadListener != null) {
            fileDownloadListener.setListenerFragment(listener);
        }
    }

    public void addDownload(String downloadUrl, String fileName, String fileFormat, int tag) {
        FileDownloader.getImpl().create(getDownloadUrl(downloadUrl, ""))
                .setPath(getDownloadPath(fileName, fileFormat))
                .setListener(fileDownloadListener)
                .setTag(tag)
                .setCallbackProgressTimes(100)
                .setAutoRetryTimes(5)
                .start();
//        FileDownloader.getImpl().start(fileDownloadListener, false);

    }

    public void addDownload(String serverPath, String audioUrl, int tag) {
        FileDownloader.getImpl().create(getDownloadUrl(serverPath, audioUrl))
                .setPath(getDownloadPath(audioUrl, ""))
                .setListener(fileDownloadListener)
                .setTag(tag)
                .setCallbackProgressTimes(100)
                .setAutoRetryTimes(5)
                .start();
//        FileDownloader.getImpl().start(fileDownloadListener, false);
    }

    @NonNull
    private String getDownloadPath(String fileName, String fileFormat) {
        return AppConstants.DOWNLOAD_PATH + File.separator + fileName
                .replaceAll("\\s+", "")
                .replaceAll("\\\\", "")
                .replaceAll("/", "")
                + fileFormat;
    }

    private String getDownloadUrl(String serverPath, String audioUrl) {
        String temp = serverPath + audioUrl;
        temp = temp.replaceAll(" ", "%20");
        try {
            URL sourceUrl = new URL(temp);
            temp = sourceUrl.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
