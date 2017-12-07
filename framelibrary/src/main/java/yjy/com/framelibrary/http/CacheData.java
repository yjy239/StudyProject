package yjy.com.framelibrary.http;

/**
 * Created by asus on 2017/7/12.
 */
public class CacheData {
    private String mUrlKey;

    public String getmResultJson() {
        return mResultJson;
    }

    private String mResultJson;

    public CacheData(){

    }

    public CacheData(String mUrlKey, String mResultJson) {
        this.mUrlKey = mUrlKey;
        this.mResultJson = mResultJson;
    }
}
