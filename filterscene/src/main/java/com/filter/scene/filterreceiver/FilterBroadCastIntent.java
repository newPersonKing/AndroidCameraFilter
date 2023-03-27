package com.filter.scene.filterreceiver;

/**
 * 广播action
 */
public class FilterBroadCastIntent {


    /**
     * 自定义倒计时广播
     */
    public static final String ACTION_ALARM_COUNT="android.intent.action.ALARM_COUNT";

    /**
     * HOME
     */
    public static final String ACTION_HOME="android.intent.action.USER_HOME";
    /**
     * RECENT
     */
    public static final String ACTION_RECENT="android.intent.action.USER_RECENT";

    /**
     * WIFI CONNECT
     */
    public static final String ACTION_WIFI_CONNECT="android.intent.action.WIFI_CONNECT";

    /**
     * WIFI DISCONNECT
     */
    public static final String ACTION_WIFI_DISCONNECT="android.intent.action.WIFI_DISCONNECT";

    /**
     * NETWORK CONNECT
     */
    public static final String ACTION_NETWORK_CONNECT="android.intent.action.NETWORK_CONNECT";

    /**
     * 应用进入后台
     */
    public static final String ACTION_APP_IN_BACKGROUND="android.intent.action.APP_IN_BACKGROUND";

    /**
     * 第三方应用进入前台
     */
    public static final String ACTION_OTHER_APP_IN_FOREGROUND="android.intent.action.OTHER_APP_IN_FOREGROUND";

    /**
     * 第三方应用进入后台
     */
    public static final String ACTION_OTHER_APP_IN_BACKGROUND="android.intent.action.OTHER_APP_IN_BACKGROUND";

    /**
     * 保活回调
     */
    public static final String ACTION_KEEP_ALIVE_CALLBACK="android.intent.action.KEEP_ALIVE_CALLBACK";

    /**
     * 拉活回调
     */
    public static final String ACTION_WEEK_UP_CALLBACK="android.intent.action.WEEK_UP_CALLBACK";

    /**
     * 开屏劫持
     */
    public static final String ACTION_SCREEN_HACK="android.intent.action.SCREEN_HACK";

}
