package com.dhsy.async.scheduled;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dhsy.async.entity.PointFlow;
import com.dhsy.async.entity.UserForKingdee;
import com.dhsy.async.entity.UserForWeimo;
import com.dhsy.async.mapper.PointFlowMapper;
import com.dhsy.async.service.KingdeeService;
import com.dhsy.async.service.WeimoService;
import com.dhsy.async.utils.ClientConfig;

@Component
public class PointScheduled {
	private Logger logger = LoggerFactory.getLogger(PointScheduled.class);
	
	@Autowired
	private KingdeeService kingdeeService;
	
	@Autowired
	private WeimoService weimoService;
	
	@Autowired
	private PointFlowMapper pointFlowMapper;
	
	boolean isRunning = false;
	
	@Scheduled(initialDelay = 30000, fixedRate = 120000)
	public void asyncPoint() {
		if(!isRunning) {
			try {
				isRunning = true;
				if (CollectionUtils.isNotEmpty(ClientConfig.mapForWeimo.keySet()) && CollectionUtils.isNotEmpty(ClientConfig.mapForKingdee.keySet())) {
					logger.info("------------ 同步 (积分信息) 开始--------------");
					long startTime = new Date().getTime();
					
					//同步未处理的流水
					List<PointFlow> pointFlowList = kingdeeService.getPointFlowList(JSONObject.toJSONString(ClientConfig.mapForKingdee.keySet()));
					if(CollectionUtils.isNotEmpty(pointFlowList)) {
						for (PointFlow pointFlow : pointFlowList) {
							boolean result = weimoService.addPointForWeimo(new BigDecimal( pointFlow.getOccurpoints()), pointFlow.getMemmobile());
							if(result) {
								pointFlow.setStatus(1);
								pointFlowMapper.insert(pointFlow);
							}
//							long id = ClientConfig.mapForKingdee.get(pointFlow.getMemmobile()).getId();
//							boolean result = kingdeeService.addPointForKingdee(String.valueOf(id),pointFlow.getOccurpoints());
//							if(result) {
//								pointFlow.setStatus(1);
//								pointFlowMapper.insert(pointFlow);
//							}
						}
					}else {
						for (UserForWeimo userForWeimo : ClientConfig.mapForWeimo.values()) {
							// 判断weimo的用户积分和金蝶用户积分不相等发起金蝶的积分变动
							UserForKingdee userForKingdee = ClientConfig.mapForKingdee.get(userForWeimo.getPhone());
							if (userForKingdee != null && userForWeimo != null) {
								BigDecimal pointForKingdee = new BigDecimal(userForKingdee.getBalpoints());
								BigDecimal pointForWeimo = new BigDecimal(userForWeimo.getCurrentPoint() == null ? 0l : userForWeimo.getCurrentPoint());
//								//微盟需要加积分
//								if ( pointForKingdee.doubleValue() > pointForWeimo.doubleValue()) {
//									BigDecimal pointAmount = pointForKingdee.subtract(pointForWeimo);
//									weimoService.addPointForWeimo(pointAmount, userForWeimo.getPhone());
//								} else {
//									//金蝶需要加积分
									if (pointForKingdee.doubleValue() != pointForWeimo.doubleValue()) {
										String changePoint = pointForWeimo.subtract(pointForKingdee).toString();
										boolean result = kingdeeService.addPointForKingdee(changePoint, userForKingdee.getId());
										if(result) {
											List<PointFlow> pointFlowListTemp = kingdeeService.getPointFlowList(JSONObject.toJSONString(userForKingdee.getMobile()));
											if(CollectionUtils.isNotEmpty(pointFlowListTemp)) {
												PointFlow pointFlow = pointFlowListTemp.get(0);
												pointFlow.setStatus(1);
												pointFlowMapper.insert(pointFlow);
											}
											
										}
									}
								}
//							}
						}
					}
					
					long endTime = new Date().getTime();
					logger.info("------------同步 (积分信息) 结束--------------");
					logger.info(String.format("-------耗时:%s 毫秒----", endTime - startTime));
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}finally {
				isRunning = false;
			}
		}
	}
}
