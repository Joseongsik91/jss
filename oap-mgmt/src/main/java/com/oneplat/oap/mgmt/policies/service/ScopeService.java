package com.oneplat.oap.mgmt.policies.service;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.policies.model.Scope;
import com.oneplat.oap.mgmt.policies.model.ScopeApi;

import java.util.List;

/**
 * Created by Hong on 2017. 2. 7..
 */
public interface ScopeService {

	void createInitScope(Scope scope);
	List<Scope> getScopeList();
	Scope getScopeDetail(Long scopeNumber);
	Object checkScopeContext(String scopeContext);
	Object createScope(Scope scope);
	Object modifyScope(Scope scope);
	void removeScope(Long scopeNumber);
	void modifyScopeSort(Scope scope);

    Object getScopeSelectList();
    Object getScopeSubList(long scopeNumber);
    Object getScopeApiList(long scopeNumber, SearchRequest searchRequest);
    Object getScopeApiRelationList(long scopeNumber);
    Object createScopeApi(List<ScopeApi> scopeApis);
    Object removeScopeApi(List<ScopeApi> scopeApis);
    void updateScopeUseYn(Scope scope);
}
