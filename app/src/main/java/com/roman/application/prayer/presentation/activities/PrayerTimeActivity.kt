package com.roman.application.prayer.presentation.activities

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.health.connect.datatypes.units.Length
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.databinding.ActivityPrayerTimeBinding
import com.roman.application.home.domain.model.response.prayer.Prayers
import com.roman.application.prayer.presentation.adapter.PrayerTimeAdapter
import com.roman.application.util.storage.MySharePreference.getSavedData
import com.roman.application.util.storage.MySharePreference.saveData
import java.util.Calendar
import java.util.Date

class PrayerTimeActivity : BaseCompatVBActivity<ActivityPrayerTimeBinding>() {

    private var prayerTime: ArrayList<Prayers>?= null
    private val channelID = "channel1"
    private val notificationID = 100

    override fun setUpViewBinding(layoutInflater: LayoutInflater): ActivityPrayerTimeBinding {
        return ActivityPrayerTimeBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init() {
        mBinding?.appCompatImageView2?.setOnClickListener {
            finish()
        }

        if (intent.getSerializableExtra("prayerTimes")!= null){
//            prayerTime = intent.getSerializableExtra("prayerTimes") as ArrayList<Prayers>
            prayerTime = getSavedData()
            setAdapter(prayerTime?: ArrayList())
        }

        mBinding?.tvSave?.setOnClickListener{
            saveData(prayerTime?: ArrayList())
            Toast.makeText(applicationContext, "saved",Toast.LENGTH_SHORT).show()
            finish()
        }
        createNotificationChannel()
    }

    private fun setAdapter(list: ArrayList<Prayers>){
      val adapter = PrayerTimeAdapter(list, ::onClickAlarm)
      mBinding?.recyclerView?.adapter = adapter
    }

    private fun onClickAlarm(prayers: Prayers){
        prayerTime?.forEach {
            if (it.namazName == prayers.namazName){
                it.isAlarmOn = prayers.isAlarmOn
            }
        }
    }


    private fun scheduleNotification() {
        // Create an intent for the Notification BroadcastReceiver
        val intent = Intent(applicationContext, Notification::class.java)

        // Extract title and message from user input
//        val title = binding.title.text.toString()
//        val message = binding.message.text.toString()

        // Add title and message as extras to the intent
        intent.putExtra("titleExtra", "title")
        intent.putExtra("messageExtra", "message")

        // Create a PendingIntent for the broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Get the AlarmManager service
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Get the selected time and schedule the notification
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)

        // Show an alert dialog with information
        // about the scheduled notification
        showAlert(time, "title", "message")
    }

    private fun showAlert(time: Long, title: String, message: String) {
        // Format the time for display
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        // Create and show an alert dialog with notification details
        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: $title\nMessage: $message\nAt: ${dateFormat.format(date)} ${timeFormat.format(date)}"
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun getTime(): Long {
        // Get selected time from TimePicker and DatePicker
        val minute = 30
        val hour = 1
        val day = 22
        val month = 1
        val year = 2025

        // Create a Calendar instance and set the selected date and time
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)


        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        // Create a notification channel for devices running
        // Android Oreo (API level 26) and above
        val name = "Notify Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc

        // Get the NotificationManager service and create the channel
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun checkNotificationPermissions(context: Context): Boolean {
        // Check if notification permissions are granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val isEnabled = notificationManager.areNotificationsEnabled()

            if (!isEnabled) {
                // Open the app notification settings if notifications are not enabled
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                context.startActivity(intent)

                return false
            }
        } else {
            val areEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()

            if (!areEnabled) {
                // Open the app notification settings if notifications are not enabled
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                context.startActivity(intent)

                return false
            }
        }

        // Permissions are granted
        return true
    }
}