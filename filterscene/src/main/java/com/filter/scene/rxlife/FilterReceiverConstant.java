package com.filter.scene.rxlife;

/**
 * @author : kevinbai
 * date      : 2021/3/29 下午2:36
 */
public class FilterReceiverConstant {

    /**
     * 网络监听广播次数统计
     */
    public static final String NETWORK_BROADCAST_TIME ="NETWORK_BROADCAST_TIME";
    /**
     * 网络监听广播次数统计阀值
     * 暂时定为2，从0开始计数，因为保活回调会连续执行两次
     */
    public static final long NETWORK_BROADCAST_TIME_VALUE =5*1000;


    /**
     * 页面传递key
     * BUNDLE
     */
    public static final String INTENT_BUNDLE="INTENT_BUNDLE";

}
