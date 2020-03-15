package com.oneplat.oap.mgmt.application.mapper

import com.oneplat.oap.core.model.SearchRequest
import com.oneplat.oap.mgmt.application.model.DashboardApplications;
import com.oneplat.oap.mgmt.application.model.DcApplication
import com.oneplat.oap.mgmt.application.model.DcApplicationAuthKey;
import com.oneplat.oap.mgmt.application.model.DcApplicationKey
import com.oneplat.oap.mgmt.application.model.DcApplicationScope;
import com.oneplat.oap.mgmt.application.model.DcApplicationService
import com.oneplat.oap.mgmt.application.model.DcApplicationSla
import com.oneplat.oap.mgmt.application.model.DcApplicationSlaRequest
import com.oneplat.oap.mgmt.application.model.DcDeveloper
import com.oneplat.oap.mgmt.application.model.DcHistoryManagement
import org.apache.ibatis.annotations.Delete

import java.util.List;

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Many
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectKey
import org.apache.ibatis.annotations.Update
import org.apache.ibatis.annotations.Param

/**
 * @author [seon], [oneplat]
 *
 */
@Mapper
public interface ApplicationMapper {

    @Insert("""<script>
        INSERT INTO DC_APP (
            DEVELOPER_NUM,
            SITE_CD,
            APP_NM,
            APP_DESC,
            APP_USE_YN,
            APP_DELETE_YN,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{developerNumber},
            #{siteCode},
            #{applicationName},
            #{applicationDescription},
            #{applicationUseYn},
            'N',
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
    </script>""")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="applicationNumber", before=false, resultType=Long.class)
    public void insertApplication(DcApplication application);
    

    @Update("""
        UPDATE  DC_APP
        SET     SITE_CD = #{siteCode},
                APP_NM = #{applicationName},
                APP_DESC = #{applicationDescription},
                APP_USE_YN = #{applicationUseYn},
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   APP_NUM = #{applicationNumber}
    """)
    public void updateApplication(DcApplication application);
    

    @Results(id="applicationListResult", value = [
        @Result(column = "APP_NUM",            property = "applicationNumber"),
        @Result(column = "APP_NM",             property = "applicationName"),
        @Result(column = "SITE_CD",            property = "siteCode"),
        @Result(column = "SVC_CNT",            property = "serviceCount"),
        @Result(column = "CREATE_DATETIME",    property = "createDatetime"),
        @Result(column = "MODIFY_DATETIME",    property = "modifyDatetime"),
        @Result(column = "APP_USE_YN",         property = "applicationUseYn")
    ])
    @Select("""
    <script>
        SELECT /* selectApplicationList | SeonIl Bae | 2016-12-05 */
               DA.APP_NUM
             , DA.APP_NM
             , DA.SITE_CD
             , (SELECT COUNT(DISTINCT S.SVC_NUM) AS CNT
                FROM   DC_APP_SVC S
                WHERE  S.END_DATETIME > NOW(6)
                AND    S.APP_NUM = DA.APP_NUM) AS SVC_CNT
             , DATE_FORMAT(DA.CREATE_DATETIME,'%Y-%m-%d %H:%i:%s') AS CREATE_DATETIME
             , DATE_FORMAT(DA.MODIFY_DATETIME,'%Y-%m-%d %H:%i:%s') AS MODIFY_DATETIME
             , DA.APP_USE_YN
        FROM   DC_APP DA INNER JOIN DC_DEVELOPER DD
               ON (DA.DEVELOPER_NUM = DD.DEVELOPER_NUM)
        WHERE  DA.APP_DELETE_YN = 'N'
        <if test="query != null">
          <if test="query.searchWord != null and query.searchWord != ''">
            <if test="query.searchWordTypeCode == 'APP_NM'">
            AND    DA.APP_NM LIKE CONCAT('%',#{query.searchWord},'%')
            </if>
            <if test="query.searchWordTypeCode == 'OPR_NM'">
            AND    DD.DEVELOPER_NM LIKE CONCAT('%',#{query.searchWord},'%')
            </if>
          </if>
          <if test="query.siteCode != null and query.siteCode != ''">
            AND    DA.SITE_CD = #{query.siteCode}
          </if>
          <if test="query.searchService != null and query.searchService != ''">
            AND EXISTS (
                SELECT 'x'
                FROM   DC_APP_SVC x
                WHERE  x.APP_NUM = DA.APP_NUM
                AND    x.SVC_NUM = #{query.searchService}
                AND    x.END_DATETIME > NOW(6)
            )
          </if>
          <if test="query.useYnCode != null and query.useYnCode != ''">
            AND    DA.APP_USE_YN = #{query.useYnCode}
          </if>
          <if test="(query.startDate != null and query.startDate != '') and (query.endDate != null and query.endDate != '')">
            <if test="query.periodTypeCode == 'CREATE'">
            AND    DA.CREATE_DATETIME BETWEEN #{query.startDate} AND DATE_ADD(DATE_ADD(#{query.endDate}, INTERVAL 1 DAY), INTERVAL -1 SECOND)
            </if>
            <if test="query.periodTypeCode == 'UPDATE'">
            AND    DA.MODIFY_DATETIME BETWEEN #{query.startDate} AND DATE_ADD(DATE_ADD(#{query.endDate}, INTERVAL 1 DAY), INTERVAL -1 SECOND)
            </if>
          </if>
          <if test="(query.startDate != null and query.startDate != '') and query.endDate == null">
            <if test="query.periodTypeCode == 'CREATE'">
            AND    DA.CREATE_DATETIME >= #{query.startDate}
            </if>
            <if test="query.periodTypeCode == 'UPDATE'">
            AND    DA.MODIFY_DATETIME >= #{query.startDate}
            </if>
          </if>
          <if test="query.startDate == null and (query.endDate != null and query.endDate != '')">
            <if test="query.periodTypeCode == 'CREATE'">
            AND    DA.CREATE_DATETIME &lt; DATE_ADD(DATE_ADD(#{query.endDate}, INTERVAL 1 DAY), INTERVAL -1 SECOND)
            </if>
            <if test="query.periodTypeCode == 'UPDATE'">
            AND    DA.MODIFY_DATETIME &lt; DATE_ADD(DATE_ADD(#{query.endDate}, INTERVAL 1 DAY), INTERVAL -1 SECOND)
            </if>
          </if>
        </if>
        ORDER BY DA.APP_NUM DESC
        <if test="pageInfo!=null">
          LIMIT #{pageInfo.startRowNum}, #{pageInfo.size}
        </if>
    </script>
    """)
    public List<DcApplication> selectApplicationList(SearchRequest searchRequest);
    
    @Select("""
    <script>
        SELECT /* selectApplicationListCount | SeonIl Bae | 2016-12-05 */
               COUNT(*) AS CNT
        FROM   DC_APP DA INNER JOIN DC_DEVELOPER DD
               ON (DA.DEVELOPER_NUM = DD.DEVELOPER_NUM)
        WHERE  DA.APP_DELETE_YN = 'N'
        <if test="query != null">
          <if test="query.searchWord != null and query.searchWord != ''">
            <if test="query.searchWordTypeCode == 'APP_NM'">
            AND    DA.APP_NM LIKE CONCAT('%',#{query.searchWord},'%')
            </if>
            <if test="query.searchWordTypeCode == 'OPR_NM'">
            AND    DD.DEVELOPER_NM LIKE CONCAT('%',#{query.searchWord},'%')
            </if>
          </if>
          <if test="query.siteCode != null and query.siteCode != ''">
            AND    DA.SITE_CD = #{query.siteCode}
          </if>
          <if test="query.searchService != null and query.searchService != ''">
            AND EXISTS (
                SELECT 'x'
                FROM   DC_APP_SVC x
                WHERE  x.APP_NUM = DA.APP_NUM
                AND    x.SVC_NUM = #{query.searchService}
                AND    x.END_DATETIME > NOW(6)
            )
          </if>
          <if test="query.useYnCode != null and query.useYnCode != ''">
            AND    DA.APP_USE_YN = #{query.useYnCode}
          </if>
          <if test="(query.startDate != null and query.startDate != '') and (query.endDate != null and query.endDate != '')">
            <if test="query.periodTypeCode == 'CREATE'">
            AND    DA.CREATE_DATETIME BETWEEN #{query.startDate} AND DATE_ADD(DATE_ADD(#{query.endDate}, INTERVAL 1 DAY), INTERVAL -1 SECOND)
            </if>
            <if test="query.periodTypeCode == 'UPDATE'">
            AND    DA.MODIFY_DATETIME BETWEEN #{query.startDate} AND DATE_ADD(DATE_ADD(#{query.endDate}, INTERVAL 1 DAY), INTERVAL -1 SECOND)
            </if>
          </if>
          <if test="(query.startDate != null and query.startDate != '') and query.endDate == null">
            <if test="query.periodTypeCode == 'CREATE'">
            AND    DA.CREATE_DATETIME >= #{query.startDate}
            </if>
            <if test="query.periodTypeCode == 'UPDATE'">
            AND    DA.MODIFY_DATETIME >= #{query.startDate}
            </if>
          </if>
          <if test="query.startDate == null and (query.endDate != null and query.endDate != '')">
            <if test="query.periodTypeCode == 'CREATE'">
            AND    DA.CREATE_DATETIME &lt; DATE_ADD(DATE_ADD(#{query.endDate}, INTERVAL 1 DAY), INTERVAL -1 SECOND)
            </if>
            <if test="query.periodTypeCode == 'UPDATE'">
            AND    DA.MODIFY_DATETIME &lt; DATE_ADD(DATE_ADD(#{query.endDate}, INTERVAL 1 DAY), INTERVAL -1 SECOND)
            </if>
          </if>
        </if>
    </script>
    """)
    public int selectApplicationListCount(SearchRequest searchRequest);
    

    @Results(id="applicationInfoResult", value = [
        @Result(column = "APP_NUM",            property = "applicationNumber"),
        @Result(column = "APP_NM",             property = "applicationName"),
        @Result(column = "SITE_CD",            property = "siteCode"),
        @Result(column = "SVC_INFO",           property = "serviceInfo"),
        @Result(column = "APP_DESC",           property = "applicationDescription"),
        @Result(column = "APP_USE_YN",         property = "applicationUseYn"),
        @Result(column = "CREATE_DATETIME",    property = "createDatetime"),
        @Result(column = "MODIFY_DATETIME",    property = "modifyDatetime"),
        @Result(column = "CREATE_ID",          property = "createId"),
        @Result(column = "MODIFY_ID",          property = "modifyId"),
        @Result(property = "applicationServiceList", column="APP_NUM", many=@Many(select = "selectApplicationServiceList"))
    ])
    @Select("""
    <script>
        SELECT /* selectApplicationInfo | SeonIl Bae | 2016-12-05 */
               DA.APP_NUM
             , DA.APP_NM
             , DA.SITE_CD
             , (
                    SELECT GROUP_CONCAT(DISTINCT CONCAT(S1.SVC_NUM,'|',S2.SVC_NM))
                    FROM   DC_APP_SVC S1 INNER JOIN MC_SVC S2
                        ON (S1.SVC_NUM = S2.SVC_NUM)
                    WHERE  S1.END_DATETIME > NOW(6)
                    AND    S1.APP_NUM = DA.APP_NUM
                    GROUP BY S1.APP_NUM
                ) AS SVC_INFO
             , DA.APP_DESC
             , DA.APP_USE_YN
             , DATE_FORMAT(DA.CREATE_DATETIME,'%Y-%m-%d %H:%i:%s') AS CREATE_DATETIME
             , DATE_FORMAT(DA.MODIFY_DATETIME,'%Y-%m-%d %H:%i:%s') AS MODIFY_DATETIME
             , DA.CREATE_ID
             , DA.MODIFY_ID
        FROM   DC_APP DA INNER JOIN DC_DEVELOPER DD
               ON (DA.DEVELOPER_NUM = DD.DEVELOPER_NUM)
        WHERE  DA.APP_DELETE_YN = 'N'
        AND    DA.APP_NUM = #{applicationNumber}
    </script>
    """)
    public DcApplication selectApplicationInfo(Long applicationNumber);

        
    @Insert("""<script>
       INSERT INTO DC_APP_KEY (
            APP_NUM,
            SVC_GRADE_CD,
            KEY_TYPE_CD,
            APP_KEY_SEQ,
            APP_KEY,
            KEY_END_DATETIME,
            KEY_TYPE_ATTR_VAL,
            TYPE_LIMIT_YN,
            KEY_DELETE_YN,
            HMAC_AUTH_TYPE_CD,
            MSG_ENCRYPTION_TYPE_CD,
            MSG_ENCRYPTION_KEY,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{applicationNumber},
            #{serviceGradeCode},
            #{keyTypeCode},
            #{applicationKeySequence},
            #{applicationKey},
            CONCAT(#{keyEndDatetime},' 23:59:59'),
            #{keyTypeAttributeValue},
            #{typeLimitYn},
            'N',
            #{hmacAuthTypeCode},
            #{msgEncryptionTypeCode},
            #{msgEncryptionKey},
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
    </script>""")
    @SelectKey(statement="SELECT IFNULL(MAX(APP_KEY_SEQ),0)+1 FROM DC_APP_KEY WHERE 1=1 AND KEY_TYPE_CD = #{keyTypeCode}",keyProperty="applicationKeySequence", before=true, resultType=Integer.class)
    public void insertApplicationKey(DcApplicationKey applicationKey);
    

    @Update("""<script>
        UPDATE  DC_APP_KEY
        SET     KEY_END_DATETIME = CONCAT(#{keyEndDatetime},' 23:59:59'),
                KEY_TYPE_ATTR_VAL = #{keyTypeAttributeValue},
                TYPE_LIMIT_YN = #{typeLimitYn},
                HMAC_AUTH_TYPE_CD = #{hmacAuthTypeCode},
                MSG_ENCRYPTION_TYPE_CD = #{msgEncryptionTypeCode},
                MSG_ENCRYPTION_KEY = #{msgEncryptionKey},
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   APP_NUM = #{applicationNumber}
        AND     SVC_GRADE_CD = #{serviceGradeCode}
        AND     KEY_TYPE_CD = #{keyTypeCode}
        AND     APP_KEY_SEQ = #{applicationKeySequence}
    </script>""")
    public void updateApplicationKey(DcApplicationKey applicationKey);
    
    @Update("""<script>
        UPDATE  DC_APP_KEY
        SET     APP_KEY = #{reissueApplicationKey},
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   APP_NUM = #{applicationNumber}
        AND     SVC_GRADE_CD = #{serviceGradeCode}
        AND     KEY_TYPE_CD = #{keyTypeCode}
        AND     APP_KEY_SEQ = #{applicationKeySequence}
        AND     APP_KEY = #{applicationKey}
    </script>""")
    public void reissueApplicationKey(DcApplicationKey applicationKey);

    @Update("""<script>
        UPDATE  DC_APP_KEY
        SET     MSG_ENCRYPTION_KEY = #{msgEncryptionKey},
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   APP_NUM = #{applicationNumber}
        AND     SVC_GRADE_CD = #{serviceGradeCode}
        AND     KEY_TYPE_CD = #{keyTypeCode}
        AND     APP_KEY_SEQ = #{applicationKeySequence}
        AND     APP_KEY = #{applicationKey}
    </script>""")
    public void reissueApplicationMsgAuthKey(DcApplicationKey applicationKey);

    
    @Update("""<script>
        UPDATE  DC_APP_KEY
        SET     KEY_DELETE_YN = 'Y',
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   APP_NUM = #{applicationNumber}
        AND     SVC_GRADE_CD = #{serviceGradeCode}
        AND     KEY_TYPE_CD = #{keyTypeCode}
        AND     APP_KEY_SEQ = #{applicationKeySequence}
        AND     KEY_DELETE_YN = 'N'
    </script>""")
    public void deleteApplicationKey(DcApplicationKey applicationKey);

        
    @Results(id="applicationKeyListResult", value = [
        @Result(column = "APP_NUM",        property = "applicationNumber"),
        @Result(column = "SVC_GRADE_CD",        property = "serviceGradeCode"),
        @Result(column = "KEY_TYPE_CD",         property = "keyTypeCode"),
        @Result(column = "APP_KEY_SEQ",         property = "applicationKeySequence"),
        @Result(column = "APP_KEY",             property = "applicationKey"),
        @Result(column = "KEY_END_DATETIME",    property = "keyEndDatetime"),
        @Result(column = "MODIFY_DATETIME",     property = "modifyDatetime"),
        @Result(column = "TYPE_LIMIT_YN",       property = "typeLimitYn"),
        @Result(column = "KEY_TYPE_ATTR_VAL",   property = "keyTypeAttributeValue"),
        @Result(column = "HMAC_AUTH_TYPE_CD",       property = "hmacAuthTypeCode"),
        @Result(column = "MSG_ENCRYPTION_TYPE_CD",       property = "msgEncryptionTypeCode"),
        @Result(column = "MSG_ENCRYPTION_KEY",       property = "msgEncryptionKey")
    ])
    @Select("""
    <script>
        SELECT /* selectApplicationKeyList | SeonIl Bae | 2016-12-07 */
               APP_NUM
             , SVC_GRADE_CD
             , KEY_TYPE_CD
             , APP_KEY_SEQ
             , APP_KEY
             , DATE_FORMAT(KEY_END_DATETIME,'%Y-%m-%d') AS KEY_END_DATETIME
             , IFNULL(DATE_FORMAT(MODIFY_DATETIME,'%Y-%m-%d %H:%i:%s'),DATE_FORMAT(CREATE_DATETIME,'%Y-%m-%d %H:%i:%s')) AS MODIFY_DATETIME
             , TYPE_LIMIT_YN
             , KEY_TYPE_ATTR_VAL
             , HMAC_AUTH_TYPE_CD
             , MSG_ENCRYPTION_TYPE_CD
             , MSG_ENCRYPTION_KEY
        FROM   DC_APP_KEY
        WHERE  KEY_DELETE_YN = 'N'
        AND    APP_NUM = #{applicationNumber}
        ORDER BY APP_KEY_SEQ ASC
    </script>
    """)
    public List<DcApplicationKey> selectApplicationKeyList(@Param("applicationNumber") Long applicationNumber);
    
    
    public DcApplicationKey selectApplicationKeyDetail();
    
    
    @Insert("""<script>
        INSERT INTO DC_APP_SVC (
            APP_NUM,
            SVC_NUM,
            END_DATETIME,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{applicationNumber},
            #{serviceNumber},
            '9999-12-31 23:59:59',
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
        ON DUPLICATE KEY UPDATE
            MODIFY_DATETIME = NOW(),
            MODIFY_ID = #{modifyId}
    </script>""")
    public void insertApplicationService(DcApplicationService applicationService);
    
    
    @Update("""<script>
        UPDATE  DC_APP_SVC
        SET     END_DATETIME = NOW(6),
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   APP_NUM = #{applicationNumber}
        AND     END_DATETIME > NOW(6)
        <if test="serviceNumberList != null and serviceNumberList.size != 0">
        AND    SVC_NUM NOT IN
            <foreach item="item" index="index" collection="serviceNumberList" open="(" separator="," close=")">
               #{item}
            </foreach>
        </if>
     </script>""")
    public void updateApplicationService(DcApplication application);
    
    
    @Insert("""<script>
        INSERT /* insertApplicationSla | SeonIl Bae | 2016-12-26 */ INTO DC_APP_SLA (
            APP_NUM,
            SVC_NUM,
            API_GRP_NUM,
            SVC_GRADE_CD,
            END_DATETIME,
            BEGIN_DATETIME,
            SVC_LIMIT_QTY,
            SVC_CRITERIA_CD,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{applicationNumber},
            #{serviceNumber},
            #{apiGroupNumber},
            #{serviceGradeCode},
            '9999-12-31 23:59:59',
            DATE_FORMAT(#{beginDatetime},'%Y.%m.%d %H:%i:%s.%f'),
            #{serviceLimitQuantity},
            #{serviceCriteriaCode},
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
    </script>""")
    public void insertApplicationSla(DcApplicationSla applicationSla);
    
    
    @Results(id="applicationServiceListResult", value = [
        @Result(column = "SVC_NUM",            property = "serviceNumber"),
        @Result(column = "SVC_NM",             property = "serviceName"),
        @Result(column = "SITE_CD",             property = "siteCode"),
        @Result(column = "SVC_SECTION_CD",      property = "serviceSectionCode"),
        @Result(column = "SVC_USE_YN",          property = "serviceUseYn"),
        @Result(column = "SLA_USE_YN",          property = "slaUseYn"),
        @Result(column = "CAPACITY_USE_YN",     property = "capacityUseYn")
    ])
    @Select("""<script>
        SELECT /* selectApplicationServiceList | SeonIl Bae | 2016-12-06 */
               T1.SVC_NUM
             , T2.SVC_NM
             , T2.SITE_CD
             , T2.SVC_SECTION_CD
             , T2.SVC_USE_YN
             , T2.SLA_USE_YN
             , T2.CAPACITY_USE_YN
        FROM   DC_APP_SVC T1 INNER JOIN MC_SVC T2
            ON (T1.SVC_NUM = T2.SVC_NUM)
        WHERE  T1.END_DATETIME > NOW(6)
        AND    T1.APP_NUM = #{applicationNumber}
    </script>""")
    public List<DcApplicationService> selectApplicationServiceList(Long applicationNumber);
    
    
    public DcApplicationService selectApplicationServiceDetail();
    
    
    @Insert("""<script>
        INSERT INTO DC_APP_SLA_MODIFY_REQUEST (
            APP_NUM,
            SVC_NUM,
            API_GRP_NUM,
            SVC_GRADE_CD,
            PREV_SVC_LIMIT_QTY,
            MODIFY_SVC_LIMIT_QTY,
            PREV_SVC_CRITERIA_CD,
            MODIFY_SVC_CRITERIA_CD,
            SLA_MODIFY_STATE_CD,
            SLA_MODIFY_STATE_REASON,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{applicationNumber},
            #{serviceNumber},
            #{apiGroupNumber},
            #{serviceGradeCode},
            #{previousServiceLimitQuantity},
            #{modifyServiceLimitQuantity},
            #{previousServiceCriteriaCode},
            #{modifyServiceCriteriaCode},
            #{slaModifyStateCode},
            #{slaModifyStateReason},
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
    </script>""")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="slaModifyRequestNumber", before=false, resultType=Long.class)
    public void insertApplicationSlaRequest(DcApplicationSlaRequest applicationSlaRequest);

        
    @Insert("""<script>
        INSERT INTO DC_DEVELOPER (
            LOGIN_ID,
            LOGIN_PASSWD,
            DEVELOPER_NM,
            EXTENSION_PHONE_NUM,
            CELL_PHONE_NUM,
            EMAIL,
            NICKNAME,
            DEVELOPER_STATE_CD,
            DEVELOPER_STATE_REASON,
            LOGIN_FAIL_CNT,
            ACCOUNT_LOCK_YN,
            ACCOUNT_LOCK_REASON,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{loginId},
            #{loginPassword},
            #{developerName},
            #{extensionPhoneNumber},
            #{cellPhoneNumber},
            #{email},
            #{nickname},
            #{developerStateCode},
            #{developerStateReason},
            0,
            'N',
            #{accountLockReason},
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
    </script>""")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="developerNumber", before=false, resultType=Long.class)
    public void insertDeveloper(DcDeveloper developer);

    
    @Insert("""<script>
        INSERT INTO DC_HIST_MGMT (
            HIST_MGMT_CD,
            HIST_MGMT_MEMO,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{historyManagementCode},
            #{historyManagementMemo},
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
    </script>""")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="historyManagementNumber", before=false, resultType=Long.class)
    public void insertHistoryManagement(DcHistoryManagement historyManagement);

    @Insert("""<script>
        INSERT INTO DC_APP_HIST (
            APP_NUM,
            HIST_END_DATETIME,
            HIST_BEGIN_DATETIME,
            DEVELOPER_NUM,
            SITE_CD,
            APP_NM,
            APP_DESC,
            APP_USE_YN,
            APP_DELETE_YN,
            HIST_MGMT_NUM,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        )
        SELECT APP_NUM
             , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f') AS HIST_END_DATETIME
             , NOW(6) AS HIST_BEGIN_DATETIME
             , DEVELOPER_NUM
             , SITE_CD
             , APP_NM
             , APP_DESC
             , APP_USE_YN
             , APP_DELETE_YN
             , #{historyManagementNumber} AS HIST_MGMT_NUM
             , CREATE_DATETIME
             , CREATE_ID
             , MODIFY_DATETIME
             , MODIFY_ID
        FROM   DC_APP
        WHERE  APP_NUM = #{applicationNumber}
    </script>""")
    public void insertApplicationHistory(@Param("applicationNumber") Long applicationNumber, @Param("historyManagementNumber") Long historyManagementNumber);
    
    
    @Insert("""<script>
        INSERT INTO DC_APP_KEY_HIST (
            APP_NUM,
            SVC_GRADE_CD,
            KEY_TYPE_CD,
            APP_KEY_SEQ,
            HIST_END_DATETIME,
            HIST_BEGIN_DATETIME,
            APP_KEY,
            KEY_END_DATETIME,
            KEY_TYPE_ATTR_VAL,
            TYPE_LIMIT_YN,
            KEY_DELETE_YN,
            HIST_MGMT_NUM,
            HMAC_AUTH_TYPE_CD,
            MSG_ENCRYPTION_TYPE_CD,
            MSG_ENCRYPTION_KEY,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        )
        SELECT APP_NUM
             , SVC_GRADE_CD
             , KEY_TYPE_CD
             , APP_KEY_SEQ
             , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f') AS HIST_END_DATETIME
             , NOW(6) AS HIST_BEGIN_DATETIME
             , APP_KEY
             , KEY_END_DATETIME
             , KEY_TYPE_ATTR_VAL
             , TYPE_LIMIT_YN
             , KEY_DELETE_YN
             , #{historyManagementNumber} AS HIST_MGMT_NUM
             , HMAC_AUTH_TYPE_CD
             , MSG_ENCRYPTION_TYPE_CD
             , MSG_ENCRYPTION_KEY
             , CREATE_DATETIME
             , CREATE_ID
             , MODIFY_DATETIME
             , MODIFY_ID
        FROM   DC_APP_KEY
        WHERE  APP_NUM = #{applicationNumber}
        AND    SVC_GRADE_CD = #{serviceGradeCode}
        AND    KEY_TYPE_CD = #{keyTypeCode}
        AND    APP_KEY_SEQ = #{applicationKeySequence}
    </script>""")
    public void insertApplicationKeyHistory(DcApplicationKey applicationKey);

    @Update("""
        UPDATE  DC_APP_KEY_HIST
        SET     HIST_END_DATETIME = NOW(6),
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   APP_NUM = #{applicationNumber}
        AND    SVC_GRADE_CD = #{serviceGradeCode}
        AND    KEY_TYPE_CD = #{keyTypeCode}
        AND    APP_KEY_SEQ = #{applicationKeySequence}
        AND     HIST_END_DATETIME > NOW(6)
    """)
    public void updateApplicationKeyHistory(DcApplicationKey applicationKey);
    
    
    @Update("""
        UPDATE  DC_APP_HIST
        SET     HIST_END_DATETIME = NOW(6),
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   APP_NUM = #{applicationNumber}
        AND     HIST_END_DATETIME > NOW(6)
    """)
    public void updateApplicationHistory(@Param("applicationNumber") Long applicationNumber, @Param("modifyId") String modifyId);

    @Results(id="serviceListResult", value = [
        @Result(column = "SVC_NUM",             property = "serviceNumber"),
        @Result(column = "SVC_NM",              property = "serviceName")
    ])
    @Select("""
    <script>
        SELECT /* selectServiceList | SeonIl Bae | 2016-12-06 */
               MS.SVC_NUM
             , MS.SVC_NM
        FROM   MC_SVC MS
        WHERE  MS.SVC_DELETE_YN = 'N'
        AND    MS.SVC_USE_YN = 'Y'
        <if test="serviceNumberList != null and serviceNumberList.size != 0">
        AND    MS.SVC_NUM IN
            <foreach item="item" index="index" collection="serviceNumberList" open="(" separator="," close=")">
               #{item}
            </foreach>
        </if>
        AND    MS.SLA_USE_YN = 'Y'
        AND NOT EXISTS (
            SELECT 'x'
            FROM   DC_APP_SVC x
            WHERE  x.APP_NUM = #{applicationNumber}
            AND    x.SVC_NUM = MS.SVC_NUM
            AND    x.END_DATETIME > NOW(6)
        )
        ORDER BY SVC_NUM ASC
    </script>
    """)
    public List<DcApplicationService> selectServiceList(DcApplication application);


    @Results(id="applicationSlaListResult", value = [
        @Result(column = "SVC_NUM",             property = "serviceNumber"),
        @Result(column = "SVC_NM",              property = "serviceName"),
        @Result(column = "API_GRP_NUM",         property = "apiGroupNumber"),
        @Result(column = "API_GRP_NM",          property = "apiGroupName"),
        @Result(column = "LIMIT_QTY1",          property = "commerceServiceLimitQuantity"),
        @Result(column = "CRITERIA_CD1",        property = "commerceServiceCriteriaCode"),
        @Result(column = "LIMIT_QTY2",          property = "testServiceLimitQuantity"),
        @Result(column = "CRITERIA_CD2",        property = "testServiceCriteriaCode"),
        @Result(column = "MODIFY_DATETIME",     property = "modifyDatetime")
    ])
    @Select("""
    <script>
        SELECT /* selectApplicationSlaList | SeonIl Bae | 2016-12-07 */
               P.SVC_NUM,
               (SELECT SVC_NM FROM MC_SVC WHERE SVC_NUM = P.SVC_NUM) AS SVC_NM,
               P.API_GRP_NUM,
               IF(P.API_GRP_NUM=0,'',(SELECT API_GRP_NM FROM MC_API_GRP WHERE SVC_NUM = P.SVC_NUM AND API_GRP_NUM = P.API_GRP_NUM)) AS API_GRP_NM,
               MAX(P.LIMIT_QTY1) AS LIMIT_QTY1,
               MAX(P.CRITERIA_CD1) AS CRITERIA_CD1,
               MAX(P.LIMIT_QTY2) AS LIMIT_QTY2,
               MAX(P.CRITERIA_CD2) AS CRITERIA_CD2,
               IFNULL(MAX(DATE_FORMAT(P.MODIFY_DATETIME, '%Y-%m-%d %H:%i:%s')),'-') AS MODIFY_DATETIME
        FROM   (
                  SELECT SVC.SVC_NUM
                       , SVC.API_GRP_NUM
                       , CASE WHEN PLC.SVC_GRADE_CD LIKE '___SVC_GRADE_01' THEN PLC.SVC_LIMIT_QTY END AS LIMIT_QTY1
                       , CASE WHEN PLC.SVC_GRADE_CD LIKE '___SVC_GRADE_01' THEN PLC.SVC_CRITERIA_CD END AS CRITERIA_CD1
                       , CASE WHEN PLC.SVC_GRADE_CD LIKE '___SVC_GRADE_02' THEN PLC.SVC_LIMIT_QTY END AS LIMIT_QTY2
                       , CASE WHEN PLC.SVC_GRADE_CD LIKE '___SVC_GRADE_02' THEN PLC.SVC_CRITERIA_CD END AS CRITERIA_CD2
                       , PLC.MODIFY_DATETIME
                  FROM   (
                              SELECT MS.SVC_NUM, 0 AS API_GRP_NUM
                              FROM   MC_SVC MS
                              WHERE  1=1
                              AND    MS.SVC_DELETE_YN = 'N'
                              AND    MS.SVC_USE_YN    = 'Y'
                              AND    MS.SLA_USE_YN    = 'Y' /* SLA 사용 */
                              UNION ALL
                              SELECT MS.SVC_NUM, MAG.API_GRP_NUM
                              FROM   MC_SVC MS INNER JOIN MC_API_GRP MAG
                                  ON (MS.SVC_NUM = MAG.SVC_NUM)
                              WHERE  1=1
                              AND    MS.SVC_DELETE_YN      = 'N'
                              AND    MS.SVC_USE_YN         = 'Y'
                              AND    MS.SLA_USE_YN         = 'Y'
                              AND    MAG.API_GRP_DELETE_YN = 'N'
                              AND    MAG.API_GRP_USE_YN    = 'Y'
                              AND    MAG.SLA_USE_YN        = 'Y' /* SLA 사용 */
                         ) SVC INNER JOIN (
                                SELECT T2.SVC_NUM
                                     , T2.API_GRP_NUM
                                     , IFNULL(T3.SVC_GRADE_CD, REPLACE(T2.SVC_GRADE_CD,'DC_','MC_')) AS SVC_GRADE_CD
                                     , IFNULL(T3.SVC_LIMIT_QTY, T2.SVC_LIMIT_QTY) AS SVC_LIMIT_QTY
                                     , IFNULL(T3.SVC_CRITERIA_CD, REPLACE(T2.SVC_CRITERIA_CD,'DC_','MC_')) AS SVC_CRITERIA_CD
                                     , T3.MODIFY_DATETIME
                                FROM   DC_APP_SVC T1 INNER JOIN MC_SVC_POLICY T2
                                       ON (    T1.SVC_NUM = T2.SVC_NUM
                                           AND T2.SVC_POLICY_CD = 'MC_SVC_POLICY_02'
                                           AND T2.SVC_POLICY_DELETE_YN = 'N'
                                       )
                                       LEFT OUTER JOIN DC_APP_SLA T3
                                       ON (    T2.SVC_NUM = T3.SVC_NUM
                                           AND T2.API_GRP_NUM = T3.API_GRP_NUM
                                           AND T2.SVC_GRADE_CD = REPLACE(T3.SVC_GRADE_CD,'DC_','MC_')
                                           AND T3.END_DATETIME > NOW(6)
                                           AND T3.APP_NUM = #{applicationNumber}
                                       )
                                WHERE  T1.APP_NUM = #{applicationNumber}
                                AND    T1.END_DATETIME > NOW(6)
                         ) PLC 
                     ON (SVC.SVC_NUM = PLC.SVC_NUM AND SVC.API_GRP_NUM = PLC.API_GRP_NUM)
        ) P
        GROUP BY SVC_NUM, API_GRP_NUM
        ORDER BY SVC_NUM, API_GRP_NUM
    </script>
    """)
    public List<DcApplicationSla> selectApplicationSlaList(@Param("applicationNumber") Long applicationNumber);

    @Select("""
    <script>
        SELECT
          SCOPE_NUM
        FROM
          DC_APP_SCOPE
        WHERE
          APP_NUM = #{applicationNumber} AND
          END_DATETIME > NOW()
    </script>
    """)
    public List<String> selectApplicationScopeNumberList(@Param("applicationNumber") Long applicationNumber);

    @Update("""
        UPDATE  DC_APP_SLA
        SET     END_DATETIME = NOW(6),
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   APP_NUM = #{applicationNumber}
        AND     SVC_NUM = #{serviceNumber}
        AND     API_GRP_NUM = #{apiGroupNumber}
        AND     SVC_GRADE_CD = #{serviceGradeCode}
        AND     END_DATETIME > NOW(6)
    """)
    public int updateApplicationSla(DcApplicationSla applicationSla);

    @Results(id="searchServiceListResult", value = [
        @Result(column = "SVC_NUM",            property = "serviceNumber"),
        @Result(column = "SVC_NM",             property = "serviceName")
    ])
    @Select("""
    <script>
        SELECT /* selectSearchServiceList | SeonIl Bae | 2016-12-09 */
               MS.SVC_NUM
             , MS.SVC_NM
        FROM   MC_SVC MS
        WHERE  MS.SVC_DELETE_YN = 'N'
        AND    MS.SVC_USE_YN = 'Y'
        AND    MS.SLA_USE_YN = 'Y'
        ORDER BY SVC_NUM ASC
    </script>
    """)
    public List<DcApplicationService> selectSearchServiceList(SearchRequest searchRequest);
    

    @Results(id="applicationDetailHistoryListResult", value = [
        @Result(column = "MODIFY_DATETIME",       property = "modifyDatetime"),
        @Result(column = "MODIFY_ID",             property = "modifyId")
    ])
    @Select("""
    <script>
        SELECT /* selectApplicationDetailListHistory | SeonIl Bae | 2016-12-22 */
               DATE_FORMAT(HIST_BEGIN_DATETIME,'%Y-%m-%d %H:%i:%s') AS MODIFY_DATETIME,
               MODIFY_ID
        FROM   DC_APP_HIST
        WHERE  APP_NUM = #{applicationNumber}
        ORDER BY HIST_BEGIN_DATETIME DESC
    </script>
    """)
    public List<DcApplication> selectApplicationDetailHistoryList(Long applicationNumber);

    
    @Results(id="applicationSlaHistoryListResult", value = [
        @Result(column = "MODIFY_DATETIME",     property = "modifyDatetime"),
        @Result(column = "MODIFY_ID",           property = "modifyId"),
        @Result(column = "SVC_NUM",             property = "serviceNumber"),
        @Result(column = "API_GRP_NUM",         property = "apiGroupNumber"),
        @Result(column = "LIMIT_QTY1",          property = "commerceServiceLimitQuantity"),
        @Result(column = "CRITERIA_CD1",        property = "commerceServiceCriteriaCode"),
        @Result(column = "LIMIT_QTY2",          property = "testServiceLimitQuantity"),
        @Result(column = "CRITERIA_CD2",        property = "testServiceCriteriaCode")
    ])
    @Select("""
    <script>
        SELECT /* selectApplicationSlaHistoryList | SeonIl Bae | 2016-12-22 */
               DATE_FORMAT(PL.MODIFY_DATETIME,'%Y-%m-%d %H:%i:%s') AS MODIFY_DATETIME,
               PL.MODIFY_ID,
               PL.SVC_NUM,
               PL.API_GRP_NUM,
               MAX(PL.LIMIT_QTY1) AS LIMIT_QTY1,
               MAX(PL.CRITERIA_CD1) AS CRITERIA_CD1,
               MAX(PL.LIMIT_QTY2) AS LIMIT_QTY2,
               MAX(PL.CRITERIA_CD2) AS CRITERIA_CD2
        FROM   (
                    SELECT MODIFY_DATETIME
                         , MODIFY_ID
                         , A.SVC_NUM
                         , A.API_GRP_NUM
                         , CASE WHEN A.SVC_GRADE_CD = 'DC_SVC_GRADE_01' THEN A.SVC_LIMIT_QTY END AS LIMIT_QTY1
                         , CASE WHEN A.SVC_GRADE_CD = 'DC_SVC_GRADE_01' THEN A.SVC_CRITERIA_CD END AS CRITERIA_CD1
                         , CASE WHEN A.SVC_GRADE_CD = 'DC_SVC_GRADE_02' THEN A.SVC_LIMIT_QTY END AS LIMIT_QTY2
                         , CASE WHEN A.SVC_GRADE_CD = 'DC_SVC_GRADE_02' THEN A.SVC_CRITERIA_CD END AS CRITERIA_CD2
                    FROM   (
                                SELECT BEGIN_DATETIME AS MODIFY_DATETIME, MODIFY_ID, SVC_NUM, API_GRP_NUM, SVC_GRADE_CD, SVC_LIMIT_QTY, SVC_CRITERIA_CD AS SVC_CRITERIA_CD
                                FROM   DC_APP_SLA
                                WHERE  1=1
                                AND    APP_NUM = #{applicationNumber}
                                AND    SVC_NUM = #{serviceNumber}
                                AND    API_GRP_NUM = #{apiGroupNumber}
                           ) A
        ) PL
        GROUP BY MODIFY_DATETIME, SVC_NUM, API_GRP_NUM
        ORDER BY MODIFY_DATETIME DESC
    </script>
    """)
    public List<DcApplicationSla> selectApplicationSlaHistoryList(DcApplicationSla applicationSla);

    @Update("""
        UPDATE  DC_APP
        SET     APP_DELETE_YN = 'Y',
                APP_USE_YN = 'N',
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   APP_NUM = #{applicationNumber}
    """)
    public void deleteApplication(DcApplication application);
    
    
    @Update("""
        UPDATE  DC_APP_SLA
        SET     END_DATETIME = NOW(6),
                MODIFY_DATETIME = NOW(),
                MODIFY_ID = #{modifyId}
        WHERE   APP_NUM = #{applicationNumber}
        AND     END_DATETIME > NOW(6)
    """)
    public void removeApplicationSla(DcApplicationSla applicationSla);
    
    
    @Select("""
    <script>
        SELECT DATE_FORMAT(NOW(6),'%Y.%m.%d %H:%i:%s.%f') AS beginDatetime
    </script>
    """)
    public String selectBeginDatetime();


    
    @Results(id="dashboardApplicationsResult", value = [
        @Result(column = "TOTAL_CNT",   property = "totalCount"),
        @Result(column = "ACTIVE_CNT",  property = "activeCount")
    ])
    @Select("""
    <script>
        SELECT /* selectDashboardApplications | SeonIl Bae | 2017-02-03 */
               TC.CNT AS TOTAL_CNT
             , AC.CNT AS ACTIVE_CNT
        FROM   (
                  SELECT COUNT(DC.APP_NUM) AS CNT
                  FROM   DC_APP DC INNER JOIN DC_APP_SVC DAS
                         ON (DC.APP_NUM = DAS.APP_NUM)
                  WHERE  DC.APP_USE_YN = 'Y'
                  AND    DC.APP_DELETE_YN = 'N'
                  AND    DAS.END_DATETIME > NOW()
                <if test="query != null">
                  <if test="(query.siteCode != null and query.siteCode eq 'MC_SITE_02'.toString())">
                    AND    DC.SITE_CD = 'DC_SITE_01'
                  </if>
                  <if test="(query.siteCode != null and query.siteCode eq 'MC_SITE_03'.toString())">
                    AND    DC.SITE_CD = 'DC_SITE_02'
                  </if>
                </if>
               ) TC,
               (
                SELECT COUNT(T1.APP_NUM) AS CNT
                FROM   (
                          SELECT AA.APP_NUM
                          FROM   AN_APP_BANDWIDTH_STATS AA
                          WHERE  AA.STATS_DATE BETWEEN DATE_ADD(NOW(), INTERVAL -1 MONTH) AND NOW()
                          AND    AA.CALL_CNT > 0
                        <if test="query != null">
                          <if test="query.siteCode != null and query.siteCode != ''">
                            AND    AA.SITE_CD = #{query.siteCode}
                          </if>
                        </if>
                          AND EXISTS (
                              SELECT 'x'
                              FROM   DC_APP x1
                              WHERE  x1.APP_NUM = AA.APP_NUM
                          )
                          GROUP BY APP_NUM
                       ) T1
               ) AC
    </script>
    """)
    public DashboardApplications selectDashboardApplications(SearchRequest searchRequest);


    @Results(id="searchScopeListResult", value = [
        @Result(column = "SCOPE_NUM",           property = "scopeNumber"),
        @Result(column = "SCOPE_NM",            property = "scopeName"),
        @Result(column = "SCOPE_CONTEXT",       property = "scopeContext"),
        @Result(column = "CRITERIA_SCOPE_NUM",  property = "criteriaScopeNumber"),
        @Result(column = "OPPONENT_SCOPE_NUM",  property = "opponentScopeNumber"),
        @Result(column = "CRITERIA_SCOPE_LEVEL",property = "criteriaScopeLevel"),
        @Result(column = "OPPONENT_SCOPE_LEVEL",property = "opponentScopeLevel"),
        @Result(column = "CRITERIA_SORT_NUM",   property = "criteriaSortNumber"),
        @Result(column = "OPPONENT_SORT_NUM",   property = "opponentSortNumber")
    ])
    @Select("""
    <script>
        SELECT 
               SCOPE_NUM, SCOPE_NM, SCOPE_CONTEXT, NULL AS CRITERIA_SCOPE_NUM, 0 AS OPPONENT_SCOPE_NUM, 0 AS CRITERIA_SCOPE_LEVEL, 0 AS OPPONENT_SCOPE_LEVEL, 0 AS CRITERIA_SORT_NUM, 0 AS OPPONENT_SORT_NUM
        FROM   MC_SCOPE
        WHERE  SCOPE_NUM = 0
        UNION ALL
        SELECT /* selectSearchScopeList | SeonIl Bae | 2017-02-08 */
               T1.SCOPE_NUM
             , T1.SCOPE_NM
             , T1.SCOPE_CONTEXT
             , T2.CRITERIA_SCOPE_NUM
             , T2.OPPONENT_SCOPE_NUM
             , T2.CRITERIA_SCOPE_LEVEL
             , T2.OPPONENT_SCOPE_LEVEL
             , T2.CRITERIA_SORT_NUM
             , T2.OPPONENT_SORT_NUM
        FROM   MC_SCOPE T1 INNER JOIN MC_SCOPE_RELATION T2
        WHERE  T1.SCOPE_NUM = T2.OPPONENT_SCOPE_NUM
        AND    T1.SCOPE_DELETE_YN = 'N'
        AND    T1.SCOPE_USE_YN = 'Y'
        AND    T2.END_DATETIME > NOW(6)
        AND    T1.SCOPE_NUM > 0
        AND EXISTS (
            SELECT 'x'
            FROM   DC_APP_SVC x
            WHERE  x.APP_NUM = #{applicationNumber}
            AND    x.SVC_NUM = T1.SVC_NUM
            AND    x.END_DATETIME > NOW(6)
        )
        ORDER BY OPPONENT_SCOPE_LEVEL ASC, OPPONENT_SORT_NUM ASC
    </script>
    """)
    public List<DcApplicationScope> selectSearchScopeList(Long applicationNumber);

    
    @Results(id="applicationAuthListResult", value = [
        @Result(column = "APP_NUM",             property = "applicationNumber"),
        @Result(column = "CLIENT_ID",           property = "clientId"),
        @Result(column = "CLIENT_SECRET",       property = "clientSecret"),
        @Result(column = "KEY_END_DATETIME",    property = "keyEndDatetime"),
        @Result(column = "CREATE_DATETIME",     property = "createDatetime"),
        @Result(column = "MODIFY_DATETIME",     property = "modifyDatetime")
    ])
    @Select("""
    <script>
        SELECT /* selectApplicationAuthList | SeonIl Bae | 2017-02-09 */
               APP_NUM
             , CLIENT_ID
             , CLIENT_SECRET
             , DATE_FORMAT(KEY_END_DATETIME,'%Y-%m-%d') AS KEY_END_DATETIME
             , DATE_FORMAT(CREATE_DATETIME,'%Y-%m-%d %H:%i:%s') AS CREATE_DATETIME
             , DATE_FORMAT(MODIFY_DATETIME,'%Y-%m-%d %H:%i:%s') AS MODIFY_DATETIME
        FROM   DC_APP_AUTH_KEY A
        WHERE  END_DATETIME > NOW(6)
        AND    APP_NUM = #{applicationNumber}
        ORDER BY CREATE_DATETIME DESC
        LIMIT 1
    </script>
    """)
    public List<DcApplicationAuthKey> selectApplicationAuthList(Long applicationNumber);


    @Insert("""<script>
        INSERT INTO DC_APP_SCOPE (
            APP_NUM,
            SCOPE_NUM,
            END_DATETIME,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{applicationNumber},
            #{scopeNumber},
            '9999-12-31 23:59:59',
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
        ON DUPLICATE KEY UPDATE
            MODIFY_DATETIME = NOW(),
            MODIFY_ID = #{modifyId}
    </script>""")
    public void insertApplicationScope(DcApplicationScope applicationScope);

    @Delete("""<script>
        DELETE
        FROM
            DC_APP_SCOPE
        WHERE
            APP_NUM = #{applicationNumber}
    </script>""")
    public void deleteApplicationScope(DcApplicationScope applicationScope);

    @Insert("""<script>
        INSERT INTO DC_APP_AUTH_KEY (
            APP_NUM,
            CLIENT_ID,
            END_DATETIME,
            CLIENT_SECRET,
            KEY_END_DATETIME,
            CREATE_DATETIME,
            CREATE_ID,
            MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{applicationNumber},
            #{clientId},
            '9999-12-31 23:59:59',
            #{clientSecret},
            '9999-12-31 23:59:59',
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
        ON DUPLICATE KEY UPDATE
            MODIFY_DATETIME = NOW(),
            MODIFY_ID = #{modifyId}
    </script>""")
    public void insertApplicationAuthKey(DcApplicationAuthKey applicationAuthKey);

    @Update("""
        UPDATE
          DC_APP_AUTH_KEY
        SET
          END_DATETIME    = NOW()
         ,MODIFY_DATETIME = NOW()
         ,KEY_END_DATETIME = NOW()
        WHERE
          APP_NUM = #{applicationNumber}
          AND CLIENT_ID = #{clientId}
          AND END_DATETIME > NOW()
    """)
    public void updateApplicationAuthSecretKey(DcApplicationAuthKey applicationAuthKey);

    @Select("""
        SELECT
          SCOPE_CONTEXT
        FROM
          MC_SCOPE
        WHERE
          SCOPE_NUM = #{scopeNumber}
    """)
    public String selectScopeContext(String scopeNumber);
}
