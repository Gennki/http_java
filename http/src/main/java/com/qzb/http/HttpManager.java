package com.qzb.http;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Single;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {
    private static HttpManager manager;
    private ApiService apiService;
    private final OkHttpClient.Builder okHttpBuilder;
    private TransformResponseBodyImpl transformResponseBody;

    private HttpManager() {
        okHttpBuilder = new OkHttpClient.Builder();
    }

    public static HttpManager getInstance() {
        if (manager == null) {
            synchronized (HttpManager.class) {
                if (manager == null) {
                    manager = new HttpManager();
                }
            }
        }
        return manager;
    }

    // 初始化
    public void init() {
        OkHttpClient okHttpClient = okHttpBuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.baidu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
                .client(okHttpClient)
                .build();
        apiService = retrofit.create(ApiService.class);

        if (transformResponseBody == null) {
            transformResponseBody = new SimpleTransformResponse();
        }
    }

    public HttpManager addInterceptor(Interceptor interceptor) {
        okHttpBuilder.addInterceptor(interceptor);
        return manager;
    }

    public HttpManager callTimeout(long milliseconds) {
        okHttpBuilder.callTimeout(milliseconds, TimeUnit.MILLISECONDS);
        return manager;
    }


    public HttpManager connectTimeout(long milliseconds) {
        okHttpBuilder.connectTimeout(milliseconds, TimeUnit.MILLISECONDS);
        return manager;
    }

    public HttpManager writeTimeout(long milliseconds) {
        okHttpBuilder.writeTimeout(milliseconds, TimeUnit.MILLISECONDS);
        return manager;
    }

    public HttpManager readTimeout(long milliseconds) {
        okHttpBuilder.readTimeout(milliseconds, TimeUnit.MILLISECONDS);
        return manager;
    }

    public HttpManager setTransformResponseBody(TransformResponseBodyImpl transformResponseBody) {
        this.transformResponseBody = transformResponseBody;
        return manager;
    }

    public <T> Single<T> get(String url, Map<String, Object> headers, Map<String, Object> params, Class<T> tClass, TransformResponseBodyImpl transformResponseBody) {
        if (transformResponseBody != null) {
            return transformResponseBody.transform(apiService.get(url, headers, params), tClass);
        } else {
            return this.transformResponseBody.transform(apiService.get(url, headers, params), tClass);
        }
    }


    public <T> Single<T> post(String url, Map<String, Object> headers, Map<String, Object> params, Class<T> tClass, TransformResponseBodyImpl transformResponseBody) {
        if (transformResponseBody != null) {
            return transformResponseBody.transform(apiService.post(url, headers, params), tClass);
        } else {
            return this.transformResponseBody.transform(apiService.post(url, headers, params), tClass);
        }
    }


    public <T> Single<T> put(String url, Map<String, Object> headers, Map<String, Object> params, Class<T> tClass, TransformResponseBodyImpl transformResponseBody) {
        if (transformResponseBody != null) {
            return transformResponseBody.transform(apiService.put(url, headers, params), tClass);
        } else {
            return this.transformResponseBody.transform(apiService.put(url, headers, params), tClass);
        }
    }


    public <T> Single<T> delete(String url, Map<String, Object> headers, Map<String, Object> params, Class<T> tClass, TransformResponseBodyImpl transformResponseBody) {
        if (transformResponseBody != null) {
            return transformResponseBody.transform(apiService.delete(url, headers, params), tClass);
        } else {
            return this.transformResponseBody.transform(apiService.delete(url, headers, params), tClass);
        }
    }


}
