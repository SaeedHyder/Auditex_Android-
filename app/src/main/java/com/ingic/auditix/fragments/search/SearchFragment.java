package com.ingic.auditix.fragments.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.entities.NewsCategoryEnt;
import com.ingic.auditix.entities.PodcastDetailHomeEnt;
import com.ingic.auditix.entities.SearchEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.books.BookDetailFragment;
import com.ingic.auditix.fragments.news.NewsCategoryDetailFragment;
import com.ingic.auditix.fragments.podcast.PodcastDetailFragment;
import com.ingic.auditix.fragments.standard.HomeTabFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.binders.books.BookSearchBinder;
import com.ingic.auditix.ui.binders.news.NewsSearchBinder;
import com.ingic.auditix.ui.binders.podcast.PodcastSearchBinder;
import com.ingic.auditix.ui.views.AnyEditTextView;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 12/23/2017.
 */
public class SearchFragment extends BaseFragment implements ViewPagerFragmentLifecycleListener {


    Unbinder unbinder;
    int currentPageNumber = 1;
    int totalCount = 10;
    @BindView(R.id.edt_search)
    AnyEditTextView edtSearch;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.btnNewsSeeAll)
    AnyTextView btnNewsSeeAll;
    @BindView(R.id.txtNewsNoData)
    AnyTextView txtNewsNoData;
    @BindView(R.id.rvNews)
    CustomRecyclerView rvNews;
    @BindView(R.id.btnPodcastSeeAll)
    AnyTextView btnPodcastSeeAll;
    @BindView(R.id.txtPodcastNoData)
    AnyTextView txtPodcastNoData;
    @BindView(R.id.rvPodcast)
    CustomRecyclerView rvPodcast;
    @BindView(R.id.btnBooksSeeAll)
    AnyTextView btnBooksSeeAll;
    @BindView(R.id.txtBooksNoData)
    AnyTextView txtBooksNoData;
    @BindView(R.id.rvBooks)
    CustomRecyclerView rvBooks;
    DisplayImageOptions options;
    @BindView(R.id.newsheader)
    FrameLayout newsheader;
    @BindView(R.id.podcastheader)
    FrameLayout podcastheader;
    @BindView(R.id.booksheader)
    FrameLayout booksheader;
    private RecyclerViewItemListener ItemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            openItemDetailPage(Ent);
        }
    };
    private SearchEnt searchCollection;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setTitleBar(((HomeTabFragment) getParentFragment()).getTitleBar());
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_SEARCH:
                bindDataToView((SearchEnt) result);
                break;
        }
    }

    public void setTitleBar(TitleBar titleBar) {
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.search));
        titleBar.showBackButton();

    }

    private void openItemDetailPage(Object searchItem) {
        if (searchItem instanceof PodcastDetailHomeEnt) {
            PodcastDetailHomeEnt ent = (PodcastDetailHomeEnt) searchItem;
            getDockActivity().replaceDockableFragment(PodcastDetailFragment.newInstance(ent), PodcastDetailFragment.TAG);
        } else if (searchItem instanceof BookDetailEnt) {
            getDockActivity().replaceDockableFragment(BookDetailFragment.newInstance(((BookDetailEnt) searchItem).getBookID()), BookDetailFragment.TAG);
        } else if (searchItem instanceof NewsCategoryEnt) {
            getDockActivity().replaceDockableFragment(NewsCategoryDetailFragment.newInstance((NewsCategoryEnt) searchItem), NewsCategoryDetailFragment.TAG);
        }
    }

    private void bindDataToView(SearchEnt result) {

        options = getMainActivity().getImageLoaderRoundCornerTransformation(
                Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        searchCollection = result;
        booksheader.setVisibility(View.VISIBLE);
        newsheader.setVisibility(View.VISIBLE);
        podcastheader.setVisibility(View.VISIBLE);
        bindPodcastSearch(result.getPodcasts());
        bindBooksSearch(result.getBooks());
        bindNewsSearch(result.getNews());
    }

    private void bindPodcastSearch(ArrayList<PodcastDetailHomeEnt> podcasts) {
        if (podcasts.size() <= 0) {
            txtPodcastNoData.setVisibility(View.VISIBLE);
            rvPodcast.setVisibility(View.GONE);
        } else {
            txtPodcastNoData.setVisibility(View.GONE);
            rvPodcast.setVisibility(View.VISIBLE);
        }
        rvPodcast.BindRecyclerView(new PodcastSearchBinder(options, ItemListener), podcasts, new LinearLayoutManager(getDockActivity()), new DefaultItemAnimator());
    }

    private void bindBooksSearch(ArrayList<BookDetailEnt> bookDetailEnts) {
        if (bookDetailEnts.size() <= 0) {
            txtBooksNoData.setVisibility(View.VISIBLE);
            rvBooks.setVisibility(View.GONE);
        } else {
            txtBooksNoData.setVisibility(View.GONE);
            rvBooks.setVisibility(View.VISIBLE);
        }

        rvBooks.BindRecyclerView(new BookSearchBinder(options, ItemListener), bookDetailEnts, new LinearLayoutManager(getDockActivity()), new DefaultItemAnimator());
    }

    private void bindNewsSearch(ArrayList<NewsCategoryEnt> newsCategoryEnts) {
        if (newsCategoryEnts.size() <= 0) {
            txtNewsNoData.setVisibility(View.VISIBLE);
            rvNews.setVisibility(View.GONE);
        } else {
            txtNewsNoData.setVisibility(View.GONE);
            rvNews.setVisibility(View.VISIBLE);
        }

        rvNews.BindRecyclerView(new NewsSearchBinder(options, ItemListener), newsCategoryEnts, new LinearLayoutManager(getDockActivity()), new DefaultItemAnimator());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListener();
        booksheader.setVisibility(View.GONE);
        newsheader.setVisibility(View.GONE);
        podcastheader.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setListener() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    searchByText(edtSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper) {

    }

    private void searchByText(String text) {
        if (text.trim().equals("")) return;
        UIHelper.hideSoftKeyboard(getDockActivity(), edtSearch);
        serviceHelper.enqueueCall(webService.getSearchItem(currentPageNumber, totalCount, text, prefHelper.getUserToken()), WebServiceConstants.GET_SEARCH);
    }

    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        if (searchCollection != null) {
            searchCollection.getPodcasts().clear();
            searchCollection.getBooks().clear();
            searchCollection.getNews().clear();

        }
        searchByText(edtSearch.getText().toString());
    }

    @OnClick({R.id.btnNewsSeeAll, R.id.btnPodcastSeeAll, R.id.btnBooksSeeAll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnNewsSeeAll:
                getDockActivity().replaceDockableFragment(SearchItemAllListingFragment.newInstance(searchCollection, AppConstants.TAB_NEWS, edtSearch.getText().toString()));
                break;
            case R.id.btnPodcastSeeAll:
                getDockActivity().replaceDockableFragment(SearchItemAllListingFragment.newInstance(searchCollection, AppConstants.TAB_PODCAST, edtSearch.getText().toString()));
                break;
            case R.id.btnBooksSeeAll:
                getDockActivity().replaceDockableFragment(SearchItemAllListingFragment.newInstance(searchCollection, AppConstants.TAB_BOOKS, edtSearch.getText().toString()));

                break;
        }
    }
}