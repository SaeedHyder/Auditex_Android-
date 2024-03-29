package com.ingic.auditix.ui.binders.books;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2/1/2018.
 */

public class AllBooksBinder extends RecyclerViewBinder<BookDetailEnt> implements View.OnClickListener {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private RecyclerViewItemListener listener;

    public AllBooksBinder(DisplayImageOptions options, RecyclerViewItemListener listener) {
        super(R.layout.row_item_podcast_new_noteworthy);
        this.options = options;
        this.listener = listener;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(BookDetailEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        imageLoader.displayImage(entity.getImageUrl(), holder.txtItemImage, options);
        holder.txtItemText.setText(entity.getBookName() + "");
        holder.btnSubscribe.setVisibility(View.GONE);
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
        @BindView(R.id.btn_subscribe)
        Button btnSubscribe;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
