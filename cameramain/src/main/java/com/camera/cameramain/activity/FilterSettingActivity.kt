package com.camera.cameramain.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.camera.cameramain.composebase.buildBaseTheme
import com.camera.cameramain.composebase.localPrimaryBgColor
import com.camera.cameramain.composebase.localPrimaryColor
import com.camera.cameramain.R
import com.camera.cameramain.entity.FilterSettingItemEntity

class FilterSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ComposeView(this).apply {
            setContent {
                buildBaseTheme {
                    Column(modifier = Modifier.fillMaxSize()
                        .background(localPrimaryBgColor.current)) {
                        createTitle()
                        Box(
                            modifier = Modifier
                                .padding(top = 25.dp, start = 12.dp, end = 12.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(vertical = 12.dp, horizontal = 20.dp)
                                .wrapContentHeight()
                        ) {
                            LazyColumn(content = {
                                val items = FilterSettingItemEntity.getSettingItems()
                                items(items.size){ index->
                                    buildItem(items[index])
                                }
                            })
                        }
                    }
                }
            }
        })
    }

    @Composable
    private fun createTitle(){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(20.dp))
            Image(painter = painterResource(id = R.mipmap.selected_image_back_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        finish()
                    }
            )

            Spacer(modifier = Modifier.width(11.dp))

            Text(text = "Setting", style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = localPrimaryColor.current)
            )
        }
    }

    @Composable
    fun buildItem(settingItemEntity: FilterSettingItemEntity){

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .height(23.dp)
            .clickable {
                settingItemEntity.onItemClick.invoke(this)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = settingItemEntity.icon),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = settingItemEntity.title, style = TextStyle(
                fontSize = 16.sp,
                color = localPrimaryColor.current
            ))
            Spacer(modifier = Modifier.weight(1f))
            Image(painter = painterResource(id = settingItemEntity.rightArrow),
                contentDescription = null,
                modifier = Modifier.size(8.dp,14.dp)
            )
        }
    }
}