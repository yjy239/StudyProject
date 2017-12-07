package yjy.com.baselibrary.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by yjy239 on 2017/5/31.
 */
public class ViewUtils {


    //activity
    public static void inject(Activity activity){
        inject(new ViewFinder(activity),activity);
    }

    //view
    public static void inject(View view){
        inject(new ViewFinder(view),view);
    }

    //fragment
    public static void inject(View view,Object obj){
        inject(new ViewFinder(view),obj);
    }

    //兼容三种 obj用来反射执行
    public static void inject(ViewFinder finder,Object obj){
        //注入属性
        injectFiled(finder,obj);
        //注入事件
        injectEvent(finder,obj);
    }

    private static void injectEvent(ViewFinder finder, Object obj) {
        //遍历该类所有方法
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        //获取OnClick的属性值
        for(Method method : methods){
           OnClick onclick = method.getAnnotation(OnClick.class);
            if(onclick != null){
                //findViewById 找到view
                int[] viewIds = onclick.value();
                for(int id :viewIds){
                    View v =finder.findViewById(id);

                    //监测网络
                    boolean isCheckNet = method.getAnnotation(CheckNet.class) != null;

                    //setOnClick
                    if(v != null){
                        v.setOnClickListener(new onDeclaredOnClickListener(obj,method,isCheckNet));
                    }
                }
            }

        }
        //反射执行框架

    }

    private static void injectFiled(ViewFinder finder, Object obj) {
        //遍历类中所有属性
        Class<?> clazz = obj.getClass();
        //获取所有属性
        Field[] fields = clazz.getDeclaredFields();

        //获取ViewById属性值
        for(Field field : fields){
            field.setAccessible(true);
            ViewById id = field.getAnnotation(ViewById.class);
            if(id != null){
                int viewId = id.value();
                //findViewById
                View v = finder.findViewById(viewId);
                if(v != null){
                    field.setAccessible(true);
                    //动态注入对应的view
                    try {
                        field.set(obj,v);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }
        }
    }

    public static boolean netWorkAvailable(Context context){
        try {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if(info !=null&&info.isConnected()){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private static class onDeclaredOnClickListener implements View.OnClickListener{

        private Object mObj;
        private Method mMethod;
        private boolean mIsChechNet;

        public onDeclaredOnClickListener(Object mObj, Method mMethod,boolean isCheckNet) {
            this.mObj = mObj;
            this.mMethod = mMethod;
            this.mIsChechNet = isCheckNet;
        }


        @Override
        public void onClick(View view) {
            //判断网络
            if(mIsChechNet){
                Log.e("ctip","check");
                Log.e("net",netWorkAvailable(view.getContext())+"");
                if(!netWorkAvailable(view.getContext())){
                    CheckNet check = mMethod.getAnnotation(CheckNet.class);
                    String tip = check.value();
                    Log.e("tip",tip);
                    if(tip!=null && !tip.equals("")){
                        Toast.makeText(view.getContext(),""+tip,Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(view.getContext(),"请检查网络",Toast.LENGTH_SHORT).show();
                    }

                    return;
                }
            }

            try{
                mMethod.setAccessible(true);
                mMethod.invoke(mObj,view);
            }catch (Exception e){
                e.printStackTrace();
                try {
                    mMethod.invoke(mObj,null);
                }catch (Exception e1){
                    e1.printStackTrace();
                }

            }

        }
    }
}
