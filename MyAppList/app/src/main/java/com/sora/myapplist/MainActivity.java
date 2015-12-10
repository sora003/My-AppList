package com.sora.myapplist;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView = null;
    private List<AppInfo> appInfoList = null;
    private List<AppInfo> history_appInfoList = null;
    private ProgressBar progressBar = null;
    private Toolbar toolbar = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        init();
        //TODO 读取HistoryAppList
        //TODO 显示HistoryAppInfoList UI
        //读取当前安装App 构造SystemAppInfoList
        makeSystemAppInfoList();
        //TODO 合并HistoryAppList和SystemAppInfoList,生成AppInfoList
        //将AppInfoList加载到适配器
        AppInfoAdapter appInfoAdapter = new AppInfoAdapter(this, appInfoList);
        //显示App列表
        listView.setAdapter(appInfoAdapter);
        //TODO  更新HistoryAPPInfoList 用AppInfoList代替 赋值有风险！
        history_appInfoList = appInfoList;
        //写入HistoryAppInfoList
        try {
            saveHistory_appInfoList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO AppList排序按钮的监听
        //监听Toolbar按钮点击事件
        toolbar.setOnMenuItemClickListener(new toolbar_OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return super.onMenuItemClick(item);
            }
        });
//        设置click事件
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

    }

    private void saveHistory_appInfoList() throws IOException {
        //判断SDCard是否存在并且可读写
        Boolean isExisted = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        //新建FileService对象
        FileService service = new FileService(getApplicationContext());
        //文件名
        List<AppInfo> fileList = history_appInfoList;
        if (isExisted){
            service.saveToData(fileList);
        }
        Toast.makeText(MainActivity.this, "已导出程序列表", Toast.LENGTH_SHORT).show();
    }


    public void init() {
        //声明toolbar控件
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //创建进度条对象
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //创建ListView对象
        listView = (ListView) findViewById(R.id.app_listView);
        //创建List 用于装填App信息
        appInfoList = new ArrayList<AppInfo>();
        history_appInfoList = new ArrayList<AppInfo>();
    }

    //读取当前系统安装App 构造SystemAppInfoList
    public void makeSystemAppInfoList() {
        //获取已安装的应用程序包
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        //非三方程序序列号
        int app_id = 0;
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
            //确认将会添加到程序列表,id+1
            app_id++;
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
            String appid = Integer.toString(app_id);
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

    //导入程序列表
    private void importFile() {

    }

    //导出程序列表
    private void exportFile() throws IOException {
        //判断SDCard是否存在并且可读写
        Boolean isExisted = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        //新建FileService对象
        FileService service = new FileService(getApplicationContext());
        //文件名
        List<AppInfo> fileList = appInfoList;
        if (isExisted){
            service.saveToSDCard(fileList);
        }
        Toast.makeText(MainActivity.this, "已导出程序列表", Toast.LENGTH_SHORT).show();
    }

    //将AppInfoList复制到剪贴板
    private void copyFile() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(getAppInfoList());
    }

    //获取AppInfoList的内容
    private String getAppInfoList(){
        String app_list = "";
        for (int i=0;i<appInfoList.size();i++){
            //获取App的id
            String appid = appInfoList.get(i).getAppid();
            //获取App的Label
            String appName = appInfoList.get(i).getAppName();
            //获取App的大小
            String appSize = appInfoList.get(i).getAppSize();
            //获取App对应的包名
            String packageName = appInfoList.get(i).getPackageName();
            //获取App的安装时间
            String installTime = appInfoList.get(i).getInstallTime();
            //获取版本名
            String edition = appInfoList.get(i).getEdition();
            app_list += appid + ":   " + appName + "    " + edition + "    " + appSize + "    " + packageName + "    " + installTime+"  \n";
        }
        return app_list;
    }

    //显示toolbar内容
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //inner class ->监听Toolbar按钮点击事件
    private class toolbar_OnMenuItemClickListener implements Toolbar.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                //用户点击了copy功能键
                case R.id.action_copy:
                    copyFile();
                    Toast.makeText(MainActivity.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                    break;
                //用户点击了export功能键
                case R.id.action_export:
                    try {
                        exportFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                //用户点击了import功能键
                case R.id.action_import:
                    importFile();
                    Toast.makeText(MainActivity.this, "已导入程序列表", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    }
}
