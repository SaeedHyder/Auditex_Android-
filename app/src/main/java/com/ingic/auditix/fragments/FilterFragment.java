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
import com.ingic.auditix.entities.FilterEnt;
import com.ingic.auditix.entities.PodcastCategoriesEnt;
import com.ingic.auditix.entities.PodcastLocationEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.FilterDoneClickListener;
import com.ingic.auditix.ui.binders.FilterBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 12/28/2017.
 */
public class FilterFragment extends BaseFragment {
    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.btn_done)
    Button btnDone;
    Unbinder unbinder;
    FilterBinder binder;
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.img_group_arrow)
    ImageView imgGroupArrow;
    @BindView(R.id.rvfilters)
    CustomRecyclerView rvfilters;
    private ArrayList<FilterEnt> child1Collection;
    private FilterDoneClickListener listener;

    public static FilterFragment newInstance() {
        Bundle args = new Bundle();

        FilterFragment fragment = new FilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_ALL_FILTER:
                bindPodcastListData((ArrayList<PodcastLocationEnt>) result);
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
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtTitle.setText(getResString(R.string.by_location));
        if (child1Collection == null && binder == null) {
            serviceHelper.enqueueCall(webService.getAllFilter(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_FILTER, false);
        } else {
            rvfilters.BindRecyclerView(binder, child1Collection,
                    new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false),
                    new DefaultItemAnimator());
        }
    }

    private void bindPodcastListData(ArrayList<PodcastLocationEnt> result) {

        child1Collection = new ArrayList<>();
        for (PodcastLocationEnt item : result
                ) {
            child1Collection.add(new FilterEnt(item.getCountrycode(), item.getCountryname()));
        }
        binder = new FilterBinder();
        rvfilters.BindRecyclerView(binder, child1Collection,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false),
                new DefaultItemAnimator());


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

    public void setListener(FilterDoneClickListener listener) {
        this.listener = listener;
    }

    @OnClick({R.id.btn_clear, R.id.btn_done, R.id.btn_close,R.id.container_genre,R.id.upper_container,R.id.lower_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                if (binder != null && rvfilters != null && child1Collection != null && child1Collection.size() > 0) {
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