# 前言

最近工作中有了新需求，就是新增用户的兴趣爱好标签，对于标签要求就是需要一行一行的摆放，空间不足的时候则跳至下一行进行摆放。看了网上这样类似的UI实现五花八门的，但是可定制化比较差，所以就自己动手简单的实现了一个可定制化UI的标签控件，方便以后复用。

### 话不多说先上一个效果图~

![随机生成的标签](https://upload-images.jianshu.io/upload_images/8901754-ba05ef7d0600c233.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/600 )

# 如何使用

1.首先需要下载源码->[源码地址](https://github.com/PM-LEE/AutoWrapTabView.git)，可以直接对项目进行依赖，或者将源码中的必要文件拷到项目中。(源码同时也是可供运行的Demo)![必要文件](https://upload-images.jianshu.io/upload_images/8901754-903bfad21d561e33.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2.在布局文件中声明一个AutoWrapAdapter标签，<com.pmlee.autowraptabview.view.AutoWrapTabView
       android:id="@+id/autoWrapView1"
       app:vertical_dash="5dp"
       app:horizontal_dash="5dp"
       android:layout_width="match_parent"
       android:layout_height="wrap_content" />
其中vertical_dash和horizontal_dash分别是设置垂直间隔以及水平间隔的值。

3.数据填充，需要实现AutoWrapAdapter的接口进行数据填充。使用的方式和ListView的Adapter一致。
```@Override
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
    }}
```
>可以在继承抽象类的同时追加数据源泛型来确定数据的类型，public class MyTabAdapter extends AutoWrapAdapter**<Hobby>**
最后通过AutoWrapTabView的setAdapter()的方式进行设置即可。
```
mAutoWrapView1 = findViewById(R.id.autoWrapView1);
mAutoWrapView1.setAdapter(new YourAdapter(data));
```
# 实现原理

考虑到需要定制化UI所以采用了适配器模式。
关于容器部分笔者为了偷懒，没有乖乖的继承至ViewGroup去算坐标，而是继承至LinearLayout将其布局设置为纵向。然后每一行为一个横向的LinearLayout，根据标签数据的数量并通过递归的方式加入到父布局中。具体的结构图如下。![view结构.png](https://upload-images.jianshu.io/upload_images/8901754-b417583e4ad50465.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# 简单的适配器

```
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
```
AutoWrapTabView关键性代码：
1.通过适配取得数据源，遍历每个数据并将定制的View资源放入集合中，然后准备加入到父容器中。
```
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
```
2.添加标签View的核心方法，并且采用递归的方式。当View开始进行摆放的时候进行调用，新增一行LinearLayout将标签View设置横向间距后加入其中，然后通过padding值及间隙值计算填充后的剩余空间。空间不足则新增一行容器。
```
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
```
3.在onLayout中进行空间的摆放调用
```
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
```
项目到这里就介绍完啦，以最简洁的方式实现了TAG自动换行的View(其实是偷懒)



