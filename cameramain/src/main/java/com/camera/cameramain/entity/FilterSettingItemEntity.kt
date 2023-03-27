package com.camera.cameramain.entity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.camera.cameramain.FilterWebViewActivity
import com.camera.cameramain.R
import com.dingmouren.androidcamerafilter.const.privacyPolicyUrl

class FilterSettingItemEntity {
    var icon = 0
    var title = ""
    var rightArrow = 0
    var onItemClick : (Context)->Unit = {}

    companion object {
        fun getSettingItems():List<FilterSettingItemEntity>{

            return mutableListOf<FilterSettingItemEntity>().apply {
                add(FilterSettingItemEntity().apply {
                    icon = R.mipmap.filter_setting_rate_icon
                    title = "Rate US"
                    rightArrow = R.mipmap.filter_setting_right_arrow
                    onItemClick = {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("market://details?id=" + it.getPackageName())
                        //存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
                        //存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
                        if (intent.resolveActivity(it.packageManager) != null) { //可以接收
                            it.startActivity(intent)
                        } else { //没有应用市场，我们通过浏览器跳转到Google Play
                            intent.data =
                                Uri.parse("https://play.google.com/store/apps/details?id=" + it.getPackageName())
                            //这里存在一个极端情况就是有些用户浏览器也没有，再判断一次
                            if (intent.resolveActivity(it.getPackageManager()) != null) { //有浏览器
                                it.startActivity(intent)
                            } else { //天哪，这还是智能手机吗？
                                Toast.makeText(it, "您没安装应用市场，连浏览器也没有", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                })

                add(FilterSettingItemEntity().apply {
                    icon = R.mipmap.filter_setting_feed_back_icon
                    title = "Feed Back"
                    rightArrow = R.mipmap.filter_setting_right_arrow
                    onItemClick = {
                        val uri = Uri.parse("mailto:12312312312")
                        val intent = Intent(Intent.ACTION_SENDTO, uri)
                        it.startActivity(Intent.createChooser(intent, "请选择邮件类应用"))
                    }
                })

                add(FilterSettingItemEntity().apply {
                    icon = R.mipmap.filter_setting_icon_privacy
                    title = "Privacy Policy"
                    rightArrow = R.mipmap.filter_setting_right_arrow
                    onItemClick = {
                        FilterWebViewActivity.start(it, privacyPolicyUrl,"Privacy policy")
                    }
                })
            }
        }
    }

}