package com.ingic.auditix.fragments.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.FavoriteEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.ui.adapters.ArrayListExpandableAdapter;
import com.ingic.auditix.ui.binders.FavoriteBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomExpandableListView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 1/5/2018.
 */
public class FavoriteFragment extends BaseFragment {
    public static final String TAG = "FavoriteFragment";
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.exl_favorite)
    CustomExpandableListView exlFavorite;
    Unbinder unbinder;
    private ArrayListExpandableAdapter<String, FavoriteEnt> adapter;
    private ArrayList<String> listGroup;
    private ArrayList<FavoriteEnt> listChild;
    private HashMap<String, ArrayList<FavoriteEnt>> childCollections;

    public static FavoriteFragment newInstance() {
        Bundle args = new Bundle();

        FavoriteFragment fragment = new FavoriteFragment();
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
        titleBar.setSubHeading(getString(R.string.my_favourite));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindListData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void bindListData() {
        listGroup = new ArrayList<>(3);
        listGroup.add(getString(R.string.group_news));
        listGroup.add(getString(R.string.group_podcast));
        listGroup.add(getString(R.string.group_books));
        childCollections = new HashMap<>();
        listChild = new ArrayList<>();
        listChild.add(new FavoriteEnt(AppConstants.DRAWABLE_PATH + R.drawable.dummyalbum,
                "Ed Sheeran", "Ed Sheeran"));
        listChild.add(new FavoriteEnt(AppConstants.DRAWABLE_PATH + R.drawable.dummyalbum,
                "Ed Sheeran", "Ed Sheeran"));
        listChild.add(new FavoriteEnt(AppConstants.DRAWABLE_PATH + R.drawable.dummyalbum,
                "Ed Sheeran", "Ed Sheeran"));
        childCollections.put(listGroup.get(0), listChild);
        childCollections.put(listGroup.get(1), listChild);
        childCollections.put(listGroup.get(2), listChild);
        if (childCollections.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            exlFavorite.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            exlFavorite.setVisibility(View.VISIBLE);

        }
        bindListView();

    }

    private void bindListView() {
//        exlFavorite.setExpanded(true);
        int radius = Math.round(getResources().getDimension(R.dimen.x10));
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(radius);
        adapter = new ArrayListExpandableAdapter<>(getDockActivity(), listGroup, childCollections, new FavoriteBinder(options));

        exlFavorite.setAdapter(adapter);
        for (int i = 0; i < childCollections.size(); i++) {
            exlFavorite.expandGroup(i);
        }
        exlFavorite.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });
        adapter.notifyDataSetChanged();


    }
}