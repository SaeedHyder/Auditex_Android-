package com.ingic.auditix.fragments.books;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookCategoriesEnt;
import com.ingic.auditix.entities.BookCategoryEnt;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.entities.EnableFilterDataEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.standard.HomeTabFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.binders.books.AllBooksBinder;
import com.ingic.auditix.ui.binders.books.BooksCategoryBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import droidninja.filepicker.utils.GridSpacingItemDecoration;

/**
 * Created on 12/23/2017.
 */
public class BooksFragment extends BaseFragment implements ViewPagerFragmentLifecycleListener {
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
    String culture = null;
    ArrayList<BookDetailEnt> allBooksCollections;
    @BindView(R.id.MainContainer)
    LinearLayout MainContainer;
    @BindView(R.id.containerFragment)
    FrameLayout containerFragment;
    private BookCategoryEnt bookCategoryEnt;
    private boolean isFilterVisible = false;
    private EnableFilterDataEnt filterDataEnt;
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
    private RecyclerViewItemListener categoryListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            openCategoryListing(((BookCategoriesEnt) Ent).getBookcategoryid());

        }
    };

    public static BooksFragment newInstance() {
        Bundle args = new Bundle();

        BooksFragment fragment = new BooksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setBookCategoryEnt(BookCategoryEnt bookCategoryEnt) {
        this.bookCategoryEnt = bookCategoryEnt;
    }

    public void openBookDetail(Integer bookID) {
        replaceFromParentFragment(BookDetailFragment.newInstance(bookID), BookDetailFragment.TAG);
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

            case WebServiceConstants.GET_BOOKS_FEATURED:
                setBookCategoryEnt((BookCategoryEnt) result);
                bindCategoriesData((BookCategoryEnt) result);
                break;
            case WebServiceConstants.GET_BOOKS_CATEGORIES:
                bindRecommended((ArrayList<BookCategoriesEnt>) result);
                break;

        }
    }
    @Override
    public void setTitleBar(TitleBar titleBar) {
        if (getMainActivity().booksFilterFragment != null) {
            getMainActivity().setRightSideFragment(getMainActivity().booksFilterFragment);
            getMainActivity().booksFilterFragment.setListener((filters, isClear) -> {
                if (isClear) {
                    filterDataEnt = null;
                    hideFilterList();
                } else {
                    filterDataEnt = filters;
                    showFilterList();
                }
            });
        }
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.books));
        titleBar.showMenuButton();
        titleBar.showFilterButton(v -> {
            getMainActivity().isNavigationGravityRight = true;
            getMainActivity().getDrawerLayout().openDrawer(Gravity.RIGHT);
        });

    }

    private void bindRecommended(ArrayList<BookCategoriesEnt> result) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        GridLayoutManager defaultLayoutManager = new GridLayoutManager(getDockActivity(), 3, GridLayoutManager.VERTICAL, false);
        rvRecommended.BindRecyclerView(new BooksCategoryBinder(options, categoryListener), result,
                defaultLayoutManager, new DefaultItemAnimator());
        rvRecommended.setHasFixedSize(true);
        rvRecommended.addItemDecoration(new GridSpacingItemDecoration(3, Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)), true));
        rvRecommended.setNestedScrollingEnabled(false);
        if (result.size() <= 0) {
            txtRecommendedNoData.setVisibility(View.VISIBLE);
            rvRecommended.setVisibility(View.GONE);
        } else {
            txtRecommendedNoData.setVisibility(View.GONE);
            rvRecommended.setVisibility(View.VISIBLE);
        }
    }

    private void bindCategoriesData(BookCategoryEnt result) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        allBooksCollections = new ArrayList<>();

        allBooksCollections.addAll(result.getCategoryBooks());

        if (allBooksCollections.size() <= 0) {
            btnAllbooksSeeall.setVisibility(View.GONE);
            txtAllBooksNoData.setVisibility(View.VISIBLE);
            rvAllBooks.setVisibility(View.GONE);
        } else {
            btnAllbooksSeeall.setVisibility(View.VISIBLE);
            txtAllBooksNoData.setVisibility(View.GONE);
            rvAllBooks.setVisibility(View.VISIBLE);
        }
        rvAllBooks.BindRecyclerView(new AllBooksBinder(options, booksItemListener), allBooksCollections,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false),
                new DefaultItemAnimator());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDefaultBooks();
        if (getMainActivity().booksFilterFragment != null) {
            getMainActivity().booksFilterFragment.clearFilters();
        }
       /* if (isFilterVisible) {
            showFilterList();
        } else {
            hideFilterList();
        }*/
    }


    private void getDefaultBooks() {
        serviceHelper.enqueueCall(webService.getBooksCategories(prefHelper.getUserToken()), WebServiceConstants.GET_BOOKS_CATEGORIES);
        serviceHelper.enqueueCall(webService.getFeaturesBooks(prefHelper.getUserToken()), WebServiceConstants.GET_BOOKS_FEATURED);
    }


    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper) {

    }

    private void openCategoryListing(int categoryID) {
        if (categoryID != -1) {
            replaceFromParentFragment(BookCategoryListFragment.newInstance(categoryID), BookCategoryListFragment.TAG);
        }
    }

    @OnClick({R.id.btn_allbooks_seeall, R.id.btn_recommne_seeall, R.id.btn_best_seeall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_allbooks_seeall:
                replaceFromParentFragment(FeatureAllBooksListingFragment.newInstance(bookCategoryEnt), FeatureAllBooksListingFragment.TAG);
                break;

        }
    }
    public void replaceFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        //transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.replace(R.id.containerFragment, fragment);
        transaction.commit();


    }
    private void showFilterList() {
        MainContainer.setVisibility(View.GONE);
        containerFragment.setVisibility(View.VISIBLE);
        replaceFragment(BookFilterListFragment.newInstance(filterDataEnt));
        isFilterVisible = true;
    }


    private void hideFilterList() {
        if (getChildFragmentManager().findFragmentById(R.id.containerFragment) != null) {
            getChildFragmentManager().beginTransaction().
                    remove(getChildFragmentManager().findFragmentById(R.id.containerFragment)).commit();
            getChildFragmentManager().popBackStack();
            getMainActivity().booksFilterFragment.clearFilters();
            MainContainer.setVisibility(View.VISIBLE);
            containerFragment.setVisibility(View.GONE);
            isFilterVisible = false;
        }

    }

   /* @Override
    public void onDoneFiltering(String filterIDs) {
        getDefaultBooks();
    }*/
}