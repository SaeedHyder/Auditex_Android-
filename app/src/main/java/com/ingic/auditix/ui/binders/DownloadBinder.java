package com.ingic.auditix.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.CartEnt;
import com.ingic.auditix.entities.PodcastDetailEnt;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 1/6/2018.
 */

public class DownloadBinder extends RecyclerViewBinder<PodcastDetailEnt> {
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public DownloadBinder() {
        super(R.layout.row_item_downloads);
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(PodcastDetailEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        viewBinderHelper.bind(holder.swipeLayout, String.valueOf(position));
    }
    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.img_item_pic)
        ImageView imgItemPic;
        @BindView(R.id.btn_delete)
        ImageView btnDelete;
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.txt_narrator_text)
        AnyTextView txtNarratorText;
        @BindView(R.id.txt_duration_text)
        AnyTextView txtDurationText;
        @BindView(R.id.txt_download_on_text)
        AnyTextView txtDownloadOnText;
        @BindView(R.id.swipe_layout)
        SwipeRevealLayout swipeLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
