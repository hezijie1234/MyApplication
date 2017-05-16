package com.example.zte.day17_zte_testfacecompare;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ZTEFace;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView tv1;
    String sdPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Jingwutong";
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        long start = System.currentTimeMillis();
        String strPicName = sdPath+"/pic.rgb8";
        byte[] b1 = null;
        byte[] bb = null;
        FileInputStream ins;
        try {
            ins=new FileInputStream(new File(strPicName));
            File ff=new File(strPicName);
            ff.length();
            bb=new byte[(int) ff.length()];
            ins.read(bb,0, (int) ff.length());
            ins.close();

            File file = new File(sdPath + "/head.jpg");
            b1= new byte[(int) file.length()];
            ins= new FileInputStream(file);
            ins.read(b1,0, (int) file.length());
            ins.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //加载模型
        ZTEFace.loadModel(sdPath+"/d.dat",sdPath+"/a.dat",sdPath+"/db.dat",sdPath+"/p.dat");
        //人脸检测 输入 rgb8或YUV
        byte[] ret = ZTEFace.detectFace(bb,100,123);
        byte[] ret1 = ZTEFace.detectFace(b1,160,160);

        //人脸信息转换
        ZTEFace.FaceInfo aa = ZTEFace.getFaceInfo(ret);
        ZTEFace.FaceInfo a1 = ZTEFace.getFaceInfo(ret1);

        byte[] fea = getBytes(bb, aa.info[0],100,123);
        byte[] fea1 = getBytes(b1, a1.info[0],160,160);
//特征比对
        float fScore = ZTEFace.feaCompare(fea,fea);
        long end = System.currentTimeMillis();
        //Toast.makeText(this, "", Toast.LENGTH_LONG).show();
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setText("score："+fScore);
        tv1.append(" 用时："+(end-start)+"ms");
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private byte[] getBytes(byte[] bb, ZTEFace.FacePointInfo facePointInfo,int width,int height) {
        int eyeLeftX = (int) facePointInfo.ptEyeLeft.x;
        int eyeLeftY= (int) facePointInfo.ptEyeLeft.y;
        int eyeRightX= (int) facePointInfo.ptEyeRight.x;
        int eyeRightY= (int) facePointInfo.ptEyeRight.y;
        int eyeNoseX= (int) facePointInfo.ptNose.x;
        int eyeNoseY =(int) facePointInfo.ptNose.y;
        int mouthLeftX=(int) facePointInfo.ptMouthLeft.x;
        int mouthLeftY = (int) facePointInfo.ptMouthLeft.y;
        int mouthRightX = (int) facePointInfo.ptMouthRight.x;
        int mouthRightY = (int) facePointInfo.ptMouthRight.y;
        //特征提取
        return ZTEFace.getFea(bb,width,height,eyeLeftX, eyeLeftY, eyeRightX, eyeRightY, eyeNoseX, eyeNoseY, mouthLeftX, mouthLeftY, mouthRightX, mouthRightY);
    }
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
