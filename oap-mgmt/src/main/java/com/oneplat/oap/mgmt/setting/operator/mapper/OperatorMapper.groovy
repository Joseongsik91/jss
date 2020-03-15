package com.oneplat.oap.mgmt.setting.operator.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Many
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectKey
import org.apache.ibatis.annotations.Update

import com.oneplat.oap.core.model.SearchRequest
import com.oneplat.oap.mgmt.setting.admin.model.Role
import com.oneplat.oap.mgmt.setting.operator.model.Operator
import com.oneplat.oap.mgmt.setting.operator.model.OperatorCode
import com.oneplat.oap.mgmt.setting.operator.model.Operator.CreateOperator
import com.oneplat.oap.mgmt.setting.operator.model.Operator.ModifyOperator
import com.oneplat.oap.mgmt.setting.operator.model.Operator.OperatorDetail
import com.oneplat.oap.mgmt.setting.operator.model.Operator.OperatorPersonInfo
import com.oneplat.oap.mgmt.setting.operator.model.Operator.ResetOperatorPassword
import com.oneplat.oap.mgmt.setting.operator.model.enums.OperatorGroup

@Mapper
public interface OperatorMapper  {

    @Results(id="operatorResult", value = [
        @Result(property = "operatorNumber", column = "OPR_NUM"),
        @Result(property = "loginId", column = "LOGIN_ID"),
        @Result(property = "operatorName", column = "OPR_NM"),
        @Result(property = "dept", column = "DEPT"),
        @Result(property = "position", column = "POSITION"),
        @Result(property = "extensionPhoneNumber", column = "EXTENSION_PHONE_NUM"),
        @Result(property = "cellPhoneNum", column = "CELL_PHONE_NUM"),
        @Result(property = "email", column = "EMAIL"),
        @Result(property = "nickName", column = "NICKNAME"),
        @Result(property = "note", column = "NOTE"),
        @Result(property = "operatorStateCode", column = "OPR_STATE_CD"),
        @Result(property = "operatorStateDesc", column = "OPR_STATE_REASON"),
        @Result(property = "loginFailCount", column = "LOGIN_FAIL_CNT"),
        @Result(property = "acountLockYn", column = "ACCOUNT_LOCK_YN"),
        @Result(property = "acountLockReason", column = "ACCOUNT_LOCK_REASON"),
        @Result(property = "joinApprovalDateTime", column = "JOIN_APPROVAL_DATETIME"),
        @Result(property = "leaveDateTime", column = "LEAVE_DATETIME"),
        @Result(property = "createDateTime", column = "CREATE_DATETIME"),
        @Result(property = "createId", column = "CREATE_ID"),
        //@Result(property = "roleList", column="OPR_NUM", many=@Many(select = "selectOprRoleList"))
        @Result(property = "roleListString", column = "ROLE_NM"),
    ])
    @Select("""<script>
        SELECT T1.OPR_NUM, LOGIN_ID, T1.OPR_NM, DEPT, POSITION
            , EXTENSION_PHONE_NUM, CELL_PHONE_NUM, EMAIL, NICKNAME, NOTE
            , OPR_STATE_CD, OPR_STATE_REASON, LOGIN_FAIL_CNT, ACCOUNT_LOCK_YN, ACCOUNT_LOCK_REASON
            , CREATE_DATETIME, CREATE_ID
            , JOIN_APPROVAL_DATETIME, LEAVE_DATETIME
            , GROUP_CONCAT(T2.ROLE_NM) AS ROLE_NM
        FROM MC_OPR T1 LEFT JOIN
          ( SELECT TOR.ROLE_NUM, TOR.ROLE_NM, OPR_NUM FROM MC_ROLE TOR, MC_OPR_ROLE OOR WHERE TOR.ROLE_NUM = OOR.ROLE_NUM AND TOR.DELETE_YN = 'N' AND OOR.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s') ) T2
          ON T1.OPR_NUM = T2.OPR_NUM
        WHERE OPR_STATE_CD IN ('MC_OPR_STATE_04','MC_OPR_STATE_05')
        <choose>
            <when test="query!=null">
                <if test="query.searchWord!=null and query.searchWord!=''">
                    <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode=='ID'">AND LOGIN_ID LIKE CONCAT('%',#{query.searchWord},'%')</if>
                    <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode=='NAME'">AND OPR_NM LIKE CONCAT('%',#{query.searchWord},'%')</if>
                </if>
                <if test="query.searchRole!=null and query.searchRole>0">
                    AND T1.OPR_NUM IN ( SELECT OPR_NUM 
                                        FROM MC_ROLE A1, MC_OPR_ROLE A2
                                        WHERE A1.ROLE_NUM = A2.ROLE_NUM AND A1.DELETE_YN = 'N' 
                                            AND A2.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
                                            AND A1.ROLE_NUM = #{query.searchRole} )
                </if>
                <if test="query.operatorStateCode!=null and query.operatorStateCode!=''"> AND OPR_STATE_CD = #{query.operatorStateCode}</if>
                <if test="query.acountLockYn!=null and query.acountLockYn!=''"> AND ACCOUNT_LOCK_YN = #{query.acountLockYn}</if>
                <if test="query.startDate!=null and query.startDate!=''">
                    <if test="query.dateType=='REGDATE'">
                         AND JOIN_APPROVAL_DATETIME <![CDATA[>=]]> #{query.startDate}
                    </if>
                </if>
                <if test="query.endDate!=null and query.endDate!=''">
                    <if test="query.dateType=='REGDATE'">
                         AND JOIN_APPROVAL_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endDate},INTERVAL 1 DAY)
                    </if>
                </if>
                GROUP BY T1.OPR_NUM
                <choose>
                    <when test="query.sortField!=null and query.sortField!=''">
                    ORDER BY \${query.sortField} <if test="query.orderBy!=null and query.orderBy!=''"> \${query.orderBy} </if>
                    </when>
                    <otherwise>
                    ORDER BY JOIN_APPROVAL_DATETIME DESC
                    </otherwise>
                </choose>
                <if test="pageInfo!=null">
                LIMIT #{pageInfo.startRowNum}, #{pageInfo.size}
                </if>
            </when>
            <otherwise>
            GROUP BY T1.OPR_NUM
            ORDER BY JOIN_APPROVAL_DATETIME DESC
            </otherwise>
        </choose>
    </script>""")
    public List<Operator> selectOprList(SearchRequest searchRequest);
    
    @Select("""<script>
        SELECT COUNT(TT.OPR_NUM) COUNT
        FROM (
            SELECT T1.OPR_NUM, GROUP_CONCAT(T2.ROLE_NM) AS ROLE_NM
            FROM MC_OPR T1 LEFT JOIN
              ( SELECT TOR.ROLE_NUM, TOR.ROLE_NM, OPR_NUM FROM MC_ROLE TOR, MC_OPR_ROLE OOR WHERE TOR.ROLE_NUM = OOR.ROLE_NUM AND TOR.DELETE_YN = 'N' AND OOR.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s') ) T2
              ON T1.OPR_NUM = T2.OPR_NUM
            WHERE OPR_STATE_CD IN ('MC_OPR_STATE_04','MC_OPR_STATE_05')
            <if test="query!=null">
                <if test="query.searchWord!=null and query.searchWord!=''">
                    <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode=='ID'">AND LOGIN_ID LIKE CONCAT('%',#{query.searchWord},'%')</if>
                    <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode=='NAME'">AND OPR_NM LIKE CONCAT('%',#{query.searchWord},'%')</if>
                </if>
                <if test="query.searchRole!=null and query.searchRole>0">
                AND T1.OPR_NUM IN ( SELECT OPR_NUM 
                                    FROM MC_ROLE A1, MC_OPR_ROLE A2
                                    WHERE A1.ROLE_NUM = A2.ROLE_NUM AND A1.DELETE_YN = 'N' 
                                        AND A2.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
                                        AND A1.ROLE_NUM = #{query.searchRole} )
                </if>
                <if test="query.operatorStateCode!=null and query.operatorStateCode!=''"> AND OPR_STATE_CD = #{query.operatorStateCode}</if>
                <if test="query.acountLockYn!=null and query.acountLockYn!=''"> AND ACCOUNT_LOCK_YN = #{query.acountLockYn}</if>
                <if test="query.startDate!=null and query.startDate!=''">
                    <if test="query.dateType=='REGDATE'">
                         AND JOIN_APPROVAL_DATETIME <![CDATA[>=]]> #{query.startDate}
                    </if>
                </if>
                <if test="query.endDate!=null and query.endDate!=''">
                    <if test="query.dateType=='REGDATE'">
                         AND JOIN_APPROVAL_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endDate},INTERVAL 1 DAY)
                    </if>
                </if>
            </if>
            GROUP BY T1.OPR_NUM
        ) TT
    </script>""")
    public int getOprListCount(SearchRequest searchRequest);
    
    @Select("""<script>
        SELECT COUNT(OPR_NUM)
        FROM MC_OPR
        WHERE OPR_STATE_CD = #{stateCode}
    </script>""")
    public int getOprCount(String stateCode);
    
    @ResultMap("operatorResult")
    @Select("""<script>
        SELECT T1.OPR_NUM, LOGIN_ID, OPR_NM, DEPT, POSITION
            , EXTENSION_PHONE_NUM, CELL_PHONE_NUM, EMAIL, NICKNAME, NOTE
            , OPR_STATE_CD, OPR_STATE_REASON, LOGIN_FAIL_CNT, ACCOUNT_LOCK_YN, ACCOUNT_LOCK_REASON
            , CREATE_DATETIME, CREATE_ID, JOIN_APPROVAL_DATETIME
            , GROUP_CONCAT(T2.ROLE_NM) AS ROLE_NM
        FROM MC_OPR T1 LEFT JOIN
          ( SELECT TOR.ROLE_NUM, TOR.ROLE_NM, OPR_NUM FROM MC_ROLE TOR, MC_OPR_ROLE OOR WHERE TOR.ROLE_NUM = OOR.ROLE_NUM AND TOR.DELETE_YN = 'N' AND OOR.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s') ) T2
          ON T1.OPR_NUM = T2.OPR_NUM
        WHERE OPR_STATE_CD IN ('MC_OPR_STATE_01','MC_OPR_STATE_03')
        <choose>
            <when test="query!=null">
                <if test="query.searchWord!=null and query.searchWord!=''">
                    <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode=='ID'">AND LOGIN_ID LIKE CONCAT('%',#{query.searchWord},'%')</if>
                    <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode=='NAME'">AND OPR_NM LIKE CONCAT('%',#{query.searchWord},'%')</if>
                </if>
                <if test="query.searchRole!=null and query.searchRole>0">
                    AND T1.OPR_NUM IN ( SELECT OPR_NUM 
                                        FROM MC_ROLE A1, MC_OPR_ROLE A2
                                        WHERE A1.ROLE_NUM = A2.ROLE_NUM AND A1.DELETE_YN = 'N' 
                                            AND A2.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
                                            AND A1.ROLE_NUM = #{query.searchRole} )
                </if>
                <if test="query.operatorStateCode!=null and query.operatorStateCode!=''"> AND OPR_STATE_CD = #{query.operatorStateCode}</if>
                <if test="query.acountLockYn!=null and query.acountLockYn!=''"> AND ACCOUNT_LOCK_YN = #{query.acountLockYn}</if>
                <if test="query.startDate!=null and query.startDate!=''">
                    <if test="query.dateType=='REGDATE'">
                         AND CREATE_DATETIME <![CDATA[>=]]> #{query.startDate}
                    </if>
                </if>
                <if test="query.endDate!=null and query.endDate!=''">
                    <if test="query.dateType=='REGDATE'">
                         AND CREATE_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endDate},INTERVAL 1 DAY)
                    </if>
                </if>
                GROUP BY T1.OPR_NUM
                <choose>
                    <when test="query.sortField!=null and query.sortField!=''">
                    ORDER BY \${query.sortField} <if test="query.orderBy!=null and query.orderBy!=''"> \${query.orderBy} </if>
                    </when>
                    <otherwise>
                    ORDER BY CREATE_DATETIME DESC
                    </otherwise>
                </choose>
                <if test="pageInfo!=null">
                LIMIT #{pageInfo.startRowNum}, #{pageInfo.size}
                </if>
            </when>
            <otherwise>
            GROUP BY T1.OPR_NUM
            ORDER BY CREATE_DATETIME DESC
            </otherwise>
        </choose>
    </script>""")
    public List<Operator> selectApprovalStandByList(SearchRequest searchRequest);
    
    @Select("""<script>
        SELECT COUNT(TT.OPR_NUM) COUNT
        FROM (
            SELECT T1.OPR_NUM, GROUP_CONCAT(T2.ROLE_NM) AS ROLE_NM
            FROM MC_OPR T1 LEFT JOIN
              ( SELECT TOR.ROLE_NUM, TOR.ROLE_NM, OPR_NUM FROM MC_ROLE TOR, MC_OPR_ROLE OOR WHERE TOR.ROLE_NUM = OOR.ROLE_NUM AND TOR.DELETE_YN = 'N' AND OOR.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s') ) T2
              ON T1.OPR_NUM = T2.OPR_NUM
            WHERE OPR_STATE_CD IN ('MC_OPR_STATE_01','MC_OPR_STATE_03')
            <if test="query!=null">
                <if test="query.searchWord!=null and query.searchWord!=''">
                    <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode=='ID'">AND LOGIN_ID LIKE CONCAT('%',#{query.searchWord},'%')</if>
                    <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode=='NAME'">AND OPR_NM LIKE CONCAT('%',#{query.searchWord},'%')</if>
                </if>
                <if test="query.searchRole!=null and query.searchRole>0">
                AND T1.OPR_NUM IN ( SELECT OPR_NUM 
                                    FROM MC_ROLE A1, MC_OPR_ROLE A2
                                    WHERE A1.ROLE_NUM = A2.ROLE_NUM AND A1.DELETE_YN = 'N' 
                                        AND A2.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
                                        AND A1.ROLE_NUM = #{query.searchRole} )
                </if>
                <if test="query.operatorStateCode!=null and query.operatorStateCode!=''"> AND OPR_STATE_CD = #{query.operatorStateCode}</if>
                <if test="query.acountLockYn!=null and query.acountLockYn!=''"> AND ACCOUNT_LOCK_YN = #{query.acountLockYn}</if>
                <if test="query.startDate!=null and query.startDate!=''">
                    <if test="query.dateType=='REGDATE'">
                         AND CREATE_DATETIME <![CDATA[>=]]> #{query.startDate}
                    </if>
                </if>
                <if test="query.endDate!=null and query.endDate!=''">
                    <if test="query.dateType=='REGDATE'">
                         AND CREATE_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endDate},INTERVAL 1 DAY)
                    </if>
                </if>
            </if>
            GROUP BY T1.OPR_NUM
        ) TT
    </script>""")
    public int getApprovalStandByListCount(SearchRequest searchRequest);

    @Results(id="oprDetailRsult", value = [
        @Result(property = "operatorNumber", column = "OPR_NUM"),
        @Result(property = "loginId", column = "LOGIN_ID"),
        @Result(property = "loginPassword", column = "LOGIN_PASSWD"),
        @Result(property = "operatorName", column = "OPR_NM"),
        @Result(property = "dept", column = "DEPT"),
        @Result(property = "position", column = "POSITION"),
        @Result(property = "extensionPhoneNumber", column = "EXTENSION_PHONE_NUM"),
        @Result(property = "cellPhoneNum", column = "CELL_PHONE_NUM"),
        @Result(property = "email", column = "EMAIL"),
        @Result(property = "nickName", column = "NICKNAME"),
        @Result(property = "note", column = "NOTE"),
        @Result(property = "operatorStateCode", column = "OPR_STATE_CD"),
        @Result(property = "operatorStateDesc", column = "OPR_STATE_REASON"),
        @Result(property = "loginFailCount", column = "LOGIN_FAIL_CNT"),
        @Result(property = "acountLockYn", column = "ACCOUNT_LOCK_YN"),
        @Result(property = "acountLockReason", column = "ACCOUNT_LOCK_REASON"),
        @Result(property = "joinApprovalDateTime", column = "JOIN_APPROVAL_DATETIME"),
        @Result(property = "leaveDateTime", column = "LEAVE_DATETIME"),
        @Result(property = "createDateTime", column = "CREATE_DATETIME"),
        @Result(property = "createId", column = "CREATE_ID"),
        @Result(property = "createName", column = "CREATE_NAME"),
        @Result(property = "modifyDateTime", column = "MODIFY_DATETIME"),
        @Result(property = "modifyId", column = "MODIFY_ID"),
        @Result(property = "modifyName", column = "MODIFY_NAME"),
        @Result(property = "roleList", column="OPR_NUM", many=@Many(select = "selectOprRoleList")),
        @Result(property = "personInfoList", column="OPR_NUM", many=@Many(select = "selectOperatorPersonInfoList"))
    ])
    @Select("""<script>
        SELECT OPR_NUM, LOGIN_ID, LOGIN_PASSWD, OPR_NM, DEPT
            , POSITION, EXTENSION_PHONE_NUM, CELL_PHONE_NUM, EMAIL, NICKNAME
            , NOTE, OPR_STATE_CD, OPR_STATE_REASON, LOGIN_FAIL_CNT, ACCOUNT_LOCK_YN
            , ACCOUNT_LOCK_REASON
            , JOIN_APPROVAL_DATETIME, LEAVE_DATETIME, CREATE_ID, CREATE_DATETIME, MODIFY_ID, MODIFY_DATETIME 
            , ( SELECT OPR_NM FROM MC_OPR WHERE LOGIN_ID = T1.CREATE_ID ) AS CREATE_NAME
            , ( SELECT OPR_NM FROM MC_OPR WHERE LOGIN_ID = T1.MODIFY_ID ) AS MODIFY_NAME
        FROM MC_OPR T1
        WHERE OPR_STATE_CD <![CDATA[<>]]> 'MC_ROLE_STATE_03' /* 검색에서는 반려건 제외 기본 */
            <if test="operatorNumber!=null and operatorNumber>0">AND OPR_NUM = #{operatorNumber}</if>
            <if test="loginId!=null and loginId!=''">AND LOGIN_ID = #{loginId}</if>
    </script>""")
    public OperatorDetail selectOperator(Operator operator);
    
    @Results(id="roleResult", value = [
        @Result(property = "roleNumber", column = "ROLE_NUM"),
        @Result(property = "roleCode", column = "ROLE_CD"),
        @Result(property = "roleName", column = "ROLE_NM"),
        @Result(property = "useYn", column = "USE_YN"),
        @Result(property = "sortNumber", column = "SORT_NUM"),
    ])
    @Select("""<script>
        SELECT T1.ROLE_NUM, T2.ROLE_NM, T2.ROLE_CD, T2.USE_YN, T2.SORT_NUM
        FROM MC_OPR_ROLE T1, MC_ROLE T2
        WHERE T1.ROLE_NUM = T2.ROLE_NUM
            AND T2.DELETE_YN = 'N'
            AND T1.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
            AND T1.OPR_NUM = #{operatorNumber}
        ORDER BY T2.SORT_NUM
    </script>""")
    public List<Role> selectOprRoleList(Long operatorNumber);
    
    @Results(id="operatorPersonInfoResult", value = [
        @Result(property = "personInfoSectionCode", column = "PERSON_INFO_SECTION_CD"),
        @Result(property = "personInfoTreatCode", column = "PERSON_INFO_TREAT_CD")
    ])
    @Select("""<script>
        SELECT PERSON_INFO_SECTION_CD, PERSON_INFO_TREAT_CD
        FROM MC_OPR_PERSON_INFO
        WHERE OPR_NUM = #{operatorNumber}
            AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
    </script>""")
    public List<OperatorPersonInfo> selectOperatorPersonInfoList(Long operatorNumber);
    
    @Select("""<script>
        SELECT COUNT(OPR_NUM)
        FROM MC_OPR
        WHERE OPR_STATE_CD NOT IN ( 'MC_ROLE_STATE_03' ) /* 반려건은 재사용가능 */
            AND LOGIN_ID = #{loginId}
    </script>""")
    public int getLoginIdCount(String loginId);

    /* 등록 관련 */
    /* 회원상태코드를 정상으로 입력하는 경우 승인날짜도 함께 등록함*/
    @Insert("""<script>
        INSERT INTO MC_OPR(OPR_NUM, LOGIN_ID, LOGIN_PASSWD, OPR_NM, DEPT
            , POSITION, EXTENSION_PHONE_NUM, CELL_PHONE_NUM, EMAIL, NICKNAME
            , NOTE, OPR_STATE_CD, OPR_STATE_REASON, ACCOUNT_LOCK_YN
            <if test="operatorStateCode!=null and operatorStateCode.getCode().equals('MC_OPR_STATE_04')">, JOIN_APPROVAL_DATETIME</if>
            , CREATE_DATETIME, CREATE_ID)
        VALUES ( #{operatorNumber}, #{loginId}, #{loginPassword}, #{operatorName}, #{dept}
            , #{position}, #{extensionPhoneNumber}, #{cellPhoneNum}, #{email}, #{nickName}
            , #{note}, #{operatorStateCode}, #{operatorStateDesc}, 'N'
            <if test="operatorStateCode!=null and operatorStateCode.getCode().equals('MC_OPR_STATE_04')">, NOW()</if>
            , NOW(), #{createId} )
    </script>""")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="operatorNumber", before=false, resultType=Long.class)
    public int createOperator(CreateOperator saveOpr);
    
    @Insert("""<script>
        INSERT INTO MC_OPR_ROLE(OPR_NUM, ROLE_NUM, END_DATETIME, CREATE_DATETIME, CREATE_ID)
        SELECT T1.OPR_NUM, T2.ROLE_NUM, STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s'), NOW(), #{createId}
        FROM MC_OPR T1, MC_ROLE T2
        WHERE T1.OPR_NUM = #{operatorNumber}
            AND T2.DELETE_YN = 'N'
            AND T2.ROLE_NUM IN (
            <foreach collection="roleSelectList" item="item" separator=",">
                #{item.roleNumber}
            </foreach>
        )
        ON DUPLICATE KEY UPDATE END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
    </script>""")
    public int insertOprRoleList(CreateOperator saveOpr);
    
    @Insert("""<script>
        INSERT INTO MC_OPR_PERSON_INFO(OPR_NUM, PERSON_INFO_SECTION_CD, PERSON_INFO_TREAT_CD, END_DATETIME, CREATE_DATETIME, CREATE_ID)
        values
        <foreach collection="list" item="item" separator=",">
            (#{item.operatorNumber}, #{item.personInfoSectionCode}, #{item.personInfoTreatCode}, STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s'), NOW(), #{item.createId} )
        </foreach>
        ON DUPLICATE KEY UPDATE END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
    </script>""")
    public int insertOprPersonInfoList(List<OperatorPersonInfo> oprPersonInfo);
    

    /* 수정 관련 */
    @Update("""<script>
        UPDATE MC_PASSWD_HIST
        SET HIST_END_DATETIME = NOW()
            , MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
        WHERE HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
            AND OPR_NUM = #{operatorNumber} 
    </script>""")
    public int updatePasswdHist(ModifyOperator modifyOpr);
    
    @Insert("""<script>
        INSERT INTO MC_PASSWD_HIST(OPR_NUM, HIST_END_DATETIME, HIST_BEGIN_DATETIME, LOGIN_PASSWD, CREATE_DATETIME, CREATE_ID)
        SELECT OPR_NUM, STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f'), NOW(), LOGIN_PASSWD, NOW(), #{createId}
        FROM MC_OPR
        WHERE OPR_NUM = #{operatorNumber} 
    </script>""")
    public int insertPasswdHist(ModifyOperator modifyOpr);
    
    @Update("""<script>
        UPDATE MC_OPR_PERSON_INFO
        SET END_DATETIME = NOW()
        WHERE END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
            AND OPR_NUM = #{operatorNumber} 
            <if test="oprPersonInfoList!=null and oprPersonInfoList.size()>0">
            AND PERSON_INFO_TREAT_CD NOT IN (
                <foreach collection="oprPersonInfoList" item="item" separator=",">
                #{item.personInfoTreatCode}
                </foreach>
            )
            </if>
    </script>""")
    public int updateOprPersonInfoList(ModifyOperator modifyOpr);
    
    @Update("""<script>
        UPDATE MC_OPR_HIST
        SET HIST_END_DATETIME = NOW()
            , MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
        WHERE HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
            AND OPR_NUM = #{operatorNumber} 
    </script>""")
    public int updateOprHist(ModifyOperator modifyOpr);

    @Update("""<script>
        INSERT INTO MC_OPR_HIST( OPR_NUM, HIST_END_DATETIME, HIST_BEGIN_DATETIME, LOGIN_ID, LOGIN_PASSWD
            , OPR_NM, DEPT, POSITION, EXTENSION_PHONE_NUM, CELL_PHONE_NUM
            , EMAIL, NICKNAME, NOTE, OPR_STATE_CD, OPR_STATE_REASON
            , LOGIN_FAIL_CNT, ACCOUNT_LOCK_YN, ACCOUNT_LOCK_REASON
            , JOIN_APPROVAL_DATETIME, CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID, LEAVE_DATETIME )
        SELECT OPR_NUM, STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f'), NOW(), LOGIN_ID, LOGIN_PASSWD
            , OPR_NM, DEPT, POSITION, EXTENSION_PHONE_NUM, CELL_PHONE_NUM
            , EMAIL, NICKNAME, NOTE, OPR_STATE_CD, OPR_STATE_REASON
            , LOGIN_FAIL_CNT, ACCOUNT_LOCK_YN, ACCOUNT_LOCK_REASON
            , JOIN_APPROVAL_DATETIME, CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID, LEAVE_DATETIME
        FROM MC_OPR
        WHERE OPR_NUM = #{operatorNumber} 
    </script>""")
    public int insertOprHist(Long operatorNumber);

    @Update("""<script>
        UPDATE MC_OPR
        SET MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
            , LOGIN_ID = #{loginId}
            , OPR_NM = #{operatorName}
            , DEPT = #{dept}
            , POSITION = #{position}
            , EXTENSION_PHONE_NUM = #{extensionPhoneNumber}
            , CELL_PHONE_NUM = #{cellPhoneNum}
            , EMAIL = #{email}
            , NICKNAME = #{nickName}
            , NOTE = #{note}
            , ACCOUNT_LOCK_YN = #{acountLockYn}
            <if test="loginPassword!=null and loginPassword!=''">, LOGIN_PASSWD = #{loginPassword}</if>
            <if test="operatorStateCode!=null and !operatorStateCode.getCode().equals('')">
            , OPR_STATE_CD = #{operatorStateCode}
                <if test="operatorStateCode.getCode().equals('MC_OPR_STATE_04')">
                , JOIN_APPROVAL_DATETIME = IFNULL(JOIN_APPROVAL_DATETIME, NOW())
                </if>
            </if>
            <if test="operatorStateDesc!=null and operatorStateDesc!=''">, OPR_STATE_REASON = #{operatorStateDesc}</if>
            <if test="!acountLockYn">, LOGIN_FAIL_CNT = null</if>
        WHERE OPR_NUM = #{operatorNumber} 
    </script>""")
    public int updateOperator(ModifyOperator saveOpr);

    @Update("""<script>
        UPDATE MC_OPR
        SET MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
            , OPR_STATE_CD = #{operatorStateCode}
            <if test="operatorStateDesc!=null and operatorStateDesc!=''">, OPR_STATE_REASON = #{operatorStateDesc}</if>
        WHERE OPR_NUM = #{operatorNumber} 
    </script>""")
    public int updateOperatorApprove(ModifyOperator saveOpr);

    /* 삭제 관련 */
    @Update("""<script>
        UPDATE MC_OPR_PERSON_INFO
        SET END_DATETIME = NOW()
            , MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
        WHERE END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
            AND OPR_NUM = #{operatorNumber} 
    </script>""")
    public int removeOprPersonInfoList(ModifyOperator modifyOpr);

    @Update("""<script>
        UPDATE MC_OPR_ROLE
        SET END_DATETIME = NOW()
            , MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
        WHERE END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
            AND OPR_NUM = #{operatorNumber} 
            <if test="roleSelectList!=null and roleSelectList.size()>0">
            AND ROLE_NUM NOT IN (
                <foreach collection="roleSelectList" item="role" separator=",">
                #{role.roleNumber}
                </foreach>
            )
            </if>
    </script>""")
    public int removeOprRoleList(ModifyOperator modifyOpr);

    @Update("""<script>
        UPDATE MC_OPR
        SET OPR_STATE_CD = #{operatorStateCode}
            , OPR_STATE_REASON = #{operatorStateDesc}
            , MODIFY_DATETIME = NOW()
            , LEAVE_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
        WHERE OPR_NUM = #{operatorNumber} 
    </script>""")
    public int removeOperator(ModifyOperator saveOpr);
    
    @ResultMap("roleResult")
    @Select("""<script>
        SELECT ROLE_NUM, ROLE_NM, ROLE_CD
        FROM MC_ROLE
        WHERE DELETE_YN = 'N'
            AND ROLE_NUM NOT IN (
                <foreach collection="roleList" item="role" separator=",">
                #{role.roleNumber}
                </foreach>
            )
    </script>""")
    public List<Role> selectRoleInfoInfoList(@Param("roleList") List<Role> roleList);
    
    @Update("""<script>
        UPDATE MC_OPR
        SET MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
            , LOGIN_PASSWD = #{loginPassword}
        WHERE OPR_NUM = #{operatorNumber} 
            AND LOGIN_ID = #{loginId}
    </script>""")
    public int updateOperatorPassword(ResetOperatorPassword passwordModel);

    @Select("""
        SELECT OO.OPR_NUM
        FROM MC_OPR AS OO
        INNER JOIN MC_OPR_ROLE AS OOR ON OO.OPR_NUM = OOR.OPR_NUM
        INNER JOIN MC_ROLE AS ORO ON OOR.ROLE_NUM = ORO.ROLE_NUM
        WHERE OOR.END_DATETIME = '9999-12-31 23:59:59' AND ORO.ROLE_CD = #{operatorGroup}
        GROUP BY OO.OPR_NUM
    """)
    public List<Long> selectOperatorListByGroupCode(final @Param("operatorGroup") OperatorGroup operatorGroup);

}