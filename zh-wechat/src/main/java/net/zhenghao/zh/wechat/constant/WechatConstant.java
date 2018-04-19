package net.zhenghao.zh.wechat.constant;

/**
 * 🙃
 * 🙃 微信常量
 * 🙃
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date :2018/4/17 15:57
 * WechatConstant.java
 */
public class WechatConstant {

    /**
     * 获取access_token的url
     */
    public static final String ACCESS_TOKEN_FETCH_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     * 提交菜单
     */
    public static final String MENU_UPDATE_FETCH_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * 删除菜单
     */
    public static final String MENU_DELETE_FETCH_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
}
