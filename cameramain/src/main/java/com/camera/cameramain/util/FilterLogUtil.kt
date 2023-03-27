package com.camera.cameramain.util

import android.util.Log
import com.camera.cameramain.BuildConfig

class FilterLogUtil {

    companion object {
        fun logE(msg:String,tag:String = ""){
            if(BuildConfig.DEBUG){
                Log.e("inland_${tag}",msg)
            }
        }
    }
}