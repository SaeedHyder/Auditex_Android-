package com.ingic.auditix.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.SubscribePodcastEnt;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 12/27/2017.
 */

public class MySubscriptionsBinder extends RecyclerViewBinder<SubscribePodcastEnt> implements View.OnClickListener {
    private ImageLoader imageLoader;
    private DisplayImageOptions imageoptions;
    private RecyclerViewItemListener listener;

    public MySubscriptionsBinder(DisplayImageOptions options, RecyclerViewItemListener listener) {
        super(R.layout.row_item_subscription);
        this.listener = listener;
        imageLoader = ImageLoader.getInstance();
        imageoptions = options;

    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(SubscribePodcastEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        imageLoader.displayImage(entity.getImageUrl(), holder.imgItemPic, imageoptions);
        holder.txtTitle.setText(entity.getTitle() + "");
        holder.txtNarratorText.setText(entity.getArtistName() + "");
        holder.btnSubscribe.setTag(R.integer.key_recycler_object, entity);
        holder.btnSubscribe.setTag(R.integer.key_recycler_position, position);
        holder.btnSubscribe.setOnClickListener(this);
        holder.itemView.setTag(R.integer.key_recycler_object, entity);
        holder.itemView.setTag(R.integer.key_recycler_position, position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_subscribe:
                if (listener != null) {
                    listener.onRecyclerItemButtonClicked(v.getTag(R.integer.key_recycler_object),
                            (int) v.getTag(R.integer.key_recycler_position));
                }
                break;
            default:
                if (listener != null) {
                    listener.onRecyclerItemClicked(v.getTag(R.integer.key_recycler_object),
                            (int) v.getTag(R.integer.key_recycler_position));
                }
                break;
        }

    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.img_item_pic)
        ImageView imgItemPic;
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.txt_narrator_text)
        AnyTextView txtNarratorText;
        @BindView(R.id.btn_subscribe)
        Button btnSubscribe;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
