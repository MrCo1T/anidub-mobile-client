package ru.mrcolt.anidub.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionUtils {
    public static void get(Activity activity, String[] permission) {
        ActivityCompat.requestPermissions(activity,
                permission,
                1);
    }

    public static boolean check(Activity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
