package yjy.com.baselibrary.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yjy239 on 2017/5/31.
 */
//@Target(ElementType.FIELD) 代表annation的位置 ，FIELD 属性,Method 方法,TYPE 类 CONSTRUCTOR 构造函数
//RetentionPolicy.CLASS 什么时候生效 CLASS编译时，RUNTIME 运行时 SOURCE 源码
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewById {
    //添加一个内容
    int value();
}
