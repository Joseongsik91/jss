package com.oneplat.oap.mgmt.setting.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.setting.system.model.Adaptor;
import com.oneplat.oap.mgmt.setting.system.model.ResponseAdaptorList;
import com.oneplat.oap.mgmt.setting.system.service.AdaptorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "/adaptors", description = "설정 > 시스템관리 > Adaptor", produces = "application/json")
@RestController
@RequestMapping(value = "/adaptors")
public class AdaptorRestController {

	@Autowired
	AdaptorService adaptorService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdaptorRestController.class);

	@ApiOperation(value = "설정 > 시스템관리 > Adaptor > Adaptor 목록 조회", notes = "Adaptor 목록 조회", response = ResponseAdaptorList.class)
	@RequestMapping(method = RequestMethod.GET)
	public Object adaptorList(SearchRequest searchRequest) {
		return new ResponseAdaptorList(adaptorService.adaptorList(searchRequest), searchRequest.getPageInfo());
	}

	@ApiOperation(value = "설정 > 시스템관리 > Adaptor > Adaptor 정보 조회", notes = "Adaptor 정보 조회")
	@RequestMapping(value = "/{adaptorNumber}", method = RequestMethod.GET)
	public Object getAdaptorInfo(@ApiParam(value = "어댑터 넘버") @PathVariable("adaptorNumber") Long adaptorNumber) {
		return adaptorService.getAdaptorInfo(adaptorNumber);
	}

	@ApiOperation(value = "설정 > 시스템관리 > Adaptor > Adaptor 등록 > BeanId 중복 확인", notes = "BeanId 중복 확인")
	@RequestMapping(value = "/beanIdCheck", method = RequestMethod.GET)
	public Object beanIdCheck(@ApiParam(value = "beanId") String adaptorBeanId) {
		return adaptorService.beanIdCheck(adaptorBeanId);
	}

	@ApiOperation(value = "설정 > 시스템관리 > Adaptor > Adaptor 등록", notes = "Adaptor 등록")
	@RequestMapping(method = RequestMethod.POST)
	public Object createAdaptor(@ApiParam(value = "어댑터 정보") @RequestBody Adaptor adaptor) {
		return adaptorService.createAdaptor(adaptor);
	}

	@ApiOperation(value = "설정 > 시스템관리 > Adaptor > Adaptor 수정", notes = "Adaptor 수정")
	@RequestMapping(value = "/{adaptorNumber}", method = RequestMethod.PUT)
	public Adaptor modifyAdaptor(@ApiParam(value = "어댑터 넘버") @PathVariable("adaptorNumber") Long adaptorNumber,
			@ApiParam(value = "어댑터 정보") @RequestBody Adaptor adaptor) {
		adaptor.setAdaptorNumber(adaptorNumber);
		return adaptorService.modifyAdaptor(adaptor);
	}

	@ApiOperation(value = "설정 > 시스템관리 > Adaptor > Adaptor 삭제", notes = "Adaptor 삭제")
	@RequestMapping(value = "/{adaptorNumber}", method = RequestMethod.DELETE)
	public void deleteAdaptor(@ApiParam(value = "어댑터 넘버") @PathVariable("adaptorNumber") Long adaptorNumber) {
		adaptorService.deleteAdaptor(adaptorNumber);
	}
}