package net.zhenghao.zh.shiro.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

import net.zhenghao.zh.common.entity.R;
import net.zhenghao.zh.common.utils.MD5Utils;
import net.zhenghao.zh.common.utils.ShiroUtils;

/**
 * 用户controller
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date  :2017年12月7日 下午3:21:53
 * SysLoginController.java
 */
@Controller
@RequestMapping("/sys")
public class SysLoginController {
	
	@Autowired
	private Producer producer;
	
	/**
	 * 验证码
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/captcha.jpg")
	public void captcha(HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		
		//生成文字验证码
		String text = producer.createText();
		
		//生成图片验证码
		BufferedImage image = producer.createImage(text);
		//保存到shiro session
		ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
		
		//将图片写入页面
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public R login(String username, String password, String captcha) {
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if (!captcha.equalsIgnoreCase(kaptcha)) {
			return R.error("验证码不正确");
		}
		
		try {
			Subject subject = ShiroUtils.getSubject();
			//shiro盐值加密
			password = MD5Utils.encrypt(username, password);
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
		} catch (UnknownAccountException e) {
			return R.error(e.getMessage());
		} catch (IncorrectCredentialsException e) {
			return R.error(e.getMessage());
		} catch (LockedAccountException e) {
			return R.error(e.getMessage());
		} catch (AuthenticationException e) {
			return R.error("账户验证失败");
		}
		
		return R.ok();
	}
	
	
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout() {
		ShiroUtils.logout();
		return "redirect:login.html";
	}

}
