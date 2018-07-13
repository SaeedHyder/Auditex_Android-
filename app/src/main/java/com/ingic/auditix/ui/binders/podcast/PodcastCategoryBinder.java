package com.ingic.auditix.ui.binders.podcast;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastCategoriesEnt;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 4/11/18.
 */
public class PodcastCategoryBinder extends RecyclerViewBinder<PodcastCategoriesEnt> implements View.OnClickListener {
    private ImageLoader imageLoader;
    private RecyclerViewItemListener listener;
    private DisplayImageOptions imageoptions;
    private int itemWidth = 0;
    public PodcastCategoryBinder(DisplayImageOptions options, RecyclerViewItemListener listener) {
        super(R.layout.row_item_podcast_category);
        this.listener = listener;
        imageLoader = ImageLoader.getInstance();
        imageoptions = options;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(PodcastCategoriesEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (itemWidth == 0) {
            DisplayMetrics matrics = context.getResources().getDisplayMetrics();
            itemWidth = (matrics.widthPixels / 3) - (Math.round(context.getResources().getDimension(R.dimen.x10)) * 3);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(holder.txtItemImage.getLayoutParams());
        layoutParams.height = itemWidth;
        holder.txtItemImage.setLayoutParams(layoutParams);
        holder.itemView.setTag(R.integer.key_recycler_object, entity);
        holder.itemView.setTag(R.integer.key_recycler_position, position);
        holder.itemView.setOnClickListener(this);
        imageLoader.displayImage(entity.getImagePath(), holder.txtItemImage, imageoptions);
        holder.txtItemText.setText(entity.getTitle());
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onRecyclerItemClicked(v.getTag(R.integer.key_recycler_object),
                    (int) v.getTag(R.integer.key_recycler_position));
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_item_image)
        ImageView txtItemImage;
        @BindView(R.id.txt_item_text)
        AnyTextView txtItemText;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
