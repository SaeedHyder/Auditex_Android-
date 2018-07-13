package com.ingic.auditix.fragments.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 1/29/2018.
 */
public class NewEpisodesFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    public static final String TAG = "NewEpisodesFragment";
    @BindView(R.id.swt_stream)
    Switch swtStream;
    @BindView(R.id.swt_downloaded_wifi)
    Switch swtDownloadedWifi;
    @BindView(R.id.swt_donload_wifi_cell)
    Switch swtDonloadWifiCell;
    Unbinder unbinder;

    public static NewEpisodesFragment newInstance() {
        Bundle args = new Bundle();

        NewEpisodesFragment fragment = new NewEpisodesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.addBackground();
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.new_episode));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_episodes, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swtStream.setOnCheckedChangeListener(this);
        swtDownloadedWifi.setOnCheckedChangeListener(this);
        swtDonloadWifiCell.setOnCheckedChangeListener(this);
        swtDonloadWifiCell.setChecked(prefHelper.isDownloadOnAll());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.swt_stream:
                willbeimplementedinfuture();
                break;
            case R.id.swt_downloaded_wifi:
                swtDonloadWifiCell.setChecked(!b);
                break;
            case R.id.swt_donload_wifi_cell:
                prefHelper.setDownloadOnAll(b);
                swtDownloadedWifi.setChecked(!b);
                break;
        }
    }
}