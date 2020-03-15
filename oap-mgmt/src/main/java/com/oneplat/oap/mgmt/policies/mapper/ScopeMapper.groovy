package com.oneplat.oap.mgmt.policies.mapper

import com.oneplat.oap.core.model.SearchRequest
import com.oneplat.oap.mgmt.policies.model.Scope
import com.oneplat.oap.mgmt.policies.model.ScopeApi

import java.util.List;

import org.apache.ibatis.annotations.*

/**
 * Created by Hong Gi Seok on 2017-02-07.
 */
@Mapper
interface ScopeMapper {
    /** Scope 등록 */
    @Insert("""
    <script>
        INSERT INTO MC_SCOPE (
           SCOPE_NM
          ,SCOPE_CONTEXT
          ,ICON_FILE_CHANNEL
          ,SCOPE_DESC
          ,SCOPE_USE_YN
          ,SCOPE_DELETE_YN
          ,CREATE_DATETIME
          ,CREATE_ID
          ,MODIFY_DATETIME
          ,MODIFY_ID
          ,SVC_NUM
        ) VALUES (
           #{scopeName}
          ,#{scopeContext}
          ,#{iconFileChannel}
          ,#{scopeDescription}
          ,#{scopeUseYn}
          ,'N'
          ,NOW(6)
          ,#{createId}
          ,NOW(6)
          ,#{modifyId}
          ,#{serviceNumber}
        )
    </script>
    """)
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="scopeNumber", before=false, resultType=Long.class)
    public void insertScope(Scope scope);

    @Insert("""
        INSERT INTO MC_SCOPE_RELATION (
           CRITERIA_SCOPE_NUM
          ,OPPONENT_SCOPE_NUM
          ,END_DATETIME
          ,CRITERIA_SCOPE_LEVEL
          ,OPPONENT_SCOPE_LEVEL
          ,CRITERIA_SORT_NUM
          ,OPPONENT_SORT_NUM
          ,CREATE_DATETIME
          ,CREATE_ID
          ,MODIFY_DATETIME
          ,MODIFY_ID
        ) VALUES (
           #{criteriaScopeNumber}
          ,#{opponentScopeNumber}
          ,STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
          ,#{criteriaScopeLevel}
          ,#{opponentScopeLevel}
          ,#{criteriaSortNumber}
          ,#{opponentSortNumber}
          ,NOW(6)
          ,#{createId}
          ,NOW(6)
          ,#{modifyId}
        )
    """)
    public void insertScopeRelation(Scope scope);

    @Insert("""
        INSERT INTO MC_SCOPE_HIST (
           SCOPE_NUM
          ,HIST_END_DATETIME
          ,HIST_BEGIN_DATETIME
          ,SCOPE_NM
          ,SCOPE_CONTEXT
          ,ICON_FILE_CHANNEL
          ,SCOPE_DESC
          ,SCOPE_USE_YN
          ,SCOPE_DELETE_YN
          ,SVC_NUM
          ,CREATE_DATETIME
          ,CREATE_ID
          ,MODIFY_DATETIME
          ,MODIFY_ID
        ) SELECT
          SCOPE_NUM
          ,STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
          ,NOW(6)
          ,SCOPE_NM
          ,SCOPE_CONTEXT
          ,ICON_FILE_CHANNEL
          ,SCOPE_DESC
          ,SCOPE_USE_YN
          ,SCOPE_DELETE_YN
          ,SVC_NUM
          ,CREATE_DATETIME
          ,CREATE_ID
          ,MODIFY_DATETIME
          ,MODIFY_ID
        FROM
          MC_SCOPE
        WHERE
          SCOPE_NUM = #{scopeNumber}
    """)
    public void insertScopeHistory(long scopeNumber);

	@Insert("""
		INSERT INTO MC_SCOPE_API (
		   SCOPE_NUM
		  ,API_NUM
		  ,END_DATETIME
		  ,CREATE_DATETIME
		  ,CREATE_ID
		  ,MODIFY_DATETIME
		  ,MODIFY_ID
		) VALUES (
		   #{scopeNumber}
		  ,#{apiNumber}
		  ,STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
		  ,NOW(6)
		  ,#{createId}
		  ,NOW(6)
		  ,#{modifyId}
		)
	""")
	public void insertScopeApi(ScopeApi scopeApi);

	@Select("""
		SELECT
		  GROUP_CONCAT(MS.SCOPE_CONTEXT)
		FROM
		  MC_SCOPE_API MSA
		  LEFT JOIN MC_SCOPE MS ON MSA.SCOPE_NUM = MS.SCOPE_NUM
		WHERE
		  MSA.API_NUM = #{apiNumber}
		GROUP BY
		  MSA.API_NUM
	""")
	public String selectScopeApiCache(long apiNumber);

	@Delete("""
		DELETE
		FROM
		  MC_SCOPE_API
		WHERE
		  SCOPE_NUM = #{scopeNumber}
		  AND API_NUM = #{apiNumber}
	""")
	public void deleteScopeApi(ScopeApi scopeApi);

    @Select("""
        SELECT
          MS.SCOPE_NUM AS scopeNumber
         ,MS.SCOPE_NM AS scopeName
        FROM
          MC_SCOPE MS
		  LEFT JOIN MC_SCOPE_RELATION MSR ON MS.SCOPE_NUM = MSR.OPPONENT_SCOPE_NUM
        WHERE
          MS.SCOPE_NUM != 0 AND
          MS.SCOPE_DELETE_YN = 'N' AND
          MS.SCOPE_USE_YN = 'Y' AND
          MSR.CRITERIA_SCOPE_NUM = 0 AND
		  MSR.END_DATETIME > NOW()
		ORDER BY
			MSR.OPPONENT_SORT_NUM
    """)
	public List<Scope> selectScopeSelectList();

	@Select("""
        SELECT
		  MS.SCOPE_NUM AS scopeNumber
		 ,MS.SCOPE_NM AS scopeName
		FROM
		  MC_SCOPE MS
		  LEFT JOIN MC_SCOPE_RELATION MSR ON MS.SCOPE_NUM = MSR.OPPONENT_SCOPE_NUM
		WHERE
		  MS.SCOPE_NUM != 0 AND
		  MS.SCOPE_DELETE_YN = 'N' AND
		  MS.SCOPE_USE_YN = 'Y' AND
		  MSR.CRITERIA_SCOPE_NUM = #{scopeNumber} AND
		  MSR.END_DATETIME > NOW()
		ORDER BY
			MSR.OPPONENT_SORT_NUM
    """)
	public List<Scope> selectScopeSubList(long scopeNumber);

    @Select("""
	<script>
        SELECT
          MSP.SCOPE_NUM AS scopeNumber
         ,MA.API_NUM AS apiNumber
         ,MA.API_NM AS apiName
         ,MA.HTTP_METHOD_CD AS httpMethodCode
         ,f_code_name(MA.HTTP_METHOD_CD) AS httpMethodCodeName
        FROM
          MC_SCOPE MSP
          LEFT JOIN MC_SVC MS ON MS.SVC_NUM = MSP.SVC_NUM
          LEFT JOIN MC_API_GRP MAG ON MAG.SVC_NUM = MS.SVC_NUM
          LEFT JOIN MC_API MA ON MA.API_GRP_NUM = MAG.API_GRP_NUM
        WHERE
          MA.API_SECTION_CD = 'MC_API_SECTION_01'
          AND MA.API_USE_YN = 'Y'
          AND MA.API_DELETE_YN = 'N'
          AND MS.SVC_USE_YN = 'Y'
          AND MS.SVC_DELETE_YN = 'N'
          AND MSP.SCOPE_NUM = #{query.scopeNumber}
          AND NOT EXISTS(SELECT 'X' FROM MC_SCOPE_API WHERE API_NUM = MA.API_NUM)

		<if test="query!=null">
			<if test="query.apiMethod!=null and query.apiMethod!=''">
				AND MA.HTTP_METHOD_CD = #{query.apiMethod}
		   </if>
		   <if test="query.apiName!=null and query.apiName!=''">
		   		AND MA.API_NM LIKE CONCAT('%', #{query.apiName}, '%')
		   </if>
	   </if>
    </script>
    """)
    public List<ScopeApi> selectScopeApiList(SearchRequest searchRequest);

    @Select("""
        SELECT
          MSP.SCOPE_NUM AS scopeNumber
         ,MA.API_NUM AS apiNumber
         ,MA.API_NM AS apiName
         ,MA.HTTP_METHOD_CD AS httpMethodCode
         ,f_code_name(MA.HTTP_METHOD_CD) AS httpMethodCodeName
        FROM
          MC_SCOPE_API MSA
          INNER JOIN MC_SCOPE MSP ON MSP.SCOPE_NUM = MSA.SCOPE_NUM
          INNER JOIN MC_SVC MS ON MS.SVC_NUM = MSP.SVC_NUM
          INNER JOIN MC_API MA ON MA.API_NUM = MSA.API_NUM
        WHERE
          MSA.SCOPE_NUM = #{scopeNumber}
    """)
    public List<ScopeApi> selectScopeApiRelationList(long scopeNumber);

	@Update("""
		UPDATE
		  MC_SCOPE MS
		  LEFT JOIN MC_SCOPE_RELATION MSR ON MS.SCOPE_NUM = MSR.OPPONENT_SCOPE_NUM
		SET
		  MS.SCOPE_USE_YN = #{scopeUseYn}
		WHERE
		  MSR.CRITERIA_SCOPE_NUM = 0
		  AND MS.SVC_NUM = #{serviceNumber}
	""")
	public void updateScopeUseYn(Scope scope);

	@Results(id="scopeListResult", value = [
		@Result(property = "scopeNumber", column = "SCOPE_NUM"),
		@Result(property = "serviceNumber", column = "SVC_NUM"),
		@Result(property = "scopeName", column = "SCOPE_NM"),
		@Result(property = "scopeContext", column = "SCOPE_CONTEXT"),
		@Result(property = "iconFileChannel", column = "ICON_FILE_CHANNEL"),
		@Result(property = "scopeDescription", column = "SCOPE_DESC"),
		@Result(property = "scopeUseYn", column = "SCOPE_USE_YN"),
		@Result(property = "criteriaScopeNumber", column = "CRITERIA_SCOPE_NUM"),
		@Result(property = "opponentScopeNumber", column = "OPPONENT_SCOPE_NUM"),
		@Result(property = "criteriaScopeLevel", column = "CRITERIA_SCOPE_LEVEL"),
		@Result(property = "opponentScopeLevel", column = "OPPONENT_SCOPE_LEVEL"),
		@Result(property = "criteriaSortNumber", column = "CRITERIA_SORT_NUM"),
		@Result(property = "opponentSortNumber", column = "OPPONENT_SORT_NUM"),
		@Result(property = "createDateTime", column = "CREATE_DATETIME"),
		@Result(property = "createId", column = "CREATE_ID"),
		@Result(property = "modifyDateTime", column = "MODIFY_DATETIME"),
		@Result(property = "modifyId", column = "MODIFY_ID")
	])
	@Select("""<script>
	SELECT
		SR.CRITERIA_SCOPE_NUM , SR.OPPONENT_SCOPE_NUM , SR.CRITERIA_SCOPE_LEVEL , 
		SR.OPPONENT_SCOPE_LEVEL , SR.CRITERIA_SORT_NUM , SR.OPPONENT_SORT_NUM , SC.*
	FROM MC_SCOPE_RELATION SR
	LEFT JOIN MC_SCOPE SC ON SC.SCOPE_NUM = SR.OPPONENT_SCOPE_NUM
	WHERE SR.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
		AND SC.SCOPE_DELETE_YN = 'N'
	ORDER BY SR.CRITERIA_SCOPE_LEVEL , SR.OPPONENT_SCOPE_LEVEL , SR.OPPONENT_SORT_NUM
    </script>""")
	public List<Scope> getScopeList();
	
	@Results(id="scopeDetailResult", value = [
		@Result(property = "scopeNumber", column = "SCOPE_NUM"),
		@Result(property = "serviceNumber", column = "SVC_NUM"),
		@Result(property = "serviceName", column = "SVC_NM"),
		@Result(property = "scopeName", column = "SCOPE_NM"),
		@Result(property = "scopeContext", column = "SCOPE_CONTEXT"),
		@Result(property = "iconFileChannel", column = "ICON_FILE_CHANNEL"),
		@Result(property = "scopeDescription", column = "SCOPE_DESC"),
		@Result(property = "scopeUseYn", column = "SCOPE_USE_YN"),
		@Result(property = "parentContext", column = "PAR_CONTEXT"),
		@Result(property = "criteriaScopeNumber", column = "CRITERIA_SCOPE_NUM"),
		@Result(property = "criteriaScopeLevel", column = "CRITERIA_SCOPE_LEVEL"),
		@Result(property = "criteriaSortNumber", column = "CRITERIA_SORT_NUM"),
		@Result(property = "opponentScopeLevel", column = "OPPONENT_SCOPE_LEVEL"),
		@Result(property = "opponentSortNumber", column = "OPPONENT_SORT_NUM"),
		@Result(property = "createDateTime", column = "CREATE_DATETIME"),
		@Result(property = "createId", column = "CREATE_ID"),
		@Result(property = "modifyDateTime", column = "MODIFY_DATETIME"),
		@Result(property = "modifyId", column = "MODIFY_ID")
	])
	@Select("""<script>
	SELECT
		SC.* ,
		SV.SVC_NM ,
		SUBSP.SCOPE_CONTEXT AS PAR_CONTEXT ,
		SR.CRITERIA_SCOPE_NUM , 
		SR.CRITERIA_SCOPE_LEVEL ,
		SR.CRITERIA_SORT_NUM ,
		SR.OPPONENT_SCOPE_LEVEL ,
		SR.OPPONENT_SORT_NUM
	FROM MC_SCOPE SC
	LEFT JOIN MC_SVC SV ON SC.SVC_NUM = SV.SVC_NUM
	LEFT JOIN MC_SCOPE_RELATION SR ON SC.SCOPE_NUM = SR.OPPONENT_SCOPE_NUM
	LEFT JOIN MC_SCOPE SUBSP ON SR.CRITERIA_SCOPE_NUM = SUBSP.SCOPE_NUM 
	WHERE SC.SCOPE_NUM = #{scopeNumber}
	AND SC.SCOPE_DELETE_YN = 'N'
	AND SR.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
	public Scope getScopeDetail(Long scopeNumber);
	
	@Select("""<script>
	SELECT
		SCOPE_NUM
	FROM MC_SCOPE
	WHERE SCOPE_CONTEXT = #{scopeContext}
    </script>""")
	public Object checkScopeContext(String scopeContext);
	
	@Insert("""<script>
	INSERT INTO MC_SCOPE_RELATION (
    	CRITERIA_SCOPE_NUM,
        OPPONENT_SCOPE_NUM,
        END_DATETIME,
        CRITERIA_SCOPE_LEVEL,
        OPPONENT_SCOPE_LEVEL,
        CRITERIA_SORT_NUM,
        OPPONENT_SORT_NUM,
        CREATE_DATETIME,
        CREATE_ID,
        MODIFY_DATETIME,
        MODIFY_ID
	) SELECT
		#{criteriaScopeNumber} AS CRITERIA_SCOPE_NUM,
        #{opponentScopeNumber} AS OPPONENT_SCOPE_NUM,
        STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f') AS END_DATETIME,
        #{criteriaScopeLevel} AS CRITERIA_SCOPE_LEVEL,
       	#{opponentScopeLevel} AS OPPONENT_SCOPE_LEVEL,
		(SELECT OPPONENT_SORT_NUM FROM MC_SCOPE_RELATION 
		WHERE END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
		AND OPPONENT_SCOPE_NUM = #{criteriaScopeNumber}) AS CRITERIA_SORT_NUM,
		(SELECT COUNT(CRITERIA_SCOPE_NUM)+1 FROM MC_SCOPE_RELATION
		WHERE END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
		AND CRITERIA_SCOPE_NUM = #{criteriaScopeNumber}) AS OPPONENT_SORT_NUM,
        NOW(6) AS CREATE_DATETIME,
        #{createId} AS CREATE_ID,
        NOW(6) AS MODIFY_DATETIME,
        #{modifyId} AS MODIFY_ID
	FROM DUAL
    </script>""")
	public void insertNewScopeRelation(Scope scope);
	
	@Update("""<script>
	UPDATE MC_SCOPE SET
		SCOPE_NM = #{scopeName} ,
		ICON_FILE_CHANNEL = #{iconFileChannel} ,
		SCOPE_DESC = #{scopeDescription} ,
		SCOPE_USE_YN = #{scopeUseYn} ,
		MODIFY_DATETIME = NOW(6) ,
        MODIFY_ID = #{modifyId}
    WHERE SCOPE_NUM = #{scopeNumber}
    </script>""")
	public void updateScope(Scope scope);
	
	@Update("""<script>
	UPDATE MC_SCOPE_HIST SET
		HIST_END_DATETIME = NOW(6) ,
		MODIFY_DATETIME = NOW(6) ,
        MODIFY_ID = #{modifyId}
    WHERE SCOPE_NUM = #{scopeNumber}
		AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
	public void updateScopeHistory(Scope scope);
	
	@Update("""<script>
	UPDATE MC_SCOPE SET
		SCOPE_DELETE_YN = 'Y' ,
		MODIFY_DATETIME = NOW(6) ,
        MODIFY_ID = #{modifyId}
    WHERE SCOPE_NUM = #{scopeNumber}
    </script>""")
	public void deleteScope(Scope scope);
	
	@Update("""<script>
	UPDATE MC_SCOPE_RELATION SET
		END_DATETIME = NOW(6) ,
		MODIFY_DATETIME = NOW(6) ,
        MODIFY_ID = #{modifyId}
    WHERE OPPONENT_SCOPE_NUM = #{scopeNumber}
		AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
	public void updateScopeRelation(Scope scope);
	
	@Results(id="scopeSortList", value = [
		@Result(property = "criteriaScopeNumber", column = "CRITERIA_SCOPE_NUM"),
		@Result(property = "opponentScopeNumber", column = "OPPONENT_SCOPE_NUM"),
		@Result(property = "criteriaScopeLevel", column = "CRITERIA_SCOPE_LEVEL"),
		@Result(property = "opponentScopeLevel", column = "OPPONENT_SCOPE_LEVEL"),
		@Result(property = "criteriaSortNumber", column = "CRITERIA_SORT_NUM"),
		@Result(property = "opponentSortNumber", column = "OPPONENT_SORT_NUM")
	])
	@Select("""<script>
	SELECT 
		CRITERIA_SCOPE_NUM ,
		OPPONENT_SCOPE_NUM ,
		CRITERIA_SCOPE_LEVEL ,
		OPPONENT_SCOPE_LEVEL ,
		CRITERIA_SORT_NUM ,
		IFNULL(OPPONENT_SORT_NUM,0) AS OPPONENT_SORT_NUM
	FROM
		MC_SCOPE_RELATION
	WHERE
		CRITERIA_SCOPE_NUM = #{criteriaScopeNumber}
		AND OPPONENT_SCOPE_NUM != 0
		AND OPPONENT_SCOPE_NUM NOT IN (#{opponentScopeNumber})
		AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
	ORDER BY OPPONENT_SORT_NUM
	</script>""")
	public List<Scope> getScopeSortList(Scope scope);
	
	@Update("""<script>
		UPDATE MC_SCOPE_RELATION SET
			OPPONENT_SORT_NUM = #{opponentSortNumber} ,
          	MODIFY_DATETIME   = NOW(6) ,
          	MODIFY_ID         = #{modifyId}
       	WHERE OPPONENT_SCOPE_NUM = #{opponentScopeNumber}
			AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
	public void updateScopeSort(Scope scope);
	
	@Update("""<script>
		UPDATE MC_SCOPE_RELATION SET
			CRITERIA_SORT_NUM = #{opponentSortNumber} ,
          	MODIFY_DATETIME   = NOW(6) ,
          	MODIFY_ID         = #{modifyId}
       	WHERE CRITERIA_SCOPE_NUM = #{opponentScopeNumber}
			AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
	public void updateCriteriaScopeLevel(Scope scope);
}