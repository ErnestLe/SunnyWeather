package com.sunnyweather.android

import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {

    companion object {
        // TOKEN 为申请到到彩云天气到 token
        const val TOKEN = ""
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}