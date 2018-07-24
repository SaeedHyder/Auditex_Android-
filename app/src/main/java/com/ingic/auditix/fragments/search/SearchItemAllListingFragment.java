package com.ingic.auditix.fragments.search;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.entities.NewsCategoryEnt;
import com.ingic.auditix.entities.PodcastDetailHomeEnt;
import com.ingic.auditix.entities.SearchEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.books.BookDetailFragment;
import com.ingic.auditix.fragments.news.NewsCategoryDetailFragment;
import com.ingic.auditix.fragments.podcast.PodcastDetailFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.LoadMoreListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.books.BookSearchBinder;
import com.ingic.auditix.ui.binders.news.NewsSearchBinder;
import com.ingic.auditix.ui.binders.podcast.PodcastSearchBinder;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 7/11/18.
 */
public class SearchItemAllListingFragment extends BaseFragment {
    public static final String TAG = "SearchItemAllListingFragment";
    @BindView(R.id.rvListing)
    CustomRecyclerView rvListing;
    Unbinder unbinder;
    SearchEnt searchEnt;
    private String type;
    private String query;
    private RecyclerViewItemListener ItemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            openItemDetailPage(Ent);
        }
    };
    private DisplayImageOptions options;
    private boolean canCallForMore = true;
    private boolean isOnCall = false;
    private Integer currentPageNumber = 1;
    private Integer totalCount = 10;

    public static SearchItemAllListingFragment newInstance(SearchEnt ent, String type, String query) {
        Bundle args = new Bundle();

        SearchItemAllListingFragment fragment = new SearchItemAllListingFragment();
        fragment.setArguments(args);
        fragment.setSearchEnt(ent);
        fragment.setType(type);
        fragment.setQuery(query);
        return fragment;
    }


    public void setQuery(String query) {
        this.query = query;
    }

    private void openItemDetailPage(Object searchItem) {
        if (searchItem instanceof PodcastDetailHomeEnt) {
            PodcastDetailHomeEnt ent = (PodcastDetailHomeEnt) searchItem;
            replaceFromParentFragment(PodcastDetailFragment.newInstance(ent), PodcastDetailFragment.TAG);
        } else if (searchItem instanceof BookDetailEnt) {
            replaceFromParentFragment(BookDetailFragment.newInstance(((BookDetailEnt) searchItem).getBookID()), BookDetailFragment.TAG);
        } else if (searchItem instanceof NewsCategoryEnt) {
            replaceFromParentFragment(NewsCategoryDetailFragment.newInstance((NewsCategoryEnt) searchItem), NewsCategoryDetailFragment.TAG);
        }
    }

    public void setSearchEnt(SearchEnt searchEnt) {
        this.searchEnt = searchEnt;
    }

    public void setType(String type) {
        this.type = type;
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
            case WebServiceConstants.GET_PAGED_SEARCH:
                bindPagedData((SearchEnt) result);
                break;
        }
    }
    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.search));
        titleBar.showBackButton();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_see_all, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        options = getMainActivity().getImageLoaderRoundCornerTransformation(
                Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        switch (type) {
            case AppConstants.TAB_PODCAST:
                rvListing.BindRecyclerView(new PodcastSearchBinder(options, ItemListener), searchEnt.getPodcasts(), new LinearLayoutManager(getDockActivity()), new DefaultItemAnimator());
                break;
            case AppConstants.TAB_BOOKS:
                rvListing.BindRecyclerView(new BookSearchBinder(options, ItemListener), searchEnt.getBooks(), new LinearLayoutManager(getDockActivity()), new DefaultItemAnimator());
                break;
            case AppConstants.TAB_NEWS:
                rvListing.BindRecyclerView(new NewsSearchBinder(options, ItemListener), searchEnt.getNews(), new LinearLayoutManager(getDockActivity()), new DefaultItemAnimator());
                break;
        }
        rvListing.getAdapter().setOnLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMoreItem(int position) {
                getPagedData();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void bindPagedData(SearchEnt result) {
        switch (type) {
            case AppConstants.TAB_PODCAST:
                searchEnt.getPodcasts().addAll(result.getPodcasts());
                rvListing.notifyItemRangeChanged(((LinearLayoutManager) rvListing.getLayoutManager()).findLastVisibleItemPosition(), result.getPodcasts().size());

                break;
            case AppConstants.TAB_BOOKS:
                searchEnt.getBooks().addAll(result.getBooks());
                rvListing.notifyItemRangeChanged(((LinearLayoutManager) rvListing.getLayoutManager()).findLastVisibleItemPosition(), result.getBooks().size());

                break;
            case AppConstants.TAB_NEWS:
                searchEnt.getNews().addAll(result.getNews());
                rvListing.notifyItemRangeChanged(((LinearLayoutManager) rvListing.getLayoutManager()).findLastVisibleItemPosition(), result.getNews().size());

                break;
        }
    }

    private void getPagedData() {
        if (canCallForMore) {
            if (!isOnCall) {
                currentPageNumber = currentPageNumber + 1;
                isOnCall = true;
                serviceHelper.enqueueCall(webService.getSearchItem(currentPageNumber, totalCount, query, prefHelper.getUserToken()), WebServiceConstants.GET_PAGED_SEARCH);
            }
        }
    }
}