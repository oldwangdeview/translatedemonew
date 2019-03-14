package translatedemo.com.translatedemo.rxjava;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.reactivestreams.Subscriber;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import translatedemo.com.translatedemo.base.BaseActivity;

/**
 * Author yichao
 * Dest  得到访问网络api
 */
public class ApiUtils {

    private static OkHttpClient.Builder sBuilder = new OkHttpClient().newBuilder();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create(new Gson());
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static Api sApi;
    public static Api getApi() {

        if (sApi == null) {
            sBuilder.readTimeout(10, TimeUnit.SECONDS);
            sBuilder.connectTimeout(10, TimeUnit.SECONDS);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient())
                    .baseUrl(Api.isRelease ? Api.baseUrl : Api.testBaseUrl)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sApi = retrofit.create(Api.class);
        }
        return sApi;
    }

    public static Api getApi2() {

        if (sApi == null) {
            sBuilder.readTimeout(10, TimeUnit.SECONDS);
            sBuilder.connectTimeout(10, TimeUnit.SECONDS);
            Retrofit retrofit = new Retrofit.Builder()
//                    .client(sBuilder.build())
                    .baseUrl(Api.isRelease ? Api.baseUrl : Api.testBaseUrl)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sApi = retrofit.create(Api.class);
        }
        return sApi;
    }


    private static OkHttpClient getOkHttpClient() {
//日志显示级别
        HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BODY;
//新建log拦截器
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("zcb","OkHttp====Message:"+message);
            }
        });
        loggingInterceptor.setLevel(level);
//定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient
                .Builder();
//OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(loggingInterceptor);
        return httpClientBuilder.build();
    }



}
