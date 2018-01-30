package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 1/29/2018.
 */
public class PlayedEpisodesFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    public static final String TAG = "PlayedEpisodesFragment";
    @BindView(R.id.rb_delete)
    RadioButton rbDelete;
    @BindView(R.id.rb_keep)
    RadioButton rbKeep;
    Unbinder unbinder;

    public static PlayedEpisodesFragment newInstance() {
        Bundle args = new Bundle();

        PlayedEpisodesFragment fragment = new PlayedEpisodesFragment();
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
        View view = inflater.inflate(R.layout.fragment_played_episode, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rbDelete.setOnCheckedChangeListener(this);
        rbKeep.setOnCheckedChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.addBackground();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.played_episodes));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.rb_delete:
                rbKeep.setChecked(!b);
                break;
            case R.id.rb_keep:
                rbDelete.setChecked(!b);
                break;
        }
    }
}