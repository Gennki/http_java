package com.qzb.http;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 全局loading弹窗
 */
public class LoadingDialog extends AppCompatActivity {
    private static int layoutId;
    @SuppressLint("StaticFieldLeak")
    private static Activity mActivity = null;

    public static void show(Context context) {
        Intent intent = new Intent(context, LoadingDialog.class);
        context.startActivity(intent);
    }

    public static void hide() {
        if (mActivity != null && !mActivity.isFinishing()) {
            mActivity.finish();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        // 设置全透明状态栏
        setStatusBarFullTransparent();
        overridePendingTransition(0, 0);
        // 部分手机设置透明主题 无效问题
        getTheme().applyStyle(R.style.ActivityTransparent, true);
        if (layoutId != 0) {
            setContentView(layoutId);
        } else {
            setContentView(com.qzb.http.R.layout.dialog_loading);
        }
    }


    /**
     * 全透状态栏
     */
    private void setStatusBarFullTransparent() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }

    public static void setLayoutId(int layoutId) {
        LoadingDialog.layoutId = layoutId;
    }
}
