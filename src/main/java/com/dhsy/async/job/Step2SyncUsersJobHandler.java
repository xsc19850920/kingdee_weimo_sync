package com.dhsy.async.job;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhsy.async.entity.UserForWeimo;
import com.dhsy.async.service.KingdeeService;
import com.dhsy.async.service.WeimoService;
import com.dhsy.async.utils.ClientConfig;
import com.dhsy.async.utils.OkHttpUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
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
@JobHandler(value="Step2SyncUsersJobHandler")
@Component
public class Step2SyncUsersJobHandler extends IJobHandler {
	@Autowired
	private KingdeeService kingdeeService;

	@Autowired
	private WeimoService weimoService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			if (!CollectionUtils.isEmpty(ClientConfig.mapForWeimo.keySet()) || !CollectionUtils.isEmpty(ClientConfig.mapForKingdee.keySet())) {
				Collection<String> updateToKingdeeList = CollectionUtils.subtract(ClientConfig.mapForWeimo.keySet(), ClientConfig.mapForKingdee.keySet());
				Collection<String> updateToWeimoList = CollectionUtils.subtract(ClientConfig.mapForKingdee.keySet(), ClientConfig.mapForWeimo.keySet());

				// part of weimo
				if (CollectionUtils.isNotEmpty(updateToWeimoList) && StringUtils.isNotEmpty( ClientConfig.weimo_access_token)) {
					XxlJobLogger.log("------------同步 (会员信息 金蝶-->微盟) 开始--------------");
					long startTime = new Date().getTime();
					String url = "https://dopen.weimob.com/api/1_0/ec/membership/importUser?accesstoken="+ ClientConfig.weimo_access_token;
					JSONObject json = new JSONObject();
					json.put("importType", 2);
					JSONArray jsonArray = new JSONArray();
					for (String phone : updateToWeimoList) {
						JSONObject phoneJson = new JSONObject();
						phoneJson.put("phone", phone);
						phoneJson.put("getChannel", 5); //客户来源 0—微信公众号、1—微信小程序、3—微商城、4—百度小程序、5—外部导入、6—自有APP
						phoneJson.put("appChannel", 5); //客户来源 0—微信公众号、1—微信小程序、3—微商城、4—百度小程序、5—外部导入、6—自有APP
						jsonArray.add(phoneJson);
					}
					json.put("userList", jsonArray);
					String result = OkHttpUtils.postJsonParams(url, json.toString());
					XxlJobLogger.log(result);
					long endTime = new Date().getTime();
					XxlJobLogger.log("------------同步 (会员信息 金蝶-->微盟) 结束--------------");
					XxlJobLogger.log(String.format("-------耗时:%s 毫秒----", endTime - startTime));
				}

				// part of kingdee
				if (CollectionUtils.isNotEmpty(updateToKingdeeList) && StringUtils.isNotEmpty( ClientConfig.kingdee_access_token) ) {
					XxlJobLogger.log("------------同步 (会员信息 微盟-->金蝶 ) 结束--------------");
					long startTime = new Date().getTime();
					for (String mobile : updateToKingdeeList) {
						UserForWeimo userForWeimo = ClientConfig.mapForWeimo.get(mobile);
						String url = "http://api.kingdee.com/jdyapi/retail/mb/add?access_token=%s&dbid=%s";
						JSONObject json = new JSONObject();
						json.put("number", userForWeimo.getMid());
						json.put("mobile", mobile);
						json.put("name", userForWeimo.getName());
						String result = OkHttpUtils.postJsonParams(
								String.format(url, ClientConfig.kingdee_access_token, ClientConfig.KINGDEE_DBID),
								json.toString());
						XxlJobLogger.log(result);
					}

					long endTime = new Date().getTime();
					XxlJobLogger.log("------------同步 (会员信息 微盟-->金蝶 ) 结束--------------");
					XxlJobLogger.log(String.format("-------耗时:%s 毫秒----", endTime - startTime));
				}

			}
		} catch (Exception e) {
			XxlJobLogger.log(e.getMessage());
		}

		return SUCCESS;
	}
}
