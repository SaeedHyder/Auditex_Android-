package com.ingic.auditix.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.ingic.auditix.R;
import com.ingic.auditix.entities.CartEnt;
import com.ingic.auditix.entities.PodcastDetailEnt;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.ingic.auditix.ui.views.SwipeRevealLayout;
import com.ingic.auditix.ui.views.ViewBinderHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 1/6/2018.
 */

public class DownloadBinder extends RecyclerViewBinder<PodcastDetailEnt> implements View.OnClickListener {
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private RecyclerViewItemListener downloadListner;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public DownloadBinder(RecyclerViewItemListener downloadListner, DisplayImageOptions options) {
        super(R.layout.row_item_downloads);
        viewBinderHelper.setOpenOnlyOne(true);
        this.downloadListner = downloadListner;
        this.options = options;
    }


    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(PodcastDetailEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        viewBinderHelper.bind(holder.swipeLayout, String.valueOf(position));
        imageLoader.displayImage(entity.getImage(), holder.imgItemPic, options);
        holder.txtTitle.setText(entity.getTitle() + "");
        holder.txtDownloadOnText.setText(entity.getDownloadedOn() + "");
        holder.btnDelete.setTag(R.integer.key_recycler_object, entity);
        holder.btnDelete.setTag(R.integer.key_recycler_position, position);
        holder.btnDelete.setOnClickListener(this);
        holder.mainFrameLayout.setTag(R.integer.key_recycler_object, entity);
        holder.mainFrameLayout.setTag(R.integer.key_recycler_position, position);
        holder.mainFrameLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                if (downloadListner != null) {
                    downloadListner.onRecyclerItemButtonClicked(v.getTag(R.integer.key_recycler_object),
                            (int) v.getTag(R.integer.key_recycler_position));
                }
                break;
            default:
                if (downloadListner != null) {
                    downloadListner.onRecyclerItemClicked(v.getTag(R.integer.key_recycler_object),
                            (int) v.getTag(R.integer.key_recycler_position));
                }
                break;
        }
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
        @BindView(R.id.mainFrameLayout)
        FrameLayout mainFrameLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
