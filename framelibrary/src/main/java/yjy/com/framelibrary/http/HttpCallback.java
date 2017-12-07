package yjy.com.framelibrary.http;

import android.content.Context;

import com.google.gson.Gson;

import java.util.Map;

import yjy.com.baselibrary.http.EngineCallBack;
import yjy.com.baselibrary.http.HttpUtils;

/**
 * Created by asus on 2017/7/3.
 */
public abstract class HttpCallback<T> implements EngineCallBack {
    @Override
    public void onPreExcute(Context context, Map<String, Object> mParams) {
        //这里可以添加公用参数，如token等
//        mParams.put("token","12312313");
        onPreExcute();
    }

    //添加公有参数，开始执行了
    public void onPreExcute(){

    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        T ObjectResult = (T)gson.fromJson(result, HttpUtils.analysisClazzInfo(this));
        onSuccess(ObjectResult);
    }

    //
    public abstract void onSuccess(T result);
}
