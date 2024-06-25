package com.roman.application.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.roman.application.home.domain.model.response.prayer.PrayersTime
import java.text.SimpleDateFormat
import java.util.Locale


fun Context.showToast(message: String?){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun View.makeVisible(){
    this.visibility = View.VISIBLE
}

fun View.makeGone(){
    this.visibility = View.GONE
}

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }
    }
    return false

}

inline fun <reified T> String.toResponseModel(): T {
    val typeToken = object : TypeToken<T>() {}.type
    return Gson().fromJson(this, typeToken)
}

fun List<String>.toPrayersModel(date:String): PrayersTime {
    val timeFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
    val fajr = timeFormat.parse("$date ${this[0]}")
    val shuruq = timeFormat.parse("$date ${this[1]}")
    val duhur = timeFormat.parse("$date ${this[2]}")
    val asr = timeFormat.parse("$date ${this[3]}")
    val maghrib = timeFormat.parse("$date ${this[4]}")
    val isha = timeFormat.parse("$date ${this[5]}")
    return PrayersTime(fajr!!, shuruq!!, duhur!!, asr!!, maghrib!!, isha!!)
}
