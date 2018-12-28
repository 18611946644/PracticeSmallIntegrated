package com.bwie.lx_yuekao.shoppingpage.presenter;


import com.bwie.lx_yuekao.shoppingpage.bean.ShoppingBean;
import com.bwie.lx_yuekao.shoppingpage.item.ICallBack;
import com.bwie.lx_yuekao.shoppingpage.model.MyModel;
import com.bwie.lx_yuekao.shoppingpage.view.IView;
import com.bwie.lx_yuekao.shoppingpage.xiangqingpage.bean.ShopXQBean;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * date:2018/12/26
 * author:张自力(DELL)
 * function:  购物车的Presenter层
 */

public class ShopPresenter {

    //初始化IView  和  Model
    private IView mIView;
    private MyModel mMyModel;

    //关联
    public void attach(IView iView){
        mIView = iView;
        mMyModel = new MyModel();
    }

    //商品请求方法
    public void getShopDataP(){
        Type type = new TypeToken<ShoppingBean>(){}.getType();
        //通过M调用网络请求方法
        mMyModel.getShoppingDataM("http://www.zhaoapi.cn/product/searchProducts?keywords=%E7%AC%94%E8%AE%B0%E6%9C%AC&page=1", new ICallBack() {
            @Override
            public void onSuccess(Object o) {
                //成功
                ShoppingBean shoppingBean = (ShoppingBean) o;
                mIView.ShoppingData(shoppingBean.getData());
            }

            @Override
            public void onFailder(Exception e) {
                //失败
                mIView.Failder(e);
            }
        },type);
    }

    //搜索请求方法  需要传keywords
    public void getSearchKeyP(String keywords){
        //进行地址拼接
        String url = "http://www.zhaoapi.cn/product/searchProducts?page=1&keywords="+keywords;
        Type type = new TypeToken<ShoppingBean>(){}.getType();
        //通过M调用网络请求方法
        mMyModel.getShoppingDataM(url, new ICallBack() {
            @Override
            public void onSuccess(Object o) {
                //成功
                ShoppingBean shoppingBean = (ShoppingBean) o;
                mIView.ShoppingData(shoppingBean.getData());
            }

            @Override
            public void onFailder(Exception e) {
                //失败
                mIView.Failder(e);
            }
        },type);
    }

    //商品详情请求方法
    public void getShopXQDataP(String pid){
        Type type = new TypeToken<ShopXQBean>(){}.getType();
        String url = "http://www.zhaoapi.cn/product/getProductDetail?pid="+pid;
        //通过M调用网络请求方法
        mMyModel.getShoppingDataM(url, new ICallBack() {
            @Override
            public void onSuccess(Object o) {
                //成功
                ShopXQBean shopXQBean = (ShopXQBean) o;
                mIView.ShoppingXQData(shopXQBean);
            }

            @Override
            public void onFailder(Exception e) {
                //失败
                mIView.Failder(e);
            }
        },type);
    }


    //解耦
    public void datach(){
        if(mIView!=null){
            mIView = null;
        }
    }

}
