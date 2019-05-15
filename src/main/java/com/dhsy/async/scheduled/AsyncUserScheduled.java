package com.dhsy.async.scheduled;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhsy.async.entity.UserForWeimo;
import com.dhsy.async.utils.ClientConfig;
import com.dhsy.async.utils.OkHttpUtils;

@Component
public class AsyncUserScheduled {
	private Logger logger = LoggerFactory.getLogger(AsyncUserScheduled.class);

	private boolean isRunning = false;
	@Scheduled(initialDelay=30000,fixedRate = 30000)
	public void asyncUsers() {
		if(!isRunning) {
			try {
				isRunning = true;
				if (!CollectionUtils.isEmpty(ClientConfig.mapForWeimo.keySet()) || !CollectionUtils.isEmpty(ClientConfig.mapForKingdee.keySet())) {
					Collection<String> updateToKingdeeList = CollectionUtils.subtract(ClientConfig.mapForWeimo.keySet(), ClientConfig.mapForKingdee.keySet());
					Collection<String> updateToWeimoList = CollectionUtils.subtract(ClientConfig.mapForKingdee.keySet(), ClientConfig.mapForWeimo.keySet());
					
					// part of weimo
					if (CollectionUtils.isNotEmpty(updateToWeimoList) && StringUtils.isNotEmpty( ClientConfig.weimo_access_token)) {
						logger.info("------------同步 (会员信息 金蝶-->微盟) 开始--------------");
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
						logger.info(result);
						long endTime = new Date().getTime();
						logger.info("------------同步 (会员信息 金蝶-->微盟) 结束--------------");
						logger.info(String.format("-------耗时:%s 毫秒----", endTime - startTime));
					}
				
					// part of kingdee
					if (CollectionUtils.isNotEmpty(updateToKingdeeList) && StringUtils.isNotEmpty( ClientConfig.kingdee_access_token) ) {
						logger.info("------------同步 (会员信息 微盟-->金蝶 ) 结束--------------");
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
							logger.info(result);
						}
						
						long endTime = new Date().getTime();
						logger.info("------------同步 (会员信息 微盟-->金蝶 ) 结束--------------");
						logger.info(String.format("-------耗时:%s 毫秒----", endTime - startTime));
					}
					
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}finally {
				isRunning = false;
			}
		}
	}
	
	

	
}
