package com.roman.application.home.domain.model.response.prayer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Prayers(var namazName: String, var namazTime: String, var isAlarmOn: Boolean = false) :
    Parcelable