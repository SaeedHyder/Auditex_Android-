package com.ingic.auditix.ui.binders.podcast;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastEpisodeEnt;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 7/12/18.
 */
public class NewPodcastEpisodeBinder extends RecyclerViewBinder<PodcastEpisodeEnt> {
    private RecyclerViewItemListener listener;

    public NewPodcastEpisodeBinder(RecyclerViewItemListener listener) {
        super(R.layout.row_item_podcast_episodes);
        this.listener = listener;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final PodcastEpisodeEnt entity, final int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.txtTitle.setText(entity.getEpisodeTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRecyclerItemClicked(entity, position);
                }
            }
        });
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
