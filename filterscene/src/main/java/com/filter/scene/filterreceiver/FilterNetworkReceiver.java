package com.filter.scene.filterreceiver;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;

import com.filter.scene.rxlife.FilterReceiverConstant;
import com.filter.scene.rxproxy.FilterBroadCastInterface;
import com.filter.scene.utils.NetworkUtil;
import com.filter.scene.utils.FilterSharePreferenceUtil;

import androidx.annotation.NonNull;


/**
 * 网络监听类
 * @author kevin.bai
 */
public class FilterNetworkReceiver {

    private final Context mContext;
    private final ConnectivityManager mConnectivityManager;
    private NetworkCallBack mNetworkCallBack;
    private FilterBroadCastInterface mExInterface;

    public FilterNetworkReceiver(Context cxt) {
        this.mContext = cxt;
        this.mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= 21) {
            this.mNetworkCallBack = new NetworkCallBack();
        }
    }

    @TargetApi(21)
    public class NetworkCallBack extends ConnectivityManager.NetworkCallback {
        //网络连接成功，通知可以使用的时候调用
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            handleIntent(mContext,null);
        }
        //当网络已断开连接时调用
        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            new Handler(msg -> {
                if(!NetworkUtil.isWiFiConnect(mContext)){
                    sendCallback(mContext, FilterBroadCastIntent.ACTION_WIFI_DISCONNECT,null);
                }
                return false;
            }).sendEmptyMessageDelayed(0, 2000);
        }
        //当网络连接超时或网络请求达不到可用要求时调用
        @Override
        public void onUnavailable() {
            super.onUnavailable();
        }
        //当网络状态修改但仍旧是可用状态时调用
        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
        }

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                    handleIntent(mContext,intent);
                }
            }
        }
    };


    /**
     * WI-FI 连接有延时
     * @param cxt
     */
    private void handleIntent(final Context cxt,Intent intent) {
        //回调时间-注册时间<5s 不触发
        Long firstTime= FilterSharePreferenceUtil.getLong(FilterReceiverConstant.NETWORK_BROADCAST_TIME);
        Long endTime=System.currentTimeMillis();
        if(firstTime==0L||(endTime-firstTime)< FilterReceiverConstant.NETWORK_BROADCAST_TIME_VALUE){
            return;
        }
        new Handler(msg -> {
            if(NetworkUtil.isWiFiConnect(cxt)){
                sendCallback(cxt, FilterBroadCastIntent.ACTION_WIFI_CONNECT,intent);
            }else if(NetworkUtil.isNetConnect(cxt)){
                sendCallback(cxt, FilterBroadCastIntent.ACTION_NETWORK_CONNECT,intent);
            }
            return false;
        }).sendEmptyMessageDelayed(0, 2000);
    }

    private void sendCallback(Context cxt,String action,Intent intent){
        try {
            if(mExInterface!=null){
                mExInterface.broadCast(cxt,action,intent);
            }
        } catch (Exception e) {
        }
    }


    public void registerListener(FilterBroadCastInterface exInterface) {
        this.mExInterface=exInterface;
        //API 大于26时
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mConnectivityManager != null && mNetworkCallBack != null) {
                mConnectivityManager.registerDefaultNetworkCallback(mNetworkCallBack);
            }
            //API大于21时
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mConnectivityManager != null && mNetworkCallBack != null) {
                NetworkRequest.Builder builder = new NetworkRequest.Builder();
                NetworkRequest request = builder.build();
                mConnectivityManager.registerNetworkCallback(request, mNetworkCallBack);
            }
        } else {//低版本
            if (mContext != null && broadcastReceiver != null) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                mContext.registerReceiver(broadcastReceiver, intentFilter);
            }
        }
        /**
         * 首次注册会触发回调，保活回调会触发重新注册
         * 为了规避首次触发问题，用时间控制，注册时间与回调时间不同才是真正响应
         * 暂定此值>5s
         */
        FilterSharePreferenceUtil.pushLong(FilterReceiverConstant.NETWORK_BROADCAST_TIME,System.currentTimeMillis());
    }

    public void unRegister() {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                if (mConnectivityManager != null && mNetworkCallBack != null) {
                    mConnectivityManager.unregisterNetworkCallback(mNetworkCallBack);
                }
            } else {
                if (mContext != null && broadcastReceiver != null) {
                    mContext.unregisterReceiver(broadcastReceiver);
                }
            }
        } catch (Exception e) {
        }
    }


}
