package com.ingic.auditix.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.CartEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.ui.adapters.ArrayListAdapter;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 1/6/2018.
 */
public class MyDownloadsFragment extends BaseFragment {
    public static final String TAG = "MyDownloadsFragment";
    @BindView(R.id.lv_downloads)
    CustomRecyclerView rvDownloads;
    Unbinder unbinder;
    private ArrayList<CartEnt> downloadsCollections;
    private ArrayListAdapter<CartEnt> adapter;

    public static MyDownloadsFragment newInstance() {
        Bundle args = new Bundle();

        MyDownloadsFragment fragment = new MyDownloadsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
//        adapter = new ArrayListAdapter<CartEnt>(getDockActivity(), new PodcastDownloadBinder());

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloads, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {
        downloadsCollections = new ArrayList<>();
        downloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        downloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        downloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        downloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        downloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        downloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        downloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));
        downloadsCollections.add(new CartEnt(0, "as", "Asd", "asd", "Asd", "Asd"));

        // rvDownloads.setExpanded(true);
        bindview();
    }

    private void bindview() {
      /*  adapter.clearList();
        rvDownloads.setAdapter(adapter);
        adapter.addAll(downloadsCollections);
        adapter.notifyDataSetChanged();*/
        rvDownloads.setNestedScrollingEnabled(false);

      /*  rvDownloads.BindRecyclerView(new PodcastDownloadBinder(), downloadsCollections, new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false),
                new DefaultItemAnimator());*/
        //setListViewHeightBasedOnChildren(sideoptions);

    }


}