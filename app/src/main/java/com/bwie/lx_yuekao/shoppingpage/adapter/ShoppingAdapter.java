package com.bwie.lx_yuekao.shoppingpage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwie.lx_yuekao.R;
import com.bwie.lx_yuekao.shoppingpage.bean.ShoppingBean;
import com.bwie.lx_yuekao.shoppingpage.utils.HttpsToHttpUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

/**
 * date:2018/12/26
 * author:张自力(DELL)
 * function:  商品展示适配器
 */

public class ShoppingAdapter extends XRecyclerView.Adapter<ShoppingAdapter.ViewHolder> {

    //定义一个接口
    public interface MLSSOnClickListener{
        //点击跳转
        void onChanger(String id);
    }
    private MLSSOnClickListener mlistener;
    public void setMLSSOnClickListener (MLSSOnClickListener listener){
        this.mlistener = listener;
    }

    private Context mContext;
    private List<ShoppingBean.DataBean> mDataBeanList;

    public ShoppingAdapter(Context context, List<ShoppingBean.DataBean> dataBeanList) {
        mContext = context;
        mDataBeanList = dataBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.shopping_adapter,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ShoppingBean.DataBean dataBean = mDataBeanList.get(position);
        if(dataBean!=null){
            //得到图片集
            String images = dataBean.getImages();
            //剪切
            String[] split = images.split("\\|");
            Glide.with(mContext).load(HttpsToHttpUtils.httpstohttp(split[0])).into(holder.mImg);
            holder.mTxtTitle.setText(dataBean.getTitle());
            holder.mTxtPrice.setText(dataBean.getPrice()+"");
            holder.mTxtPingLun.setText(dataBean.getSalenum()+"");
        }

        //点击回调
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onChanger(dataBean.getPid()+"");
            }
        });

        //长按删除
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDataBeanList.remove(position);
                notifyItemRemoved(position);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataBeanList.size();
    }

    class ViewHolder extends XRecyclerView.ViewHolder {

        private final ImageView mImg;
        private final TextView mTxtTitle;
        private final TextView mTxtPrice;
        private final TextView mTxtPingLun;

        public ViewHolder(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);//图像
            mTxtTitle = itemView.findViewById(R.id.txt_title);//标题
            mTxtPrice = itemView.findViewById(R.id.txt_price);//价格
            mTxtPingLun = itemView.findViewById(R.id.txt_pinglun);//评论
        }
    }

}
