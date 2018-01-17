package com.ingic.auditix.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NavigationEnt;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 12/15/2017.
 */

public class NavigationBinder extends ViewBinder<NavigationEnt> {
    private BasePreferenceHelper preferenceHelper;

    public NavigationBinder(BasePreferenceHelper prefHelper) {
        super(R.layout.row_item_nav);
        preferenceHelper = prefHelper;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(NavigationEnt entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.imgSelected.setImageResource(entity.getResId());
        holder.txtHome.setText(entity.getTitle());
        if (preferenceHelper.getNotificationCount() <= 0) {
            holder.badge.setVisibility(View.GONE);
        } else {
            holder.badge.setVisibility(View.VISIBLE);
            holder.txtBadge.setText(preferenceHelper.getNotificationCount() + "");
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.img_selected)
        ImageView imgSelected;
        @BindView(R.id.txt_home)
        AnyTextView txtHome;
        @BindView(R.id.txtBadge)
        AnyTextView txtBadge;
        @BindView(R.id.badge)
        RelativeLayout badge;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
