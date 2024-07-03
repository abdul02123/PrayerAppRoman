package com.roman.application.base.location

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.roman.application.util.storage.PermissionUtil.isLocationEnabled

class LocationManager {
    private var activity: AppCompatActivity? = null
    private var fragment: Fragment? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient?=null
    companion object{
        const val REQUEST_CHECK_SETTINGS = 800
    }
    fun setActivity(activity: AppCompatActivity) {
        this.activity = activity
    }
    fun setFragment(fragment: Fragment) {
        this.fragment = fragment
    }

    private var listener: ((locationResult: LocationResult) -> Unit)? = null
    private var progressListener: (() -> Unit)? = null

    @SuppressLint("MissingPermission")
    fun userCurrentLocation() {
        progressListener?.invoke()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity as Activity)
        fusedLocationProviderClient?.requestLocationUpdates(
            getLocationRequest(),
            locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
//                    UserSession.setLatLong(locationResult)
//                    LuxuryBroadcastHandler(activity as AppCompatActivity).sendLatLongBroadCast(locationResult)
            listener?.invoke(locationResult)
        }
    }

    fun removeLocationListener(){
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }

    private fun getLocationRequest(): LocationRequest {
        return LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
            .setWaitForAccurateLocation(false)
            .setIntervalMillis(10000)
            .build()
    }


    fun setLocationListener(listener: (locationResult: LocationResult) -> Unit) {
        this.listener = listener
    }
    fun setProgressListener(progressListener: () -> Unit) {
        this.progressListener = progressListener
    }

    private fun checkLocationSettings() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(getLocationRequest())
        builder.setAlwaysShow(true)
        val mSettingsClient = LocationServices.getSettingsClient(activity as Activity)
        mSettingsClient.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                userCurrentLocation()
            }
            .addOnFailureListener { e ->
                when ((e as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.e("GPS", "ResolvableApiException")
                        val resolvableApiException = e as ResolvableApiException
                        try {
                            if (fragment != null) {
                                fragment?.apply {
                                    startIntentSenderForResult(
                                        resolvableApiException.resolution.intentSender, REQUEST_CHECK_SETTINGS, null, 0, 0, 0, null)
                                }
                            } else {
                                resolvableApiException.startResolutionForResult(activity as Activity, REQUEST_CHECK_SETTINGS
                                )
                            }

                        } catch (_: IntentSender.SendIntentException) {
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Log.e("GPS", "Location settings are inadequate, and cannot be fixed here. Fix in Settings.")
                    }
                }
            }.addOnCanceledListener {
                Log.e("GPS", "checkLocationSettings -> onCanceled")
            }

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                userCurrentLocation()
            }
        }
    }
    fun checkLocationEnable() {
        if (isLocationEnabled(activity as AppCompatActivity)) {
            userCurrentLocation()
        } else {
            checkLocationSettings()
        }
//        CommonUiDialogue.showGPSNotEnabledDialog(requireActivity() as AppCompatActivity)
    }

}