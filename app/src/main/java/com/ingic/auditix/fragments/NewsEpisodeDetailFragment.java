package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NewsEpisodeEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 3/15/2018.
 */
public class NewsEpisodeDetailFragment extends BaseFragment {
    public static final String TAG = "NewsEpisodeDetailFragment";
    @BindView(R.id.img_item_pic)
    ImageView imgItemPic;
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.btn_play)
    ImageView btnPlay;
    @BindView(R.id.txt_duration)
    AnyTextView txtDuration;
    @BindView(R.id.txt_duration_text)
    AnyTextView txtDurationText;
    @BindView(R.id.btn_download)
    ImageView btnDownload;
    @BindView(R.id.txt_about)
    AnyTextView txtAbout;
    @BindView(R.id.txt_about_text)
    AnyTextView txtAboutText;
    Unbinder unbinder;
    NewsEpisodeEnt episodeData;
    DisplayImageOptions options;
    private int startingIndex = 0;
    private int newsCategoryID = 0;
    public static NewsEpisodeDetailFragment newInstance(NewsEpisodeEnt data, int newsCategoryID) {
        Bundle args = new Bundle();

        NewsEpisodeDetailFragment fragment = new NewsEpisodeDetailFragment();
        fragment.setArguments(args);
        fragment.setEpisodeData(data);
        fragment.setNewsCategoryID(newsCategoryID);
        return fragment;
    }

    public void setNewsCategoryID(int newsCategoryID) {
        this.newsCategoryID = newsCategoryID;
    }

    public void setEpisodeData(NewsEpisodeEnt episodeData) {
        this.episodeData = episodeData;
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
        titleBar.setSubHeading(getResString(R.string.news_detail));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_episode_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        bindData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public String getDurationText(Integer secound) {
        int minutes = (secound % 3600) / 60;
        int hours = secound / 3600;

        if (hours > 1) {
            return (hours) + " Hr " + (minutes) + " Mins";
        } else if (minutes > 0) {
            return (minutes) + " Mins";
        } else if (secound > 0) {
            return (secound) + " Sec";
        } else {
            return "-";
        }

    }

    private void bindData() {
        ImageLoader.getInstance().displayImage(episodeData.getImage_url(), imgItemPic, options);
        txtDurationText.setText(getDurationText(episodeData.getDuration()));
        txtTitle.setEllipsize(TextUtils.TruncateAt.END);
        txtTitle.setMaxLines(3);
        txtTitle.setText(episodeData.getTitle() + "");
        txtAboutText.setText(episodeData.getDescription() + "");
        txtAboutText.setMovementMethod(new ScrollingMovementMethod());
    }

    @OnClick(R.id.btn_play)
    public void onViewClicked() {
        getMainActivity().showBottomPlayer(null, newsCategoryID,
                AppConstants.TAB_NEWS, null, episodeData, startingIndex,null);
        /*getDockActivity().replaceDockableFragment(PlayerFragment.newInstance(null, 0,
                AppConstants.TAB_NEWS, null, episodeData, startingIndex), PlayerFragment.TAG);*/

    }
}