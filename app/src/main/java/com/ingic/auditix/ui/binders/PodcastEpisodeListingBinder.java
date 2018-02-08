package com.ingic.auditix.ui.binders;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastTrackEnt;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 1/17/2018.
 */

public class PodcastEpisodeListingBinder extends RecyclerViewBinder<PodcastTrackEnt> implements View.OnClickListener {
    private RecyclerViewItemListener listener;

    public PodcastEpisodeListingBinder(RecyclerViewItemListener listener) {
        super(R.layout.row_item_episodes_listing);
        this.listener = listener;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(PodcastTrackEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.txtname.setText(String.format("%s %d", context.getResources().getString(R.string.episode), position+1));
        holder.txtdescription.setText(entity.getName() + "");
        if (entity.isSelected()){
            holder.txtname.setTextColor(context.getResources().getColor(R.color.app_title_orange));
            holder.txtdescription.setTextColor(context.getResources().getColor(R.color.app_title_orange));
        }else {
            holder.txtname.setTextColor(context.getResources().getColor(R.color.app_font_black));
            holder.txtdescription.setTextColor(context.getResources().getColor(R.color.app_font_black));
        }
        holder.itemView.setTag(R.integer.key_recycler_object, entity);
        holder.itemView.setTag(R.integer.key_recycler_position, position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
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

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_name)
        AnyTextView txtname;
        @BindView(R.id.txt_description)
        AnyTextView txtdescription;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
