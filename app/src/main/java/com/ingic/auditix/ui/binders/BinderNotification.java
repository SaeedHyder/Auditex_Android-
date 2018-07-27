package com.ingic.auditix.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.NotificationEnt;
import com.ingic.auditix.helpers.BasePreferenceHelper;
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
 * Created by khan_muhammad on 9/15/2017.
 */

public class BinderNotification extends RecyclerViewBinder<NotificationEnt> implements View.OnClickListener {

    private Context context;
    private BasePreferenceHelper preferenceHelper;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private DisplayImageOptions imageoptions;
    private ImageLoader imageLoader;
    private RecyclerViewItemListener listener;


    public BinderNotification(DisplayImageOptions imageoptions, RecyclerViewItemListener listener) {
        super(R.layout.fragment_notification_item);
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
    public void bindView(NotificationEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;

        viewBinderHelper.bind(holder.swipeLayout, String.valueOf(position));
        holder.btnDelete.setTag(R.integer.key_recycler_object, entity);
        holder.btnDelete.setTag(R.integer.key_recycler_position, position);
        holder.btnDelete.setOnClickListener(this);
        holder.tvMsg.setText(entity.getNotificationMessage());
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
        @BindView(R.id.tv_msg)
        AnyTextView tvMsg;
        @BindView(R.id.swipe_layout)
        SwipeRevealLayout swipeLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
