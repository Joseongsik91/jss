package com.oneplat.oap.mgmt.api.model;

import com.oneplat.oap.core.model.AbstractObject;
import lombok.Data;

/**
 * Api
 * <p>
 * Created by Hong Gi Seok on 2016-12-15.
 */
@Data
public class ApiService extends AbstractObject {
    Long   serviceNumber;
    String siteCode;
    String serviceName;
    String serviceContext;
    String serviceSectionCode;
    String sandBoxUseYn;
    String serviceApprovalYn;
    String slaUseYn;
    String capacityUseYn;
    String serviceDescription;
    String iconFileChannel;
    int    insideSortNumber;
    int    outsideSortNumber;
    String serviceRegisterDate;
    String serviceUseYn;
    String serviceDeleteYn;
    String serviceComponentUseYn;
    String apiSectionCode;
    String nbBaseUrl;
    String sbBaseUrl;
    String sbBaseTestUrl;
}
