package com.oneplat.oap.mgmt.common.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.setting.system.model.CommonCode;
import com.oneplat.oap.mgmt.setting.system.model.ResponseCommonCode;
import com.oneplat.oap.mgmt.setting.system.service.CommonCodeService;

/**
 * 공통 코드 Cache 데이타
 *
 * @author mike 
 * @date 2015. 4. 9
 */
@Component
public class CommonCodeCache {
    @Autowired
    CommonCodeService commonCodeService;
    
    private Map<String, String> commonCodeData;
    
    public String getCodeValue(String key){
        return commonCodeData.get(key);
    }
    @PostConstruct
    public void init(){
        commonCodeData = new HashMap<String, String>();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setData();
        ResponseCommonCode codeList = commonCodeService.searchCommonCodeList(searchRequest);
        for(CommonCode commonCode: codeList.getData()){
            commonCodeData.put(commonCode.getCode(), commonCode.getCodeName());
        }
    }
    
    public void update(){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setData();
        ResponseCommonCode codeList = commonCodeService.searchCommonCodeList(searchRequest);
        for(CommonCode commonCode: codeList.getData()){
            commonCodeData.put(commonCode.getCode(), commonCode.getCodeName());
        }
    }

}
