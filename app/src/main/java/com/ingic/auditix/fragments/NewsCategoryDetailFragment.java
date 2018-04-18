package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NewsCategoryEnt;
import com.ingic.auditix.entities.NewsEpisodeEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.NewsEpisodesBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ingic.auditix.global.WebServiceConstants.GET_ALL_NEWS_BY_CATEGORIES;

/**
 * Created on 3/15/2018.
 */
public class NewsCategoryDetailFragment extends BaseFragment {
    public static final String TAG = "NewsCategoryDetailFragment";
    @BindView(R.id.img_item_pic)
    ImageView imgItemPic;
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.txt_about)
    AnyTextView txtAbout;
    @BindView(R.id.rv_episodes)
    CustomRecyclerView rvEpisodes;
    Unbinder unbinder;
    @BindView(R.id.txt_episodes_no_data)
    AnyTextView txtEpisodesNoData;
    DisplayImageOptions options;
    @BindView(R.id.btn_subscribe)
    ToggleButton btnSubscribe;
    private NewsCategoryEnt categoryDetail;
    private RecyclerViewItemListener episodeListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            getDockActivity().replaceDockableFragment(NewsEpisodeDetailFragment.newInstance((NewsEpisodeEnt) Ent, categoryDetail.getId()), NewsEpisodeDetailFragment.TAG);
        }
    };

    public static NewsCategoryDetailFragment newInstance(NewsCategoryEnt categoryDetail) {
        Bundle args = new Bundle();

        NewsCategoryDetailFragment fragment = new NewsCategoryDetailFragment();
        fragment.setArguments(args);
        fragment.setCategoryDetail(categoryDetail);
        return fragment;
    }
    public void setCategoryDetail(NewsCategoryEnt categoryDetail) {
        this.categoryDetail = categoryDetail;
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
            case GET_ALL_NEWS_BY_CATEGORIES:
                bindData((ArrayList<NewsEpisodeEnt>) result);
                break;
        }
    }
    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.addBackground();
        // TODO: 4/17/18 Show Favorite Button
        titleBar.setSubHeading(getResString(R.string.news_detail));
    }

    private void bindData(ArrayList<NewsEpisodeEnt> result) {
        ImageLoader.getInstance().displayImage(categoryDetail.getSourceImageUrl(), imgItemPic, options);
        txtTitle.setText(categoryDetail.getSourceName());
        if (result.size() <= 0) {
            txtEpisodesNoData.setVisibility(View.VISIBLE);
            rvEpisodes.setVisibility(View.GONE);
        } else {
            txtEpisodesNoData.setVisibility(View.GONE);
            rvEpisodes.setVisibility(View.VISIBLE);
        }
        rvEpisodes.BindRecyclerView(new NewsEpisodesBinder(options, episodeListener),
                result, new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false),
                new DefaultItemAnimator());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_category_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        serviceHelper.enqueueCall(webService.getAllNewsByCategory(categoryDetail.getId(), prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_BY_CATEGORIES);
        btnSubscribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (prefHelper.isGuest()) {
                    showGuestMessage();
                } else {
                    if (b) {
                        serviceHelper.enqueueCall(webService.subscribeNews(categoryDetail.getId(), prefHelper.getUserToken()), WebServiceConstants.SUBSCRIBE_NEWS);
                    } else {
                        serviceHelper.enqueueCall(webService.unsubscribeNews(categoryDetail.getId(), prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_NEWS);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}