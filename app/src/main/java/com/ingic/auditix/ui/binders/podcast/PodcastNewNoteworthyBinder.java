package com.ingic.auditix.ui.binders.podcast;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.PodcastEpisodeEnt;
import com.ingic.auditix.helpers.BasePreferenceHelper;
import com.ingic.auditix.helpers.ServiceHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 7/11/18.
 */
public class PodcastNewNoteworthyBinder extends RecyclerViewBinder<PodcastEpisodeEnt> implements View.OnClickListener {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private RecyclerViewItemListener listener;
    private BasePreferenceHelper prefHelper;

    public PodcastNewNoteworthyBinder(DisplayImageOptions options, RecyclerViewItemListener listener, BasePreferenceHelper prefHelper) {
        super(R.layout.row_item_podcast_new_noteworthy);
        this.options = options;
        this.listener = listener;
        this.prefHelper = prefHelper;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_subscribe:
                if (prefHelper.isGuest()) {
                    UIHelper.showShortToastInCenter(v.getContext(), v.getContext().getString(R.string.guest_message));
                    return;
                }
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
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(PodcastEpisodeEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        imageLoader.displayImage(entity.getPodcast().getImageUrl(), holder.txtItemImage, options);
        holder.btnSubscribe.setVisibility(View.GONE);
        holder.txtItemText.setText(entity.getEpisodeTitle() + "");
        if (entity.getPodcast().getSubscribed()) {
            holder.btnSubscribe.setText(R.string.unsubscribe);
        } else {
            holder.btnSubscribe.setText(R.string.subscribe);
        }
        holder.btnSubscribe.setTag(R.integer.key_recycler_object, entity);
        holder.btnSubscribe.setTag(R.integer.key_recycler_position, position);
        holder.btnSubscribe.setOnClickListener(this);
        holder.itemView.setTag(R.integer.key_recycler_object, entity);
        holder.itemView.setTag(R.integer.key_recycler_position, position);
        holder.itemView.setOnClickListener(this);
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
