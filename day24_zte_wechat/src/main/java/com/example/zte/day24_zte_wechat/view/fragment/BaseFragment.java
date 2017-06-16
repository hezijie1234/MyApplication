package com.example.zte.day24_zte_wechat.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zte.day24_zte_wechat.R;

/**
 * Created by Administrator on 2017-05-31.
 */

public class BaseFragment extends Fragment {

    private Dialog progressDialog;
    protected View view;
    private boolean isFirstResume = true;

    private void initDialog(){
        progressDialog = createDialog(getActivity(),"");
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
        if(progressDialog == null && !isRemoving()){
            initDialog();
        }
        if(progressDialog !=null && !progressDialog.isShowing()){
            progressDialog.setCanceledOnTouchOutside(!isModel);
            progressDialog.show();
        }
    }
    private boolean isFirst = true;

    @Override
    public void onResume() {
        super.onResume();
        if(isFirstResume){
            isFirstResume = false;
            return;
        }
        //如果不是第一次fragment可见，那说明前面，加载过数据，就不用再次加载数据了。
        if(getUserVisibleHint()){
            setUserData(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(isFirst){
                isFirst = false;
                setUserData(true);
            }else {
                setUserData(false);
            }
        }
    }

    protected void setUserData(boolean b) {

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

    protected ImageButton showBackView(int resId,View.OnClickListener listener){
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.back);
        if(imageButton != null){
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setImageResource(resId);
            imageButton.setOnClickListener(listener);
        }
        return imageButton;
    }

    protected TextView showTitleView(String title){
        TextView titleView = (TextView)view.findViewById(R.id.title);
        if(titleView != null){
            titleView.setText(title);
        }
        return titleView;
    }
}
