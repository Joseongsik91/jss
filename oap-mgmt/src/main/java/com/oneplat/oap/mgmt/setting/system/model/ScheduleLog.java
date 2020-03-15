package com.oneplat.oap.mgmt.setting.system.model;

import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.mybatis.enums.YesNoType;

import lombok.Data;

@Data
public class ScheduleLog extends AbstractObject {

    // 스케줄 로그 번호
    private Long scheduleLogNumber;
    // 스케줄 번호
    private Long scheduleNumber;
    // 로그
    private String log;
    // 성공여부
    private YesNoType successYn;
    // 스케줄명
    private String scheduleName;
    // 정렬 번호
    private int sortNumber;
}