package jjb.bj.shop.com.jiajiabang.customView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/7/21.
 *
 * @author ezt
 * @title 图片滑动动画时间控制类
 * @describe 如果用默认时间可用不这个类
 * @created 2014年12月9日
 */


public class FixedSpeedScroller extends Scroller {
    Context context;
    private int mDuration = 500;

    public FixedSpeedScroller(Context context) {
        super(context);
        this.context = context;
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
        this.context = context;
    }

    /**
     * 设置滑动时间 ,如果用默认时间可不用这个类 ,反射技术实现
     *
     * @param vp   ViewPager 对象
     * @param time 时间
     */

    public void setDuration(ViewPager vp, int time) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            this.setmDuration(time);
            field.set(vp, this);
        } catch (Exception e) {

        }
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setmDuration(int time) {
        mDuration = time;
    }

    public int getmDuration() {
        return mDuration;
    }
}
