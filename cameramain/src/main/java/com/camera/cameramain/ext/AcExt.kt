package com.camera.cameramain.ext

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Activity> Activity.startExt(block:Intent.()->Unit = {}){
    startActivity(Intent(this,T::class.java).apply(block))
}

inline fun <reified T : Activity> Context.startExt(block:Intent.()->Unit = {}){
    startActivity(Intent(this,T::class.java).apply(block))
}