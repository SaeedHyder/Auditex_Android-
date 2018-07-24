package com.ingic.auditix.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.LocationEnt;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2/2/2018.
 */

public class FilterBinder extends RecyclerViewBinder<LocationEnt> {
    private ArrayList<String> filterCheckIDs;
    private boolean isAllClear = true;

    public FilterBinder() {
        super(R.layout.row_item_filter_child_check);
        filterCheckIDs = new ArrayList<>();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final LocationEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (filterCheckIDs.contains(entity.getId())) {
            holder.chkGenre.setChecked(true);
        } else {
            holder.chkGenre.setChecked(false);
        }
        holder.chkGenre.setText(entity.getCountryName() + "");
        holder.chkGenre.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!filterCheckIDs.contains(entity.getId())) {
                    isAllClear = false;
                    filterCheckIDs.add(entity.getId());
                }
            } else {
                if (filterCheckIDs.contains(entity.getId())) {
                    filterCheckIDs.remove(entity.getId());
                    if (filterCheckIDs.isEmpty()) {
                        isAllClear = true;
                    }
                }
            }
        });
    }

    public boolean isAllClear() {
        return isAllClear;
    }

    public void clearFilterIDs() {
        if (filterCheckIDs != null) {
            filterCheckIDs.clear();
            isAllClear = true;
        }
    }

    public String getFilterCheckIDs() {

        return StringUtils.join(filterCheckIDs, ',');
    }


    static class ViewHolder extends RecyclerViewBinder.BaseViewHolder {
        @BindView(R.id.chk_genre)
        CheckBox chkGenre;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

