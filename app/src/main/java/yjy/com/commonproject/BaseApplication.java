package yjy.com.commonproject;

import android.app.Application;


/**
 * Created by asus on 2017/6/4.
 */
public class BaseApplication extends Application{

//    public static PatchManager patchManager;

    @Override
    public void onCreate() {
        super.onCreate();
//        ExceptionCrashHandler.getInstance().init(this);
//        HttpUtils.init(new OkHttpEngine());
//        SkinManager.getInstance().init(this);
        //初始化热修复
//        try {
//            PackageManager manager = this.getPackageManager();
//            PackageInfo info = manager.getPackageInfo(this.getPackageName(),0);
//            String version = info.versionName;
//            patchManager = new PatchManager(this);
//            patchManager.init(version);
//            patchManager.loadPatch();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        try{
//            FixClassManager manager = new FixClassManager(this);
//            manager.loadFixDex();
//        }catch(Exception e){
//            e.printStackTrace();
//        }



    }
}
