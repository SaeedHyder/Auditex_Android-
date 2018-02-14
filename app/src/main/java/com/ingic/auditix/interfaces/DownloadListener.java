package com.ingic.auditix.interfaces;

import com.ingic.auditix.R;
import com.ingic.auditix.activities.DockActivity;
import com.ingic.auditix.entities.DownloadItemModel;
import com.ingic.auditix.global.AppConstants;
import com.ingic.auditix.helpers.UIHelper;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;

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
                    .equalTo("downloadTag", (Integer) task.getTag()).findFirst();
            getRealm().beginTransaction();
            if (object == null) {
                object = getRealm().createObject(DownloadItemModel.class);
            }

            object.setDownloadID(task.getId());
            object.setDownloadTag((Integer) task.getTag());
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
                    .equalTo("downloadTag", (Integer) task.getTag()).findFirst();
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
            listenerFragment.error(task, e);
            UIHelper.showShortToastInCenter(context, String.format("%s %s", context.getResources().getString(R.string.download_error), task.getTag(context.getResources().getInteger(R.integer.key_item_name))));
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

    private int getProgressPercentage(int soFarBytes, int totalBytes) {
        return Math.round(((soFarBytes * 100.0f) / totalBytes));
    }
}
