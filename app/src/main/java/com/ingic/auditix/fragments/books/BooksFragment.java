package com.ingic.auditix.fragments.books;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookCategoryEnt;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.standard.HomeTabFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.interfaces.FilterDoneClickListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.binders.books.AllBooksBinder;
import com.ingic.auditix.ui.binders.books.BestBooksBinder;
import com.ingic.auditix.ui.binders.books.RecommendedBooksBinder;
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
public class BooksFragment extends BaseFragment implements ViewPagerFragmentLifecycleListener, FilterDoneClickListener {
    @BindView(R.id.txt_allbooks)
    AnyTextView txtAllbooks;
    @BindView(R.id.btn_allbooks_seeall)
    AnyTextView btnAllbooksSeeall;
    @BindView(R.id.rv_subscribe)
    CustomRecyclerView rvAllBooks;
    @BindView(R.id.txt_subscription_no_data)
    AnyTextView txtAllBooksNoData;
    @BindView(R.id.txt_recommended)
    AnyTextView txtRecommended;
    @BindView(R.id.btn_recommne_seeall)
    AnyTextView btnRecommneSeeall;
    @BindView(R.id.rv_recommended)
    CustomRecyclerView rvRecommended;
    @BindView(R.id.txt_recommended_no_data)
    AnyTextView txtRecommendedNoData;
    @BindView(R.id.txt_best)
    AnyTextView txtBest;
    @BindView(R.id.btn_best_seeall)
    AnyTextView btnBestSeeall;
    @BindView(R.id.rv_best)
    CustomRecyclerView rvBest;
    @BindView(R.id.txt_default_no_data)
    AnyTextView txtBestNoData;
    @BindView(R.id.parent_scroll)
    NestedScrollView parentScroll;
    Unbinder unbinder;
    int currentPageNumber = 1;
    int totalCount = 2;
    String filterGenreID = "";
    String culture = null;
    ArrayList<BookDetailEnt> allBooksCollections;
    ArrayList<BookDetailEnt> recommendedBooksCollections;
    ArrayList<BookDetailEnt> bestBooksCollections;
    private RecyclerViewItemListener booksItemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            BookDetailEnt ent = (BookDetailEnt) Ent;
            openBookDetail(ent.getBookID());

        }
    };
    private ArrayList<BookCategoryEnt> bookCategoryEnts;

    public static BooksFragment newInstance() {
        Bundle args = new Bundle();

        BooksFragment fragment = new BooksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void openBookDetail(Integer bookID) {
        getDockActivity().replaceDockableFragment(BookDetailFragment.newInstance(bookID), BookDetailFragment.TAG);
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
            case WebServiceConstants.GET_DEFAULT_BOOKS:
                this.bookCategoryEnts = (ArrayList<BookCategoryEnt>) result;
                bindDataToViews(bookCategoryEnts);
                break;

        }
    }

    public void setTitleBar(TitleBar titleBar) {
        if (getMainActivity().booksFilterFragment != null) {
            getMainActivity().setRightSideFragment(getMainActivity().booksFilterFragment);
            getMainActivity().booksFilterFragment.setListener(this);
        }
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.books));
        titleBar.showBackButton();
        titleBar.showFilterButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().isNavigationGravityRight = true;
                getMainActivity().getDrawerLayout().openDrawer(Gravity.RIGHT);
            }
        });

    }

    private void bindDataToViews(ArrayList<BookCategoryEnt> result) {
            DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
            bindData(result);

            rvAllBooks.BindRecyclerView(new AllBooksBinder(options, booksItemListener), allBooksCollections,
                    new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false),
                    new DefaultItemAnimator());
            rvRecommended.BindRecyclerView(new RecommendedBooksBinder(options, booksItemListener), recommendedBooksCollections,
                    new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false),
                    new DefaultItemAnimator());
            rvBest.BindRecyclerView(new BestBooksBinder(options, booksItemListener), bestBooksCollections,
                    new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false),
                    new DefaultItemAnimator());
        // rvRecommended.addOnScrollListener(scrollListener);

    }

    private void bindData(ArrayList<BookCategoryEnt> result) {
        allBooksCollections = new ArrayList<>();
        allBooksCollections.addAll(result.size() > 0 ? result.get(0).getCategoryBooks() : new ArrayList<BookDetailEnt>());
        recommendedBooksCollections = new ArrayList<>();
        recommendedBooksCollections.addAll(result.size() > 1 ? result.get(1).getCategoryBooks() : new ArrayList<BookDetailEnt>());
        bestBooksCollections = new ArrayList<>();
        bestBooksCollections.addAll(result.size() > 2 ? result.get(2).getCategoryBooks() : new ArrayList<BookDetailEnt>());

        if (allBooksCollections.size() <= 0) {
            txtAllBooksNoData.setVisibility(View.VISIBLE);
            rvAllBooks.setVisibility(View.GONE);
        } else {
            txtAllBooksNoData.setVisibility(View.GONE);
            rvAllBooks.setVisibility(View.VISIBLE);
        }
        if (recommendedBooksCollections.size() <= 0) {
            txtRecommendedNoData.setVisibility(View.VISIBLE);
            rvRecommended.setVisibility(View.GONE);
        } else {
            txtRecommendedNoData.setVisibility(View.GONE);
            rvRecommended.setVisibility(View.VISIBLE);
        }
        if (bestBooksCollections.size() <= 0) {
            txtBestNoData.setVisibility(View.VISIBLE);
            rvBest.setVisibility(View.GONE);
        } else {
            txtBestNoData.setVisibility(View.GONE);
            rvBest.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        setTitleBar(((HomeTabFragment) getParentFragment()).getTitleBar());
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDefaultBooks();
    }


    private void getDefaultBooks() {
        filterGenreID =  getMainActivity().booksFilterFragment.getFiltersList();
        serviceHelper.enqueueCall(
                webService.getDefaultBooks(currentPageNumber, totalCount, filterGenreID, culture, prefHelper.getUserToken()),
                WebServiceConstants.GET_DEFAULT_BOOKS);
    }


    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper) {

    }

    private void openCategoryListing(int categoryID) {
        if (categoryID != -1) {
            getDockActivity().replaceDockableFragment(BookCategoryListFragment.newInstance(categoryID), BookCategoryListFragment.TAG);
        }
    }

    @OnClick({R.id.btn_allbooks_seeall, R.id.btn_recommne_seeall, R.id.btn_best_seeall})
    public void onViewClicked(View view) {
        if (bookCategoryEnts == null){
            return;
        }
        switch (view.getId()) {
            case R.id.btn_allbooks_seeall:
                openCategoryListing(bookCategoryEnts.size() > 0 ? bookCategoryEnts.get(0).getCategoryId() : -1);
                break;
            case R.id.btn_recommne_seeall:
                openCategoryListing(bookCategoryEnts.size() > 1 ? bookCategoryEnts.get(1).getCategoryId() : -1);
                break;
            case R.id.btn_best_seeall:
                openCategoryListing(bookCategoryEnts.size() > 2 ? bookCategoryEnts.get(2).getCategoryId() : -1);
                break;
        }
    }

    @Override
    public void onDoneFiltering(String filterIDs) {
        getDefaultBooks();
    }
}