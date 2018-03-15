package com.ingic.auditix.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.ingic.auditix.R;
import com.ingic.auditix.entities.DownloadItemModel;
import com.ingic.auditix.entities.PodcastTrackEnt;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.interfaces.RecyclerViewItemListener;
import com.ingic.auditix.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.ingic.auditix.ui.views.AnyTextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created on 1/16/2018.
 */

public class PodcastEpisodeBinder extends RecyclerViewBinder<PodcastTrackEnt> implements View.OnClickListener {
    private RecyclerViewItemListener listener;
    private Realm realm;

    public PodcastEpisodeBinder(RecyclerViewItemListener listener, Realm realm) {
        super(R.layout.row_item_podcast_episodes_download);
        this.listener = listener;
        this.realm = realm;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final PodcastTrackEnt entity, int position, Object viewHolder, Context context) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.txtTitle.setText(entity.getName() + "");
        holder.txt_position.setText(context.getResources().getString(R.string.episode) + " " + (position+1));
        holder.btnDownload.setTag(R.integer.key_recycler_object, entity);
        holder.btnDownload.setTag(R.integer.key_recycler_position, position);
        holder.btnDownload.setOnClickListener(this);
        holder.btnPlay.setTag(R.integer.key_recycler_object, entity);
        holder.btnPlay.setTag(R.integer.key_recycler_position, position);
        holder.btnPlay.setOnClickListener(this);
        holder.itemView.setTag(R.integer.key_recycler_object, entity);
        holder.itemView.setTag(R.integer.key_recycler_position, position);
        holder.itemView.setOnClickListener(this);
        if (isAlreadyDownloaded(entity.getName())) {
            holder.btnDownload.setVisibility(View.GONE);
            holder.downloadProgress.setVisibility(View.INVISIBLE);
            holder.btnPlay.setVisibility(View.VISIBLE);
        } else if (entity.getStatusState() != AppConstants.DownloadStates.DOWNLOADING) {
            final DownloadItemModel downloadItem = getObjectfromRealm(entity.getId());
            if (downloadItem != null) {
                entity.setStatusState(downloadItem.getDownloadState());
                entity.setDownloadProgress(downloadItem.getDownloadProgress());
            }
        }
        int status = entity.getStatusState();
        switch (status) {
            case AppConstants.DownloadStates.ERROR:
                holder.btnDownload.setVisibility(View.VISIBLE);
                holder.btnPlay.setVisibility(View.GONE);
                holder.downloadProgress.setVisibility(View.INVISIBLE);
                break;
            case AppConstants.DownloadStates.STARTED:
              /*  holder.btnDownload.setVisibility(View.GONE);
                holder.btnPlay.setVisibility(View.GONE);
                holder.downloadProgress.setVisibility(View.VISIBLE);*/
                break;
            case AppConstants.DownloadStates.DOWNLOADING:
                holder.btnDownload.setVisibility(View.GONE);
                holder.btnPlay.setVisibility(View.GONE);
                holder.downloadProgress.setVisibility(View.VISIBLE);
//                entity.setDownloadProgress(entity.getDownloadProgress() + 1);
                holder.downloadProgress.setProgress(entity.getDownloadProgress());
                break;
            case AppConstants.DownloadStates.PENDING:
                holder.btnDownload.setVisibility(View.GONE);
                holder.btnPlay.setVisibility(View.GONE);
                holder.downloadProgress.setVisibility(View.VISIBLE);
                break;
            case AppConstants.DownloadStates.COMPLETE:
                holder.btnDownload.setVisibility(View.GONE);
                holder.downloadProgress.setVisibility(View.INVISIBLE);
                holder.btnPlay.setVisibility(View.VISIBLE);
                break;
            default:

                break;
        }
    }

    public Realm getRealm() {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    private DownloadItemModel getObjectfromRealm(Integer chapterID) {
        return getRealm()
                .where(DownloadItemModel.class)
                .equalTo("downloadTag", chapterID).findFirst();
    }


    private boolean isAlreadyDownloaded(String audioUrl) {
        return new File(AppConstants.DOWNLOAD_PATH + File.separator + audioUrl
                .replaceAll("\\s+", "")
                .replaceAll("\\\\", "")
                .replaceAll("/", "") + ".mp3")
                .exists();
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
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.txt_position)
        AnyTextView txt_position;
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
