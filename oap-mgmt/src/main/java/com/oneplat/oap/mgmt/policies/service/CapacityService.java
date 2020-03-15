package com.oneplat.oap.mgmt.policies.service;

import com.oneplat.oap.mgmt.policies.model.ServicePolicy;

import java.util.List;

/**
 * Created by LSH on 2016. 11. 30..
 */
public interface CapacityService {
    /**
     * Capacity list list.
     *
     * @return the list
     */
    List<ServicePolicy> capacityList();

    /**
     * Modify capacity.
     *
     * @param servicePolicy the service policy
     */
    void modifyCapacity(ServicePolicy servicePolicy);

    /**
     * Create capacity policy service policy.
     *
     * @param serviceNumber  the service number
     * @param apiGroupNumber the api group number
     * @return the service policy
     */
    ServicePolicy createCapacityPolicy(long serviceNumber, long apiGroupNumber);

    /**
     * Update capacity policy.
     *
     * @param serviceNumber  the service number
     * @param apiGroupNumber the api group number
     */
    void updateCapacityPolicy(long serviceNumber, long apiGroupNumber);

    /**
     * Update capacity policy delete.
     *
     * @param serviceNumber  the service number
     * @param apiGroupNumber the api group number
     */
    void updateCapacityPolicyDelete(long serviceNumber, long apiGroupNumber);
}
