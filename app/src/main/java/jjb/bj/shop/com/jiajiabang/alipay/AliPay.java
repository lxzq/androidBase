package jjb.bj.shop.com.jiajiabang.alipay;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 *
 * 阿里支付
 * Created by Administrator on 2016-07-21.
 */
public class AliPay {
     /**
     *商户私钥，pkcs8格式
     */
    private static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANm3fhGwNYGRlpDBCcsnqdnI9NU01JrbA9+mHBBMkif4jiM9o9fwVsA1xBut/KubSPHhBWXifXrUogbQX/HPW+C5NKVw8trgCdB+yibO4JpsDeqOklALhELTb5N9VUDEGOvpAkMf9P9PuZCQ//78EcDbrujzwvWM3mQ8Rm8IsaRfAgMBAAECgYAuDbF+Pl7jHI/T6PGQZsB4EBkyLG24oBGOso2HKRtVly9B5x0MZYuENxtXm5MyHbPtbmL87U8Lt4TamiNhfDJ7z8eYK/j8Zanye4Of9gmuACam2/iCm9T9OKDjzM/882O2I/qYeFOIy+an0r5z9CDvXPvpoNfuLTDR14hEVkb0gQJBAPnT+ehNoua/IczLFAB2KBb3C3ba2BCiczqEhjlgxPhpIZY0iprvgMeQw3wQdaBrZ+pjEJ/M6CS2DMvLIpNQtJ8CQQDfGG4ozT880SuvnLsiTANjpbWDYzbS+8rNmc9j1Pv2z+ALsbxhawnyACSkZW6Itb1Fqgyl2XKQTbxHK5TfrzhBAkBwsdFqnBLe2dsqYXUtXB9rdJdkTwXmQxGNlRhjK6bOk8YF1r+rclx6KXA1N8uHyMuAbJ2kKC+T19df2stG/sHrAkEAgFFt8xvytoldBjowzI5KGMOUPokPXJUk1dMVHUfA0PpJ1JAbWTEW3FHGwaPxysI1pl8jvTTwFx4PICRqEaqXwQJAQqi9krWJTNarmaBnMhFzmNN7oYTFemV6eOUqF0EjVNg3cE99Nn8h922A0YjTaFk/RV/ivLOsKX5nwZT0W3biEQ==";
    /**
     * 签约合作者身份ID
     */
    private static final String PARTNER = "2088711908903334";
    /**
     * 商户收款账号
     */
    private static final String SELLER = "ahjjb777@163.com";

    // 支付宝公钥
    private static final String RSA_PUBLIC = "";

    private String name;
    private String notes;
    private String price;
    private Activity activity;
    private String orderNo;

    /**
     * @param name 商品名称
     * @param notes 商品描述
     * @param price 商品价格(0.01)
     */
    public AliPay(Activity activity , String name,String notes ,String price){
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.activity =activity;
    }

    public void pay(final AliPayListener aliPayListener){
        String orderInfo = getOrderInfo(name, notes, price);
        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                PayResult payResult = new PayResult(result);
                /**
                 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                 * docType=1) 建议商户依赖异步通知
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                if (TextUtils.equals(resultStatus, "9000")) {
                    aliPayListener.onSuccess(orderNo);
                }else {
                    // 判断resultStatus 为非"9000"则代表可能支付失败
                    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        aliPayListener.onError(resultInfo);
                   } else {
                      // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        aliPayListener.onError(resultInfo);
            }
        }
        }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public interface AliPayListener{
        /**
         * 支付成功，返回订单号
         * @param orderNo
         */
        void onSuccess(String orderNo);
        void onError(String msg);
    }


    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";
        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }


    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        orderNo = key;
        return orderNo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
