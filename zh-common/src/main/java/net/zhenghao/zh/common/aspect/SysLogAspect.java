package net.zhenghao.zh.common.aspect;

import java.lang.reflect.Method;


import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.zhenghao.zh.common.annotation.SysLog;
import net.zhenghao.zh.common.constant.SystemConstant;
import net.zhenghao.zh.common.entity.R;
import net.zhenghao.zh.common.entity.SysLogEntity;
import net.zhenghao.zh.common.manager.SysLogManager;
import net.zhenghao.zh.common.utils.IPUtils;
import net.zhenghao.zh.common.utils.JSONUtils;
import net.zhenghao.zh.common.utils.ShiroUtils;

/**
 * 系统日志,切面处理类
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date  :2017年11月30日 下午1:33:58
 * SysLogAspect.java
 */
@Aspect
@Component
public class SysLogAspect {

	@Autowired
	private SysLogManager sysLogManager;
	
	@Pointcut("@annotation(net.zhenghao.zh.common.annotation.SysLog)")
	public void logPointCut() {
		
	}
	
	/**
	 * 操作异常日志
	 * @param joinPoint
	 * @param ex
	 */
	@AfterThrowing(pointcut = "logPointCut()", throwing = "ex")
	public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		SysLogEntity sysLog = new SysLogEntity();
		
		sysLog.setType(SystemConstant.LogType.ERROR.getValue());
		
		SysLog sysLogAnnotation = method.getAnnotation(SysLog.class);
		if(sysLogAnnotation != null){
			//注解上的描述
			sysLog.setOperation(sysLogAnnotation.value());
		}
		//请求的方法名
		//例:net.zhenghao.zh.common.controller.SysLogController
		String className = joinPoint.getTarget().getClass().getName();
		//例:batchRemoveAll
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");
		//请求的参数
		Object[] args = joinPoint.getArgs();
		try{
			String params = JSONUtils.beanToJson(args[0]);
			sysLog.setParams(params);
		}catch (Exception e){

		}
		
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr());

		//用户名
		sysLog.setUserId(ShiroUtils.getUserId());
		sysLog.setUsername(ShiroUtils.getUserEntity().getUsername());
		
		//操作状态和结果
		sysLog.setResult(SystemConstant.StatusType.DISABLE.getValue());
		sysLog.setRemark("异常信息：" + ex.getMessage());

		sysLogManager.saveLog(sysLog);
	}
	
	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法(这里应该是获取被注解的方法执行时长)
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		saveSysLog(point, time, result);
		return result;
	}
	
	
	private void saveSysLog(ProceedingJoinPoint joinPoint, long time, Object result) {
		//反射获取一个方法中的参数名（不是类型）
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		SysLogEntity sysLog = new SysLogEntity();
		SysLog syslog = method.getAnnotation(SysLog.class);
		if (syslog != null) {
			//获取注解上面的描述,set到sysLog类中
			sysLog.setOperation(syslog.value());
			String type = syslog.type();
			if (StringUtils.isNotBlank(type)) {
				sysLog.setType(SystemConstant.LogType.valueOf(type).getValue());
			} else {
				sysLog.setType(SystemConstant.LogType.OPERATION.getValue());
			}
		}
		//请求的方法名
		//例:net.zhenghao.zh.common.controller.SysLogController
		String className = joinPoint.getTarget().getClass().getName();
		//例:batchRemoveAll
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");
		//请求的参数
		Object[] args = joinPoint.getArgs();
		try {
			String params = JSONUtils.beanToJson(args[0]);
			sysLog.setParams(params);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr());
		
		//用户名
		sysLog.setUserId(ShiroUtils.getUserId());
		sysLog.setUsername(ShiroUtils.getUserEntity().getUsername());
		
		sysLog.setTime(time);
		
		//操作执行状态
		if (result instanceof R) {
			R r = (R) result;
			int code = (int) r.get("code");
			if(code == 0) {
				//操作成功
				sysLog.setResult(SystemConstant.StatusType.ENABLE.getValue());

				//响应时间：ms
				sysLog.setRemark("响应时间：" + time + "ms");
			} else {
				sysLog.setResult(SystemConstant.StatusType.DISABLE.getValue());
				sysLog.setRemark(String.valueOf(r.get("msg")));
			}
		}
		//保存系统日志
		sysLogManager.saveLog(sysLog);
	}
	
}
