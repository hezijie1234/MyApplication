package com.example.jingwutongtest;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lny on 2016/4/20.
 */
public class DialogUtils {

    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_progress, null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);
        if (TextUtils.isEmpty(msg)) {
            tipTextView.setVisibility(View.GONE);
        } else {
            tipTextView.setText(msg);
        }

        Dialog loadingDialog = new Dialog(context, R.style.zzDialog);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }

//    public static Dialog createOrderCompleteDialog(Context context, String msg, DialogInterface.OnClickListener onOkClick) {
//        Dialog dialog = new AlertDialog.Builder(context)
//                .setMessage(msg)
//                .setPositiveButton(R.string.confirm, onOkClick)
//                .create();
//        dialog.setCancelable(false);
//        return dialog;
//    }
}
