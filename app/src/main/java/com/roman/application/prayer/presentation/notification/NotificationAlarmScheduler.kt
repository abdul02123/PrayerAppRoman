package com.roman.application.prayer.presentation.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import com.roman.application.base.BaseApplication
import com.roman.application.home.domain.model.response.prayer.Prayers
import com.roman.application.util.toMillisecondsFromDate
import java.util.Calendar

object NotificationAlarmScheduler {

    private val alarmManager = BaseApplication.getInstance().getSystemService(AlarmManager::class.java)

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(item: Prayers) {
        var timeInMillis = item.namazTime.toMillisecondsFromDate("EE MMM dd HH:mm:ss 'GMT'Z yyyy")
        val currentTimeInMillis = System.currentTimeMillis()
        if (timeInMillis < currentTimeInMillis) {
            val calendar = Calendar.getInstance().apply {
//                timeInMillis = getMillisecondsFromDate(dateString, dateFormat)
                add(Calendar.DAY_OF_MONTH, 1) // Add one day
            }
            timeInMillis = calendar.timeInMillis
        }
        try {
            val intent = Intent(BaseApplication.getInstance(), NotificationReceiver::class.java).apply {
                putExtra("prayer_name", item.namazName)
            }
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                AlarmManager.INTERVAL_DAY,
                PendingIntent.getBroadcast(
                    BaseApplication.getInstance(),
                    item.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )

        } catch (e: Exception) {
            Log.e("AndroidAlarmScheduler", "schedule: ${e.message}")
        }
    }

     fun cancelNotification(item: Prayers) {
        try {
            val pendingIntent = PendingIntent.getBroadcast(
                BaseApplication.getInstance(),
                item.hashCode(),
                Intent(BaseApplication.getInstance(), NotificationReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
            }
        } catch (e: Exception) {
            Log.e("AndroidAlarmScheduler", "cancel: ${e.message}")
        }
    }
}