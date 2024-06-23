package com.roman.application.presentation.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.roman.application.R
import com.roman.application.base.BaseCompatVBActivity
import com.roman.application.databinding.ActivityHomeBinding
import com.roman.application.databinding.ActivityPrayerTimeBinding

class PrayerTimeActivity : BaseCompatVBActivity<ActivityPrayerTimeBinding>() {

    override fun setUpViewBinding(layoutInflater: LayoutInflater): ActivityPrayerTimeBinding {
        return ActivityPrayerTimeBinding.inflate(layoutInflater)
    }

    override fun init() {
        mBinding?.appCompatImageView2?.setOnClickListener {
            finish()
        }
    }
}