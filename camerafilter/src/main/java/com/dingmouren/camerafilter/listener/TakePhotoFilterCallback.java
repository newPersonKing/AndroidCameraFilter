package com.dingmouren.camerafilter.listener;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import org.wysaid.view.CameraRecordGLSurfaceView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Created by dingmouren
 * email: naildingmouren@gmail.com
 * github: https://github.com/DingMouRen
 */

public class TakePhotoFilterCallback implements CameraRecordGLSurfaceView.TakePictureCallback {

    private Context mContext;

    public TakePhotoFilterCallback(Context context){
        this.mContext = context;
    }

    @Override
    public void takePictureOK(Bitmap bmp) {
        if (bmp != null) {
            File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "filter_"+System.currentTimeMillis()+".jpg");
            savePhotoAlbum(bmp,file,mContext);
            bmp.recycle();
            Toast.makeText(mContext,"picture save at:"+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(mContext,"picture save fail",Toast.LENGTH_SHORT).show();
        }
    }

    private void savePhotoAlbum(Bitmap src, File file,Context context) {
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
            ContentResolver contentResolver = context.getContentResolver();
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
                    context.getApplicationContext(),
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
}
