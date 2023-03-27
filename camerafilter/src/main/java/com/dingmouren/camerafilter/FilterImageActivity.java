package com.dingmouren.camerafilter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.dingmouren.camerafilter.callback.LoadAssetsImageCallback;
import com.dingmouren.camerafilter.dialog.DialogFilter;
import com.dingmouren.camerafilter.dialog.DialogFilterAdapter;
import com.dingmouren.camerafilter.mgr.SelectedImageManager;
import com.luck.picture.lib.entity.LocalMedia;
import org.wysaid.nativePort.CGENativeLibrary;
import org.wysaid.view.ImageGLSurfaceView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Created by 钉某人
 * github: https://github.com/DingMouRen
 * email: naildingmouren@gmail.com
 */

public class FilterImageActivity extends AppCompatActivity {

    private static final String TAG = "FilterImageActivity";
    public static final String BITMAP_UN_FILTER = "bitmap_un_filter";
    public static final String BITMAP_FILTER = "bitmap_filtert";
    public static final int REQUEST_CODE_FILTER_IMG = 101;

    private ImageGLSurfaceView mGLSurfaceView;
    private AppCompatTextView mImgFilter;
    private ImageView mImgCancel;
    private ImageView mImgConfirm;

    private Bitmap mBitmap;
    private DialogFilter mDialogFilter;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    public static void disposeBitmap(Activity activity, byte[] bytesArray){
        Intent intent = new Intent(activity,FilterImageActivity.class);
//        intent.putExtra(BITMAP_UN_FILTER,bytesArray);
        activity.startActivityForResult(intent,REQUEST_CODE_FILTER_IMG);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_image);
        CGENativeLibrary.setLoadImageCallback(new LoadAssetsImageCallback(this), null);
        LocalMedia media = SelectedImageManager.INSTANCE.getSelectedLocalMedia();
        mBitmap = BitmapFactory.decodeFile(media.getRealPath());

        initView();

        initListener();
    }

    private void initView() {
        mGLSurfaceView = findViewById(R.id.gl_surface_view);
        mImgFilter = findViewById(R.id.tv_filter);
        mImgCancel = findViewById(R.id.img_cancel);
        mImgConfirm = findViewById(R.id.img_confirm);

        DialogFilterAdapter.currentIndex = -1;
        mDialogFilter = new DialogFilter(this);
    }

    private void initListener(){
        /*GLSurfaceView创建时的监听*/
        mGLSurfaceView.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
            @Override
            public void surfaceCreated() {
                mGLSurfaceView.setImageBitmap(mBitmap);
                mGLSurfaceView.setDisplayMode(ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FIT);
            }
        });
        /*显示原图*/
        mGLSurfaceView.post(new Runnable() {
            @Override
            public void run() {
                mGLSurfaceView.setFilterWithConfig(ConstantFilters.FILTERS[0]);
            }
        });
        /*弹出选择滤镜的对话框*/
        mImgFilter.setOnClickListener(v -> mDialogFilter.show());
        /*滤镜对话框选择滤镜的监听*/
        mDialogFilter.setOnFilterChangedListener(position -> mGLSurfaceView.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG,ConstantFilters.FILTERS[position]);
                mGLSurfaceView.setFilterWithConfig(ConstantFilters.FILTERS[position]);
            }
        }));
        /*过滤对话框显示的监听*/
        mDialogFilter.setOnShowListener(dialog -> {
            mImgFilter.animate().alpha(0).setDuration(1000).start();
            mImgCancel.animate().alpha(0).setDuration(1000).start();
            mImgConfirm.animate().alpha(0).setDuration(1000).start();
        });
        /*过滤对话框隐藏的监听*/
        mDialogFilter.setOnDismissListener(dialog -> {
            mImgFilter.animate().alpha(1).setDuration(1000).start();
            mImgCancel.animate().alpha(1).setDuration(1000).start();
            mImgConfirm.animate().alpha(1).setDuration(1000).start();
        });
        /*关闭*/
        mImgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*返回滤镜处理过的bitmap*/
        mImgConfirm.setOnClickListener(v -> mGLSurfaceView.getResultBitmap(new ImageGLSurfaceView.QueryResultBitmapCallback() {
            @Override
            public void get(final Bitmap bmp) {

                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "filter_"+System.currentTimeMillis()+".jpg");
                savePhotoAlbum(bmp,file);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FilterImageActivity.this,"picture save at:"+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }));
    }

    private void savePhotoAlbum(Bitmap src, File file) {
        //先保存到文件
        OutputStream outputStream;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            src.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            if (!src.isRecycled()) {
                src.recycle();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //再更新图库
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName());
            values.put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(file));
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  values);
            if (uri == null) {
                return;
            }
            try {
                outputStream = contentResolver.openOutputStream(uri);
                FileInputStream fileInputStream = new FileInputStream(file);
                FileUtils.copy(fileInputStream, outputStream);
                fileInputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MediaScannerConnection.scanFile(
                    getApplicationContext(),
                    new String[]{file.getAbsolutePath()},
                    new String[]{"image/jpeg"},
                    (path, uri) -> {
                        // Scan Completed
                    });
        }
    }


    private  String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    public  String getMimeType(File file){
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBitmap != null){
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}
