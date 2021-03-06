package com.sora.myapplist;

import android.content.Intent;

import java.io.Serializable;

/**
 * Created by Sora on 2015/12/6.
 *序列化
 */

public class AppInfo implements Serializable {
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
    //具体大小值
    private Double exact_AppSize;
    //安装时间
    private String installTime;
    //appID
    private String appID;
    //序列化需要参数
    public static final long serialVersionUID = 9527L;

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

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

    public Double getExact_AppSize() {
        return exact_AppSize;
    }

    public void setExact_AppSize(Double exact_AppSize) {
        this.exact_AppSize = exact_AppSize;
    }

    @Override
    public String toString() {
        return appID + ":   " + appName + "    " + edition + "    " + appSize + "    " + packageName + "    " + installTime;
    }
}
