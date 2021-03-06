package net.zhenghao.zh.wechat.service.impl;

import net.zhenghao.zh.common.entity.Query;
import net.zhenghao.zh.common.entity.R;
import net.zhenghao.zh.common.utils.CommonUtils;
import net.zhenghao.zh.common.utils.JSONUtils;
import net.zhenghao.zh.wechat.entity.WechatButtonEntity;
import net.zhenghao.zh.wechat.entity.WechatErrorEntity;
import net.zhenghao.zh.wechat.entity.WechatMenuEntity;
import net.zhenghao.zh.wechat.manager.WechatMenuManager;
import net.zhenghao.zh.wechat.service.WechatMenuService;
import net.zhenghao.zh.wechat.utils.MenuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 微信菜单
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date  :2018年4月18日 下午2:51:12
 * SysMenuServiceImpl.java
 */
@Service("wechatMenuService")
public class WechatMenuServiceImpl implements WechatMenuService {
	
	@Autowired
	private WechatMenuManager wechatMenuManager;

	@Override
	public List<WechatMenuEntity> findAllRecursion() {
		return wechatMenuManager.findAllRecursion();
	}

	@Override
	public List<WechatMenuEntity> listMenu(Map<String, Object> params) {
		Query query = new Query(params);
		List<WechatMenuEntity> menuList = wechatMenuManager.listMenu(query);
		return menuList;
	}

	@Override
	public R listButton() {
		List<WechatMenuEntity> menuList = wechatMenuManager.listButton();
		WechatMenuEntity root = new WechatMenuEntity();
		root.setId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);
		return CommonUtils.msgNotNull(menuList);
	}

	@Override
	public R saveMenu(WechatMenuEntity menu) {
		int count = wechatMenuManager.saveMenu(menu);
		return CommonUtils.msg(count);
	}

	@Override
	public R getMenuById(Long id) {
		WechatMenuEntity menu = wechatMenuManager.getMenuById(id);
		return CommonUtils.msg(menu);
	}

	@Override
	public R updateMenu(WechatMenuEntity menu) {
		int count = wechatMenuManager.updateMenu(menu);
		return CommonUtils.msg(count);
	}

	@Override
	public R batchRemove(Long[] id) {
		int count = wechatMenuManager.batchRemove(id);
		return CommonUtils.msg(id, count);
	}

	@Override
	public R submit() {
		List<WechatMenuEntity> menuList = wechatMenuManager.findAllRecursion();
		WechatErrorEntity error = new WechatErrorEntity();
		if (menuList.size() > 0) {
			WechatButtonEntity button = new WechatButtonEntity();
			button.setButton(menuList);
			error = MenuUtils.update(button);
		} else {
			return R.error("提交失败，未配置菜单信息!");
		}
		if (error.getErrcode() == 0) {
			return R.ok("提交成功");
		} else {
			return R.error("提交失败，错误代码" + error.getErrcode() + ",错误信息" + error.getErrmsg());
		}
	}

	@Override
	public R delete() {
		WechatErrorEntity error = MenuUtils.delete();
		if (error.getErrcode() == 0) {
			return R.ok("删除成功");
		} else {
			return R.error("删除失败，错误代码" + error.getErrcode() + ",错误信息" + error.getErrmsg());
		}
	}

}
