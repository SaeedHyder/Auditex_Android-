package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.CartEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.interfaces.CartDeleteButtonListener;
import com.ingic.auditix.ui.binders.CartBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 12/21/2017.
 */
public class CartListFragment extends BaseFragment implements CartDeleteButtonListener {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.rv_cart)
    CustomRecyclerView rvCart;
    Unbinder unbinder;
    private ArrayList<CartEnt> cartCollections;

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

    private void bindView() {
        cartCollections = new ArrayList<>();
        cartCollections.add(new CartEnt(R.drawable.dummyprofile,"Just Courage","Peter Parker",
                "Peter Parker","Thrill","$100"));
        cartCollections.add(new CartEnt(R.drawable.dummyprofile,"Just Courage","Peter Parker",
                "Peter Parker","Thrill","$100"));
        cartCollections.add(new CartEnt(R.drawable.dummyprofile,"Just Courage","Peter Parker",
                "Peter Parker","Thrill","$100"));
        cartCollections.add(new CartEnt(R.drawable.dummyprofile,"Just Courage","Peter Parker",
                "Peter Parker","Thrill","$100"));
        if (cartCollections.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvCart.setVisibility(View.VISIBLE);

        }
        rvCart.BindRecyclerView(new CartBinder(this),cartCollections,new LinearLayoutManager(getDockActivity(),LinearLayoutManager.VERTICAL,false),
                new DefaultItemAnimator());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
    public void onDeletePressed(Object ent, int position) {
        //cartCollections.remove(position);
        //rvCart.notifyItemRemoved(position);
    }
}