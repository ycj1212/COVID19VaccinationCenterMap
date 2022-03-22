package com.ycj1212.covid19vaccinationcentermap

import android.app.Application
import com.naver.maps.map.NaverMapSdk

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAP_CLIENT_ID)
    }
}
