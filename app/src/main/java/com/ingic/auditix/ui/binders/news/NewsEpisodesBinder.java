package com.ingic.auditix.ui.binders.news;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NewsEpisodeEnt;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 3/15/2018.
 */

public class NewsEpisodesBinder extends RecyclerViewBinder<NewsEpisodeEnt> implements View.OnClickListener {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private RecyclerViewItemListener listener;

    public NewsEpisodesBinder(DisplayImageOptions options, RecyclerViewItemListener listener) {
        super(R.layout.row_item_news_category_episodes);
        imageLoader = ImageLoader.getInstance();
        this.options = options;
        this.listener = listener;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(NewsEpisodeEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        imageLoader.displayImage(entity.getImage_url(), holder.imgItemPic, options);
        holder.txtTitle.setEllipsize(TextUtils.TruncateAt.END);
        holder.txtTitle.setMaxLines(2);
        holder.txtTitle.setText(entity.getTitle() + "");
        holder.txtDurationText.setText(getDurationText(entity.getDuration()));
        holder.itemView.setTag(R.integer.key_recycler_object, entity);
        holder.itemView.setTag(R.integer.key_recycler_position, position);
        holder.itemView.setOnClickListener(this);
    }
    public String getDurationText(Integer secound) {
        int minutes = (secound % 3600) / 60;
        int hours = secound / 3600;

        if (hours > 1) {
            return (hours) + " Hr " + (minutes) + " Mins";
        } else if (minutes > 0) {
            return (minutes) + " Mins";
        } else if (secound > 0) {
            return (secound) + " Sec";
        } else {
            return "-";
        }

    }
    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onRecyclerItemClicked(view.getTag(R.integer.key_recycler_object),
                    (int) view.getTag(R.integer.key_recycler_position));
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.img_item_pic)
        ImageView imgItemPic;
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.btn_delete)
        ImageView btnDelete;
        @BindView(R.id.txt_narrator)
        AnyTextView txtNarrator;
        @BindView(R.id.txt_narrator_text)
        AnyTextView txtNarratorText;
        @BindView(R.id.txt_duration)
        AnyTextView txtDuration;
        @BindView(R.id.txt_duration_text)
        AnyTextView txtDurationText;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
