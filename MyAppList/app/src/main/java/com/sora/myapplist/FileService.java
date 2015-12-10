package com.sora.myapplist;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sora on 2015/12/10.
 * 保存到SD卡中
 * 传递文件名，数据，实现在SD卡根目录下创建文件并将数据保存在该文件中的功能
 */

public class FileService {

    private Context context;

    //得到传入的上下文对象的引用
    public FileService(Context context) {
        this.context = context;
    }

    public void saveToSDCard(String filename,String filecontent) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory(),filename);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(filecontent.getBytes());
        outputStream.close();
    }
}
