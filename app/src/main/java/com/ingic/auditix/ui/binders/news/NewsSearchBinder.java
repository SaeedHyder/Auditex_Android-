package com.ingic.auditix.ui.binders.news;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NewsCategoryEnt;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 7/11/18.
 */
public class NewsSearchBinder extends RecyclerViewBinder<NewsCategoryEnt> implements View.OnClickListener {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private RecyclerViewItemListener listener;

    public NewsSearchBinder(DisplayImageOptions options, RecyclerViewItemListener listener) {
        super(R.layout.row_item_search);
        this.options = options;
        this.listener = listener;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(NewsCategoryEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        imageLoader.displayImage(entity.getSourceImageUrl(), holder.imgItemPic, options);
        holder.txtTitle.setText(entity.getSourceName() + "");
       /* holder.txtNarratorText.setText(entity.getNarratorName() + "");
        if (entity.getNarratorName() == null) {*/
            holder.txtNarrator.setVisibility(View.INVISIBLE);
            holder.txtNarratorText.setVisibility(View.INVISIBLE);
       /* } else {
            holder.txtNarrator.setVisibility(View.VISIBLE);
            holder.txtNarratorText.setVisibility(View.VISIBLE);
        }*/
        holder.itemView.setTag(R.integer.key_recycler_object, entity);
        holder.itemView.setTag(R.integer.key_recycler_position, position);
        holder.itemView.setOnClickListener(this);
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
        @BindView(R.id.txt_narrator)
        AnyTextView txtNarrator;
        @BindView(R.id.txt_narrator_text)
        AnyTextView txtNarratorText;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
