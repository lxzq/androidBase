package jjb.bj.shop.com.jiajiabang.tools;

import android.os.Handler;

import com.aliyun.mbaas.oss.model.OSSException;

/**
 * OSS 上传回调类
 * Created by Administrator on 2016-07-14.
 */
public class OSSHandler extends Handler {
    /**
     * OSS上传图片成功后回调函数
     * @param strPath 返回上传后的图片路径
     */
    public void onSuccess(String strPath) { }
    /**
     * OSS上传图片进度回调
     * @param strPath 返回上传后图片路径
     * @param byteCount 上传大小
     * @param totalSize 图片总大小
     */
    public void onProgress(String  strPath, int byteCount, int totalSize) { }
    /**
     * OSS上传图片失败回调
     * @param strPath
     * @param ossException
     */
    public void onFailure(String  strPath, OSSException ossException) {}
}
