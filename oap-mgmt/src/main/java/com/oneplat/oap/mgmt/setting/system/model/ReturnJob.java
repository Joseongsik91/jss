package com.oneplat.oap.mgmt.setting.system.model;

import com.oneplat.oap.core.mybatis.enums.YesNoType;

import lombok.Data;

/**
 * @author lee
 * @date 2016-07-27
 */
@Data
public class ReturnJob {
    private YesNoType successYn;
    private String message;
}
