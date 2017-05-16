package com.example.zte.day22_zte_baseactivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-05-15.
 */

public class BaseActivity extends AppCompatActivity {

    private Dialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private void initDialog(){
        progressDialog = createDialog(this,"");
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    progressDialog.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    public void showProgressDialog(boolean isModel){
        if(progressDialog == null && !isFinishing()){
            initDialog();
        }
        if(progressDialog !=null && !progressDialog.isShowing()){
            progressDialog.setCanceledOnTouchOutside(!isModel);
            progressDialog.show();
        }
    }

    public void showProgressDialog(){
        showProgressDialog(false);
    }

    public void hideProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }



    private Dialog createDialog(Context context, String s) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
        LinearLayout ll = (LinearLayout) inflate.findViewById(R.id.dialog_linear);
        Dialog loadingDialog = new Dialog(context,R.style.testDialog);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(inflate,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return loadingDialog;
    }

    protected ImageButton showBackView(){
        ImageButton imageButton = (ImageButton) findViewById(R.id.back);
        if(imageButton != null){
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        return imageButton;
    }

    protected TextView showTitleView(String title){
        TextView titleView = (TextView) findViewById(R.id.title);
        if(titleView != null){
            titleView.setText(title);
        }
        return titleView;
    }
}
