package com.qzb.http;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;

public class Http {

    private final String url;
    private final Map<String, Object> params;
    private final Map<String, Object> headers;
    private TransformResponseBodyImpl transformResponseBody;

    private Http(String url, Map<String, Object> headers, Map<String, Object> params, TransformResponseBodyImpl transformResponseBody) {
        this.url = url;
        this.params = params;
        this.headers = headers;
        this.transformResponseBody = transformResponseBody;
    }


    public <T> Single<T> get(Class<T> tClass) {
        return HttpManager.getInstance().get(url, headers, params, tClass, transformResponseBody);
    }

    public <T> Single<T> post(Class<T> tClass) {
        return HttpManager.getInstance().post(url, headers, params, tClass, transformResponseBody);
    }

    public <T> Single<T> put(Class<T> tClass) {
        return HttpManager.getInstance().put(url, headers, params, tClass, transformResponseBody);
    }

    public <T> Single<T> delete(Class<T> tClass) {
        return HttpManager.getInstance().delete(url, headers, params, tClass, transformResponseBody);
    }


    public static class Builder {

        private String url;
        private Map<String, Object> params = new HashMap();
        private Map<String, Object> headers = new HashMap<>();
        private TransformResponseBodyImpl transformResponseBody;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder addParam(String key, Object value) {
            params.put(key, value);
            return this;
        }

        public Builder addParams(Map<String, Object> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder addHeader(String key, Object value) {
            headers.put(key, value);
            return this;
        }

        public Builder addHeaders(Map<String, Object> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder setTransformResponseBody(TransformResponseBodyImpl transformResponseBody) {
            this.transformResponseBody = transformResponseBody;
            return this;
        }

        public Http build() {
            return new Http(url, headers, params, transformResponseBody);
        }

    }
}
