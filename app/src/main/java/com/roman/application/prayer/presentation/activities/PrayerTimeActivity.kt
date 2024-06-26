package com.roman.application.prayer.presentation.activities

import android.view.LayoutInflater
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.databinding.ActivityPrayerTimeBinding
import com.roman.application.home.domain.model.response.prayer.Prayers
import com.roman.application.prayer.presentation.adapter.PrayerTimeAdapter

class PrayerTimeActivity : BaseCompatVBActivity<ActivityPrayerTimeBinding>() {

    override fun setUpViewBinding(layoutInflater: LayoutInflater): ActivityPrayerTimeBinding {
        return ActivityPrayerTimeBinding.inflate(layoutInflater)
    }

    override fun init() {
        mBinding?.appCompatImageView2?.setOnClickListener {
            finish()
        }

        if (intent.getSerializableExtra("prayerTimes")!= null){
            val data = intent.getSerializableExtra("prayerTimes") as ArrayList<Prayers>
            setAdapter(data)
        }
    }

    private fun setAdapter(string: ArrayList<Prayers>){
      val adapter = PrayerTimeAdapter(string)
      mBinding?.recyclerView?.adapter = adapter
    }
}