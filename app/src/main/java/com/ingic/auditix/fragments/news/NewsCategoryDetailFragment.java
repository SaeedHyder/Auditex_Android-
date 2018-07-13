package com.ingic.auditix.fragments.news;

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
import com.ingic.auditix.interfaces.FavoriteCheckChangeListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.news.NewsEpisodesBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ingic.auditix.global.WebServiceConstants.FAVORITE_NEWS;
import static com.ingic.auditix.global.WebServiceConstants.GET_ALL_NEWS_BY_CATEGORIES;
import static com.ingic.auditix.global.WebServiceConstants.UNFAVORITE_NEWS;

/**
 * Created on 3/15/2018.
 */
public class NewsCategoryDetailFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
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
    @BindView(R.id.btn_add_favorite)
    ToggleButton btnAddFavorite;
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

    private FavoriteCheckChangeListener playerNewsFavoriteListener = new FavoriteCheckChangeListener() {
        @Override
        public void onFavoriteCheckChange(boolean check, int ID) {
            if (ID == categoryDetail.getId()) {
                btnAddFavorite.setOnCheckedChangeListener(null);
                btnAddFavorite.setChecked(check);
                btnAddFavorite.setOnCheckedChangeListener(NewsCategoryDetailFragment.this);
            }
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
            case FAVORITE_NEWS:
                categoryDetail.setFavoriteNews(true);
                getAllNews();
                break;
            case UNFAVORITE_NEWS:
                categoryDetail.setFavoriteNews(false);
                getAllNews();
                break;
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

    private void bindData(ArrayList<NewsEpisodeEnt> result) {
        ImageLoader.getInstance().displayImage(categoryDetail.getSourceImageUrl(), imgItemPic, options);
        txtTitle.setText(categoryDetail.getSourceName());
        if (result.size() <= 0) {
            setSubscribeFavoriteToggle(categoryDetail.isFavoriteNews(), categoryDetail.isNewsSubscribed());
            txtEpisodesNoData.setVisibility(View.VISIBLE);
            rvEpisodes.setVisibility(View.GONE);
        } else {
            setSubscribeFavoriteToggle(result.get(0).isFavourite(), result.get(0).isSubscribed());
            //titleBar.showFavoriteButton(result.get(0).isFavourite(), newFavoriteListener);
            txtEpisodesNoData.setVisibility(View.GONE);
            rvEpisodes.setVisibility(View.VISIBLE);
        }
        rvEpisodes.BindRecyclerView(new NewsEpisodesBinder(options, episodeListener),
                result, new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false),
                new DefaultItemAnimator());
    }

    private void setSubscribeFavoriteToggle(boolean favoriteToggle, boolean subscribeToggle) {
        btnSubscribe.setOnCheckedChangeListener(null);
        btnAddFavorite.setOnCheckedChangeListener(null);
        btnAddFavorite.setChecked(favoriteToggle);
        btnSubscribe.setChecked(subscribeToggle);
        btnSubscribe.setOnCheckedChangeListener(this);
        btnAddFavorite.setOnCheckedChangeListener(this);
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
        getAllNews();
        if (getMainActivity().getPlayerFragment() != null)
            getMainActivity().getPlayerFragment().setCheckChangeListener(playerNewsFavoriteListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getAllNews() {
        if (categoryDetail.getId() == 0) {
            serviceHelper.enqueueCall(webService.getAllNewsByCategory(categoryDetail.getNewsCategoryId(), prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_BY_CATEGORIES);
        } else {
            serviceHelper.enqueueCall(webService.getAllNewsByCategory(categoryDetail.getId(), prefHelper.getUserToken()), WebServiceConstants.GET_ALL_NEWS_BY_CATEGORIES);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int ID = (categoryDetail.getId() != 0) ? categoryDetail.getId() : categoryDetail.getNewsCategoryId();
        switch (buttonView.getId()) {
            case R.id.btn_subscribe:
                if (prefHelper.isGuest()) {
                    showGuestMessage();
                    btnSubscribe.setChecked(!isChecked);
                } else {
                    if (isChecked) {
                        serviceHelper.enqueueCall(webService.subscribeNews(ID, prefHelper.getUserToken()), WebServiceConstants.SUBSCRIBE_NEWS);
                    } else {
                        serviceHelper.enqueueCall(webService.unsubscribeNews(ID, prefHelper.getUserToken()), WebServiceConstants.UNSUBSCRIBE_NEWS);
                    }
                }
                break;
            case R.id.btn_add_favorite:
                if (prefHelper.isGuest()) {
                    showGuestMessage();
                    btnAddFavorite.setChecked(!isChecked);
                } else {
                    if (getMainActivity().getPlayerFragment() != null)
                        getMainActivity().getPlayerFragment().onFavoriteCheckChange(isChecked, categoryDetail.getId());
                    if (isChecked) {
                        serviceHelper.enqueueCall(webService.favoriteNews(ID, prefHelper.getUserToken()), FAVORITE_NEWS);
                    } else {
                        serviceHelper.enqueueCall(webService.unFavoriteNews(ID, prefHelper.getUserToken()), UNFAVORITE_NEWS);
                    }
                }
                break;
        }
    }
}