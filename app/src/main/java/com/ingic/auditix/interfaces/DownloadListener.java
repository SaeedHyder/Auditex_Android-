package com.ingic.auditix.interfaces;

import com.ingic.auditix.R;
import com.ingic.auditix.activities.DockActivity;
import com.ingic.auditix.entities.BookDetailEnt;
import com.ingic.auditix.entities.BooksChapterItemEnt;
import com.ingic.auditix.entities.DownloadItemModel;
import com.ingic.auditix.entities.PodcastDetailEnt;
import com.ingic.auditix.entities.PodcastTrackEnt;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.helpers.DateHelper;
import com.ingic.auditix.helpers.UIHelper;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;

import java.util.Objects;

import io.realm.Realm;

/**
 * Created on 2/12/2018.
 */

public class DownloadListener extends FileDownloadListener {
    private DownloadListenerFragment listenerFragment;

    private Realm realm;
    private DockActivity context;

    public DownloadListener(Realm realm, DockActivity context) {
        this.realm = realm;
        this.context = context;
    }

    public Realm getRealm() {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    public void setListenerFragment(DownloadListenerFragment listenerFragment) {
        this.listenerFragment = listenerFragment;
    }

    private void addTaskToRealm(final BaseDownloadTask task, final int state) {
        if (!getRealm().isInTransaction()) {
            DownloadItemModel object = getRealm()
                    .where(DownloadItemModel.class)
                    .equalTo("downloadTag", (String) task.getTag()).findFirst();
            getRealm().beginTransaction();
            if (object == null) {
                object = getRealm().createObject(DownloadItemModel.class);
            }

            object.setDownloadID(task.getId());
            object.setDownloadTag((String) task.getTag());
            object.setDownloadedBytes(task.getSmallFileSoFarBytes());
            object.setTotalBytes(task.getSmallFileTotalBytes());
            object.setDownloadState(state);
            object.setDownloadProgress(getProgressPercentage(task.getSmallFileSoFarBytes(), task.getSmallFileTotalBytes()));
//                    realm.insertOrUpdate(object);
            getRealm().commitTransaction();
        }

    }

    private void removeTaskToRealm(BaseDownloadTask task) {
        if (!getRealm().isInTransaction()) {
            DownloadItemModel object = getRealm()
                    .where(DownloadItemModel.class)
                    .equalTo("downloadTag", (String) task.getTag()).findFirst();
            if (object == null) {
                return;
            }
            getRealm().beginTransaction();
            object.deleteFromRealm();
            getRealm().commitTransaction();
        }

    }

    @Override
    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        if (task.getListener() != this) {
            return;
        }
        if (listenerFragment != null) {
            addTaskToRealm(task, AppConstants.DownloadStates.PENDING);
            listenerFragment.pending(task, soFarBytes, totalBytes);
        }
    }

    @Override
    protected void started(BaseDownloadTask task) {
        super.started(task);
        if (task.getListener() != this) {
            return;
        }
        if (listenerFragment != null) {
            addTaskToRealm(task, AppConstants.DownloadStates.STARTED);
            listenerFragment.started(task);
        }
    }

    @Override
    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
        if (task.getListener() != this) {
            return;
        }
        if (listenerFragment != null) {
            addTaskToRealm(task, AppConstants.DownloadStates.STARTED);
            listenerFragment.connected(task, etag, isContinue, soFarBytes, totalBytes);
        }
    }

    @Override
    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        if (task.getListener() != this) {
            return;
        }
        if (listenerFragment != null) {
            addTaskToRealm(task, AppConstants.DownloadStates.DOWNLOADING);
            listenerFragment.progress(task, getProgressPercentage(soFarBytes, totalBytes));
        }
    }

    @Override
    protected void completed(BaseDownloadTask task) {
        if (task.getListener() != this) {
            return;
        }
        if (checkIsPodcast(task.getTag(R.integer.key_item_name))) {
            addPodcastToRealm((PodcastDetailEnt) task.getTag(R.integer.key_item_name), task);
        }
        if (checkIsBook(task.getTag(R.integer.key_item_name))) {
            addBookToRealm((BookDetailEnt) task.getTag(R.integer.key_item_name), task);
        }
        if (listenerFragment != null) {
            removeTaskToRealm(task);
            listenerFragment.completed(task);
        }
    }

    @Override
    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        if (task.getListener() != this) {
            return;
        }
        if (listenerFragment != null) {
            addTaskToRealm(task, AppConstants.DownloadStates.PAUSED);
        }
    }

    @Override
    protected void error(BaseDownloadTask task, Throwable e) {
        if (task.getListener() != this) {
            return;
        }
        if (listenerFragment != null) {
            removeTaskToRealm(task);
            e.fillInStackTrace();
            listenerFragment.error(task, e);
            UIHelper.showShortToastInCenter(context, String.format("%s %s", context.getResources().getString(R.string.download_error), task.getTag(R.integer.key_Download_failed)));
        }
    }

    @Override
    protected void warn(BaseDownloadTask task) {
        if (task.getListener() != this) {
            return;
        }
        if (listenerFragment != null) {
            listenerFragment.warn(task);
        }
    }

    private void addPodcastToRealm(PodcastDetailEnt object, BaseDownloadTask task) {
        PodcastTrackEnt patsyObject = new PodcastTrackEnt();
        getRealm().beginTransaction();
        object.setDownloadedOn(DateHelper.getFormatedTodayDate());
        if (object.getTrackList() != null) {
            patsyObject.setId((String) task.getTag());
            if (object.getTrackList().contains(patsyObject)) {
                int index = object.getTrackList().indexOf(patsyObject);
                object.getTrackList().get(index).setStatusState(AppConstants.DownloadStates.COMPLETE);
            }
        }
        getRealm().copyToRealmOrUpdate(object);
        getRealm().commitTransaction();
    }

    private void addBookToRealm(BookDetailEnt object, BaseDownloadTask task) {
        BooksChapterItemEnt patsyObject = new BooksChapterItemEnt();
        getRealm().beginTransaction();
        object.setDownloadedOn(DateHelper.getFormatedTodayDate());
        if (object.getChapters().getChapter() != null) {
            patsyObject.setChapterID((String) task.getTag());
            if (object.getChapters().getChapter().contains(patsyObject)) {
                int index = object.getChapters().getChapter().indexOf(patsyObject);
                Objects.requireNonNull(object.getChapters().getChapter().get(index)).setStatusState(AppConstants.DownloadStates.COMPLETE);
            }
        }
        getRealm().copyToRealmOrUpdate(object);
        getRealm().commitTransaction();
    }

    private boolean checkIsPodcast(Object object) {
        return object instanceof PodcastDetailEnt;
    }

    private boolean checkIsBook(Object object) {
        return object instanceof BookDetailEnt;
    }

    private int getProgressPercentage(int soFarBytes, int totalBytes) {
        return Math.round(((soFarBytes * 100.0f) / totalBytes));
    }

}
