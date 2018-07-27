package com.ingic.auditix.fragments.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NewsCategoryEnt;
import com.ingic.auditix.entities.NotificationEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.adapters.ArrayListAdapter;
import com.ingic.auditix.ui.binders.BinderNotification;
import com.ingic.auditix.ui.binders.news.NewsFavoriteBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by khan_muhammad on 9/15/2017.
 */

public class NotificationsFragment extends BaseFragment {
    public static final String TAG = "NotificationsFragment";

    @BindView(R.id.lv_notification)
    CustomRecyclerView lvNotification;
    Unbinder unbinder;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    private ArrayList<NotificationEnt> notificationCollection;
    private RecyclerView.LayoutManager layoutManager;
    private DisplayImageOptions options;

    private int currentClickedItemPosition = 0;

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        prefHelper.setNotificationCount(0);
        serviceHelper.enqueueCall(webService.getAllNotifications(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NOTIFICATION);
        serviceHelper.enqueueCall(webService.getUnreadCount(prefHelper.getUserToken()), WebServiceConstants.GET_UNREAD_NOTIFICATION_COUNT);

        layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        lvNotification.setNestedScrollingEnabled(false);
        options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        //bindData();


    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_ALL_NOTIFICATION:
                bindData((ArrayList<NotificationEnt>) result);
                break;
            case WebServiceConstants.GET_UNREAD_NOTIFICATION_COUNT:
                prefHelper.setNotificationCount((Integer) result);
                getMainActivity().refreshSideMenu();
                break;
            case WebServiceConstants.DELETE_NOTIFICATION:
                if ((Boolean) result) {
                    notificationCollection.remove(currentClickedItemPosition);
                    lvNotification.BindRecyclerView(new BinderNotification(options, listener), notificationCollection, layoutManager, new DefaultItemAnimator());
                   // lvNotification.getAdapter().notifyItemRemoved(currentClickedItemPosition);
                }
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.addBackground();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.notification));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    public void bindData(ArrayList<NotificationEnt> result) {


        notificationCollection = new ArrayList<>();
        notificationCollection.addAll(result);
        if (result.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvNotification.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvNotification.setVisibility(View.VISIBLE);
        }

        lvNotification.BindRecyclerView(new BinderNotification(options, listener), notificationCollection, layoutManager, new DefaultItemAnimator());

    }

    private RecyclerViewItemListener listener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            NotificationEnt ent = (NotificationEnt) Ent;
            serviceHelper.enqueueCall(webService.deleteNotification(prefHelper.getUserToken(), ent.getNotificationID() + ""), WebServiceConstants.DELETE_NOTIFICATION);
            currentClickedItemPosition = position;
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {

        }
    };

}
