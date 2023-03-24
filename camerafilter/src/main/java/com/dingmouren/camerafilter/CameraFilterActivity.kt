package com.dingmouren.camerafilter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.dingmouren.camerafilter.callback.LoadAssetsImageCallback
import com.dingmouren.camerafilter.dialog.DialogFilter
import com.dingmouren.camerafilter.dialog.DialogFilterAdapter
import com.dingmouren.camerafilter.engine.GlideEngine
import com.dingmouren.camerafilter.listener.EndRecordingFilterCallback
import com.dingmouren.camerafilter.listener.StartRecordingFilterCallback
import com.dingmouren.camerafilter.listener.TakePhotoFilterCallback
import com.dingmouren.camerafilter.mgr.SelectedImageManager
import com.dingmouren.camerafilter.view.VideoControlView
import com.dingmouren.camerafilter.view.VideoControlView.OnRecordListener
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import org.wysaid.camera.CameraInstance
import org.wysaid.myUtils.ImageUtil
import org.wysaid.nativePort.CGENativeLibrary
import org.wysaid.view.CameraRecordGLSurfaceView


class CameraFilterActivity : AppCompatActivity() {
    private val TAG = CameraFilterActivity::class.java.name
    private lateinit var mCameraView: CameraRecordGLSurfaceView
    private var mRelaBottom: View? = null
    //    private var mImgFlash: ImageView? = null
//    private var mImgFilter: ImageView? = null
    private var mVideoControlView: VideoControlView? = null
    private var mImgSwitch: ImageView? = null
    private var mImgBack: ImageView? = null

    private var mDialogFilter: DialogFilter? = null
    private var mIsFlashOpened = false
    private var mCurrentFilter: String? = null
    private var mTakePhotoFilterCallback /*拍照的回调*/: TakePhotoFilterCallback? = null
    private var mStartRecordingFilterCallback /*开始录制视频的回调*/: StartRecordingFilterCallback? = null
    private var mEndRecordingFilterCallback /*录制视频结束的回调*/: EndRecordingFilterCallback? = null
    private lateinit var filter1 : View
    private lateinit var filter2 : View
    private lateinit var filter3 : View

    private val mPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_camera)
        ImageUtil.getPath(this)
        CGENativeLibrary.setLoadImageCallback(LoadAssetsImageCallback(this), null)

        for (i in mPermissions.indices) {
            if (PermissionChecker.checkSelfPermission(
                    this,
                    mPermissions[i]
                ) != PermissionChecker.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(mPermissions[i]), 1)
            }
        }

        initView()

        initListener()

        initDialog()

    }

    private fun initDialog(){
        mDialogFilter = DialogFilter(this)

        /*滤镜对话框选择滤镜的监听*/
        mDialogFilter!!.setOnFilterChangedListener { position ->
            mCameraView.setFilterWithConfig(ConstantFilters.FILTERS[position])
            mCurrentFilter = ConstantFilters.FILTERS[position]
        }

        /*过滤对话框显示的监听*/
        mDialogFilter!!.setOnShowListener {
            mRelaBottom!!.animate().alpha(0f).setDuration(1000).start()
        }
        /*过滤对话框隐藏的监听*/
        mDialogFilter!!.setOnDismissListener {
            mRelaBottom!!.animate().alpha(1f).setDuration(1000).start()
        }

    }

    private fun initView() {
        mCameraView = findViewById(R.id.camera_view)
        mRelaBottom = findViewById(R.id.rela_bottom)
//        mImgFlash = findViewById(R.id.img_flash)
//        mImgFilter = findViewById(R.id.img_filter)
        mVideoControlView = findViewById(R.id.video_control_view)
        mImgSwitch = findViewById(R.id.img_switch)
        mImgBack = findViewById(R.id.img_back)
        /*设置摄像头方向*/
        mCameraView.presetCameraForward(true)
        /*录制视频大小*/
        mCameraView.presetRecordingSize(480, 640)
        /*拍照大小。*/
        mCameraView.setPictureSize(2048, 2048, true)
        /*充满view*/
        mCameraView.setFitFullView(true)
        /*设置图片保存的目录*/ImageUtil.setDefaultFolder("dingmouren")
        mTakePhotoFilterCallback = TakePhotoFilterCallback(this)
        mStartRecordingFilterCallback = StartRecordingFilterCallback(this)
        mEndRecordingFilterCallback = EndRecordingFilterCallback(this)

        filter1 = findViewById(R.id.filter_1)
        filter2 = findViewById(R.id.filter_2)
        filter3 = findViewById(R.id.filter_3)
    }

    private fun initListener() {
        /*返回*/
        mImgBack!!.setOnClickListener { finish() }
        /*切换前后摄像头*/mImgSwitch!!.setOnClickListener { mCameraView.switchCamera() }

        /*触摸对焦*/
        mCameraView.setOnTouchListener { v: View?, event: MotionEvent ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    Log.i(
                        TAG,
                        String.format("Tap to focus: %g, %g", event.x, event.y)
                    )
                    val focusX = event.x / mCameraView.width
                    val focusY = event.y / mCameraView.height
                    mCameraView.focusAtPoint(
                        focusX,
                        focusY
                    ) { success, camera ->
                        if (success) {
                            Log.e(
                                TAG,
                                String.format(
                                    "Focus OK, pos: %g, %g",
                                    focusX,
                                    focusY
                                )
                            )
                        } else {
                            Log.e(
                                TAG,
                                String.format(
                                    "Focus failed, pos: %g, %g",
                                    focusX,
                                    focusY
                                )
                            )
                            mCameraView.cameraInstance()
                                .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)
                        }
                    }
                }
                else -> {}
            }
            true
        }
        /*弹出选择滤镜的对话框*/
//        mImgFilter!!.setOnClickListener { mDialogFilter!!.show() }

        findViewById<View>(R.id.album).setOnClickListener {
            PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setSelectionMode(SelectModeConfig.SINGLE)
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>?) {
                        if((result?.size ?: 0) == 1){
                            SelectedImageManager.selectedLocalMedia = result!![0]
                            startActivity(Intent(this@CameraFilterActivity,ImageSelectedActivity::class.java))
                        }
                    }
                    override fun onCancel() {

                    }
                })
        }

        /*拍照 录制*/
        mVideoControlView!!.setOnRecordListener(object : OnRecordListener() {
            override fun onShortClick() {
                mCameraView.takePicture(mTakePhotoFilterCallback, null, mCurrentFilter, 1.0f, true)
            }

            override fun OnRecordStartClick() {
                val videoFileName = ImageUtil.getPath(this@CameraFilterActivity) + "/" + System.currentTimeMillis() + ".mp4"
                mEndRecordingFilterCallback!!.setVideoFilePath(videoFileName)
                mCameraView.startRecording(videoFileName, mStartRecordingFilterCallback)
            }

            override fun OnFinish(resultCode: Int) {
                when (resultCode) {
                    0 -> Toast.makeText(this@CameraFilterActivity, "录制时间过短", Toast.LENGTH_SHORT)
                        .show()
                    1 -> mCameraView.endRecording(mEndRecordingFilterCallback)
                }
            }

            override fun check(): Boolean {
                if(ActivityCompat.checkSelfPermission(this@CameraFilterActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this@CameraFilterActivity,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this@CameraFilterActivity,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this@CameraFilterActivity, "need agree ", Toast.LENGTH_SHORT).show()
                    return false
                }
                return true
            }
        })

        filter1.setOnClickListener {
            DialogFilterAdapter.currentIndex = 0
            initDialog()
            mDialogFilter?.show()
        }

        filter2.setOnClickListener {
            DialogFilterAdapter.currentIndex = 1
            initDialog()
            mDialogFilter?.show()
        }

        filter3.setOnClickListener {
            DialogFilterAdapter.currentIndex = 2
            initDialog()
            mDialogFilter?.show()
        }
    }

    override fun onResume() {
        super.onResume()
        mCameraView.resumePreview()
    }

    override fun onPause() {
        super.onPause()
        mCameraView.stopPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCameraView.release(null)
        CameraInstance.getInstance().stopCamera()
    }

}