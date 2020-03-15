package com.oneplat.oap.mgmt.policies.controller;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.policies.model.ScopeApi;
import com.oneplat.oap.mgmt.policies.service.ScopeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "/scopeApi", description = "Policies > Scope-API", produces = "application/json")
@RestController
@RequestMapping(value = "/scopeApi")
public class ScopeApiController {

	@Autowired
	ScopeService scopeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ScopeApiController.class);

	@ApiOperation(value = "Policies > Scope-API", notes = "Policies > Scope-API")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Object getScopeList() {
		return scopeService.getScopeSelectList();
	}

	@ApiOperation(value = "Policies > Scope-API", notes = "Policies > Scope-API")
	@RequestMapping(value = "/{scopeNumber}", method = RequestMethod.GET)
	public Object getScopeSubList(@PathVariable(value = "scopeNumber") long scopeNumber) {
		return scopeService.getScopeSubList(scopeNumber);
	}

	@ApiOperation(value = "Policies > Scope-API", notes = "Policies > Scope-API")
	@RequestMapping(value = "/{scopeNumber}/api", method = RequestMethod.GET)
	public Object getScopeApiList(@PathVariable(value = "scopeNumber") long scopeNumber, SearchRequest searchRequest) {
		return scopeService.getScopeApiList(scopeNumber, searchRequest);
	}

	@ApiOperation(value = "Policies > Scope-API", notes = "Policies > Scope-API")
	@RequestMapping(value = "/{scopeNumber}/apiRelation", method = RequestMethod.GET)
	public Object getScopeApiRelationList(@PathVariable(value = "scopeNumber") long scopeNumber) {
		return scopeService.getScopeApiRelationList(scopeNumber);
	}


	@ApiOperation(value = "Policies > Scope-API", notes = "Policies > Scope-API")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public Object createScopeApi(@RequestBody List<ScopeApi> scopeApis) {
		LOGGER.debug("CreateScopeApi========================{}", scopeApis);
		return scopeService.createScopeApi(scopeApis);
	}

	@ApiOperation(value = "Policies > Scope-API", notes = "Policies > Scope-API")
	@RequestMapping(value = "", method = RequestMethod.DELETE)
	public Object removeScopeApi(@RequestBody List<ScopeApi> scopeApis) {
		LOGGER.debug("RemoveScopeApi========================{}", scopeApis);
		return scopeService.removeScopeApi(scopeApis);
	}
}
