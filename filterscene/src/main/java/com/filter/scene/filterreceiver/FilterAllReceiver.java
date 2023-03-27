package com.filter.scene.filterreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.filter.scene.rxproxy.FilterBroadCastInterface;

/**
 * 全局广播接受类
 * @author kevinbai
 */
public class FilterAllReceiver extends BroadcastReceiver {

    private FilterBroadCastInterface mExInterface;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            handleIntent(context,intent);
        }
    }


    /**
     * 外插广告
     * @param cxt
     */
    private void handleIntent(Context cxt,Intent intent) {
        sendCallback(cxt,intent);
    }

    private void sendCallback(Context cxt,Intent intent){
        if(mExInterface!=null){
            mExInterface.broadCast(cxt,intent.getAction(),intent);
        }
    }


    public void registerListener(Context cxt, FilterBroadCastInterface exInterface) {
        this.mExInterface=exInterface;
        IntentFilter filter = new IntentFilter();
        //优先级
        filter.setPriority(1000);
        //USB
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        //亮屏 灭屏 解锁
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        //45s 倒计时
        filter.addAction(FilterBroadCastIntent.ACTION_ALARM_COUNT);
        //应用进入后台
        filter.addAction(FilterBroadCastIntent.ACTION_APP_IN_BACKGROUND);
        //第三方应用进入前台
        filter.addAction(FilterBroadCastIntent.ACTION_OTHER_APP_IN_FOREGROUND);
        //第三方应用进入后台
        filter.addAction(FilterBroadCastIntent.ACTION_OTHER_APP_IN_BACKGROUND);
        //保活回调
        filter.addAction(FilterBroadCastIntent.ACTION_KEEP_ALIVE_CALLBACK);
        //拉活回调
        filter.addAction(FilterBroadCastIntent.ACTION_WEEK_UP_CALLBACK);
        //开屏劫持
        filter.addAction(FilterBroadCastIntent.ACTION_SCREEN_HACK);

        cxt.registerReceiver(this, filter);
    }

    public void unRegister(Context cxt) {
        try {
            if (cxt != null) {
                cxt.unregisterReceiver(this);
            }
        } catch (Exception e) {
        }
    }
}
