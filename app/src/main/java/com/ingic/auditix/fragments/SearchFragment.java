package com.ingic.auditix.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NewsCategoryEnt;
import com.ingic.auditix.entities.SearchEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.LoadMoreListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.interfaces.ViewPagerFragmentLifecycleListener;
import com.ingic.auditix.ui.binders.SearchBinder;
import com.ingic.auditix.ui.views.AnyEditTextView;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 12/23/2017.
 */
public class SearchFragment extends BaseFragment implements ViewPagerFragmentLifecycleListener {
    @BindView(R.id.edt_search)
    AnyEditTextView edtSearch;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.rv_search)
    CustomRecyclerView rvSearch;
    Unbinder unbinder;
    int currentPageNumber = 1;
    int totalCount = 10;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    private RecyclerViewItemListener ItemListener = new RecyclerViewItemListener() {
        @Override
        public void onRecyclerItemButtonClicked(Object Ent, int position) {

        }

        @Override
        public void onRecyclerItemClicked(Object Ent, int position) {
            SearchEnt ent = (SearchEnt) Ent;
            openItemDetailPage(ent, Integer.parseInt(ent.getType()));
        }
    };

    private LinearLayoutManager layoutManager;
    private ArrayList<SearchEnt> searchCollection;
    private boolean canCallForMore = true;
    private boolean isOnCall;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
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
    public void onResume() {
        super.onResume();
        setTitleBar(((HomeTabFragment) getParentFragment()).getTitleBar());
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_SEARCH:
                bindDataToView((ArrayList<SearchEnt>) result);
                break;
            case WebServiceConstants.GET_PAGED_SEARCH:
                isOnCall = false;
                bindPagedSearchList((ArrayList<SearchEnt>) result);
                break;
        }
    }

    public void setTitleBar(TitleBar titleBar) {
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.search));
        titleBar.showBackButton();

    }

    private void openItemDetailPage(SearchEnt searchItem, Integer type) {
        switch (type) {
            case WebServiceConstants.SEARCH_TYPE_PODCAST:
                getDockActivity().replaceDockableFragment(PodcastDetailFragment.newInstance(searchItem.getID()), PodcastDetailFragment.TAG);
                break;
            case WebServiceConstants.SEARCH_TYPE_BOOKS:
                getDockActivity().replaceDockableFragment(BookDetailFragment.newInstance(searchItem.getID()), BookDetailFragment.TAG);
                break;
            case WebServiceConstants.SEARCH_TYPE_NEWS:
                NewsCategoryEnt ent = new NewsCategoryEnt();
                ent.setId(searchItem.getID());
                ent.setSourceImageUrl(searchItem.getImageUrl());
                ent.setSourceName(searchItem.getName());
                getDockActivity().replaceDockableFragment(NewsCategoryDetailFragment.newInstance(ent), NewsCategoryDetailFragment.TAG);
                break;
        }
    }

    private void bindPagedSearchList(ArrayList<SearchEnt> result) {
        if (result.size() <= 0) {
            canCallForMore = false;
        } else {
            searchCollection.addAll(result);
            rvSearch.notifyItemRangeChanged(layoutManager.findLastVisibleItemPosition(), result.size());
        }
    }

    private void bindDataToView(ArrayList<SearchEnt> result) {
        DisplayImageOptions options = getMainActivity().getImageLoaderRoundCornerTransformation(
                Math.round(getDockActivity().getResources().getDimension(R.dimen.x10)));
        searchCollection = new ArrayList<>();
        searchCollection.addAll(result);

        if (searchCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            rvSearch.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            rvSearch.setVisibility(View.VISIBLE);
        }
        layoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false);
        rvSearch.BindRecyclerView(new SearchBinder(options, ItemListener), searchCollection,
                layoutManager,
                new DefaultItemAnimator());
        rvSearch.getAdapter().setOnLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMoreItem(int position) {
                getPagedData();
            }
        });
    }

    private void getPagedData() {
        if (canCallForMore) {
            if (!isOnCall) {
                currentPageNumber = currentPageNumber + 1;
                //  progressBar.setVisibility(View.VISIBLE);
                isOnCall = true;
                serviceHelper.enqueueCall(webService.getSearchItem(currentPageNumber, totalCount, edtSearch.getText().toString(), prefHelper.getUserToken()),
                        WebServiceConstants.GET_PAGED_SEARCH);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListener();
//        setTitleBar(((HomeTabFragment) getParentFragment()).getTitleBar());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setListener() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    searchByText(edtSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment(Context context, BasePreferenceHelper preferenceHelper) {

    }

    private void searchByText(String text) {
        if (text.trim().equals("")) return;
        UIHelper.hideSoftKeyboard(getDockActivity(), edtSearch);
        serviceHelper.enqueueCall(webService.getSearchItem(currentPageNumber, totalCount, text, prefHelper.getUserToken()), WebServiceConstants.GET_SEARCH);
    }

    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        if (searchCollection != null)
            searchCollection.clear();
        searchByText(edtSearch.getText().toString());
    }
}