package yjy.com.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by asus on 2017/5/31.
 */
public class ViewFinder {

    private Activity mActivity;
    private View mView;

    public ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    public ViewFinder(View view) {
        this.mView = view;
    }

    public View findViewById(int ViewId){
        return mActivity != null?mActivity.findViewById(ViewId) : mView.findViewById(ViewId);
    }

}
