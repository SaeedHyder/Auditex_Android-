package com.ingic.auditix.ui.binders.books;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;


import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookFavoriteEnt;
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
 * Created on 1/30/2018.
 */

public class BookFavoriteBinder extends RecyclerViewBinder<BookFavoriteEnt> implements View.OnClickListener {
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private DisplayImageOptions imageoptions;
    private ImageLoader imageLoader;
    private RecyclerViewItemListener listener;

    public BookFavoriteBinder(DisplayImageOptions imageoptions, RecyclerViewItemListener listener) {
        super(R.layout.row_item_favorite_new);
        this.imageoptions = imageoptions;
        this.listener = listener;
        viewBinderHelper.setOpenOnlyOne(true);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(BookFavoriteEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        imageLoader.displayImage(entity.getImageUrl(), holder.imgItemPic, imageoptions);
        holder.txtTitle.setText(entity.getBookName() + "");
        holder.txtNarratorText.setText(entity.getNarratorName() + "");
        viewBinderHelper.bind(holder.swipeLayout, String.valueOf(position));
        holder.btnDelete.setTag(R.integer.key_recycler_object, entity);
        holder.btnDelete.setTag(R.integer.key_recycler_position, position);
        holder.btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onRecyclerItemButtonClicked(v.getTag(R.integer.key_recycler_object),
                    (int) v.getTag(R.integer.key_recycler_position));
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.btn_delete)
        ImageView btnDelete;
        @BindView(R.id.img_item_pic)
        ImageView imgItemPic;
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.txt_narrator)
        AnyTextView txtNarrator;
        @BindView(R.id.txt_narrator_text)
        AnyTextView txtNarratorText;
        @BindView(R.id.swipe_layout)
        SwipeRevealLayout swipeLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
