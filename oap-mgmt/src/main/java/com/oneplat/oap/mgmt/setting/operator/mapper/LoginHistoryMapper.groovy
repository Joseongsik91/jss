package com.oneplat.oap.mgmt.setting.operator.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select

import com.oneplat.oap.core.model.SearchRequest
import com.oneplat.oap.mgmt.setting.operator.model.LoginHistory

@Mapper
public interface LoginHistoryMapper {

    @Results(id="historyResult", value = [
        @Result(property = "operatorNumber", column = "OPR_NUM"),
        @Result(property = "loginId", column = "LOGIN_ID"),
        @Result(property = "operatorName", column = "OPR_NM"),
        //@Result(property = "loginDateTime, ", column = "LOGIN_DATETIME"),
        @Result(property = "accessIp4Addr", column = "ACCESS_IP4_ADDR"),
        @Result(property = "insideYn", column = "INSIDE_YN"),
        @Result(property = "createDateTime", column = "CREATE_DATETIME"),
        @Result(property = "createId", column = "CREATE_ID")
    ])
    @Select("""<script>
        SELECT T1.OPR_NUM, T2.LOGIN_ID, T2.OPR_NM, ACCESS_IP4_ADDR, INSIDE_YN, T1.CREATE_DATETIME, T1.CREATE_ID, T1.LOGIN_DATETIME
        FROM (
            SELECT OPR_NUM, LOGIN_DATETIME, ACCESS_IP4_ADDR, INSIDE_YN, CREATE_DATETIME, CREATE_ID
            FROM MC_LOGIN_HIST
            WHERE LOGIN_DATETIME <![CDATA[>]]> DATE_ADD(CURDATE(), INTERVAL -30 DAY)
        ) T1, MC_OPR T2
        WHERE T1.OPR_NUM = T2.OPR_NUM
        <choose>
        <when test="query!=null">
            <if test="query.searchName!=null and query.searchName!=''">
                <if test="query.searchType!=null and query.searchType=='ID'">AND T2.LOGIN_ID LIKE CONCAT('%',#{query.searchName},'%')</if>
                <if test="query.searchType!=null and query.searchType=='NAME'">AND T2.OPR_NM LIKE CONCAT('%',#{query.searchName},'%')</if>
            </if>
            <if test="query.insideYn!=null and query.insideYn!=''"> AND INSIDE_YN = #{query.insideYn}</if>
            <if test="query.startDate!=null and query.startDate!=''"> AND 	T1.CREATE_DATETIME <![CDATA[>=]]> STR_TO_DATE(concat(#{query.startDate}, "000001"),"%Y%m%d%H%i%S")</if>
            <if test="query.endDate!=null and query.endDate!=''"> AND T1.CREATE_DATETIME <![CDATA[<=]]> STR_TO_DATE(concat(#{query.endDate}, "235959"),"%Y%m%d%H%i%S")</if>
            <choose>
                <when test="query.sortField!=null and query.sortField!=''">
                ORDER BY \${query.sortField} <if test="query.orderBy!=null and query.orderBy!=''"> \${query.orderBy} </if>
                </when>
                <otherwise>
                ORDER BY T1.CREATE_DATETIME DESC
                </otherwise>
            </choose>
            <if test="pageInfo!=null">
            LIMIT #{pageInfo.startRowNum}, #{pageInfo.size}
            </if>
        </when>
        <otherwise>
        ORDER BY T1.CREATE_DATETIME DESC
        </otherwise>
        </choose>
    </script>""")
    public List<LoginHistory> selectLoginHistoryList(SearchRequest searchRequest);
    
    @Select("""<script>
        SELECT COUNT(T1.OPR_NUM)
        FROM (
            SELECT OPR_NUM, LOGIN_DATETIME, ACCESS_IP4_ADDR, INSIDE_YN, CREATE_DATETIME, CREATE_ID
            FROM MC_LOGIN_HIST
            WHERE LOGIN_DATETIME <![CDATA[>]]> DATE_ADD(CURDATE(), INTERVAL -30 DAY)
        ) T1, MC_OPR T2
        WHERE T1.OPR_NUM = T2.OPR_NUM
        <if test="query!=null">
            <if test="query.searchName!=null and query.searchName!=''">
                <if test="query.searchType!=null and query.searchType=='ID'">AND T2.LOGIN_ID LIKE CONCAT('%',#{query.searchName},'%')</if>
                <if test="query.searchType!=null and query.searchType=='NAME'">AND T2.OPR_NM LIKE CONCAT('%',#{query.searchName},'%')</if>
            </if>
            <if test="query.insideYn!=null and query.insideYn!=''"> AND INSIDE_YN = #{query.insideYn}</if>
            <if test="query.startDate!=null and query.startDate!=''"> AND LOGIN_DATETIME <![CDATA[>=]]> STR_TO_DATE(concat(#{query.startDate}, "000001"),"%Y%m%d%H%i%S")</if>
            <if test="query.endDate!=null and query.endDate!=''"> AND LOGIN_DATETIME <![CDATA[<=]]> STR_TO_DATE(concat(#{query.endDate}, "235959"),"%Y%m%d%H%i%S")</if>
        </if>
    </script>""")
    public int getLoginHistoryListCount(SearchRequest searchRequest);
    
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
            ,#{loginDateTime}
            ,#{accessIp4Addr}
            ,#{insideYn}
            ,NOW()
            ,#{createId}
            ,NOW()
            ,#{modifyId}
        )
    </script>""")
    public int insertLoginHistory(LoginHistory);

}