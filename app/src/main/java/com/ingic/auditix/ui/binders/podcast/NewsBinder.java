package com.ingic.auditix.ui.binders.podcast;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.News;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.interfaces.ToggleListner;
import com.ingic.auditix.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewsBinder extends ViewBinder<News> {

    private Context context;
    private ImageLoader imageLoader;
    private BasePreferenceHelper preferenceHelper;
    private DisplayImageOptions imageoptions;
    private ToggleListner toggleListner;
    private boolean isAutoDownload;

    public NewsBinder(Context context, BasePreferenceHelper prefHelper, DisplayImageOptions options, ToggleListner toggleListner, boolean isAutoDownload) {
        super(R.layout.row_item_notification_settings);
        this.context = context;
        this.preferenceHelper = prefHelper;
        imageoptions = options;
        imageLoader = ImageLoader.getInstance();
        this.toggleListner = toggleListner;
        this.isAutoDownload = isAutoDownload;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(News entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        imageLoader.displayImage(entity.getImageUrl(), viewHolder.imgItemPic, imageoptions);
        viewHolder.txtTitle.setText(entity.getName() + "");

        if (isAutoDownload) {
            viewHolder.swtContinousPlay.setChecked(entity.getAutoDownload());
        } else {
            viewHolder.swtContinousPlay.setChecked(entity.getNotificationEnabled());
        }

        viewHolder.swtContinousPlay.setOnCheckedChangeListener((compoundButton, b) -> {
            if (viewHolder.swtContinousPlay.isPressed()) {
                toggleListner.onCheckedChanged(entity, position, b, AppConstants.News);
            }
        });

    }


    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.img_item_pic)
        ImageView imgItemPic;
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.swt_continous_play)
        Switch swtContinousPlay;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
