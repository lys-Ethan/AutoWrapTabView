package com.pmlee.autowraptabview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.pmlee.autowraptabview.R;
import com.pmlee.autowraptabview.adapter.AutoWrapAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyunshuang on 2018/4/16.
 * <p>
 * Email 522940943@qq.com or liyunshuang21@gmail.com
 * <p>
 * 自动换行标签控件
 */
public class AutoWrapTabView extends LinearLayout  {

    private Context context;
    /**
     * 适配器
     */
    private AutoWrapAdapter mAdapter;
    /**
     * 点击事件是否可用
     */
    private boolean clickEnable = false;
    /**
     * 条目集合
     */
    private List<View> mItems;
    /**
     * 条目数量
     */
    private int itemCount;
    /**
     * 横向间隔大小
     */
    private int hGashSize;
    /**
     * 纵向间隔大小
     */
    private int vGashSize;
    /**
     * 是否填充完成
     */
    private boolean hasChanged;

    /**
     * 间距类型，分为横向和纵向
     */
    public enum GashTYpe {
        HORIZONTAL, VERTICAL
    }

    public AutoWrapTabView(Context context) {
        this(context, null);

    }

    public AutoWrapTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoWrapTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        if (attrs!=null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.auto_wrap_tabview);
            hGashSize = typedArray.getDimensionPixelSize(R.styleable.auto_wrap_tabview_horizontal_dash,0);
            vGashSize = typedArray.getDimensionPixelSize(R.styleable.auto_wrap_tabview_vertical_dash,0);
            typedArray.recycle();
        }
        initView();
    }

    /**
     * 数据初始化
     */
    private void initView() {
        mItems = new ArrayList<>();
        this.setOrientation(VERTICAL);
    }


    /**
     * 只能是纵向布局
     * @param orientation 无效
     */
    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(AutoWrapAdapter adapter) {
        this.mAdapter = adapter;
        buildView();
    }

    /**
     * 构建视图
     */
    private void buildView() {
        if (mAdapter == null)
            return;
        mItems.clear();
        itemCount = mAdapter.getItemCount();
        //创建Item
        for (int i = 0; i < itemCount; i++) {
            View targetView = mAdapter.createView(context, i);
            //打上标签
            targetView.setTag(i);
            mItems.add(targetView);
            //数据绑定
            mAdapter.onBindView(targetView, i);
        }
        //重新布局
        removeAllViews();
        hasChanged = false;
        //请求重新摆放位置
        requestLayout();
    }

    /**
     * 设置间隙 需在调用setAdapter之前
     *
     * @param gashType 间隙类型
     * @param gashSize 间隙大小
     */
    public void setGash(GashTYpe gashType, int gashSize) {
        switch (gashType) {
            case VERTICAL:
                vGashSize = gashSize;
                break;
            case HORIZONTAL:
                hGashSize = gashSize;
                break;
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //视图填充
        if (!hasChanged) {
            int paddingLeft = this.getPaddingLeft();
            int paddingRight = this.getPaddingRight();
            Rect rawRect = new Rect(l, t, r, b);
            //绑定位置
            if (itemCount == 0)
                return;
            LinearLayout line1 = getNewParent(0);
            int maxWidth = rawRect.width()-paddingLeft-paddingRight;
            addChild(line1, 0, maxWidth, maxWidth);
            hasChanged = true;
        }
    }

    /**
     * 添加子控件
     *
     * @param parent     父容器
     * @param startIndex 开始索引
     * @param space      剩余空间
     * @param maxWidth   父容器最大宽度
     */
    private void addChild(LinearLayout parent, int startIndex, int space, int maxWidth) {
        if (startIndex >= itemCount)
            return;
        View view = mItems.get(startIndex);
        //测量子视图宽度
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int requestWidth = measuredWidth + hGashSize;
        if (space >= requestWidth) {
            //
            if (startIndex == 0) {
                parent.addView(view);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(measuredWidth,measuredHeight);
                params.leftMargin = hGashSize;
                parent.addView(view, params);
            }
            //重新计算宽度
            space -= requestWidth;
            addChild(parent, ++startIndex, space, maxWidth);
        } else {
            //超出长度新增一行
            LinearLayout newParent = getNewParent(vGashSize);
            newParent.addView(view);
            addChild(newParent, ++startIndex, maxWidth - requestWidth, maxWidth);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 获取新的一行布局
     *
     * @return
     */
    public LinearLayout getNewParent(int vDash) {
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                , LinearLayout.HORIZONTAL);
        layoutParams.topMargin = vDash;
        linearLayout.setLayoutParams(layoutParams);
        this.addView(linearLayout);
        return linearLayout;
    }

}
