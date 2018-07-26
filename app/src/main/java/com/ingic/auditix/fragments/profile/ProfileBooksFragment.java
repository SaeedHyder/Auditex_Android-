package com.ingic.auditix.fragments.profile;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.entities.BookFavoriteEnt;
import com.ingic.auditix.entities.FavoriteBookEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.fragments.books.BookDetailFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.books.BooksProfileFavouriteBinder;
import com.ingic.auditix.ui.binders.books.LibraryBooksBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.RealmResults;

/**
 * Created on 1/6/2018.
 */
public class ProfileBooksFragment extends BaseFragment {
    public static final String TAG = "ProfileBooksFragment";
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.rv_books)
    CustomRecyclerView rvBooks;
    Unbinder unbinder;
    String culture = null;
    @BindView(R.id.rvBooksFavourite)
    CustomRecyclerView rvBooksFavourite;
    @BindView(R.id.txt_favourite_no_data)
    AnyTextView txtFavouriteNoData;
    @BindView(R.id.txt_recommended)
    AnyTextView txtRecommended;
    @BindView(R.id.btn_recommne_seeall)
    AnyTextView btnRecommneSeeall;
    private ArrayList<BookDetailEnt> booksCollections;
    private LinearLayoutManager layoutManager;
    private RecyclerViewItemListener newSubscriptionListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {
            serviceHelper.enqueueCall(webService.RemoveBookFromFavorite(((BookFavoriteEnt) Ent).getBookID(), prefHelper.getUserToken()), WebServiceConstants.REMOVE_FAVORITE_BOOK);
        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            replaceFromParentFragment(BookDetailFragment.newInstance(((BookFavoriteEnt) Ent).getBookID()), BookDetailFragment.TAG);
        }
    };
    private RecyclerViewItemListener booksItemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            BookDetailEnt detailEnt = (BookDetailEnt) Ent;
            if (detailEnt != null && detailEnt.getChapters() != null && detailEnt.getChapters().getChapter().size() > 0) {
                if (getMainActivity().booksFilterFragment != null) {
                    getMainActivity().booksFilterFragment.clearFilters();
                }
                replaceFromParentFragment(BookDetailFragment.newInstance(detailEnt.getBookID()), BookDetailFragment.TAG);
            }
        }
    };
    private ArrayList<BookFavoriteEnt> favouriteCollection;

    public static ProfileBooksFragment newInstance() {
        Bundle args = new Bundle();

        ProfileBooksFragment fragment = new ProfileBooksFragment();
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
            case WebServiceConstants.REMOVE_FAVORITE_BOOK:
                serviceHelper.enqueueCall(webService.getBooksAllFavorite(culture, prefHelper.getUserToken()), WebServiceConstants.GET_ALL_FAVORITE);
                break;
            case WebServiceConstants.GET_ALL_FAVORITE:
                getAllFavouriteBooks(((FavoriteBookEnt) result).getBooks());
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    private void getAllFavouriteBooks(ArrayList<BookFavoriteEnt> results) {
        favouriteCollection = new ArrayList<>(results);
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(
                Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        rvBooksFavourite.BindRecyclerView(new BooksProfileFavouriteBinder(options,
                        newSubscriptionListener, prefHelper), favouriteCollection, new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false)
                , new DefaultItemAnimator());
        rvBooksFavourite.setNestedScrollingEnabled(false);
        if (favouriteCollection.size() <= 0) {
            txtFavouriteNoData.setVisibility(View.VISIBLE);
            rvBooksFavourite.setVisibility(View.GONE);
        } else {
            txtFavouriteNoData.setVisibility(View.GONE);
            rvBooksFavourite.setVisibility(View.VISIBLE);

        }
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
        serviceHelper.enqueueCall(webService.getBooksAllFavorite(culture, prefHelper.getUserToken()), WebServiceConstants.GET_ALL_FAVORITE);
        RealmResults<BookDetailEnt> results = getMainActivity().realm.where(BookDetailEnt.class).findAll();
        bindDataToView(new ArrayList<>(results.subList(0, results.size())));
    }

    private void bindDataToView(ArrayList<BookDetailEnt> result) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(
                Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        booksCollections = new ArrayList<>();
        booksCollections.addAll(result);

        /*if (booksCollections.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvBooks.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvBooks.setVisibility(View.VISIBLE);
        }*/
        rvBooks.BindRecyclerView(new LibraryBooksBinder(options, booksItemListener), booksCollections,
                new LinearLayoutManager(getDockActivity()),
                new DefaultItemAnimator());
        rvBooks.setNestedScrollingEnabled(false);
    }

}