package com.oneplat.oap.mgmt.setting.system.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectKey
import org.apache.ibatis.annotations.Update
import com.oneplat.oap.core.model.SearchRequest
import com.oneplat.oap.mgmt.setting.system.model.CommonCode
import com.oneplat.oap.mgmt.setting.system.model.CommonGroupCode
import com.oneplat.oap.mgmt.setting.system.model.CommonCode.ModifyCommonCodeSort

@Mapper
public interface CommonCodeMapper {

	// 그룹코드목록
	@Results(id="commonGroupCodeResult", value = [
		@Result(property = "groupCode", column = "GRP_CD"),
		@Result(property = "groupCodeName", column = "GRP_CD_NM"),
		@Result(property = "groupCodeDesc", column = "GRP_CD_DESC"),
		@Result(property = "sortNumber", column = "SORT_NUM"),
		@Result(property = "useYn", column = "USE_YN"),
		@Result(property = "createDateTime", column = "CREATE_DATETIME"),
		@Result(property = "createId", column = "CREATE_ID"),
		@Result(property = "modifyDateTime", column = "MODIFY_DATETIME"),
		@Result(property = "modifyId", column = "MODIFY_ID")
	])
	@Select("""<script>
        SELECT GRP_CD, GRP_CD_NM, GRP_CD_DESC, SORT_NUM, USE_YN
            , CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID
        FROM MC_COMMON_GRP_CD
        <if test="query!=null">
            <where>
                <if test="query.searchWord!=null and query.searchWord!=''">
                    <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode!=''">
                        <if test="query.searchWordTypeCode=='GRP_CD_NM'">
                        GRP_CD_NM LIKE CONCAT('%',#{query.searchWord},'%')
                        </if>
                        <if test="query.searchWordTypeCode=='CD_NM'">
                        GRP_CD IN ( SELECT GRP_CD FROM MC_COMMON_CD WHERE CD_NM LIKE CONCAT('%',#{query.searchWord},'%') )
                        </if>
                        <if test="query.searchWordTypeCode=='CD'">
                        GRP_CD IN ( SELECT GRP_CD FROM MC_COMMON_CD WHERE CD LIKE CONCAT('%',#{query.searchWord},'%') )
                        </if>
                    </if>
                </if>
                <if test="query.useYnCode!=null and query.useYnCode!=''">
                AND USE_YN = #{query.useYnCode}
                </if>
            </where>
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
        </if>
    </script>""")
	public List<CommonGroupCode> selectCommonGroupCodeList(SearchRequest searchRequest);
	
	@Select("""<script>
        SELECT COUNT(GRP_CD)
        FROM MC_COMMON_GRP_CD
        <if test="query!=null">
            <where>
                <if test="query.searchWord!=null and query.searchWord!=''">
                    <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode!=''">
                        <if test="query.searchWordTypeCode=='GRP_CD_NM'">
                        GRP_CD_NM LIKE CONCAT('%',#{query.searchWord},'%')
                        </if>
                        <if test="query.searchWordTypeCode=='CD_NM'">
                        GRP_CD IN ( SELECT GRP_CD FROM MC_COMMON_CD WHERE CD_NM LIKE CONCAT('%',#{query.searchWord},'%') )
                        </if>
                        <if test="query.searchWordTypeCode=='CD'">
                        GRP_CD IN ( SELECT GRP_CD FROM MC_COMMON_CD WHERE CD LIKE CONCAT('%',#{query.searchWord},'%') )
                        </if>
                    </if>
                </if>
                <if test="query.useYnCode!=null and query.useYnCode!=''">
                AND USE_YN = #{query.useYnCode}
                </if>
            </where>
        </if>
    </script>""")
	public int getCommonGroupCodeListCount(SearchRequest searchRequest);

	// 코드 목록
	@Results(id="commonCodeResult", value = [
		@Result(property = "groupCode", column = "GRP_CD"),
		@Result(property = "code", column = "CD"),
		@Result(property = "codeName", column = "CD_NM"),
		@Result(property = "codeDesc", column = "CD_DESC"),
		@Result(property = "codeCharVal1", column = "CD_CHAR_VAL1"),
		@Result(property = "codeCharVal2", column = "CD_CHAR_VAL2"),
		@Result(property = "codeCharVal3", column = "CD_CHAR_VAL3"),
		@Result(property = "codeNumberVal1", column = "CD_NUM_VAL1"),
		@Result(property = "codeNumberVal2", column = "CD_NUM_VAL2"),
		@Result(property = "codeNumberVal3", column = "CD_NUM_VAL3"),
		@Result(property = "sortNumber", column = "SORT_NUM"),
		@Result(property = "useYn", column = "USE_YN"),
		@Result(property = "createDateTime", column = "CREATE_DATETIME"),
		@Result(property = "createId", column = "CREATE_ID")
	])
	@Select("""<script>
        SELECT GRP_CD, CD, CD_NM, CD_DESC, SORT_NUM
            , CD_CHAR_VAL1, CD_CHAR_VAL2, CD_CHAR_VAL3, CD_NUM_VAL1, CD_NUM_VAL2, CD_NUM_VAL3
            , USE_YN, CREATE_DATETIME, CREATE_ID
        FROM MC_COMMON_CD
        <if test="query!=null">
            <where>
                <if test="query.groupCode!=null and query.groupCode!=''">
                GRP_CD = #{query.groupCode}
                </if>
                <if test="query.codeName!=null and query.codeName!=''">
                AND CD_NM LIKE CONCAT('%',#{query.codeName},'%')
                </if>
                <if test="query.useYnCode!=null and query.useYnCode!=''">
                AND USE_YN = #{query.useYnCode}
                </if>
            </where>
            <choose>
                <when test="query.sortField!=null and query.sortField!=''">
            ORDER BY \${query.sortField} <if test="query.orderBy!=null and query.orderBy!=''"> \${query.orderBy} </if>
                </when>
                <otherwise>
            ORDER BY SORT_NUM
                </otherwise>
            </choose>
            <if test="pageInfo!=null">
            LIMIT #{pageInfo.startRowNum}, #{pageInfo.size}
            </if>
        </if>
    </script>""")
	public List<CommonCode> selectCommonoCodeList(SearchRequest searchRequest);
	
	@Select("""<script>
        SELECT COUNT(CD)
        FROM MC_COMMON_CD
        <if test="query!=null">
            <where>
                <if test="query.groupCode!=null and query.groupCode!=''">
                GRP_CD = #{query.groupCode}
                </if>
                <if test="query.useYnCode!=null and query.useYnCode!=''">
                AND USE_YN = #{query.useYnCode}
                </if>
            </where>
            <choose>
                <when test="query.sortField!=null and query.sortField!=''">
            ORDER BY \${query.sortField} <if test="query.orderBy!=null and query.orderBy!=''"> \${query.orderBy} </if>
                </when>
                <otherwise>
            ORDER BY SORT_NUM
                </otherwise>
            </choose>
        </if>
    </script>""")
	public int getCommonoCodeListCount(SearchRequest searchRequest);

	// 코드 중복 건수-codeType에 따라 group테이블을 조회하거나 코드테이블을 조회하도록 작성함
	// 중복코드여부 확인시 그룹/공통코드 모두 code값만 비교함
	@Select("""<script>
        SELECT COUNT(\${query.codeType})
        FROM MC_COMMON_\${query.codeType}
        <where>
            <if test="query.code!=null and query.code!=''">
            \${query.codeType} = #{query.code}
            </if>
            <if test="query.groupCode!=null and query.groupCode!=''">
            AND GRP_CD = #{query.groupCode}
            </if>
            <if test="query.codeName!=null and query.codeName!=''">
            AND \${query.codeType}_NM = #{query.codeName}
            </if>
        </where>
    </script>""")
	public int getDuplicateCount(SearchRequest searchRequest);
	
	// 그룹코드상세
	@ResultMap("commonGroupCodeResult")
	@Select("""<script>
        SELECT GRP_CD, GRP_CD_NM, GRP_CD_DESC, SORT_NUM, USE_YN
            , CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID
        FROM MC_COMMON_GRP_CD
        <where>
            <if test="groupCode!=null and groupCode!=''">
            GRP_CD = #{groupCode}
            </if>
            <if test="groupCodeName!=null and groupCodeName!=''">
            AND GRP_CD_NM = #{groupCodeName}
            </if>
            <if test="useYn!=null and useYn!=''">
            AND USE_YN = #{useYn}
            </if>
        </where>
        LIMIT 1
    </script>""")
	public CommonGroupCode selectCommonGroupCode(CommonGroupCode commonGroupCode);

	// 그룹코드건수
	@Select("""<script>
        SELECT COUNT(GRP_CD)
        FROM MC_COMMON_GRP_CD
        <where>
            <if test="groupCode!=null and groupCode!=''">
            GRP_CD = #{groupCode}
            </if>
            <if test="groupCodeName!=null and groupCodeName!=''">
            AND GRP_CD_NM = #{groupCodeName}
            </if>
            <if test="useYn!=null and useYn!=''">
            AND USE_YN = #{useYn}
            </if>
        </where>
    </script>""")
	public int getCommonGroupCodeCount(CommonGroupCode commonGroupCode);

	// 그룹코드상세
	@ResultMap("commonCodeResult")
	@Select("""<script>
        SELECT GRP_CD, CD, CD_NM, CD_DESC, SORT_NUM
            , CD_CHAR_VAL1, CD_CHAR_VAL2, CD_CHAR_VAL3, CD_NUM_VAL1, CD_NUM_VAL2, CD_NUM_VAL3
            , USE_YN, CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID
        FROM MC_COMMON_CD
        <where>
            <if test="groupCode!=null and groupCode!=''">
            GRP_CD = #{groupCode}
            </if>
            <if test="code!=null and code!=''">
            AND CD = #{groupCode}
            </if>
            <if test="codeName!=null and codeName!=''">
            AND CD_NM = #{codeName}
            </if>
            <if test="useYn!=null and useYn.getCode()!=''">
            AND USE_YN = #{useYn}
            </if>
        </where>
        LIMIT 1
    </script>""")
	public CommonCode selectCommonCode(CommonCode commonCode);

	// 코드 건수
	@Select("""<script>
        SELECT COUNT(CD)
        FROM MC_COMMON_CD
        <where>
            <if test="groupCode!=null and groupCode!=''">
            GRP_CD = #{groupCode}
            </if>
            <if test="code!=null and code!=''">
            AND CD = #{code}
            </if>
            <if test="codeName!=null and codeName!=''">
            AND CD_NM = #{codeName}
            </if>
            <if test="useYn!=null and useYn!=''">
            AND USE_YN = #{useYn}
            </if>
        </where>
    </script>""")
	public int getCommonCodeCount(CommonCode commonCode);
	
	// 공통그룹코드 등록
	@Insert("""<script>
        INSERT INTO MC_COMMON_GRP_CD( GRP_CD, GRP_CD_NM, GRP_CD_DESC, USE_YN, SORT_NUM, CREATE_DATETIME, CREATE_ID )
        VALUES ( #{groupCode}, #{groupCodeName}, #{groupCodeDesc}, #{useYn}, #{sortNumber}, NOW(), #{createId} )
    </script>""")
	@SelectKey(statement="SELECT COUNT(GRP_CD)+1 FROM MC_COMMON_GRP_CD", keyProperty="sortNumber", before=true, resultType=Integer.class)
	public int insertCommonGroupCode(CommonGroupCode commonGroupCode);
	
	// 공통그룹코드 수정
	@Update("""<script>
        UPDATE MC_COMMON_GRP_CD
        SET GRP_CD = #{groupCode}
            , GRP_CD_NM = #{groupCodeName}
            , GRP_CD_DESC = #{groupCodeDesc}
            , USE_YN = #{useYn}
            , MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
        WHERE GRP_CD = #{groupCode}
    </script>""")
	public int updateCommonGroupCode(CommonGroupCode commonGroupCode);
	
	// 공통코드 등록
	@Insert("""<script>
        INSERT INTO MC_COMMON_CD( GRP_CD, CD, CD_NM, CD_DESC, USE_YN
            , CD_CHAR_VAL1, CD_CHAR_VAL2, CD_CHAR_VAL3, CD_NUM_VAL1, CD_NUM_VAL2, CD_NUM_VAL3
            , SORT_NUM, CREATE_DATETIME, CREATE_ID )
        VALUES ( #{groupCode}, #{code}, #{codeName}, #{codeDesc}, #{useYn}
            , #{codeCharVal1}, #{codeCharVal2}, #{codeCharVal3}, #{codeNumberVal1}, #{codeNumberVal2}, #{codeNumberVal3}
            , #{sortNumber}, NOW(), #{createId} )
    </script>""")
	@SelectKey(statement="SELECT COUNT(CD)+1 FROM MC_COMMON_CD WHERE GRP_CD = #{groupCode}", keyProperty="sortNumber", before=true, resultType=Integer.class)
	public int insertCommonCode(CommonCode commonCode);
	
	// 공통코드 수정
	@Update("""<script>
        UPDATE MC_COMMON_CD
        SET CD = #{code}
            , CD_NM = #{codeName}
            , CD_DESC = #{codeDesc}
            , USE_YN = #{useYn}
            , MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
            <if test="codeCharVal1!=null">
            , CD_CHAR_VAL1 = #{codeCharVal1}
            </if>
            <if test="codeCharVal2!=null">
            , CD_CHAR_VAL2 = #{codeCharVal2}
            </if>
            <if test="codeCharVal3!=null">
            , CD_CHAR_VAL3 = #{codeCharVal3}
            </if>
            <if test="codeNumberVal1!=null">
            , CD_NUM_VAL1 = #{codeNumberVal1}
            </if>
            <if test="codeNumberVal2!=null">
            , CD_NUM_VAL2 = #{codeNumberVal2}
            </if>
            <if test="codeNumberVal3!=null">
            , CD_NUM_VAL3 = #{codeNumberVal3}
            </if>
        WHERE GRP_CD = #{groupCode}
            AND CD = #{code}
    </script>""")
	public int updateCommonCode(CommonCode commonCode);
	
	@Update("""<script>
        UPDATE MC_COMMON_\${codeType}
        SET MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
            , SORT_NUM = ( CASE WHEN SORT_NUM<![CDATA[>=]]>#{startNum} AND SORT_NUM<![CDATA[<=]]>#{endNum} AND #{beforeSortNum}!=#{afterSortNum} 
                              THEN (
                                  CASE WHEN \${codeType}=#{code} THEN #{afterSortNum}
                                      WHEN SORT_NUM>#{startNum} AND SORT_NUM = #{endNum} THEN SORT_NUM-1
                                      WHEN SORT_NUM = #{afterSortNum} THEN SORT_NUM+1
                                      ELSE ( IF(SORT_NUM<![CDATA[<]]>#{afterSortNum}, SORT_NUM-1, IF(SORT_NUM <![CDATA[>]]> #{afterSortNum},SORT_NUM+1,SORT_NUM) ) )
                                      END
                              )
                              ELSE SORT_NUM END )
        WHERE SORT_NUM IS NOT NULL
        <if test="groupCode!=null and groupCode!=''">
        AND GRP_CD = #{groupCode}
        </if>
    </script>""")
	public int updateSortNumber(ModifyCommonCodeSort sortModel);
	
	@Update("""<script>
        UPDATE MC_COMMON_GRP_CD TTT1, (
            SELECT @rnum:=@rnum+1 AS RNUM, GRP_CD
            FROM (
                SELECT ROWNUM, GRP_CD, SORT_NUM AS OLD_SORT_NUM
                    , IF(GRP_CD=#{groupCode},#{afterSortNum},(IF(SORT_NUM=#{afterSortNum},IF(#{beforeSortNum}<![CDATA[>]]>#{afterSortNum},ROWNUM+1,ROWNUM-1),ROWNUM))) AS NEW_SORT_NUM
                FROM (
                    SELECT @rownum:=@rownum+1 AS ROWNUM, GRP_CD, SORT_NUM
                    FROM MC_COMMON_GRP_CD T1, ( SELECT @rownum:=0 ) T2
                    ORDER BY SORT_NUM
                ) TT1, ( SELECT @rnum:=0 ) TT2
            ORDER BY NEW_SORT_NUM, OLD_SORT_NUM
        ) AS T3 ) AS TTT2
        SET TTT1.SORT_NUM = TTT2.RNUM
        WHERE TTT1.GRP_CD = TTT2.GRP_CD
    </script>""")
	public int updateGroupCodeSortNumber(ModifyCommonCodeSort sortModel);

	@Update("""<script>
        UPDATE MC_COMMON_CD TTT1, (
            SELECT @rnum:=@rnum+1 AS RNUM, GRP_CD, CD
            FROM (
                SELECT ROWNUM, GRP_CD, CD, CD_NM, SORT_NUM AS OLD_SORT_NUM
                    , IF(CD=#{code},#{afterSortNum},(IF(SORT_NUM=#{afterSortNum},IF(#{beforeSortNum}<![CDATA[>]]>#{afterSortNum},ROWNUM+1,ROWNUM-1),ROWNUM))) AS NEW_SORT_NUM
                FROM (
                    SELECT @rownum:=@rownum+1 AS ROWNUM, GRP_CD, CD, CD_NM, SORT_NUM
                    FROM MC_COMMON_CD T1, ( SELECT @rownum:=0 ) T2
                    WHERE GRP_CD = #{groupCode}
                    ORDER BY SORT_NUM
                ) TT1, ( SELECT @rnum:=0 ) TT2
            ORDER BY NEW_SORT_NUM, OLD_SORT_NUM
        ) AS T3 ) AS TTT2
        SET TTT1.SORT_NUM = TTT2.RNUM
        WHERE TTT1.GRP_CD = TTT2.GRP_CD
        AND TTT1.CD = TTT2.CD
    </script>""")
	public int updateCodeSortNumber(ModifyCommonCodeSort sortModel);

}
