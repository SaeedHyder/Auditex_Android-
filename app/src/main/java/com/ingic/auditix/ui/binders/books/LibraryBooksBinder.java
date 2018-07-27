package com.ingic.auditix.ui.binders.books;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookDetailEnt;
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
 * Created on 2/6/2018.
 */

public class LibraryBooksBinder extends RecyclerViewBinder<BookDetailEnt> implements View.OnClickListener{
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private RecyclerViewItemListener listener;

    public LibraryBooksBinder(DisplayImageOptions options, RecyclerViewItemListener booksItemListener) {
        super(R.layout.row_item_book_library);
        this.options = options;
        this.listener = booksItemListener;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
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

    @Override
    public void bindView(BookDetailEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        viewBinderHelper.bind(holder.swipeLayout, String.valueOf(position));
        imageLoader.displayImage(entity.getImageUrl(), holder.imgItemPic, options);
        holder.txtTitle.setText(entity.getBookName() + "");
        holder.txtAuthorText.setText(entity.getAuthorName() + "");
        holder.txtNarratorText.setText(entity.getNarratorName() + "");
        holder.txtGenreText.setText(entity.getGenre() + "");
        holder.btnDelete.setTag(R.integer.key_recycler_object, entity);
        holder.btnDelete.setTag(R.integer.key_recycler_position, position);
        holder.btnDelete.setOnClickListener(this);
        holder.mainFrameLayout.setTag(R.integer.key_recycler_object, entity);
        holder.mainFrameLayout.setTag(R.integer.key_recycler_position, position);
        holder.mainFrameLayout.setOnClickListener(this);
    }

    static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.img_item_pic)
        ImageView imgItemPic;
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.txt_narrator_text)
        AnyTextView txtNarratorText;
        @BindView(R.id.txt_author_text)
        AnyTextView txtAuthorText;
        @BindView(R.id.txt_genre_text)
        AnyTextView txtGenreText;
        @BindView(R.id.swipe_layout)
        SwipeRevealLayout swipeLayout;
        @BindView(R.id.mainFrameLayout)
        FrameLayout mainFrameLayout;
        @BindView(R.id.btn_delete)
        ImageView btnDelete;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
