package ru.mrcolt.anidub.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import stream.customalert.CustomAlertDialogue;

public class DialogUtils {

    public static void defaultAlert(Activity context, String title, String message, String buttonText) {
        CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(context)
                .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                .setTitle(title)
                .setMessage(message)
                .setPositiveText(buttonText)
                .setOnPositiveClicked((view, dialog) -> dialog.dismiss())
                .setDecorView(context.getWindow().getDecorView())
                .build();
        alert.show();
    }

    public static ProgressDialog createLoading(Context context, String title, String message) {
        return ProgressDialog.show(context, title, message, true);
    }

    public static ProgressDialog createLoading(Context context, String message) {
        return ProgressDialog.show(context, "", message, true);
    }

    public static void destroyLoading(ProgressDialog dialog) {
        dialog.dismiss();
    }

}
