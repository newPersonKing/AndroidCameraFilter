package com.camera.cameramain

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
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
import androidx.core.app.ActivityCompat
import com.camera.cameramain.activity.FilterSettingActivity
import com.camera.cameramain.ext.startExt
import com.camera.cameramain.util.FilterLogUtil
import com.filter.camerafilter.CameraFilterActivity
import com.filter.camerafilter.ImageSelectedActivity
import com.filter.camerafilter.engine.GlideEngine
import com.filter.camerafilter.mgr.SelectedImageManager
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.language.LanguageConfig
import org.wysaid.myUtils.ImageUtil

class FilterMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImageUtil.getPath(this)
        setContentView(ComposeView(this).apply {
            setContent {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                ) {
                    val state = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .background(color = Color.White)
                            .verticalScroll(state)
                    ) {
                        createTopTitle()
                        createCenterImg()
                        createTipImg()
                    }

                    createBottomTab(modifier = Modifier
                        .padding(horizontal = 30.dp)
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
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(modifier = Modifier
                .size(width = 140.dp, height = 70.dp)
                .background(color = Color(0xFF3893FF), shape = RoundedCornerShape(8.dp))
                .clickable {
                    ActivityCompat.requestPermissions(
                        this@FilterMainActivity,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        ),
                        100
                    )
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Image(painter = painterResource(id = R.mipmap.filter_main_camera_icon),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Text(text = "Camera",
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
                    PictureSelector
                        .create(this@FilterMainActivity)
                        .openGallery(SelectMimeType.ofImage())
                        .setLanguage(LanguageConfig.ENGLISH)
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .setSelectionMode(SelectModeConfig.SINGLE)
                        .forResult(object : OnResultCallbackListener<LocalMedia?> {
                            override fun onResult(result: ArrayList<LocalMedia?>?) {
                                if ((result?.size ?: 0) == 1) {
                                    SelectedImageManager.selectedLocalMedia = result!![0]
                                    startActivity(
                                        Intent(
                                            this@FilterMainActivity,
                                            ImageSelectedActivity::class.java
                                        )
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
                modifier = Modifier
                    .size(27.dp)
                    .clickable {
                        startExt<FilterSettingActivity>()
                    }
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

    private var mhandler = Handler(Looper.getMainLooper())
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        FilterLogUtil.logE("agree_size_${grantResults.size}")
        if(requestCode == 100){
            val  isNotAllAgree = grantResults.any {
                it == PackageManager.PERMISSION_DENIED
            }
            if(isNotAllAgree){
                Toast.makeText(this, "need agree WRITE_EXTERNAL_STORAGE " +
                        "\n READ_EXTERNAL_STORAGE CAMERA permission", Toast.LENGTH_SHORT).show()
                mhandler.postDelayed({
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri =
                            Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },1500)
            }else {
                startExt<CameraFilterActivity>()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}