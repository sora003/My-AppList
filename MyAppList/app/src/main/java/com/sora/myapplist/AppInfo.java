package com.sora.myapplist;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by Sora on 2015/12/6.
 */
public class AppInfo {
    //app名称
    private String appName;
    //
    private Intent intent;
    //包名
    private String packageName;
    //版本号
    private String edition;
    //大小
    private String appSize;
    //安装时间
    private String installTime;

    public AppInfo(){}

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }
}
