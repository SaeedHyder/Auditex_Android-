package com.ingic.auditix.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ingic.auditix.R;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.ui.slidinglayout.SlidingUpPanelLayout;
import com.ingic.auditix.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 4/2/2018.
 */
public class SampleFragment extends BaseFragment {
    public static final String TAG = "SampleFragment";
    @BindView(R.id.img_player_cover)
    ImageView imgPlayerCover;
    @BindView(R.id.txt_player_item_name)
    AnyTextView txtPlayerItemName;
    @BindView(R.id.txt_player_item_album)
    AnyTextView txtPlayerItemAlbum;
    @BindView(R.id.btn_player_play)
    ImageView btnPlayerPlay;
    @BindView(R.id.container_player)
    LinearLayout containerPlayer;

    Unbinder unbinder;

    public static SampleFragment newInstance() {
        Bundle args = new Bundle();

        SampleFragment fragment = new SampleFragment();
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
        View view = inflater.inflate(R.layout.fragm, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().mSlidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                float offset = Math.abs((slideOffset - 1));
                Log.e(TAG, "onPanelSlide: offset :  " + offset);
                containerPlayer.setAlpha(offset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                switch (newState) {
                    case HIDDEN:
                        break;
                    case DRAGGING:
                        break;
                    case EXPANDED:
                        break;
                    case ANCHORED:
                        break;
                    case COLLAPSED:
                        break;
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}