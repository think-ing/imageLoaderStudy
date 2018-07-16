package com.mzw.imageloaderstudy.imageLoader.cache.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.mzw.imageloaderstudy.utils.CloseUtils;
import com.mzw.imageloaderstudy.utils.MD5Util;
import com.mzw.imageloaderstudy.imageLoader.cache.ImageCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * sd缓存
 * Created by think on 2018/7/12.
 */

public class DiskCache implements ImageCache {
    // 缓存地址
    static String cacheDir = getSDPath();

    //从缓存中取图片
    @Override
    public Bitmap get(String url) {
        String suffix = url.trim().substring(url.lastIndexOf(".") + 1,url.length()).toLowerCase();
        if(TextUtils.isEmpty(suffix) || suffix.length() > 4){
            suffix = "jpg";
        }

        return BitmapFactory.decodeFile(cacheDir + MD5Util.md5(url) + "." + suffix);
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        String suffix = url.trim().substring(url.lastIndexOf(".") + 1,url.length()).toLowerCase();
        if(TextUtils.isEmpty(suffix) || suffix.length() > 4){
            suffix = "jpg";
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(cacheDir + MD5Util.md5(url) + "." + suffix);
            Bitmap.CompressFormat format = suffix.equals("png") ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;
            Boolean b = bitmap.compress(format,100,fos);
//            Log.i("-----","-=-=-保存图片到sd-=-=-" + b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            CloseUtils.closeQuietly(fos);
        }
    }

    /**
     * 获取SD卡地址
     * @return
     */
    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist){
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }

        if(sdDir != null) {
            String cacheDir = sdDir.toString() + "/imageCache/";
            //新建一个File，传入文件夹目录
            File file = new File(cacheDir);
            //判断文件夹是否存在，如果不存在就创建，否则不创建
            if (!file.exists()) {
                //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
                boolean b =  file.mkdirs();
            }
            return cacheDir;
        }
        return null;
    }
}
