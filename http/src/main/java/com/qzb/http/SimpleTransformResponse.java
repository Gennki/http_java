package com.qzb.http;

import com.google.gson.Gson;

import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;

public class SimpleTransformResponse implements TransformResponseBodyImpl {

    private final Gson gson = new Gson();

    @Override
    public <T> Single<T> transform(Single<ResponseBody> single, Class<T> tClass) {
        return single.map(responseBody -> {
            if (tClass == String.class) {
                return (T) responseBody.string();
            } else {
                return gson.fromJson(responseBody.string(), tClass);
            }
        });
    }
}
