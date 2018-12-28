package com.bwie.lx_yuekao.shoppingpage.maplodingpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.maps.MapView;
import com.bwie.lx_yuekao.R;

public class MapLodingActivity extends AppCompatActivity {

    private MapView map;
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_loding);
        //初始化
        initView();
        //地图
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
    }


    private void initView() {
        map = (MapView) findViewById(R.id.map);
    }
}
