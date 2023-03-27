package com.dingmouren.androidcamerafilter.control

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.dingmouren.androidcamerafilter.BuildConfig
import com.tradplus.bn.initTradPlus

class AdsController {

    companion object {
        private var retryHandler = Handler(Looper.getMainLooper())
        private var adsKey = "2B34D817BB00D4EA7394A47232779E9B"
        fun initChatAds(application: Application){
            initTradPlus(application,  debugLog = BuildConfig.DEBUG,{

            },adsKey)
        }
    }
}