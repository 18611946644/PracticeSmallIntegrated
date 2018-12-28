package com.bwie.lx_yuekao.shoppingpage.utils;

/**
 * date:2018/12/26
 * author:张自力(DELL)
 * function:  将网址的Https换成Http的工具类
 */

public class HttpsToHttpUtils {

   //写一个方法
    public static String httpstohttp(String url){
        return url.replace("https","http");
    }

}
