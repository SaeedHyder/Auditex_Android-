package com.ingic.auditix.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastCategoriesEnt;
import com.ingic.auditix.ui.viewbinders.abstracts.ExpandableListViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.sRangeSeekBar;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 12/28/2017.
 */

public class FilterExpandableBinder extends ExpandableListViewBinder<String, PodcastCategoriesEnt> {
    private ArrayList<Integer> filterCheckIDs;

    public FilterExpandableBinder() {
        super(R.layout.row_item_filter_group, R.layout.row_item_filter_child_check);
        filterCheckIDs = new ArrayList<>();
    }

    @Override
    public BaseGroupViewHolder createGroupViewHolder(View view) {
        return new ViewHolderGroup(view);
    }

    @Override
    public BaseChildViewHolder createChildViewHolder(View view, int groupPosition) {
      /*  String type = (String) view.getTag(R.integer.key_view_type);
        if (type.equals(AppConstants.FILTER_VIEW_1)) {
            return new ViewHolderChild1(view);
        } else {*/
        return new ViewHolderChild2(view);
        // }

    }

    @Override
    public void bindGroupView(String entity, int position, boolean isExpanded, View view, Activity activity) {
        ViewHolderGroup holderGroup = (ViewHolderGroup) view.getTag();
        if (isExpanded) {
            holderGroup.imgGroupArrow.setImageResource(R.drawable.down_arrow);
        } else {
            holderGroup.imgGroupArrow.setImageResource(R.drawable.right_arrow);
        }
        holderGroup.txtTitle.setText(entity + "");
    }

    @Override
    public void bindChildView(final PodcastCategoriesEnt entity, int position, int grpPosition, View view, final Activity activity) {
       /* String type = (String) view.getTag(R.integer.key_view_type);
        if (type.equals(AppConstants.FILTER_VIEW_1)) {
            final ViewHolderChild1 holderChild1 = (ViewHolderChild1) view.getTag();

            holderChild1.hourRangeBar.setOnRangeSeekBarChangeListener(new sRangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(sRangeSeekBar bar, Number minValue, Number maxValue) {
                    int minHour = minValue.intValue() / 60;
                    int minMinute = minValue.intValue() % 60;
                    int maxHour = maxValue.intValue() / 60;
                    int maxMinute = maxValue.intValue() % 60;
                    holderChild1.txtMinTime.setText(String.format("%02d:%02d%s", minHour, minMinute, activity.getString(R.string.min)));
                    holderChild1.txtMaxTime.setText(String.format("%02d:%02d%s", maxHour, maxMinute, activity.getString(R.string.hrs)));

                }
            });
            holderChild1.hourRangeBar.resetSelectedValues();
            holderChild1.txtMinTime.setText(String.format("%02d:%02d%s", 0, 0, activity.getString(R.string.min)));
            holderChild1.txtMaxTime.setText(String.format("%02d:%02d%s", 0, 0, activity.getString(R.string.hrs)));
        } else {*/
        ViewHolderChild2 holderChild2 = (ViewHolderChild2) view.getTag();
        holderChild2.chkGenre.setChecked(false);
        holderChild2.chkGenre.setText(entity.getTitle() + "");
        holderChild2.chkGenre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!filterCheckIDs.contains(entity.getCategoryId())) {
                        filterCheckIDs.add(entity.getCategoryId());
                    }
                } else {
                    if (filterCheckIDs.contains(entity.getCategoryId())) {
                        filterCheckIDs.remove(entity.getCategoryId());
                    }
                }
            }
        });
        // }
    }

    public String getFilterCheckIDs() {

        return StringUtils.join(filterCheckIDs, ',');
    }

    static class ViewHolderGroup extends BaseGroupViewHolder {
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.img_group_arrow)
        ImageView imgGroupArrow;

        ViewHolderGroup(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderChild1 extends BaseChildViewHolder {
        @BindView(R.id.hourRangeBar)
        sRangeSeekBar hourRangeBar;
        @BindView(R.id.txt_min_time)
        AnyTextView txtMinTime;
        @BindView(R.id.txt_max_time)
        AnyTextView txtMaxTime;

        ViewHolderChild1(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderChild2 extends BaseChildViewHolder {
        @BindView(R.id.chk_genre)
        CheckBox chkGenre;

        ViewHolderChild2(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
