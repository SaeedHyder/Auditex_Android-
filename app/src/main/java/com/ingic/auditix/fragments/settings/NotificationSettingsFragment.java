package com.ingic.auditix.fragments.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.News;
import com.ingic.auditix.entities.NotificaitonsSettingsEnt;
import com.ingic.auditix.entities.Podcast;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.ToggleListner;
import com.ingic.auditix.ui.adapters.ArrayListAdapter;
import com.ingic.auditix.ui.binders.PodcastsBinder;
import com.ingic.auditix.ui.binders.podcast.NewsBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.ExpandedListView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ingic.auditix.global.WebServiceConstants.SET_NOTIFICATIONS_SETTING;
import static com.ingic.auditix.global.WebServiceConstants.UNSET_NOTIFICATIONS_SETTING;

public class NotificationSettingsFragment extends BaseFragment implements ToggleListner {

    public static final String TAG = "NotificationSettingsFragment";

    @BindView(R.id.rv_podcast)
    ExpandedListView lvPodcast;
    @BindView(R.id.rv_news_channels)
    ExpandedListView lvNewsChannels;
    Unbinder unbinder;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.txt_no_data_2)
    AnyTextView txtNoData2;

    private List<Podcast> collectionPodcast;
    private ArrayListAdapter<Podcast> adapterPodcast;

    private List<News> collectionNews;
    private ArrayListAdapter<News> adapterNews;

    public static NotificationSettingsFragment newInstance() {
        Bundle args = new Bundle();

        NotificationSettingsFragment fragment = new NotificationSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        adapterPodcast = new ArrayListAdapter<>(getDockActivity(), new PodcastsBinder(getDockActivity(), prefHelper, options,this,false));
        adapterNews = new ArrayListAdapter<>(getDockActivity(), new NewsBinder(getDockActivity(), prefHelper, options, this,false));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_settings, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceHelper.enqueueCall(webService.getAllPodcastsAndNews(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_PODCAST_NEWS);
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.addBackground();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.notification));

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_ALL_PODCAST_NEWS:
                NotificaitonsSettingsEnt notificaitonsSettingsEnt = (NotificaitonsSettingsEnt) result;
                bindData(notificaitonsSettingsEnt);
                break;

            case SET_NOTIFICATIONS_SETTING:
                break;

            case UNSET_NOTIFICATIONS_SETTING:
                break;
        }
    }

    public void bindData(NotificaitonsSettingsEnt result) {

        collectionPodcast = new ArrayList<>();
        collectionNews = new ArrayList<>();

        collectionPodcast.addAll(result.getPodcast());
        collectionNews.addAll(result.getNews());

        if (collectionPodcast.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            lvPodcast.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            lvPodcast.setVisibility(View.VISIBLE);

        }
        if (collectionNews.size() <= 0) {
            txtNoData2.setVisibility(View.VISIBLE);
            lvNewsChannels.setVisibility(View.GONE);
        } else {
            txtNoData2.setVisibility(View.GONE);
            lvNewsChannels.setVisibility(View.VISIBLE);

        }

        adapterPodcast.clearList();
        lvPodcast.setAdapter(adapterPodcast);
        adapterPodcast.addAll(collectionPodcast);


        adapterNews.clearList();
        lvNewsChannels.setAdapter(adapterNews);
        adapterNews.addAll(collectionNews);


    }


    @Override
    public void onCheckedChanged(Object entity, int position, boolean isChecked, String channel) {

        if (channel.equals(AppConstants.News)) {
            News newsEnt = (News) entity;
            if (isChecked) {
                serviceHelper.enqueueCall(webService.setNotificationSetting(prefHelper.getUserToken(), newsEnt.getNewsID() + "", "2"), SET_NOTIFICATIONS_SETTING);
            } else {
                serviceHelper.enqueueCall(webService.unsetNotificationSetting(prefHelper.getUserToken(), newsEnt.getNewsID() + "", "2"), UNSET_NOTIFICATIONS_SETTING);
            }
        } else {
            Podcast podcastEnt = (Podcast) entity;
            if (isChecked) {
                serviceHelper.enqueueCall(webService.setNotificationSetting(prefHelper.getUserToken(), podcastEnt.getPodcastId() + "", "1"), SET_NOTIFICATIONS_SETTING);
            } else {
                serviceHelper.enqueueCall(webService.unsetNotificationSetting(prefHelper.getUserToken(), podcastEnt.getPodcastId() + "", "1"), UNSET_NOTIFICATIONS_SETTING);
            }

        }
    }
}
