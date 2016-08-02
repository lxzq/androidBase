package jjb.bj.shop.com.jiajiabang.tools;

import android.app.Activity;
import android.content.Intent;
import com.aliyun.mbaas.oss.model.OSSException;

import java.util.Calendar;
import java.util.Date;

import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.AccessControlList;
import com.aliyun.mbaas.oss.storage.OSSBucket;
import com.aliyun.mbaas.oss.storage.OSSFile;
import com.aliyun.mbaas.oss.storage.TaskHandler;
import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 阿里云图片管理
 * Created by Administrator on 2016-07-14.
 */
public class OSSImageTools {

    /**
     * 图片选择器返回的代码
     */
     public static final int REQUEST_IMAGE = 1000;
     private static final String IMAGE_FOLDER = "bj";
     public static final String IMAGE_SERVER = "http://image.happycity777.com/";

     private static TaskHandler ossHandler;
     private static OSSBucket jjbBucket;

     static {
        jjbBucket = new OSSBucket("jiajiabang");
        jjbBucket.setBucketACL(AccessControlList.PUBLIC_READ_WRITE); // 如果这个Bucket跟全局默认的访问权限不一致，就需要单独设置
    }
    /**
     * 图片选择器
     * @param activity 界面activity
     * @param mSelectPath 返回存储本地图片的路径（支持多选）
     * @param maxPicNum 允许图片的选择数量 单选 1 ，
     */
    public static void imageSelectorForResult(Activity activity, ArrayList<String> mSelectPath, int maxPicNum){
        Intent intent = new Intent(activity, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxPicNum);
        // 选择模式 多选模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        intent.putExtra(MultiImageSelectorActivity.FILE_TYPE, MultiImageSelectorActivity.IMAGE_FILE);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        activity.startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * 上传图片
     * @param localPath 本地图片路径
     * @param handler 上传图片回调处理
     */
    public static void upload(String localPath,final OSSHandler handler){
        String name=String.valueOf(System.currentTimeMillis());
        String ext=localPath.substring(localPath.lastIndexOf("."));
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        String year=String.valueOf(calendar.get(Calendar.YEAR));
        String month=String.valueOf(calendar.get(Calendar.MONTH)+1);
        final String strPath= String.format("/%s/%s/%s/%s%s",IMAGE_FOLDER, year,month,name,ext);
        final OSSFile ossFile = new OSSFile(jjbBucket, strPath);
        // 指明需要上传的文件的路径， 和文件内 容类型
        ossFile. setUploadFilePath(localPath, ext) ;
        ossHandler = ossFile.uploadInBackground(new SaveCallback() {
            @Override
            public void onSuccess(String objectKey) {
                handler.onSuccess( strPath);
            }
            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {
                handler.onProgress( objectKey,  byteCount,  totalSize);
            }
            @Override
            public void onFailure(String objectKey, OSSException ossException) {
                handler.onFailure(objectKey,ossException);
            }
        });

    }
    public static void cancel(){
        if ( ossHandler != null) {
            ossHandler.cancel(); // 取消上传任务
        }
    }


    public static String getImage_x(String strName) {
       return IMAGE_SERVER + strName + "@1e_117w_117h_1c_0i_1o_90Q_1x.jpg";
    }

    public static String getImage_xx(String strName) {
        return IMAGE_SERVER + strName + "@1e_150w_150h_1c_0i_1o_90Q_1x.jpg";
    }

    public static String getImage_xxx(String strName) {
        return IMAGE_SERVER + strName + "@1e_270w_270h_1c_0i_1o_90Q_1x.jpg";
    }
    /**
     * 返回原图地址
     * @param strName
     * @return
     */
    public static String getImage(String strName) {
        return IMAGE_SERVER + strName;
    }
    /**
     * 返回指定长宽大小图片地址
     * @param strName
     * @param w
     * @param h
     * @return
     */
    public static String getImageFitXY(String strName, int w, int h) {
        return IMAGE_SERVER + strName + "@1e_" + w + "w_" + h
                + "h_1c_0i_1o_90Q_1x";
    }
}
