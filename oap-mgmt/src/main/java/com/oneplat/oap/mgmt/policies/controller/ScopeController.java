package com.oneplat.oap.mgmt.policies.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.oneplat.oap.mgmt.policies.model.Scope;
import com.oneplat.oap.mgmt.policies.service.ScopeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "/scope", description = "Policies > Scope", produces = "application/json")
@RestController
@RequestMapping(value = "/scope")
public class ScopeController {

	@Autowired
	ScopeService scopeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ScopeController.class);

	@ApiOperation(value = "Policies > Scope > ScopeList(tree)", notes = "Policies > Scope > ScopeList(tree)")
	@RequestMapping(value = "/scopeList", method = RequestMethod.GET)
	public Object getScopeList() {
		LOGGER.debug("getScopeList");
		return scopeService.getScopeList();
	}

	@ApiOperation(value = "Policies > Scope > ScopeDetail", notes = "Policies > Scope > ScopeDetail")
	@RequestMapping(value = "/{scopeNumber}", method = RequestMethod.GET)
	public Object getScopeDetail(@PathVariable Long scopeNumber) {
		LOGGER.debug("getScopeDetail");
		return scopeService.getScopeDetail(scopeNumber);
	}

	@ApiOperation(value = "Policies > Scope > Scope 등록 > ScopeContext 중복 확인", notes = "ScopeContext 중복 확인")
	@RequestMapping(value = "/checkContext", method = RequestMethod.GET)
	public Object checkScopeContext(@ApiParam(value = "scopeContext") String scopeContext) {
		LOGGER.debug("checkScopeContext" + " : " + scopeContext);
		return scopeService.checkScopeContext(scopeContext);
	}

	@ApiOperation(value = "Policies > Scope > Scope 등록", notes = "Scope 등록")
	@RequestMapping(method = RequestMethod.POST)
	public Object createScope(@ApiParam(value = "Scope 정보") @RequestBody Scope scope) {
		LOGGER.debug("createScope {}", scope);
		return scopeService.createScope(scope);
	}

	@ApiOperation(value = "Policies > Scope > Scope 수정", notes = "Scope 수정")
	@RequestMapping(value = "/{scopeNumber}", method = RequestMethod.PUT)
	public Object modifyScope(@ApiParam(value = "Scope 정보") @RequestBody Scope scope) {
		LOGGER.debug("modifyScope {}", scope);
		return scopeService.modifyScope(scope);
	}

	@ApiOperation(value = "Policies > Scope > Scope 삭제", notes = "Scope 삭제")
	@RequestMapping(value = "/{scopeNumber}", method = RequestMethod.DELETE)
	public void removeScope(@PathVariable Long scopeNumber) {
		LOGGER.debug("removeScope scopeNumber : ", scopeNumber);
		scopeService.removeScope(scopeNumber);
	}

	@ApiOperation(value = "Policies > Scope > Scope Tree 순서 변경", notes = "Scope Tree 순서 변경")
	@RequestMapping(value = "/sort", method = RequestMethod.PUT)
	public void modifyScopeSort(@RequestBody Scope scope) {
		LOGGER.debug("modifyScopeSort {}", scope);
		scope.setDeleteYn(false);
		scopeService.modifyScopeSort(scope);
	}
}
