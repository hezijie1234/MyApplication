package com.example.zte.day29_zte_fileutils_test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;

/**
 * Created by Administrator on 2017-07-03.
 */

public class MyAlertDialog  extends AlertDialog{
    protected MyAlertDialog(@NonNull Context context) {
        super(context);
    }

    protected MyAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected MyAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
