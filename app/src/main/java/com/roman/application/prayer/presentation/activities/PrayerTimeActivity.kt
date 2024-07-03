package com.roman.application.prayer.presentation.activities

import android.os.Build
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.databinding.ActivityPrayerTimeBinding
import com.roman.application.home.domain.model.response.prayer.Prayers
import com.roman.application.prayer.presentation.adapter.PrayerTimeAdapter
import com.roman.application.prayer.presentation.notification.NotificationAlarmScheduler.cancelNotification
import com.roman.application.prayer.presentation.notification.NotificationAlarmScheduler.scheduleNotification
import com.roman.application.util.storage.MySharePreference.getSavedData
import com.roman.application.util.storage.MySharePreference.saveData

class PrayerTimeActivity : BaseCompatVBActivity<ActivityPrayerTimeBinding>() {

    private var prayerTime: ArrayList<Prayers>?= null

    override fun setUpViewBinding(layoutInflater: LayoutInflater): ActivityPrayerTimeBinding {
        return ActivityPrayerTimeBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init() {
        mBinding?.appCompatImageView2?.setOnClickListener {
            finish()
        }

        if (intent.getSerializableExtra("prayerTimes")!= null){
            prayerTime = getSavedData()
            setAdapter(prayerTime?: ArrayList())
        }

        mBinding?.tvSave?.setOnClickListener{
            saveData(prayerTime?: ArrayList())
            saveNotification()
            Toast.makeText(applicationContext, "Saved",Toast.LENGTH_SHORT).show()
            finish()


        }
    }

    private fun setAdapter(list: ArrayList<Prayers>){
      val adapter = PrayerTimeAdapter(list, ::onClickAlarm)
      mBinding?.recyclerView?.adapter = adapter
    }

    private fun onClickAlarm(prayer: Prayers){
        prayerTime?.forEach {
            if (it.namazName == prayer.namazName){
                it.isAlarmOn = prayer.isAlarmOn
            }
        }

    }

    private fun saveNotification(){
        prayerTime?.forEach {
            cancelNotification(it)
        }
        prayerTime?.forEach {
            if (it.isAlarmOn){
                scheduleNotification(it)
            }else{
                cancelNotification(it)
            }
        }
    }
}