package jjb.bj.shop.com.jiajiabang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jjb.bj.shop.com.jiajiabang.activity.BaseActivity;


/**
 * Created by zll on 2016/7/11.
 * 所有Fragment的基类
 */
public class FinalFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseActivity ss=(BaseActivity) getActivity();
       //ss.hideProgressToast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseActivity ss=(BaseActivity) getActivity();
        //ss.hideProgressToast();
    }

}
