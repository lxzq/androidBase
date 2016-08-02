package jjb.bj.shop.com.jiajiabang.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jjb.bj.shop.com.jiajiabang.BaseApplication;


/**
 * Created by zll on 2016/7/11.
 * 版本控制工具类：用来更新版本
 */
public class VersionTools {

    /**
     * 获取本地版本号并判断是否更新
     */
    public Boolean upgrade(Context cont, int ServersNUM) {
        Boolean ifUpGrade = false;

        try {
            PackageManager pm = cont.getPackageManager();// context为当前Activity上下文
            PackageInfo pi;
            pi = pm.getPackageInfo(cont.getPackageName(), 0);
            String version = pi.versionName;
            int num = pi.versionCode;
            if (ServersNUM > num)
                ifUpGrade = true;

        } catch (PackageManager.NameNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return ifUpGrade;
    }

    /**
     * JSONArray转换成List
     */

    public List jsonArraygetList4JsonArray(String jsonString) {
        JSONObject jsonObject = null;
        Object pojoValue;
        JSONArray JArray;
        List list = new ArrayList();
        try {
            JArray = new JSONArray(jsonString);
            int length = JArray.length();
            for (int n = 0; n < length; n++) {
                jsonObject = JArray.getJSONObject(n);
                list.add(jsonObject);
            }
        } catch (JSONException e1) {
            // TODO 自动生成的 catch 块
            e1.printStackTrace();
        }
        return list;
    }

    /**
     * 获取短信按钮变灰色并延时
     */

    public void changeLoginButton(Message message, Handler handler) {
        final Message mes = message;
        final Handler hand = handler;
        new Thread(new Runnable() {

            public void run() {

                for (int time = 60; time >= 0; time--) {
                    mes.obj = time;
                    hand.sendMessage(mes);
                    try {

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }
                }

            }

        }).start();

    }


    /**
     * 发送用线程
     */

    public void sentData4Severs(Message message, Handler handler,JSONObject json,String add){
        final Message mes = message;
        final Handler hand = handler;
        final JSONObject js = json;
        final String befordadd = add;
        new Thread(new Runnable() {

            public void run() {

                String get = HttpTools.sendPostMessage( BaseApplication.getInstance().getServerIp()+befordadd,js,null);
                if(get!=null){
                    Log.d("myFragmentTab", get);
                }
                Bundle bundle = new Bundle();
                bundle.putString("intent", get);
                mes.setData(bundle);
                mes.obj = get;
                hand.sendMessage(mes);
            }
        } ).start();
    }

//    public void sentData4Severs(Message message, Map<String, String> map, Handler handler, String where) {
//        final Message mes = message;
//        final Map<String, String> jsonMap = map;
//        final Handler hand = handler;
//        final String witch = where;
//        new Thread(new Runnable() {
//
//            public void run() {
//                // TODO 自动生成的方法存根
//
//
//                BaseApplication screen = BaseApplication.getInstance();
//                JSONObject json = new JSONObject(jsonMap);
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("json", json.toString());
//                String get = HttpTools.sendPostMessage(
//                        screen.getServerIp() + ":"
//                                + screen.getSendPort() + "/api/" + witch,
//                        jsonMap, null);
//                Bundle bundle = new Bundle();
//                bundle.putString("intent", get);
//                mes.setData(bundle);
//                mes.obj = get;
//                hand.sendMessage(mes);
//            }
//
//        }).start();
//
//    }

    /**
     * 特殊方法，将json的时间格式变成通用格式
     */
    public String jsonTime2Time(String jsonTime) {
        StringBuffer getTimeStr = new StringBuffer(jsonTime);
        String timeStr;
        timeStr = getTimeStr.substring(6, 19);
        getTimeStr = new StringBuffer(timeStr);
        Time time = new Time();
        time.set(Long.parseLong(timeStr));
        int year = time.year; //2010
        int month = time.month + 1; //9 （0-11）此处值得注意
        int date = time.monthDay; //10
        int hour = time.hour; // 15， 24小时制（0-23）
        int minute = time.minute; //11
        timeStr = year + "-" + month + "-" + date + "  " + hour + ":" + minute;
//		System.out.println("1===========>"+timeStr);
        return timeStr;

    }


}
