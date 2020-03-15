package com.oneplat.oap.mgmt.setting.admin.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Many
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu
import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu.MenuTree
import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu.RoleMenu
import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu.SaveGroupMenu

@Mapper
public interface MenuGroupMapper {

    @Results(id="menuRsult", value = [
        @Result(property = "menuNumber", column = "MENU_NUM"),
        @Result(property = "menuName", column = "MENU_NM"),
        @Result(property = "roleNumber", column = "ROLE_NUM"),
        @Result(property = "subMenuList", column="{menuNumber=MENU_NUM,roleNumber=ROLE_NUM, hasFlag=HAS_FLAG}", many=@Many(select = "selectRoleMenu"))
    ])
    @Select("""<script>
        SELECT MENU_NUM, MENU_NM, #{roleNumber} AS ROLE_NUM, #{hasFlag} AS HAS_FLAG
        FROM MC_MENU
        WHERE MENU_LEVEL = 0
            AND DELETE_YN = 'N'
    </script>""")
    public MenuTree selectRootMenu(GroupMenu groupMenu);

    @Results(id="subMenuRsult", value = [
        @Result(property = "menuNumber", column = "MENU_NUM"),
        @Result(property = "menuName", column = "MENU_NM"),
        @Result(property = "criteriaMenuNumber", column = "CRITERIA_MENU_NUM"),
        @Result(property = "useYn", column = "USE_YN"),
        @Result(property = "roleNumber", column = "ROLE_NUM"),
        @Result(property = "authSetupYn", column = "AUTH_SETUP_YN"),
        @Result(property = "menuAuthCode", column = "MENU_AUTH_CD"),
        @Result(property = "subMenuList", column="{menuNumber=MENU_NUM,roleNumber=ROLE_NUM,hasFlag=HAS_FLAG}", many=@Many(select = "selectRoleMenu"))
    ])
    @Select("""<script>
        SELECT MENU_NUM, MENU_NM, CRITERIA_MENU_NUM, USE_YN, AUTH_SETUP_YN, MENU_AUTH_CD, #{roleNumber} AS ROLE_NUM, #{hasFlag} AS HAS_FLAG
        FROM ( 
            SELECT T1.MENU_NUM, MENU_NM, CRITERIA_MENU_NUM, OPPONENT_SORT_NUM, USE_YN, AUTH_SETUP_YN, MENU_AUTH_CD
            FROM (
                SELECT A.OPPONENT_MENU_NUM AS MENU_NUM, B.MENU_NM, A.CRITERIA_MENU_NUM, B.USE_YN, B.AUTH_SETUP_YN, A.END_DATETIME, A.OPPONENT_SORT_NUM
                FROM MC_MENU_RELATION AS A, MC_MENU AS B
                WHERE A.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59', '%Y-%m-%d %H:%i:%s')
                    AND A.OPPONENT_MENU_NUM = B.MENU_NUM
                    AND A.CRITERIA_MENU_NUM = #{menuNumber}
            ) T1 LEFT JOIN MC_ROLE_MENU T2 ON T1.MENU_NUM = T2.MENU_NUM AND T1.END_DATETIME = T2.END_DATETIME AND T2.ROLE_NUM = #{roleNumber}
            GROUP BY MENU_NUM
        ) TT
        <if test="hasFlag">
        WHERE MENU_AUTH_CD IS NOT NULL
        </if>
        ORDER BY OPPONENT_SORT_NUM
    </script>""")
    public MenuTree selectRoleMenu(GroupMenu groupMenu);
    
    @Results(id="roleMenuRsult", value = [
        @Result(property = "criteriaMenuNumber", column = "CRITERIA_MENU_NUM"),
        @Result(property = "menuNumber", column = "MENU_NUM"),
        @Result(property = "menuName", column = "MENU_NM"),
        @Result(property = "menuLevel", column = "MENU_LEVEL"),
        @Result(property = "authSetupYn", column = "AUTH_SETUP_YN"),
        @Result(property = "exposureYn", column = "EXPOSURE_YN"),
        @Result(property = "useYn", column = "USE_YN"),
        @Result(property = "roleNumber", column = "ROLE_NUM"),
        @Result(property = "menuAuthCode", column = "MENU_AUTH_CD")
    ])
    @Select("""<script>
    SELECT TT1.MENU_NUM, TT1.MENU_NM, TT1.MENU_LEVEL, TT1.AUTH_SETUP_YN, TT1.EXPOSURE_YN, TT1.USE_YN, TT1.ROLE_NUM, TT1.MENU_AUTH_CD
        , TT2.CRITERIA_MENU_NUM
    FROM (
            SELECT MENU_NUM, MENU_NM, MENU_LEVEL, AUTH_SETUP_YN, EXPOSURE_YN, USE_YN, #{roleNumber} AS ROLE_NUM, null AS MENU_AUTH_CD
            FROM MC_MENU
            WHERE MENU_LEVEL = 0
				AND MENU_TYPE_CD = #{menuTypeCode}
            UNION ALL
            SELECT T1.MENU_NUM, T1.MENU_NM, T1.MENU_LEVEL, T1.AUTH_SETUP_YN, T1.EXPOSURE_YN, T1.USE_YN, T2.ROLE_NUM, T2.MENU_AUTH_CD
            FROM MC_MENU T1, MC_ROLE_MENU T2
            WHERE T1.MENU_NUM = T2.MENU_NUM
                AND T1.DELETE_YN = 'N'
				AND T1.MENU_TYPE_CD = #{menuTypeCode} 
                AND T2.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                AND T2.ROLE_NUM = #{roleNumber}
        ) TT1  LEFT JOIN MC_MENU_RELATION TT2
        ON TT1.MENU_NUM = TT2.OPPONENT_MENU_NUM AND TT2.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
    public List<RoleMenu> selectRoleMenus(GroupMenu groupMenu);
    
    @Update("""<script>
        UPDATE MC_ROLE_MENU
        SET END_DATETIME = NOW(6)
            , MODIFY_DATETIME = NOW(6)
            , MODIFY_ID = #{modifyId}
        WHERE ROLE_NUM = #{roleNumber}
            AND MENU_NUM = #{menuNumber}
            AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
    public int updateRoleMenu(GroupMenu groupMenu);
    
    @Insert("""<script>
        INSERT INTO
			MC_ROLE_MENU(
				ROLE_NUM, 
				MENU_NUM, 
				MENU_AUTH_CD, 
				END_DATETIME, 
				CREATE_DATETIME, 
				CREATE_ID)
        VALUES (
			#{roleNumber}, 
			#{menuNumber}, 
			#{menuAuthCode}, 
			STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f'), 
			NOW(6), 
			#{createId} )
    </script>""")
    public int insertRoleMenu(GroupMenu groupMenu);

    @Insert("""<script>
        INSERT INTO MC_ROLE_MENU(ROLE_NUM, MENU_NUM, END_DATETIME, MENU_AUTH_CD, CREATE_DATETIME, CREATE_ID)
        SELECT T1.ROLE_NUM, T2.MENU_NUM, STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f'), T2.MENU_AUTH_CD, NOW(6), #{createId}
        FROM MC_ROLE T1, MC_MENU T2
        WHERE T1.DELETE_YN = 'N'
        AND T1.DELETE_YN = T2.DELETE_YN
        AND T2.MENU_LEVEL <![CDATA[>]]> 0
        AND T1.ROLE_NUM = #{roleNumber}
        AND T2.MENU_NUM IN (
            <foreach collection="groupMenuList" item="item" separator=",">
                #{item.menuNumber}
            </foreach>
        )
        ON DUPLICATE KEY UPDATE END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
    public int insertRoleMenuList(SaveGroupMenu saveGroupMenu);
    
    @Update("""<script>
        UPDATE MC_ROLE_MENU
        SET END_DATETIME = NOW(6)
            , MODIFY_DATETIME = NOW(6)
            , MODIFY_ID = #{modifyId}
        WHERE ROLE_NUM = #{roleNumber}
            AND END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
            AND MENU_NUM IN (
            <foreach collection="groupMenuList" item="item" separator=",">
                #{item.menuNumber}
            </foreach>
        )
    </script>""")
    public int updateRoleMenuList(SaveGroupMenu saveGroupMenu);

}
