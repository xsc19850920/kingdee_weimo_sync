package com.dhsy.async.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.dhsy.async.service.WeimoService;
import com.dhsy.async.utils.ClientConfig;

@Controller
public class WeimoController {
    private Logger logger = LoggerFactory.getLogger(WeimoController.class);
    
   
	
	@Autowired
	private WeimoService weimoService;
    /**
     * 同步用户
     */
    @Scheduled(fixedRate = 60000)
    public void getUsers() {
    	weimoService.getAccessToken();
    	//part of weimo
        if(StringUtils.isNotBlank(ClientConfig.weimo_access_token)){
           weimoService.getUserList();
           logger.info("获取微盟会员个数：" +ClientConfig.mapForWeimo.size());
        }
    }
    
    
}


