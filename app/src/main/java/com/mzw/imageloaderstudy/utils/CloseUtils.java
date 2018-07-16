package com.mzw.imageloaderstudy.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 所有实现Closeable接口的类 都可使用此类关闭
 * 避免代码中过多的 if 和 try catch
 * Created by think on 2018/7/12.
 */

public final class CloseUtils {
    private CloseUtils() {
    }

    /**
     * 关闭closeable对象
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
