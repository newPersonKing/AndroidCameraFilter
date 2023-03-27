package com.camera.cameramain.composebase

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object NoRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor(): Color {
        return Color.Transparent
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        return RippleAlpha(0f, 0f, 0f, 0f)
    }

}