package com.example.zte.day20_zte_permissiondependency;

import android.Manifest;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissonItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void threePermissionClick(View view) {
        HiPermission.create(this).checkMutiPermission(new PermissionCallback() {
            @Override
            public void onClose() {
                Log.e(TAG, "onClose: 用户关闭了权限申请界面" );
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish: 权限申请完成" );
            }

            @Override
            public void onDeny(String permisson, int position) {
                Log.e(TAG, "onDeny: 权限申请被拒绝" );
            }

            @Override
            public void onGuarantee(String permisson, int position) {
                Log.e(TAG, "onGuarantee: "+permisson );
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermission(View view) {
        List<PermissonItem> list = new ArrayList<>();
        list.add(new PermissonItem(Manifest.permission.ACCESS_COARSE_LOCATION,"网络定位权限",R.mipmap.ic_launcher));
        list.add(new PermissonItem(Manifest.permission.ACCESS_FINE_LOCATION,"gps定位权限",R.mipmap.ic_launcher));
        list.add(new PermissonItem(Manifest.permission.WRITE_EXTERNAL_STORAGE,"文件写入权限",R.mipmap.ic_launcher));

        HiPermission.create(this)
                .title("亲爱的上帝啊")
                .filterColor(getResources().getColor(R.color.colorPrimary,getTheme()))
                .permissions(list)
                .msg("为了保护世界的和平，开启这些权限吧！\n你我一起拯救世界！")
                .style(R.style.PermissionBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
            @Override
            public void onClose() {
                Log.e(TAG, "onClose: 用户关闭了权限申请界面" );
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish: 权限申请完成" );
            }

            @Override
            public void onDeny(String permisson, int position) {
                Log.e(TAG, "onDeny: 权限申请被拒绝" );
            }

            @Override
            public void onGuarantee(String permisson, int position) {
                Log.e(TAG, "onGuarantee: "+permisson );
            }
        });
    }

    public void matchClick(View view) {
        Pattern pattern = Pattern.compile("\\d\\d\\d[-]\\d\\d\\d\\d\\d\\d\\d");
        Matcher matcher = pattern.matcher("010-1234567");
        if(matcher.matches()){
            Log.e(TAG, "matchClick: 是座机号码" );
        }else {
            Log.e(TAG, "matchClick: 不是座机号码" );
        }
    }

    public void getInnerFile(View view) {
        File file = getDir("abc", MODE_PRIVATE);
        try {
            FileOutputStream fos = new FileOutputStream(new File(file,"abc.txt"));
            fos.write("hezijie".getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "getInnerFile: "+file.getAbsolutePath() );
        try {
            FileInputStream fis = new FileInputStream(new File(file,"abc.txt"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int length = 0;
            byte [] buffer = new byte[1024];
            if((length = fis.read(buffer)) > 0){
                baos.write(buffer,0,length);
                baos.flush();
            }
            baos.close();
            fis.close();
            Log.e(TAG, "getInnerFile: "+baos.toString() );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
