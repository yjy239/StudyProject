package yjy.com.baselibrary.fix;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by asus on 2017/6/11.
 */
public class FixClassManager {

    private Context mContext;
    private File mDexDir;
    private static final String TAG = "FixClassManager";


    public FixClassManager(Context context){
        this.mContext = context;
        File file = new File(Environment.getExternalStorageDirectory(),"");
        //获取应用可以访问的dex目录
        this.mDexDir = context.getDir("odex",Context.MODE_PRIVATE);

    }


    /**
     * 修复dex包
     * @param dexPath
     * **/
    public void fixDexClass(String dexPath) throws Exception {

        //获取下载好补丁的补丁dexelement
        File srcFile = new File(dexPath);

        if(!srcFile.exists()){
            throw new FileNotFoundException(dexPath);
        }

        File destFile = new File(mDexDir,srcFile.getName());
        if(destFile.exists()){
            Log.d(TAG, "patch [" + dexPath + "] has be loaded.");
            return;
        }


        //移动到app可以访问的dex的目录下，最后变成dexclassloader
        //复制文件
        copyFile(srcFile,destFile);

        //classloader 读取dex路径
        ArrayList<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);

        fixDexFiles(fixDexFiles);


    }


    /***
     *把dexElement注入回去
     * @param classLoader
     * @param dexElements
     * **/
    private void injectDexElement(ClassLoader classLoader, Object dexElements) throws Exception {
        Field pathlistFeild = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathlistFeild.setAccessible(true);
        Object pathList = pathlistFeild.get(classLoader);

        //获取pathlist中的dexElements
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList,dexElements);
    }




    /***
     * 获取dexelement
     * @param classLoader
     * **/
    private Object getElementByClassLoader(ClassLoader classLoader) throws Exception{
        //先获取pathlist
        Field pathlistFeild = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathlistFeild.setAccessible(true);
        Object pathList = pathlistFeild.get(classLoader);

        //获取pathlist中的dexElements
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        return dexElementsField.get(pathList);
    }

    /***合并数组
     * **/
    public static Object combineArray(Object arrayLhs,Object arrayRhs){
        Class<?> loadClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j =i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(loadClass,j);
        for(int k = 0;k<j;++k){
            if(k<i){
                Array.set(result,k,Array.get(arrayLhs,k));
            }else {
                Array.set(result,k,Array.get(arrayRhs,k-1));
            }

        }
        return  result;
    }

    /***
     * 复制文件
     * @param src
     * @param  dest**/
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }


    /***加载全部的修复包**/
    public void loadFixDex() throws Exception{
        File[] dexFiles = mDexDir.listFiles();
        List<File> fixDexFiles = new ArrayList<>();
        for(File fixFile : dexFiles){
            if(fixFile.getName().endsWith(".dex")){
                fixDexFiles.add(fixFile);
            }
        }
        fixDexFiles(fixDexFiles);
    }

    private void fixDexFiles(List<File> fixDexFiles) throws Exception{
        //获取运行的dexElement
        ClassLoader applicationClassLoader = mContext.getClassLoader();

        //反射获取BaseClassLoader中的pathlist
        Object applicationdexElements = getElementByClassLoader(applicationClassLoader);

        File optimizedDirectory = new File(mDexDir,"odex");
        if(!optimizedDirectory.exists()){
            optimizedDirectory.mkdir();
        }

        //修复
        //转化为classloader,获取其中的dexElements,最后插入运来dexElements前面
        for(File fixDexFile : fixDexFiles){
            //需要fixdexFile路径，解压路径，library路径,so文件位置,父classloader
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(fixDexFile.getAbsolutePath(),
                    optimizedDirectory,null,applicationClassLoader);
            Object fixDexElements = getElementByClassLoader(fixDexClassLoader);
            //把下载好的dexelement的补丁放在运行的dexelement最前面

            //applicationClassLoader中的dexElement是一个数组,数据之间的合并
            applicationdexElements= combineArray(fixDexElements,applicationdexElements);
        }

        //注入回原来的类
        injectDexElement(applicationClassLoader,applicationdexElements);

    }
}
