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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView = null;
    private List<AppInfo> appInfoList = null;
    private ProgressBar progressBar = null;
    private Toolbar toolbar = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        init();
        AppService appService = new AppService(getApplicationContext());
        //获取App信息
        appService.createAppList();
        appInfoList = appService.getAppInfoList();
        //将list加载到适配器
        AppInfoAdapter appInfoAdapter = new AppInfoAdapter(this, appInfoList);
        //显示程序列表
        listView.setAdapter(appInfoAdapter);
        //监听Toolbar按钮点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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
        });
//        设置click事件
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
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
        String filename = "My AppList.txt";
        String filecontent = "";
        for (int i=0;i<appInfoList.size();i++){
            //获取App的Label
            String appName = appInfoList.get(i).getAppName();
            //获取App的大小
            String appSize = appInfoList.get(i).getAppSize();
            //获取App对应的包名
            String packageName = appInfoList.get(i).getPackageName();
            //获取App的安装时间
            String installTime = appInfoList.get(i).getInstallTime();
            //获取版本名
            String editon = appInfoList.get(i).getEdition();
            filecontent += (i + 1) + ":   " + appName + "    " + editon + "    " + appSize + "    " + packageName + "    " + installTime+"  \n";
        }
        if (isExisted){
            service.saveToSDCard(filename,filecontent);
        }
        Toast.makeText(getApplicationContext(), "已导出程序列表", Toast.LENGTH_SHORT).show();
    }

    //将程序列表复制到剪贴板
    private void copyFile() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

    }

    //显示toolbar内容
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
