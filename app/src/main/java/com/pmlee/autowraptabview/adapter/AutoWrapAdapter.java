package com.pmlee.autowraptabview.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by liyunshuang on 2018/4/16.
 * <p>
 * Email 522940943@qq.com or liyunshuang21@gmail.com
 */
public abstract class AutoWrapAdapter<T> {
    protected List<T> datas;

    public AutoWrapAdapter(List<T> datas) {
        this.datas = datas;
    }

    public AutoWrapAdapter() {
    }

    /**
     * 获取数据大小
     *
     * @return
     */
    public abstract int getItemCount();

    /**
     * 获取对应位置的数据
     *
     * @param position 对应条目位置
     * @return
     */
    public T getItem(int position) {
        if (datas != null)
            return datas.get(position);
        else
            return null;
    }

    /**
     * 设置数据源
     */
    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    /**
     * 进行数据绑定
     *
     * @param position 对应条目位置
     */
    public abstract void onBindView(View targetView, int position);

    /**
     * 填充视图
     * @param context 上下文
     * @param position 对应条目位置
     * @return
     */
    public abstract View createView(Context context, int position);


}
