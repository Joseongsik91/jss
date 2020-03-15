package com.oneplat.oap.mgmt.login.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Many
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

import com.oneplat.oap.mgmt.login.model.LoginOperator
import com.oneplat.oap.mgmt.setting.admin.model.Role

@Mapper
public interface LoginOperatorMapper {

    @Results(id="loginOperatorDetailRsult", value = [
        @Result(property = "operatorNumber", column = "OPR_NUM"),
        @Result(property = "loginId", column = "LOGIN_ID"),
        @Result(property = "loginPassword", column = "LOGIN_PASSWD"),
        @Result(property = "operatorName", column = "OPR_NM"),
        @Result(property = "loginFailCount", column = "LOGIN_FAIL_CNT"),
        @Result(property = "operatorStateCode", column = "OPR_STATE_CD"),
        @Result(property = "roleList", column="OPR_NUM", many=@Many(select = "selectOprRoleList"))
    ])
    @Select("""<script>
        SELECT OPR_NUM, LOGIN_ID, LOGIN_PASSWD, OPR_NM, LOGIN_FAIL_CNT, ACCOUNT_LOCK_YN, OPR_STATE_CD
        FROM MC_OPR
        WHERE LOGIN_ID = #{loginId}
    </script>""")
    public LoginOperator selectLoginOperator(String loginId);
    
    @Update("""<script>
        UPDATE MC_OPR
        SET 
            <if test="loginFailCount!=null">LOGIN_FAIL_CNT = #{loginFailCount}</if>
            <if test="operatorStateCode!=null and operatorStateCode!=''">, OPR_STATE_CD = #{operatorStateCode}</if>
            <if test="acountLockYn!=null and acountLockYn!=''">, ACCOUNT_LOCK_YN = #{acountLockYn}</if>
        WHERE OPR_NUM = #{operatorNumber} 
    </script>""")
	public int updateOperatorLoginInfo(LoginOperator loginOperator);
	@Insert("""<script>
		        INSERT INTO MC_LOGIN_HIST(
		           OPR_NUM
		          ,LOGIN_DATETIME
		          ,ACCESS_IP4_ADDR
		          ,INSIDE_YN
		          ,CREATE_DATETIME
		          ,CREATE_ID
		          ,MODIFY_DATETIME
		          ,MODIFY_ID
		        ) VALUES (
		            #{operatorNumber}
		            ,NOW()
		            ,#{accessIp4Addr}
		            ,#{insideYn}
		            ,NOW()
		            ,#{createId}
		            ,NOW()
		            ,#{modifyId}
		        )
		    </script>""")
	public int insertLoginHistory(LoginHistory);
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

}