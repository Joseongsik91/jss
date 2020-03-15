package com.oneplat.oap.mgmt.dashboard.controller;

import com.oneplat.oap.mgmt.dashboard.model.DashboardNotice;
import com.oneplat.oap.mgmt.dashboard.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * DashboardController
 * <p>
 * Created by chungyeol.kim on 2017-02-06.
 */
@Api(description="대시보드", produces = "application/json")
@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @RequestMapping(value="/notice", method = RequestMethod.GET)
    @ApiOperation(value = "대시보드 notice 정보", notes = "대시보드 notice 정보", response = DashboardNotice.class)
    public Object dashboardNotice(){
        return dashboardService.getDashboardNotice();
    }

}
