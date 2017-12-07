package yjy.com.commonproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

import yjy.com.baselibrary.banner.BannerAdapter;
import yjy.com.baselibrary.banner.BannerView;
import yjy.com.baselibrary.http.EngineCallBack;
import yjy.com.baselibrary.http.HttpUtils;

public class MainActivity extends AppCompatActivity {

    private BannerView vp;
    private ArrayList<Integer> testlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp = (BannerView)findViewById(R.id.vp);
        HttpUtils.with(MainActivity.this)
                .url("http://sevenshop.lht9.com/")
                .addParam("id","46")
                .addParam("unique_id","869515024751809")
                .post()
                .execute(new EngineCallBack() {
                    @Override
                    public void onPreExcute(Context context, Map<String, Object> map) {
                        Toast.makeText(MainActivity.this, "pre", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }
                });


        for(int i = 0;i<3;i++){
            testlist.add(R.drawable.default_loading_pic);
        }
        vp.startScroll();
        vp.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                ImageView iv = null;
                if(convertView == null){
                    iv = new ImageView(MainActivity.this);
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                }else {
                    iv = (ImageView)convertView;
                }
                Glide.with(MainActivity.this)
                        .load(testlist.get(position))
                        .into(iv);
                return iv;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });



    }
}
