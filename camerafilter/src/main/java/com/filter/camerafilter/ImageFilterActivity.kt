package com.filter.camerafilter

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.dingmouren.camerafilter.R
import com.filter.camerafilter.callback.LoadAssetsImageCallback
import com.filter.camerafilter.dialog.DialogFilterList
import com.filter.camerafilter.dialog.DialogFilterListAdapter
import com.filter.camerafilter.mgr.SelectedImageManager.selectedLocalMedia
import com.filter.camerafilter.util.QFileUtil
import org.wysaid.nativePort.CGENativeLibrary
import org.wysaid.view.ImageGLSurfaceView
import java.io.*

class ImageFilterActivity : AppCompatActivity() {
    private lateinit var mBitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_image)
        CGENativeLibrary.setLoadImageCallback(LoadAssetsImageCallback(this), null)

        val media = selectedLocalMedia
        val  uri = QFileUtil.getImageContentUri(this,media!!.realPath)
        mBitmap  = QFileUtil.getBitmapFromUri(this,uri)
//        mBitmap = BitmapFactory.decodeFile(media!!.realPath)

        initView()

        initListener()
    }

    private var mGLSurfaceView: ImageGLSurfaceView? = null
    private var mImgFilter: AppCompatTextView? = null
    private var mImgCancel: ImageView? = null
    private var mImgConfirm: ImageView? = null
    private var mDialogFilter: DialogFilterList? = null
    private val mHandler = Handler(Looper.getMainLooper())
    private fun initView() {
        mGLSurfaceView = findViewById(R.id.gl_surface_view)
        mImgFilter = findViewById(R.id.tv_filter)
        mImgCancel = findViewById(R.id.img_cancel)
        mImgConfirm = findViewById(R.id.img_confirm)
        DialogFilterListAdapter.currentIndex = -1
        mDialogFilter = DialogFilterList(this)
    }

    private fun initListener() {
        /*GLSurfaceView创建时的监听*/
        mGLSurfaceView!!.setSurfaceCreatedCallback {
            mGLSurfaceView!!.setImageBitmap(mBitmap)
            mGLSurfaceView!!.displayMode = ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FIT
        }
        /*显示原图*/
        mGLSurfaceView!!.post { mGLSurfaceView!!.setFilterWithConfig(ConstantFilters.FILTERS[0]) }
        /*弹出选择滤镜的对话框*/
        mImgFilter!!.setOnClickListener { v: View? -> mDialogFilter!!.show() }
        /*滤镜对话框选择滤镜的监听*/
        mDialogFilter!!.setOnFilterChangedListener { position: Int ->
            mGLSurfaceView!!.post {
                mGLSurfaceView!!.setFilterWithConfig(ConstantFilters.FILTERS[position])
            }
        }
        /*过滤对话框显示的监听*/
        mDialogFilter!!.setOnShowListener { dialog: DialogInterface? ->
            mImgFilter!!.animate().alpha(0f).setDuration(1000).start()
            mImgCancel!!.animate().alpha(0f).setDuration(1000).start()
            mImgConfirm!!.animate().alpha(0f).setDuration(1000).start()
        }
        /*过滤对话框隐藏的监听*/
        mDialogFilter!!.setOnDismissListener {
            mImgFilter!!.animate().alpha(1f).setDuration(1000).start()
            mImgCancel!!.animate().alpha(1f).setDuration(1000).start()
            mImgConfirm!!.animate().alpha(1f).setDuration(1000).start()
        }
        /*关闭*/
        mImgCancel!!.setOnClickListener { v: View? -> finish() }
        /*返回滤镜处理过的bitmap*/mImgConfirm!!.setOnClickListener { v: View? ->
            mGLSurfaceView!!.getResultBitmap { bmp: Bitmap ->
                val file = File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "filter_" + System.currentTimeMillis() + ".jpg"
                )
                savePhotoAlbum(bmp, file)
                mHandler.post(Runnable {
                    Toast.makeText(
                        this,
                        "picture save at:" + file.absolutePath,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                })
            }
        }
    }

    private fun savePhotoAlbum(src: Bitmap, file: File) {
        //先保存到文件
        var outputStream: OutputStream?
        try {
            outputStream = BufferedOutputStream(FileOutputStream(file))
            src.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            if (!src.isRecycled) {
                src.recycle()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        //再更新图库
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            values.put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(file))
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            val contentResolver = contentResolver
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: return
            try {
                outputStream = contentResolver.openOutputStream(uri)
                val fileInputStream = FileInputStream(file)
                FileUtils.copy(fileInputStream, outputStream!!)
                fileInputStream.close()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                System.currentTimeMillis().toString() + ""
            )
            contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse("file://" + file.absolutePath)
                )
            )
        }
    }


    private fun getSuffix(file: File?): String? {
        if (file == null || !file.exists() || file.isDirectory) {
            return null
        }
        val fileName = file.name
        if (fileName == "" || fileName.endsWith(".")) {
            return null
        }
        val index = fileName.lastIndexOf(".")
        return if (index != -1) {
            fileName.substring(index + 1).lowercase()
        } else {
            null
        }
    }

    private fun getMimeType(file: File?): String {
        val suffix = getSuffix(file) ?: return "file/*"
        val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix)?:""
        return type.ifEmpty { "file/*" }
    }
}