package com.example.zte.day_zte_permissions_android60;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0; // 请求码
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE
    };
    private static final String TAG = "111";
    private List<String> list = new ArrayList<>();
    private PermissionCheck mPermissionsChecker; // 权限检测器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mPermissionsChecker = new PermissionCheck(this);
//        // 缺少权限时, 进入权限配置页面
//        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
//            PermissionActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
//        }
        if(Build.VERSION.SDK_INT >= 23){
            Log.e(TAG, "onCreate: "+"这是6.0的手机" );
            for (int i = 0; i < PERMISSIONS.length; i++) {
                if (ContextCompat.checkSelfPermission(this, PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {
                    list.add(PERMISSIONS[i]);
                }
            }
            String[] permission = new String[list.size()];
            for (int i = 0; i < permission.length; i++) {
                permission[i] = list.get(i);
            }
            if (list.size() > 0) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                Log.e(TAG, "onCreate: " + list.size());
            } else {
                //执行初始化等逻辑。

            }
        }
        Log.e(TAG, "onCreate: 这不是6.0的手机" );
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10086");
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + "10086");
                    intent.setData(data);

//                    startActivity(intent);
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
//        if (requestCode == REQUEST_CODE && resultCode == PermissionActivity.PERMISSIONS_DENIED) {
////            finish();
////            Helper.showToast("拒绝权限将导致部分功能无法使用");
//        }
//    }

}
