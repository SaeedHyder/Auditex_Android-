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
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.binders.CartBinder;
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
 * Created on 12/21/2017.
 */
public class CartListFragment extends BaseFragment implements RecyclerViewItemListener {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.rv_cart)
    CustomRecyclerView rvCart;
    Unbinder unbinder;
    private ArrayList<BookDetailEnt> cartCollections;

    public static CartListFragment newInstance() {
        Bundle args = new Bundle();

        CartListFragment fragment = new CartListFragment();
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
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.addBackground();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.cart));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void bindView() {
        cartCollections = new ArrayList<>();
        RealmResults<BookDetailEnt> results = getMainActivity().realm.where(BookDetailEnt.class).findAll();
        cartCollections.addAll(results.subList(0, results.size()));
        if (cartCollections.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvCart.setVisibility(View.VISIBLE);

        }
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        rvCart.BindRecyclerView(new CartBinder(options, this), cartCollections, new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false),
                new DefaultItemAnimator());
    }

    @Override
    public void onRecyclerItemButtonClicked(Object Ent, int position) {
        BookDetailEnt ent = (BookDetailEnt) Ent;
        if (!getMainActivity().realm.isInTransaction()) {
            getMainActivity().realm.beginTransaction();
        }

        ent.deleteFromRealm();

        getMainActivity().realm.commitTransaction();
        cartCollections.remove(position);
        rvCart.notifyItemRemoved(position);
        rvCart.notifyItemRangeChanged(position, cartCollections.size());
        if (cartCollections.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRecyclerItemClicked(Object Ent, int position) {

    }
}