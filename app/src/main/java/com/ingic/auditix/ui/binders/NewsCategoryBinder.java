package com.ingic.auditix.ui.binders;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
 * Created on 3/14/2018.
 */

public class NewsCategoryBinder extends RecyclerViewBinder<NewsCategoryEnt> implements View.OnClickListener {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private RecyclerViewItemListener listener;
    private int itemWidth = 0;

    public NewsCategoryBinder(DisplayImageOptions options, RecyclerViewItemListener listener) {
        super(R.layout.row_item_new_home);
        this.imageLoader = ImageLoader.getInstance();
        this.options = options;
        this.listener = listener;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(NewsCategoryEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (itemWidth == 0) {
            DisplayMetrics matrics = context.getResources().getDisplayMetrics();
            itemWidth = (matrics.widthPixels / 2) - (Math.round(context.getResources().getDimension(R.dimen.x10)) * 2);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(holder.txtItemImage.getLayoutParams());
        layoutParams.height = itemWidth;
        holder.txtItemImage.setLayoutParams(layoutParams);
        imageLoader.displayImage(entity.getSourceImageUrl(), holder.txtItemImage, options);
        holder.txtItemText.setText(entity.getSourceName() + "");
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
