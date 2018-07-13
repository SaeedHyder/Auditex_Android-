package com.ingic.auditix.fragments.standard;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.CMSEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 1/26/2018.
 */
public class AboutUsFragment extends BaseFragment {
    public static final String TAG = "AboutUsFragment";
    @BindView(R.id.txt_about_us)
    TextView txtAboutUs;
    Unbinder unbinder;

    public static AboutUsFragment newInstance() {
        Bundle args = new Bundle();

        AboutUsFragment fragment = new AboutUsFragment();
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
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.about_us));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CMSEnt results = getMainActivity().realm.where(CMSEnt.class).equalTo(AppConstants.KEY_CMS_TYPE,
                WebServiceConstants.CMS_TYPE_ABOUT).findFirst();
        if (results != null) {
            txtAboutUs.setText(results.getValue());
            txtAboutUs.setMovementMethod(new ScrollingMovementMethod());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}