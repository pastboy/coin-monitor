//package com.sh.third.itchat;
//
//import cn.zhouyafeng.itchat4j.Wechat;
//import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class WechatStarter implements InitializingBean {
//
//    @Autowired
//    private IMsgHandlerFace msgHandlerFace;
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        String qrPath = "D://wechat//login"; // 保存登陆二维码图片的路径，这里需要在本地新建目录
////        IMsgHandlerFace msgHandler = new SimpleDemo(); // 实现IMsgHandlerFace接口的类
////        Wechat wechat = new Wechat(msgHandlerFace, qrPath); // 【注入】
////        wechat.start();
//    }
//}
