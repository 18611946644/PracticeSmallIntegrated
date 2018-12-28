package com.bwie.lx_yuekao.shoppingpage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bwie.lx_yuekao.shoppingpage.utils.HttpsToHttpUtils;
import com.bwie.lx_yuekao.shoppingpage.xiangqingpage.bean.ShopXQBean;


import java.util.List;

/**
 * date:2018/11/14
 * author:张自力(DELL)
 * function: 首页轮播的Adapter
 */

public class XQLBAdapter extends PagerAdapter{

    private Context mContext;
    private ShopXQBean mShopXQBean;

    public XQLBAdapter(Context context, ShopXQBean shopXQBean) {
        mContext = context;
        mShopXQBean = shopXQBean;
    }

    @Override
    public int getCount() {
        if(mShopXQBean==null){
            return 0;
        }
        return 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    //覆写两个方法
    //1 添加 的方法
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //定义一个imageView对象
        ImageView imageView = new ImageView(mContext);
        //使用Glid将数据imageView
        String[] split = mShopXQBean.getData().getImages().split("\\|");
        Glide.with(mContext).load(HttpsToHttpUtils.httpstohttp(split[0])).into(imageView);
        //加入
        container.addView(imageView);
        return imageView;
    }
    //2 删除的方法

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //记得要删除super
        /*super.destroyItem(container, position, object);*/
        container.removeView((View) object);
    }
}
