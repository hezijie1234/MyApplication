package com.example.zte.day_zte_permissions_android60;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2017-05-09.
 */

public class PermissionCheck {

    private final Context context;

    public PermissionCheck(Context context) {
        this.context = context.getApplicationContext();
    }

    public  boolean lacksPermissions(String... permissions){
        for(String permission : permissions){
            return lacksPermission(permission);
        }
        return false;
    }

    private boolean lacksPermission(String permission){
        return ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_DENIED;
    }
}
