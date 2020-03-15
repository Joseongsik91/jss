package com.oneplat.oap.mgmt.oapservice.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.oneplat.oap.core.annotation.DbValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lee
 * @date 2016-12-15
 */
@AllArgsConstructor
public enum ServiceComposeCode {
    HEADER("MC_SVC_COMP_01"),
    CONTENTTYPE("MC_SVC_COMP_02"),
    ACCEPT("MC_SVC_COMP_03");
    private @Getter(onMethod = @__({@DbValue, @JsonValue}) ) String code;
}
