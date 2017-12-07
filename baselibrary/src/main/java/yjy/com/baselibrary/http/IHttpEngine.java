package yjy.com.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by asus on 2017/6/29.
 * 网络引擎的规范
 */
public interface IHttpEngine {
    //get

    void get(boolean iscache, Context context, String url, Map<String, Object> parms, EngineCallBack callBack);

    //post
    void post(boolean iscache, Context context, String url, Map<String, Object> parms, EngineCallBack callBack);

    //下载文件

    //上传文件

    //https 添加证书


}
