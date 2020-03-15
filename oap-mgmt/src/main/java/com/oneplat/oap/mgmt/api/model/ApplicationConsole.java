package com.oneplat.oap.mgmt.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * ApplicationConsole
 * <p>
 * Created by Hong Gi Seok on 2017-02-01.
 */
@ApiModel
@Data
public class ApplicationConsole {

    long applicationNumber;
    String applicationKey;
    String hmacAuthTypeCode;
    String msgEncryptionKey;
    String msgEncryptionType;
    String serviceGradeCode;

}
