package vault.gallery.loc.ui.spl

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class MyWorkManager(context: Context, worker: WorkerParameters) : Worker(context, worker) {

    override fun doWork(): Result {
        val inAD = mainCheck()
        Log.e("Loading", "Work Success?")
        val output: Data = workDataOf(KEY_RESULT to inAD)

        return Result.success(output)
        return Result.success()
    }

    fun mainCheck(): Boolean {
        fun isNotADB(): Boolean =
            Settings.Global.getString(
                applicationContext.contentResolver,
                Settings.Global.ADB_ENABLED
            ) != "1"
        return isNotADB()
    }

    companion object {
        const val KEY_RESULT = "KEY_RESULT"
    }

}