package yjy.com.framelibrary.db;

import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by asus on 2017/7/5.
 */
public class DaoUtils {
    public static String getTableName(Class<?> clazz){
        return clazz.getSimpleName();
    }

    public static String getColumnType(String type){
        String value = null;
        if(type.contains("String")){
            value =  "text";
        }else if(type.contains("int")){
            value = "Integer";
        }else if(type.contains("boolean")){
            value = "boolean";
        }else if(type.contains("float")){
            value = "float";
        }else if(type.contains("double")){
            value = "double";
        }else if(type.contains("char")){
            value = "varchar";
        }else if(type.contains("long")){
            value = "long";
        }
        return value;
    }

    public static String captialize(String name) {
        if(!TextUtils.isEmpty(name)){
            return name.substring(0,1).toUpperCase(Locale.US) + name.substring(1);
        }
        return "";
    }
}
