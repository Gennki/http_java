package com.qzb.http;

import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;

public interface TransformResponseBodyImpl {
    public <T> Single<T> transform(Single<ResponseBody> single, Class<T> tClass);
}
