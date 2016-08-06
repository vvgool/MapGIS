package com.wiesen.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiesen.provider.local.MapDecodeJNI;
import com.wiesen.utils.FileUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ImageView mIvMap;
    private TextView mTvHello;
    private MapDecodeJNI decodeJNI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionsUtils.verifyStoragePermissions(this,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                decodeJNI = new MapDecodeJNI(FileUtils.getStoragePath()+ File.separator+"map.dat");
            }
        }
    }
}
