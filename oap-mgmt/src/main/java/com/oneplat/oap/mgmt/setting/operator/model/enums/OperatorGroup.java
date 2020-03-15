package com.oneplat.oap.mgmt.setting.operator.model.enums;

import com.oneplat.oap.core.annotation.DbValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hyung Joo Lee
 *
 */
@AllArgsConstructor
public enum OperatorGroup {

    PLATFORM("PLATFORM", "플랫폼담당"),
    HOUSING("HOUSING", "housing담당"),
    NEWFORM("NEWFORM", "newform담당"),
    SHOP("SHOP", "shop담당"),
    SERVICE("SERVICE", "service담당"),
    ACCOUNT("ACCOUNT", "정산담당"),
    CS("CS", "CS담당"),
    DEVELOP("DEVELOP", "개발담당");

    private @Getter(onMethod = @__(@DbValue) ) String code;
    private @Getter String displayName;

}
