package com.oneplat.oap.mgmt.oapservice.mapper

import com.oneplat.oap.mgmt.oapservice.model.*
import org.apache.ibatis.annotations.*

/**
 * @author lee
 * @date 2016-12-02
 */
@Mapper
interface ApiGroupMapper {

    @Insert("""<script>
                INSERT INTO MC_API_GRP
                    ( SVC_NUM
                    , API_GRP_NM
                    , API_GRP_CONTEXT
                    , SLA_USE_YN
                    , CAPACITY_USE_YN
                    , API_GRP_DESC
                    , API_GRP_USE_YN
                    , API_GRP_DELETE_YN
                    , CREATE_DATETIME
                    , CREATE_ID
                    , MODIFY_DATETIME
                    , MODIFY_ID )
            VALUES (
                      #{serviceNumber}
                    , 'GROUP'
                    , ''
                    , 'N'
                    , 'N'
                    , 'DEFAULT GROUP'
                    , 'Y'
                    , 'N'
                    , NOW()
                    , #{createId}
                    , NOW()
                    , #{modifyId}
                    )
            </script>""")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="apiGroupNumber", before=false, resultType=Long.class)
    public int insertDefaultApiGroup(ApiGroup apiGroup);

    @Insert("""<script>
                INSERT INTO MC_API_GRP_HIST
                    ( API_GRP_NUM
                    , HIST_END_DATETIME
                    , HIST_BEGIN_DATETIME
                    , SVC_NUM
                    , API_GRP_NM
                    , API_GRP_CONTEXT
                    , SLA_USE_YN
                    , CAPACITY_USE_YN
                    , API_GRP_DESC
                    , API_GRP_USE_YN
                    , API_GRP_DELETE_YN
                    , CREATE_DATETIME
                    , CREATE_ID
                    , MODIFY_DATETIME
                    , MODIFY_ID )

            SELECT
                      API_GRP_NUM
                    , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                    , NOW(6)
                    , SVC_NUM
                    , API_GRP_NM
                    , API_GRP_CONTEXT
                    , SLA_USE_YN
                    , CAPACITY_USE_YN
                    , API_GRP_DESC
                    , API_GRP_USE_YN
                    , API_GRP_DELETE_YN
                    , CREATE_DATETIME
                    , CREATE_ID
                    , MODIFY_DATETIME
                    , MODIFY_ID
             FROM MC_API_GRP
            WHERE API_GRP_NUM = #{apiGroupNumber}
            </script>""")
    public int insertApiGroupHistory(Long apiGroupNumber);

    @Insert("""<script>
                INSERT INTO MC_API_GRP_RELATION
                    ( CRITERIA_API_GRP_NUM
                    , OPPONENT_API_GRP_NUM
                    , END_DATETIME
                    , CRITERIA_API_GRP_LEVEL
                    , OPPONENT_API_GRP_LEVEL
                    , CRITERIA_SORT_NUM
                    , OPPONENT_SORT_NUM
                    , CREATE_DATETIME
                    , CREATE_ID
                    , MODIFY_DATETIME
                    , MODIFY_ID )
            VALUES (
                      0
                    , #{opponentApiGroupNumber}
                    , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                    , 0
                    , 1
                    , 1
                    , 1
                    , NOW()
                    , #{createId}
                    , NOW()
                    , #{modifyId}
                    )
            </script>""")
    public int insertDefaultApiGroupRelation(ApiGroup.ApiGroupRelation apiGroupRelation);

    @Results(id="serviceGroupResult", value = [
            @Result(property = "apiGroupNumber", column = "API_GRP_NUM"),
            @Result(property = "apiGroupName", column = "API_GRP_NM"),
            @Result(property = "apiGroupContext", column = "API_GRP_CONTEXT"),
            @Result(property = "slaUseYn", column = "SLA_USE_YN"),
            @Result(property = "capacityUseYn", column = "CAPACITY_USE_YN"),
            @Result(property = "apiGroupDescription", column = "API_GRP_DESC"),
            @Result(property = "apiGroupUseYn", column = "API_GRP_USE_YN"),
            @Result(property = "createDateTime", column = "CREATE_DATETIME"),
            @Result(property = "createId", column = "CREATE_ID"),
            @Result(property = "modifyDateTime", column = "MODIFY_DATETIME"),
            @Result(property = "modifyId", column = "MODIFY_ID"),
            @Result(property = "criteriaApiGroupNumber", column = "CRITERIA_API_GRP_NUM"),
            @Result(property = "opponentApiGroupNumber", column = "OPPONENT_API_GRP_NUM"),
            @Result(property = "criteriaApiGroupLevel", column = "CRITERIA_API_GRP_LEVEL"),
            @Result(property = "opponentApiGroupLevel", column = "OPPONENT_API_GRP_LEVEL"),
            @Result(property = "criteriaSortNumber", column = "CRITERIA_SORT_NUM"),
            @Result(property = "opponentSortNumber", column = "OPPONENT_SORT_NUM")
    ])
    @Select("""<script>
            SELECT
                   GR.API_GRP_NUM
                 , GR.API_GRP_NM
                 , GR.API_GRP_CONTEXT
                 , GR.SLA_USE_YN
                 , GR.CAPACITY_USE_YN
                 , GR.API_GRP_DESC
                 , GR.API_GRP_USE_YN
                 , GR.CREATE_DATETIME
                 , GR.CREATE_ID
                 , GR.MODIFY_DATETIME
                 , GR.MODIFY_ID
                 , GRR.CRITERIA_API_GRP_NUM
                 , GRR.OPPONENT_API_GRP_NUM
                 , GRR.CRITERIA_API_GRP_LEVEL
                 , GRR.OPPONENT_API_GRP_LEVEL
                 , GRR.CRITERIA_SORT_NUM
                 , GRR.OPPONENT_SORT_NUM
            FROM MC_API_GRP_RELATION GRR
               , MC_API_GRP          GR
           WHERE GRR.OPPONENT_API_GRP_NUM = GR.API_GRP_NUM
             AND GR.API_GRP_DELETE_YN = 'N'
             AND GR.SVC_NUM IN (0, #{serviceNumber})
             AND GRR.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
           ORDER BY GRR.OPPONENT_API_GRP_LEVEL, GRR.OPPONENT_SORT_NUM

        </script>""")
    public List<ApiGroup.ApiGroupRelation> selectServiceGroupList(long serviceNumber);

    @ResultMap("serviceGroupResult")
    @Select("""<script>
        SELECT
               GR.API_GRP_NUM
             , GR.API_GRP_NM
             , GR.API_GRP_CONTEXT
             , GR.SLA_USE_YN
             , GR.CAPACITY_USE_YN
             , GR.API_GRP_DESC
             , GR.API_GRP_USE_YN
             , GR.CREATE_DATETIME
             , GR.CREATE_ID
             , GR.MODIFY_DATETIME
             , GR.MODIFY_ID
             , GRR.CRITERIA_API_GRP_NUM
             , GRR.OPPONENT_API_GRP_NUM
             , GRR.CRITERIA_API_GRP_LEVEL
             , GRR.OPPONENT_API_GRP_LEVEL
             , GRR.CRITERIA_SORT_NUM
             , GRR.OPPONENT_SORT_NUM
        FROM MC_API_GRP_RELATION GRR
           , MC_API_GRP          GR
       WHERE GRR.OPPONENT_API_GRP_NUM = GR.API_GRP_NUM
         AND GR.API_GRP_DELETE_YN = 'N'
         AND GR.API_GRP_NUM != 0 
         AND GR.SVC_NUM IN (0, #{serviceNumber})
         AND GRR.END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
       ORDER BY GRR.OPPONENT_API_GRP_LEVEL, GRR.OPPONENT_SORT_NUM
    </script>""")
    public List<ApiGroup.ApiGroupRelation> selectServiceGroupListExceptRoot(long serviceNumber);


    @Insert("""<script>
                INSERT INTO MC_API_GRP
                    ( SVC_NUM
                    , API_GRP_NM
                    , API_GRP_CONTEXT
                    , SLA_USE_YN
                    , CAPACITY_USE_YN
                    , API_GRP_DESC
                    , API_GRP_USE_YN
                    , API_GRP_DELETE_YN
                    , CREATE_DATETIME
                    , CREATE_ID
                    , MODIFY_DATETIME
                    , MODIFY_ID )
            VALUES (
                      #{serviceNumber}
                    , #{apiGroupName}
                    , #{apiGroupContext}
                    , #{slaUseYn}
                    , #{capacityUseYn}
                    , #{apiGroupDescription}
                    , #{apiGroupUseYn}
                    , 'N'
                    , NOW()
                    , #{createId}
                    , NOW()
                    , #{modifyId}
                    )
            </script>""")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="apiGroupNumber", before=false, resultType=Long.class)
    public int insertApiGroup(ApiGroup.ApiGroupRelation apiGroup);

    @Insert("""<script>
                INSERT INTO MC_API_GRP
                    ( SVC_NUM
                    , API_GRP_NM
                    , API_GRP_DESC
                    , API_GRP_USE_YN
                    , API_GRP_DELETE_YN
                    , CREATE_DATETIME
                    , CREATE_ID
                    , MODIFY_DATETIME
                    , MODIFY_ID )
            VALUES (
                      #{serviceNumber}
                    , #{apiGroupName}
                    , #{apiGroupDescription}
                    , #{apiGroupUseYn}
                    , 'N'
                    , NOW()
                    , #{createId}
                    , NOW()
                    , #{modifyId}
                    )
            </script>""")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="apiGroupNumber", before=false, resultType=Long.class)
    public int insertSecondApiGroup(ApiGroup.ApiGroupRelation apiGroup);

    @Insert("""<script>
                INSERT INTO MC_API_GRP_RELATION
                    ( CRITERIA_API_GRP_NUM
                    , OPPONENT_API_GRP_NUM
                    , END_DATETIME
                    , CRITERIA_API_GRP_LEVEL
                    , OPPONENT_API_GRP_LEVEL
                    , CRITERIA_SORT_NUM
                    , OPPONENT_SORT_NUM
                    , CREATE_DATETIME
                    , CREATE_ID
                    , MODIFY_DATETIME
                    , MODIFY_ID )
            VALUES (
                      #{criteriaApiGroupNumber}
                    , #{opponentApiGroupNumber}
                    , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                    , #{criteriaApiGroupLevel}
                    , #{opponentApiGroupLevel}
                    , #{criteriaSortNumber}
                    , (SELECT MAX(IFNULL(OPPONENT_SORT_NUM, 0)) + 1
                         FROM MC_API_GRP_RELATION A
                        WHERE A.CRITERIA_API_GRP_NUM = #{criteriaApiGroupNumber}
                    )
                    , NOW()
                    , #{createId}
                    , NOW()
                    , #{modifyId}
                    )
            </script>""")
    public int insertApiGroupRelation(ApiGroup.ApiGroupRelation apiGroupRelation);

    @Update("""
        UPDATE MC_API_GRP
        SET
                API_GRP_NM = #{apiGroupName}
              , API_GRP_DESC = #{apiGroupDescription}
              , API_GRP_USE_YN = #{apiGroupUseYn}
              , MODIFY_DATETIME = NOW()
              , MODIFY_ID = #{modifyId}
        WHERE SVC_NUM = #{serviceNumber}
          AND API_GRP_NUM = #{apiGroupNumber}
    """)
    public int updateSecondApiGroup(ApiGroup.ApiGroupRelation apiGroupRelation);

    @Update("""<script>
        UPDATE MC_API_GRP_HIST
        SET
           HIST_END_DATETIME = NOW(6)
        WHERE API_GRP_NUM = #{apiGroupNumber}
          AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
    public int updateApiGroupHistory(Long apiGroupNumber);

    @Update("""
        UPDATE MC_API_GRP
        SET
                API_GRP_NM = #{apiGroupName}
              , API_GRP_CONTEXT = #{apiGroupContext}
              , SLA_USE_YN = #{slaUseYn}
              , CAPACITY_USE_YN = #{capacityUseYn}
              , API_GRP_DESC = #{apiGroupDescription}
              , API_GRP_USE_YN = #{apiGroupUseYn}
              , MODIFY_DATETIME = NOW()
              , MODIFY_ID = #{modifyId}
        WHERE SVC_NUM = #{serviceNumber}
          AND API_GRP_NUM = #{apiGroupNumber}
    """)
    public int updateApiGroup(ApiGroup.ApiGroupRelation apiGroupRelation);

    @ResultMap("serviceGroupResult")
    @Select("""<script>
         SELECT   MAGR.CRITERIA_API_GRP_NUM
                , MAGR.OPPONENT_API_GRP_NUM
                , MAGR.CRITERIA_API_GRP_LEVEL
                , MAGR.OPPONENT_API_GRP_LEVEL
                , MAGR.CRITERIA_SORT_NUM
                , IFNULL(MAGR.OPPONENT_SORT_NUM, 0) AS OPPONENT_SORT_NUM
           FROM MC_API_GRP_RELATION MAGR
              , MC_API_GRP MAG
          WHERE MAGR.OPPONENT_API_GRP_NUM = MAG.API_GRP_NUM
            AND MAGR.CRITERIA_API_GRP_NUM = #{criteriaApiGroupNumber}
            AND MAGR.OPPONENT_API_GRP_NUM NOT IN (#{opponentApiGroupNumber})
            AND MAGR.OPPONENT_API_GRP_LEVEL > 0
            AND MAGR.END_DATETIME > NOW()
            AND MAG.SVC_NUM = #{serviceNumber}
            AND MAG.API_GRP_DELETE_YN = 'N'
          ORDER BY MAGR.OPPONENT_SORT_NUM
    </script>""")
    public List<ApiGroup.ApiGroupRelation> selectGroupRelationOrderList(ApiGroup.ApiGroupRelation apiGroupRelation);

    @ResultMap("serviceGroupResult")
    @Select("""<script>
         SELECT   MAGR.CRITERIA_API_GRP_NUM
                , MAGR.OPPONENT_API_GRP_NUM
                , MAGR.CRITERIA_API_GRP_LEVEL
                , MAGR.OPPONENT_API_GRP_LEVEL
                , MAGR.CRITERIA_SORT_NUM
           FROM MC_API_GRP_RELATION MAGR
          WHERE MAGR.CRITERIA_API_GRP_NUM = #{criteriaApiGroupNumber}
            AND MAGR.OPPONENT_API_GRP_NUM = #{opponentApiGroupNumber}
            AND END_DATETIME > NOW()
    </script>""")
    public ApiGroup.ApiGroupRelation selectGroupRelationOrder(ApiGroup.ApiGroupRelation apiGroupRelation);

    @Update("""
        UPDATE MC_API_GRP_RELATION
        SET
                END_DATETIME = NOW(6)
              , MODIFY_DATETIME = NOW()
              , MODIFY_ID = #{modifyId}
        WHERE END_DATETIME > NOW()
          AND CRITERIA_API_GRP_NUM = #{criteriaApiGroupNumber}
          AND OPPONENT_API_GRP_NUM = #{opponentApiGroupNumber}
    """)
    public int updateApiGroupRelationDelete(ApiGroup.ApiGroupRelation apiGroupRelation);

    @Insert("""<script>
                INSERT INTO MC_API_GRP_RELATION
                    ( CRITERIA_API_GRP_NUM
                    , OPPONENT_API_GRP_NUM
                    , END_DATETIME
                    , CRITERIA_API_GRP_LEVEL
                    , OPPONENT_API_GRP_LEVEL
                    , CRITERIA_SORT_NUM
                    , OPPONENT_SORT_NUM
                    , CREATE_DATETIME
                    , CREATE_ID
                    , MODIFY_DATETIME
                    , MODIFY_ID )
            VALUES (
                      #{criteriaApiGroupNumber}
                    , #{opponentApiGroupNumber}
                    , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                    , #{criteriaApiGroupLevel}
                    , #{opponentApiGroupLevel}
                    , #{criteriaSortNumber}
                    , #{opponentSortNumber}
                    , NOW()
                    , #{createId}
                    , NOW()
                    , #{modifyId}
                    )
            </script>""")
    public int insertApiGroupRelationSort(ApiGroup.ApiGroupRelation apiGroupRelation);

    @Update("""
        UPDATE MC_API_GRP
        SET
                API_GRP_DELETE_YN = 'Y'
              , MODIFY_DATETIME = NOW()
              , MODIFY_ID = #{modifyId}
        WHERE SVC_NUM = #{serviceNumber}
          AND API_GRP_NUM = #{apiGroupNumber}
    """)
    public int deleteApiGroup(ApiGroup.ApiGroupRelation apiGroupRelation);

    @Update("""
        UPDATE MC_API_GRP
        SET
                API_GRP_DELETE_YN = 'Y'
              , MODIFY_DATETIME = NOW()
              , MODIFY_ID = #{modifyId}
        WHERE SVC_NUM = #{serviceNumber}
    """)
    public int deleteServiceAllApiGroup(OapService oapService);

    @Update("""<script>
        UPDATE MC_API_GRP_HIST
        SET
           HIST_END_DATETIME = NOW(6)
        WHERE SVC_NUM = #{serviceNumber}
          AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
    public int updateServiceAllApiGroupHistory(Long serviceNumber);

    @Insert("""<script>
                INSERT INTO MC_API_GRP_HIST
                    ( API_GRP_NUM
                    , HIST_END_DATETIME
                    , HIST_BEGIN_DATETIME
                    , SVC_NUM
                    , API_GRP_NM
                    , API_GRP_CONTEXT
                    , SLA_USE_YN
                    , CAPACITY_USE_YN
                    , API_GRP_DESC
                    , API_GRP_USE_YN
                    , API_GRP_DELETE_YN
                    , CREATE_DATETIME
                    , CREATE_ID
                    , MODIFY_DATETIME
                    , MODIFY_ID )

            SELECT
                      API_GRP_NUM
                    , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                    , NOW(6)
                    , SVC_NUM
                    , API_GRP_NM
                    , API_GRP_CONTEXT
                    , SLA_USE_YN
                    , CAPACITY_USE_YN
                    , API_GRP_DESC
                    , API_GRP_USE_YN
                    , API_GRP_DELETE_YN
                    , CREATE_DATETIME
                    , CREATE_ID
                    , MODIFY_DATETIME
                    , MODIFY_ID
             FROM MC_API_GRP
            WHERE SVC_NUM = #{serviceNumber}
            </script>""")
    public int insertServiceAllApiGroupHistory(Long serviceNumber);

    @Select("""<script>
         SELECT SVC_NUM
           FROM MC_API_GRP
          WHERE API_GRP_NUM = #{apiGroupNumber}
            AND API_GRP_USE_YN = 'Y'
            AND API_GRP_DELETE_YN = 'N'
    </script>""")
    public Long selectGroupServiceId(Long apiGroupNumber);
}