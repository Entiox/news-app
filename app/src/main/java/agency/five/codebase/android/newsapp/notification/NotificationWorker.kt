package agency.five.codebase.android.newsapp.notification

import agency.five.codebase.android.newsapp.MainActivity
import agency.five.codebase.android.newsapp.R
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NotificationWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        if (hasPermissionForNotifications.value) {
            createNotification()
        }
        return Result.success()
    }

    private fun createNotification() {
        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                appContext,
                0,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
            )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat.from(appContext).notify(
                1, NotificationCompat.Builder(appContext, "NEWS_01")
                    .setSmallIcon(R.drawable.ic_newspaper)
                    .setContentTitle("Check out news")
                    .setContentText("Enter NewsApp to keep up with variety of news.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .build()
            )
        } else {
            NotificationManagerCompat.from(appContext).notify(
                1, NotificationCompat.Builder(appContext)
                    .setSmallIcon(R.drawable.ic_newspaper)
                    .setContentTitle("Check out news")
                    .setContentText("Enter NewsApp to keep up with variety of news.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .build()
            )
        }
    }
}
