package com.sora.myapplist;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sora on 2015/12/10.
 */
public class AppInfoService extends AppCompatActivity {

    private List<AppInfo> appInfoList = null;
    Context context;

    public AppInfoService(Context context) {
        this.context = context;
    }

    public List<AppInfo> getAppInfoList() {
        return appInfoList;
    }

    //获取所有的App信息
    //获取AppList信息
    public void createAppList(String appName,String appSize,String packageName,String installTime,String editon) {
        //创建List 用于装填App信息
        appInfoList = new ArrayList<AppInfo>();

    }


}
