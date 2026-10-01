package com.focusforge.app.core.maintenance

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.focusforge.app.data.storage.AppStorage

class MaintenanceWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val storage = AppStorage(applicationContext)
        if (storage.availableBytes() < 100L * 1024L * 1024L) {
            storage.deleteCache()
        }
        return Result.success()
    }
}
