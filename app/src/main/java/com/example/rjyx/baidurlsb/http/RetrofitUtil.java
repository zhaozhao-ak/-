package com.example.rjyx.baidurlsb.http;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/2/20.
 */

public class RetrofitUtil {

    /**
     * 声明请求的接口
     */
    public static MyApiService myApiService;
    public static HttpLoggingInterceptor loggingInterceptor;

    /**
     * 网络请求框架
     * connectTimeout 建立连接的超时时间
     * ReadTimeout 传递数据的超时时间。
     *
     */
    public static Retrofit retrofit;


    public static MyApiService grtRetrofitToGson(String url) {
        loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.i("zhao____log_____","retrofitBack = "+message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS).build())
                .build();
        //让框架自动实现我们的请求接口,让我们的请求接口可以被调用
        myApiService = retrofit.create(MyApiService.class);
        return myApiService;
    }



}
