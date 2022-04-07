package com.qzb.http_java;

import android.app.Application;

import com.qzb.http.HttpManager;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 日志拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // 初始化网络框架
        HttpManager.getInstance()
                .addInterceptor(httpLoggingInterceptor)
                .callTimeout(10000)
                .connectTimeout(10000)
                .readTimeout(10000)
                .writeTimeout(10000)
                .setTransformResponseBody(new CommonTransformResponse())
                .init();
        // 网络全局异常统一处理
        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);

    }
}
