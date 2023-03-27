package com.filter.scene.filterreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.filter.scene.rxproxy.FilterBroadCastInterface;

import java.util.concurrent.TimeUnit;

/**
 * 按键监听
 * 锁屏页面使用
 * @author kevinbai
 */
public class FilterHomeReceiver extends BroadcastReceiver {

    private FilterBroadCastInterface mExInterface;
    private long lastTime=0L;//上次触发时间

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    if ("homekey".equals(reason)) {
                        //部分机型HOME按键会连续触发两次，这里用时间过滤，暂定2s
                        long curTime=System.currentTimeMillis();
                        if(lastTime!=0&&curTime-lastTime< TimeUnit.SECONDS.toMillis(2)){
                            return;
                        }
                        handleIntent(context, FilterBroadCastIntent.ACTION_HOME,intent);
                        lastTime=curTime;
                    }else if ("recentapps".equals(reason)) {
                        handleIntent(context, FilterBroadCastIntent.ACTION_RECENT,intent);
                    }
                }
            }
        }
    }


    /**
     * 外插广告
     * @param cxt
     * @param action
     */
    private void handleIntent(Context cxt, String action,Intent intent) {
        sendCallback(cxt,action,intent);
    }

    private void sendCallback(Context cxt,String action,Intent intent){
        if(mExInterface!=null){
            mExInterface.broadCast(cxt,action,intent);
        }
    }

    public void registerListener(Context cxt, FilterBroadCastInterface exInterface) {
        this.mExInterface=exInterface;
        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
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
