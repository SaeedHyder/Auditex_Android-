package com.ingic.auditix.fragments.settings;

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
public class PrivacyPolicyFragment extends BaseFragment {
    public static final String TAG = "PrivacyPolicyFragment";
    @BindView(R.id.txt_privacy_policy)
    TextView txtPrivacyPolicy;
    Unbinder unbinder;

    public static PrivacyPolicyFragment newInstance() {
        Bundle args = new Bundle();

        PrivacyPolicyFragment fragment = new PrivacyPolicyFragment();
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
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CMSEnt results = getMainActivity().realm.where(CMSEnt.class).equalTo(AppConstants.KEY_CMS_TYPE,
                WebServiceConstants.CMS_TYPE_PRIVACY).findFirst();
        if (results != null) {
            txtPrivacyPolicy.setText(results.getValue());
            txtPrivacyPolicy.setMovementMethod(new ScrollingMovementMethod());
        }
    }
    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.addBackground();
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.privacy_policy));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}