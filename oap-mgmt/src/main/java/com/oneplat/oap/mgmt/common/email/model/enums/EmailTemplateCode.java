package com.oneplat.oap.mgmt.common.email.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lee
 * @date 2016-12-29
 */
@AllArgsConstructor
public enum EmailTemplateCode {
    OPERATOR_JOIN_REQUEST("/templates/email/MC_EMAIL_TEMPLATE_01.html","운영자 가입 신청승인 요청 메일"),
    OPERATOR_JOIN_COMPLETE("/templates/email/MC_EMAIL_TEMPLATE_02.html","운영자 가입 승인 안내 메일"),
    OPERATOR_JOIN_REJECT("/templates/email/MC_EMAIL_TEMPLATE_03.html","운영자 가입 반려 안내 메일"),
    OPERATOR_JOIN_CREATE("/templates/email/MC_EMAIL_TEMPLATE_04.html","운영자 가입 안내 메일"),
    OPERATOR_JOIN_PASSWORD("/templates/email/MC_EMAIL_TEMPLATE_05.html","임시 비밀번호 안내 메일");

    private @Getter() String path;
    private @Getter() String title;
}
