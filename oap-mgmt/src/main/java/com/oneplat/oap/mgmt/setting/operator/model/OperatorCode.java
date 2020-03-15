package com.oneplat.oap.mgmt.setting.operator.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.oneplat.oap.core.annotation.DbValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum OperatorCode {
    MC_OPR_STATE_STAND_BY("MC_OPR_STATE_01", "승인 요청"),
    MC_OPR_STATE_RE_STAND_BY("MC_OPR_STATE_02", "재승인 요청"),
    MC_OPR_STATE_REJECT("MC_OPR_STATE_03", "반려"),
    MC_OPR_STATE_NORMAL("MC_OPR_STATE_04", "정상"),
    MC_OPR_STATE_DELETE("MC_OPR_STATE_05", "삭제"),
    MC_OPR_STATE_LOCK("MC_OPR_STATE_06", "잠금"),
    MC_OPR_ACCOUNT_LOCK("Y", "계정잠금"),
    MC_OPR_ACCOUNT_UNLOCK("N", "정상"),
    
    MC_ROLE_STATE_STAND_BY("MC_ROLE_STATE_01", "승인 요청"),
    MC_ROLE_STATE_RE_STAND_BY("MC_ROLE_STATE_02", "재승인 요청"),
    MC_ROLE_STATE_REJECT("MC_ROLE_STATE_03", "반려"),
    MC_ROLE_STATE_NORMAL("MC_ROLE_STATE_04", "승인"),
    
    MC_PERSON_INFO_SECTION_01("MC_PERSON_INFO_SECTION_01", "고객 개인정보"),
    MC_PERSON_INFO_SECTION_02("MC_PERSON_INFO_SECTION_02", "판매 사업자 정보"),
    MC_PERSON_INFO_SECTION_03("MC_PERSON_INFO_SECTION_03", "판매 담당자 정보");

    private @Getter(onMethod = @__({@DbValue,@JsonValue}) ) String code;
    private @Getter() String name;
    
}
