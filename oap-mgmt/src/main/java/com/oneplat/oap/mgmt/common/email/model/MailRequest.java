package com.oneplat.oap.mgmt.common.email.model;

import com.oneplat.oap.mgmt.common.email.model.enums.EmailTemplateCode;
import lombok.Data;

import java.util.HashMap;

/**
 * @author lee
 * @date 2016-12-29
 */
@Data
public class MailRequest {

    EmailTemplateCode emailTemplateCode;
    String receiverNm;
    String[] receiverEmails;
    HashMap<String, Object> convertInfoMap;

    public MailRequest(EmailTemplateCode emailTemplateCode, String[] receiverEmails, HashMap<String, Object> convertInfoMap){
        super();
        this.emailTemplateCode = emailTemplateCode;
        this.receiverEmails = receiverEmails;
        this.convertInfoMap = convertInfoMap;
    }
}
