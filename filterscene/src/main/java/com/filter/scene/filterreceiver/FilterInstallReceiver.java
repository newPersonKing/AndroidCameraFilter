package com.filter.scene.filterreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.filter.scene.rxproxy.FilterBroadCastInterface;

/**
 * 安装卸载广播
 * @author kevinbai
 */
public class FilterInstallReceiver extends BroadcastReceiver {

    private FilterBroadCastInterface mExInterface;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ccccccccc","CallInstallReceiver==="+intent.getAction());
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            handleIntent(context,action,intent);
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
        Log.e("cccccccc","registerListener");
        this.mExInterface=exInterface;
        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
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
