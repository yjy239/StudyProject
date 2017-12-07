package yjy.com.baselibrary.http;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2017/6/29.
 *自己实现http
 */
public class HttpUtils {

    private static IHttpEngine mHttpEngine = null;

    //请求地址
    private String mUrl;

    //请求方式
    private int mType = GET_TYPE;
    private static final int POST_TYPE = 0x0010;
    private static final int GET_TYPE = 0X0011;

    private Context mContext;
    private Map<String ,Object> mParams;
    private boolean mCache = false;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public HttpUtils(Context context){
        mContext = context;
        mParams = new HashMap<>();
    }

    public static HttpUtils with(Context context){
        return new HttpUtils(context);
    }

    //请求方式
    public HttpUtils post(){
        mType = POST_TYPE;
        return this;
    }
    public HttpUtils get(){
        mType = GET_TYPE;
        return this;
    }

    public HttpUtils cache(boolean isCache){
        mCache = isCache;
        return this;
    }

    //添加url
    public HttpUtils url(String mUrl){
        this.mUrl = mUrl;
        return this;
    }

    //添加参数
    public HttpUtils addParam(String key,Object value){
        mParams.put(key,value);
        return this;
    }

    public HttpUtils addParams(Map<String,Object> mParams){
        mParams.putAll(mParams);
        return this;
    }


    //执行回调
    public void execute(EngineCallBack callback){
        if(callback == null){
            callback = EngineCallBack.DefaultCallback;
        }

        callback.onPreExcute(mContext,mParams);

        //判断执行的方法
        if(mType == GET_TYPE){
            get(mUrl,mParams,callback);
        }else if(mType == POST_TYPE){
            post(mUrl,mParams,callback);
        }
    }

    public void execute(){
        execute(null);
    }



    //初始化引擎
    public static void init(IHttpEngine engine){
        mHttpEngine = engine;
    }

    //切换引擎
    public HttpUtils exchangeEngine(IHttpEngine engine){
        mHttpEngine = engine;
        return this;
    }


    private void get(String url, Map<String, Object> parms, EngineCallBack callBack) {
        mHttpEngine.get(mCache,mContext,url,parms,callBack);
    }


    private void post(String url, Map<String, Object> parms, EngineCallBack callBack) {
        mHttpEngine.post(mCache,mContext,url,parms,callBack);
    }

    public static String joinParms(String url, Map<String, Object> parms) {
        if(parms ==null || parms.size() <= 0){
            return url;
        }

        StringBuffer buffer = new StringBuffer(url);
        if(!url.contains("?")){
            buffer.append("?");
        }else {
            if(!url.endsWith("?")){
                buffer.append("&");
            }
        }

        for(Map.Entry<String,Object> entry : parms.entrySet()){
            buffer.append(entry.getKey()+"="+entry.getValue()+"&");
        }

        buffer.deleteCharAt(buffer.length()-1);
        return buffer.toString();
    }

    public static Class<?> analysisClazzInfo(Object object){
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        return (Class<?>)params[0];
    }
}
