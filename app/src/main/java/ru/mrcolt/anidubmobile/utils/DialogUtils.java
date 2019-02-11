package ru.mrcolt.anidubmobile.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtils {

    public ProgressDialog createLoading(Context context, String title, String message) {
        return ProgressDialog.show(context, title, message, true);
    }

    public ProgressDialog createLoading(Context context, String message) {
        return ProgressDialog.show(context, "", message, true);
    }

    public void destroyLoading(ProgressDialog dialog) {
        dialog.dismiss();
    }
}
