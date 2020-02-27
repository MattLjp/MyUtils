package com.matt.myutils.utils;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

/**
 * @author matt.Ljp
 * @time 2020/2/27 15:49
 * @description Dialog工具类
 */
public class DialogUtil {

    /**
     * 创建一个选择对话框
     *
     * @param activity
     * @param pContent            提示消息
     * @param dialogClickListener 点击监听
     * @return
     */
    public static void showSelectDialog(Activity activity, String title, String pContent, String pLeftBtnStr,
                                        String pRightBtnStr,
                                        final DialogClickListener dialogClickListener) {
        if (!isLiving(activity)) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog dialog = builder.setTitle(title)
                .setMessage(pContent)
                .setPositiveButton(pRightBtnStr, (dialog12, which) -> {
                    dialogClickListener.confirm();
                    dialog12.dismiss();
                })
                .setNegativeButton(pLeftBtnStr, (dialog1, which) -> {
                    dialogClickListener.cancel();
                    dialog1.dismiss();

                })
                .setCancelable(false)
                .create();
        if (!dialog.isShowing()) {
            dialog.dismiss();
            dialog.show();
        }
    }

    public static void showSelectDialog(Activity activity, String title, String pContent,
                                        String pRightBtnStr,
                                        final DialogClickListener dialogClickListener) {
        if (!isLiving(activity)) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog dialog = builder.setTitle(title)
                .setMessage(pContent)
                .setPositiveButton(pRightBtnStr, (dialog1, which) -> dialogClickListener.confirm())
                .setCancelable(false)
                .create();
        if (!dialog.isShowing()) {
            dialog.dismiss();
            dialog.show();
        }
    }


    private static boolean isLiving(Activity activity) {
        if (activity == null) {
            LogUtils.d("activity == null");
            return false;
        }
        if (activity.isFinishing()) {
            LogUtils.d("activity is finishing");
            return false;
        }
        return true;
    }

    public interface DialogClickListener {

        void confirm();

        void cancel();

    }
}
