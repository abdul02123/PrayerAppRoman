package com.roman.application.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionManager(private val activity: Activity)  {

    private var fragment: Fragment? = null

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 100
        private const val LOCATION_PERMISSION_CODE = 200
        const val NOTIFICATION_PERMISSION_CODE = 300
        const val HOME_NOTIFICATION_PERMISSION_CODE = 400
        const val PROFILE_NOTIFICATION_PERMISSION_CODE = 500
        const val LOCATION_PERMISSION_SETTINGS = 600
        const val LOCATION_PERMISSION_TOGGLE_OFF = 700

        val requiredLocationPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        val requiredPermissionsNotification = arrayOf(
            Manifest.permission.POST_NOTIFICATIONS)

        val requiredPermissions = arrayOf(
            Manifest.permission.CAMERA)
    }

    fun checkPermissions(): Boolean {
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun checkLocationPermissions(): Boolean {
        var result = false
        for (permission in requiredLocationPermissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                result = false
            }else{
                result = true
            }
        }
        return result
    }

    fun showAlert() {




//        val dialog = AlertDialog.Builder(activity)
//            .setMessage(activity.getString(R.string.camera_permission_is_required))
//            .setPositiveButton(activity.getString(R.string.open_settings)) { _, _ ->
//                openAppSettings()
//            }
//            .setNegativeButton(activity.getString(R.string.cancel), null)
//            .create()
//        dialog.show()
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(activity, requiredPermissions, REQUEST_CODE_PERMISSIONS)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun notificationPermissionRationale() {
        ActivityCompat.requestPermissions(activity, requiredPermissionsNotification, NOTIFICATION_PERMISSION_CODE)
    }

    fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            activity,
            requiredLocationPermissions,
            LOCATION_PERMISSION_CODE
        )
    }

    fun handlePermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ): Boolean {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        } else if (requestCode == LOCATION_PERMISSION_CODE) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }else if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }
        return false
    }

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.getPackageName(), null)
        intent.data = uri
        activity.startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun openNotificationSettings(fragment: Fragment?= null, requestCode: Int){
        try {
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
            if (fragment!= null){
                fragment.startActivityForResult(intent, requestCode)
            }else{
                activity.startActivityForResult(intent, requestCode)
            }
        }catch (e:Exception){
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                val uri = Uri.fromParts(
                    "package", activity.packageName,
                    null
                )
                intent.data = uri
                if (fragment!= null){
                    fragment.startActivityForResult(intent, requestCode)
                }else{
                    activity.startActivityForResult(intent, requestCode)
                }

            }catch (_:Exception){

            }
        }
    }

    fun openAppSettings(fragment: Fragment?, requestCode: Int) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        fragment?.startActivityForResult(intent, requestCode)
    }

    fun openLocationSettings(fragment: Fragment?, requestCode: Int) {
        try {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            fragment?.startActivityForResult(intent, requestCode)
        }catch (_: Exception){
        }

    }

    fun setFragment(fragment: Fragment) {
        this.fragment = fragment
    }
}