package com.dingmouren.androidcamerafilter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dingmouren.camerafilter.FilterImageActivity;
import com.dingmouren.camerafilter.util.ConvertBitmapUtils;



import static com.dingmouren.camerafilter.FilterImageActivity.BITMAP_FILTER;
import static com.dingmouren.camerafilter.FilterImageActivity.REQUEST_CODE_FILTER_IMG;

public class MainActivity extends AppCompatActivity {
    private ImageView mImg_1,mImg_2;
    private Button mBtnImg;

    private Bitmap mBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initListener();
    }

    private void initView() {
        mImg_1 = findViewById(R.id.img_1);
        mImg_2 = findViewById(R.id.img_2);
        mBtnImg = findViewById(R.id.btn_img);

        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_test);
        mImg_1.setImageResource(R.drawable.img_test);

    }

    private void initListener(){
        mBtnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapCompressUtils.compressScale(MainActivity.this,R.drawable.img_test,0.3f,0.3f);
                FilterImageActivity.disposeBitmap(MainActivity.this, ConvertBitmapUtils.bitmapToByteArray(bitmap));
            }
        });
    }

    public void cameraFilter(View view){
//        startActivity(new Intent(MainActivity.this, FilterCameraActivity.class));
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK){
//            switch (requestCode){
//                case REQUEST_CODE_FILTER_IMG:
//                    if (data != null){
//                        byte[] bytesArray = data.getByteArrayExtra(BITMAP_FILTER);
//                        if (null != bytesArray && bytesArray.length > 0) {
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytesArray, 0, bytesArray.length);
//                            mImg_2.setImageBitmap(bitmap);
//                        }
//                    }
//                    break;
//            }
//        }
//    }
}
