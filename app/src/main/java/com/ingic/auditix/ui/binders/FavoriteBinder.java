package com.ingic.auditix.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.ingic.auditix.R;
import com.ingic.auditix.entities.FavoriteEnt;
import com.ingic.auditix.helpers.UIHelper;
import com.ingic.auditix.ui.viewbinders.abstracts.ExpandableListViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 1/5/2018.
 */

public class FavoriteBinder extends ExpandableListViewBinder<String, FavoriteEnt> {
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public FavoriteBinder(DisplayImageOptions options) {
        super(R.layout.row_item_favorite_group, R.layout.row_item_favorite_child);
        this.imageOptions = options;
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public BaseGroupViewHolder createGroupViewHolder(View view) {
        return new GroupViewHolder(view);
    }

    @Override
    public BaseChildViewHolder createChildViewHolder(View view, int groupPosition) {
        return new ChildViewHolder(view);
    }

    @Override
    public void bindGroupView(String entity, int position, boolean isExpanded, View view, final Activity activity) {
        GroupViewHolder holder = (GroupViewHolder) view.getTag();
        holder.txtTitle.setText(entity + "");
        holder.btnSeeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showShortToastInCenter(activity, "clicked");
            }
        });
    }

    @Override
    public void bindChildView(FavoriteEnt entity, int position, int grpPosition, View view, Activity activity) {
        ChildViewHolder holder = (ChildViewHolder) view.getTag();
        imageLoader.displayImage(entity.getAlbumImage(), holder.imgItemPic, imageOptions);
        holder.txtTitle.setText(entity.getAlbumTitle());
        holder.txtNarratorText.setText(entity.getAlbumNarrator());
    }

    static class GroupViewHolder extends BaseGroupViewHolder {
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.btn_seeall)
        AnyTextView btnSeeall;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder extends BaseChildViewHolder {
        @BindView(R.id.img_item_pic)
        ImageView imgItemPic;
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.txt_narrator_text)
        AnyTextView txtNarratorText;
        @BindView(R.id.btn_like)
        CheckBox btnLike;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
