package com.camera.cameramain.composebase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color


@Composable
fun buildBaseTheme(content:@Composable ()->Unit){
    CompositionLocalProvider(values = arrayOf(localPrimaryColor provides Color(0xFF3893FF)
        , localPrimaryBgColor provides Color.White)) {
        content()
    }
}

val localPrimaryColor = compositionLocalOf<Color> { throw IllegalStateException("primary color not set") }

val localPrimaryBgColor = compositionLocalOf<Color> { throw IllegalStateException("primary bg color not set") }