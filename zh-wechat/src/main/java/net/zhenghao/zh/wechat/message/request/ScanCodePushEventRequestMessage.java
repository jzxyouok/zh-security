package net.zhenghao.zh.wechat.message.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 🙃
 * 🙃 扫码推事件
 * 🙃
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date :2018/4/27 11:35
 * ScanCodePushEventRequestMessage.java
 */
@XStreamAlias("xml")
public class ScanCodePushEventRequestMessage extends EventRequestMessage {

    @XStreamAlias("ScanCodeInfo")
    private ScanCodeInfo scanCodeInfo = new ScanCodeInfo();

    public ScanCodeInfo getScanCodeInfo() {
        return scanCodeInfo;
    }

    public void setScanCodeInfo(ScanCodeInfo scanCodeInfo) {
        this.scanCodeInfo = scanCodeInfo;
    }
}
