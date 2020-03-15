package com.oneplat.oap.mgmt.policies.service.impl;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.policies.mapper.ScopeMapper;
import com.oneplat.oap.mgmt.policies.model.Scope;
import com.oneplat.oap.mgmt.policies.model.ScopeApi;
import com.oneplat.oap.mgmt.policies.service.ScopeService;
import com.oneplat.oap.mgmt.policies.support.PolicyCacheService;
import com.oneplat.oap.mgmt.util.UrlPrefixUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

/**
 * Created by Hong on 2017. 2. 7..
 */
@Service
public class ScopeServiceImpl implements ScopeService {

	@Autowired
	ScopeMapper scopeMapper;
	@Autowired
	AuthenticationInjector authenticationInjector;
	@Autowired
	Environment env;
	@Autowired
	PolicyCacheService policyCacheService;
	
	@Override
	@Transactional
	public void createInitScope(Scope scope) {
		// Scope 등록
		scopeMapper.insertScope(scope);

		// Scope Relation 등록
		scope.setCriteriaScopeNumber(Long.parseLong("0"));
		scope.setOpponentScopeNumber(scope.getScopeNumber());
		scope.setCriteriaScopeLevel(0);
		scope.setOpponentScopeLevel(1);
		scope.setCriteriaSortNumber(1);
		scope.setOpponentSortNumber(1);
		scope.setCreateId(scope.getCreateId());
		scope.setModifyId(scope.getModifyId());
		scopeMapper.insertScopeRelation(scope);

		// 신규 Scope 이력 등록
		scopeMapper.insertScopeHistory(scope.getScopeNumber());

		/** OSDF Interfacing*/
		/*if(env.getProperty("system.interfaceYn").equals("Y"))
			policyCacheService.policyScopeCacheCreate(scope);*/
	}

	@Override
	public List<Scope> getScopeList() {
		// TODO Auto-generated method stub
		return scopeMapper.getScopeList();
	}

	@Override
	public Scope getScopeDetail(Long scopeNumber) {
		// TODO Auto-generated method stub
		Scope scope = scopeMapper.getScopeDetail(scopeNumber);
		scope.setIconFileChannel(UrlPrefixUtil.addPrefix(scope.getIconFileChannel()));
		return scope;
	}

	@Override
	public Object checkScopeContext(String scopeContext) {
		// TODO Auto-generated method stub
		return scopeMapper.checkScopeContext(scopeContext);
	}

	@Transactional
	@Override
	public Object createScope(Scope scope) {
		// TODO Auto-generated method stub
		authenticationInjector.setAuthentication(scope);

		// Scope 등록
		scopeMapper.insertScope(scope);
		scope.setOpponentScopeNumber(scope.getScopeNumber());

		// Scope Relation 등록
		scopeMapper.insertNewScopeRelation(scope);

		// 이력 등록
		scopeMapper.insertScopeHistory(scope.getScopeNumber());
		
		/** OSDF Interfacing*/
		Scope scopeCache = getScopeDetail(scope.getScopeNumber());
		/*if(env.getProperty("system.interfaceYn").equals("Y"))
			policyCacheService.policyScopeCacheCreate(scopeCache);*/
		
		return scopeCache;
	}

	@Transactional
	@Override
	public Object modifyScope(Scope scope) {
		// TODO Auto-generated method stub
		authenticationInjector.setAuthentication(scope);

		scope.setIconFileChannel(UrlPrefixUtil.removePrefix(scope.getIconFileChannel()));
		
		// Scope 수정
		scopeMapper.updateScope(scope);

		// 이력 수정 및 등록
		scopeMapper.updateScopeHistory(scope);
		scopeMapper.insertScopeHistory(scope.getScopeNumber());

		/** OSDF Interfacing*/
		Scope scopeCache = getScopeDetail(scope.getScopeNumber());
		/*if(env.getProperty("system.interfaceYn").equals("Y"))
			policyCacheService.policyScopeCacheModify(scopeCache);*/
		
		return scopeCache;
	}

	@Transactional
	@Override
	public void removeScope(Long scopeNumber) {
		// TODO Auto-generated method stub
		Scope scope = getScopeDetail(scopeNumber);
		scope.setOpponentScopeNumber(scopeNumber);
		scope.setDeleteYn(true);
		authenticationInjector.setAuthentication(scope);

		// Scope 삭제
		scopeMapper.deleteScope(scope);

		// Relation 수정
		scopeMapper.updateScopeRelation(scope);

		// Re Sorting
		modifyScopeSort(scope);
		
		// 이력 수정 및 등록
		scopeMapper.updateScopeHistory(scope);
		scopeMapper.insertScopeHistory(scope.getScopeNumber());
		
		/*if(env.getProperty("system.interfaceYn").equals("Y"))
			policyCacheService.policyScopeCacheDelete(scope);*/
	}

	@Transactional
	@Override
	public void modifyScopeSort(Scope scope) {
		// TODO Auto-generated method stub

		int addSort = 0;
		List<Scope> list = scopeMapper.getScopeSortList(scope);
		List<Scope> result = new ArrayList<Scope>();
		Long sortNumber = null;
		
		if(!scope.getDeleteYn()){
			Scope target = getScopeDetail(scope.getOpponentScopeNumber());
			target.setOpponentScopeNumber(scope.getOpponentScopeNumber());
			target.setOpponentSortNumber(scope.getOpponentSortNumber());
			result.add(target);
			sortNumber = Long.parseLong(scope.getOpponentSortNumber() + "");
		}
		
		for (int i = 0; i < list.size(); i++) {
			Scope sort = list.get(i);
			sort.setScopeNumber(sort.getOpponentScopeNumber());
			
			if (!scope.getDeleteYn() && sortNumber.compareTo(Long.parseLong((i + 1) + "")) == 0)
				addSort = 1;
			
			sort.setOpponentSortNumber(i + 1 + addSort);
			result.add(sort);
		}

		for (Scope buf : result){
			authenticationInjector.setAuthentication(buf);
			scopeMapper.updateScopeRelation(buf);
			buf.setCreateId(buf.getModifyId());
			scopeMapper.insertScopeRelation(buf);
			//scopeMapper.updateScopeSort(buf);
		}
		if (scope.getOpponentScopeLevel() == 1)
			scopeMapper.updateCriteriaScopeLevel(scope);
	}

	@Override
	public Object getScopeSelectList() {
		return scopeMapper.selectScopeSelectList();
	}

	@Override
	public Object getScopeSubList(long scopeNumber) {
		return scopeMapper.selectScopeSubList(scopeNumber);
	}

	@Override
	public Object getScopeApiList(long scopeNumber, SearchRequest searchRequest) {
		searchRequest.setData();
		searchRequest.addQueryData("scopeNumber", scopeNumber);
		return scopeMapper.selectScopeApiList(searchRequest);
	}

	@Override
	public Object getScopeApiRelationList(long scopeNumber) {
		return scopeMapper.selectScopeApiRelationList(scopeNumber);
	}

	@Override
	@Transactional
	public Object createScopeApi(List<ScopeApi> scopeApis) {
		for(ScopeApi scopeApi : scopeApis){
			authenticationInjector.setAuthentication(scopeApi);
			scopeMapper.insertScopeApi(scopeApi);
		}

		//cache연동
		updateScopeApiCache(scopeApis);

		return scopeApis;
	}

	@Override
	@Transactional
	public Object removeScopeApi(List<ScopeApi> scopeApis) {
		for(ScopeApi scopeApi : scopeApis){
			scopeMapper.deleteScopeApi(scopeApi);
		}

		//cache연동
		updateScopeApiCache(scopeApis);

		return scopeApis;
	}

	public void updateScopeApiCache(List<ScopeApi> scopeApis){
		/** OSDF Interfacing*/
		if(env.getProperty("system.interfaceYn").equals("Y")){
			for(ScopeApi scopeApiCache : scopeApis){
				Map<String, Object> scopeApiCacheData = new HashMap<>();
				scopeApiCacheData.put("apiNumber", scopeApiCache.getApiNumber());
				scopeApiCacheData.put("scopeContext", scopeMapper.selectScopeApiCache(scopeApiCache.getApiNumber()));

				policyCacheService.policyScopeApiCacheModify(scopeApiCacheData);
			}
		}
	}

	@Override
	public void updateScopeUseYn(Scope scope) {
		scopeMapper.updateScopeUseYn(scope);
	}
}
