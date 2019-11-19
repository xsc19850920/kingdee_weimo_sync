package com.dhsy.async.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhsy.async.entity.UserForWeimo;
import com.dhsy.async.utils.ClientConfig;
import com.dhsy.async.utils.OkHttpUtils;

@Service
public class WeimoService {
	private Logger logger = LoggerFactory.getLogger(WeimoService.class);
	
	public void getAccessToken() {
	  if(StringUtils.isNotBlank(ClientConfig.weimo_refresh_token)){
            Map<String,String> map = new HashMap<>();
            map.put("client_id",ClientConfig.WEIMO_CLIENT_ID);
            map.put("client_secret",ClientConfig.WEIMO_CLIENT_SECRET);
            map.put("refresh_token",ClientConfig.weimo_refresh_token);
            map.put("grant_type",ClientConfig.WEIMO_GRANT_TYPE_REFRESH_TOKEN);
            String response =  OkHttpUtils.post(ClientConfig.WEIMO_API_URI,map);

            if (StringUtils.isNotBlank(response)) {
                try {
                    JSONObject responseJsonObj = JSONArray.parseObject(response);
                    XxlJobLogger.log("微盟刷新token:"+responseJsonObj);
                    ClientConfig.weimo_access_token = responseJsonObj.get("access_token").toString();
                    ClientConfig.weimo_refresh_token = responseJsonObj.get("refresh_token").toString();
                } catch (Exception e) {
                    logger.error("微盟刷新token出错: " + e.getMessage());
                }
            }else {
            	 XxlJobLogger.log("微盟刷新token 返回值 未获取到");
            }
        }
	}
	
	public void getUserList() {
		// 用户列表的游标
		Long cursor = 0l;
		int listSize = 0;
		do {
			Map<String, String> map = new HashMap<>();
			if (cursor != null) {
				map.put("cursor", cursor.toString());
			}
			String responseStr = OkHttpUtils.postJsonParams("https://dopen.weimob.com/api/1_0/mc/member/getMemberList?accesstoken="
							+ ClientConfig.weimo_access_token, JSONObject.toJSONString(map));
			if (StringUtils.isNotBlank(responseStr)) {
				try {
					JSONObject responseJsonObj = JSONArray.parseObject(responseStr);
					if (responseJsonObj != null && responseJsonObj.getJSONObject("code") != null
							&& responseJsonObj.getJSONObject("code").getInteger("errcode") == 0
							&& responseJsonObj.getJSONObject("data") != null) {
						cursor = responseJsonObj.getJSONObject("data").getLong("cursor");
						if (responseJsonObj.getJSONObject("data").containsKey("items")
								&& responseJsonObj.getJSONObject("data").getJSONArray("items") != null) {
							List<UserForWeimo> tempList = responseJsonObj.getJSONObject("data").getJSONArray("items")
									.toJavaList(UserForWeimo.class);
							listSize = tempList.size();
							for (UserForWeimo userForWeimo : tempList) {
								if (!StringUtils.isEmpty(userForWeimo.getPhone())) {
									if (!ClientConfig.mapForWeimo.containsKey(userForWeimo.getPhone())) {
										ClientConfig.mapForWeimo.put(userForWeimo.getPhone(), userForWeimo);
									} else {
										// 存在并且积分变动 重新设置map 的key
										UserForWeimo existUserForWeimo = ClientConfig.mapForWeimo
												.get(userForWeimo.getPhone());
										if (userForWeimo.getCurrentPoint() != null && existUserForWeimo != null) {
											if (!userForWeimo.getCurrentPoint()
													.equals(existUserForWeimo.getCurrentPoint())) {
												ClientConfig.mapForWeimo.put(userForWeimo.getPhone(), userForWeimo);
											}
										}
									}
								}
							}
						} else {
							cursor = null;
							listSize = 0;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("微盟用户列表出错: " + e.getMessage());
				}
			}
		} while (listSize != 0);
	}

	public boolean addPointForWeimo(BigDecimal pointAmount, String mobile) {
		String getUserListUri = "https://dopen.weimob.com/api/1_0/mc/member/addMemberPointAmount?accesstoken=";
		Map<String, Object> map = new HashMap<>();
		map.put("mid",  ClientConfig.mapForWeimo.get(mobile).getMid());
		map.put("addPointReason", "sync from kingdee");
		map.put("point", pointAmount);
		map.put("channelType", "1");
		String responseStr = OkHttpUtils.postJsonParams(getUserListUri + ClientConfig.weimo_access_token,
				JSONObject.toJSONString(map));
		if (StringUtils.isNotBlank(responseStr)) {
			try {
				JSONObject responseJsonObj = JSONArray.parseObject(responseStr);
				if (responseJsonObj != null && responseJsonObj.getJSONObject("code") != null
						&& responseJsonObj.getJSONObject("code").getInteger("errcode") == 0
						&& responseJsonObj.getJSONObject("data") != null) {
					return true;
				}
			} catch (Exception e) {
				logger.error("积分 金蝶 --> 微盟 错误 <-- {} ", responseStr);
			}
		} else {
			logger.error("金蝶积分同步到微盟 错误");
		}
		return false;
	}
}
