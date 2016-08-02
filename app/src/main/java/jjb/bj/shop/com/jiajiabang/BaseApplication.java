package jjb.bj.shop.com.jiajiabang;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.aliyun.mbaas.oss.OSSClient;
import com.aliyun.mbaas.oss.model.AccessControlList;
import com.aliyun.mbaas.oss.model.TokenGenerator;
import com.aliyun.mbaas.oss.util.OSSToolKit;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zll on 2016/7/11.
 * 全局的application类
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //创建默认的imageloader配置函数
        //ImageLoaderConfiguration configuration=ImageLoaderConfiguration.createDefault(this);
        //初始化imageloader
        //ImageLoader.getInstance().init(configuration);
        //初始化imageloader
        initImageLoader(getApplicationContext());
        intOSS(this);
    }

//

    private static BaseApplication socketApp;

    public BaseApplication() {
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static BaseApplication getInstance() {
        if (null == socketApp) {
            socketApp = new BaseApplication();


        }
        return socketApp;
    }


    private List<Boolean> analyze = new ArrayList<Boolean>();

    public List<Boolean> getAnalyze() {
        return analyze;
    }

    public void setAnalyze(List<Boolean> analyze) {
        this.analyze = analyze;
    }

    /**
     * ip地址
     */
    private String serverIp = "http://192.168.0.105:8080/sys/rest/";
    /**
     * 广告图更换时间间隔
     */
    public static int SCROLL_TIME = 5000;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    /**
     * 发送信息的端口号
     */
    private int sendPort = 8080;

    public int getSendPort() {
        return sendPort;
    }

    public void setSendPort(int sendPort) {
        this.sendPort = sendPort;
    }

    /**
     * 接收信息的端口号
     */
    private int receivePort = 20058;

    public int getReceivePort() {
        return receivePort;
    }

    public void setReceivePort(int receivePort) {
        this.receivePort = receivePort;
    }

    private Double jingDu = 0.0, weiDu = 0.0;//经纬度

    private String newVersionAdd;//更新版本地址

    private int SCREEN_WIDTH;//屏幕宽高
    private int SCREEN_HEIGHT;
//例子
    private int schoolNUM = 1;//学校字典编号

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private JSONArray moveMentList;

      public JSONArray getMimeForwardList() {
        return mimeForwardList;
    }

    public void setMimeForwardList(JSONArray mimeForwardList) {
        this.mimeForwardList = mimeForwardList;
    }

    private JSONArray mimeForwardList;

    public JSONArray getMoveMentList() {
        return moveMentList;
    }

    public void setMoveMentList(JSONArray moveMentList) {
        this.moveMentList = moveMentList;
    }







    static final String accessKey = "ukZxh4uCD6VWvwd2"; // 测试代码没有考虑AK/SK的安全性
    static final String screctKey = "H6NsydEk5SJUXb3pWYyj48etiWUgeq";

    public void intOSS(BaseApplication customApplication) {
        OSSClient.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
            @Override
            public String generateToken(String httpMethod, String md5, String type, String date,
                                        String ossHeaders, String resource) {
                String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n" + ossHeaders
                        + resource;
                return OSSToolKit.generateToken(accessKey, screctKey, content);
            }
        });
        OSSClient.setGlobalDefaultHostId("oss-cn-hangzhou.aliyuncs.com"); // 设置全局默认数据中心域名
        OSSClient.setGlobalDefaultACL(AccessControlList.PRIVATE); // 设置全局默认bucket访问权限
        OSSClient.setApplicationContext(getApplicationContext()); // 传入应用程序context
    }

    private void initImageLoader(Context context) {

        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "bjJjb/Images");
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 16) * 1024 * 1024; // 1/8 of app
            // memory limit
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                /*.showImageOnLoading(R.drawable.default_loading_100x100)
                .showImageForEmptyUri(R.drawable.default_loading_100x100)
                .showImageOnFail(R.drawable.default_loading_100x100)*/
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .memoryCacheSize(memoryCacheSize)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .defaultDisplayImageOptions(defaultDisplayImageOptions)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


}
