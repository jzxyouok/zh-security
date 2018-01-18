package net.zhenghao.zh.quartz.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.zhenghao.zh.common.entity.Page;
import net.zhenghao.zh.common.entity.Query;
import net.zhenghao.zh.quartz.dao.QuartzJobMapper;
import net.zhenghao.zh.quartz.entity.QuartzJobEntity;
import net.zhenghao.zh.quartz.manager.QuartzJobManager;

/**
 * 定时任务
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date  :2018年1月18日 上午9:41:35
 * QuartzJobManagerImpl.java
 */
@Component("quartzJobManager")
public class QuartzJobManagerImpl implements QuartzJobManager {
	
	@Autowired
	private QuartzJobMapper quartzJobMapper;

	@Override
	public List<QuartzJobEntity> listForPage(Page<QuartzJobEntity> page, Query query) {
		return quartzJobMapper.listForPage(page, query);
	}

	@Override
	public List<QuartzJobEntity> listNormalJob() {
		return quartzJobMapper.list();
	}

	@Override
	public int saveQuartzJob(QuartzJobEntity job) {
		return quartzJobMapper.save(job);
	}

	@Override
	public QuartzJobEntity getQuartzJobById(Long jobId) {
		return quartzJobMapper.getObjectById(jobId);
	}

	@Override
	public int updateQuartzJob(QuartzJobEntity job) {
		return quartzJobMapper.update(job);
	}

	@Override
	public int batchRemoveQuartzJob(Long[] id) {
		return quartzJobMapper.batchRemove(id);
	}

	@Override
	public int batchUpdate(Long[] jobId, Integer status) {
		Query query = new Query();
		query.put("jobIdList", jobId);
		query.put("status", status);
		return quartzJobMapper.batchUpdate(query);
	}

}
