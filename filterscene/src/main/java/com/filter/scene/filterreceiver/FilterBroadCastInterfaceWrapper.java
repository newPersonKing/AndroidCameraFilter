package com.filter.scene.filterreceiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.filter.scene.rxproxy.FilterBroadCastInterface;
import com.filter.scene.utils.FilterSharePreferenceUtil;

public class FilterBroadCastInterfaceWrapper implements FilterBroadCastInterface {
    FilterBroadCastInterface wrapper;
    FilterBroadCastInterfaceWrapper(FilterBroadCastInterface wrapper){
        this.wrapper = wrapper;
    }

    @Override
    public void broadCast(Context cxt, String action, Intent intent) {
        Log.e("inland","broadCast action "+action);
        if(!FilterSharePreferenceUtil.getBoolean("isBackground")){
            Log.e("inland","is not background return");
            return;
        }
        wrapper.broadCast(cxt,action,intent);
    }
}
