package com.dhsy.async.job;


import com.dhsy.async.service.KingdeeService;
import com.dhsy.async.service.WeimoService;
import com.dhsy.async.utils.ClientConfig;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 任务Handler示例（Bean模式）
 *
 * 开发步骤：
 * 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHandler(value="Step1InitUsersJobHandler")
@Component
public class Step1InitUsersJobHandler extends IJobHandler {
	@Autowired
	private KingdeeService kingdeeService;

	@Autowired
	private WeimoService weimoService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		kingdeeService.getAccessToken();
		// part of kingdee
		if (StringUtils.isNotBlank(ClientConfig.kingdee_access_token)) {
			kingdeeService.getUserList();
			kingdeeService.setBalance();
			XxlJobLogger.log("获取金蝶会员个数：" +ClientConfig.mapForKingdee.size());
		}

		weimoService.getAccessToken();
		//part of weimo
		if(StringUtils.isNotBlank(ClientConfig.weimo_access_token)){
			weimoService.getUserList();
			XxlJobLogger.log("获取微盟会员个数：" +ClientConfig.mapForWeimo.size());
		}

		return SUCCESS;
	}
}
