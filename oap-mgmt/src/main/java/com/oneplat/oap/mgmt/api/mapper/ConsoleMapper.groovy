package com.oneplat.oap.mgmt.api.mapper

import com.oneplat.oap.mgmt.api.model.ApiComponent
import com.oneplat.oap.mgmt.api.model.Api
import com.oneplat.oap.mgmt.api.model.ApiService
import com.oneplat.oap.mgmt.api.model.ApplicationConsole
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

/**
 *
 * ConsoleMapper
 *
 * Created by Hong Gi Seok 2016-12-08
 */
@Mapper
interface ConsoleMapper {

    @Select("""
    <script>
        SELECT
          API_NUM AS apiNumber
         ,API_COMP_CD AS apiComponentCode
         ,API_COMP_DATA AS apiComponentData
         ,API_COMP_DELETE_YN AS apiComponentDeleteYn
         ,CREATE_DATETIME AS createDateTime
         ,CREATE_ID AS createId
         ,MODIFY_DATETIME AS modifyDateTime
         ,MODIFY_ID AS modifyId
        FROM
          MC_API_COMP
        WHERE
          API_NUM = #{apiNumber}
    </script>
    """)
    List<ApiComponent> selectApiComponent(Long apiNumber);

    @Select("""
    <script>
        SELECT T1.apiNumber
             , T1.apiGroupNumber
             , CASE WHEN T1.apiGroupName2 IS NOT NULL THEN CONCAT(T1.apiGroupName2,' > ',T1.apiGroupName)
                    ELSE T1.apiGroupName END AS apiGroupName
             , T1.adaptorNumber
             , T1.siteCode, T1.siteCodeName
             , T1.apiName, T1.apiDesc
             , T1.apiSectionCode, T1.apiSectionCodeName
             , T1.apiVersion
             , T1.httpMethodCode, T1.httpMethodCodeName
             , T1.nbBaseUrl
             , T1.apiStateCode, T1.apiStateCodeName
             , T1.serviceBeginDateTime, T1.serviceEndDateTime
             , T1.apiUseYn, T1.apiDeleteYn
             , T1.createDateTime, T1.createId
             , T1.modifyDateTime, T1.modifyId
        FROM   (
                SELECT
                  MA.API_NUM AS apiNumber
                 ,MA.API_GRP_NUM AS apiGroupNumber
                 ,MAG.API_GRP_NM AS apiGroupName
                 ,(SELECT G.API_GRP_NM
                   FROM   MC_API_GRP_RELATION R INNER JOIN MC_API_GRP G
                          ON (R.CRITERIA_API_GRP_NUM = G.API_GRP_NUM AND R.CRITERIA_API_GRP_NUM > 0 AND R.END_DATETIME > NOW())
                   WHERE  R.OPPONENT_API_GRP_NUM = MA.API_GRP_NUM) AS apiGroupName2
                 ,MA.ADAPTOR_NUM AS adaptorNumber
                 ,MA.SITE_CD AS siteCode
                 ,(SELECT f_code_name(MA.SITE_CD)) AS siteCodeName
                 ,MA.API_NM AS apiName
                 ,MA.API_DESC AS apiDesc
                 ,MA.API_SECTION_CD AS apiSectionCode
                 ,(SELECT f_code_name(MA.API_SECTION_CD)) AS apiSectionCodeName
                 ,MA.API_VER AS apiVersion
                 ,MA.HTTP_METHOD_CD AS httpMethodCode
                 ,(SELECT f_code_name(MA.HTTP_METHOD_CD)) AS httpMethodCodeName
                 ,MA.NB_API_URL AS nbBaseUrl
                 ,MA.API_STATE_CD AS apiStateCode
                 ,(SELECT f_code_name(MA.API_STATE_CD)) AS apiStateCodeName
                 ,MA.SVC_BEGIN_DATETIME AS serviceBeginDateTime
                 ,MA.SVC_END_DATETIME AS serviceEndDateTime
                 ,MA.API_USE_YN AS apiUseYn
                 ,MA.API_DELETE_YN AS apiDeleteYn
                 ,MA.CREATE_DATETIME AS createDateTime
                 ,MA.CREATE_ID AS createId
                 ,MA.MODIFY_DATETIME AS modifyDateTime
                 ,MA.MODIFY_ID AS modifyId
                FROM MC_API MA
                  INNER JOIN MC_API_GRP MAG ON MA.API_GRP_NUM = MAG.API_GRP_NUM
                  INNER JOIN MC_SVC MS ON MS.SVC_NUM = MAG.SVC_NUM
                WHERE MS.SVC_NUM = #{serviceNumber}
                  AND MA.API_DELETE_YN = 'N'
                  AND MA.API_USE_YN = 'Y'
        ) T1
    </script>
    """)
    List<Api> selectApiList(long serviceNumber);


    @Select("""
        SELECT
          SVC_NUM AS serviceNumber
         ,SITE_CD AS siteCode
         ,SVC_NM AS serviceName
         ,SVC_CONTEXT AS serviceContext
         ,SVC_SECTION_CD AS serviceSectionCode
         ,SANDBOX_USE_YN AS sandBoxUseYn
         ,SVC_APPROVAL_YN AS serviceApprovalYn
         ,SLA_USE_YN AS slaUseYn
         ,CAPACITY_USE_YN AS capacityUseYn
         ,SVC_DESC AS serviceDescription
         ,ICON_FILE_CHANNEL AS iconFileChannel
         ,INSIDE_SORT_NUM AS insideSortNumber
         ,OUTSIDE_SORT_NUM AS outsideSortNumber
         ,SVC_REGISTER_DATE AS serviceRegisterDate
         ,SVC_USE_YN AS serviceUseYn
         ,SVC_DELETE_YN AS serviceDeleteYn
         ,SVC_COMP_USE_YN AS serviceComponentUseYn
         ,API_SECTION_CD AS apiSectionCode
         ,NB_BASE_URL AS nbBaseUrl
         ,SB_BASE_URL AS sbBaseUrl
         ,SB_BASE_TEST_URL AS sbBaseTestUrl
         ,CREATE_DATETIME AS createDateTime
         ,CREATE_ID AS createId
         ,MODIFY_DATETIME AS modifyDateTime
         ,MODIFY_ID AS modifyId
        FROM
          MC_SVC
        WHERE
          SVC_USE_YN = 'Y' AND
          SVC_DELETE_YN = 'N'
    """)
    List<ApiService> selectApiServiceList();

    @Select("""
        SELECT f_code_name(#{code})
    """)
    String getCosoleCodeName(String code);


    @Select("""
        SELECT
          APP_NUM AS applicationNumber
         ,APP_KEY AS applicationKey
         ,SVC_GRADE_CD AS serviceGradeCode
         ,HMAC_AUTH_TYPE_CD AS hmacAuthTypeCode
         ,MSG_ENCRYPTION_KEY AS msgEncryptionKey
         ,MSG_ENCRYPTION_TYPE_CD AS msgEncryptionType
        FROM
          DC_APP_KEY
        WHERE
          APP_NUM = 10000001 AND
          KEY_TYPE_CD = 'DC_KEY_TYPE_01' AND
          KEY_DELETE_YN = 'N'
    """)
    List<ApplicationConsole> selectApplicationConsole();
}