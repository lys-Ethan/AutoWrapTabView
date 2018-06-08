package com.pmlee.autowraptabview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pmlee.autowraptabview.adapter.AutoWrapAdapter;
import com.pmlee.autowraptabview.adapter.MyTabAdapter;
import com.pmlee.autowraptabview.bean.Hobby;
import com.pmlee.autowraptabview.view.AutoWrapTabView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private AutoWrapTabView mAutoWrapView1;
    private AutoWrapTabView mAutoWrapView2;
    private AutoWrapTabView mAutoWrapView3;
    private AutoWrapTabView mAutoWrapView4;
    private String[] tabs = {"钢铁侠","嘿美","阿拉斯加","雪橇犬","我是一个长标签哈哈哈","复仇者联盟3",
    "万磁王","老狗币"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAutoWrapView1 = findViewById(R.id.autoWrapView1);
        mAutoWrapView2 = findViewById(R.id.autoWrapView2);
        mAutoWrapView3 = findViewById(R.id.autoWrapView3);
        mAutoWrapView4 = findViewById(R.id.autoWrapView4);
        //模拟网络获取数据
        mAutoWrapView1.setAdapter(createAdapter());
        mAutoWrapView2.setAdapter(createAdapter());
        mAutoWrapView3.setAdapter(createAdapter());
        mAutoWrapView4.setAdapter(createAdapter());
    }

    /**
     * 添加数据及适配器
     * @return
     */
    private AutoWrapAdapter createAdapter() {
        int category = (int) (Math.random()*6);
        List<Hobby> datas = new ArrayList<>();
        int count = (int) (Math.random()*20);
        for (int i = 0;i<count;i++){
           Hobby hobby = new Hobby();
           hobby.setCategory(category);
           hobby.setName(tabs[(int) (Math.random()*tabs.length)]);
           datas.add(hobby);
        }
        MyTabAdapter tabAdapter = new MyTabAdapter(datas, this, category, category < 3);
        tabAdapter.setOnItemClickListener(new MyTabAdapter.OnItemClickListener() {
            @Override
            public void onClicked(View view, int position) {
                Toast.makeText(MainActivity.this,String.format("%d位置被点击",position),Toast.LENGTH_SHORT).show();
            }
        });
        return tabAdapter;
    }

}
