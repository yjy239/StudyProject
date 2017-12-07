package yjy.com.framelibrary.http;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import yjy.com.baselibrary.http.EngineCallBack;
import yjy.com.baselibrary.http.HttpUtils;
import yjy.com.baselibrary.http.IHttpEngine;


/**
 * Created by asus on 2017/6/29.
 * 默认网络引擎
 */
public class OkHttpEngine implements IHttpEngine {

    private static OkHttpClient mOkHttpClient = new OkHttpClient();
    private static Handler mHandler = new Handler();


    @Override
    public void get(final boolean iscache, Context context, String url, Map<String, Object> parms, final EngineCallBack callBack) {
        final String joinUrl = HttpUtils.joinParms(url,parms);
//        if(callBack!=null){
//            callBack.onPreExcute(joinUrl);
//        }

//        final IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactroy().getDao(CacheData.class);
        Log.e("http->get",joinUrl);
        if(iscache){
            String result = CacheUtils.getCacheJson(joinUrl);
            //已经有缓存
            if(!TextUtils.isEmpty(result)){
                callBack.onSuccess(result);
            }
        }

        RequestBody body = appendBody(parms);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .get()
                .tag(context);
        builder.method("GET",null);
        Request request = builder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String requestResult = response.body().string();
                //此处判断没有一样的缓存则保存
                if (iscache){
                    String result = CacheUtils.getCacheJson(joinUrl);
                    if(!TextUtils.isEmpty(result)){
                        if(result.equals(requestResult)){
                            //内容一样不需要执行成功方法
                            Log.e("数据一致：",requestResult);
                            return;
                        }
                    }
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(requestResult);
                    }
                });
                //执行成功方法


                //缓存数据
                if(iscache){
                    CacheUtils.cacheData(joinUrl,requestResult);
                }
            }
        });


    }

    private RequestBody appendBody(Map<String, Object> parms) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder,parms);
        return builder.build();
    }

    private void addParams(MultipartBody.Builder builder, Map<String, Object> parms) {
        if(parms!=null && !parms.isEmpty()){
            for(String key : parms.keySet()){
                builder.addFormDataPart(key,parms.get(key)+"");
                Object value = parms.get(key);
                if(value instanceof File){
                    File file = (File) value;
                    builder.addFormDataPart(key,file.getName(),RequestBody
                            .create(MediaType.parse(guessMineType(file.getAbsolutePath())),file));
                }else if(value instanceof List){
                    //提交是一个集合
                    try {
                        List<File> listfile = (List<File>)value;
                        //循环获取文件列表
                        for(int i = 0;i<listfile.size();i++){
                            File file = listfile.get(i);
                            builder.addFormDataPart(key,file.getName(),RequestBody
                                    .create(MediaType.parse(guessMineType(file.getAbsolutePath())),file));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    builder.addFormDataPart(key,value+"");
                }
            }
        }
    }

    private String guessMineType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if(contentTypeFor ==null){
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    @Override
    public void post(boolean iscache,Context context,String url, Map<String, Object> parms, final EngineCallBack callBack) {
        final String joinUrl = HttpUtils.joinParms(url,parms);
//        if(callBack!=null){
//            callBack.onPreExcute(joinUrl);
//        }

        Log.e("http->post",joinUrl);

        RequestBody body = appendBody(parms);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(body)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
               final String responseStr = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            callBack.onSuccess(responseStr);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }
}
