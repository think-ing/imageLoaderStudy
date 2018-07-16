package com.mzw.imageloaderstudy.imageLoader.cache;

import android.graphics.Bitmap;

/**
 * Created by think on 2018/7/12.
 */

public interface ImageCache {
    //取图片
    public Bitmap get(String url);
    //存图片
    public void put(String url,Bitmap bitmap);
}
