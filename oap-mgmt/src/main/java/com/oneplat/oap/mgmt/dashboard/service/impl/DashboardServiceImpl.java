package com.oneplat.oap.mgmt.dashboard.service.impl;


import com.oneplat.oap.mgmt.dashboard.mapper.DashboardMapper;
import com.oneplat.oap.mgmt.dashboard.model.DashboardNotice;
import com.oneplat.oap.mgmt.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DashboardServiceImpl
 * <p>
 * Created by chungyeol.kim on 2017-02-06.
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardMapper dashboardMapper;

    @Override
    public DashboardNotice getDashboardNotice() {
        DashboardNotice dashboardNotice = new DashboardNotice();
        dashboardNotice.setSchedulerFailCount(dashboardMapper.selectScheduleFailCount());
        dashboardNotice.setOperatorApprovalCount(dashboardMapper.selectOperatorApprovalCount());
        return dashboardNotice;
    }
}
