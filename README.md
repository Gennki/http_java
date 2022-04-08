# 项目简介

http_java是retrofit2+rxjava3封装的一套轻量级网络请求框架，支持**RESTful API**

# 集成

1. 在根目录下的build.gradle文件中添加
  
  ```groovy
  allprojects {
          repositories {
              ...
              maven { url 'https://jitpack.io' }
          }
      }
  ```
  
2. 在项目module下的build.gradle文件中添加依赖
  
  ```groovy
  dependencies {
              implementation 'com.github.Gennki:http_java:Tag'
      }
  ```
  

# 基础使用

1. 在Application的onCreate中初始化
  

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化网络框架
        HttpManager.getInstance().init();
    }
}
```

2. 调用接口，使用Rxjava的链式调用
  
  ```java
  private void getData() {
      new Http.Builder()
              .url("http://rest.apizza.net/mock/62b7b6fb6153e600b55523484082b43b/testPost")    // 设置url
              .addHeader("header1", 1)    // 添加请求头
              .addParam("pageIndex", 1)   // 添加请求参数 
              .build()
              .get(DataBean.class)        // 返回的数据想要转换成什么类型
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
  }
  ```
  
  除了`get`以外，还有`post`，`put`和`delete`方法   
  

# 其他用法

1. 框架自带LoadingDialog可以通过以下方式
  
  - `LoadingDialog.show()`
    
  - `LoadingDialog.hide()`
    
  
  如果想要自定义样式，可以使用`LoadingDialog.setLayoutId(R.layout.xxx)`来设置想要的布局
  
2. 不同的服务端，返回的数据格式（BaseResponse）可能不一样，比如有的返回的可能长这样
  
  ```json
  {
      "code": 200,
      "msg": "成功",
      "data": {
          "list": [
              "xxx",
              "xxx",
              "xxx"
          ]
      }
  }
  ```
  
  有的可能字段就变了
  
  ```json
  {
      "request_code": 200,
      "message": "成功",
      "data": {
          "list": [
              "xxx",
              "xxx",
              "xxx"
          ]
      }
  }
  ```
  
  针对这种情况，可以自己实现`TransformResponseBodyImpl`接口，例如针对第一种格式的`BaseResponse`，我们想要将`data`里面的数据取出来，在请求网络时，自动转换成`get`，`post`，`put`，`delete`中指定的格式，就可以这么写：
  
  ```java
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
  ```
  
  定义好`CommonTransformResponse`后，可以在Application中初始化时，指定解析方式：
  
  ```java
  // 初始化网络框架
  HttpManager.getInstance()
          .setTransformResponseBody(new CommonTransformResponse())    // 自定义解析器
          .init();
  ```
  
  框架中`SimpleTransformResponse`默认实现了`TransformResponseBodyImpl`接口,如果要使用`SimpleTransformResponse`，初始化时可以不指定解析器
  

2. 可以自己添加拦截器和超时时间
  
  ```java
  // 日志拦截器（需要）
  HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
  httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
  // 初始化网络框架
  HttpManager.getInstance()
          // 设置拦截器
          .addInterceptor(httpLoggingInterceptor)
          // 设置超时时间
          .callTimeout(10000)
          .connectTimeout(10000)
          .readTimeout(10000)
          .writeTimeout(10000)
          .init();
  ```
  

3. 可以在初始化时设置全局异常捕获
  
  ```java
  // 网络全局异常统一处理
  RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);
  ```
  

# 所有初始化方法

```java
package com.qzb.http_java;

import android.app.Application;

import com.qzb.http.HttpManager;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 日志拦截器（需要）
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 初始化网络框架
        HttpManager.getInstance()
                // 设置拦截器
                .addInterceptor(httpLoggingInterceptor)

                // 设置超时时间
                .callTimeout(10000)
                .connectTimeout(10000)
                .readTimeout(10000)
                .writeTimeout(10000)

                // 自定义解析器
                .setTransformResponseBody(new CommonTransformResponse())
                .init();

        // 网络全局异常统一处理
        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);

    }
}
```

# 关于内存泄漏

由于项目使用Rxjava，因此网络请求可能会有内存泄漏，可以通过传统的`Disposable.dispose()`或者`CompositeDisposable.dispose()`来解决。

框架本身不应该限制方式，因此也可以使用一些优秀的开源库来解决此问题，比如[RxLifecycle]([GitHub - trello/RxLifecycle: Lifecycle handling APIs for Android apps using RxJava](https://github.com/trello/RxLifecycle))，[AutoDispose]([GitHub - uber/AutoDispose: Automatic binding+disposal of RxJava streams.](https://github.com/uber/AutoDispose))或者[RxLife]([GitHub - liujingxing/rxlife: 一行代码解决RxJava 内存泄漏，一款轻量级别的RxJava生命周期管理库](https://github.com/liujingxing/rxlife))
