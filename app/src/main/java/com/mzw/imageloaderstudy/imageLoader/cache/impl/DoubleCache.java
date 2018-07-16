package com.mzw.imageloaderstudy.imageLoader.cache.impl;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.mzw.imageloaderstudy.imageLoader.cache.ImageCache;

/**
 * 双缓存
 * Created by think on 2018/7/12.
 */

public class DoubleCache implements ImageCache {
    MemoryCache mMemoryCache = new MemoryCache();
    DiskCache mDiskCache = new DiskCache();

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        if(bitmap == null){
            Log.i("-----","内存中没有缓存...");
            bitmap = mDiskCache.get(url);
            if(bitmap == null){
                Log.i("-----","SD中没有缓存...");
            }else{
                Log.i("-----","返回SD中缓存..."+bitmap);
            }
        }else{
            Log.i("-----","返回内存中缓存..."+bitmap);
        }
        return bitmap;
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        if(!TextUtils.isEmpty(url) && bitmap != null){
            mMemoryCache.put(url,bitmap);
            mDiskCache.put(url,bitmap);
        }
    }
}
