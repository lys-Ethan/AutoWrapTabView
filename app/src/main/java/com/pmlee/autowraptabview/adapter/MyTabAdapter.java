package com.pmlee.autowraptabview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pmlee.autowraptabview.bean.Hobby;
import com.pmlee.autowraptabview.bean.MyRoundRectShape;
import com.pmlee.autowraptabview.utils.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyunshuang on 2018/4/20.
 * <p>
 * Email 522940943@qq.com or liyunshuang21@gmail.com
 */
public class MyTabAdapter extends AutoWrapAdapter<Hobby> {
    private Context mContext;
    private int category;
    private boolean sameStyle;
    private OnItemClickListener mOnItemClickListener;


    public MyTabAdapter(List<Hobby> datas, Context mContext, int category) {
        super(datas);
        this.mContext = mContext;
        this.category = category;
        initData();
        Map<String,String> val = new HashMap<>();
    }

    public MyTabAdapter(List<Hobby> datas, Context mContext, int category, boolean sameStyle) {
        super(datas);
        this.mContext = mContext;
        this.category = category;
        this.sameStyle = sameStyle;
        initData();
    }


    private void initData() {

    }

    /**
     * 创建背景图片
     * @param needSolid 是否需要实体填充
     * @param category 种类
     * @return
     */
    protected Drawable createBac(boolean needSolid,int category) {
        float radii = (float) AppUtil.dip2px(mContext, 4);
        float[] outerRadii = new float[]{radii, radii, radii, radii, radii, radii, radii, radii};
        float width = AppUtil.dip2px(mContext, 1)/2;
        RectF inside = new RectF(width, width, width, width);
        MyRoundRectShape rr = new MyRoundRectShape(outerRadii, needSolid ? null : inside, outerRadii, width);
        ShapeDrawable drawable = new ShapeDrawable(rr);
        Paint drawablePaint = drawable.getPaint();
        drawablePaint.setStyle(Paint.Style.FILL);
        drawablePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        drawablePaint.setColor(getSolidColor(category, needSolid));
        return drawable;
    }

    /**
     * 设置条目点击事件
     * @param mOnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (datas != null)
            return datas.size();
        else
            return 0;
    }

    @Override
    public void onBindView(View targetView, int position) {
        TextView view = (TextView) targetView;
        Hobby hobby = datas.get(position);
        view.setText(hobby.getName());
        if (sameStyle) {
            view.setBackground(createBac(hobby.isSame(),hobby.getCategory()));
            view.setTextColor(getTextColor(hobby.getCategory(), hobby.isSame()));
        } else {
            view.setBackground(createBac(false,hobby.getCategory()));
            view.setTextColor(getTextColor(hobby.getCategory(), false));
        }
    }

    @Override
    public View createView(final Context context, final int position) {
        TextView textView = new TextView(context);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onClicked(view,position);
                }
            }
        });
        textView.setPadding(AppUtil.dip2px(mContext, 4),
                AppUtil.dip2px(mContext, 4),
                AppUtil.dip2px(mContext, 4),
                AppUtil.dip2px(mContext, 4));
        return textView;
    }

    /**
     * 根据类型返回对应的颜色
     *
     * @param flag      标签的种类
     * @param needSolid 是否为填充
     * @return
     */
    protected int getTextColor(int flag, boolean needSolid) {
        if (needSolid) {
            return Color.WHITE;
        } else {
            switch (flag) {
                case 0:
                    return Color.parseColor("#9CCDD8");
                case 1:
                    return Color.parseColor("#A0D3C7");
                case 2:
                    return Color.parseColor("#BDBBDF");
                case 3:
                    return Color.parseColor("#DEB0A4");
                case 4:
                    return Color.parseColor("#CCD596");
                case 5:
                    return Color.parseColor("#DDC4A7");
                case 6:
                    return Color.parseColor("#D8AFB8");
            }
            return 0;
        }
    }

    /**
     * 获取边框颜色
     *
     * @param flag      标签种类
     * @param needSolid 是否为填充
     * @return
     */
    protected int getSolidColor(int flag, boolean needSolid) {
        if (needSolid) {
            switch (flag) {
                case 0:
                    return Color.parseColor("#A0DEEB");
                case 1:
                    return Color.parseColor("#A7EDDD");
                case 2:
                    return Color.parseColor("#C1BFEE");
                case 3:
                    return Color.parseColor("#F2BCAE");
                case 4:
                    return Color.parseColor("#DBEC7C");
                case 5:
                    return Color.parseColor("#F1D4A2");
                case 6:
                    return Color.parseColor("#EEBAC6");
            }
            return 0;
        } else {
            switch (flag) {
                case 0:
                    return Color.parseColor("#BEE1E8");
                case 1:
                    return Color.parseColor("#C1E9E0");
                case 2:
                    return Color.parseColor("#CECDEB");
                case 3:
                    return Color.parseColor("#EEC7BD");
                case 4:
                    return Color.parseColor("#E0EB9F");
                case 5:
                    return Color.parseColor("#EDD8B4");
                case 6:
                    return Color.parseColor("#EAC7CF");
            }
            return 0;
        }
    }

    /**
     * 定制条目点击事件
     */
    public interface OnItemClickListener{
        void onClicked(View view,int position);
    }
}
