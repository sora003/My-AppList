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
    public void createAppList() {
        //创建List 用于装填App信息
        appInfoList = new ArrayList<AppInfo>();
        //获取已安装的应用程序包
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        //对获得的应用程序包进行相关操作
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            //过滤无版本名的app
            if (p.versionName == null) {
                continue;
            }
            //过滤非三方应用程序
            if (!filterApp(p.applicationInfo)) {
                continue;
            }
            //获得App创建时对应的文件夹
            String dir = p.applicationInfo.publicSourceDir;
            //获取App创建时对应文件夹的大小
            Long size_long = new File(dir).length();
            //定义时间格式
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            //App创建时对应的文件夹的最近修改时间-最近一次的更新时间
            Date date = new Date(new File(dir).lastModified());
            //获取App的Label
            String appName = (String) p.applicationInfo.loadLabel(getPackageManager());
            //获取App的大小
            String appSize = Long.toString(size_long / 1024 / 1024) + "MB";
            //获取App对应的包名
            String packageName = p.packageName;
            //获取App的安装时间
            String installTime = formatter.format(date);
            //获取版本名
            String editon = p.versionName;
            //构建AppInfo对象
            AppInfo appInfo = new AppInfo();
            //设置App所需输出的各参数值
            appInfo.setAppName(appName);
            appInfo.setEdition(editon);
            appInfo.setPackageName(packageName);
            appInfo.setAppSize(appSize);
            appInfo.setInstallTime(installTime);
            //添加进列表中
            appInfoList.add(appInfo);
            int now_progress = (int) ((i + 1) / (float) packs.size() * progressBar.getMax());
            progressBar.setProgress(now_progress);
        }
        progressBar.setProgress(progressBar.getMax());
        progressBar.setVisibility(View.GONE);
    }

    //三方应用程序过滤器
    private boolean filterApp(ApplicationInfo applicationInfo) {
        //代表被用户升级过的系统应用
        if ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        }
        //代表用户应用
        else if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }
}
