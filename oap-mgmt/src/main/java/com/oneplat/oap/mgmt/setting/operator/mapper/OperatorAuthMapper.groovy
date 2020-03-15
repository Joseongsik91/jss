package com.oneplat.oap.mgmt.setting.operator.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

import com.oneplat.oap.mgmt.setting.operator.model.OperatorAuth

@Mapper
public interface OperatorAuthMapper {

    @Results(id="operatorResult", value = [
        @Result(property = "operatorNumber", column = "OPR_NUM"),
        @Result(property = "authSectionCode", column = "AUTH_SECTION_CD"),
        @Result(property = "issueDatetime", column = "ISSUE_DATETIME"),
        @Result(property = "authNumber", column = "AUTH_NUM"),
        @Result(property = "authValidDatetime", column = "AUTH_VALID_DATETIME"),
        
    ])
    @Select("""<script>
        SELECT OPR_NUM, AUTH_SECTION_CD, ISSUE_DATETIME, AUTH_NUM, AUTH_VALID_DATETIME, CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID 
        FROM MC_AUTH
        WHERE 
            OPR_NUM = #{operatorNumber} AND AUTH_VALID_DATETIME>NOW() AND AUTH_SECTION_CD = 'MC_AUTH_SECTION_01'
    </script>""")
    public OperatorAuth selectOperatorAuth(Long operatorNumber);
    
    @Select("""<script>
        SELECT COUNT(*)
        FROM MC_AUTH
        WHERE 
            OPR_NUM = #{operatorNumber} AND AUTH_VALID_DATETIME>NOW() AND AUTH_SECTION_CD = 'MC_AUTH_SECTION_01'
    </script>""")
    public int selectOperatorAuthCnt(Long operatorNumber);

    @Update("""<script>
        INSERT INTO MC_AUTH(
           OPR_NUM
          ,AUTH_SECTION_CD
          ,ISSUE_DATETIME
          ,AUTH_NUM
          ,AUTH_VALID_DATETIME
          ,CREATE_DATETIME
          ,CREATE_ID
          ,MODIFY_DATETIME
          ,MODIFY_ID
        ) VALUES (
            #{operatorNumber}
            ,#{authSectionCode}
            ,NOW()
            ,#{authNumber}
            ,DATE_ADD(NOW(), INTERVAL 3 MINUTE)
            ,NOW()
            ,#{createId}
            ,NOW()
            ,#{modifyId}
        )
    </script>""")
    public int insertOperatorAuth(OperatorAuth operatorAuth);
}