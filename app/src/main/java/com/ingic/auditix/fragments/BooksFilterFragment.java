package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookGenreEnt;
import com.ingic.auditix.entities.FilterEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.FilterDoneClickListener;
import com.ingic.auditix.ui.binders.FilterBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 2/2/2018.
 */
public class BooksFilterFragment extends BaseFragment {
    public static final String TAG = "BooksFilterFragment";
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.img_group_arrow)
    ImageView imgGroupArrow;
    @BindView(R.id.rvfilters)
    CustomRecyclerView rvfilters;
    Unbinder unbinder;
    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.btn_done)
    Button btnDone;
    FilterBinder binder;
    private ArrayList<FilterEnt> bookFilters;
    private FilterDoneClickListener listener;

    public static BooksFilterFragment newInstance() {
        Bundle args = new Bundle();

        BooksFilterFragment fragment = new BooksFilterFragment();
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
            case WebServiceConstants.GET_ALL_BOOKS_FILTER:
                bindData((ArrayList<BookGenreEnt>) result);
                break;
        }
    }

    public void clearFilters() {
        if (rvfilters != null && binder != null) {
            binder.clearFilterIDs();
            rvfilters.notifyDataSetChanged();
        }
    }

    public String getFiltersList() {
        return binder == null ? "" : binder.getFilterCheckIDs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (bookFilters == null && binder == null) {
            serviceHelper.enqueueCall(webService.getAllBooksFilters(null, prefHelper.getUserToken()), WebServiceConstants.GET_ALL_BOOKS_FILTER, false);
        } else {
            rvfilters.BindRecyclerView(binder, bookFilters,
                    new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false),
                    new DefaultItemAnimator());
        }
    }

    public void setListener(FilterDoneClickListener listener) {
        this.listener = listener;
    }

    private void bindData(ArrayList<BookGenreEnt> result) {

        bookFilters = new ArrayList<>();
        for (BookGenreEnt item : result
                ) {
            bookFilters.add(new FilterEnt(item.getGenreID(), item.getGenreName()));
        }
        binder = new FilterBinder();
        rvfilters.BindRecyclerView(binder, bookFilters,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false),
                new DefaultItemAnimator());

    }

    @OnClick({R.id.btn_clear, R.id.btn_done, R.id.btn_close,R.id.container_genre,R.id.upper_container,R.id.lower_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                if (binder != null && rvfilters != null && bookFilters != null && bookFilters.size() > 0) {
                    binder.clearFilterIDs();
                    rvfilters.notifyDataSetChanged();
                }
                break;
            case R.id.btn_done:
                if (listener != null) {
                    listener.onDoneFiltering(binder == null ? "" : binder.getFilterCheckIDs());
                }
                getMainActivity().closeDrawer();
                break;
            case R.id.btn_close:
                getMainActivity().closeDrawer();
                break;
            case R.id.container_genre:
                break;

        }
    }
}