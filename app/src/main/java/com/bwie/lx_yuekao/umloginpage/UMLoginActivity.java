package com.bwie.lx_yuekao.umloginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.lx_yuekao.R;
import com.bwie.lx_yuekao.shoppingpage.view.MainActivity;
import com.bwie.lx_yuekao.shoppingpage.xiangqingpage.XiangQingActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class UMLoginActivity extends AppCompatActivity {

    private TextView tvResult;
    private ImageView ivLogin;
    private ImageView ivShare;

    //A.定义装平台的容器
    public ArrayList<SnsPlatform> platforms = new ArrayList<SnsPlatform>();
    private SHARE_MEDIA[] list = {SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};
    private UMShareAPI mUMShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umlogin);
        //1 初始化数据
        initView();
        //A.三方平台,添加到遍历的集合中
        initPlatforms();

        //A.获取UM的对象
        mUMShareAPI = UMShareAPI.get(UMLoginActivity.this);

        //A.获取是否授权
        final boolean isauth = UMShareAPI.get(this).isAuthorize(this, platforms.get(0).mPlatform);

        //A.点击QQ的头像,进行授权
        ivLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isauth){
                    Toast.makeText(UMLoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                    mUMShareAPI.deleteOauth(UMLoginActivity.this, platforms.get(0).mPlatform,authListener);
                }else{
                    Toast.makeText(UMLoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                    mUMShareAPI.doOauthVerify(UMLoginActivity.this, platforms.get(0).mPlatform,authListener);
                }
                mUMShareAPI.getPlatformInfo(UMLoginActivity.this, platforms.get(0).mPlatform,authListener);
                //跳回原来线程
                Intent intent = new Intent(UMLoginActivity.this, XiangQingActivity.class);
                startActivity(intent);
            }
        });

        //B.分享的逻辑代码
        ImageView iv_share = (ImageView) findViewById(R.id.iv_share);

        final UMImage image = new UMImage(UMLoginActivity.this, "http://b.hiphotos.baidu.com/zhidao/pic/item/63d9f2d3572c11df28e42e30602762d0f703c2e8.jpg");//网络图片
        final UMImage imagelocal = new UMImage(this, R.mipmap.ic_launcher);
        imagelocal.setThumb(new UMImage(this, R.mipmap.ic_launcher));
        imagelocal.setTitle("易宸锋好帅");
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(UMLoginActivity.this).withMedia(image)
                        .setPlatform(platforms.get(0).mPlatform)
                        .setCallback(shareListener).share();

                new ShareAction(UMLoginActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withText("hello")
                        .setCallback(shareListener)
                        .share();
            }
        });
    }

    //A.
    private void initPlatforms() {
        //A.集合清空
        platforms.clear();
        //A.通过for循环,把数组数据添加到集合中
        for (SHARE_MEDIA e : list) {
            if (!e.toString().equals(SHARE_MEDIA.GENERIC.toString())) {
                platforms.add(e.toSnsPlatform());
            }
        }
    }

    //A.
    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调，可以用来处理等待框，或相关的文字提示
        }

        @Override//授权成功时回调
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            //获取用户授权后的信息
            Set<String> strings = data.keySet();
            data.get("profile_image_url");
            String temp="";
            for(String key: strings ){
                temp =temp +key +" :" +data.get(key) +"\n";
            }
            tvResult.setText(temp); //返回值
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(UMLoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(UMLoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    //A.
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    //B.分享的逻辑代码
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(UMLoginActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(UMLoginActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(UMLoginActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };
    /**
     * //1 初始化数据
     * */
    private void initView() {
        tvResult = (TextView) findViewById(R.id.tv_result);//返回值
        ivLogin = (ImageView) findViewById(R.id.iv_login);//QQ登录
        ivShare = (ImageView) findViewById(R.id.iv_share);//空间分享
    }
}
