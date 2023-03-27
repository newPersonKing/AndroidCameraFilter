package com.filter.scene.filterreceiver;

import android.app.Application;
import android.content.Context;

import com.filter.scene.rxlife.FilterLifeObserver;
import com.filter.scene.rxproxy.FilterBroadCastInterface;
import com.filter.scene.utils.FilterSharePreferenceUtil;

/**
 * 广播全局管理类
 * @author kevin.bai
 */
public class FilterBroadCoastManager {

    private static FilterBroadCoastManager mFAdsReceiver;
    private final Context mContext;

    FilterAllReceiver mAllReceiver;
    FilterNetworkReceiver mNetworkReceiver;
    FilterInstallReceiver mAddReceiver;
    FilterHomeReceiver mExHomeReceiver;
    FilterBatReceiver mExBatteryReceiver;
    FilterBroadCastInterface mExInterface;

    private FilterBroadCoastManager(Application cxt, FilterBroadCastInterface exInterface) {
        this.mContext = cxt;
        //注册全局存储
        FilterSharePreferenceUtil.setApplication(cxt);
        //注册广播
        mAddReceiver = new FilterInstallReceiver();
        mAllReceiver = new FilterAllReceiver();
        mNetworkReceiver = new FilterNetworkReceiver(cxt);
        mExHomeReceiver=new FilterHomeReceiver();
        mExBatteryReceiver=new FilterBatReceiver();

        //注册代理
        registerListener(exInterface);
        //注册生命周期
        FilterLifeObserver.getInstance(cxt);

    }

    public static FilterBroadCoastManager with(Application cxt, FilterBroadCastInterface exInterface) {
        if (null == mFAdsReceiver) {
            mFAdsReceiver = new FilterBroadCoastManager(cxt,exInterface);
        }
        return mFAdsReceiver;
    }

    /**
     * 开启注册
     * 为了避免多次注册，使用单例处理
     */
    private void registerListener(FilterBroadCastInterface exInterface) {
        //注册代理
        mExInterface= new FilterBroadCastInterfaceWrapper(exInterface);

        if (mAddReceiver != null) {
            mAddReceiver.registerListener(mContext,mExInterface);
        }
        if (mAllReceiver != null && mContext != null) {
            mAllReceiver.registerListener(mContext,mExInterface);
        }
        if (mNetworkReceiver != null) {
            mNetworkReceiver.registerListener(mExInterface);
        }
        if(mExHomeReceiver!=null){
            mExHomeReceiver.registerListener(mContext,mExInterface);
        }
        if(mExBatteryReceiver!=null){
            mExBatteryReceiver.registerListener(mContext,mExInterface);
        }
    }

    public void unRegister() {
        if (mAddReceiver != null) {
            mAddReceiver.unRegister(mContext);
        }
        if (mNetworkReceiver != null) {
            mNetworkReceiver.unRegister();
        }
        if (mAllReceiver != null && mContext != null) {
            mAllReceiver.unRegister(mContext);
        }
        if(mExHomeReceiver!=null){
            mExHomeReceiver.unRegister(mContext);
        }
    }

}
