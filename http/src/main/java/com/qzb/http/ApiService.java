package com.qzb.http;

import java.util.Map;
import java.util.Observable;

import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {
    @GET
    public Single<ResponseBody> get(@Url String url, @HeaderMap Map<String, Object> headers, @QueryMap Map<String, Object> params);

    @POST
    public Single<ResponseBody> post(@Url String url, @HeaderMap Map<String, Object> headers, @Body Map<String, Object> params);

    @PUT
    public Single<ResponseBody> put(@Url String url, @HeaderMap Map<String, Object> headers, @Body Map<String, Object> params);

    @DELETE
    public Single<ResponseBody> delete(@Url String url, @HeaderMap Map<String, Object> headers, @QueryMap Map<String, Object> params);
}
