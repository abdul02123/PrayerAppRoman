package com.roman.application.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.Toast


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