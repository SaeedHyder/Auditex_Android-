package com.ingic.auditix.interfaces;

import com.liulishuo.filedownloader.BaseDownloadTask;


/**
 * Created on 2/12/2018.
 */

public interface DownloadListenerFragment {

    /**
     * Enqueue, and pending, waiting for {@link #started(BaseDownloadTask)}.
     *
     * @param task       The task
     * @param soFarBytes Already downloaded and reusable bytes stored in the db
     * @param totalBytes Total bytes stored in the db
     */
      void pending(final BaseDownloadTask task, final int soFarBytes,
                                    final int totalBytes);

    /**
     * Finish pending, and start the download runnable.
     *
     * @param task Current task.
     */
     void started(final BaseDownloadTask task) ;


    /**
     * Already connected to the server, and received the Http-response.
     *
     * @param task       The task
     * @param etag       ETag
     * @param isContinue Is resume from breakpoint
     * @param soFarBytes Number of bytes download so far
     * @param totalBytes Total size of the download in bytes
     */
     void connected(final BaseDownloadTask task, final String etag,
                             final boolean isContinue, final int soFarBytes, final int totalBytes) ;



    /**
     * Fetching datum from network and Writing to the local disk.
     *
     * @param task       The task
     * @param progress Percentage of download so far

     */
      void progress(final BaseDownloadTask task, int progress);



    // ======================= The task is over, if execute below methods =======================

    /**
     * Achieve complete ceremony.
     * <p/>
     * Complete downloading.
     *
     * @param task The task
     */
      void completed(final BaseDownloadTask task);


    /**
     * Occur a exception, but don't has any chance to retry.
     *
     * @param task The task
     * @param e    Any throwable on download pipeline
     * @see com.liulishuo.filedownloader.exception.FileDownloadHttpException
     * @see com.liulishuo.filedownloader.exception.FileDownloadGiveUpRetryException
     * @see com.liulishuo.filedownloader.exception.FileDownloadOutOfSpaceException
     */
      void error(final BaseDownloadTask task, final Throwable e);

    /**
     * There has already had some same Tasks(Same-URL & Same-SavePath) in Pending-Queue or is
     * running.
     *
     * @param task The task
     */
      void warn(final BaseDownloadTask task);
}
