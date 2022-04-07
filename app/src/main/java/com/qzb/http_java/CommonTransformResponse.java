package com.qzb.http_java;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qzb.http.TransformResponseBodyImpl;

import org.json.JSONObject;

import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;

public class CommonTransformResponse implements TransformResponseBodyImpl {

    private final Gson gson = new Gson();

    @Override
    public <T> Single<T> transform(Single<ResponseBody> single, Class<T> tClass) {
        return single.map(responseBody -> {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            String data = jsonObject.get("data").toString();
            if (tClass == String.class) {
                return (T) data;
            } else {
                return gson.fromJson(data, tClass);
            }
        });
    }
}
