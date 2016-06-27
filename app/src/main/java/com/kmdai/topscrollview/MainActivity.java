package com.kmdai.topscrollview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    TopScrollView mTopScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTopScrollView = (TopScrollView) findViewById(R.id.topscrollview);
    }

    public void open(View view) {
        mTopScrollView.open();
    }
}
