package com.pmlee.autowraptabview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pmlee.autowraptabview.view.AutoWrapTabView;

public class MainActivity extends AppCompatActivity {
    private AutoWrapTabView mAutoWrapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAutoWrapView = findViewById(R.id.autoWrapView);
    }
}
