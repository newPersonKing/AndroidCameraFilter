package com.camera.cameramain.ext

import android.app.Activity
import android.content.Intent

inline fun <reified T : Activity>Activity.startExt(block:Intent.()->Unit = {}){
    startActivity(Intent(this,T::class.java).apply(block))
}