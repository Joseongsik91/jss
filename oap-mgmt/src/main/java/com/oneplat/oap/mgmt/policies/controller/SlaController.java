package com.oneplat.oap.mgmt.policies.controller;

import com.oneplat.oap.mgmt.policies.model.ServicePolicy;
import com.oneplat.oap.mgmt.policies.service.SlaService;
import com.oneplat.oap.mgmt.setting.operator.model.ResponseOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Created by LSH on 2016. 11. 30..
 */

@Api(tags="/policy/sla", description="policy > SLA", produces = "application/json")
@RestController
@RequestMapping(value = "/policy/sla")
public class SlaController {

    @Autowired
    private SlaService slaService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SlaController.class);

    private String baseUrl;


    /**
     * SlaList
     * */
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "policy/slaList", notes = "policy > SLAList", response=ResponseOperator.class)
    public @ResponseBody
    Object slaList() {
        LOGGER.debug("slaList");
        return slaService.slaList();
    }

    /**
     * modifySLA and insertHistorySLA
     * */
    @RequestMapping(value="/{serviceNumber}", method = RequestMethod.PUT)
    @ApiOperation(value = "policy/modifySla", notes = "policy > modifySLA", response=ResponseOperator.class)
    public Object modifySla(
            @ApiParam(value="sla 서비스 번호", defaultValue="") @PathVariable Long serviceNumber,
            @ApiParam(value="sla 수정 데이터", defaultValue="") @RequestBody ServicePolicy servicePolicy
    ) {

        /*object 형식으로 바꾸기*/
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.writeValueAsString(servicePolicy);
        servicePolicy.setServiceNumber(serviceNumber);
        LOGGER.debug("modifySlaServicePolicy : {} ", servicePolicy);
        slaService.modifySla(servicePolicy);
        return servicePolicy;
    }

}
