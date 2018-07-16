package com.mzw.imageloaderstudy.imageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mzw.imageloaderstudy.imageLoader.cache.impl.DoubleCache;
import com.mzw.imageloaderstudy.imageLoader.cache.ImageCache;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片加载类
 * 使用   mImageloader.displayImage(url,iv);
 * Created by think on 2018/7/12.
 */

public class Imageloader {

    //下载失败记录  失败后本次不重复下载
    private Map<String,String> tempMap = new HashMap<String,String>();

    private final int SUCCESS = 1;//下载成功
    private final int DEFAULT = 2;//使用默认图片
    private final int FAILED = 3;//使用失败图片
    private final int LOADING = 0;//下载中

    //默认图片和下载失败图片
    private int defaultImg = 0,failedImg = 0;
    //默认最大图片尺寸
    private int ivMaxW = 1080,ivMaxH = 1920;

    private Handler handler = new Handler();
    //缓存 默认使用双缓存
    private ImageCache mImageCache = new DoubleCache();
    //线程池，线程数量为CPU数量 一半
    private int nThreads = Runtime.getRuntime().availableProcessors() / 2;
    ExecutorService mExecutorService = Executors.newFixedThreadPool(nThreads <= 1 ? 1 : nThreads);
    /**
     * 注入缓存实现
     * MemoryCache 内存缓存
     * DiskCache sd卡缓存
     * DoubleCache 双缓存
     * new ImageCache 自定义实现
     * @param mImageCache
     */
    public void setImageCache(ImageCache mImageCache) {
        this.mImageCache = mImageCache;
    }

    public void setDefaultImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }

    public void setFailedImg(int failedImg) {
        this.failedImg = failedImg;
    }

    /**
     * 加载图片入口
     * @param url  网络全路径
     * @param imageView
     * @param progressBar loading， 不想用可传 NULL
     */
    public void displayImage(final String url, final ImageView imageView,final ProgressBar progressBar){
        Log.i("-----","---1---" + url);
        if(progressBar != null){
            progressBar.setTag(url);
        }
        imageView.setTag(url);
        Bitmap bitmap = mImageCache.get(url);
        if(bitmap != null){
            onLoadingComplete(bitmap,url,imageView,progressBar, SUCCESS);
            return;
        }
        //缓存中没有图片 去下载
        if(tempMap == null || TextUtils.isEmpty(tempMap.get(url))){
            onLoadingComplete(null,url,imageView, progressBar,DEFAULT);
            submitLoaderRequest(url,imageView,progressBar);
        }else{
            Log.i("-----","无效路径，不下载...");
            onLoadingComplete(null,url,imageView, progressBar,FAILED);
        }
    }

    private void submitLoaderRequest(final String url, final ImageView imageView,final ProgressBar progressBar) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url,imageView,progressBar);
                if(bitmap == null){
                    return;
                }else{
                    mImageCache.put(url,bitmap);
                }
            }
        };
        mExecutorService.submit(r);
    }

    private Bitmap downloadImage(final String imageUrl,final ImageView imageView,final ProgressBar progressBar) {
        Log.i("-----","下载图片：" + imageUrl);
        onLoadingComplete(null,imageUrl,imageView,progressBar,LOADING);
        tempMap.put(imageUrl,imageUrl);
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            conn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            conn.setReadTimeout(8 * 1000);
            conn.setRequestMethod("GET");
            conn.connect();

            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            Log.i("-----","原图高度：" + h + "宽度:" + w);
            double inSampleSize = 1;
            if(w > ivMaxW || h > ivMaxH){//图片太大会导致 OOM
                inSampleSize = Math.max(w / ivMaxW , h / ivMaxH);
            }

            int dstWidth = (int) (w / inSampleSize);
            int dstHeight = (int) (h / inSampleSize);
            bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);
            int w1 = bitmap.getWidth();
            int h1 = bitmap.getHeight();
//            Log.i("-----","缩放图高度：" + h1 + "宽度:" + w1 + " , 缩放：" + inSampleSize);
            conn.disconnect();
        } catch (IOException e) {
            bitmap = null;
            Log.i("-----","下载图片错误：" + e.getMessage());
            e.printStackTrace();
        }
        if(bitmap != null){
            tempMap.remove(imageUrl);
            onLoadingComplete(bitmap,imageUrl,imageView,progressBar, SUCCESS);
        }else{
            onLoadingComplete(bitmap,imageUrl,imageView,progressBar, FAILED);
        }
        return bitmap;
    }


    public void onLoadingComplete(final Bitmap bitmap,final String var1, final ImageView var2,final ProgressBar progressBar,final int sign) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if(var2.getTag().equals(var1)){
                    switch (sign){
                        case LOADING:
                            Log.i("-----","-=-=-=-=-=-1=-=-=-=-=");
                            if(progressBar != null && progressBar.getTag().equals(var1)){
                                progressBar.setVisibility(View.VISIBLE);
                            }
                            break;
                        case SUCCESS:
                            if(progressBar != null && progressBar.getTag().equals(var1)){
                                progressBar.setVisibility(View.GONE);
                            }
                            var2.setImageBitmap(bitmap);
                            break;
                        case DEFAULT:
                            if(progressBar != null && progressBar.getTag().equals(var1)){
                                progressBar.setVisibility(View.GONE);
                            }
                            var2.setImageResource(defaultImg);
                            break;
                        case FAILED:
                            if(progressBar != null && progressBar.getTag().equals(var1)){
                                progressBar.setVisibility(View.GONE);
                            }
                            var2.setImageResource(failedImg);
                             break;
                    }
                }
            }
        };
        handler.post(r);
    }
}
