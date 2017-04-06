package com.example.day1_zte_dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private Dialog progressDialog;
    protected View viewone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initProgressDialog() {

        progressDialog = DialogUtils.createLoadingDialog(this, "");
        //这个方法的用处是设置在在对话框的外面点击是否让对话框消失
        progressDialog.setCanceledOnTouchOutside(false);
        //设置dialog监听返回键。
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    public void dialogClick(View view) {
//        initProgressDialog();
//        progressDialog.show();
        ImageButton imageButton = (ImageButton) viewone.findViewById(R.id.btn_action_one);
        if(imageButton != null){
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setImageResource(R.mipmap.zte_23);
        }
    }
}
