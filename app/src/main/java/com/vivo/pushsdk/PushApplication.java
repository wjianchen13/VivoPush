package com.vivo.pushsdk;

import android.app.Application;
import android.util.Log;

import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.vivo.push.util.VivoPushException;

public class PushApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        //初始化push
        try {
            PushClient.getInstance(getApplicationContext()).initialize();
        } catch (VivoPushException e) {
            e.printStackTrace();
        }

        // 打开push开关, 关闭为turnOffPush，详见api接入文档
        PushClient.getInstance(getApplicationContext()).turnOnPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int state) {
                // TODO: 开关状态处理， 0代表成功
                Log.d("tanliang", " state= " + state);
                if (state == 0) {
                    String regId = PushClient.getInstance(PushApplication.this).getRegId();
                    Log.d("PushApplication", " regId= " + regId);
                }
            }
        });
    }
}
