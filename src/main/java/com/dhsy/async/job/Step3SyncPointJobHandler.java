package com.dhsy.async.job;


import com.alibaba.fastjson.JSONObject;
import com.dhsy.async.entity.PointFlow;
import com.dhsy.async.entity.UserForKingdee;
import com.dhsy.async.entity.UserForWeimo;
import com.dhsy.async.mapper.PointFlowMapper;
import com.dhsy.async.service.KingdeeService;
import com.dhsy.async.service.WeimoService;
import com.dhsy.async.utils.ClientConfig;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


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
@JobHandler(value="Step3SyncPointJobHandler")
@Component
public class Step3SyncPointJobHandler extends IJobHandler {
	@Autowired
	private KingdeeService kingdeeService;

	@Autowired
	private WeimoService weimoService;

	@Autowired
	private PointFlowMapper pointFlowMapper;


	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			if (CollectionUtils.isNotEmpty(ClientConfig.mapForWeimo.keySet()) && CollectionUtils.isNotEmpty(ClientConfig.mapForKingdee.keySet())) {
				XxlJobLogger.log("------------ 同步 (积分信息) 开始--------------");
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
				XxlJobLogger.log("------------同步 (积分信息) 结束--------------");
				XxlJobLogger.log(String.format("-------耗时:%s 毫秒----", endTime - startTime));
			}
		} catch (Exception e) {
			XxlJobLogger.log(e.getMessage());
		}
		return SUCCESS;
	}
}
