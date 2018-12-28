package com.bwie.lx_yuekao.shoppingpage.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bwie.lx_yuekao.R;
import com.bwie.lx_yuekao.db.HuanCunBeanDao;
import com.bwie.lx_yuekao.shoppingpage.adapter.ShoppingAdapter;
import com.bwie.lx_yuekao.shoppingpage.application.MyApplication;
import com.bwie.lx_yuekao.shoppingpage.bean.HuanCunBean;
import com.bwie.lx_yuekao.shoppingpage.bean.ShoppingBean;
import com.bwie.lx_yuekao.shoppingpage.maplodingpage.MapLodingActivity;
import com.bwie.lx_yuekao.shoppingpage.presenter.ShopPresenter;
import com.bwie.lx_yuekao.shoppingpage.utils.NetWorkUtils;
import com.bwie.lx_yuekao.shoppingpage.xiangqingpage.XiangQingActivity;
import com.bwie.lx_yuekao.shoppingpage.xiangqingpage.bean.ShopXQBean;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IView, View.OnClickListener {

    private Button btnMap;
    private XRecyclerView xlvShop;
    private List<ShoppingBean.DataBean> mList;
    private ShoppingAdapter mShoppingAdapter;
    private ShopPresenter mShopPresenter;
    private HuanCunBeanDao mHuanCunBeanDao;
    private long mInsert;
    private ImageView imgBack;
    private SearchView searchview;
    private ImageView imgQiehuan;

    //切换使用的标识符
    private boolean isHV = false;//默认瀑布流
    private Button btnSearch;
    private XRecyclerView xlvSearchkey;

    //设置点击搜索的变量标识符
    boolean issearch = false;//默认不搜索  不展示搜索内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1 初始化数据
        initView();
        //2 点击搜索  进行搜索数据
        initSearchView();
        //3 初始化list 和  adapter
        initListAndAdapter();
        //6 数据库缓存
        initHuanCun();
        //2 初始化presenter
        initShopPresenter();
        //4 条目点击事件
        setOnItemClickListener();
    }

    /**
     * //2 点击搜索  进行搜索数据
     */
    //定义全局搜索内容
    String isSerachContent = null;

    private void initSearchView() {
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //点击搜索按钮
                Toast.makeText(MainActivity.this, "第一个哈哈", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //搜索的内容
                Toast.makeText(MainActivity.this, "第二个" + newText, Toast.LENGTH_SHORT).show();
                isSerachContent = newText;//将搜索内容赋值
                return true;
            }
        });

    }

    /**
     * //6 数据库缓存
     */
    private void initHuanCun() {
        mHuanCunBeanDao = MyApplication.getDaoSession().getHuanCunBeanDao();
    }

    /**
     * //4 条目点击事件
     */
    private void setOnItemClickListener() {
        mShoppingAdapter.setMLSSOnClickListener(new ShoppingAdapter.MLSSOnClickListener() {
            @Override
            public void onChanger(String id) {
                //进行跳转
                Intent intent = new Intent(MainActivity.this, XiangQingActivity.class);
                intent.putExtra("pid", id);
                startActivity(intent);
            }
        });
    }

    /**
     * //3 初始化list 和  adapter
     */
    private void initListAndAdapter() {
        mList = new ArrayList<>();
        //判断是否点击了搜索
        /*if(issearch){//有搜索
            mShoppingAdapter = new ShoppingAdapter(this, mList);
            xlvSearchkey.setAdapter(mShoppingAdapter);
        }else{//没有点击搜索
            mShoppingAdapter = new ShoppingAdapter(this, mList);
            xlvShop.setAdapter(mShoppingAdapter);
        }*/

        mShoppingAdapter = new ShoppingAdapter(this, mList);
        xlvShop.setAdapter(mShoppingAdapter);
        xlvSearchkey.setAdapter(mShoppingAdapter);
    }

    /**
     * //2 初始化presenter
     */
    private void initShopPresenter() {
        mShopPresenter = new ShopPresenter();
        mShopPresenter.attach(this);
        //先判断网络状态
        boolean b = NetWorkUtils.isNetWorkAvailable(MainActivity.this);
        if (b) {//如果网络可用  就请求网络
            mShopPresenter.getShopDataP();
        } else {//如果网络不可用  就进行数据库读取
            ShoppingBean shoppingBean = new ShoppingBean();
            List<HuanCunBean> huanCunBeans = mHuanCunBeanDao.loadAll();//查询所有
            for (int j = 0; j < huanCunBeans.size(); j++) {
                HuanCunBean huanCunBean = huanCunBeans.get(j);
                String detailUrl = huanCunBean.getDetailUrl();
                int pid = huanCunBean.getPid();
                String createtime = huanCunBean.getCreatetime();
                String images = huanCunBean.getImages();
                double price = huanCunBean.getPrice();
                String title = huanCunBean.getTitle();
                Toast.makeText(this, "" + pid + "====" + price + "=====" +
                                detailUrl + "====" + images + "====" + title + "===="
                        , Toast.LENGTH_LONG).show();
            }
        }


    }

    /**
     * //1 初始化数据
     */
    private void initView() {
        btnMap = (Button) findViewById(R.id.btn_map);
        xlvShop = (XRecyclerView) findViewById(R.id.xlv_shop);
        //设置xlvshop布局管理器
        //4 瀑布流  瀑布流管理器，只有两个参数的构造方法，第一个是列数，第二个是方向
        StaggeredGridLayoutManager layoutManager2 = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        xlvShop.setLayoutManager(layoutManager2);


        imgBack = (ImageView) findViewById(R.id.img_back);//返回
        searchview = (SearchView) findViewById(R.id.searchview);//搜索
        imgQiehuan = (ImageView) findViewById(R.id.img_qiehuan);//切换
        imgQiehuan.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
        //搜索展示的内容
        xlvSearchkey = (XRecyclerView) findViewById(R.id.xlv_searchkey);
        StaggeredGridLayoutManager layoutManager3 = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        xlvSearchkey.setLayoutManager(layoutManager3);//设置默认布局管理
        xlvSearchkey.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_map://点击跳转到map
                Intent intent = new Intent(MainActivity.this, MapLodingActivity.class);
                startActivity(intent);
                break;

            case R.id.img_back://点击返回
                //返回上一层
                finish();
                break;

            case R.id.img_qiehuan://点击切换
                //判断标识符
                if (isHV) {//如果是
                    StaggeredGridLayoutManager layoutManager2 = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                    xlvShop.setLayoutManager(layoutManager2);
                    isHV = false;
                } else {
                    //不是改变为V管理器
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    xlvShop.setLayoutManager(layoutManager);
                    //改变标识
                    isHV = true;
                }
                break;
            case R.id.btn_search://点击搜索
                //方法一: 得到数据  进行跳转传值
                /*Intent intent1 = new Intent(MainActivity.this, SearchContentActivity.class);
                intent1.putExtra("idserachkey",isSerachContent);
                startActivity(intent1);*/

                //方法二：进行传值搜索  直接Fragmente切换
                //搜索之后  进行隐藏展示
                if(issearch){//没有点击搜索  将布局切换过来
                    issearch = false;
                    mShopPresenter.getShopDataP();
                    //布局切换
                    xlvShop.setVisibility(View.VISIBLE);
                    xlvSearchkey.setVisibility(View.GONE);
                }else{//如果点击了搜索
                    issearch = true;//设置标识符
                    //搜索  访问数据
                    mShopPresenter.getSearchKeyP(isSerachContent);
                    //布局切换
                    xlvShop.setVisibility(View.GONE);
                    xlvSearchkey.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    /**
     * 实现IView接口 覆写他的方法
     */
    @Override
    public void ShoppingData(List<ShoppingBean.DataBean> list) {
        if (list != null) {
            mList.clear();
            mList.addAll(list);
            mShoppingAdapter.notifyDataSetChanged();

            HuanCunBean huanCunBean = new HuanCunBean();
            for (int i = 0; i < list.size(); i++) {
                ShoppingBean.DataBean dataBean = list.get(i);
                huanCunBean.setTitle(dataBean.getTitle());
                huanCunBean.setBargainPrice(dataBean.getPrice());
                huanCunBean.setCreatetime(dataBean.getCreatetime());
                huanCunBean.setDetailUrl(dataBean.getImages());
                huanCunBean.setPid(dataBean.getPid());
                huanCunBean.setPrice(dataBean.getPrice());
                //添加进数据库
                mInsert = mHuanCunBeanDao.insert(huanCunBean);
            }

            if (mInsert != 0) {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void ShoppingXQData(ShopXQBean shopXQBean) {
        //在详情界面使用即可
    }

    @Override
    public void SearchKeyData(List<ShoppingBean.DataBean> list) {
        //搜索成功得到的数据
        if (list != null) {
            mList.clear();
            mList.addAll(list);
            mShoppingAdapter.notifyDataSetChanged();

            HuanCunBean huanCunBean = new HuanCunBean();
            for (int i = 0; i < list.size(); i++) {
                ShoppingBean.DataBean dataBean = list.get(i);
                huanCunBean.setTitle(dataBean.getTitle());
                huanCunBean.setBargainPrice(dataBean.getPrice());
                huanCunBean.setCreatetime(dataBean.getCreatetime());
                huanCunBean.setDetailUrl(dataBean.getImages());
                huanCunBean.setPid(dataBean.getPid());
                huanCunBean.setPrice(dataBean.getPrice());
                //添加进数据库
                mInsert = mHuanCunBeanDao.insert(huanCunBean);
            }

            if (mInsert != 0) {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            }

        }
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
