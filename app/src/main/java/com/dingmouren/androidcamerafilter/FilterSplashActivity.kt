package com.dingmouren.androidcamerafilter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.camera.cameramain.FilterMainActivity
import com.camera.cameramain.FilterWebViewActivity
import com.camera.cameramain.composebase.buildBaseTheme
import com.camera.cameramain.composebase.localPrimaryColor
import com.dingmouren.androidcamerafilter.const.privacyPolicyUrl

class FilterSplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ComposeView(this).apply {
            setContent {
               buildBaseTheme {
                   ConstraintLayout(
                       modifier = Modifier
                           .fillMaxSize()
                           .background(color = Color(0xFF73BA69))
                   ) {

                       val (splashCameraImg,splashCenterTv,splashBg,lineProgress,startBtn) = createRefs()

                       Image(painter = painterResource(
                           id = R.mipmap.splash_bg),
                           contentDescription = null,
                           modifier = Modifier
                               .fillMaxSize()
                               .constrainAs(splashBg) {
                                   start.linkTo(parent.start)
                                   end.linkTo(parent.end)
                                   top.linkTo(parent.top)
                                   bottom.linkTo(parent.bottom)
                               },
                           contentScale = ContentScale.FillBounds
                       )

                       Image(painter = painterResource(
                           id = R.mipmap.ic_launcher),
                           contentDescription = null,
                           modifier = Modifier
                               .size(width = 100.dp, height = 85.dp)
                               .constrainAs(splashCameraImg) {
                                   start.linkTo(parent.start)
                                   end.linkTo(parent.end)
                                   top.linkTo(parent.top,margin = 200.dp)
                               },
                           contentScale = ContentScale.FillBounds
                       )


                       Text(text = "Wow! \n" +
                               "Beauty Camera &\n" +
                               "Photo Retoucher", style = TextStyle(
                           color = Color.White,
                           fontSize = 18.sp,
                           fontWeight = FontWeight.W600,),
                           textAlign = TextAlign.Center,
                           modifier = Modifier.constrainAs(splashCenterTv){
                               top.linkTo(splashCameraImg.bottom, margin = 40.dp)
                               start.linkTo(parent.start)
                               end.linkTo(parent.end)
                           }
                       )

                       var isLoading by remember {
                           mutableStateOf(false)
                       }

                       if(isLoading){
                           drawLineProgress(modifier = Modifier
                               .height(15.dp)
                               .constrainAs(lineProgress) {
                                   start.linkTo(parent.start, margin = 30.dp)
                                   end.linkTo(parent.end, margin = 30.dp)
                                   bottom.linkTo(parent.bottom, margin = 30.dp)
                                   width = Dimension.fillToConstraints
                               }, activeColor = localPrimaryColor.current, deActiveColor = Color.White)
                       }else {
                           buildStartBtn(modifier = Modifier.constrainAs(startBtn) {
                               start.linkTo(parent.start, margin = 30.dp)
                               end.linkTo(parent.end, margin = 30.dp)
                               bottom.linkTo(parent.bottom, margin = 100.dp)
                               width = Dimension.fillToConstraints
                           }){
                               isLoading = true
                           }
                       }
                   }
               }
            }
        })
    }

    @Composable
    fun buildStartBtn(modifier: Modifier,onStartClick:()->Unit){
        Column(
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier
                .size(158.dp, 50.dp)
                .background(color = localPrimaryColor.current, shape = RoundedCornerShape(12.dp))
                .clickable {
                    onStartClick.invoke()
                },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Start", style = TextStyle(fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Privacy policy",
                style = TextStyle(color = Color(0xFF8833FF), fontSize = 16.sp),
                modifier = Modifier.clickable {
                    FilterWebViewActivity.start(this@FilterSplashActivity, url = privacyPolicyUrl,"Privacy policy")
                }
            )
        }
    }
    private var adIsLoad = false
    @Composable
    fun drawLineProgress(modifier: Modifier,activeColor:Color,deActiveColor: Color){

        var isStartAnim by remember {
            mutableStateOf(false)
        }
        val percent by animateFloatAsState(targetValue = if(isStartAnim) 1f else 0f,
            animationSpec = tween(7 * 1000),
            finishedListener = {
                if (adIsLoad)return@animateFloatAsState
                startActivity(Intent(this,FilterMainActivity::class.java))
                finish()
            })
        val realPercent = percent.coerceIn(0f,1f)
        androidx.compose.foundation.Canvas(modifier = modifier) {
            val width = size.width
            val height = size.height
            drawRoundRect(
                color = deActiveColor,
                size = Size(width, height),
                cornerRadius = CornerRadius(height / 2)
            )
            drawRoundRect(
                color = activeColor,
                size = Size(width * realPercent, height),
                cornerRadius = CornerRadius(height / 2)
            )
        }

        LaunchedEffect(key1 = Unit, block = {
            isStartAnim = true
        })
    }
}