package com.ingic.auditix.fragments.books;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookCategoryEnt;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.books.LibraryBooksBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 7/18/18.
 */
public class FeatureAllBooksListingFragment extends BaseFragment {
    public static final String TAG = "FeatureAllBooksListingFragment";
    @BindView(R.id.txt_subscription_no_data)
    AnyTextView txtSubscriptionNoData;
    @BindView(R.id.rvListing)
    CustomRecyclerView rvListing;
    Unbinder unbinder;
    private BookCategoryEnt bookCategoryEnt;
    private RecyclerViewItemListener itemClickListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            BookDetailEnt ent = (BookDetailEnt) Ent;
            openBookDetail(ent.getBookID());
        }
    };

    public static FeatureAllBooksListingFragment newInstance(BookCategoryEnt bookCategoryEnt) {
        Bundle args = new Bundle();

        FeatureAllBooksListingFragment fragment = new FeatureAllBooksListingFragment();
        fragment.setArguments(args);
        fragment.setBookCategoryEnt(bookCategoryEnt);
        return fragment;
    }

    public void setBookCategoryEnt(BookCategoryEnt bookCategoryEnt) {
        this.bookCategoryEnt = bookCategoryEnt;
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
        titleBar.addBackground();
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.books));
        titleBar.showBackButton();
    }
    private void openBookDetail(Integer bookID) {
        replaceFromParentFragment(BookDetailFragment.newInstance(bookID), BookDetailFragment.TAG);
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
        if (bookCategoryEnt != null && bookCategoryEnt.getCategoryBooks().size() <= 0) {
            txtSubscriptionNoData.setVisibility(View.VISIBLE);
        } else
            txtSubscriptionNoData.setVisibility(View.GONE);
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(
                Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        rvListing.BindRecyclerView(new LibraryBooksBinder(options, itemClickListener), bookCategoryEnt.getCategoryBooks(), new LinearLayoutManager(getDockActivity()), new DefaultItemAnimator());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}