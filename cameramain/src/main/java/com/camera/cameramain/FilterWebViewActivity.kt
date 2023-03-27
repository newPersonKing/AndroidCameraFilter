package com.camera.cameramain

import android.content.Context
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.camera.cameramain.ext.startExt
import kotlinx.android.synthetic.main.activity_filter_webview.*

class FilterWebViewActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context, url:String, title:String){
            context.startExt<FilterWebViewActivity> {
                putExtra("title",title)
                putExtra("url",url)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_webview)

        iv_back.setOnClickListener {
            finish()
        }
        val title = intent.getStringExtra("title")
        val url = intent.getStringExtra("url")
        webview.webViewClient = WebViewClient()
        url?.apply {
            webview.loadUrl(this)
        }

        tv_title.text = title
    }
}