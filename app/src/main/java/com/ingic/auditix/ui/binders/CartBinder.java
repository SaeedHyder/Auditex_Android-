package com.ingic.auditix.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.entities.CartEnt;
import com.ingic.auditix.interfaces.CartDeleteButtonListener;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 12/21/2017.
 */

public class CartBinder extends RecyclerViewBinder<BookDetailEnt> {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private RecyclerViewItemListener listener;
    public CartBinder(DisplayImageOptions options,RecyclerViewItemListener listener) {
        super(R.layout.row_item_cart);
        this.listener = listener;
        this.options = options;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final BookDetailEnt entity, final int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        imageLoader.displayImage(entity.getImageUrl(), holder.imgItemPic, options);
        holder.txtTitle.setText(entity.getBookName() + "");
        holder.txtAuthorText.setText(entity.getAuthorName() + "");
        holder.txtNarratorText.setText(entity.getNarratorName() + "");
        holder.txtGenreText.setText(entity.getGenre() + "");
        holder.txtPrice.setText(entity.getPrice()+"");
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRecyclerItemButtonClicked(entity, position);
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.img_item_pic)
        ImageView imgItemPic;
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.btn_delete)
        ImageView btnDelete;
        @BindView(R.id.txt_narrator_text)
        AnyTextView txtNarratorText;
        @BindView(R.id.txt_author_text)
        AnyTextView txtAuthorText;
        @BindView(R.id.txt_genre_text)
        AnyTextView txtGenreText;
        @BindView(R.id.txt_price)
        AnyTextView txtPrice;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
