package net.zhenghao.zh.shiro.dao;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import net.zhenghao.zh.common.dao.BaseMapper;
import net.zhenghao.zh.common.entity.Query;
import net.zhenghao.zh.common.entity.SysUserEntity;

/**
 * 系统用户dao
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date  :2017年12月6日 上午10:42:58
 * SysUserMapper.java
 */
@MapperScan
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

	SysUserEntity getByUserName(String username);
	/**
	 * 根据用户id得到对应菜单id
	 * @param userId
	 * @return
	 */
	List<Long> listAllMenuId(Long userId);
	
	/**
	 * 根据用户名密码修改密码
	 * @param query
	 * @return
	 */
	int updatePswdByUser(Query query);
	
	/**
	 * 批量修改用户状态
	 * @param query
	 * @return
	 */
	int updateUserStatus(Query query);
	
	/**
	 * 根据用户id修改密码
	 * @param user
	 * @return
	 */
	int updatePswd(SysUserEntity user);
}
