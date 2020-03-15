package com.oneplat.oap.mgmt.oapservice.controller;

import com.oneplat.oap.mgmt.oapservice.model.ApiGroup;
import com.oneplat.oap.mgmt.oapservice.service.OapServiceGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lee
 * @date 2016-12-20
 */
@Api(tags="/services/group",  description="서비스 그룹", produces = "application/json")
@RestController
@RequestMapping(value = "/services/group")
public class OapServiceGroupRestController {

    @Autowired
    OapServiceGroupService oapServiceGroupService;

    @ApiOperation(value = "서비스 그룹 조회", notes = "서비스 그룹 조회", response = List.class)
    @RequestMapping(value = "/{serviceNumber}", method = RequestMethod.GET)
    public Object getServiceGroupList(@ApiParam(value="서비스 번호", defaultValue="123") @PathVariable long serviceNumber) {
        return oapServiceGroupService.selectServiceGroupList(serviceNumber);
    }

    @ApiOperation(value = "서비스 그룹 등록", notes = "서비스 그룹 등록", response = ApiGroup.ApiGroupRelation.class)
    @RequestMapping(value = "/{serviceNumber}", method = RequestMethod.POST)
    public Object createServiceGroup(@ApiParam(value="서비스 번호", defaultValue="123") @PathVariable long serviceNumber,
                                      @RequestBody ApiGroup.ApiGroupRelation apiGroup) {
        oapServiceGroupService.createApiGroup(serviceNumber, apiGroup);
        return apiGroup;
    }

    @ApiOperation(value = "서비스 그룹 수정", notes = "서비스 그룹 수정", response = ApiGroup.ApiGroupRelation.class)
    @RequestMapping(value = "/{serviceNumber}", method = RequestMethod.PUT)
    public Object updateServiceGroup(@ApiParam(value="서비스 번호", defaultValue="123") @PathVariable long serviceNumber,
                                      @RequestBody ApiGroup.ApiGroupRelation apiGroup) {
        oapServiceGroupService.updateApiGroup(serviceNumber, apiGroup);
        return apiGroup;
    }

    @ApiOperation(value = "서비스 그룹 순서 변경", notes = "서비스 그룹 순서 변경", response = ApiGroup.ApiGroupRelation.class)
    @RequestMapping(value = "/sort/{serviceNumber}", method = RequestMethod.PUT)
    public Object updateServiceGroupSort(@ApiParam(value="서비스 번호", defaultValue="123") @PathVariable long serviceNumber,
                                     @RequestBody ApiGroup.ApiGroupRelation apiGroup) {
        oapServiceGroupService.updateApiGroupOrder(serviceNumber, apiGroup);
        return apiGroup;
    }

    @ApiOperation(value = "서비스 그룹 삭제", notes = "서비스 그룹 삭제", response = Long.class)
    @RequestMapping(value = "/{serviceNumber}/{opponentApiGroupLevel}/{apiGroupNumber}", method = RequestMethod.DELETE)
    public Object deleteServiceGroup(@ApiParam(value="서비스 번호", defaultValue="123") @PathVariable long serviceNumber,
                                     @ApiParam(value="그룹 레벨", defaultValue="123") @PathVariable long opponentApiGroupLevel,
                                     @ApiParam(value="그룹 번호", defaultValue="123") @PathVariable long apiGroupNumber) {
        oapServiceGroupService.deleteApiGroup(serviceNumber, opponentApiGroupLevel, apiGroupNumber);
        return apiGroupNumber;
    }
}
