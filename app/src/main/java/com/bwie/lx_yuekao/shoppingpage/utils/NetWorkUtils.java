package com.bwie.lx_yuekao.shoppingpage.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * date:2018/12/27
 * author:张自力(DELL)
 * function: 网络判断工具类
 */

public class NetWorkUtils {

    //进行网络判断
    public static boolean isNetWorkAvailable(Context context){
        boolean available = false;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo!=null){
            available = networkInfo.isAvailable();
        }
        return available;
    }

}
