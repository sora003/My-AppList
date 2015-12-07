package com.sora.myapplist;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView = null;
    private List<AppInfo> appInfoList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        init();
        //获取App信息
        getAppInfos();
        AppInfoAdapter appInfoAdapter = new AppInfoAdapter(this,appInfoList);
        listView.setAdapter(appInfoAdapter);
//        设置click事件
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }

    public void init(){
        //工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.app_listView);
        appInfoList = new ArrayList<AppInfo>();
    }


    //    //获取所有的App信息
//    public void getAppInfos(){
//        //获取PackagManager对象
//        PackageManager packageManager = this.getPackageManager();
//        Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        //通过查询，获取所有ResolveInfo对象
//        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(mainIntent,PackageManager.MATCH_DEFAULT_ONLY);
//        //根据Name排序
//        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(packageManager));
//        if (appInfoList != null){
//            appInfoList.clear();
//            for (ResolveInfo resolveInfo : resolveInfos){
//                String appName = (String) resolveInfo.loadLabel(packageManager);
//                String appSize = "无法获取";
//                String packageName = resolveInfo.activityInfo.packageName;
//                String installTime = "无法获取" ;
//                //获取版本名
//                String editon = null;
//                try {
//                    editon = packageManager.getPackageInfo(packageName,PackageManager.GET_CONFIGURATIONS).versionName;
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//                //构建AppInfo对象
//                AppInfo appInfo = new AppInfo();
//                appInfo.setAppName(appName);
//                appInfo.setEdition(editon);
//                appInfo.setPackageName(packageName);
//                appInfo.setAppSize(appSize);
//                appInfo.setInstallTime(installTime);
//                //添加进列表中
//                appInfoList.add(appInfo);
//            }
//        }
//    }

    //获取所有的App信息
    public void getAppInfos(){
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i=0;i<packs.size();i++){
            PackageInfo p = packs.get(i);
            if (p.versionName == null){
                continue;
            }
            String dir = p.applicationInfo.publicSourceDir;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(new File(dir).lastModified());
            String appName = (String) p.applicationInfo.loadLabel(getPackageManager());
            String appSize =Long.toString(new File(dir).length());
            String packageName = p.packageName;
            String installTime = formatter.format(date);
            //获取版本名
            String editon = p.versionName;
//            try {
//                editon = packageManager.getPackageInfo(packageName,PackageManager.GET_CONFIGURATIONS).versionName;
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
            //构建AppInfo对象
            AppInfo appInfo = new AppInfo();
            appInfo.setAppName(appName);
            appInfo.setEdition(editon);
            appInfo.setPackageName(packageName);
            appInfo.setAppSize(appSize);
            appInfo.setInstallTime(installTime);
            //添加进列表中
            appInfoList.add(appInfo);
        }
    }

    //三方应用程序过滤器
    private boolean filterApp(ApplicationInfo applicationInfo){
        //代表被用户升级过的系统应用
        if ((applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
            return true;
        }
        //代表用户应用
        else if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
