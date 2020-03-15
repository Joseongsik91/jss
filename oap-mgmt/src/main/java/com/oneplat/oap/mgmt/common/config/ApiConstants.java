package com.oneplat.oap.mgmt.common.config;

public final class ApiConstants {
    public static final String JSON_VALIDATION_FAILD = "1401";
    public static final String ID_NOT_FOUND = "1402";
    public static final String ID_CONFLICT = "1403";
    public static final String API_URI_VALIDATION_FAILD = "1404";
    public static final String SERVICE_PARAMETER_VALIDATION_FAILD = "1405";
    public static final String SERVICE_ID_VALIDATION_FAILD = "1406";
    public static final String APPKEYID_VALIDATION_FAILD = "1407";
    public static final String DATA_VALIDATION_FAILD = "1408";
    public static final String DATA_NOT_FOUND = "1409";
    public static final String DATA_EXIST_NOT_DELETE = "1410";
    public static final String DATA_INTERFACE_FAIL = "1700";
    
    public static final String URL_ACCESS_DENY = "1600";
    public static final String LOGIN_FAIL = "1601";
    public static final String WRITE_AUTHORITY_FAIL = "1602";
    public static final String INTERNAL_ERROR = "8401";
    
    public final static String LanguageKOREA = "KOR";

    public final static String REG_EMAIL = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$";

    public final static String REG_PHONE_NUMBER = "[0][1][01789]\\s*-\\s*[0-9]{3,4}\\s*-\\s*[0-9]{4}";

    public final static String REG_NUMBER= "^[0-9]*$";

    public final static String REG_ALPHABET= "^[a-zA-Z]*$";

    public final static String REG_KO_LANGUAGE = "^[가-힣]*$";

    public final static String REG_START_ALPHABET_WITH_NUMBER = "^[a-zA-Z]([a-zA-Z0-9])*$";

    public final static String REG_START_ALPHABET_WITH_SLUSH = "^[a-zA-Z]([a-zA-Z0-9/])*$";

    public final static String REG_ALPHABET_WITH_NUMBER = "^[a-zA-Z0-9]*$";

    public final static String REG_URL = "https?://([^/]+)/.*";

    public final static String REG_API_KEY = "^[a-zA-Z]([a-zA-Z0-9_/:])*$";
    
    public final static String isdStnCodeDefaultText       = "ISD"; // 내부/외부 구분 기본값
    
    public final static String GUD_MEN_STN_CD_SERVICE      = "MENU_SERVICE"; // 카테고리 서비스 구분
    
    public final static String GUD_MEN_STN_CD_COMPONENT    = "MENU_COMPONENT"; // 카테고리 컴포넌트 구분
    
    public final static String GUD_MEN_STN_CD_STATISTIC    = "MENU_STATISTIC"; // 통계 카테고리  구분
    
    
    //CommonCode 데이타 
    public final static String CONTENT_TYPE_CODE = "CONTENT_TYPE";
    public final static String HEADER_TYPE_CODE = "HEADER";
    public final static String ACCEPT_TYPE_CODE = "ACCEPT";
    public final static String PROTOCOL_TYPE_CODE = "PROTOCOL";
    
    public final static String API_CREATED_TYPE_CODE = "CREATED";
    public final static String API_TESTED_TYPE_CODE = "TESTED";
    public final static String API_PUBKISHED_TYPE_CODE = "PUBLISHED";
    
    public final static String POLICY_CATPACITY_TYPE_CODE = "CAP";
    public final static String POLICY_SLA_TYPE_CODE = "SLA";
    public final static String POLICY_ABUSSING_TYPE_CODE = "ABU";

    public final static String POLICY_SERVICE_TYPE_CODE = "SERVICE";
    public final static String POLICY_APIGROUP_TYPE_CODE = "GROUP";
    
    public final static String SECOND_TYPE_CODE = "MC_SVC_CRITERIA_01";
    //public final static String SECOND_TYPE_CODE = "SEC";
    public final static String MINUTE_TYPE_CODE = "MC_SVC_CRITERIA_02";
    //public final static String MINUTE_TYPE_CODE = "MIN";
    public final static String HOUR_TYPE_CODE = "MC_SVC_CRITERIA_03";
    //public final static String HOUR_TYPE_CODE = "HOUR";

    public final static String CONTENT_JSON_TYPE_CODE = "JSON";
    public final static String CONTENT_YAML_TYPE_CODE = "YAML";
    
    
    public final static String DAY_TYPE_CODE = "MC_SVC_CRITERIA_04";
    //public final static String DAY_TYPE_CODE = "DAY";
    public final static String MONTH_TYPE_CODE = "MC_SVC_CRITERIA_05";
    //public final static String MONTH_TYPE_CODE = "MON";
    public final static String YEAR_TYPE_CODE = "YEAR";
    
    public final static String MODEL_DEVELOPER_CODE = "DER";
    public final static String MODEL_APPLICATION_CODE = "APP";
    
    public final static String TEMPLATE_GROUP_SERVICE_CODE = "SERVICE";
    public final static String TEMPLATE_GROUP_MEMBER_CODE = "MEMBER";
    public final static String TEMPLATE_GROUP_COMMUNITY_CODE = "COMMUNITY";
    
    public final static String EMAIL_TARGET_OPERATOR_TYPE = "OPR";
    public final static String EMAIL_TARGET_DEVELOPER_TYPE = "DEV";
    public final static String EMAIL_TARGET_ETC_TYPE = "ETC";
    
    public final static String EMAIL_SEND_INDIVIDUAL_TYPE = "IND";
    public final static String EMAIL_SEND_BATCH_TYPE = "BAT";
    
    public final static String EMAIL_SEND_PUSH_TYPE = "PUS";
    public final static String EMAIL_SEND_SUCCESS_TYPE = "SUC";
    public final static String EMAIL_SEND_FAIL_TYPE = "FAL";
    
    
    public final static String OPERATOR_REQUESTED_TYPE_CODE = "REQ";
    public final static String OPERATOR_REJECTED_TYPE_CODE = "REJ";
    public final static String OPERATOR_APROVED_TYPE_CODE = "APR";
    
    public final static String METHOD_GET_TYPE = "GET";
    public final static String METHOD_PUT_TYPE = "PUT";
    public final static String METHOD_POST_TYPE = "POST";
    public final static String METHOD_DELETE_TYPE = "DELETE";
    
    public final static String CYCLE_DIRECT_TYPE = "DRT";
    public final static String CYCLE_EASY_TYPE = "EAS";
    
    public final static String APP_TYPE_TEST = "DEV";
    public final static String APP_TYPE_PRODUCT = "PRO";
}

