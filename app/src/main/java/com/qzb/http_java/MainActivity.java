package com.qzb.http_java;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.qzb.http.Http;
import com.qzb.http.LoadingDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends BaseActivity {


    private TextView contentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentTV = findViewById(R.id.tv_content);
        findViewById(R.id.btn_get_data).setOnClickListener(view -> getData());
    }

    private void getData() {
        Disposable disposable = new Http.Builder()
                .url("http://rest.apizza.net/mock/62b7b6fb6153e600b55523484082b43b/testPost")
                .addHeader("header1", 1)
                .addParam("pageIndex", 1)
                .build()
                .get(DataBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> LoadingDialog.show(MainActivity.this))
                .doFinally(LoadingDialog::hide)
                .subscribe(dataBean -> {
                    StringBuilder sb = new StringBuilder();
                    for (String item : dataBean.getList()) {
                        sb.append(item).append("\n").append("\n");
                    }
                    contentTV.setText(sb);
                });
        bindLifeCycle(disposable);

    }

}