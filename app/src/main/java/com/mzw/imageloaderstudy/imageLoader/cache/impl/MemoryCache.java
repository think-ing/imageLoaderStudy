package com.mzw.imageloaderstudy.imageLoader.cache.impl;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.mzw.imageloaderstudy.imageLoader.cache.ImageCache;
import com.mzw.imageloaderstudy.utils.MD5Util;

/**
 * 内存缓存
 * Created by think on 2018/7/12.
 */

public class MemoryCache implements ImageCache {
    LruCache<String,Bitmap> mImageCache;
    public MemoryCache() {
        initImageCache();
    }

    private void initImageCache() {
        //计算可使用的最大内存
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        //取四分之一可用内存做为缓存
        final int cacheSize = maxMemory / 4;
        mImageCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap get(String url) {
        return mImageCache.get(MD5Util.md5(url));
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        mImageCache.put(MD5Util.md5(url),bitmap);
    }
}
