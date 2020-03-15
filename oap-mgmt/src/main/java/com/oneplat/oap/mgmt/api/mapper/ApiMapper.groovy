package com.oneplat.oap.mgmt.api.mapper

import com.oneplat.oap.mgmt.api.model.Api
import com.oneplat.oap.mgmt.api.model.ApiComp
import com.oneplat.oap.mgmt.api.model.ApiDashBoard
import com.oneplat.oap.mgmt.api.model.ApiGeneralInfo
import com.oneplat.oap.mgmt.api.model.ApiGroupInfo
import com.oneplat.oap.mgmt.api.model.ApiSearchRequest
import com.oneplat.oap.mgmt.api.model.HistoryManagement
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectKey
import org.apache.ibatis.annotations.Update

/**
 *
 * ApiMapper
 *
 * Created by chungyeol.kim on 2016-11-28.
 */
@Mapper
interface ApiMapper {

    @Select("""
    <script>
        SELECT  API.API_NUM AS apiNumber
              , A.API_GRP_NUM AS apiGroupNumber
              , A.API_GRP_NM AS apiGroupName
              , A.OPPONENT_API_GRP_LEVEL AS apiGroupLevel
              , API.ADAPTOR_NUM AS adaptorNumber
              , API.API_NM AS apiName
              , API.API_SECTION_CD AS apiSectionCode
              , API.API_VER AS apiVersion
              , API.HTTP_METHOD_CD AS httpMethodCode
              , f_code_name(API.HTTP_METHOD_CD) AS httpMethodCodeName
              , API.NB_API_URL AS nbBaseUrl
              , API.API_STATE_CD AS apiStateCode
              , f_code_name(API.API_STATE_CD) AS apiStateCodeName
              , API.API_USE_YN AS apiUseYn
              , API.API_DELETE_YN AS apiDeleteYn
              , DATE_FORMAT(API.CREATE_DATETIME, '%Y-%m-%d %H:%i:%s') AS createDateTime
              , API.CREATE_ID AS createId
              , A.CRITERIA_API_GRP_NUM AS criteriaApiGroupNumber
        FROM
                ( SELECT    IFNULL(GRP.API_GRP_NUM, 0) AS API_GRP_NUM
                          , GRP.SVC_NUM AS SVC_NUM
                          , GRP.API_GRP_NM AS API_GRP_NM
                          , REL.OPPONENT_API_GRP_LEVEL AS OPPONENT_API_GRP_LEVEL
                          , REL.CRITERIA_API_GRP_NUM AS CRITERIA_API_GRP_NUM
                          , REL.CRITERIA_API_GRP_LEVEL AS CRITERIA_API_GRP_LEVEL
                          , REL.OPPONENT_SORT_NUM AS OPPONENT_SORT_NUM
                  FROM      MC_API_GRP GRP,
                            MC_API_GRP_RELATION REL
                  WHERE     GRP.API_GRP_NUM = REL.OPPONENT_API_GRP_NUM
                  AND       GRP.API_GRP_USE_YN = 'Y'
                  AND       GRP.API_GRP_DELETE_YN = 'N'
                  AND       REL.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                ) A
        LEFT OUTER JOIN MC_API API
        ON      A.API_GRP_NUM = API.API_GRP_NUM
        AND     API.API_DELETE_YN = 'N'
        <if test="(searchWordTypeCode != null and searchWordTypeCode == 'API_NM') and (searchWord != null and searchWord != '')">
            AND     API.API_NM LIKE CONCAT('%', #{searchWord}, '%')
        </if>
        <if test="(searchWordTypeCode != null and searchWordTypeCode == 'GROUP_NM') and (searchWord != null and searchWord != '')">
            AND     A.API_GRP_NM LIKE CONCAT('%', #{searchWord}, '%')
        </if>
        <if test="(searchWordTypeCode != null and searchWordTypeCode == 'BASE_URL') and (searchWord != null and searchWord != '')">
            AND     A.NB_API_URL LIKE CONCAT('%', #{searchWord}, '%')
        </if>
        <if test="siteCode != null and siteCode != ''">
            AND     API.SITE_CD = #{siteCode}
        </if>
        <if test="apiSectionCode != null and apiSectionCode != ''">
            AND     API.API_SECTION_CD = #{apiSectionCode}
        </if>
        <if test="adaptorNumber != null">
            AND     API.ADAPTOR_NUM = #{adaptorNumber}
        </if>
        <if test="apiStateCode != null and apiStateCode != ''">
            AND     API.API_STATE_CD = #{apiStateCode}
        </if>
        <if test="useYn != null and useYn != ''">
            AND     API.API_USE_YN = #{useYn}
        </if>
        <if test="periodTypeCode != null and periodTypeCode == 'CREATE'">
            <choose>
                <when test="(startDateTime != null and startDateTime != '') and (endDateTime != null and endDateTime != '')">
                    AND     DATE_FORMAT(API.CREATE_DATETIME, '%Y-%m-%d') BETWEEN #{startDateTime} AND #{endDateTime}
                </when>
                <when test="startDateTime != null and startDateTime != ''">
                <![CDATA[
                    AND     DATE_FORMAT(API.CREATE_DATETIME, '%Y-%m-%d') >= #{startDateTime}
                ]]>
                </when>
                <when test="endDateTime != null and endDateTime != ''">
                <![CDATA[
                    AND     DATE_FORMAT(API.CREATE_DATETIME, '%Y-%m-%d') <= #{endDateTime}
                ]]>
                </when>
            </choose>
        </if>
        <if test="periodTypeCode != null and periodTypeCode == 'UPDATE'">
            <choose>
                <when test="(startDateTime != null and startDateTime != '') and (endDateTime != null and endDateTime != '')">
                    AND     DATE_FORMAT(API.MODIFY_DATETIME, '%Y-%m-%d') BETWEEN #{startDateTime} AND #{endDateTime}
                </when>
                <when test="startDateTime != null and startDateTime != ''">
                <![CDATA[
                    AND     DATE_FORMAT(API.MODIFY_DATETIME, '%Y-%m-%d') >= #{startDateTime}
                ]]>
                </when>
                <when test="endDateTime != null and endDateTime != ''">
                <![CDATA[
                    AND     DATE_FORMAT(API.MODIFY_DATETIME, '%Y-%m-%d') <= #{endDateTime}
                ]]>
                </when>
            </choose>
        </if>
        <if test="httpMethodCodeList != null and httpMethodCodeList.size > 0">
            AND     API.HTTP_METHOD_CD IN
            <foreach item="item" index="index" collection="httpMethodCodeList" open="(" separator="," close=")">
                #{item}
            </foreach>
        </if>
        <if test="apiVersion != null and apiVersion != ''">
            AND     API.API_VER = #{apiVersion}
        </if>
        WHERE 1 = 1
        <if test="serviceNumber != null">
            AND     A.SVC_NUM = #{serviceNumber}
        </if>
        ORDER BY A.CRITERIA_API_GRP_LEVEL, A.OPPONENT_API_GRP_LEVEL, A.OPPONENT_SORT_NUM, API.API_NUM
    </script>
    """)
    List<Api> selectApiList(ApiSearchRequest apiSearchRequest);

    @Select("""
        SELECT    COMP.SVC_COMP_CD AS apiCompCode
                , COMP.SVC_COMP_DATA AS apiCompData
                , COMP.SVC_COMP_DELETE_YN AS deleteYn
        FROM    MC_SVC SVC,
                MC_SVC_COMP COMP
        WHERE   COMP.SVC_NUM = #{serviceNumber}
        AND     SVC.SVC_NUM = COMP.SVC_NUM
        AND     SVC.SVC_COMP_USE_YN = 'Y'
        AND     COMP.SVC_COMP_DELETE_YN = 'N'
    """)
    List<ApiComp> selectServiceBaseCompList(@Param("serviceNumber") long serviceNumber);

    @Select("""
        SELECT  SVC.API_SECTION_CD AS apiSectionCode
               ,SVC.NB_BASE_URL AS nbBaseUrl
               ,SVC.SB_BASE_URL AS sbBaseUrl
               ,SVC.SB_BASE_TEST_URL AS sbApiTestUrl
               ,SVC.SVC_COMP_USE_YN AS serviceCompUseYn
               ,SVC.SVC_CONTEXT AS serviceContext
        FROM    MC_SVC SVC
        WHERE   SVC.SVC_NUM = #{serviceNumber}
    """)
    ApiGeneralInfo selectServiceBaseInfo(@Param("serviceNumber") long serviceNumber);

    @Insert("""
        INSERT INTO MC_API (
            ORIGIN_API_NUM,
            API_GRP_NUM,
            ADAPTOR_NUM,
            SITE_CD,
            API_NM,
            API_DESC,
            API_SECTION_CD,
            API_VER,
            HTTP_METHOD_CD,
            NB_API_URL,
            API_STATE_CD,
            SVC_BEGIN_DATETIME,
            SVC_END_DATETIME,
            API_USE_YN,
            API_DELETE_YN,
			MULTIPART_YN,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            0,
            #{apiGroupNumber},
            #{adaptorNumber},
            #{siteCode},
            #{apiName},
            #{apiDesc},
            #{apiSectionCode},
            #{apiVersion},
            #{httpMethodCode},
            #{nbBaseUrl},
            #{apiStateCode},
            NOW(),
            NOW(),
            #{apiUseYn},
            'N',
			#{multipartYn},
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
    """)
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="apiNumber", before=false, resultType=Long.class)
    int insertApi(Api api);

    @Insert("""
        INSERT INTO MC_API_COMP (
            API_NUM,
            API_COMP_CD,
            API_COMP_DATA,
            API_COMP_DELETE_YN,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{apiNumber},
            #{apiCompCode},
            #{apiCompData},
            'N',
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
    """)
    int insertApiComp(ApiComp apiComp);

    @Update("""
    <script>
        UPDATE MC_API
        SET
        <if test="apiGroupNumber != null and apiGroupNumber != 0">
          API_GRP_NUM = #{apiGroupNumber},
        </if>
        <if test="adaptorNumber != null and adaptorNumber != 0">
          ADAPTOR_NUM = #{adaptorNumber},
        </if>
        <if test="siteCode != null and siteCode != ''">
          SITE_CD = #{siteCode},
        </if>
        <if test="apiName != null and apiName != ''">
          API_NM = #{apiName},
        </if>
        <if test="apiDesc != null and apiDesc != ''">
          API_DESC = #{apiDesc},
        </if>
        <if test="apiSectionCode != null and apiSectionCode != ''">
          API_SECTION_CD = #{apiSectionCode},
        </if>
        <if test="apiVersion != null and apiVersion != ''">
          API_VER = #{apiVersion},
        </if>
        <if test="httpMethodCode != null and httpMethodCode != ''">
          HTTP_METHOD_CD = #{httpMethodCode},
        </if>
		<if test="multipartYn != null and multipartYn != ''">
          MULTIPART_YN = #{multipartYn},
        </if>
        <if test="nbBaseUrl != null and nbBaseUrl != ''">
          NB_API_URL = #{nbBaseUrl},
        </if>
        <if test="apiStateCode != null and apiStateCode != ''">
          API_STATE_CD = #{apiStateCode},
        </if>
        <if test="apiUseYn != null and apiUseYn != ''">
          API_USE_YN = #{apiUseYn},
        </if>
        <if test="apiDeleteYn != null and apiDeleteYn != ''">
          API_DELETE_YN = #{apiDeleteYn},
        </if>
        <if test="originApiNumber != null and originApiNumber != 0">
           ORIGIN_API_NUM = #{originApiNumber},
        </if>
          MODIFY_DATETIME = NOW(),
          MODIFY_ID = #{modifyId}
        WHERE API_NUM = #{apiNumber}
    </script>
    """)
    int updateApi(Api api);

    @Update("""
    <script>
        UPDATE MC_API_COMP
        SET
            <if test="apiCompData != null and apiCompData != ''">
                API_COMP_DATA = #{apiCompData},
            </if>
            <if test="deleteYn != null and deleteYn != ''">
                API_COMP_DELETE_YN = #{deleteYn},
            </if>
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   API_NUM = #{apiNumber}
        AND     API_COMP_CD = #{apiCompCode}
    </script>
    """)
    int updateApiComp(ApiComp apiComp);

    @Insert("""
        INSERT INTO MC_HIST_MGMT
        (HIST_MGMT_CD, HIST_MGMT_MEMO, CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID)
        VALUES ('MC_HIST_MGMT_01', '', NOW(), #{createId}, NOW(), #{createId})
    """)
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="historyNumber", before=false, resultType=Long.class)
    int insertApiHistManagement(HistoryManagement historyManagement);

    @Insert("""
        INSERT INTO MC_API_HIST
        (
            API_NUM,
            ORIGIN_API_NUM,
            HIST_END_DATETIME,
            HIST_BEGIN_DATETIME,
            API_GRP_NUM,
            ADAPTOR_NUM,
            SITE_CD,
            API_NM,
            API_DESC,
            API_SECTION_CD,
            API_VER,
            HTTP_METHOD_CD,
            NB_API_URL,
            API_STATE_CD,
            SVC_BEGIN_DATETIME,
            SVC_END_DATETIME,
            API_USE_YN,
            API_DELETE_YN,
			MULTIPART_YN,
            HIST_MGMT_NUM,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{apiNumber},
            #{originApiNumber},
            STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f'),
            NOW(6),
            #{apiGroupNumber},
            #{adaptorNumber},
            #{siteCode},
            #{apiName},
            #{apiDesc},
            #{apiSectionCode},
            #{apiVersion},
            #{httpMethodCode},
            #{nbBaseUrl},
            #{apiStateCode},
            NOW(),
            STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s'),
            #{apiUseYn},
            #{apiDeleteYn},
			#{multipartYn},
            #{historyNumber},
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
    """)
    int insertApiHist(Api api);

    @Update("""
        UPDATE  MC_API_HIST
        SET     HIST_END_DATETIME = NOW(6),
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   API_NUM = #{apiNumber}
        AND     HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    """)
    int updateApiHist(Api api);

    @Insert("""
        INSERT INTO MC_API_COMP_HIST (
            API_NUM,
            API_COMP_CD,
            HIST_END_DATETIME,
            HIST_BEGIN_DATETIME,
            API_COMP_DATA,
            API_COMP_DELETE_YN,
            HIST_MGMT_NUM,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{apiNumber},
            #{apiCompCode},
            STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f'),
            NOW(6),
            #{apiCompData},
            #{deleteYn},
            #{historyNumber},
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
    """)
    int insertApiCompHist(ApiComp apiComp);

    @Update("""
        UPDATE  MC_API_COMP_HIST
        SET     HIST_END_DATETIME = NOW(6),
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   API_NUM = #{apiNumber}
        AND     HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    """)
    int updateApiCompHist(ApiComp apiComp);

    @Select("""
        SELECT
                    API.API_NUM AS apiNumber
                  , API.ORIGIN_API_NUM AS originApiNumber
                  , API.API_GRP_NUM AS apiGroupNumber
                  , A.API_GRP_NM AS apiGroupName
                  , A.OPPONENT_API_GRP_LEVEL AS apiGroupLevel
                  , API.ADAPTOR_NUM AS adaptorNumber
                  , (SELECT MA.ADAPTOR_BEAN_ID FROM MC_ADAPTOR MA WHERE MA.ADAPTOR_NUM = API.ADAPTOR_NUM ) AS adaptorBeanId
                  , API.SITE_CD AS siteCode
                  , f_code_name(API.SITE_CD) AS siteCodeName
                  , API.API_NM AS apiName
                  , API.API_DESC AS apiDesc
                  , API.API_SECTION_CD AS apiSectionCode
                  , f_code_name(API.API_SECTION_CD) AS apiSectionCodeName
                  , API.API_VER AS apiVersion
                  , API.HTTP_METHOD_CD AS httpMethodCode
                  , f_code_name(API.HTTP_METHOD_CD) AS httpMethodCodeName
                  , API.NB_API_URL AS nbBaseUrl
                  , API.API_STATE_CD AS apiStateCode
                  , f_code_name(API.API_STATE_CD) AS apiStateCodeName
                  , API.API_USE_YN AS apiUseYn
                  , API.API_DELETE_YN AS apiDeleteYn
				  , API.MULTIPART_YN AS multipartYn
                  , DATE_FORMAT(API.CREATE_DATETIME, '%Y-%m-%d %H:%i:%s') AS createDateTime
                  , API.CREATE_ID AS createId
                  , DATE_FORMAT(API.MODIFY_DATETIME, '%Y-%m-%d %H:%i:%s') AS modifyDateTime
                  , API.MODIFY_ID AS modifyId
        FROM      MC_API API
        INNER JOIN
                    ( SELECT    IFNULL(GRP.API_GRP_NUM, 0) AS API_GRP_NUM
                              , GRP.API_GRP_NM AS API_GRP_NM
                              , REL.OPPONENT_API_GRP_LEVEL AS OPPONENT_API_GRP_LEVEL
                      FROM      MC_API_GRP GRP,
                                MC_API_GRP_RELATION REL
                      WHERE     GRP.API_GRP_NUM = REL.OPPONENT_API_GRP_NUM
                      AND       GRP.API_GRP_USE_YN = 'Y'
                      AND       GRP.API_GRP_DELETE_YN = 'N'
                      AND       REL.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                    ) A
        ON        A.API_GRP_NUM = API.API_GRP_NUM
        WHERE     API.API_NUM = #{apiNumber}
    """)
    Api selectApi(@Param("apiNumber") long apiNumber);

    @Select("""
        SELECT  COMP.API_COMP_CD AS apiCompCode
               ,COMP.API_COMP_DATA AS apiCompData
               ,COMP.API_COMP_DELETE_YN AS deleteYn
        FROM    MC_API_COMP COMP
        WHERE   COMP.API_NUM = #{apiNumber}
    """)
    List<ApiComp> selectApiCompList(@Param("apiNumber") long apiNumber);

    @Select("""
    <script>
        SELECT    IFNULL(GRP.API_GRP_NUM, 0) AS apiGroupNumber
                , GRP.API_GRP_NM AS apiGroupName
                , GRP.API_GRP_CONTEXT AS apiGroupContext
        FROM      MC_API_GRP GRP,
                  MC_API_GRP_RELATION REL
        WHERE     GRP.API_GRP_NUM = REL.OPPONENT_API_GRP_NUM
        AND       GRP.API_GRP_USE_YN = 'Y'
        AND       GRP.API_GRP_DELETE_YN = 'N'
        AND       REL.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
        AND       GRP.SVC_NUM = #{serviceNumber}
        AND       REL.OPPONENT_API_GRP_LEVEL = #{apiGroupLevel}
        <if test="apiGroupNumber != null and apiGroupNumber != 0">
            AND       REL.CRITERIA_API_GRP_NUM = #{apiGroupNumber}
        </if>
        ORDER BY  REL.OPPONENT_SORT_NUM
    </script>
    """)
    List<Api> selectGroupList(ApiSearchRequest apiSearchRequest);

    @Select("""
        SELECT     GRP.API_GRP_NUM AS apiGroupNumber
                  ,GRP.API_GRP_NM AS apiGroupName
        FROM      MC_API_GRP GRP,
                  MC_API_GRP_RELATION REL
        WHERE     REL.OPPONENT_API_GRP_NUM = #{apiGroupNumber}
        AND       REL.CRITERIA_API_GRP_NUM = GRP.API_GRP_NUM
        AND       REL.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')

    """)
    Api selectFirstGroupInfo(@Param("apiGroupNumber") long apiGroupNumber);

    @Select("""
        SELECT      API.API_VER AS apiVersion
                   ,API.API_NUM AS apiNumber
        FROM        MC_API API
        WHERE       API.ORIGIN_API_NUM = #{apiNumber}
        AND         API.API_DELETE_YN = 'N'
        ORDER BY    API.API_NUM
    """)
    List<Api> selectApiVersionList(@Param("apiNumber") long apiNumber);

    @Select("""
        SELECT  API.API_VER AS apiVersion
        FROM    MC_API API,
                MC_API_GRP GRP
        WHERE   GRP.SVC_NUM = #{serviceNumber}
        AND     GRP.API_GRP_NUM = API.API_GRP_NUM
        GROUP BY API.API_VER
        ORDER BY API.API_NUM DESC
    """)
    List<String> selectApiVersionListForService(@Param("serviceNumber") long serviceNumber);

    @Select("""
        SELECT  API.API_NUM AS apiNumber
        FROM    MC_API API
        WHERE   API.NB_API_URL = #{nbBaseUrl}
        AND     API.HTTP_METHOD_CD = #{httpMethodCode}
        AND     API.API_VER = #{apiVersion}
    """)
    List<Api> selectApiNumber(@Param("nbBaseUrl") String nbBaseUrl, @Param("httpMethodCode") String httpMethodCode, @Param("apiVersion") String apiVersion);

    @Select("""
        SELECT       DATE_FORMAT(MAH.HIST_BEGIN_DATETIME, '%Y-%m-%d %H:%i:%s') AS createDateTime
                    ,MAH.CREATE_ID AS createId
                    ,MAH.API_STATE_CD AS apiStateCode
                    ,MAH.HIST_MGMT_NUM AS historyNumber
        FROM        MC_API_HIST MAH
        WHERE       MAH.API_NUM = #{apiNumber}
        ORDER BY    MAH.CREATE_DATETIME DESC
    """)
    List<Api> selectApiHistoryList(@Param("apiNumber") long apiNumber);

    @Select("""
        SELECT
                    API.API_NUM AS apiNumber
                  , API.ORIGIN_API_NUM AS originApiNumber
                  , API.API_GRP_NUM AS apiGroupNumber
                  , A.API_GRP_NM AS apiGroupName
                  , A.OPPONENT_API_GRP_LEVEL AS apiGroupLevel
                  , API.ADAPTOR_NUM AS adaptorNumber
                  , (SELECT MA.ADAPTOR_BEAN_ID FROM MC_ADAPTOR MA WHERE MA.ADAPTOR_NUM = API.ADAPTOR_NUM ) AS adaptorBeanId
                  , API.SITE_CD AS siteCode
                  , f_code_name(API.SITE_CD) AS siteCodeName
                  , API.API_NM AS apiName
                  , API.API_DESC AS apiDesc
                  , API.API_SECTION_CD AS apiSectionCode
                  , f_code_name(API.API_SECTION_CD) AS apiSectionCodeName
                  , API.API_VER AS apiVersion
                  , API.HTTP_METHOD_CD AS httpMethodCode
                  , f_code_name(API.HTTP_METHOD_CD) AS httpMethodCodeName
                  , API.NB_API_URL AS nbBaseUrl
                  , API.API_STATE_CD AS apiStateCode
                  , f_code_name(API.API_STATE_CD) AS apiStateCodeName
                  , API.API_USE_YN AS apiUseYn
                  , API.API_DELETE_YN AS apiDeleteYn
                  , DATE_FORMAT(API.CREATE_DATETIME, '%Y-%m-%d %H:%i:%s') AS createDateTime
                  , API.CREATE_ID AS createId
                  , DATE_FORMAT(API.MODIFY_DATETIME, '%Y-%m-%d %H:%i:%s') AS modifyDateTime
                  , API.MODIFY_ID AS modifyId
        FROM      MC_API_HIST API
        INNER JOIN
                    ( SELECT    IFNULL(GRP.API_GRP_NUM, 0) AS API_GRP_NUM
                              , GRP.API_GRP_NM AS API_GRP_NM
                              , REL.OPPONENT_API_GRP_LEVEL AS OPPONENT_API_GRP_LEVEL
                      FROM      MC_API_GRP GRP,
                                MC_API_GRP_RELATION REL
                      WHERE     GRP.API_GRP_NUM = REL.OPPONENT_API_GRP_NUM
                      AND       GRP.API_GRP_USE_YN = 'Y'
                      AND       GRP.API_GRP_DELETE_YN = 'N'
                      AND       REL.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                    ) A
        ON        A.API_GRP_NUM = API.API_GRP_NUM
        WHERE     API.API_NUM = #{apiNumber}
        AND       API.HIST_MGMT_NUM = #{historyNumber}
    """)
    Api selectApiHistory(@Param("apiNumber") long apiNumber, @Param("historyNumber") long historyNumber);

    @Select("""
        SELECT  COMP.API_COMP_CD AS apiCompCode
               ,COMP.API_COMP_DATA AS apiCompData
               ,COMP.API_COMP_DELETE_YN AS deleteYn
        FROM    MC_API_COMP_HIST COMP
        WHERE   COMP.API_NUM = #{apiNumber}
        AND     COMP.HIST_MGMT_NUM = #{historyNumber}
    """)
    List<ApiComp> selectApiCompHistoryList(@Param("apiNumber") long apiNumber, @Param("historyNumber") long historyNumber);

    @Select("""
        SELECT
                    API.API_NUM AS apiNumber
                  , API.API_GRP_NUM AS apiGroupNumber
                  , IF (A.OPPONENT_API_GRP_LEVEL > 1,
                    (SELECT     GRP.API_GRP_NUM AS groupNumber
                      FROM      MC_API_GRP GRP,
                                MC_API_GRP_RELATION REL
                      WHERE     REL.OPPONENT_API_GRP_NUM = A.API_GRP_NUM
                      AND       REL.CRITERIA_API_GRP_NUM = GRP.API_GRP_NUM
                      AND       REL.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')), '') AS firstApiGroupNumber
                  , A.API_GRP_NM AS apiGroupName
                  , A.OPPONENT_API_GRP_LEVEL AS apiGroupLevel
                  , API.ADAPTOR_NUM AS adaptorNumber
                  , (SELECT MA.ADAPTOR_BEAN_ID FROM MC_ADAPTOR MA WHERE MA.ADAPTOR_NUM = API.ADAPTOR_NUM ) AS adaptorBeanId
                  , API.SITE_CD AS siteCode
                  , f_code_name(API.SITE_CD) AS siteCodeName
                  , API.API_NM AS apiName
                  , API.API_DESC AS apiDesc
                  , API.API_SECTION_CD AS apiSectionCode
                  , f_code_name(API.API_SECTION_CD) AS apiSectionCodeName
                  , API.API_VER AS apiVersion
                  , API.HTTP_METHOD_CD AS httpMethodCode
                  , f_code_name(API.HTTP_METHOD_CD) AS httpMethodCodeName
                  , API.NB_API_URL AS nbBaseUrl
                  , API.API_STATE_CD AS apiStateCode
                  , f_code_name(API.API_STATE_CD) AS apiStateCodeName
                  , API.API_USE_YN AS apiUseYn
                  , API.API_DELETE_YN AS apiDeleteYn
                  , DATE_FORMAT(API.CREATE_DATETIME, '%Y-%m-%d %H:%i:%s') AS createDateTime
                  , API.CREATE_ID AS createId
                  , DATE_FORMAT(API.MODIFY_DATETIME, '%Y-%m-%d %H:%i:%s') AS modifyDateTime
                  , API.MODIFY_ID AS modifyId
                  , (SELECT GEN.API_COMP_DATA FROM MC_API_COMP GEN WHERE GEN.API_NUM = API.API_NUM AND GEN.API_COMP_CD = 'MC_API_COMP_01') AS generalData
                  , (SELECT REQ.API_COMP_DATA FROM MC_API_COMP REQ WHERE REQ.API_NUM = API.API_NUM AND REQ.API_COMP_CD = 'MC_API_COMP_02') AS requestData
                  , (SELECT RES.API_COMP_DATA FROM MC_API_COMP RES WHERE RES.API_NUM = API.API_NUM AND RES.API_COMP_CD = 'MC_API_COMP_03') AS responseData
        FROM      MC_API API
        INNER JOIN
                    ( SELECT    IFNULL(GRP.API_GRP_NUM, 0) AS API_GRP_NUM
                              , GRP.API_GRP_NM AS API_GRP_NM
                              , REL.OPPONENT_API_GRP_LEVEL AS OPPONENT_API_GRP_LEVEL
                              , GRP.SVC_NUM AS SVC_NUM
                      FROM      MC_API_GRP GRP,
                                MC_API_GRP_RELATION REL
                      WHERE     GRP.API_GRP_NUM = REL.OPPONENT_API_GRP_NUM
                      AND       GRP.API_GRP_USE_YN = 'Y'
                      AND       GRP.API_GRP_DELETE_YN = 'N'
                      AND       REL.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                    ) A
        ON        A.API_GRP_NUM = API.API_GRP_NUM
        AND       API.API_DELETE_YN = 'N'
    """)
    List<Api> selectAllApiList();

    @Select("""
    <script>
        SELECT   API.API_NM AS apiName
               , SVC.SVC_NM AS serviceName
          FROM MC_API API,
               MC_API_GRP GRP,
               MC_SVC SVC
         WHERE API.API_USE_YN = 'Y'
           AND API.API_DELETE_YN = 'N'
           <![CDATA[
           AND (DATE_FORMAT(DATE_ADD(NOW(), interval-1 week), '%Y-%m-%d') <= DATE_FORMAT(API.CREATE_DATETIME, '%Y-%m-%d'))
           ]]>
           AND API.API_GRP_NUM = GRP.API_GRP_NUM
           AND GRP.SVC_NUM = SVC.SVC_NUM
         ORDER BY API.CREATE_DATETIME DESC
         LIMIT 4
    </script>
    """)
    List<ApiDashBoard> selectApiListForDashBoard();
}