package com.bwie.lx_yuekao.shoppingpage.view;

import com.bwie.lx_yuekao.shoppingpage.bean.ShoppingBean;
import com.bwie.lx_yuekao.shoppingpage.xiangqingpage.bean.ShopXQBean;

import java.util.List;

/**
 * date:2018/12/26
 * author:张自力(DELL)
 * function:  商品的接口
 */

public interface IView {

    //商品
    void ShoppingData(List<ShoppingBean.DataBean> list);
    //商品详情
    void ShoppingXQData(ShopXQBean shopXQBean);
    //商品搜索
    void SearchKeyData(List<ShoppingBean.DataBean> list);
    void Failder(Exception e);

}
