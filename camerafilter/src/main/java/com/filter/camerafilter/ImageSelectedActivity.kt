package com.filter.camerafilter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dingmouren.camerafilter.R
import com.filter.camerafilter.mgr.SelectedImageManager
import kotlinx.android.synthetic.main.activity_selected_image.*

class ImageSelectedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_image)
        val media = SelectedImageManager.selectedLocalMedia
        media?.apply {
            Glide.with(this@ImageSelectedActivity).load(media.realPath).into(iv_selected)
        }
        iv_back.setOnClickListener {
            finish()
        }

        tv_Edit.setOnClickListener {
            startActivity(Intent(this, ImageFilterActivity::class.java))
        }
    }
}