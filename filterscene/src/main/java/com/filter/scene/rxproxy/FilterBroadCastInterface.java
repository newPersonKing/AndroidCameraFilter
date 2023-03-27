package com.filter.scene.rxproxy;

import android.content.Context;
import android.content.Intent;

/**
 * @author : kevinbai
 * date      : 2021/3/27 下午1:57
 */
public interface FilterBroadCastInterface {

     void broadCast(Context cxt, String action, Intent intent);

}
