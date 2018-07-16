package com.mzw.imageloaderstudy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mzw.imageloaderstudy.imageLoader.Imageloader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;
    private LayoutInflater layoutInflater;

    private Imageloader mImageloader;
    private Context mContext;
    private MyAdapter mMyAdapter;
    List<String> imgList = new ArrayList<String>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    int sign = msg.arg1;
                    if(sign == 0){
                        imgList.clear();
                        imgList = (List<String>)msg.obj;
                    }else{
                        List<String> list = (List<String>)msg.obj;
                        imgList.addAll(list);
                    }
                    mMyAdapter.setList(imgList);
                    mMyAdapter.notifyDataSetChanged();
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        layoutInflater = getLayoutInflater();

        listView = findViewById(R.id.id_listView);


        mImageloader = new Imageloader();
        mImageloader.setDefaultImg(R.mipmap.default_img);
        mImageloader.setFailedImg(R.mipmap.failed_img);


        mMyAdapter = new MyAdapter();
        listView.setAdapter(mMyAdapter);

//        listView.setOnScrollListener(new AbsListView.OnScrollListener(){
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if((firstVisibleItem + visibleItemCount) >= totalItemCount){
//                    //加载更多
//                    getImages(1);
//                }
//            }
//        });
        getImages(0);
    }

    public class MyAdapter extends BaseAdapter{
        List<String> list;

        public void setList(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String url = list.get(position);

            final ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                assert convertView != null;
                holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            mImageloader.displayImage(url,holder.imageView,holder.progressBar);

            return convertView;
        }
    }
    class ViewHolder{
        ImageView imageView;
        ProgressBar progressBar;
    }

    private void getImages(int sign){
        List<String> list = new ArrayList<String>();

        list.add("http://hbimg.b0.upaiyun.com/225a5f3f75d1d4c59532704782eebd25d323fd801e57a-VlY5c4_fw658");
        list.add("http://img0.imgtn.bdimg.com/it/u=1289259320,3462539789&fm=214&gp=0.jpg");
        list.add("http://s10.sinaimg.cn/mw690/006LDoUHzy7auXEvyzLd9&690");
        list.add("http://dynamic-image.yesky.com/480x-/uploadImages/2015/287/01/9E78UUBDI92Q.jpg");
        list.add("http://s1.sinaimg.cn/mw690/006LDoUHzy7auXElZGE40&690");

        list.add("http://images.china.cn/attachement/jpg/site1000/20160629/d02788e9b33e18dd5d4d2a.jpg");
        list.add("http://img.zcool.cn/community/0121bb5608950d32f875a132611ce7.jpg@1280w_1l_2o_100sh.jpg");

        list.add("http://s3.sinaimg.cn/middle/8ee3e0acxb0171b67a2d2&690");
        list.add("http://s13.sinaimg.cn/bmiddle/7e489919tadf50ece79fc&690");
        list.add("http://s2.sinaimg.cn/mw690/006LDoUHzy7auXttGlbd1&690");
        list.add("http://s10.sinaimg.cn/mw690/006LDoUHzy7auXJBwj7e9&690");
        list.add("http://5b0988e595225.cdn.sohucs.com/images/20170811/3be26d2a08a144b6a3a252e93984c049.jpeg");
        list.add("http://s7.sinaimg.cn/middle/8ee3e0acxb0171ba4aec6&690");
        list.add("http://img.19196.com/uploads/151125/9-151125103F5930.jpg");
        list.add("http://www.192.168.1.158/uploads/151125/9");

        Message msg = new Message();
        msg.what = 1;
        msg.obj = list;
        msg.arg1 = sign;
        mHandler.sendMessage(msg);
    }
}
