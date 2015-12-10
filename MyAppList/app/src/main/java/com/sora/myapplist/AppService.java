package com.sora.myapplist;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sora on 2015/12/10.
 */
public class AppService extends AppCompatActivity {

    private List<AppInfo> appInfoList = null;
    private ProgressBar progressBar = null;
    Context context;

    public AppService(Context context) {
        this.context = context;
    }

    public List<AppInfo> getAppInfoList() {
        return appInfoList;
    }

    //获取所有的App信息

}
