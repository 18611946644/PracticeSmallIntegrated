package com.bwie.lx_yuekao.shoppingpage.utils;

import android.os.Handler;

import com.bwie.lx_yuekao.shoppingpage.item.ICallBack;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * date:2018/12/26
 * author:张自力(DELL)
 * function:  网络请求工具类
 */

public class OKHttpUtils {

    //使用单例模式
    public static volatile OKHttpUtils instance;
    public Handler handler = new Handler();
    public OkHttpClient client;
    public OKHttpUtils(){
        //构造方法
        client = new OkHttpClient();
    }
    public static OKHttpUtils getInstance(){
        if(instance==null){
            synchronized (OKHttpUtils.class){
                if(null == instance){
                    instance = new OKHttpUtils();
                }
            }
        }
       return instance;
    }

    //创建请求数据方法
    public void getData(String url, final ICallBack callBack, final Type type){

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //失败时
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailder(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                 //成功
                //得到数据
                String result = response.body().string();
                //使用gson解析
                Gson gson = new Gson();
                final Object o = gson.fromJson(result, type);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(o);
                    }
                });
            }
        });
    }
}
