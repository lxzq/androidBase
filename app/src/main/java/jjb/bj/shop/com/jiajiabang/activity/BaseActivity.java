package jjb.bj.shop.com.jiajiabang.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import jjb.bj.shop.com.jiajiabang.customView.CustomProgressToast;
import jjb.bj.shop.com.jiajiabang.R;


/**
 * Created by zll on 2016/7/11.
 * 所有activity的基类
 */
public class BaseActivity extends AppCompatActivity {
    private Context mContext = this;
    private static BaseActivity instance = null;
    private CustomProgressToast progressToast = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //统计应用启动数据

        //  PushAgent.getInstance(mContext).onAppStart();
        //获取设备的device_token
        //   String device_token = UmengRegistrar.getRegistrationId(mContext);

    }

    public static BaseActivity getInstance() {
        return instance;
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void onBackPressed() {
        hideProgressToast();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    /**
     * 初始化标题栏1(返回Button)
     *
     * @param isShowBackBtn
     * @param titleText
     * @param rightBtText   按钮上的文字
     * @return 右上角按钮，便于添加事件
     */
    public TextView loadTitleBarBackButton(boolean isShowBackBtn, String titleText,
                                           String rightBtText) {
        // 左上角返回按钮
        TextView leftBtn = (TextView) findViewById(R.id.left_btn);
        leftBtn.setVisibility(isShowBackBtn ? View.VISIBLE : View.GONE);
        leftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                // hideProgressToast();
            }
        });

        // 标题文字
        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        if (titleTv != null) {
            if (titleText == null)
                titleTv.setVisibility(View.INVISIBLE);
            else if (!titleText.equals("")) {
                titleTv.setVisibility(View.VISIBLE);
                titleTv.setText(titleText);
            }
        }

        // 右上角按钮
        TextView rightBtn = (TextView) findViewById(R.id.right_btn);
        if (rightBtn != null) {
            if (rightBtText == null)
                rightBtn.setVisibility(View.GONE);
            else
                rightBtn.setVisibility(View.VISIBLE);
            rightBtn.setText(rightBtText);
        }

        return rightBtn;
    }

    /**
     * 初始化标题栏2(返回ImgView)
     *
     * @param isShowBackBtn
     * @param titleText
     * @param imgId         图片对应的id(参数为0的时候为隐藏)
     * @return 右上角按钮，便于添加事件
     */
    public ImageView loadTitleBarBackImageView(boolean isShowBackBtn, String titleText,
                                               int imgId) {
        // 左上角返回按钮
        TextView leftBtn = (TextView) findViewById(R.id.left_btn);
        leftBtn.setVisibility(isShowBackBtn ? View.VISIBLE : View.GONE);
        leftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                // hideProgressToast();
            }
        });

        // 标题文字
        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        if (titleTv != null) {
            if (titleText == null)
                titleTv.setVisibility(View.INVISIBLE);
            else if (!titleText.equals("")) {
                titleTv.setVisibility(View.VISIBLE);
                titleTv.setText(titleText);
            }
        }

        // 右上角按钮
        ImageView rightImgBt = (ImageView) findViewById(R.id.right_btn1);
        if (rightImgBt != null) {
            if (imgId == 0)
                rightImgBt.setVisibility(View.GONE);
            else
                rightImgBt.setVisibility(View.VISIBLE);
            rightImgBt.setImageResource(imgId);
        }

        return rightImgBt;
    }


    /**
     * 显示加载进度条
     */
    public void showProgressToast() {
        if (progressToast == null) {
            progressToast = CustomProgressToast.makeText(
                    getApplicationContext(), Integer.MAX_VALUE);
            progressToast.setGravity(Gravity.CENTER, 0, 0);
        }
        try {
            progressToast.show();

        } catch (Exception e) {

        }
    }


    /**
     * 隐藏加载进度条
     */
    public void hideProgressToast() {
        if (progressToast != null) {
            progressToast.hide();
            progressToast = null;
        }
    }


    /**
     * 登录提醒
     */
    public void HintToLogin(final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("提示")
                .setMessage("你还未登录！")
                .setCancelable(false)
                .setNegativeButton("去登录",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                               /* startActivityForResult(new Intent(mContext,
                                        LoginActivity.class), requestCode);*/

                            }
                        }).setPositiveButton("取消", null);

        AlertDialog dialog = builder.create();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
                return false;
            }
        });
        dialog.show();
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getWindowWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return (int) (dm.widthPixels);
    }

}
