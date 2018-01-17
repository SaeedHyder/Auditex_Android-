package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastCategoriesEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.FilterDoneClickListener;
import com.ingic.auditix.ui.ArrayListExpandableAdapter;
import com.ingic.auditix.ui.binders.FilterExpandableBinder;
import com.ingic.auditix.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;

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
    @BindView(R.id.exp_filters)
    ExpandableListView expFilters;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.btn_done)
    Button btnDone;
    Unbinder unbinder;
    FilterExpandableBinder binder;
    private ArrayListExpandableAdapter<String, PodcastCategoriesEnt> adapter;
    private ArrayList<String> groupCollections;
    private HashMap<String, ArrayList<PodcastCategoriesEnt>> childCollections;
    private ArrayList<PodcastCategoriesEnt> child1Collection;
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

        if (getArguments() != null) {
        }

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_ALL_FILTER:
                bindListData((ArrayList<PodcastCategoriesEnt>) result);
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
        serviceHelper.enqueueCall(webService.getAllFilters(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_FILTER, false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void bindListData(ArrayList<PodcastCategoriesEnt> result) {
        groupCollections = new ArrayList<>(4);
        // groupCollections.add(new FilterGroupEnt(getString(R.string.by_duration)));
        groupCollections.add(getString(R.string.by_genre));
        child1Collection = new ArrayList<>();
        child1Collection.addAll(result);
        childCollections = new HashMap<>();

        childCollections.put(groupCollections.get(0), child1Collection);
        bindListView();

    }

    private void bindListView() {
//        expFilters.setExpanded(true);

        binder = new FilterExpandableBinder();
        adapter = new ArrayListExpandableAdapter<>(getDockActivity(), groupCollections, childCollections, binder);

        expFilters.setAdapter(adapter);
        for (int i = 0; i < childCollections.size(); i++) {
            expFilters.expandGroup(i);
        }
        expFilters.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });
        adapter.notifyDataSetChanged();
        btnClose.requestFocus();
        btnClose.setFocusable(true);
        btnClose.requestFocus();
        /*setListViewHeight(expFilters, 0);
        setListViewHeight(expFilters, 1);*/

    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        BaseExpandableListAdapter listAdapter = (BaseExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public void clearFilters() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void setListener(FilterDoneClickListener listener) {
        this.listener = listener;
    }

    @OnClick({R.id.btn_clear, R.id.btn_done, R.id.btn_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                adapter.notifyDataSetChanged();
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

        }
    }

}