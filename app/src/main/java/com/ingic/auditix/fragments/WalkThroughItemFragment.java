package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.interfaces.OnNextButtonListener;
import com.ingic.auditix.ui.views.AnyTextView;
import com.rd.PageIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 12/12/2017.
 */
public class WalkThroughItemFragment extends BaseFragment {
    @BindView(R.id.txt_title)
    AnyTextView txtTitle;
    @BindView(R.id.txt_subtitle)
    AnyTextView txtSubtitle;
    @BindView(R.id.txt_heading)
    AnyTextView txtHeading;
    @BindView(R.id.txt_description)
    AnyTextView txtDescription;
    Unbinder unbinder;
    private int position = 0;
    private int totalcount = 0;
    private boolean isFinished = false;
    private OnNextButtonListener nextButtonListener;

    public static WalkThroughItemFragment newInstance() {
        Bundle args = new Bundle();

        WalkThroughItemFragment fragment = new WalkThroughItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setContent(int Position, int TotalCount) {
        position = Position;
        totalcount = TotalCount;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walkthrough_item, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

   /* @OnClick(R.id.btn_next)
    public void onViewClicked() {
        if (position == totalcount - 1) {
            getDockActivity().popBackStackTillEntry(0);
            getDockActivity().replaceDockableFragment(AccessSelectionFragment.newInstance(), false);
        } else {
            if (nextButtonListener != null) {
                nextButtonListener.moveToNextWalkthrough(position);
            }
        }
    }*/
}