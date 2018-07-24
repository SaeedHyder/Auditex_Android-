package com.ingic.auditix.ui.binders.news;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NewsEpisodeEnt;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 7/16/18.
 */
public class NewsEpisodeListingBinder extends RecyclerViewBinder<NewsEpisodeEnt> {
    private RecyclerViewItemListener listener;

    public NewsEpisodeListingBinder(RecyclerViewItemListener listener) {
        super(R.layout.row_item_podcast_episodes);
        this.listener = listener;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(NewsEpisodeEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.txtTitle.setText(getResString(R.string.episode_no, context) + " " + (position + 1));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecyclerItemClicked(entity, position);
            }
        });
    }

    public String getResString(int stringResouseID, Context context) {
        return context.getResources().getString(stringResouseID);
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.btnNext)
        ImageView btnNext;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
