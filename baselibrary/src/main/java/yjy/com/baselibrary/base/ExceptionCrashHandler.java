package yjy.com.baselibrary.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2017/6/4.
 * 单例模式
 */
public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static ExceptionCrashHandler mInstance;
    private static final String TAG = "ExceptionCrashHandler";
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public static ExceptionCrashHandler getInstance(){
        if(mInstance == null){
            synchronized(ExceptionCrashHandler.class){
                if(mInstance ==null){
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }

        return mInstance;
    }

    private Context mContext;
    public void init(Context context){
        this.mContext = context;
        //设置全局异常处理类
        Thread.currentThread().setUncaughtExceptionHandler(this);

        //获取系统默认的处理机制，不然就不会处理系统的默认的异常处理机制
        mDefaultExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e(TAG,"报异常");

        //写入本地文件 thowable 当前版本 应用的信息 手机信息


        //保存当前文件，在下一次应用启动时上传
        String cashFileName = saveInfoToSD(throwable);

        Log.e(TAG,"FILENAME -->"+cashFileName);

        cacheCrashFile(cashFileName);


        //系统处理异常
        mDefaultExceptionHandler.uncaughtException(thread,throwable);
    }

    private void cacheCrashFile(String filename){
        SharedPreferences sp = mContext.getSharedPreferences("crash",Context.MODE_PRIVATE);
        sp.edit().putString("CRASH_FILE_NAME",filename).commit();
    }

    /****
     * 获取异常文件*/
    public File getCrashFile(){
        String crashFile = mContext.getSharedPreferences("crash",Context.MODE_PRIVATE)
                .getString("CRASH_FILE_NAME","");
        return new File(crashFile);
    }

    private String saveInfoToSD(Throwable throwable){
        String filename = null;
        StringBuffer sb = new StringBuffer();
        //组成异常消息
        for(Map.Entry<String,String> entry :obtainSimpleInfo(mContext).entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }


        sb.append(obtainExpectionInfo(throwable));

        //保存文件
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File dir = new File(mContext.getFilesDir()+File.separator+"crash"
            +File.separator);

            if(dir.exists()){
                deleteDir(dir);
            }

            if(!dir.exists()){
                dir.mkdir();
            }

            try{
               filename = dir.toString()+
                       File.separator +
                       getAssigin("yyyy_MM_dd_HH_mm") + ".txt";

                FileOutputStream fos = new FileOutputStream(filename);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return filename;

    }

    /**
     * 添加时间信息
     * @param date**/
    private String getAssigin(String date) {
        DateFormat dateFormat = new SimpleDateFormat(date);
        long currentTime = System.currentTimeMillis();
        return dateFormat.format(currentTime);
    }

    private boolean deleteDir(File dir) {
        if(dir.isDirectory()){
            File[] children = dir.listFiles();
            for(int i = 0;i<children.length;i++){
                children[i].delete();
            }
        }
        return true;
    }

    /**保存异常信息
     * @param throwable***/
    private String obtainExpectionInfo(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    /**保存手机信息
     * @param mContext***/
    private Map<String,String> obtainSimpleInfo(Context mContext) {
        HashMap<String,String> map = new HashMap<>();
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = packageManager.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);

        }catch (Exception e){
            e.printStackTrace();
        }
        map.put("versonName",mPackageInfo.versionName);
        map.put("VersonCode",mPackageInfo.versionCode+"");
        map.put("MODEL",Build.MODEL);
        map.put("SDK_INT",Build.VERSION.SDK_INT+"");
        map.put("PRODUCT",Build.PRODUCT+"");
        map.put("MOBILE_INFO",getMobileInfo());
        return map;
    }

    public static String getMobileInfo(){
        StringBuffer buffer = new StringBuffer();
        try{
            Field[] fields = Build.class.getDeclaredFields();
            for(Field field : fields){
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                buffer.append(name+" = "+value);
                buffer.append("\n");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
