package net.zhenghao.zh.wechat.handler;

import net.zhenghao.zh.wechat.entity.ReceiveXmlEntity;

/**
 * 🙃
 * 🙃 消息处理器接口
 * 🙃
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date :2018/4/23 14:28
 * MessageHandler.java
 */
public interface MessageHandler {

    void dealMessage(ReceiveXmlEntity receiveXmlEntity);

}
