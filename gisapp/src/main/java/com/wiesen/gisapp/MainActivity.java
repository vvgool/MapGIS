package com.wiesen.gisapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiesen.provider.local.MapDecodeJNI;

public class MainActivity extends AppCompatActivity {
    private ImageView mIvMap;
    private TextView mTvHello;
    private MapDecodeJNI decodeJNI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        decodeJNI = new MapDecodeJNI(FileUtils.getStoragePath()+ File.separator+"map.dat");
        mIvMap = (ImageView) findViewById(R.id.iv_map);
        mTvHello = (TextView) findViewById(R.id.tv_hello);
        mTvHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (decodeJNI!=null) {
                        Bitmap bitmap = decodeJNI.queryBitmapByIndex("1_1_0");
                        mIvMap.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
