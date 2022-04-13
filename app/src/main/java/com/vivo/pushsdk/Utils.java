package com.vivo.pushsdk;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils {
    public static final String TAG = "PushDemoActivity";
    public static final String SYSTEM_PACKAGE_NAME = "com.vivo.abe";
    public static final String PACKAGE_NAME_PUSH_DEMO = "com.vivo.pushdemo.test";
    public static final String PACKAGE_NAME_SDK_TEST = "com.vivo.sdk.test";

    public static Notification.Builder createNotificationForTest(Context context) {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String notifyId = "pushtest";
            NotificationChannel channel = new NotificationChannel(notifyId, "推送通知测试", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            builder = new Notification.Builder(context, notifyId);
            Bundle bundle = new Bundle();
            bundle.putInt("vivo.summaryIconRes", R.drawable.ic_launcher_background);
            builder.setExtras(bundle);
        } else {
            builder = new Notification.Builder(context);
        }
        return builder;
    }

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {

        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    public static ArrayList<String> getTagsList(String originalText) {

        if (originalText == null || originalText.equals("")) {
            return null;
        }
        ArrayList<String> tags = new ArrayList<>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String encodeUTF(String source) {

        String code = "";

        try {
            code = URLEncoder.encode(source, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return code;

    }

    /**
     * 是否可执行一些有权限的操作
     *
     * @return
     */
    public static boolean isHasPermission(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();
        String curSign = null;
        String sysSign = null;
        while (iter.hasNext()) {
            PackageInfo packageinfo = iter.next();
            String packageName = packageinfo.packageName;
            if (packageName.equals(SYSTEM_PACKAGE_NAME)) {
                sysSign = packageinfo.signatures[0].toCharsString();
            } else if (packageName.equals(context.getPackageName())) {
                curSign = packageinfo.signatures[0].toCharsString();
            }
        }
        if (!TextUtils.isEmpty(curSign) && !TextUtils.isEmpty(sysSign)) {
            return curSign.equals(sysSign);
        }
        return true;
    }

    /**
     * 只针对系统push所做的拦截措施
     *
     * @param context
     * @return
     */
    public static boolean isSupportSystemPushSDK(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = null;
            packInfo = packageManager.getPackageInfo(SYSTEM_PACKAGE_NAME, 0);
            int version = packInfo.versionCode;
            if (version >= 1000) {
                return true;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否支持实时push版本，用于提高push时效性，目前用于适配v消息的版本
     *
     * @param context
     * @return
     */
    public static boolean isSupportVivoPushSDK(Context context) {
        boolean isSupport = true;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = null;
            packInfo = packageManager.getPackageInfo(SYSTEM_PACKAGE_NAME, 0);
            int version = packInfo.versionCode;
            if (version >= 1000 && version <= 2000) {
                isSupport = false;
            }
        } catch (NameNotFoundException e) {
            //do nothing
        }
        return isSupport;
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("AutoTestActivity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences("AutoTestActivity", Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

}
