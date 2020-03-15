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
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenuDel
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenuRelation
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.AdminMenuRel
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.AdminMenuSort
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.AdminMenuTree
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.DeleteAdminMenu

@Mapper
public interface AdminMenuMapper {

    @Results(id="menuRsult", value = [
        @Result(property = "menuNumber", column = "MENU_NUM"),
        @Result(property = "menuName", column = "MENU_NM"),
        @Result(property = "criteriaMenuNumber", column = "CRITERIA_MENU_NUM"),
        @Result(property = "useYn", column = "USE_YN"),
        @Result(property = "subMenuList", column="MENU_NUM", many=@Many(select = "selectAdminMenu"))
    ])
    @Select("""<script>
        SELECT MENU_NUM, MENU_NM, 0 AS CRITERIA_MENU_NUM
        FROM MC_MENU
        WHERE MENU_LEVEL = 0
            AND DELETE_YN = 'N'
    </script>""")
    public AdminMenuTree selectRootAdminMenu();
    
    @Results(id="menuRelRsult", value = [
        @Result(property = "menuNum", column = "MENU_NUM"),
        @Result(property = "authSetupYn", column = "AUTH_SETUP_YN"),
        @Result(property = "menuAuthCode", column = "MENU_AUTH_CD"),
        @Result(property = "leafNodeYn", column = "LEAF_NODE_YN"),
        @Result(property = "exposureYn", column = "EXPOSURE_YN"),
        @Result(property = "useYn", column = "USE_YN"),
        @Result(property = "menuNm", column = "MENU_NM"),
        @Result(property = "menuDesc", column = "MENU_DESC"),
        @Result(property = "pageUrl", column = "PAGE_URL"),
        @Result(property = "menuLevel", column = "MENU_LEVEL"),
        @Result(property = "sortNum", column = "SORT_NUM"),
        @Result(property = "opponentSortNum", column = "OPPONENT_SORT_NUM"),
        @Result(property = "opponentMenuLevel", column = "OPPONENT_MENU_LEVEL"),
        @Result(property = "opponentMenuNum", column = "OPPONENT_MENU_NUM"),
        @Result(property = "criteriaMenuLevel", column = "CRITERIA_MENU_LEVEL"),
        @Result(property = "criteriaMenuNum", column = "CRITERIA_MENU_NUM"),
        @Result(property = "criteriaSortNum", column = "CRITERIA_SORT_NUM"),
        @Result(property = "createDateTime", column = "CREATE_DATETIME"),
        @Result(property = "createId", column = "CREATE_ID"),
        @Result(property = "modifyDateTime", column = "MODIFY_DATETIME"),
        @Result(property = "modifyId", column = "MODIFY_ID")
    ])
    @Select("""<script>
        SELECT T1.MENU_NUM, T1.AUTH_SETUP_YN, T1.MENU_AUTH_CD, T1.LEAF_NODE_YN, T1.EXPOSURE_YN, T1.USE_YN                                     
            , T1.MENU_NM, T1.MENU_DESC, T1.PAGE_URL, T1.MENU_LEVEL, T1.SORT_NUM                                                           
            , T2.OPPONENT_SORT_NUM, T2.OPPONENT_MENU_LEVEL, T2.OPPONENT_MENU_NUM, T2.CRITERIA_MENU_LEVEL, T2.CRITERIA_MENU_NUM
            , T2.CRITERIA_SORT_NUM, CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID
        FROM MC_MENU T1 LEFT JOIN
          ( SELECT OPPONENT_SORT_NUM, OPPONENT_MENU_LEVEL, OPPONENT_MENU_NUM, CRITERIA_MENU_LEVEL, CRITERIA_MENU_NUM, CRITERIA_SORT_NUM 
            FROM MC_MENU_RELATION WHERE END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f') ) T2
          ON T1.MENU_NUM = T2.OPPONENT_MENU_NUM
        WHERE T1.DELETE_YN = 'N'
			AND T1.MENU_TYPE_CD = #{type}
        ORDER BY CRITERIA_MENU_LEVEL, OPPONENT_MENU_LEVEL, OPPONENT_SORT_NUM
    </script>""")
    public List<AdminMenuRel> selectAdminMenuList(String type);

    @ResultMap("menuRsult")
    @Select("""<script>
        SELECT A.OPPONENT_MENU_NUM AS MENU_NUM, B.MENU_NM, B.USE_YN
        FROM MC_MENU_RELATION AS A, MC_MENU AS B
        WHERE A.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
            AND A.OPPONENT_MENU_NUM = B.MENU_NUM
            AND B.DELETE_YN = 'N'
            AND A.CRITERIA_MENU_NUM = #{menuNumber}
        ORDER BY A.OPPONENT_SORT_NUM
    </script>""")
    public AdminMenuTree selectAdminMenu(Long menuNumber);
    
    @Select("""<script>
        SELECT A.OPPONENT_MENU_NUM, B.MENU_NM AS OPPONENT_MENU_NM, A.CRITERIA_MENU_NUM, A.CRITERIA_MENU_LEVEL, A.OPPONENT_MENU_LEVEL
            , A.CRITERIA_SORT_NUM, A.OPPONENT_SORT_NUM
        FROM MC_MENU_RELATION AS A, MC_MENU AS B
        WHERE A.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
            AND A.OPPONENT_MENU_NUM = B.MENU_NUM
            AND B.DELETE_YN = 'N'
        ORDER BY A.CRITERIA_MENU_LEVEL, A.OPPONENT_MENU_LEVEL, A.CRITERIA_SORT_NUM, A.OPPONENT_SORT_NUM
    </script>""")
    public List<AdminMenuTree> selectAdminMenuAllList();

    @Results(id="menuDetailRsult", value = [
        @Result(property = "menuNum", column = "MENU_NUM"),
        @Result(property = "menuNm", column = "MENU_NM"),
        @Result(property = "menuDesc", column = "MENU_DESC"),
        @Result(property = "pageUrl", column = "PAGE_URL"),
        @Result(property = "menuLevel", column = "MENU_LEVEL"),
        @Result(property = "sortNum", column = "SORT_NUM"),
        @Result(property = "authSetupYn", column = "AUTH_SETUP_YN"),
        @Result(property = "menuAuthCode", column = "MENU_AUTH_CD"),
        @Result(property = "exposureYn", column = "EXPOSURE_YN"),
        @Result(property = "useYn", column = "USE_YN"),
        @Result(property = "leafNodeYn", column = "LEAF_NODE_YN"),
        @Result(property = "deleteYn", column = "DELETE_YN"),
        @Result(property = "criteriaMenuNum", column = "CRITERIA_MENU_NUM"),
		@Result(property = "createDateTime", column = "CREATE_DATETIME"),
		@Result(property = "createId", column = "CREATE_ID"),
		@Result(property = "modifyDateTime", column = "MODIFY_DATETIME"),
		@Result(property = "modifyId", column = "MODIFY_ID")
    ])
    @Select("""<script>
        SELECT T1.MENU_NUM, T1.MENU_NM, T1.MENU_DESC, T1.PAGE_URL, T1.MENU_LEVEL
            , T1.SORT_NUM, T1.AUTH_SETUP_YN, T1.MENU_AUTH_CD, T1.EXPOSURE_YN, T1.USE_YN
            , T1.LEAF_NODE_YN, T1.DELETE_YN, T2.CRITERIA_MENU_NUM
			, T1.CREATE_DATETIME, T1.CREATE_ID, T1.MODIFY_DATETIME, T1.MODIFY_ID
        FROM MC_MENU T1 LEFT JOIN MC_MENU_RELATION T2 ON T1.MENU_NUM = T2.OPPONENT_MENU_NUM
                AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f') 
        WHERE T1.DELETE_YN = 'N'
            AND T1.MENU_NUM = #{menuNum}
    </script>""")
    public AdminMenuRel selectAdminMenuDetail(Long menuNum);

    @Select("""<script>
        SELECT OPPONENT_SORT_NUM, CRITERIA_SORT_NUM, CRITERIA_MENU_NUM, OPPONENT_MENU_NUM, B.MENU_NM AS OPPONENT_MENU_NM
            , CRITERIA_MENU_LEVEL, OPPONENT_MENU_LEVEL
        FROM (
          SELECT OPPONENT_SORT_NUM, CRITERIA_SORT_NUM, CRITERIA_MENU_NUM, OPPONENT_MENU_NUM, CRITERIA_MENU_LEVEL, OPPONENT_MENU_LEVEL
          FROM MC_MENU_RELATION
          WHERE CRITERIA_MENU_NUM = ( SELECT CRITERIA_MENU_NUM FROM MC_MENU_RELATION WHERE OPPONENT_MENU_NUM = #{menuNum}
                                      AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f') )
            AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
        ) A, MC_MENU B
        WHERE A.OPPONENT_MENU_NUM = B.MENU_NUM
            AND B.DELETE_YN = 'N'
        ORDER BY OPPONENT_SORT_NUM
    </script>""")
    public List<AdminMenuRelation> selectAdminCriteriaMenuList(Long menuNum);
    
    @Select("""<script>
        SELECT COUNT(T2.MENU_NUM)
        FROM MC_MENU_RELATION T1, MC_MENU T2
        WHERE T1.OPPONENT_MENU_NUM = T2.MENU_NUM
            AND T2.DELETE_YN = 'N'
            AND T2.USE_YN = 'Y'
            AND T1.CRITERIA_MENU_NUM = #{menuNumber}
            AND T1.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f') 
    </script>""")
    public int getIsAbleSubMenuCount(Long menuNumber);
    
    @Insert("""<script>
        INSERT INTO MC_MENU( MENU_NM, MENU_DESC, PAGE_URL, MENU_LEVEL, AUTH_SETUP_YN, MENU_AUTH_CD
            , MENU_TYPE_CD, EXPOSURE_YN, USE_YN, DELETE_YN, CREATE_DATETIME, CREATE_ID, LEAF_NODE_YN
            , MODIFY_DATETIME, MODIFY_ID, SORT_NUM )
        SELECT #{menuNm}, #{menuDesc}, #{pageUrl}, #{menuLevel}, #{authSetupYn}, #{menuAuthCode}
            , #{menuTypeCode}, #{exposureYn}, #{useYn}, 'N', NOW(6), #{createId}, 'Y'
            , NOW(6), #{modifyId}
            , COUNT(CRITERIA_MENU_NUM)+1
        FROM MC_MENU_RELATION
        WHERE CRITERIA_MENU_NUM = #{criteriaMenuNum} AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="menuNum", before=false, resultType=Long.class)
    public int insertAdminMenu(AdminMenu adminMenu);
    
    // 메뉴 상세정보 변경(상위메뉴 변경에 따른 rel 변경, 이력 관리 정보 저장이 필요하다)
    @Update("""<script>
        UPDATE MC_MENU
        SET MODIFY_DATETIME = NOW(6)
            , MODIFY_ID = #{modifyId}
            <if test="menuNm!=null">, MENU_NM = #{menuNm}</if>
            <if test="menuDesc!=null">, MENU_DESC = #{menuDesc}</if>
            <if test="pageUrl!=null">, PAGE_URL = #{pageUrl}</if>
            <if test="authSetupYn!=null">, AUTH_SETUP_YN = #{authSetupYn}</if>
            <if test="menuAuthCode!=null">, MENU_AUTH_CD = #{menuAuthCode}</if>
            <if test="exposureYn!=null">, EXPOSURE_YN = #{exposureYn}</if>
            <if test="useYn!=null">, USE_YN = #{useYn}</if>
        WHERE MENU_NUM = #{menuNum}
            AND DELETE_YN = 'N'
    </script>""")
    public int updateAdminMenu(AdminMenu adminMenu);
    
    // leafNodeYn 변경(상위메뉴 변경에 따른 rel 변경, 이력 관리 정보 저장이 필요할까?)
    @Update("""<script>
        UPDATE MC_MENU
        SET MODIFY_DATETIME = NOW(6)
            , MODIFY_ID = #{modifyId}
            , LEAF_NODE_YN = #{leafNodeYn}
        WHERE MENU_NUM = #{menuNum}
            AND DELETE_YN = 'N'
    </script>""")
    public int updateAdminMenuLeafNode(AdminMenu adminMenu);
    
    
    // 삭제(하위메뉴도 몽땅 삭제되어야 합니다)
    @Update("""<script>
            UPDATE MC_MENU
            SET DELETE_YN = 'Y'
                , MODIFY_DATETIME = NOW(6)
                , MODIFY_ID = #{modifyId}
            WHERE MENU_NUM = #{menuNum} 
        </script>""")
    public int deleteAdminMenu(AdminMenu adminMenu);

    // 이력테이블 갱신
    @Update("""<script>
        UPDATE MC_MENU_HIST
        SET HIST_END_DATETIME = NOW(6)
            , MODIFY_DATETIME = NOW(6)
            , MODIFY_ID = #{modifyId}
        WHERE MENU_NUM = #{menuNum}
            AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
    public int updateAdmnMenuHist(AdminMenu adminMenu);

    // 이력테이블 등록
    @Insert("""<script>
        INSERT INTO MC_MENU_HIST(
			MENU_NUM, 
			HIST_END_DATETIME, 
			HIST_BEGIN_DATETIME, 
			MENU_NM, 
			MENU_DESC, 
			PAGE_URL, 
			MENU_LEVEL, 
			SORT_NUM, 
			AUTH_SETUP_YN,
			LEAF_NODE_YN,
			EXPOSURE_YN, 
			MENU_AUTH_CD, 
			MENU_TYPE_CD, 
			USE_YN, 
			DELETE_YN, 
			CREATE_DATETIME, 
			CREATE_ID, 
			MODIFY_DATETIME, 
			MODIFY_ID )
        SELECT
			MENU_NUM, STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f'), NOW(6), MENU_NM, MENU_DESC
          , PAGE_URL, MENU_LEVEL, SORT_NUM, AUTH_SETUP_YN, LEAF_NODE_YN, EXPOSURE_YN, MENU_AUTH_CD
          , MENU_TYPE_CD, USE_YN, DELETE_YN, CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME
          , MODIFY_ID
        FROM MC_MENU
        WHERE MENU_NUM = #{menuNum}
    </script>""")
    public int insertAdmnMenuHist(Long menuNum);
    
    // 상위메뉴변경 - rel
    // 1. 대상건 SELECT
    @Select("""<script>
        SELECT CRITERIA_MENU_NUM, OPPONENT_MENU_NUM, CRITERIA_MENU_LEVEL, OPPONENT_MENU_LEVEL, CRITERIA_SORT_NUM
            , OPPONENT_SORT_NUM
        FROM MC_MENU_RELATION
        WHERE CRITERIA_MENU_NUM = #{criteriaMenuNum}
            <if test="opponentMenuNum != null and opponentMenuNum > 0">
            AND OPPONENT_MENU_NUM = #{opponentMenuNum}
            </if>
            AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
        ORDER BY OPPONENT_SORT_NUM
    </script>""")
    public List<AdminMenuRelation> selectAdminMenuRelationList(AdminMenuRelation adminMenuRelation);

    // 재정렬 목록
    @Select("""<script>
        SELECT RNUM AS NEW_SORT_NUM, CRITERIA_MENU_NUM, OPPONENT_MENU_NUM, CRITERIA_MENU_LEVEL, OPPONENT_MENU_LEVEL, CRITERIA_SORT_NUM
            , OPPONENT_SORT_NUM
        FROM (
            SELECT @rnum:=@rnum+1 AS RNUM, CRITERIA_MENU_NUM, OPPONENT_MENU_NUM, CRITERIA_MENU_LEVEL, OPPONENT_MENU_LEVEL, CRITERIA_SORT_NUM
            , OPPONENT_SORT_NUM, NEW_SORT_NUM
            FROM (
                SELECT CRITERIA_MENU_NUM, OPPONENT_MENU_NUM, CRITERIA_MENU_LEVEL, OPPONENT_MENU_LEVEL, CRITERIA_SORT_NUM
                    , OPPONENT_SORT_NUM
                    , IF(OPPONENT_MENU_NUM=#{opponentMenuNum}, #{opponentSortNum}, OPPONENT_SORT_NUM) AS NEW_SORT_NUM
                    , IF(OPPONENT_MENU_NUM=#{criteriaMenuNum}, 0, 1) AS NEW_ORDER_NUM
                FROM MC_MENU_RELATION
                WHERE CRITERIA_MENU_NUM = #{criteriaMenuNum}
                    AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                ORDER BY NEW_SORT_NUM, NEW_ORDER_NUM
            ) AS T, ( SELECT @rnum:=0 ) TT2
            ORDER BY NEW_SORT_NUM, NEW_ORDER_NUM
        ) TT
    </script>""")
    public List<AdminMenuRelation> selectAdminMenuRelationSortingList(AdminMenuSort adminMenuSort);

        // 2. 최신건(9999-12-31 23:59:59)의 종료일시를 NOW로 업데이트
    @Update("""<script>
        UPDATE MC_MENU_RELATION
            SET END_DATETIME = NOW(6)
                , MODIFY_DATETIME = NOW(6)
                , MODIFY_ID = #{modifyId}
        WHERE CRITERIA_MENU_NUM = #{criteriaMenuNum}
            AND OPPONENT_MENU_NUM = #{opponentMenuNum}
            AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
    public int updateAdmnMenuRelation(AdminMenuRelation adminMenuRelation);

    // 3. 최신건 등록(정렬순서에 대한 값이 없는 경우 최하위(max+1)로 등록함
    @Insert("""<script>
        INSERT MC_MENU_RELATION( CRITERIA_MENU_NUM, OPPONENT_MENU_NUM, END_DATETIME, CRITERIA_MENU_LEVEL, OPPONENT_MENU_LEVEL
            , CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID, CRITERIA_SORT_NUM, OPPONENT_SORT_NUM )
        SELECT #{criteriaMenuNum} AS CRITERIA_MENU_NUM
            , #{opponentMenuNum} AS OPPONENT_MENU_NUM
            , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f') AS END_DATETIME
            , #{criteriaMenuLevel} AS CRITERIA_MENU_LEVEL
            , #{opponentMenuLevel} AS OPPONENT_MENU_LEVEL
            , NOW(6) AS CREATE_DATETIME, #{modifyId} AS CREATE_ID, NOW(6) AS MODIFY_DATETIME, #{modifyId} AS MODIFY_ID
        <choose>
            <when test="criteriaSortNum != null and criteriaSortNum > 0">
            , #{criteriaSortNum} AS CRITERIA_SORT_NUM
            </when>
            <otherwise>
            , ( SELECT IFNULL(MAX(CRITERIA_SORT_NUM),0) FROM MC_MENU_RELATION WHERE CRITERIA_MENU_NUM = #{criteriaMenuNum} AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f') )
                AS CRITERIA_SORT_NUM
            </otherwise>
        </choose>
        <choose>
            <when test="opponentSortNum != null and opponentSortNum > 0">
            , #{opponentSortNum} AS OPPONENT_SORT_NUM
            </when>
            <otherwise>
            , ( SELECT COUNT(CRITERIA_MENU_NUM)+1 FROM MC_MENU_RELATION WHERE CRITERIA_MENU_NUM = #{criteriaMenuNum} AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f') )
                 AS OPPONENT_SORT_NUM
            </otherwise>
        </choose>
        FROM ( SELECT 1 ) AS A
    </script>""")
    public int insertAdmnMenuRelation(AdminMenuRelation adminMenuRelation);
    
    /***************************************************************************************************/
    
    @Select("""<script>
SELECT DISTINCT IFNULL((SELECT MENU_NUM FROM MC_MENU WHERE MENU_NUM=B.OPPONENT_MENU_NUM AND DELETE_YN='N'),0) AS SUB_OPPONENT_MENU_NUM
             , IFNULL((SELECT MENU_NUM FROM MC_MENU WHERE MENU_NUM=C.OPPONENT_MENU_NUM AND DELETE_YN='N'),0) AS SUB_SUB_OPPONENT_MENU_NUM
             , IFNULL((SELECT MENU_NUM FROM MC_MENU WHERE MENU_NUM=D.OPPONENT_MENU_NUM AND DELETE_YN='N'),0) AS SUB_SUB_SUB_OPPONENT_MENU_NUM
             , IFNULL((SELECT MENU_NUM FROM MC_MENU WHERE MENU_NUM=E.OPPONENT_MENU_NUM AND DELETE_YN='N'),0) AS SUB_SUB_SUB_SUB_OPPONENT_MENU_NUM
          FROM MC_MENU_RELATION A
          LEFT JOIN MC_MENU_RELATION B ON A.OPPONENT_MENU_NUM = B.CRITERIA_MENU_NUM
          LEFT JOIN MC_MENU BM ON B.OPPONENT_MENU_NUM = BM.MENU_NUM
          LEFT JOIN MC_MENU_RELATION C ON B.OPPONENT_MENU_NUM = C.CRITERIA_MENU_NUM
          LEFT JOIN MC_MENU_RELATION D ON C.OPPONENT_MENU_NUM = D.CRITERIA_MENU_NUM
          LEFT JOIN MC_MENU_RELATION E ON D.OPPONENT_MENU_NUM = E.CRITERIA_MENU_NUM
         WHERE A.OPPONENT_MENU_NUM = #{menuNum}
    </script>""")
    public List<AdminMenuDel> selectChildrensMenu(DeleteAdminMenu deleteModel);
    
	@ResultMap("targetRelation")
	@Select("""<script>
		SELECT
			R.CRITERIA_MENU_NUM ,
			R.OPPONENT_MENU_NUM ,
      		R.CRITERIA_MENU_LEVEL ,
      		R.OPPONENT_MENU_LEVEL ,
      		R.CRITERIA_SORT_NUM ,
			IFNULL(R.OPPONENT_SORT_NUM,0) AS OPPONENT_SORT_NUM
		FROM 
			MC_MENU_RELATION R 
			LEFT JOIN MC_MENU M ON R.OPPONENT_MENU_NUM = M.MENU_NUM
		WHERE M.DELETE_YN='N'
			AND R.CRITERIA_MENU_NUM = #{criteriaMenuNum}
			AND R.OPPONENT_MENU_NUM NOT IN (#{opponentMenuNum})
			AND R.OPPONENT_MENU_LEVEL > 0
			AND R.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
		ORDER BY R.OPPONENT_SORT_NUM
	</script>""")
	public List<AdminMenuRelation> getAdminMenuSortList(AdminMenuRelation adminMenuRelation);
	
	@Results(id="targetRelation", value = [
        @Result(property = "criteriaMenuNum", column = "CRITERIA_MENU_NUM"),
        @Result(property = "opponentMenuNum", column = "OPPONENT_MENU_NUM"),
		@Result(property = "criteriaMenuLevel", column = "CRITERIA_MENU_LEVEL"),
		@Result(property = "opponentMenuLevel", column = "OPPONENT_MENU_LEVEL"),
		@Result(property = "criteriaSortNum", column = "CRITERIA_SORT_NUM"),
		@Result(property = "opponentSortNum", column = "OPPONENT_SORT_NUM")
    ])
	@Select("""<script>
		SELECT
			R.CRITERIA_MENU_NUM ,
			R.OPPONENT_MENU_NUM ,
      		R.CRITERIA_MENU_LEVEL ,
      		R.OPPONENT_MENU_LEVEL ,
      		R.CRITERIA_SORT_NUM ,
			R.OPPONENT_SORT_NUM
		FROM 
			MC_MENU_RELATION R 
			LEFT JOIN MC_MENU M ON R.OPPONENT_MENU_NUM = M.MENU_NUM
		WHERE M.DELETE_YN='N'
			AND R.CRITERIA_MENU_NUM = #{criteriaMenuNum}
			AND R.OPPONENT_MENU_NUM = #{opponentMenuNum}
			AND R.OPPONENT_MENU_LEVEL > 0
			AND R.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
	</script>""")
	public AdminMenuRelation getTargetSortMenu(AdminMenuRelation adminMenuRelation);
	
	@Update("""<script>
		UPDATE MC_MENU_RELATION SET
			END_DATETIME = NOW(6) ,
          	MODIFY_DATETIME = NOW(6) ,
          	MODIFY_ID = #{modifyId}
       	WHERE OPPONENT_MENU_NUM = #{opponentMenuNum}
			AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
	public void updateAdminMenuSortRelation(AdminMenuRelation adminMenuRelation);
	
	@Update("""<script>
		UPDATE MC_MENU SET
			SORT_NUM = #{opponentSortNum} ,
			MODIFY_DATETIME = NOW(6) ,
			MODIFY_ID = #{modifyId}
		WHERE MENU_NUM = #{opponentMenuNum}
    </script>""")
	public int updateAdminMenuSort(AdminMenuRelation adminMenuRelation);
}
