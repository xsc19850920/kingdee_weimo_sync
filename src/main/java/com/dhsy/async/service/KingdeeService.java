package com.dhsy.async.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhsy.async.entity.PointFlow;
import com.dhsy.async.entity.PointFlowCriteria;
import com.dhsy.async.entity.UserForKingdee;
import com.dhsy.async.mapper.PointFlowMapper;
import com.dhsy.async.utils.ClientConfig;
import com.dhsy.async.utils.OkHttpUtils;
import com.dhsy.async.utils.RSAUtils;

@Service
public class KingdeeService {
	private Logger logger = LoggerFactory.getLogger(KingdeeService.class);
	@Autowired
	private PointFlowMapper pointFlowMapper;

	public void getAccessToken() {
		Map<String, Object> map = new HashMap<>();
		map.put("client_id", ClientConfig.KINGDEE_CLIENT_ID);
		map.put("client_secret", ClientConfig.KINGDEE_CLIENT_SECRET);
		map.put("username", ClientConfig.KINGDEE_USERNAME);
		map.put("password", ClientConfig.KINGDEE_PASSWORD);
		String responseStr = OkHttpUtils.get(ClientConfig.KINGDEE_API_URL, map);

		if (StringUtils.isNotBlank(responseStr)) {
			try {
				JSONObject responseJsonObj = JSONArray.parseObject(responseStr);
				XxlJobLogger.log("金蝶获取token:" + responseJsonObj);
				ClientConfig.kingdee_access_token = responseJsonObj.getJSONObject("data").getString("access_token");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("金蝶获取token出错: " + e.getMessage());
			}
		}else {
			XxlJobLogger.log("金蝶获取token 返回值未获取到" );
		}
	}

	public void getUserList() {
		XxlJobLogger.log("--金蝶获取会员 start--");
		String getUserListUri = "http://api.kingdee.com/jdyapi/retail/mb/thirdlist?access_token=%s&dbid=%s";
		int pageIndex = 1;
		int pageCount = 0;
		do {
			JSONObject paramJson = new JSONObject();
			paramJson.put("pageIndex", pageIndex);
			String responseStr = OkHttpUtils.postJsonParams(
					String.format(getUserListUri, ClientConfig.kingdee_access_token, ClientConfig.KINGDEE_DBID),
					paramJson.toJSONString());
			if (StringUtils.isNotBlank(responseStr)) {
				try {
					JSONObject responseJsonObj = JSONArray.parseObject(responseStr);
					if (responseJsonObj != null && responseJsonObj.getInteger("errcode") == 200) {
						JSONObject pageResultJson = responseJsonObj.getJSONObject("pageResult");

						pageCount = pageResultJson.getInteger("pageCount");
						pageIndex++;

						if (responseJsonObj.getJSONArray("data") != null) {
							List<UserForKingdee> list = responseJsonObj.getJSONArray("data")
									.toJavaList(UserForKingdee.class);
							if (!CollectionUtils.isEmpty(list)) {
								list.stream().filter(l -> !StringUtils.isEmpty(l.getMobile()))
										.filter(l -> !ClientConfig.mapForKingdee.containsKey(l.getMobile()))
										.forEach(l -> ClientConfig.mapForKingdee.put(l.getMobile(), l));
							}
						}
					}
				} catch (Exception e) {
					logger.error("金蝶用户列表出错: " + e.getMessage());
				}
			}
		} while (pageIndex <= pageCount);
		XxlJobLogger.log("--金蝶获取会员 end--");
	}

	/**
	 * 
	 * @param isAll true 获取全部 false 只获得增加的
	 * @return
	 */
	public List<PointFlow> getPointFlowList(String mobilesStr) {
		// 判断金蝶是否有增加的积分流水
		PointFlowCriteria criteria = new PointFlowCriteria();
//		criteria.createCriteria().andStatusEqualTo(0);
		List<PointFlow> pointFlowFromDbList = pointFlowMapper.selectByExample(criteria);

		List<PointFlow> pointFlowList = new ArrayList<PointFlow>();
		String getPointFlowtUri = "http://api.kingdee.com/jdyapi/retail/mb/point/flow/list?access_token=%s&dbid=%s";
		int pageIndexForFlow = 1;
		int pageCountForFlow = 0;
		do {
			JSONObject paramJson = new JSONObject();
			paramJson.put("pageIndex", pageIndexForFlow);
			paramJson.put("pageSize", 100);
			paramJson.put("cardOrMobile", mobilesStr);
			String responseStr = OkHttpUtils.postJsonParams(
					String.format(getPointFlowtUri, ClientConfig.kingdee_access_token, ClientConfig.KINGDEE_DBID),
					paramJson.toJSONString());
			if (StringUtils.isNotBlank(responseStr)) {
				try {
					JSONObject responseJsonObj = JSONArray.parseObject(responseStr);
					if (responseJsonObj != null && responseJsonObj.getInteger("errcode") == 200) {
						JSONObject pageResultJson = responseJsonObj.getJSONObject("pageResult");
						pageCountForFlow = pageResultJson.getInteger("pageCount");
						pageIndexForFlow++;

						if (responseJsonObj.getJSONArray("data") != null) {
							List<PointFlow> tempList = responseJsonObj.getJSONArray("data").toJavaList(PointFlow.class);
							if (CollectionUtils.isNotEmpty(tempList)) {
								for (PointFlow pointFlow : tempList) {
									if ("加".equals(pointFlow.getDirection())) {
										if (CollectionUtils.isNotEmpty(pointFlowFromDbList)) {
											if (!pointFlowFromDbList.stream()
													.anyMatch(item -> pointFlow.getNumber().equals(item.getNumber()))) {
												pointFlowList.add(pointFlow);
											}
										} else {
											pointFlowList.add(pointFlow);
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					logger.error("金蝶用户积分流水出错: " + e.getMessage());
				}
			}
		} while (pageIndexForFlow <= pageCountForFlow);
		return pointFlowList;
	}

	public boolean addPointForKingdee(String changePoint, long id) {
		String url = "http://api.kingdee.com/jdyapi/retail/mb/point/adjust?access_token=%s&dbid=%s";
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(id);
		json.put("ids", jsonArray);
		json.put("changePoint", changePoint);
		String responseStr = OkHttpUtils.postJsonParams(
				String.format(url, ClientConfig.kingdee_access_token, ClientConfig.KINGDEE_DBID), json.toString());
		if (StringUtils.isNotBlank(responseStr)) {
			try {
				JSONObject responseJsonObj = JSONArray.parseObject(responseStr);
				if (responseJsonObj != null && responseJsonObj.getInteger("errcode") == 200) {
					return true;
				}
			} catch (Exception e) {
				logger.error("积分: 微盟 --> 金蝶 错误 <-- {} ", responseStr);
			}
		}

		return false;
	}

	public void setBalance() {
		String findBalanceUri = "http://api.kingdee.com/jdyapi/retail/mb/findBalance?access_token=%s&dbid=%s";
		//根据会员id 查询会员的余额积分
		if(MapUtils.isNotEmpty(ClientConfig.mapForKingdee)) {
			for (UserForKingdee userforKingdee : ClientConfig.mapForKingdee.values()) {
				TreeMap<String, Object> params = new TreeMap<String, Object>();// TreeMap 自带排序
				params.put("id", userforKingdee.getId());// 会员id
				String sign = RSAUtils.sign(params);// 生成签名
				JSONObject json = new JSONObject();
				json.put("id", userforKingdee.getId());// 会员id
				json.put("signStr", sign);
				String responseStr = OkHttpUtils.postJsonParams(String.format(findBalanceUri, ClientConfig.kingdee_access_token,ClientConfig.KINGDEE_DBID),json.toJSONString() );
				if (StringUtils.isNotBlank(responseStr)) {
					try {
						JSONObject responseJsonObj = JSONArray.parseObject(responseStr);
						if (responseJsonObj != null && responseJsonObj.getInteger("errcode") == 200) {
							JSONObject dataJson = responseJsonObj.getJSONObject("data");
							if(dataJson != null && dataJson.getString("balpoints") != null) {
								userforKingdee.setBalpoints(dataJson.getString("balpoints"));
							}
						}
					} catch (Exception e) {
						logger.error("金蝶查询会员余额出错: " + e.getMessage());
					}
				}
			}

		}
	}
}
