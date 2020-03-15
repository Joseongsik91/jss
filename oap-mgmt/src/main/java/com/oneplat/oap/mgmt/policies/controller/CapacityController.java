package com.oneplat.oap.mgmt.policies.controller;

import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.policies.model.ServicePolicy;
import com.oneplat.oap.mgmt.policies.service.CapacityService;
import com.oneplat.oap.mgmt.setting.operator.model.ResponseOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LSH on 2016. 11. 30..
 */

@Api(tags="/policy/capacity", description="policy > Capacity", produces = "application/json")
@RestController
@RequestMapping(value = "/policy/capacity")
public class CapacityController {

    @Autowired
    private CapacityService capacityService;

    @Autowired
    AuthenticationInjector authenticationInjector;

    private static final Logger LOGGER = LoggerFactory.getLogger(CapacityController.class);

    /**
     * capacityList
     * */
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "policy/capacity", notes = "policy > CapacityList", response=ResponseOperator.class)
    public @ResponseBody
    Object capacityList() {
        LOGGER.debug("capacityList");
        return capacityService.capacityList();
    }

    /**
     * modifyCapacity
     * */
    @RequestMapping(value="/{serviceNumber}", method = RequestMethod.PUT)
    @ApiOperation(value = "policy/capacity", notes = "policy > modifyCapacity", response=ResponseOperator.class)
    public Object modifyCapacity(
            @ApiParam(value="capacity 서비스 번호", defaultValue="") @PathVariable Long serviceNumber,
            @ApiParam(value="capacity 수정 데이터", defaultValue="") @RequestBody ServicePolicy servicePolicy
            ) {
        authenticationInjector.setAuthentication(servicePolicy);
        servicePolicy.setServiceNumber(serviceNumber);
        capacityService.modifyCapacity(servicePolicy);
        return servicePolicy;
    }
}
