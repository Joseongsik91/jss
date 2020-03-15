package com.oneplat.oap.mgmt.oapservice.service;

import com.oneplat.oap.mgmt.oapservice.model.ApiGroup;

import java.util.List;

/**
 * The interface Service group service.
 *
 * @author lee
 * @date 2016 -12-20
 */
public interface OapServiceGroupService {
    /**
     * Select All Group list.
     * 초기화 데이타를 위한 리스트 조회 추가 
     * @return the list
     */
    public List<ApiGroup.ApiGroupRelation> selectServiceGroupListExceptRoot(long serviceNumber); 
    /**
     * Select service list list.
     *
     * @param serviceNumber the service number
     * @return the list
     */
    public List<ApiGroup.ApiGroupRelation> selectServiceGroupList(long serviceNumber);

    /**
     * Create api group.
     *
     * @param serviceNumber the service number
     * @param apiGroup      the api group
     */
    public void createApiGroup(long serviceNumber, ApiGroup.ApiGroupRelation apiGroup);

    /**
     * Update api group.
     *
     * @param serviceNumber the service number
     * @param apiGroup      the api group
     */
    public void updateApiGroup(long serviceNumber, ApiGroup.ApiGroupRelation apiGroup);

    /**
     * Update api group order.
     *
     * @param serviceNumber    the service number
     * @param apiGroupRelation the api group
     */
    public void updateApiGroupOrder(long serviceNumber, ApiGroup.ApiGroupRelation apiGroupRelation);

    /**
     * Delete api group.
     *
     * @param serviceNumber the service number
     * @param apiGroupNumber      the api group
     */
    public void deleteApiGroup(long serviceNumber, long opponentApiGroupLevel, long apiGroupNumber);
}
