package com.filter.scene.filterreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.filter.scene.rxproxy.FilterBroadCastInterface;

/**
 * 电池电量检测
 */
public class FilterBatReceiver extends BroadcastReceiver {

    private FilterBroadCastInterface mExInterface;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null&&intent.getAction()!=null) {
            String action = intent.getAction();
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

    private void sendCallback(Context cxt, Intent intent){
        if(mExInterface!=null){
            mExInterface.broadCast(cxt,intent.getAction(),intent);
        }
    }

    public void registerListener(Context cxt, FilterBroadCastInterface exInterface) {
        this.mExInterface=exInterface;
        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
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
