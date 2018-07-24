package com.ingic.auditix.fragments.news;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PlayerNewsEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.news.NewsEpisodeListingBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 7/16/18.
 */
public class NewsChannelEpisodesFragment extends BaseFragment {
    public static final String TAG = "NewsChannelEpisodesFragment";
    @BindView(R.id.img_item_pic)
    ImageView imgItemPic;
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.txtDescription)
    AnyTextView txtDescription;
    @BindView(R.id.btnDownloadAll)
    Button btnDownloadAll;
    @BindView(R.id.rvEpisodes)
    CustomRecyclerView rvEpisodes;
    Unbinder unbinder;
    @BindView(R.id.txtNoData)
    AnyTextView txtNoData;

    private PlayerNewsEnt playerNewsEnt;
    private RecyclerViewItemListener itemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            replaceFromParentFragment(NewsEpisodeDetailFragment.newInstance(playerNewsEnt, position), NewsEpisodeDetailFragment.TAG);
        }
    };

    public static NewsChannelEpisodesFragment newInstance(PlayerNewsEnt playerNewsEnt) {
        Bundle args = new Bundle();

        NewsChannelEpisodesFragment fragment = new NewsChannelEpisodesFragment();
        fragment.setArguments(args);
        fragment.setPlayerNewsEnt(playerNewsEnt);
        return fragment;
    }

    public void setPlayerNewsEnt(PlayerNewsEnt playerNewsEnt) {
        this.playerNewsEnt = playerNewsEnt;
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
        if (playerNewsEnt == null)
            titleBar.setSubHeading(getResString(R.string.news_detail));
        else
            titleBar.setSubHeading(playerNewsEnt.getDetailEnt().getName());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_channel_details_episodes, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        ImageLoader.getInstance().displayImage(playerNewsEnt.getDetailEnt().getImageUrl(), imgItemPic, options);
        txtTitle.setText(playerNewsEnt.getDetailEnt().getName() + "");
        txtDescription.setText(playerNewsEnt.getDetailEnt().getDescription() + "");
        if (playerNewsEnt.getNewsepisodeslist().size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            btnDownloadAll.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            btnDownloadAll.setVisibility(View.VISIBLE);
        }
        rvEpisodes.BindRecyclerView(new NewsEpisodeListingBinder(itemListener), playerNewsEnt.getNewsepisodeslist(), new LinearLayoutManager(getDockActivity()), new DefaultItemAnimator());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}