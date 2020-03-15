package com.oneplat.oap.mgmt.policies.service;

import com.oneplat.oap.mgmt.policies.model.ServicePolicy;

import java.util.List;

/**
 * Created by LSH on 2016. 11. 30..
 */
public interface SlaService  {
    /**
     * Sla list list.
     *
     * @return the list
     */
    List<ServicePolicy> slaList();

    /**
     * Modify sla.
     *
     * @param servicePolicy the service policy
     */
    void modifySla(ServicePolicy servicePolicy);

    /**
     * Create sla policy service policy.
     *
     * @param serviceNumber  the service number
     * @param apiGroupNumber the api group number
     * @return the service policy
     */
    ServicePolicy createSlaPolicy(long serviceNumber, long apiGroupNumber);

    /**
     * Update sla policy.
     *
     * @param serviceNumber  the service number
     * @param apiGroupNumber the api group number
     */
    void updateSlaPolicy(long serviceNumber, long apiGroupNumber);

    /**
     * Update sla policy delete.
     *
     * @param serviceNumber  the service number
     * @param apiGroupNumber the api group number
     */
    void updateSlaPolicyDelete(long serviceNumber, long apiGroupNumber);
}
