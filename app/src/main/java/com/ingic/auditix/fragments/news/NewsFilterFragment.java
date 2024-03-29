package com.ingic.auditix.fragments.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.DurationEnt;
import com.ingic.auditix.entities.EnableFilterDataEnt;
import com.ingic.auditix.entities.LocationEnt;
import com.ingic.auditix.entities.NewsFilterEnt;
import com.ingic.auditix.fragments.abstracts.BaseFragment;
import com.ingic.auditix.global.WebServiceConstants;
import com.ingic.auditix.interfaces.FilterDoneClickListener;
import com.ingic.auditix.ui.binders.FilterBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.CustomRecyclerView;
import com.ingic.auditix.ui.views.sRangeSeekBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created on 7/20/18.
 */
public class NewsFilterFragment extends BaseFragment {
    public static final String TAG = "NewsFilterFragment";
    @BindView(R.id.rgbduration)
    sRangeSeekBar<Integer> rgbduration;
    @BindView(R.id.rgbSubscriber)
    sRangeSeekBar<Integer> rgbSubscriber;
    @BindView(R.id.swtInternational)
    Switch swtInternational;
    @BindView(R.id.rvfilters)
    CustomRecyclerView rvfilters;
    Unbinder unbinder;
    @BindView(R.id.txtMinDurationText)
    AnyTextView txtMinDurationText;
    @BindView(R.id.txtMaxDurationText)
    AnyTextView txtMaxDurationText;
    @BindView(R.id.txtMinSubscriberText)
    AnyTextView txtMinSubscriberText;
    @BindView(R.id.txtMaxSubscriberText)
    AnyTextView txtMaxSubscriberText;
    private FilterBinder binder;
    private ArrayList<LocationEnt> locationCollection;
    private FilterDoneClickListener listener;
    private boolean isClear = true;

    public boolean isClear() {
        return isClear;
    }

    public static NewsFilterFragment newInstance() {
        Bundle args = new Bundle();

        NewsFilterFragment fragment = new NewsFilterFragment();
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
            case WebServiceConstants.GET_ALL_FILTER:
                bindFilterData((NewsFilterEnt) result);
                break;
        }
    }

    public void clearFilters() {
        if (rvfilters != null && binder != null) {
            binder.clearFilterIDs();
            rvfilters.notifyDataSetChanged();
            rgbduration.resetSelectedValues();
            rgbSubscriber.resetSelectedValues();
            isClear = false;
        }
    }

    public String getFiltersCountries() {
        return binder == null ? "" : binder.getFilterCheckIDs();
    }

    public void setListener(FilterDoneClickListener listener) {
        this.listener = listener;
    }

    private void bindFilterData(NewsFilterEnt result) {
        DurationEnt durationEnt =result.getMinMaxSubscibersAndDuration();
        rgbduration.setRangeValues(durationEnt.getMinDuration(), durationEnt.getMaxDuration());
        rgbSubscriber.setRangeValues(durationEnt.getMinSubscriber(), durationEnt.getMaxSubscriber());
        txtMinDurationText.setText(durationEnt.getMinDuration()+"");
        txtMaxDurationText.setText(durationEnt.getMaxDuration()+"");
        txtMinSubscriberText.setText(durationEnt.getMinSubscriber()+"");
        txtMaxSubscriberText.setText(durationEnt.getMaxSubscriber()+"");
        swtInternational.setVisibility(View.VISIBLE);
        rgbSubscriber.setOnRangeSeekBarChangeListener((bar, minValue, maxValue) -> {

            txtMinSubscriberText.setText(rgbSubscriber.getSelectedMinValue().toString());
            txtMaxSubscriberText.setText(rgbSubscriber.getSelectedMaxValue().toString());
            isClear = false;
        });
        rgbduration.setOnRangeSeekBarChangeListener((bar, minValue, maxValue) -> {
            txtMinDurationText.setText(rgbduration.getSelectedMinValue().toString());
            txtMaxDurationText.setText(rgbduration.getSelectedMaxValue().toString());
            isClear = false;
        });
        locationCollection = new ArrayList<>(result.getLocations());
        rvfilters.BindRecyclerView(binder, locationCollection,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false),
                new DefaultItemAnimator());
        rvfilters.setNestedScrollingEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binder = new FilterBinder();

        serviceHelper.enqueueCall(webService.getNewsFilterData(prefHelper.getUserToken()), WebServiceConstants.GET_ALL_FILTER);

    }



    @OnClick({R.id.btn_close, R.id.btn_clear, R.id.btn_done, R.id.buttons})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                if (binder != null && rvfilters != null && locationCollection != null && locationCollection.size() > 0) {
                    binder.clearFilterIDs();
                    rvfilters.notifyDataSetChanged();
                    rgbduration.resetSelectedValues();
                    rgbSubscriber.resetSelectedValues();
                    isClear = true;
                }
                break;
            case R.id.btn_done:
                if (listener != null && binder != null) {
                    if (isClear)
                        isClear = binder.isAllClear();
                    listener.onDoneFiltering(getUserEnableFilters(), isClear);

                }
                getMainActivity().closeDrawer();
                break;
            case R.id.btn_close:
                getMainActivity().closeDrawer();
                break;
        }
    }

    @NonNull
    public EnableFilterDataEnt getUserEnableFilters() {
        return new EnableFilterDataEnt(rgbduration.getSelectedMinValue().intValue(), rgbduration.getSelectedMaxValue().intValue(),
                rgbSubscriber.getSelectedMinValue().intValue(), rgbSubscriber.getSelectedMaxValue().intValue(),
                getFiltersCountries());
    }
}