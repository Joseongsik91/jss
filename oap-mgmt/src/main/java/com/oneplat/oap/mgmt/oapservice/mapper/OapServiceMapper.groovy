package com.oneplat.oap.mgmt.oapservice.mapper

import com.oneplat.oap.core.model.SearchRequest
import com.oneplat.oap.mgmt.oapservice.model.OapService
import com.oneplat.oap.mgmt.oapservice.model.OapServiceCompose
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectKey
import org.apache.ibatis.annotations.Update

/**
 * @author lee
 * @date 2016-12-01
 */
@Mapper
public interface OapServiceMapper {

    @Results(id="serviceResult", value = [
            @Result(property = "serviceNumber", column = "SVC_NUM"),
            @Result(property = "serviceName", column = "SVC_NM"),
            @Result(property = "siteCode", column = "SITE_CD"),
            @Result(property = "serviceContext", column = "SVC_CONTEXT"),
            @Result(property = "serviceSectionCode", column = "SVC_SECTION_CD"),
            @Result(property = "sandboxUseYn", column = "SANDBOX_USE_YN"),
            @Result(property = "serviceApprovalYn", column = "SVC_APPROVAL_YN"),
            @Result(property = "slaUseYn", column = "SLA_USE_YN"),
            @Result(property = "capacityUseYn", column = "CAPACITY_USE_YN"),
            @Result(property = "serviceDescription", column = "SVC_DESC"),
            @Result(property = "iconFileChannel", column = "ICON_FILE_CHANNEL"),
            @Result(property = "serviceRegisterDate", column = "SVC_REGISTER_DATE"),
            @Result(property = "serviceUseYn", column = "SVC_USE_YN"),
            @Result(property = "serviceComposeUseYn", column = "SVC_COMP_USE_YN"),
            @Result(property = "apiSectionCode", column = "API_SECTION_CD"),
            @Result(property = "northboundBaseUrl", column = "NB_BASE_URL"),
            @Result(property = "southboundBaseUrl", column = "SB_BASE_URL"),
            @Result(property = "southboundBaseTestUrl", column = "SB_BASE_TEST_URL"),
            @Result(property = "createId", column = "CREATE_ID"),
            @Result(property = "createDateTime", column = "CREATE_DATETIME"),
            @Result(property = "modifyId", column = "MODIFY_ID"),
            @Result(property = "modifyDateTime", column = "MODIFY_DATETIME"),
            @Result(property = "apiCount", column = "API_CNT"),
            @Result(property = "newIconUseYn", column = "NEW_ICON_USE_YN"),
            @Result(property = "scopeUseYn", column = "SCOPE_USE_YN")
            ])
    @Select("""<script>
            SELECT
                   MC.SVC_NUM
                 , MC.SVC_NM
                 , MC.SVC_USE_YN
                 , MC.SVC_REGISTER_DATE
                 , MC.ICON_FILE_CHANNEL
                 , IFNULL(SUB.API_CNT,0) AS API_CNT
                 , MC.SITE_CD
                 , MC.SVC_SECTION_CD
                 , MC.SLA_USE_YN
                 , MC.CAPACITY_USE_YN
            FROM MC_SVC MC
            LEFT JOIN MC_SCOPE MS ON MS.SVC_NUM = MC.SVC_NUM
            LEFT JOIN MC_SCOPE_RELATION MSR ON MSR.OPPONENT_SCOPE_NUM = MS.SCOPE_NUM
            LEFT JOIN
                 (SELECT MAG.SVC_NUM, COUNT(MA.API_NUM) AS API_CNT
                    FROM MC_API_GRP MAG LEFT JOIN MC_API MA ON MAG.API_GRP_NUM = MA.API_GRP_NUM AND MA.API_USE_YN = 'Y' AND MA.API_DELETE_YN = 'N'
                   WHERE MAG.API_GRP_DELETE_YN = 'N'
                     AND MAG.SVC_NUM > 0
                   GROUP BY MAG.SVC_NUM) SUB ON MC.SVC_NUM = SUB.SVC_NUM ,
                 MC_OPR MO
           WHERE MC.SVC_DELETE_YN = 'N'
             AND MC.CREATE_ID = MO.LOGIN_ID
             AND MSR.CRITERIA_SCOPE_NUM = 0
			 AND MSR.END_DATETIME > NOW()
        <if test="query!=null">
                <if test="query.searchWord!=null and query.searchWord!=''">
                    <if test="query.searchWordTypeCode == 'SVC_NM'">
                        AND MC.SVC_NM LIKE  CONCAT('%', #{query.searchWord}, '%')
                    </if>
                    <if test="query.searchWordTypeCode == 'OPR_NM'">
                        AND MO.OPR_NM LIKE  CONCAT('%', #{query.searchWord}, '%')
                    </if>
               </if>
                <if test="query.siteCode!=null and query.siteCode!=''">
                        AND MC.SITE_CD = #{query.siteCode}
                </if>
                <if test="query.useYn!=null and query.useYn!=''">
                        AND MC.SVC_USE_YN = #{query.useYn}
                </if>
                <if test="query.serviceSectionCode!=null and query.serviceSectionCode!=''">
                        AND MC.SVC_SECTION_CD = #{query.serviceSectionCode}
                </if>
                <if test="query.slaUseYn!=null and query.slaUseYn!=''">
                        AND MC.SLA_USE_YN = #{query.slaUseYn}
                </if>
                <if test="query.capacityUseYn!=null and query.capacityUseYn!=''">
                        AND MC.CAPACITY_USE_YN = #{query.capacityUseYn}
                </if>
                <if test="query.scopeUseYn!=null and query.scopeUseYn!=''">
                        AND MS.SCOPE_USE_YN = #{query.scopeUseYn}
                </if>
                <if test="query.startDateTime!=null and query.startDateTime!=''
                            and query.endDateTime!=null and query.endDateTime!=''">
                        <if test="query.periodTypeCode == 'CREATE'">
                            AND DATE_FORMAT(MC.CREATE_DATETIME,'%Y-%m-%d') BETWEEN #{query.startDateTime } AND #{query.endDateTime }
                        </if>
                        <if test="query.periodTypeCode == 'UPDATE'">
                            AND DATE_FORMAT(MC.MODIFY_DATETIME,'%Y-%m-%d') BETWEEN #{query.startDateTime } AND #{query.endDateTime }
                        </if>
                </if>
                <if test="query.startDateTime!=null and query.startDateTime!='' and query.endDateTime==null">
                        <if test="query.periodTypeCode == 'CREATE'">
                        <![CDATA[   AND DATE_FORMAT(MC.CREATE_DATETIME,'%Y-%m-%d') >= #{query.startDateTime } ]]>
                        </if>
                        <if test="query.periodTypeCode == 'UPDATE'">
                        <![CDATA[   AND DATE_FORMAT(MC.MODIFY_DATETIME,'%Y-%m-%d') >= #{query.startDateTime } ]]>
                        </if>
                </if>
                <if test="query.endDateTime!=null and query.endDateTime!='' and query.startDateTime==null">
                        <if test="query.periodTypeCode == 'CREATE'">
                        <![CDATA[   AND DATE_FORMAT(MC.CREATE_DATETIME,'%Y-%m-%d') <= #{query.endDateTime } ]]>
                        </if>
                        <if test="query.periodTypeCode == 'UPDATE'">
                        <![CDATA[   AND DATE_FORMAT(MC.MODIFY_DATETIME,'%Y-%m-%d') <= #{query.endDateTime } ]]>
                        </if>
                </if>
        </if>
        ORDER BY MC.SVC_NUM DESC
        <if test="pageInfo!=null">
          LIMIT #{pageInfo.startRowNum}, #{pageInfo.size}
        </if>
        </script>""")
    public List<OapService> selectServiceList(SearchRequest searchRequest);

    @Select("""<script>
            SELECT
                   COUNT(*)
            FROM MC_SVC MC
                 LEFT JOIN MC_SCOPE MS ON MS.SVC_NUM = MC.SVC_NUM
                 LEFT JOIN MC_SCOPE_RELATION MSR ON MSR.OPPONENT_SCOPE_NUM = MS.SCOPE_NUM
                 ,MC_OPR MO
           WHERE MC.SVC_DELETE_YN = 'N'
             AND MC.CREATE_ID = MO.LOGIN_ID
             AND MSR.CRITERIA_SCOPE_NUM = 0
			 AND MSR.END_DATETIME > NOW()
        <if test="query!=null">
                <if test="query.searchWord!=null and query.searchWord!=''">
                    <if test="query.searchWordTypeCode == 'SVC_NM'">
                        AND MC.SVC_NM LIKE  CONCAT('%', #{query.searchWord}, '%')
                    </if>
                    <if test="query.searchWordTypeCode == 'OPR_NM'">
                        AND MO.OPR_NM LIKE  CONCAT('%', #{query.searchWord}, '%')
                    </if>
               </if>
                <if test="query.siteCode!=null and query.siteCode!=''">
                        AND MC.SITE_CD = #{query.siteCode}
                </if>
                <if test="query.useYn!=null and query.useYn!=''">
                        AND MC.SVC_USE_YN = #{query.useYn}
                </if>
                <if test="query.serviceSectionCode!=null and query.serviceSectionCode!=''">
                        AND MC.SVC_SECTION_CD = #{query.serviceSectionCode}
                </if>
                <if test="query.slaUseYn!=null and query.slaUseYn!=''">
                        AND MC.SLA_USE_YN = #{query.slaUseYn}
                </if>
                <if test="query.capacityUseYn!=null and query.capacityUseYn!=''">
                        AND MC.CAPACITY_USE_YN = #{query.capacityUseYn}
                </if>
                <if test="query.scopeUseYn!=null and query.scopeUseYn!=''">
                        AND MS.SCOPE_USE_YN = #{query.scopeUseYn}
                </if>
                <if test="query.startDateTime!=null and query.startDateTime!=''
                            and query.endDateTime!=null and query.endDateTime!=''">
                        <if test="query.periodTypeCode == 'CREATE'">
                            AND DATE_FORMAT(MC.CREATE_DATETIME,'%Y%m%d') BETWEEN #{query.startDateTime } AND #{query.endDateTime }
                        </if>
                        <if test="query.periodTypeCode == 'UPDATE'">
                            AND DATE_FORMAT(MC.MODIFY_DATETIME,'%Y%m%d') BETWEEN #{query.startDateTime } AND #{query.endDateTime }
                        </if>
                </if>
                <if test="query.startDateTime!=null and query.startDateTime!='' and query.endDateTime==null">
                        <if test="query.periodTypeCode == 'CREATE'">
                        <![CDATA[   AND DATE_FORMAT(MC.CREATE_DATETIME,'%Y-%m-%d') >= #{query.startDateTime } ]]>
                        </if>
                        <if test="query.periodTypeCode == 'UPDATE'">
                        <![CDATA[   AND DATE_FORMAT(MC.MODIFY_DATETIME,'%Y-%m-%d') >= #{query.startDateTime } ]]>
                        </if>
                </if>
                <if test="query.endDateTime!=null and query.endDateTime!='' and query.startDateTime==null">
                        <if test="query.periodTypeCode == 'CREATE'">
                        <![CDATA[   AND DATE_FORMAT(MC.MODIFY_DATETIME,'%Y-%m-%d') <= #{query.endDateTime } ]]>
                        </if>
                        <if test="query.periodTypeCode == 'UPDATE'">
                        <![CDATA[   AND DATE_FORMAT(MC.MODIFY_DATETIME,'%Y-%m-%d') <= #{query.endDateTime } ]]>
                        </if>
                </if>
        </if>
        </script>""")
    public int selectServiceListTotal(SearchRequest searchRequest);

    @ResultMap("serviceResult")
    @Select("""<script>
            SELECT
                   A.SVC_NUM
                 , A.SITE_CD
                 , A.SVC_NM
                 , A.SVC_CONTEXT
                 , A.SVC_SECTION_CD
                 , A.SANDBOX_USE_YN
                 , A.SVC_APPROVAL_YN
                 , A.SLA_USE_YN
                 , A.CAPACITY_USE_YN
                 , A.SVC_DESC
                 , A.ICON_FILE_CHANNEL
                 , A.SVC_REGISTER_DATE
                 , A.SVC_USE_YN
                 , A.SVC_COMP_USE_YN
                 , A.API_SECTION_CD
                 , A.NB_BASE_URL
                 , A.SB_BASE_URL
                 , A.SB_BASE_TEST_URL
                 , B.SCOPE_USE_YN
                 , A.CREATE_DATETIME
                 , A.CREATE_ID
                 , A.MODIFY_DATETIME
                 , A.MODIFY_ID
            FROM MC_SVC A
            LEFT JOIN MC_SCOPE B ON A.SVC_NUM = B.SVC_NUM
            LEFT JOIN MC_SCOPE_RELATION MSR ON MSR.OPPONENT_SCOPE_NUM = B.SCOPE_NUM
           WHERE
               A.SVC_NUM = #{serviceNumber}
               AND MSR.CRITERIA_SCOPE_NUM = 0
			   AND MSR.END_DATETIME > NOW()
        </script>""")
    public OapService selectService(Long serviceNumber);

    @Insert("""<script>
        INSERT INTO MC_SVC
                ( SITE_CD
                , SVC_NM
                , SVC_CONTEXT
                , SVC_SECTION_CD
                , SANDBOX_USE_YN
                , SVC_APPROVAL_YN
                , SLA_USE_YN
                , CAPACITY_USE_YN
                , SVC_DESC
                , ICON_FILE_CHANNEL
                , INSIDE_SORT_NUM
                , OUTSIDE_SORT_NUM
                , SVC_REGISTER_DATE
                , SVC_USE_YN
                , SVC_DELETE_YN
                , SVC_COMP_USE_YN
        <if test="serviceComposeUseYn!=null and serviceComposeUseYn">
                , API_SECTION_CD
                , NB_BASE_URL
                , SB_BASE_URL
                , SB_BASE_TEST_URL
        </if>
                , CREATE_DATETIME
                , CREATE_ID
                , MODIFY_DATETIME
                , MODIFY_ID )
        VALUES (
                  #{siteCode}
                , #{serviceName}
                , #{serviceContext}
                , #{serviceSectionCode}
                , 'N'
                , 'N'
                , #{slaUseYn}
                , #{capacityUseYn}
                , #{serviceDescription}
                , #{iconFileChannel}
                , 0
                , 0
                , NOW()
                , #{serviceUseYn}
                , 'N'
                , #{serviceComposeUseYn}
        <if test="serviceComposeUseYn!=null and serviceComposeUseYn">
                , #{apiSectionCode}
                , #{northboundBaseUrl}
                , #{southboundBaseUrl}
                , #{southboundBaseTestUrl}
        </if>
                , NOW()
                , #{createId}
                , NOW()
                , #{modifyId}
                )
    </script>""")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="serviceNumber", before=false, resultType=Long.class)
    public int insertService(OapService oapService);

    @Insert("""<script>
        INSERT INTO MC_SVC_HIST
                ( SVC_NUM
                , HIST_END_DATETIME
                , HIST_BEGIN_DATETIME
                , SITE_CD
                , SVC_NM
                , SVC_CONTEXT
                , SVC_SECTION_CD
                , SANDBOX_USE_YN
                , SVC_APPROVAL_YN
                , SLA_USE_YN
                , CAPACITY_USE_YN
                , SVC_DESC
                , ICON_FILE_CHANNEL
                , INSIDE_SORT_NUM
                , OUTSIDE_SORT_NUM
                , SVC_REGISTER_DATE
                , SVC_USE_YN
                , SVC_DELETE_YN
                , SVC_COMP_USE_YN
                , API_SECTION_CD
                , NB_BASE_URL
                , SB_BASE_URL
                , SB_BASE_TEST_URL
                , CREATE_DATETIME
                , CREATE_ID
                , MODIFY_DATETIME
                , MODIFY_ID )
        SELECT
                  SVC_NUM
                , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                , NOW(6)
                , SITE_CD
                , SVC_NM
                , SVC_CONTEXT
                , SVC_SECTION_CD
                , SANDBOX_USE_YN
                , SVC_APPROVAL_YN
                , SLA_USE_YN
                , CAPACITY_USE_YN
                , SVC_DESC
                , ICON_FILE_CHANNEL
                , INSIDE_SORT_NUM
                , OUTSIDE_SORT_NUM
                , SVC_REGISTER_DATE
                , SVC_USE_YN
                , SVC_DELETE_YN
                , SVC_COMP_USE_YN
                , API_SECTION_CD
                , NB_BASE_URL
                , SB_BASE_URL
                , SB_BASE_TEST_URL
                , CREATE_DATETIME
                , CREATE_ID
                , MODIFY_DATETIME
                , MODIFY_ID
         FROM MC_SVC
        WHERE SVC_NUM = #{serviceNumber}

    </script>""")
    public int insertServiceHistory(Long serviceNumber);

    @Insert("""<script>
        INSERT INTO MC_SVC_COMP
                  ( SVC_NUM
                  , SVC_COMP_CD
                  , SVC_COMP_DATA
                  , SVC_COMP_DELETE_YN
                  , CREATE_DATETIME
                  , CREATE_ID
                  , MODIFY_DATETIME
                  , MODIFY_ID )
            VALUES (
                    #{serviceNumber}
                  , #{serviceComposeCode}
                  , #{serviceComposeData}
                  , 'N'
                  , NOW()
                  , #{createId}
                  , NOW()
                  , #{modifyId}
                   )
    </script>""")
    public int insertServiceCompose(OapServiceCompose oapServiceCompose);

    @Insert("""<script>
        INSERT INTO MC_SVC_COMP_HIST
                  ( SVC_NUM
                  , SVC_COMP_CD
                  , HIST_END_DATETIME
                  , HIST_BEGIN_DATETIME
                  , SVC_COMP_DATA
                  , SVC_COMP_DELETE_YN
                  , CREATE_DATETIME
                  , CREATE_ID
                  , MODIFY_DATETIME
                  , MODIFY_ID )
            SELECT
                    SVC_NUM
                  , SVC_COMP_CD
                  , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                  , NOW(6)
                  , SVC_COMP_DATA
                  , SVC_COMP_DELETE_YN
                  , CREATE_DATETIME
                  , CREATE_ID
                  , MODIFY_DATETIME
                  , MODIFY_ID
              FROM MC_SVC_COMP
              WHERE SVC_NUM = #{serviceNumber}
    </script>""")
    public int insertServiceComposeHistory(Long serviceNumber);

    @Results(id="serviceComposeResult", value = [
            @Result(property = "serviceNumber", column = "SVC_NUM"),
            @Result(property = "serviceComposeCode", column = "SVC_COMP_CD"),
            @Result(property = "serviceComposeData", column = "SVC_COMP_DATA")
    ])
    @Select("""<script>
            SELECT
                   SVC_NUM
                 , SVC_COMP_CD
                 , SVC_COMP_DATA
            FROM MC_SVC_COMP
           WHERE SVC_NUM = #{serviceNumber}
             AND SVC_COMP_DELETE_YN = 'N'
        </script>""")
    public List<OapServiceCompose> selectServiceComposeList(Long serviceNumber);

    // 서비스 삭제 처리
    @Update("""<script>
        UPDATE MC_SVC
        SET
           SVC_DELETE_YN = 'Y'
         , MODIFY_DATETIME = NOW()
         , MODIFY_ID = #{modifyId}
       WHERE SVC_NUM = #{serviceNumber}
    </script>""")
    public int deleteService(OapService oapService);

    // 서비스 이력 테이블 종료
    @Update("""<script>
        UPDATE MC_SVC_HIST
        SET
           HIST_END_DATETIME = NOW(6)
        WHERE SVC_NUM = #{serviceNumber}
          AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
    public int updateServiceHistory(Long serviceNumber);

    // 서비스 구성 항목 등록 or 수정
    @Insert("""<script>
        INSERT INTO MC_SVC_COMP
                  ( SVC_NUM
                  , SVC_COMP_CD
                  , SVC_COMP_DATA
                  , SVC_COMP_DELETE_YN
                  , CREATE_DATETIME
                  , CREATE_ID
                  , MODIFY_DATETIME
                  , MODIFY_ID )
            VALUES (
                    #{serviceNumber}
                  , #{serviceComposeCode}
                  , #{serviceComposeData}
                  , 'N'
                  , NOW()
                  , #{createId}
                  , NOW()
                  , #{modifyId}
                   )
        ON DUPLICATE KEY UPDATE
                   SVC_COMP_DATA = #{serviceComposeData}
                 , SVC_COMP_DELETE_YN = 'N'
                 , MODIFY_DATETIME = NOW()
                 , MODIFY_ID = #{modifyId}
    </script>""")
    public int updateServiceCompose(OapServiceCompose oapServiceCompose);

    // 서비스 구성 항목 삭제 처리
    @Update("""<script>
        UPDATE MC_SVC_COMP
        SET
           SVC_COMP_DELETE_YN = 'Y'
         , MODIFY_DATETIME = NOW()
         , MODIFY_ID = #{modifyId}
       WHERE SVC_NUM = #{serviceNumber}
         AND SVC_COMP_CD = #{serviceComposeCode}
    </script>""")
    public int deleteServiceComposeCode(OapServiceCompose oapServiceCompose);

    // 서비스 구성 이력 테이블 종료
    @Update("""<script>
        UPDATE MC_SVC_COMP_HIST
        SET
           HIST_END_DATETIME = NOW(6)
        WHERE SVC_NUM = #{serviceNumber}
          AND SVC_COMP_CD = #{serviceComposeCode}
          AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
    public int updateServiceComposeCodeHistory(OapServiceCompose oapServiceCompose);

    @Insert("""<script>
        INSERT INTO MC_SVC_COMP_HIST
                  ( SVC_NUM
                  , SVC_COMP_CD
                  , HIST_END_DATETIME
                  , HIST_BEGIN_DATETIME
                  , SVC_COMP_DATA
                  , SVC_COMP_DELETE_YN
                  , CREATE_DATETIME
                  , CREATE_ID
                  , MODIFY_DATETIME
                  , MODIFY_ID )
            SELECT
                    SVC_NUM
                  , SVC_COMP_CD
                  , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                  , NOW(6)
                  , SVC_COMP_DATA
                  , SVC_COMP_DELETE_YN
                  , CREATE_DATETIME
                  , CREATE_ID
                  , MODIFY_DATETIME
                  , MODIFY_ID
              FROM MC_SVC_COMP
              WHERE SVC_NUM = #{serviceNumber}
                AND SVC_COMP_CD = #{serviceComposeCode}
    </script>""")
    public int insertServiceComposeCodeHistory(OapServiceCompose oapServiceCompose);

    @Update("""<script>
        UPDATE MC_SVC_COMP
        SET
           SVC_COMP_DELETE_YN = 'Y'
         , MODIFY_DATETIME = NOW()
         , MODIFY_ID = #{modifyId}
       WHERE SVC_NUM = #{serviceNumber}
    </script>""")
    public int deleteServiceCompose(OapServiceCompose oapServiceCompose);

    // 서비스 구성 이력 테이블 종료
    @Update("""<script>
        UPDATE MC_SVC_COMP_HIST
        SET
           HIST_END_DATETIME = NOW(6)
        WHERE SVC_NUM = #{serviceNumber}
          AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
    public int updateServiceComposeHistory(OapServiceCompose oapServiceCompose);

    // 서비스 수정
    @Update("""<script>
        UPDATE MC_SVC
          SET SITE_CD = #{siteCode}
            , SVC_NM = #{serviceName}
            , SVC_CONTEXT = #{serviceContext}
            , SVC_SECTION_CD = #{serviceSectionCode}
            , SLA_USE_YN = #{slaUseYn}
            , CAPACITY_USE_YN = #{capacityUseYn}
            , SVC_DESC = #{serviceDescription}
     <if test="iconFileChannel!=null and iconFileChannel!=''">
            , ICON_FILE_CHANNEL = #{iconFileChannel}
     </if>
            , SVC_USE_YN = #{serviceUseYn}
            , SVC_COMP_USE_YN = #{serviceComposeUseYn}
            , MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
     <if test="serviceComposeUseYn!=null and serviceComposeUseYn">
            , API_SECTION_CD = #{apiSectionCode}
            , NB_BASE_URL = #{northboundBaseUrl}
            , SB_BASE_URL = #{southboundBaseUrl}
            , SB_BASE_TEST_URL = #{southboundBaseTestUrl}
     </if>
            WHERE SVC_NUM = #{serviceNumber}
    </script>""")
    public int updateService(OapService oapService);

    @Results(id="serviceHistoryResult", value = [
            @Result(property = "historyBeginDateTime", column = "HIST_BEGIN_DATETIME"),
            @Result(property = "createDateTime", column = "CREATE_DATETIME"),
            @Result(property = "createId", column = "CREATE_ID"),
            @Result(property = "modifyDateTime", column = "MODIFY_DATETIME"),
            @Result(property = "modifyId", column = "MODIFY_ID")
    ])
    @Select("""<script>
            SELECT
                    HIST_BEGIN_DATETIME
                  , CREATE_DATETIME
                  , CREATE_ID
                  , MODIFY_DATETIME
                  , MODIFY_ID
            FROM MC_SVC_HIST
           WHERE SVC_NUM = #{serviceNumber}
        ORDER BY HIST_BEGIN_DATETIME DESC
        </script>""")
    public List<OapService> selectServiceHistoryList(Long serviceNumber);

    @Select("""<script>
            SELECT
                   COUNT(*)
            FROM MC_SVC
           WHERE SVC_DELETE_YN = 'N'
             AND SVC_CONTEXT = #{serviceContext}
        </script>""")
    public int selectServiceNameCount(String serviceContext);

    @Select("""<script>
            SELECT COUNT(MA.API_NUM) AS CNT
              FROM MC_API MA JOIN MC_API_GRP MAG ON MAG.API_GRP_NUM = MA.API_GRP_NUM AND MA.API_USE_YN = 'Y' AND MA.API_DELETE_YN = 'N'
             WHERE MAG.API_GRP_DELETE_YN = 'N'
               AND MAG.API_GRP_USE_YN = 'Y'
               AND MAG.SVC_NUM = #{serviceNumber}
        </script>""")
    public int selectServiceApiCount(Long serviceNumber);

    @Select("""<script>
            SELECT COUNT(DA.APP_NUM) AS CNT
              FROM DC_APP DA,
                   DC_APP_SVC DAS
             WHERE DA.APP_NUM = DAS.APP_NUM
               AND DA.APP_USE_YN = 'Y'
               AND DA.APP_DELETE_YN = 'N'
               AND DAS.SVC_NUM = #{serviceNumber}
               AND DAS.END_DATETIME > NOW()
        </script>""")
    public int selectServiceAppCount(Long serviceNumber);


    @ResultMap("serviceResult")
    /* 2020.03.10 김동균 주석의 쿼리가 원본이다
     *    SELECT
                     MC.SVC_NUM
                   , MC.SVC_NM
                   , MC.SVC_USE_YN
                   , MC.SVC_REGISTER_DATE
                   , MC.ICON_FILE_CHANNEL
                   , IFNULL(SUB.API_CNT,0) AS API_CNT
                   , MC.SITE_CD
                   , MC.SVC_SECTION_CD
                   , MC.SLA_USE_YN
                   , MC.CAPACITY_USE_YN
                   <![CDATA[
                   , IF((DATE_FORMAT(DATE_ADD(NOW(), interval-2 day), '%Y-%m-%d') <= DATE_FORMAT(MC.MODIFY_DATETIME, '%Y-%m-%d'))
                          OR (DATE_FORMAT(DATE_ADD(NOW(), interval-2 day), '%Y-%m-%d') <= DATE_FORMAT(SUB.CREATE_DATETIME, '%Y-%m-%d')), 'Y', 'N') AS NEW_ICON_USE_YN
                   ]]>
              FROM MC_SVC MC LEFT JOIN
                   (SELECT MAG.SVC_NUM, COUNT(MA.API_NUM) AS API_CNT, IFNULL(MAX(MA.CREATE_DATETIME), 0) AS CREATE_DATETIME
                      FROM MC_API_GRP MAG LEFT JOIN MC_API MA ON MAG.API_GRP_NUM = MA.API_GRP_NUM AND MA.API_USE_YN = 'Y' AND MA.API_DELETE_YN = 'N'
                     WHERE MAG.API_GRP_DELETE_YN = 'N'
                       AND MAG.SVC_NUM > 0
                     GROUP BY MAG.SVC_NUM) SUB ON MC.SVC_NUM = SUB.SVC_NUM
             WHERE MC.SVC_DELETE_YN = 'N'
               AND MC.SVC_USE_YN = 'Y'
        <if test="query!=null">
                <if test="query.siteCode!=null and query.siteCode!=''">
                        AND MC.SITE_CD = #{query.siteCode}
                </if>
        </if>
        ORDER BY MC.SVC_NUM DESC
     * */
    @Select("""<script>
            SELECT
                     MC.SVC_NUM
                   , MC.SVC_NM
                   , MC.SVC_USE_YN
                   , MC.SVC_REGISTER_DATE
                   , MC.ICON_FILE_CHANNEL
                   , IFNULL(SUB.API_CNT,0) AS API_CNT
                   , MC.SITE_CD
                   , MC.SVC_SECTION_CD
                   , MC.SLA_USE_YN
                   , MC.CAPACITY_USE_YN
                   <![CDATA[
                   , IF((DATE_FORMAT(DATE_ADD(NOW(), interval-3 day), '%Y-%m-%d') <= DATE_FORMAT(SUB.CREATE_DATETIME, '%Y-%m-%d')), 'Y', 'N') AS NEW_ICON_USE_YN
                   ]]>
              FROM MC_SVC MC LEFT JOIN
                   (SELECT MAG.SVC_NUM, COUNT(MA.API_NUM) AS API_CNT, IFNULL(MAX(MA.CREATE_DATETIME), 0) AS CREATE_DATETIME
                      FROM MC_API_GRP MAG LEFT JOIN MC_API MA ON MAG.API_GRP_NUM = MA.API_GRP_NUM AND MA.API_USE_YN = 'Y' AND MA.API_DELETE_YN = 'N'
                     WHERE MAG.API_GRP_DELETE_YN = 'N'
                       AND MAG.SVC_NUM > 0
                     GROUP BY MAG.SVC_NUM) SUB ON MC.SVC_NUM = SUB.SVC_NUM
             WHERE MC.SVC_DELETE_YN = 'N'
               AND MC.SVC_USE_YN = 'Y'
        <if test="query!=null">
                <if test="query.siteCode!=null and query.siteCode!=''">
                        AND MC.SITE_CD = #{query.siteCode}
                </if>
        </if>
        ORDER BY MC.SVC_NUM DESC
        <if test="pageInfo!=null">
        LIMIT 0, 15
        </if>
        </script>""")
    public List<OapService> selectServiceListForDashBoard(SearchRequest searchRequest);
}
