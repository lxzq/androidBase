package jjb.bj.shop.com.jiajiabang.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zll on 2016/7/11.
 * 网络访问工具类
 */
public class HttpTools {
    /**
     * 网络下载数据
     */

    /**
     * get方式
     *
     * @param :String url:网址
     * @return:byte[]:下载后的数据为数组格式
     */
    public static byte[] getDataFromUrl(String url) {
        byte[] data = null;

        HttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                data = EntityUtils.toByteArray(httpResponse.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return data;
    }

    /**
     * get方式
     *
     * @param :String url:网址
     * @return:String:下载后的数据为字符串格式
     */
    public static String getStringFromUrl(String url) {
        String data = null;
        HttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                data = EntityUtils.toString(httpResponse.getEntity());
            }else{
                Log.d("http返回",httpResponse.getStatusLine().getStatusCode()+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return data;
    }

    /**
     * post提交数据到服务器
     *
     * @param url_path:网址
     * @param encode:编码格式
     * @return
     */
    public static String sendPostMessage(String url_path, JSONObject json, String encode) {
        HttpClient httpClient = new DefaultHttpClient();
        JSONObject js=json;

        System.out.println("调用网络接口");
        if (encode == null)
            encode = HTTP.UTF_8;
        try {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("jsonString", js.toString()));
            //请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
            //读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
//			HttpPost httpPost = new HttpPost("http://172.16.7.111:8080");
            System.out.println(url_path);
            HttpPost httpPost = new HttpPost(url_path);
//            HttpEntity entity = new UrlEncodedFormEntity(list, encode);
            StringEntity entity = new StringEntity(js.toString(),"utf-8");//解决中文乱码问题
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);

            System.out.println("!+++++++++++++" + httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String isReturn = EntityUtils.toString(httpResponse.getEntity(), encode);
                isReturn = isReturn.replace("\\", "");

                System.out.println("返回内容是：" + isReturn);
                return isReturn;
            } else {
                return "&&" + httpResponse.getStatusLine().getStatusCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return null;
    }

    public static String sendGetMessage(String url_path) {
        System.out.println("进入get方式");
        String uriAPI = url_path;
     /*建立HTTP Get对象*/
        HttpGet httpRequest = new HttpGet(uriAPI);
        try {
       /*发送请求并等待响应*/
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
       /*若状态码为200 ok*/
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
         /*读*/
                String strResult = EntityUtils.toString(httpResponse.getEntity());
         /*去没有用的字符*/
//         strResult = eregi_replace("(\r\n|\r|\n|\n\r)","",strResult);
//         mTextView1.setText(strResult);
            } else {
//         mTextView1.setText("Error Response: "+httpResponse.getStatusLine().toString());
            }
        } catch (ClientProtocolException e) {
//       mTextView1.setText(e.getMessage().toString());
            e.printStackTrace();
        } catch (IOException e) {
//       mTextView1.setText(e.getMessage().toString());
            e.printStackTrace();
        } catch (Exception e) {
//       mTextView1.setText(e.getMessage().toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查是否有网络连接
     *
     * @param context
     * @return
     */

    public static boolean isNetConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        System.out.println("网络正常");
                        return true;
                    }
                }
            }
        } catch (Exception e) {
// TODO: handle exception
            Log.v("error", e.toString());
        }
        return false;
    }

}
