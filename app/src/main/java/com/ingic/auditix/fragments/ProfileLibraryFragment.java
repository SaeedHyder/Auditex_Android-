package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.LoadMoreListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.LibraryBooksBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 1/6/2018.
 */
public class ProfileLibraryFragment extends BaseFragment {
    public static final String TAG = "ProfileLibraryFragment";
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.rv_books)
    CustomRecyclerView rvBooks;
    Unbinder unbinder;
    int currentPageNumber = 1;
    int totalCount = 10;
    String culture = null;
    private ArrayList<BookDetailEnt> booksCollections;
    private LinearLayoutManager layoutManager;
    private int categoryID;
    private boolean canCallForMore = true;
    private boolean isOnCall;
    private RecyclerViewItemListener booksItemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            BookDetailEnt detailEnt = (BookDetailEnt) Ent;
            if (detailEnt != null && detailEnt.getChapters() != null && detailEnt.getChapters().getChapter().size() > 0) {
                detailEnt.setIsPurchased(true);
                detailEnt.setIsFavorite(false);
                getDockActivity().replaceDockableFragment(PlayerFragment.newInstance(null, detailEnt.getBookID(), AppConstants.TAB_BOOKS, detailEnt,0), "PlayerFragment");
            }
        }
    };

    public static ProfileLibraryFragment newInstance() {
        Bundle args = new Bundle();

        ProfileLibraryFragment fragment = new ProfileLibraryFragment();
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
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_ALL_LIBRARY_BOOK:
                bindDataToView((ArrayList<BookDetailEnt>) result);
                break;
            case WebServiceConstants.GET_PAGED_BOOKS:
                isOnCall = false;
                bindPagedBooksList((ArrayList<BookDetailEnt>) result);
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_library, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentPageNumber = 1;
        serviceHelper.enqueueCall(webService.getLibraryBooks(currentPageNumber, totalCount, culture, prefHelper.getUserToken()),
                WebServiceConstants.GET_ALL_LIBRARY_BOOK);
    }

    private void bindDataToView(ArrayList<BookDetailEnt> result) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(
                Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        booksCollections = new ArrayList<>();
        booksCollections.addAll(result);

        if (booksCollections.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvBooks.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvBooks.setVisibility(View.VISIBLE);
        }
        layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        rvBooks.BindRecyclerView(new LibraryBooksBinder(options, booksItemListener), booksCollections,
                layoutManager,
                new DefaultItemAnimator());
        rvBooks.getAdapter().setOnLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMoreItem(int position) {
                getPagedData();
            }
        });
    }

    private void bindPagedBooksList(ArrayList<BookDetailEnt> result) {
        if (result.size() <= 0) {
            canCallForMore = false;
        } else {
            booksCollections.addAll(result);
            rvBooks.notifyItemRangeChanged(layoutManager.findLastVisibleItemPosition(), result.size());
        }
    }

    private void getPagedData() {
        if (canCallForMore) {
            if (!isOnCall) {
                currentPageNumber = currentPageNumber + 1;
                //  progressBar.setVisibility(View.VISIBLE);
                isOnCall = true;
                serviceHelper.enqueueCall(webService.getLibraryBooks(currentPageNumber, totalCount, culture, prefHelper.getUserToken()),
                        WebServiceConstants.GET_PAGED_BOOKS);
            }
        }
    }
}