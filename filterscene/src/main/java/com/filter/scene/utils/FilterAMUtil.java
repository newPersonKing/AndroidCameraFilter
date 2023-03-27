package com.filter.scene.utils;

import android.content.Context;
import android.content.Intent;

/**
 * @author : kevinbai
 * date      : 2021/3/29 下午2:04
 */
public class FilterAMUtil {

//    /**
//     * 延时发送广播
//     * @param cxt
//     * @param delay
//     */
//    public static void send(Context cxt, long delay, String action){
//        AlarmManager alarmManager = (AlarmManager) cxt.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(action);
//        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(cxt, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
//    }

//    /**
//     * 延时发送广播
//     * 携带参数
//     * @param cxt
//     * @param delay
//     */
//    public static void send(Context cxt, long delay, String action, Bundle bundle){
//        AlarmManager alarmManager = (AlarmManager) cxt.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(action);
//        intent.putExtra(CallReceiverConstant.INTENT_BUNDLE,bundle);
//        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(cxt, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
//    }

    /**
     * 发送广播
     * @param cxt
     */
    public static void send(Context cxt, String action){
        Intent intent=new Intent(action);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        cxt.sendBroadcast(intent);
    }

//    /**
//     * 发送广播
//     * @param cxt
//     */
//    public static void send(Context cxt, String action,Bundle bundle){
//        Intent intent=new Intent(action);
//        intent.putExtra(CallReceiverConstant.INTENT_BUNDLE,bundle);
//        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//        cxt.sendBroadcast(intent);
//    }

//    /**
//     * 延时循环发送广播
//     * 时间偏差1分钟之内
//     * @param cxt
//     * @param delay
//     * @param interval
//     * RTC 、RTC_WAKEUP 绝对时间 System.currentTimeMillis()
//     * ELAPSED_REALTIME ELAPSED_REALTIME_WAKEUP 相对时间 SystemClock.elapsedRealtime()
//     */
//    public static void send(Context cxt,long delay,long interval,String action){
//        AlarmManager alarmManager = (AlarmManager) cxt.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(action);
//        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(cxt, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+delay, interval, pendingIntent);
//    }

//    /**
//     * 延时启动Activity
//     * @param cxt
//     * @param delay
//     * @param activity
//     */
//    public static void startActivity(Context cxt, long delay, Class<?> activity){
//        AlarmManager alarmManager = (AlarmManager) cxt.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(cxt, activity);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(cxt, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
//        } else {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
//        }
//    }
}
