package com.roman.application.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appClassInstance = this
    }

    companion object{

        private lateinit var appClassInstance: BaseApplication

        @Synchronized
        fun getInstance(): BaseApplication {
            return appClassInstance
        }
    }
}