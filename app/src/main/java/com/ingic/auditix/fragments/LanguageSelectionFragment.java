package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 1/2/2018.
 */
public class LanguageSelectionFragment extends BaseFragment {
    @BindView(R.id.btn_lan_eng)
    AnyTextView btnLanEng;
    @BindView(R.id.btn_lan_ar)
    AnyTextView btnLanAr;
    @BindView(R.id.btn_next)
    Button btnSkip;
    Unbinder unbinder;

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    public static LanguageSelectionFragment newInstance() {
        Bundle args = new Bundle();

        LanguageSelectionFragment fragment = new LanguageSelectionFragment();
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
        View view = inflater.inflate(R.layout.fragment_language_selection, container, false);
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

    @OnClick({R.id.btn_lan_eng, R.id.btn_lan_ar, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_lan_eng:
                setTextColor(btnLanEng, btnLanAr);
               // willbeimplementedinfuture();
                break;
            case R.id.btn_lan_ar:
                //setTextColor(btnLanAr, btnLanEng);
                willbeimplementedinfuture();
                break;
            case R.id.btn_next:
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(WalkThroughFragment.newInstance(), "WalkThroughFragment");
                break;
        }
    }

    private void setTextColor(AnyTextView selectedBtn, AnyTextView unSelectedBtn) {
        selectedBtn.setTextColor(getResources().getColor(R.color.app_orange));
        unSelectedBtn.setTextColor(getResources().getColor(R.color.language_font_gray));
    }
}