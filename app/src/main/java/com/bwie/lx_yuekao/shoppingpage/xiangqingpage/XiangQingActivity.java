package com.bwie.lx_yuekao.shoppingpage.xiangqingpage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bwie.lx_yuekao.R;
import com.bwie.lx_yuekao.shoppingpage.adapter.XQLBAdapter;
import com.bwie.lx_yuekao.shoppingpage.bean.ShoppingBean;
import com.bwie.lx_yuekao.shoppingpage.presenter.ShopPresenter;
import com.bwie.lx_yuekao.shoppingpage.utils.HttpsToHttpUtils;
import com.bwie.lx_yuekao.shoppingpage.view.IView;
import com.bwie.lx_yuekao.shoppingpage.xiangqingpage.bean.ShopXQBean;
import com.bwie.lx_yuekao.umloginpage.UMLoginActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class XiangQingActivity extends AppCompatActivity implements IView, View.OnClickListener {

    private SimpleDraweeView simpleTouxiang;
    private ViewPager vp;
    private TextView txtTitle;
    private TextView txtContent;
    private TextView txtPrice;
    private Button btnAddcar;
    private ShopPresenter mShopPresenter;
    private String mPid;
    private ShopXQBean mShopXQBean;
    private XQLBAdapter mXqlbAdapter;


    //轮播图片Handler发送
    private int FLAG = 1001;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == FLAG) {
                //得到当前轮播图的条目(不会提示补充)
                int item = vp.getCurrentItem();
                //重新为vp设置当前条目
                vp.setCurrentItem(item);
                //延迟发送
                handler.sendEmptyMessageDelayed(FLAG, 2000);
            }
        }
    };
    private ImageView imgLunbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiang_qing);
        //初始化
        initView();
        Intent intent = getIntent();
        mPid = intent.getStringExtra("pid");
        //2 初始化presenter
        initShopPresenter();
        //3 adapter
        initAdapter();
    }

    /**
     * //3 添加adapter
     */
    private void initAdapter() {
        vp.setAdapter(mXqlbAdapter);
        //延迟发送
        handler.sendEmptyMessageDelayed(FLAG, 2000);
    }

    /**
     * //2 初始化presenter
     */
    private void initShopPresenter() {
        mShopPresenter = new ShopPresenter();
        mShopPresenter.attach(this);
        mShopPresenter.getShopXQDataP(mPid);
    }

    private void initView() {
        simpleTouxiang = (SimpleDraweeView) findViewById(R.id.simple_touxiang);
        vp = (ViewPager) findViewById(R.id.vp);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtContent = (TextView) findViewById(R.id.txt_content);
        txtPrice = (TextView) findViewById(R.id.txt_price);
        btnAddcar = (Button) findViewById(R.id.btn_addcar);

        btnAddcar.setOnClickListener(this);
        imgLunbo = (ImageView) findViewById(R.id.img_lunbo);
        imgLunbo.setOnClickListener(this);
        simpleTouxiang.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.simple_touxiang://点击头像 跳转登录
                Intent intent = new Intent(XiangQingActivity.this, UMLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_addcar://加入购物车

                break;
        }
    }

    @Override
    public void ShoppingData(List<ShoppingBean.DataBean> list) {
        //没用
    }

    @Override
    public void ShoppingXQData(ShopXQBean shopXQBean) {
        //详情数据
        if (shopXQBean != null) {
            //得到图片集
            mXqlbAdapter = new XQLBAdapter(this, shopXQBean);
            //赋值
            String images = shopXQBean.getData().getImages();
            String[] split = images.split("\\|");
            Glide.with(this).load(HttpsToHttpUtils.httpstohttp(split[0])).into(imgLunbo);

            txtTitle.setText(shopXQBean.getData().getTitle());
            txtContent.setText(shopXQBean.getData().getSubhead());
            txtPrice.setText(shopXQBean.getData().getPrice() + "");
        }

    }

    @Override
    public void SearchKeyData(List<ShoppingBean.DataBean> list) {
        //只在搜索页 进行使用
    }

    @Override
    public void Failder(Exception e) {
        Toast.makeText(this, "网络请求错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShopPresenter != null) {
            mShopPresenter.datach();
        }
    }
}
