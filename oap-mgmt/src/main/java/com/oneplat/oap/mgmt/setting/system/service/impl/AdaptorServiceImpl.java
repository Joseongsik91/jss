package com.oneplat.oap.mgmt.setting.system.service.impl;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.oneplat.oap.core.exception.ServiceException;
import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.CacheSender;
import com.oneplat.oap.mgmt.common.config.ApiConstants;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.setting.system.mapper.AdaptorMapper;
import com.oneplat.oap.mgmt.setting.system.model.Adaptor;
import com.oneplat.oap.mgmt.setting.system.service.AdaptorService;
import com.oneplat.oap.mgmt.setting.system.support.AdaptorCacheSendData;

@Service
@PropertySource("classpath:system.properties")
public class AdaptorServiceImpl implements AdaptorService {
	@Autowired
	AdaptorMapper adaptorMapper;

	@Autowired
	AuthenticationInjector authenticationInjector;
	
	@Autowired
    private Environment env;
	
    @Autowired
    private RestTemplate restTemplate;

	@Override
	public List<Adaptor> adaptorList(SearchRequest searchRequest) {
		// TODO Auto-generated method stub
		if (searchRequest.getPage() == null || searchRequest.getSize() == null) {
			searchRequest.setPage(1);
			searchRequest.setSize(20);
		}
		searchRequest.setData();

		List<Adaptor> res = adaptorMapper.adaptorList(searchRequest);

		if (searchRequest.getPageInfo() != null) {
			searchRequest.getPageInfo().setResultCount(res.size());
			searchRequest.getPageInfo().setTotalCount(adaptorMapper.adaptorListTotalCount(searchRequest));
		}

		return res;
	}

	@Override
	public Adaptor getAdaptorInfo(Long adaptorNumber) {
		// TODO Auto-generated method stub
		return adaptorMapper.getAdaptorInfo(adaptorNumber);
	}

	@Override
	public Object beanIdCheck(String adaptorBeanId) {
		// TODO Auto-generated method stub
		return adaptorMapper.beanIdCheck(adaptorBeanId);
	}

	@Override
	@Transactional
	public Object createAdaptor(Adaptor adaptor) {
		// TODO Auto-generated method stub
		authenticationInjector.setAuthentication(adaptor);
		adaptorMapper.createAdaptor(adaptor);
		adaptorMapper.createAdaptorHist(adaptor.getAdaptorNumber());
		/*
         * OSDF Interfacing
         */
        if(env.getProperty("system.interfaceYn").equals("Y")){
            CacheSendData serviceCacheData = new AdaptorCacheSendData(adaptor, env);
            CacheSender cacheSender = new CacheSender(serviceCacheData, restTemplate);
            
            try {
                int statusCode = cacheSender.sendCreate();
                if(statusCode != HttpStatus.SC_OK)
                    throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode); 
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
            }
        }
		return adaptor.getAdaptorNumber();
	}

	@Override
	@Transactional
	public Adaptor modifyAdaptor(Adaptor adaptor) {
		// TODO Auto-generated method stub
		authenticationInjector.setAuthentication(adaptor);
		adaptorMapper.modifyAdaptor(adaptor);
		adaptorMapper.modifyAdaptorHist(adaptor);
		adaptorMapper.createAdaptorHist(adaptor.getAdaptorNumber());
		adaptor = getAdaptorInfo(adaptor.getAdaptorNumber());
        /*
         * OSDF Interfacing
         */
        if(env.getProperty("system.interfaceYn").equals("Y")){
            CacheSendData serviceCacheData = new AdaptorCacheSendData(adaptor, env);
            CacheSender cacheSender = new CacheSender(serviceCacheData, restTemplate);
            
            try {
                int statusCode = cacheSender.sendModify();
                if(statusCode != HttpStatus.SC_OK)
                    throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode); 
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
            }
        }
		return adaptor;
	}

	@Override
	@Transactional
	public void deleteAdaptor(Long adaptorNumber) {
		// TODO Auto-generated method stub
		Adaptor adaptor = new Adaptor();
		adaptor.setAdaptorNumber(adaptorNumber);
		authenticationInjector.setAuthentication(adaptor);
		adaptorMapper.deleteAdaptor(adaptor);
		adaptorMapper.modifyAdaptorHist(adaptor);
		adaptorMapper.createAdaptorHist(adaptor.getAdaptorNumber());
		/*
         * OSDF Interfacing
         */
        if(env.getProperty("system.interfaceYn").equals("Y")){
            CacheSendData serviceCacheData = new AdaptorCacheSendData(adaptor, env);
            CacheSender cacheSender = new CacheSender(serviceCacheData, restTemplate);
            
            try {
                int statusCode = cacheSender.sendDelete();
                if(statusCode != HttpStatus.SC_OK)
                    throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode); 
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
            }
        }
	}
}
