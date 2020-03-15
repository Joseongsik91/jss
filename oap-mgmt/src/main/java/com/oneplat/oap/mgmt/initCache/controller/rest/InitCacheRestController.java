package com.oneplat.oap.mgmt.initCache.controller.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oneplat.oap.mgmt.initCache.service.InitCacheService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * TODO 초기 데이타 Cache interface api 클래스
 *
 * @author mike 
 * @date 2015. 7. 21
 */
@Api(description="초기화 데이타 연동", produces = "application/json")
@Controller
@RequestMapping(value = "/initCache")
public class InitCacheRestController{

	@Autowired
	private InitCacheService initCacheService;
	
    /**
     * <pre>
     * 초기화 데이타 
     * <pre>
     * @param searchRequest
     * @param principal
     * @return
     * @throws Exception
     * @Method Name: initializeCache
     */
	@ApiOperation(value = "초기화 데이타 Interface 연동", notes = "초기화 데이타 Interface 연동")
    @RequestMapping(value = "/initialize", method = RequestMethod.PUT)
    @ResponseBody
    public Object initializeCache() throws Exception{

        
        Map<String, Object> result = initCacheService.CreateInitData();
        //response setting
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("data", result);
        
        return map;
    }
}
