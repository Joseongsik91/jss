package com.oneplat.oap.mgmt.setting.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.setting.system.mapper.SchedulerMapper;
import com.oneplat.oap.mgmt.setting.system.model.Schedule;
import com.oneplat.oap.mgmt.setting.system.service.SchedulerManagementServiceForSearch;

@Service
public class SchedulerManagementServiceForSearchImpl implements SchedulerManagementServiceForSearch {

    @Autowired
    SchedulerMapper schedulerMapper;
    @Autowired
    AuthenticationInjector authenticationInjector;

    @Override
    public List<Schedule> scheduleList(SearchRequest searchRequest) {
        // TODO Auto-generated method stub
        if (searchRequest.getPage() == null || searchRequest.getSize() == null) {
            searchRequest.setPage(1);
            searchRequest.setSize(25);
        }
        searchRequest.setData();

        if (searchRequest.getQueryData("searchscheduleName") != null) {
            String nameCheck = (String) searchRequest.getQueryData("searchscheduleName");
            if (!"".equals(nameCheck)) {
                nameCheck = "%" + nameCheck + "%";
                searchRequest.getQuery().put("searchscheduleName", nameCheck);
            }
        }

        List<Schedule> res = schedulerMapper.scheduleList(searchRequest);

        if (searchRequest.getPageInfo() != null) {
            searchRequest.getPageInfo().setResultCount(res.size());
            searchRequest.getPageInfo().setTotalCount(schedulerMapper.scheduleListTotalConunt(searchRequest));
        }

        return res;
    }


    @Override
    public Schedule getScheduleInfo(Long scheduleNumber) {
        // TODO Auto-generated method stub
        return schedulerMapper.getScheduleInfo(scheduleNumber);
    }

}