package com.example.zte.day16_zte_retrofit_uploading;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "111";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    String sdPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= 23){
            //检查定位权限是否开启
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "onCreate: "+"发现权限没有" );
                //没开启，久执行下面的方法申请权限，权限申请结果在回调借口中。
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }else{
                Log.e(TAG, "onCreate: "+"定位权限已经拥有，可以开始定位" );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意权限.
                    Log.e(TAG, "onRequestPermissionsResult: "+"用户同意了权限申请" );
                } else {
                    //当用户拒绝提供定位权限时
                    Log.e(TAG, "onRequestPermissionsResult: "+"用户拒绝了权限" );
                }
                return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void uploadClick(View view) {
        boolean sdExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(sdExist){
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(sdPath);
            //pic.rgb8
            //File ff = new File(file,"pic.rgb8");
            //Log.e(TAG, "uploadClick: "+ff.getAbsolutePath() );
            byte[] bb = null;
            FileInputStream ins;
            try {
                //Log.e(TAG, "uploadClick: "+ff.length() );
//                ins=new FileInputStream(ff);
                ins=new FileInputStream(file.getCanonicalFile()+"/pic.rgb8");

                //ff.length();

//                bb=new byte[(int) ff.length()];
                bb = new byte[ins.available()];
                ins.read(bb);
                Log.e(TAG, "uploadClick: "+"读取流完毕" );
//                ins.read(bb,0, (int) ff.length());
                ins.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeFile(View view) {
        try{
    /* 获取File对象，确定数据文件的信息 */
            //File file  = new File(Environment.getExternalStorageDirectory()+"/f.txt");
            File file  = new File(Environment.getExternalStorageDirectory(),"test.txt");

    /* 判断sd的外部设置状态是否可以读写 */
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

        /* 流的对象 *//*  */
                FileOutputStream fos = new FileOutputStream(file);

        /* 需要写入的数据 */
                String message = "天气不是很好";

        /* 将字符串转换成字节数组 */
                byte[] buffer = message.getBytes();

        /* 开始写入数据 */
                fos.write(buffer);

        /* 关闭流的使用 */
                fos.close();
                Toast.makeText(MainActivity.this, "文件写入成功", Toast.LENGTH_SHORT).show();
            }

        }catch(Exception ex){
            Toast.makeText(MainActivity.this, "文件写入失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void readFile(View view) {
        try{

        /* 创建File对象，确定需要读取文件的信息 */
            File file = new File(Environment.getExternalStorageDirectory(),"test.txt");
            Log.e(TAG, "readFile: "+file.getAbsolutePath() );

        /* FileInputSteam 输入流的对象， */
            FileInputStream fis = new FileInputStream(file);

        /* 准备一个字节数组用户装即将读取的数据 */
            byte[] buffer = new byte[fis.available()];

        /* 开始进行文件的读取 */
            fis.read(buffer);

        /* 关闭流  */
            fis.close();

        /* 将字节数组转换成字符创， 并转换编码的格式 */
            //String res = EncodingUtils.getString(buffer, "UTF-8");

            Toast.makeText(MainActivity.this, "文件读取成功，您读取的数据为："+new String(buffer,"UTF-8"), Toast.LENGTH_SHORT).show();

        }catch(Exception ex){
            Toast.makeText(MainActivity.this, "文件读取失败！", Toast.LENGTH_SHORT).show();
        }
    }

    public void readResource(View view) {
        InputStream is = this.getResources().openRawResource(R.raw.pic);
        DataInputStream dis = new DataInputStream(is);
        byte[]buffer = new byte[0];
        try {
            buffer = new byte[is.available()];
            dis.readFully(buffer);
            Log.e(TAG, "readResource: "+new String(buffer,"UTF-8") );
            dis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
