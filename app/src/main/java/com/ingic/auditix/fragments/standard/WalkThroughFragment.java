package com.ingic.auditix.fragments.standard;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.WalkthroughEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.OnNextButtonListener;
import com.ingic.auditix.ui.adapters.ViewPagerAdapter;
import com.ingic.auditix.ui.views.TitleBar;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 12/12/2017.
 */
public class WalkThroughFragment extends BaseFragment implements OnNextButtonListener {
    @BindView(R.id.pager)
    ViewPager pager;
    Unbinder unbinder;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;
    @BindView(R.id.btn_next)
    Button btnNext;
    private ArrayList<WalkThroughItemFragment> pagesArray;
    private ViewPagerAdapter adapter;
    private int totalcount;

    public static WalkThroughFragment newInstance() {
        Bundle args = new Bundle();

        WalkThroughFragment fragment = new WalkThroughFragment();
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
            case WebServiceConstants.GET_ALL_WALKTHROUGH:
                initViewPager((ArrayList<WalkthroughEnt>) result);
                break;
        }
    }

    @Override
    public void ResponseFailure(String tag) {
        switch (tag) {
            case WebServiceConstants.GET_ALL_WALKTHROUGH:
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(AccessSelectionFragment.newInstance(), false);
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
        View view = inflater.inflate(R.layout.fragment_walkthrough, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serviceHelper.enqueueCall(webService.getAllWalkthrough(), WebServiceConstants.GET_ALL_WALKTHROUGH);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initViewPager(ArrayList<WalkthroughEnt> result) {
        pagesArray = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            WalkThroughItemFragment fragment = new WalkThroughItemFragment();
            fragment.setContent(i, result.size(), result.get(i));
            pagesArray.add(fragment);
        }
        adapter = new ViewPagerAdapter(getChildFragmentManager(), pagesArray);
        pager.setAdapter(adapter);
        totalcount = pagesArray.size();
//        pageIndicatorView.setViewPager(pager);
        pageIndicatorView.setCount(totalcount);
        pageIndicatorView.setSelection(pager.getCurrentItem());
        if (0 == totalcount - 1) {
            btnNext.setText(R.string.lets_begin);
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == totalcount - 1) {
                    btnNext.setText(R.string.lets_begin);
                }else {
                    btnNext.setText(R.string.next);
                }
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void moveToNextWalkthrough(int position) {
        if (pager != null) {
            if (position + 1 < pagesArray.size()) {
                pager.setCurrentItem(position + 1, true);
            }
        }
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        int position = pager.getCurrentItem();
        if (position == totalcount - 1) {
            getDockActivity().popBackStackTillEntry(0);
            getDockActivity().replaceDockableFragment(AccessSelectionFragment.newInstance(), false);
        } else {
            if (position + 1 < totalcount) {
                pageIndicatorView.setSelection(position + 1);
                pager.setCurrentItem(position + 1, true);
            }
        }
    }
}