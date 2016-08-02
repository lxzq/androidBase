package jjb.bj.shop.com.jiajiabang.customView;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by hasee on 2016/7/25.
 */
public class MyFragmentTabHost extends FragmentTabHost{
    public MyFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyFragmentTabHost(Context context) {
        super(context);
    }

    @Override
    public void onTabChanged(String tabId) {
        super.onTabChanged(tabId);
        Log.d("tableChange",tabId);
//        if(){
//
//        }

    }
}
