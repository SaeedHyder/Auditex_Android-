package com.ingic.auditix.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.ingic.auditix.R;
import com.ingic.auditix.activities.DockActivity;
import com.ingic.auditix.entities.NavigationEnt;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.interfaces.LanguageInterface;
import com.ingic.auditix.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 12/15/2017.
 */

public class NavigationBinder extends ViewBinder<NavigationEnt> {
    private BasePreferenceHelper preferenceHelper;
    private DockActivity dockActivity;
    private LanguageInterface languageInterface;

    public NavigationBinder(BasePreferenceHelper prefHelper, DockActivity dockActivity, LanguageInterface languageInterface) {
        super(R.layout.row_item_nav);
        preferenceHelper = prefHelper;
        this.dockActivity = dockActivity;
        this.languageInterface = languageInterface;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(NavigationEnt entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.imgSelected.setImageResource(entity.getResId());
        if (entity.getTitle().equals(dockActivity.getResources().getString(R.string.language))) {
            holder.llSelectLanguage.setVisibility(View.VISIBLE);
            holder.llNavigation.setVisibility(View.GONE);

        } else {
            holder.llSelectLanguage.setVisibility(View.GONE);
            holder.llNavigation.setVisibility(View.VISIBLE);
            holder.txtHome.setText(entity.getTitle());
        }
        if (preferenceHelper.getNotificationCount() <= 0) {
            holder.badge.setVisibility(View.GONE);
        } else {
            holder.badge.setVisibility(View.VISIBLE);
            holder.txtBadge.setText(preferenceHelper.getNotificationCount() + "");
        }

        holder.swtContinousPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                languageInterface.changeListner(entity, position, b);
            }
        });
    }


    static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.img_selected)
        ImageView imgSelected;
        @BindView(R.id.txt_home)
        AnyTextView txtHome;
        @BindView(R.id.txtBadge)
        AnyTextView txtBadge;
        @BindView(R.id.badge)
        RelativeLayout badge;
        @BindView(R.id.ll_navigation)
        RelativeLayout llNavigation;
        @BindView(R.id.txt_arabic)
        AnyTextView txtArabic;
        @BindView(R.id.swt_continous_play)
        Switch swtContinousPlay;
        @BindView(R.id.txt_english)
        AnyTextView txtEnglish;
        @BindView(R.id.ll_select_language)
        LinearLayout llSelectLanguage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
