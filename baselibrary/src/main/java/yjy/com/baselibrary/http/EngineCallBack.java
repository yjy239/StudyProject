package yjy.com.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by asus on 2017/6/29.
 */
public interface EngineCallBack {

    void onPreExcute(Context context, Map<String, Object> map);

    void onError(Exception e);


    void onSuccess(String result);

    //默认执行
     EngineCallBack DefaultCallback = new EngineCallBack() {
        @Override
        public void onPreExcute(Context context,Map<String,Object>map) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };

}
