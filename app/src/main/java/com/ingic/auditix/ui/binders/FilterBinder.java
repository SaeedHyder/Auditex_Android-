package com.ingic.auditix.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.FilterEnt;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2/2/2018.
 */

public class FilterBinder extends RecyclerViewBinder<FilterEnt> {
    private ArrayList<String> filterCheckIDs;

    public FilterBinder() {
        super(R.layout.row_item_filter_child_check);
        filterCheckIDs = new ArrayList<>();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final FilterEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (filterCheckIDs.contains(entity.getGenreID())) {
            holder.chkGenre.setChecked(true);
        } else {
            holder.chkGenre.setChecked(false);
        }
        holder.chkGenre.setText(entity.getGenreName() + "");
        holder.chkGenre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!filterCheckIDs.contains(entity.getGenreID())) {
                        filterCheckIDs.add(entity.getGenreID());
                    }
                } else {
                    if (filterCheckIDs.contains(entity.getGenreID())) {
                        filterCheckIDs.remove(entity.getGenreID());
                    }
                }
            }
        });
    }

    public void clearFilterIDs() {
        if (filterCheckIDs != null) {
            filterCheckIDs.clear();
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

