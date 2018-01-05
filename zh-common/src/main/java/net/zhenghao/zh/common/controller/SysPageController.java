package net.zhenghao.zh.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统页面视图
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date  :2017年12月12日 上午9:09:25
 * SysPageController.java
 */
@Controller
public class SysPageController {

	/**
	 * 页面跳转
	 * @param module
	 * @param function
	 * @param url
	 * @return
	 */
	@RequestMapping("{module}/{function}/{url}.html")
	public String page(@PathVariable("module") String module, @PathVariable("function") String function,
			@PathVariable("url") String url) {
		return module + "/" + function + "/" +url + ".html";
	}
	
	
	/**
	 * 页面跳转
	 * @param module
	 * @param url
	 * @return
	 */
	@RequestMapping("{module}/{url}.html")
	public String page(@PathVariable("module") String module, @PathVariable("url") String url) {
		return module + "/" +url + ".html";
	}
}
