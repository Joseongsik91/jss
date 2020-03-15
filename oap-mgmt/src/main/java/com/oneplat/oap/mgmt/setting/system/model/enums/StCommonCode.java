/**
 * 
 */
package com.oneplat.oap.mgmt.setting.system.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.oneplat.oap.core.annotation.DbValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ejyoo
 * ST 관련코드
 */
@AllArgsConstructor
public enum StCommonCode {

    /* kg_uppp.ST_COMMON_CD 에 정의된  
     * ST관련 코드를 저장시 서비스 로직에서 사용하기 위해 enum화 하였음
     * 해당 내용은 현행화되지 않을수 있으므로
     * 실제 조회 화면에서는 DB로 조회하여 표시하기를 권함 
     * */
    ST_SEND_SECTION_AD("ST_SEND_SECTION_01", "광고"),
    ST_SEND_SECTION_NOTICE("ST_SEND_SECTION_02", "안내"),
    
    ST_RECEIVE_STATE_STANDBY("ST_RECEIVE_STATE_01", "대기"),
    ST_RECEIVE_STATE_SUCCESS("ST_RECEIVE_STATE_02", "성공"),
    ST_RECEIVE_STATE_FAIL("ST_RECEIVE_STATE_03", "실패"),

    ST_SEND_TYPE_RESERV("ST_SEND_TYPE_01", "예약"),
    ST_SEND_TYPE_IMMEDIATELY("ST_SEND_TYPE_02", "즉시"),
    ST_SEND_TYPE_AUTO("ST_SEND_TYPE_03", "자동"),
    
    ST_SEND_STATE_STANDBY("ST_SEND_STATE_01", "대기"),
    ST_SEND_STATE_SUCCESS("ST_SEND_STATE_02", "성공"),
    ST_SEND_STATE_CANCEL("ST_SEND_STATE_03", "취소"),
    ST_SEND_STATE_FAIL("ST_SEND_STATE_04", "실패"),
    
    MC_MENU_AUTH_READ("MC_MENU_AUTH_01", "읽기"),
    MC_MENU_AUTH_WRITE("MC_MENU_AUTH_02", "쓰기");
    

    private @Getter(onMethod = @__({@DbValue,@JsonValue}) ) String code;
    private @Getter() String name;

}
