package com.oneplat.oap.mgmt.policies.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.oneplat.oap.core.annotation.DbValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lee
 * @date 2016-12-13
 */
@AllArgsConstructor
public enum OapServicePolicyCode {
    CAPACITY("MC_SVC_POLICY_01"),
    SLA("MC_SVC_POLICY_02");
    private @Getter(onMethod = @__({@DbValue, @JsonValue}) ) String code;
}
