package com.ingic.auditix.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ingic.auditix.interfaces.LoadMoreListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;

import java.util.List;

/**
 * Created on 8/10/2017.
 */

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewBinder.BaseViewHolder> {
    private List<T> collections;
    private RecyclerViewBinder<T> viewBinder;
    private Context mContext;
    private LoadMoreListener onLoadMoreListener;

    public RecyclerViewAdapter(List<T> collections, RecyclerViewBinder<T> viewBinder, Context context) {
        this.collections = collections;
        this.viewBinder = viewBinder;
        this.mContext = context;

    }

    public void setOnLoadMoreListener(LoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public RecyclerViewBinder.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return (RecyclerViewBinder.BaseViewHolder) this.viewBinder.createViewHolder(this.viewBinder.createView(this.mContext));
    }

    @Override
    public void onBindViewHolder(RecyclerViewBinder.BaseViewHolder holder, int position) {
        T entity = (T) this.collections.get(position);
        this.viewBinder.bindView(entity, position, holder, this.mContext);
        if (position == collections.size() - 1) {
            if (onLoadMoreListener != null) {
                onLoadMoreListener.onLoadMoreItem(position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.collections.size();
    }

    public T getItemFromList(int index) {
        return collections.get(index);
    }

    public List<T> getList() {
        return collections;
    }

    /**
     * Clears the internal list
     */
    public void clearList() {
        collections.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds a entity to the list and calls {@link #notifyDataSetChanged()}.
     * Should not be used if lots of NotificationDummy are added.
     *
     * @see #addAll(List)
     */
    public void add(T entity) {
        collections.add(entity);
        notifyDataSetChanged();
    }

    /**
     * Adds a NotificationDummy to the list and calls
     * {@link #notifyDataSetChanged()}. Can be used {
     * {@link List#subList(int, int)}.
     *
     * @see #addAll(List)
     */
    public void addAll(List<T> entityList) {
        collections.addAll(entityList);
        notifyDataSetChanged();
    }
}
