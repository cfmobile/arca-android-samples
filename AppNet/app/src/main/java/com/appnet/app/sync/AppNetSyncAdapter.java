package com.appnet.app.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.appnet.app.operations.SyncPostsOperation;
import com.appnet.app.providers.AppNetContentProvider;

import io.pivotal.arca.service.OperationService;

public class AppNetSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final long MANUAL_SYNC_INTERVAL = 5 * 1000;

    private long mLastSyncTime = 0;

    public AppNetSyncAdapter(final Context context, final boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        if (System.currentTimeMillis() > (mLastSyncTime + MANUAL_SYNC_INTERVAL)) {
            OperationService.start(getContext(), new SyncPostsOperation(AppNetContentProvider.Uris.POSTS_URI));
            mLastSyncTime = System.currentTimeMillis();
        }
    }

}
