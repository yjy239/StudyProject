package yjy.com.baselibrary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by asus on 2017/6/4.
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置layout
        setContentView();

        //初始化头部
        initTitle();

        //初始化界面
        initView();

        //初始化数据
        initData();
    }

    public abstract void setContentView();

    public abstract void initData();

    public abstract void initView();

    public abstract void initTitle();

}
