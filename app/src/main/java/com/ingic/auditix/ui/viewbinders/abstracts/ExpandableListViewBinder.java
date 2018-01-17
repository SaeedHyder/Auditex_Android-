package com.ingic.auditix.ui.viewbinders.abstracts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ingic.auditix.R;
import com.ingic.auditix.global.AppConstants;

public abstract class ExpandableListViewBinder<T, E> {

    int mLayoutGroupId;
    int mLayoutChildId;
    int mLayoutChild2Id;

    public ExpandableListViewBinder() {

    }

    public ExpandableListViewBinder(int layoutGroupId, int layoutChildId, int layoutChild2Id) {
        mLayoutGroupId = layoutGroupId;
        mLayoutChildId = layoutChildId;
        mLayoutChild2Id = layoutChild2Id;
    }
    public ExpandableListViewBinder(int layoutGroupId, int layoutChildId) {
        mLayoutGroupId = layoutGroupId;
        mLayoutChildId = layoutChildId;
    }
    public View createGroupView(Activity activity) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(mLayoutGroupId, null);
        view.setTag(createGroupViewHolder(view));
        return view;

    }

    public View createChildView(Activity activity) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(mLayoutChildId, null);
        view.setTag(createChildViewHolder(view,0));
        return view;

    }

    public View createChildView(Activity activity, int groupPosition) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view;
        if (groupPosition == 0) {
            view = inflater.inflate(mLayoutChildId, null);
            view.setTag(R.integer.key_view_type, AppConstants.FILTER_VIEW_1);
        } else {
            view = inflater.inflate(mLayoutChild2Id, null);
            view.setTag(R.integer.key_view_type,AppConstants.FILTER_VIEW_2);
        }
        view.setTag(createChildViewHolder(view, groupPosition));
        return view;

    }

    public abstract BaseGroupViewHolder createGroupViewHolder(View view);

    public abstract BaseChildViewHolder createChildViewHolder(View view, int groupPosition);

    /**
     * @param entity
     * @param position
     * @param isExpanded In cases applicable, for e.g in expandable listview
     * @param view
     * @param activity
     */
    public abstract void bindGroupView(T entity, int position, boolean isExpanded, View view, Activity activity);

    public abstract void bindChildView(E entity, int position, int grpPosition, View view, Activity activity);

    protected static class BaseGroupViewHolder {

    }

    protected static class BaseChildViewHolder {

    }
}

