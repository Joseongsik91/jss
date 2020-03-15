package com.oneplat.oap.mgmt.setting.admin.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Many
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectKey
import org.apache.ibatis.annotations.Update
import com.oneplat.oap.core.model.SearchRequest
import com.oneplat.oap.mgmt.setting.admin.model.OperatorRole
import com.oneplat.oap.mgmt.setting.admin.model.Role
import com.oneplat.oap.mgmt.setting.admin.model.OperatorRole.SaveOperatorRole
import com.oneplat.oap.mgmt.setting.admin.model.Role.CreateRole
import com.oneplat.oap.mgmt.setting.admin.model.Role.ModifyRole
import com.oneplat.oap.mgmt.setting.admin.model.Role.RoleOpr
import com.oneplat.oap.mgmt.setting.admin.model.Role.RolePersonInfo
import com.oneplat.oap.mgmt.setting.admin.model.Role.RoleSearch

@Mapper
public interface GroupManagementMapper{

    @Results(id="roleResult", value = [
        @Result(property = "roleNumber", column = "ROLE_NUM"),
        @Result(property = "roleName", column = "ROLE_NM"),
        @Result(property = "roleCode", column = "ROLE_CD"),
        @Result(property = "sortNumber", column = "SORT_NUM"),
        @Result(property = "useYn", column = "USE_YN"),
        @Result(property = "roleDesc", column = "ROLE_DESC"),
        @Result(property = "customerInfoTreatYn", column = "CUSTOMER_INFO_TREAT_YN"),
        @Result(property = "sellerInfoTreatYn", column = "SELLER_INFO_TREAT_YN"),
        @Result(property = "createDateTime", column = "CREATE_DATETIME"),
        @Result(property = "createId", column = "CREATE_ID"),
        @Result(property = "modifyDateTime", column = "MODIFY_DATETIME"),
        @Result(property = "modifyId", column = "MODIFY_ID")
    ])
    
    @ResultMap("roleDetailResult")
    @Select("""<script>
        SELECT ROLE_NUM, ROLE_CD, ROLE_NM, SORT_NUM, USE_YN
              , ROLE_DESC, CUSTOMER_INFO_TREAT_YN, SELLER_INFO_TREAT_YN
              , CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID
        FROM MC_ROLE
        WHERE DELETE_YN = 'N'
        <if test="query!=null and query.searchWord!=null and query.searchWord!=''">
            AND ROLE_NM LIKE CONCAT('%',#{query.searchWord},'%') </if>
        ORDER BY SORT_NUM
    </script>""")
    public List<Role> selectRoleList(SearchRequest searchRequest);
	
	
	//업무 목록중에서 미사용 제외
	@ResultMap("roleDetailResult")
	@Select("""<script>
        SELECT ROLE_NUM, ROLE_CD, ROLE_NM, SORT_NUM, USE_YN
              , ROLE_DESC, CUSTOMER_INFO_TREAT_YN, SELLER_INFO_TREAT_YN
              , CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID
        FROM MC_ROLE
        WHERE DELETE_YN = 'N'
		AND USE_YN = 'Y'
        <if test="query!=null and query.searchWord!=null and query.searchWord!=''">
            AND ROLE_NM LIKE CONCAT('%',#{query.searchWord},'%') </if>
        ORDER BY SORT_NUM
    </script>""")
	public List<Role> selectUseRoleList(SearchRequest searchRequest);
    
    @Results(id="roleDetailResult", value = [
        @Result(property = "roleNumber", column = "ROLE_NUM"),
        @Result(property = "roleName", column = "ROLE_NM"),
        @Result(property = "roleCode", column = "ROLE_CD"),
        @Result(property = "sortNumber", column = "SORT_NUM"),
        @Result(property = "useYn", column = "USE_YN"),
        @Result(property = "roleDesc", column = "ROLE_DESC"),
        @Result(property = "customerInfoTreatYn", column = "CUSTOMER_INFO_TREAT_YN"),
        @Result(property = "sellerInfoTreatYn", column = "SELLER_INFO_TREAT_YN"),
        @Result(property = "createDateTime", column = "CREATE_DATETIME"),
        @Result(property = "createId", column = "CREATE_ID"),
        @Result(property = "modifyDateTime", column = "MODIFY_DATETIME"),
        @Result(property = "modifyId", column = "MODIFY_ID")
    ])
    @Select("""<script>
        SELECT
			ROLE_NUM,
			ROLE_NM, 
			ROLE_CD, 
			SORT_NUM, 
			USE_YN, 
			ROLE_DESC, 
			CUSTOMER_INFO_TREAT_YN, 
			SELLER_INFO_TREAT_YN, 
			CREATE_DATETIME, 
			CREATE_ID, 
			MODIFY_DATETIME, 
			MODIFY_ID
        FROM MC_ROLE
        WHERE DELETE_YN = 'N'
            AND ROLE_NUM = #{roleNumber}
    </script>""")
    public Role selectRole(Long roleNumber);
	
    
    @Results(id="rolePersonInfoResult", value = [
        @Result(property = "roleNumber", column = "ROLE_NUM"),
        @Result(property = "personInfoSectionCode", column = "PERSON_INFO_SECTION_CD"),
        @Result(property = "personInfoTreatCode", column = "PERSON_INFO_TREAT_CD")
    ])
    @Select("""<script>
        SELECT ROLE_NUM, PERSON_INFO_SECTION_CD, PERSON_INFO_TREAT_CD
        FROM MC_ROLE_PERSON_INFO
        WHERE ROLE_NUM = #{roleNumber}
            AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
    </script>""")
    public List<RolePersonInfo> selectRolePersonInfoList(Long roleNumber);
    
    @Select("""<script>
        SELECT COUNT(ROLE_NUM)
        FROM MC_ROLE
        <where>
            <if test="roleNumber!=null and roleNumber>0"> ROLE_NUM = #{roleNumber} </if>
            <if test="roleName!=null and roleName!=''"> AND ROLE_NM = #{roleName} </if>
            <if test="roleCode!=null and roleCode!=''"> AND ROLE_CD = #{roleCode} </if>
        </where>
    </script>""")
    public int getRoleCount(RoleSearch role);

    @Select("""<script>
        SELECT COUNT(ROLE_NUM)
        FROM MC_ROLE
        <where>
            <if test="query!=null">
				<if test="query.searchName!=null and query.searchName!=''">
                	<if test="query.searchType!=null and query.searchType=='CD'">
						AND ROLE_CD = #{query.searchName}
					</if>
                	<if test="query.searchType!=null and query.searchType=='NAME'">
						AND ROLE_NM = #{query.searchName}
					</if>
            	</if>
			</if>
        </where>
    </script>""")
    public int checkDuplication(SearchRequest searchRequest);

    
    @Insert("""<script>
        INSERT INTO MC_ROLE(
			ROLE_NM, 
			ROLE_CD, 
			ROLE_DESC, 
			CUSTOMER_INFO_TREAT_YN , 
			SELLER_INFO_TREAT_YN, 
			USE_YN, 
			DELETE_YN, 
			CREATE_DATETIME, 
			CREATE_ID,
			MODIFY_DATETIME,
			MODIFY_ID,
			SORT_NUM)
        VALUES(
			#{roleName}, 
			#{roleCode}, 
			#{roleDesc}, 
			'Y', 
			'Y',
            #{useYn},
			'N', 
			NOW(6), 
			#{createId},
			NOW(6),
			#{createId},
			(SELECT COUNT(M.ROLE_NUM)+1 FROM MC_ROLE M WHERE M.DELETE_YN = 'N'))
    </script>""")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="roleNumber", before=false, resultType=Long.class)
    public int insertRole(CreateRole role);
    
    @Update("""<script>
        UPDATE MC_ROLE SET 
			ROLE_NM = #{roleName},
            ROLE_DESC = #{roleDesc},
            USE_YN = #{useYn},
            MODIFY_DATETIME = NOW(6),
            MODIFY_ID = #{modifyId}
        WHERE ROLE_NUM = #{roleNumber}
    </script>""")
    public int updateRole(Role role);
    
    @Update("""<script>
        UPDATE MC_ROLE
        SET DELETE_YN = 'Y'
            , MODIFY_DATETIME = NOW(6)
            , MODIFY_ID = #{modifyId}
        WHERE ROLE_NUM = #{roleNumber}
    </script>""")
    public int deleteRole(Role role);

    @Update("""<script>
        UPDATE MC_ROLE_HIST SET 
			HIST_END_DATETIME = NOW(6),
			MODIFY_DATETIME = NOW(6),
            MODIFY_ID = #{modifyId}
        WHERE ROLE_NUM = #{roleNumber}
            AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
    public int updateRoleHist(Role role);
    
    @Insert("""<script>
        INSERT INTO MC_ROLE_HIST(
			ROLE_NUM, HIST_END_DATETIME, HIST_BEGIN_DATETIME, 
			ROLE_NM, ROLE_CD, ROLE_DESC, 
			SORT_NUM, CUSTOMER_INFO_TREAT_YN, SELLER_INFO_TREAT_YN, 
			USE_YN, DELETE_YN, CREATE_DATETIME, 
			CREATE_ID, MODIFY_DATETIME, MODIFY_ID)
        SELECT
			ROLE_NUM, STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f'), NOW(6), 
			ROLE_NM, ROLE_CD, ROLE_DESC, 
			SORT_NUM, CUSTOMER_INFO_TREAT_YN, SELLER_INFO_TREAT_YN, 
			USE_YN, DELETE_YN, NOW(6), 
			#{createId}, NOW(6), #{createId}
        FROM MC_ROLE
        WHERE ROLE_NUM = #{roleNumber}
    </script>""")
    public int insertRoleHist(Role role);
    
	@Results(id="sortResult", value = [
		@Result(property = "roleNumber", column = "ROLE_NUM"),
		@Result(property = "sortNumber", column = "SORT_NUM")
	])
	@Select("""<script>
		SELECT
			ROLE_NUM , SORT_NUM 
		FROM MC_ROLE 
		WHERE DELETE_YN = 'N'
			AND ROLE_NUM NOT IN (#{roleNumber})
		ORDER BY SORT_NUM
	</script>""")
	public List<Role> getRoleSortList(Role role);
	
    @Update("""<script>
        UPDATE MC_ROLE
        SET MODIFY_DATETIME = NOW(6)
            , MODIFY_ID = #{modifyId}
            , SORT_NUM = #{sortNumber}
        WHERE DELETE_YN = 'N' 
        AND ROLE_NUM = #{roleNumber}
    </script>""")
    public int updateSortNumber(Role role);
    
    @Insert("""<script>
        INSERT INTO MC_ROLE_PERSON_INFO(ROLE_NUM, PERSON_INFO_SECTION_CD, PERSON_INFO_TREAT_CD, END_DATETIME, CREATE_DATETIME, CREATE_ID)
        values
        <foreach collection="list" item="item" separator=",">
            (#{item.roleNumber}, #{item.personInfoSectionCode}, #{item.personInfoTreatCode}, STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s'), NOW(), #{item.createId} )
        </foreach>
        ON DUPLICATE KEY UPDATE END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
    </script>""")
    public int insertRolePersonInfoList(List<RolePersonInfo> rolePersonInfo);
    
    @Update("""<script>
        UPDATE MC_ROLE_PERSON_INFO
        SET END_DATETIME = NOW()
            , MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
        WHERE ROLE_NUM = #{roleNumber}
            AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
            <if test="customPersonInfoCheckedList!=null or sellerPersonInfoCheckedList!=null or managerPersonInfoCheckedList!=null">
            AND (
                <if test="customPersonInfoCheckedList!=null">
                ( PERSON_INFO_SECTION_CD = #{customPersonInfoCheckedList[0].personInfoSectionCode} AND PERSON_INFO_TREAT_CD NOT IN (
                    <foreach collection="customPersonInfoCheckedList" item="item" separator=",">
                    #{item.personInfoTreatCode}
                    </foreach>
                    ) )
                </if>
                <if test="sellerPersonInfoCheckedList!=null">
                   <if test="customPersonInfoCheckedList!=null"> OR </if>
                ( PERSON_INFO_SECTION_CD = #{sellerPersonInfoCheckedList[0].personInfoSectionCode} AND PERSON_INFO_TREAT_CD NOT IN (
                    <foreach collection="sellerPersonInfoCheckedList" item="item" separator=",">
                    #{item.personInfoTreatCode}
                    </foreach>
                    ) )
                </if>
                <if test="managerPersonInfoCheckedList!=null">
                   <if test="customPersonInfoCheckedList!=null or sellerPersonInfoCheckedList!=null"> OR </if>
                ( PERSON_INFO_SECTION_CD = #{managerPersonInfoCheckedList[0].personInfoSectionCode} AND PERSON_INFO_TREAT_CD NOT IN (
                    <foreach collection="managerPersonInfoCheckedList" item="item" separator=",">
                    #{item.personInfoTreatCode}
                    </foreach>
                    ) )
                </if>
            )
            </if>
    </script>""")
    public int updateRolePersonInfoOldList(ModifyRole modifyRole);
    
    @Update("""<script>
        UPDATE MC_ROLE_PERSON_INFO
        SET END_DATETIME = NOW()
            , MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
        WHERE END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
            AND ROLE_NUM = #{roleNumber}
    </script>""")
    public int deleteRolePersonInfo(Role role);
    
    @Results(id="roleDetailOprResult", value = [
        @Result(property = "roleNumber", column = "ROLE_NUM"),
        @Result(property = "roleCode", column = "ROLE_CD"),
        @Result(property = "roleName", column = "ROLE_NM"),
        @Result(property = "useYn", column = "USE_YN"),
        @Result(property = "oprCount", column = "COUNT"),
        @Result(property = "oprList", column="ROLE_NUM", many=@Many(select = "selectOprRoleList"))
    ])
    @Select("""<script>
        SELECT ROLE_NUM, ROLE_CD, ROLE_NM, USE_YN
            , ( SELECT COUNT(OPR_NUM) FROM MC_OPR_ROLE WHERE T1.ROLE_NUM = ROLE_NUM AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s') ) AS COUNT
        FROM MC_ROLE T1
        WHERE DELETE_YN = 'N'
            AND ROLE_NUM = #{roleNumber}
    </script>""")
    public RoleOpr selectRoleOpr(Long roleNumber);

    @Select("""<script>
		SELECT
			COUNT(MOR.OPR_NUM)
		FROM
        	MC_ROLE MR
            LEFT JOIN MC_OPR_ROLE MOR ON MR.ROLE_NUM = MOR.ROLE_NUM
		WHERE MR.DELETE_YN = 'N'
            AND MOR.END_DATETIME > NOW()
            AND MR.ROLE_NUM = #{roleNumber}
    </script>""")
    public int selectRoleOprCount(Long roleNumber);

    @Results(id="roleOprResult", value = [
        @Result(property = "oprNumber", column = "OPR_NUM"),
        @Result(property = "oprName", column = "OPR_NM"),
        @Result(property = "loginId", column = "LOGIN_ID"),
        @Result(property = "email", column = "EMAIL"),
        @Result(property = "cellPhoneNumber", column = "CELL_PHONE_NUM"),
        @Result(property = "dept", column = "DEPT"),
        @Result(property = "position", column = "POSITION"),
        @Result(property = "roleName", column = "ROLE_NM")
    ])
    @Select("""<script>
        SELECT T1.OPR_NUM
            , T3.OPR_NM, T3.LOGIN_ID, T3.OPR_NM, T3.EMAIL, T3.DEPT
            , T3.POSITION, T3.CELL_PHONE_NUM
            , GROUP_CONCAT(T2.ROLE_NM) AS ROLE_NM
        FROM MC_OPR_ROLE T1, MC_ROLE T2, MC_OPR T3
        WHERE T1.ROLE_NUM = T2.ROLE_NUM
        AND T1.OPR_NUM = T3.OPR_NUM
        AND T3.OPR_STATE_CD = 'MC_OPR_STATE_04'
        AND T2.DELETE_YN = 'N'
        AND T1.ROLE_NUM = #{roleNumber}
        AND T1.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
        GROUP BY T1.OPR_NUM
    </script>""")
    public List<OperatorRole> selectOprRoleList(Long roleNumber);

    @ResultMap("roleOprResult")
    @Select("""<script>
        SELECT T1.OPR_NUM, T3.OPR_NM, T3.LOGIN_ID, GROUP_CONCAT(T2.ROLE_NM) AS ROLE_NM
        FROM MC_OPR_ROLE T1, MC_ROLE T2, MC_OPR T3
        WHERE T1.ROLE_NUM = T2.ROLE_NUM
        AND T1.OPR_NUM = T3.OPR_NUM
        AND T3.OPR_STATE_CD = 'MC_OPR_STATE_04'
        AND T2.DELETE_YN = 'N'
        AND T1.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
        <if test="query!=null">
            <if test="query.searchType!=null"> AND T2.ROLE_NUM = #{query.searchType} </if>
            <if test="query.searchName!=null and query.searchName!=''"> AND T3.OPR_NM = #{query.searchName} </if>
            <if test="query.searchList!=null">
                AND T1.OPR_NUM IN (
                    <foreach collection="query.searchList" item="item" separator=",">
                    #{item.value}
                    </foreach>
                )
            </if>
        </if>
        GROUP BY T1.OPR_NUM
    </script>""")
    public List<OperatorRole> selectOprRoleSearchList(SearchRequest searchRequest);
    
    @ResultMap("roleOprResult")
    @Select("""<script>
        SELECT OPR_NUM, OPR_NM, LOGIN_ID, GROUP_CONCAT(ROLE_NM) AS ROLE_NM
        FROM (
            SELECT TT1.OPR_NUM, TT1.OPR_NM, TT1.LOGIN_ID, TT2.ROLE_NUM, TT2.ROLE_NM, TT2.ROLE_STATE_CD
            FROM MC_OPR TT1 LEFT JOIN (
                SELECT T1.ROLE_NUM, T1.ROLE_NM, T2.ROLE_STATE_CD, T2.OPR_NUM
                FROM MC_ROLE T1, MC_OPR_ROLE T2
                WHERE T1.ROLE_NUM = T2.ROLE_NUM
                    /* AND T2.ROLE_STATE_CD = 'MC_ROLE_STATE_04' */
                    AND T1.DELETE_YN = 'N'
                    AND T2.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
            ) AS TT2 ON TT1.OPR_NUM = TT2.OPR_NUM 
            WHERE TT1.OPR_STATE_CD = 'MC_OPR_STATE_04'
        ) A
        <if test="query!=null">
        <where>
            <if test="query.roleNumber!=null and query.roleNumber!=''"> OPR_NUM IN ( 
                SELECT OPR_NUM FROM MC_OPR_ROLE
                WHERE ROLE_NUM = #{query.roleNumber}
                    AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s') ) 
            </if>
            <if test="query.searchName!=null and query.searchName!=''"> AND OPR_NM LIKE CONCAT('%',#{query.searchName},'%') </if>
            <if test="query.searchList!=null">
                AND OPR_NUM IN (
                    <foreach collection="query.searchList" item="item" separator=",">
                    #{item.value}
                    </foreach>
                )
            </if>
        </where>
        </if>
        GROUP BY OPR_NUM
    </script>""")
    public List<OperatorRole> selectOprRolesSearchList(SearchRequest searchRequest);
    
    @Update("""<script>
        UPDATE MC_OPR_ROLE
        SET END_DATETIME = NOW()
            , MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
        WHERE ROLE_NUM = #{roleNumber}
            AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
            <if test="oprNumberList!=null">
            AND OPR_NUM IN (
                    <foreach collection="oprNumberList" item="item" separator=",">
                    #{item.value}
                    </foreach>
                )
            </if>
    </script>""")
    public int updateOprRoleList(SaveOperatorRole createOprRole);
    
    @Insert("""<script>
        INSERT INTO MC_OPR_ROLE(OPR_NUM, ROLE_NUM, END_DATETIME, CREATE_DATETIME, CREATE_ID)
        SELECT T1.OPR_NUM, T2.ROLE_NUM, STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s'), NOW(), #{createId}
        FROM MC_OPR T1, MC_ROLE T2
        WHERE T2.ROLE_NUM = #{roleNumber}
            AND T2.DELETE_YN = 'N'
            AND T1.OPR_NUM IN (
            <foreach collection="oprNumberList" item="item" separator=",">
                #{item.value}
            </foreach>
        )
        ON DUPLICATE KEY UPDATE END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
    </script>""")
    public int insertOprRoleForOprList(SaveOperatorRole createOprRole);
    
}