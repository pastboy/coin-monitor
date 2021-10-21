package com.sh.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Slf4j
public class DingDingUtil {
    //发送超时时间10s
    private static final int TIME_OUT = 10000;

    public static String sendMsg(String content) {
        return sendMsg(null, null, content, null);
    }

    /**
     * 钉钉机器人文档地址https://ding-doc.dingtalk.com/doc#/serverapi2/qf2nxq
     *
     * @param webhook1（可选）
     * @param secretStr （可选）    安全设置 3选1【方式一，自定义关键词 】 【方式二，加签 ，创建机器人时选择加签 secret以SE开头】【方式三，IP地址（段）】
     * @param content    发送内容
     * @param mobileList 通知具体人的手机号码列表 （可选）
     * @return
     */
    public static String sendMsg(String webhook1, String secretStr, String content, List<String> mobileList) {
        String webhook = "https://oapi.dingtalk.com/robot/send?access_token=c00d9ac0c43beebbdacccac7a170c550fff6231022787ef93f060c47435d17f8";
        String secret = "SEC68fcbc2ce2f9c1d7be253cc4e816f752f01d2738a60738c7ad3242054669e85a";
        try {
            if (StrUtil.isNotBlank(webhook1)) {
                webhook = webhook1;
            }
            if (StrUtil.isNotBlank(secretStr)) {
                secret = secretStr;
            }
            //钉钉机器人地址（配置机器人的webhook）
            if (StrUtil.isNotBlank(secret)) {
                Long timestamp = System.currentTimeMillis();
                String sign = getSign(timestamp, secret);
                webhook = new StringBuilder(webhook)
                        .append("&timestamp=")
                        .append(timestamp)
                        .append("&sign=")
                        .append(sign)
                        .toString();
            }
            //是否通知所有人
            boolean isAtAll = false;
            //组装请求内容
            String reqStr = buildReqStr(content, isAtAll, mobileList);
            //推送消息（http请求）
            String result = postJson(webhook, reqStr);
            log.info("推送结果result == " + result);
            return result;
        } catch (Exception e) {
            log.info("发送群通知异常 异常原因：{}", e.getStackTrace());
            return null;
        }
    }

    /**
     * 组装请求报文
     * 发送消息类型 text
     *
     * @param content
     * @return
     */
    private static String buildReqStr(String content, boolean isAtAll, List<String> mobileList) {
        //消息内容
        Map<String, String> contentMap = Maps.newHashMap();
        contentMap.put("content", content);
        //通知人
        Map<String, Object> atMap = Maps.newHashMap();
        //1.是否通知所有人
        atMap.put("isAtAll", isAtAll);
        //2.通知具体人的手机号码列表
        atMap.put("atMobiles", mobileList);
        Map<String, Object> reqMap = Maps.newHashMap();
        reqMap.put("msgtype", "text");
        reqMap.put("text", contentMap);
        reqMap.put("at", atMap);
        return JSON.toJSONString(reqMap);
    }


    private static String postJson(String url, String reqStr) {
        String body = null;
        try {
            body = HttpRequest.post(url).body(reqStr).timeout(TIME_OUT).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * 自定义机器人获取签名
     * 创建机器人时选择加签获取secret以SE开头
     *
     * @param timestamp
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    private static String getSign(Long timestamp, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(Charset.defaultCharset()));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        System.out.println("singn:" + sign);
        return sign;
    }


    public static void main(String[] args) {
        String webhook = "https://oapi.dingtalk.com/robot/send?access_token=c00d9ac0c43beebbdacccac7a170c550fff6231022787ef93f060c47435d17f8";
        String secret = "SEC68fcbc2ce2f9c1d7be253cc4e816f752f01d2738a60738c7ad3242054669e85a";
        List<String> mobileList = Lists.newArrayList();
        mobileList.add("18888888888");
        DingDingUtil.sendMsg(webhook, secret, "1", null);
    }
}
