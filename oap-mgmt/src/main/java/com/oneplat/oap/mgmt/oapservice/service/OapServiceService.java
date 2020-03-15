package com.oneplat.oap.mgmt.oapservice.service;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.oapservice.model.DashboardService;
import com.oneplat.oap.mgmt.oapservice.model.OapService;

import java.util.List;
import java.util.Map;

/**
 * The interface Oap service service.
 *
 * @author lee
 * @date 2016 -12-01
 */
public interface OapServiceService {


    /**
     * Select total order list list.
     *
     * @param searchRequest the search request
     * @return the list
     */
    public List<OapService> selectServiceList(SearchRequest searchRequest);


    /**
     * Create service.
     *
     * @param oapService the oap service
     */
    public void createService(OapService oapService);


    /**
     * Select service oap service.
     *
     * @param serviceNumber the service number
     * @return the oap service
     */
    public OapService selectService(long serviceNumber);

    /**
     * Update service.
     *
     * @param serviceNumber the service number
     * @param oapService    the oap service
     */
    public void updateService(long serviceNumber, OapService oapService);


    /**
     * Delete service.
     *
     * @param serviceNumber the service number
     */
    public void deleteService(long serviceNumber);


    /**
     * Select service history list list.
     *
     * @param serviceNumber the service number
     * @return the list
     */
    public List<OapService> selectServiceHistoryList(long serviceNumber);

    /**
     * Select service name count int.
     *
     * @param serviceContext the service context
     * @return the int
     */
    public int selectServiceNameCount(String serviceContext);

    /**
     * Select service delete inquiry int.
     *
     * @param serviceNumber the service number
     * @return the List
     */
    public List<Integer> selectServiceDeleteInquiry(long serviceNumber);

    /**
     * Select service list for dashboard
     *
     * @param searchRequest
     * @return
     */
    DashboardService selectServiceListForDashBoard(SearchRequest searchRequest);
}
