package com.dhsy.async.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.dhsy.async.service.KingdeeService;
import com.dhsy.async.utils.ClientConfig;

@Controller
public class KingdeeController {
	private static Logger logger = LoggerFactory.getLogger(KingdeeController.class);
	
	@Autowired
	private KingdeeService kingdeeService;
	
	@Scheduled(initialDelay = 30000,fixedRate = 60000)
	public void getUsers() {
		kingdeeService.getAccessToken();
		// part of kingdee
		if (StringUtils.isNotBlank(ClientConfig.kingdee_access_token)) {
			kingdeeService.getUserList();
			kingdeeService.setBalance();
			logger.info("获取金蝶会员个数：" +ClientConfig.mapForKingdee.size());
			//所有的积分流水为了获取用户的积分余额用
//			List<PointFlow> allPointFlowList = kingdeeService.getPointFlowList(JSONObject.toJSONString(ClientConfig.mapForKingdee.keySet()));
//			if (CollectionUtils.isNotEmpty(allPointFlowList)) {
//				Map<String, Long> phoneAndBalpointsMap = new HashMap<>();
//				for (PointFlow pointFlow : allPointFlowList) {
//					if (phoneAndBalpointsMap.containsKey(pointFlow.getMemmobile())) {
//						continue;
//					} else {
//						phoneAndBalpointsMap.put(pointFlow.getMemmobile(), pointFlow.getPoints());
//					}
//				}
//				if (MapUtils.isNotEmpty(phoneAndBalpointsMap)) {
//					for (Map.Entry<String, UserForKingdee> entry : ClientConfig.mapForKingdee.entrySet()) {
//						if (phoneAndBalpointsMap.containsKey(entry.getKey())) {
//							entry.getValue().setBalpoints(String.valueOf(phoneAndBalpointsMap.get(entry.getKey())));
//						} else {
//							entry.getValue().setBalpoints("0.0");
//						}
//					}
//				}
//			}
//			}
		}
	}

	

	

	
}
