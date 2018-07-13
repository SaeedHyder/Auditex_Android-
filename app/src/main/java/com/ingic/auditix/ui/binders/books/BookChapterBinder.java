package com.ingic.auditix.ui.binders.books;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.BooksChapterItemEnt;
import com.ingic.auditix.entities.DownloadItemModel;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.global.AppConstants.DownloadStates;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created on 2/9/2018.
 */

public class BookChapterBinder extends RecyclerViewBinder<BooksChapterItemEnt> implements View.OnClickListener {
    private RecyclerViewItemListener listener;
    private Realm realm;
    private String parentFolderName = "";

    public BookChapterBinder(RecyclerViewItemListener listener, Realm realm, String parentFolderName) {
        super(R.layout.row_item_chapters_download);
        this.listener = listener;
        this.realm = realm;
        this.parentFolderName = parentFolderName;
    }

    public Realm getRealm() {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(BooksChapterItemEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.txtName.setText(context.getResources().getString(R.string.chapters) + " " + (position + 1));
        holder.btnDownload.setTag(R.integer.key_recycler_object, entity);
        holder.btnDownload.setTag(R.integer.key_recycler_position, position);
        holder.btnDownload.setOnClickListener(this);
        holder.btnPlay.setTag(R.integer.key_recycler_object, entity);
        holder.btnPlay.setTag(R.integer.key_recycler_position, position);
        holder.btnPlay.setOnClickListener(this);
        holder.itemView.setTag(R.integer.key_recycler_object, entity);
        holder.itemView.setTag(R.integer.key_recycler_position, position);
        holder.itemView.setOnClickListener(this);

       /* holder.btnDownload.setVisibility(View.VISIBLE);
        holder.btnPlay.setVisibility(View.GONE);
        holder.downloadProgress.setVisibility(View.GONE);*/
        ;
        if (isAlreadyDownloaded(entity.getAudioUrl())) {
            holder.btnDownload.setVisibility(View.GONE);
            holder.downloadProgress.setVisibility(View.GONE);
            holder.btnPlay.setVisibility(View.VISIBLE);
        } else if (entity.getStatusState() != AppConstants.DownloadStates.DOWNLOADING) {
            DownloadItemModel downloadItem = getObjectfromRealm(entity.getChapterID());
            if (downloadItem != null) {
                entity.setStatusState(downloadItem.getDownloadState());
                entity.setDownloadProgress(downloadItem.getDownloadProgress());
            }
        }
        int status = entity.getStatusState();
        switch (status) {
            case DownloadStates.ERROR:
                holder.btnDownload.setVisibility(View.VISIBLE);
                holder.btnPlay.setVisibility(View.GONE);
                holder.downloadProgress.setVisibility(View.GONE);
                break;
            case DownloadStates.STARTED:
              /*  holder.btnDownload.setVisibility(View.GONE);
                holder.btnPlay.setVisibility(View.GONE);
                holder.downloadProgress.setVisibility(View.VISIBLE);*/
                break;
            case DownloadStates.DOWNLOADING:
                holder.btnDownload.setVisibility(View.GONE);
                holder.btnPlay.setVisibility(View.GONE);
                holder.downloadProgress.setVisibility(View.VISIBLE);
                // entity.setDownloadProgress(entity.getDownloadProgress() + 1);
                holder.downloadProgress.setProgress(entity.getDownloadProgress());
                break;
            case DownloadStates.PENDING:
                holder.btnDownload.setVisibility(View.GONE);
                holder.btnPlay.setVisibility(View.GONE);
                holder.downloadProgress.setVisibility(View.VISIBLE);
                break;
            case DownloadStates.COMPLETE:
                holder.btnDownload.setVisibility(View.GONE);
                holder.downloadProgress.setVisibility(View.GONE);
                holder.btnPlay.setVisibility(View.VISIBLE);
                break;
            default:

                break;
        }


    }

    private DownloadItemModel getObjectfromRealm(String chapterID) {
        return getRealm()
                .where(DownloadItemModel.class)
                .equalTo("downloadTag", chapterID).findFirst();
    }

    private boolean isAlreadyDownloaded(String audioUrl) {
        return new File(AppConstants.DOWNLOAD_PATH + File.separator + parentFolderName + File.separator + audioUrl.
                replaceAll("\\s+", "")
                .replaceAll("\\\\", "")
                .replaceAll("/", "")).exists();
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
            case R.id.btn_play:
                if (listener != null) {
                    listener.onRecyclerItemButtonClicked(null,
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
        AnyTextView txtName;
        @BindView(R.id.btn_download_progress)
        CircleProgressBar downloadProgress;
        @BindView(R.id.btn_download)
        ImageView btnDownload;
        @BindView(R.id.btn_play)
        ImageView btnPlay;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
