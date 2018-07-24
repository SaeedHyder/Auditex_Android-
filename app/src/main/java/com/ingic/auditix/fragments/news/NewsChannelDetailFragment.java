package com.ingic.auditix.fragments.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NewItemDetailEnt;
import com.ingic.auditix.entities.NewsChannelDetailEnt;
import com.ingic.auditix.entities.NewsEpisodeEnt;
import com.ingic.auditix.entities.PlayerNewsEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 7/16/18.
 */
public class NewsChannelDetailFragment extends BaseFragment {
    public static final String TAG = "NewsChannelDetailFragment";
    @BindView(R.id.img_item_pic)
    ImageView imgItemPic;
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.txtDescription)
    AnyTextView txtDescription;
    @BindView(R.id.txtToday)
    AnyTextView txtToday;
    @BindView(R.id.txtYesterday)
    AnyTextView txtYesterday;
    @BindView(R.id.txtOlder)
    AnyTextView txtOlder;
    Unbinder unbinder;
    private NewItemDetailEnt detailEnt;
    private NewsChannelDetailEnt newsChannelDetailEnt;

    public static NewsChannelDetailFragment newInstance(NewItemDetailEnt detailEnt) {
        Bundle args = new Bundle();

        NewsChannelDetailFragment fragment = new NewsChannelDetailFragment();
        fragment.setArguments(args);
        fragment.setDetailEnt(detailEnt);
        return fragment;
    }

    public void setNewsChannelDetailEnt(NewsChannelDetailEnt newsChannelDetailEnt) {
        this.newsChannelDetailEnt = newsChannelDetailEnt;
    }

    public void setDetailEnt(NewItemDetailEnt detailEnt) {
        this.detailEnt = detailEnt;
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
            case WebServiceConstants.GET_ALL_NEWS_CHANNEL_EPISODES:
                setNewsChannelDetailEnt((NewsChannelDetailEnt) result);
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.addBackground();
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.addBackground();
        titleBar.setSubHeading(getResString(R.string.news_detail));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_channel_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        ImageLoader.getInstance().displayImage(detailEnt.getImageUrl(), imgItemPic, options);
        txtTitle.setText(detailEnt.getName() + "");
        txtDescription.setText(detailEnt.getDescription() + "");
        serviceHelper.enqueueCall(webService.getAllChannelEpisode(detailEnt.getNewsID(), prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_CHANNEL_EPISODES);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    private void openShowDetails(NewItemDetailEnt detailEnt, List<NewsEpisodeEnt>list){
        if (list.isEmpty()){
            UIHelper.showShortToastInCenter(getDockActivity(),getResString(R.string.show_error));
            return;
        }
        replaceFromParentFragment(NewsChannelEpisodesFragment.newInstance(new PlayerNewsEnt(detailEnt, list)), NewsChannelEpisodesFragment.TAG);

    }

    @OnClick({R.id.txtToday, R.id.txtYesterday, R.id.txtOlder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtToday:
                openShowDetails(detailEnt, newsChannelDetailEnt.getNewsepisodestoday());
                break;
            case R.id.txtYesterday:
                openShowDetails(detailEnt, newsChannelDetailEnt.getNewsepisodesyesterday());
                break;
            case R.id.txtOlder:
                openShowDetails(detailEnt, newsChannelDetailEnt.getNewsepisodeslater());
                break;
        }
    }
}