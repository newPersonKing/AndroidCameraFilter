package com.filter.scene.rxlife;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.filter.scene.filterreceiver.FilterBroadCastIntent;
import com.filter.scene.utils.FilterAMUtil;
import com.filter.scene.utils.FilterSharePreferenceUtil;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

/**
 * 保活全局管理类
 * @author kevin.bai
 */
public class FilterLifeObserver implements ComponentCallbacks2, Application.ActivityLifecycleCallbacks, LifecycleObserver {

    /**
     * 当前上下文对象
     */
    private WeakReference<Activity> activityReference=new WeakReference<>(null);

    private static FilterLifeObserver mInstance;

    public static FilterLifeObserver getInstance(Application cxt) {
        if (null == mInstance) {
            mInstance = new FilterLifeObserver(cxt);
        }
        return mInstance;
    }

    private FilterLifeObserver(Application application) {
        //应用状态监听
        application.registerComponentCallbacks(this);
        //生命周期监听
        application.registerActivityLifecycleCallbacks(this);
        //前后台监听
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        FilterSharePreferenceUtil.pushBoolean("isBackground",true);
    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {

    }

    @Override
    public void onLowMemory() {

    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        activityReference=new WeakReference<>(activity);
        FilterSharePreferenceUtil.pushBoolean("isBackground",false);
        Log.e("inland","is background false");
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onForeground() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onBackground() {
        if (activityReference != null) {
            Activity activity=activityReference.get();
            if(activity!=null){
                FilterAMUtil.send(activity, FilterBroadCastIntent.ACTION_APP_IN_BACKGROUND);
            }
        }
        Log.e("inland","is background true");
        FilterSharePreferenceUtil.pushBoolean("isBackground",true);
    }


}
