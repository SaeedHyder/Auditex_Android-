package com.ingic.auditix.ui.binders;

import android.content.Context;
import android.view.View;
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
 * Created on 2/6/2018.
 */

public class LibraryBooksBinder extends RecyclerViewBinder<BookDetailEnt> implements View.OnClickListener{
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
    public void onClick(View view) {
        if (listener != null) {
            listener.onRecyclerItemClicked(view.getTag(R.integer.key_recycler_object),
                    (int) view.getTag(R.integer.key_recycler_position));
        }
    }

    @Override
    public void bindView(BookDetailEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        imageLoader.displayImage(entity.getImageUrl(), holder.imgItemPic, options);
        holder.txtTitle.setText(entity.getBookName() + "");
        holder.txtAuthorText.setText(entity.getAuthorName() + "");
        holder.txtNarratorText.setText(entity.getNarratorName() + "");
        holder.txtGenreText.setText(entity.getGenre() + "");
        holder.itemView.setTag(R.integer.key_recycler_object, entity);
        holder.itemView.setTag(R.integer.key_recycler_position, position);
        holder.itemView.setOnClickListener(this);
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
