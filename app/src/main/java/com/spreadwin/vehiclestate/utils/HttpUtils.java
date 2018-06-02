package com.spreadwin.vehiclestate.utils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.spreadwin.vehiclestate.config.AppConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
//import com.zhy.http.okhttp.callback.GenericsCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Administrator on 2018/4/13 0013.
 */

public class HttpUtils {
    private String TAG="HttpUtils";
    private Context context;


    public HttpUtils(Context context) {
        this.context = context;
    }


    public void initHttp() {

        String time= String.valueOf(new Date().getTime());
        String dateStr="2018-05-24";
        String type="mxTp";
        String sign = "appID=" + AppConfig.API_ID + "&timeStamp=" + time + "&vin=" + AppConfig.VIN + "&dateStr=" + dateStr + "&type=" + type;
       String hmac=hmacSha1(sign, AppConfig.API_KEY).replace("\n","");
        Log.i(TAG,"sign="+sign+",hmac="+hmac);


//        OkHttpUtils
//                .post()
//                .url(AppConfig.HTTP_HOST_TEST+AppConfig.URL_PATH)
//                .addParams("vin",AppConfig.VIN)//
//                .addParams("dateStr",dateStr)//
//                .addParams("type",type)
//                .addParams("sign",hmac)//
//                .addParams("timeStamp",time)//
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        Log.i(TAG,"response="+response);
//                    }
//                });


        OkHttpUtils
                .post()//
                .url(AppConfig.HTTP_HOST_TEST+AppConfig.URL_PATH)//
                .addParams("vin",AppConfig.VIN)//
                .addParams("dateStr",dateStr)//
                .addParams("type",type)
                .addParams("sign",hmac)//
                .addParams("timeStamp",time)//
                .build()//
        .execute(new GenericsCallback<String>(new JsonGenericsSerializator()) {
//            @Override
//            public Object parseNetworkResponse(Response response, int id) throws Exception {
//                Log.i(TAG,"onError response="+response);
//                return null;
//            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i(TAG,"onError call="+call.toString());
            }

//            @Override
//            public void onResponse(Object response, int id) {
//                Log.i(TAG,"response="+response);
//            }


            @Override
            public void onResponse(String response, int id) {
                Log.i(TAG,"response="+response);
            }
        });



    }



    public  String hmacSha1(String sign, String appSecret) {
        try {
            String type = "HmacSHA1";
            SecretKeySpec secret = new SecretKeySpec(appSecret.getBytes("UTF-8"), type);
            Mac mac = Mac.getInstance(type);
            mac.init(secret);
            byte[] digest = mac.doFinal(sign.getBytes("UTF-8"));
            return Base64.encodeToString(digest, Base64.DEFAULT).trim();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";

    }

    public static String sha1(String val,String appSecret) {
        try {
            byte[] data = val.getBytes("utf-8");
            MessageDigest mDigest =MessageDigest.getInstance("sha1");

            return Base64.encodeToString(mDigest.digest(data), Base64.DEFAULT).trim();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
