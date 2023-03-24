package com.camera.cameramain

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.camera.cameramain.ext.startExt
import com.dingmouren.camerafilter.CameraFilterActivity
import com.dingmouren.camerafilter.ImageSelectedActivity
import com.dingmouren.camerafilter.engine.GlideEngine
import com.dingmouren.camerafilter.mgr.SelectedImageManager
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import org.wysaid.myUtils.ImageUtil

class FilterMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImageUtil.getPath(this)
        setContentView(ComposeView(this).apply {
            setContent {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White)
                    ) {
                        createTopTitle()
                        createCenterImg()
                        createTipImg()
                    }

                    createBottomTab(modifier = Modifier.align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp))
                }
            }
        })
    }

    @Composable
    private fun createBottomTab(modifier: Modifier){
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(modifier = Modifier
                .size(width = 140.dp, height = 70.dp)
                .background(color = Color(0xFF3893FF), shape = RoundedCornerShape(8.dp))
                .clickable {
                    startExt<CameraFilterActivity>()
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Image(painter = painterResource(id = R.mipmap.filter_main_camera_icon),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Text(text = "camera",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(modifier = Modifier
                .size(width = 140.dp, height = 70.dp)
                .background(color = Color.Black, shape = RoundedCornerShape(8.dp))
                .clickable {
                    PictureSelector.create(this@FilterMainActivity)
                        .openGallery(SelectMimeType.ofImage())
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .setSelectionMode(SelectModeConfig.SINGLE)
                        .forResult(object : OnResultCallbackListener<LocalMedia?> {
                            override fun onResult(result: ArrayList<LocalMedia?>?) {
                                if((result?.size ?: 0) == 1){
                                    SelectedImageManager.selectedLocalMedia = result!![0]
                                    startActivity(
                                        Intent(this@FilterMainActivity,
                                            ImageSelectedActivity::class.java)
                                    )
                                }
                            }
                            override fun onCancel() {

                            }
                        })
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Image(painter = painterResource(id = R.mipmap.filter_main_edit_icon),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Text(text = "Edit",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }

    @Composable
    private fun createTopTitle(){
        Row(
            modifier = Modifier
                .padding(end = 32.dp, start = 20.dp, top = 32.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(text = "Camera",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.Black
                ),
                modifier = Modifier.weight(1f)
            )

            Image(painter = painterResource(id = R.mipmap.filter_main_setting_enter_icon),
                contentDescription = null,
                modifier = Modifier.size(27.dp)
            )
        }
    }
    @Composable
    private fun createCenterImg(){
        Image(painter = painterResource(id = R.mipmap.filter_main_center_bg),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 20.dp)
                .fillMaxWidth()
                .height(340.dp)
        )
    }

    @Composable
    private fun createTipImg(){
        Image(painter = painterResource(id = R.mipmap.filter_main_center_tip_img),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 19.dp)
                .height(46.dp)

        )
    }
}