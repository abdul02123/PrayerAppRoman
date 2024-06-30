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
import java.util.Date
import java.util.Locale
import java.util.TimeZone


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

  fun String.formatDate(inputFormat: String, outputFormat: String): String{
      val input = SimpleDateFormat(inputFormat, Locale.getDefault())
      input.timeZone = TimeZone.getTimeZone("GMT")
      val output = SimpleDateFormat(outputFormat, Locale.getDefault())
      val dateFormat = input.parse(this)
      val outputResult = output.format(dateFormat)
      return outputResult
  }


fun String.toMillisecondsFromDate(inputFormat: String): Long{
    val inputFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("GMT")
    val date: Date? = inputFormat.parse(this)
    val outputFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
    outputFormat.timeZone = TimeZone.getDefault()
    return date?.time ?: 0L
}


fun String.toPrayerDate(): Date {
    return try {
        SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).parse(this) ?: Date()
    } catch (e: Exception) {
        Date()
    }
}
