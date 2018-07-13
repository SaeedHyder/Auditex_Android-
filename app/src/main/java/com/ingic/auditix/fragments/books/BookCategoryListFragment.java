package com.ingic.auditix.fragments.books;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookCategoryDetailEnt;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.LoadMoreListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.CategoryListBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 2/1/2018.
 */
public class BookCategoryListFragment extends BaseFragment {
    public static final String TAG = "BookCategoryListFragment";
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.rv_categories)
    CustomRecyclerView rvCategories;
    Unbinder unbinder;
    ArrayList<BookDetailEnt> booksCollections;
    String filterGenreID = "";
    int currentPageNumber = 1;
    int totalCount = 10;
    String culture = null;
    private int categoryID;
    private boolean canCallForMore = true;
    private boolean isOnCall;
    private LinearLayoutManager layoutManager;

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
    public static BookCategoryListFragment newInstance(int categoryID) {
        Bundle args = new Bundle();

        BookCategoryListFragment fragment = new BookCategoryListFragment();
        fragment.setArguments(args);
        fragment.setCategoryID(categoryID);
        return fragment;
    }

    private void openBookDetail(Integer bookID) {
        getDockActivity().replaceDockableFragment(BookDetailFragment.newInstance(bookID), BookDetailFragment.TAG);
    }

    public void setCategoryID(int CID) {
        this.categoryID = CID;
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
            case WebServiceConstants.GET_ALL_CATEGORY_BOOK:
                bindDataToView((BookCategoryDetailEnt) result);
                break;
            case WebServiceConstants.GET_PAGED_BOOKS:
                isOnCall = false;
                bindPagedBooksList((BookCategoryDetailEnt) result);
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.addBackground();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.books));
    }

    private void bindPagedBooksList(BookCategoryDetailEnt result) {
        if (result.getBooks().size() <= 0) {
            canCallForMore = false;
        } else {
            booksCollections.addAll(result.getBooks());
            rvCategories.notifyItemRangeChanged(layoutManager.findLastVisibleItemPosition(), result.getBooks().size());
        }
    }

    private void bindDataToView(BookCategoryDetailEnt result) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getResources().getDimension(R.dimen.x10)));
        booksCollections = new ArrayList<>();
        booksCollections.addAll(result.getBooks());

        if (booksCollections.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvCategories.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvCategories.setVisibility(View.VISIBLE);
        }
        layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        rvCategories.BindRecyclerView(new CategoryListBinder(options, booksItemListener), booksCollections,
                layoutManager,
                new DefaultItemAnimator());
        rvCategories.getAdapter().setOnLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMoreItem(int position) {
                getPagedData();
            }
        });
    }

    private void getPagedData() {
        if (canCallForMore) {
            if (!isOnCall) {
                currentPageNumber = currentPageNumber + 1;
                //  progressBar.setVisibility(View.VISIBLE);
                isOnCall = true;
                serviceHelper.enqueueCall(webService.getAllCategoryBooks(this.categoryID, currentPageNumber, totalCount, culture, prefHelper.getUserToken()),
                        WebServiceConstants.GET_PAGED_BOOKS);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentPageNumber = 1;
        serviceHelper.enqueueCall(webService.getAllCategoryBooks(this.categoryID, currentPageNumber, totalCount, culture, prefHelper.getUserToken()),
                WebServiceConstants.GET_ALL_CATEGORY_BOOK);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}