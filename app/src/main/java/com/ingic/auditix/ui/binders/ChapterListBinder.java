package com.ingic.auditix.ui.binders;

import android.content.Context;
import android.view.View;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.BooksChapterItemEnt;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2/3/2018.
 */

public class ChapterListBinder extends RecyclerViewBinder<BooksChapterItemEnt> implements View.OnClickListener {
    private RecyclerViewItemListener listener;

    public ChapterListBinder(RecyclerViewItemListener listener) {
        super(R.layout.row_item_chapter_item);
        this.listener = listener;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(BooksChapterItemEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.txtName.setText(String.format("%s %d", context.getResources().getString(R.string.chapters), position + 1));
        if (entity.isSelected()){
            holder.txtName.setTextColor(context.getResources().getColor(R.color.app_title_orange));
        }else {
            holder.txtName.setTextColor(context.getResources().getColor(R.color.app_font_black));
        }
        holder.itemView.setTag(R.integer.key_recycler_object, entity);
        holder.itemView.setTag(R.integer.key_recycler_position, position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        if (listener != null) {
            listener.onRecyclerItemClicked(v.getTag(R.integer.key_recycler_object),
                    (int) v.getTag(R.integer.key_recycler_position));
        }

    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_name)
        AnyTextView txtName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
