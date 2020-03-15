package com.oneplat.oap.mgmt.policies.mapper

import com.oneplat.oap.mgmt.policies.model.ServicePolicy
import org.apache.ibatis.annotations.*

/**
 * Created by LSH on 2016. 11. 30..
 */

@Mapper
interface CapacityMapper {

    @Results(id="servicePolicyList", value=[
            @Result(property = "serviceNumber", column="serviceNumber"),
            @Result(property = "apiGroupNumber", column="apiGroupNumber"),
            @Result(property = "policyCode", column="policyCode"),
            @Result(property = "gradeCode", column="gradeCode"),
            @Result(property = "testGradeCode", column="testGradeCode"),
            @Result(property = "limitQuantity", column="limitQuantity"),
            @Result(property = "criteriaCode", column="criteriaCode"),
            @Result(property = "policyDeleteYn", column="policyDeleteYn"),
            @Result(property = "createDateTime", column="createDateTime"),
            @Result(property = "createId", column="createId"),
            @Result(property = "modifyDateTime", column="modifyDateTime"),
            @Result(property = "modifyId", column="modifyId"),
            @Result(property = "groupList", column="serviceNumber", many=@Many(select = "capacityPoliciesGroupList"))
    ])
    @Select("""
         SELECT
             A.serviceNumber
            ,A.apiGroupNumber
            ,A.policyCode
            ,(SELECT SVC_NM FROM MC_SVC WHERE SVC_NUM = A.serviceNumber) AS serviceName
            ,SUBSTRING_INDEX(A.gradeCode, '-', 1) AS gradeCode
            ,SUBSTRING_INDEX(A.gradeCode, '-', -1) AS testGradeCode
            ,SUBSTRING_INDEX(A.limitQuantity, '-', 1) AS limitQuantity
            ,SUBSTRING_INDEX(A.limitQuantity, '-', -1) AS testLimitQuantity
            ,SUBSTRING_INDEX(A.criteriaCode, '-', 1) AS criteriaCode
            ,SUBSTRING_INDEX(A.criteriaCode, '-', -1) AS testCriteriaCode
            ,A.policyDeleteYn
            ,A.createDateTime
            ,A.createId
            ,A.modifyDateTime
            ,A.modifyId
        FROM
            (
                SELECT
                SVC_NUM AS serviceNumber
                ,API_GRP_NUM AS apiGroupNumber
                ,SVC_POLICY_CD AS policyCode
                ,GROUP_CONCAT(SVC_GRADE_CD SEPARATOR '-') AS gradeCode
                ,GROUP_CONCAT(SVC_LIMIT_QTY SEPARATOR '-') AS limitQuantity
                ,GROUP_CONCAT(SVC_CRITERIA_CD SEPARATOR '-') AS criteriaCode
                ,SVC_POLICY_DELETE_YN AS policyDeleteYn
                ,CREATE_DATETIME AS createDateTime
                ,CREATE_ID AS createId
                ,MODIFY_DATETIME AS modifyDateTime
                ,MODIFY_ID AS modifyId
               FROM
             MC_SVC_POLICY
               WHERE
             API_GRP_NUM = 0
             AND SVC_POLICY_DELETE_YN = 'N'
             AND SVC_POLICY_CD = 'MC_SVC_POLICY_01'
               GROUP BY
             SVC_NUM) A

    """)
    List<ServicePolicy> capacityPoliciesList();

    @Select("""
         SELECT
              A.serviceNumber
             ,A.apiGroupNumber
             ,A.policyCode
             ,(SELECT SVC_NM FROM MC_SVC WHERE SVC_NUM = A.serviceNumber) AS serviceName
             ,(SELECT API_GRP_NM FROM MC_API_GRP WHERE SVC_NUM = A.serviceNumber AND API_GRP_NUM = A.apiGroupNumber) AS  apiGroupName
             ,SUBSTRING_INDEX(A.gradeCode, '-', 1) AS gradeCode
             ,SUBSTRING_INDEX(A.gradeCode, '-', -1) AS testGradeCode
             ,SUBSTRING_INDEX(A.limitQuantity, '-', 1) AS limitQuantity
             ,SUBSTRING_INDEX(A.limitQuantity, '-', -1) AS testLimitQuantity
             ,SUBSTRING_INDEX(A.criteriaCode, '-', 1) AS criteriaCode
             ,SUBSTRING_INDEX(A.criteriaCode, '-', -1) AS testCriteriaCode
             ,A.policyDeleteYn
             ,A.createDateTime
             ,A.createId
             ,A.modifyDateTime
             ,A.modifyId
         FROM
              (SELECT
              SVC_NUM AS serviceNumber
             ,API_GRP_NUM AS apiGroupNumber
             ,SVC_POLICY_CD AS policyCode
             ,GROUP_CONCAT(SVC_GRADE_CD SEPARATOR '-') AS gradeCode
             ,GROUP_CONCAT(SVC_LIMIT_QTY SEPARATOR '-') AS limitQuantity
             ,GROUP_CONCAT(SVC_CRITERIA_CD SEPARATOR '-') AS criteriaCode
             ,SVC_POLICY_DELETE_YN AS policyDeleteYn
             ,CREATE_DATETIME AS createDateTime
             ,CREATE_ID AS createId
             ,MODIFY_DATETIME AS modifyDateTime
             ,MODIFY_ID AS modifyId
          FROM
             MC_SVC_POLICY
         WHERE
             API_GRP_NUM != 0
             AND SVC_NUM = #{serviceNumber}
             AND SVC_POLICY_DELETE_YN = 'N'
             AND SVC_POLICY_CD = 'MC_SVC_POLICY_01'
               GROUP BY
             SVC_NUM, API_GRP_NUM) A
    """)
    List<ServicePolicy> capacityPoliciesGroupList(Long serviceNumber);



    /**
     * Capacity Update
     * */

    @Update("""
        UPDATE MC_SVC_POLICY
    SET SVC_LIMIT_QTY = #{limitQuantity}, SVC_CRITERIA_CD = #{criteriaCode}, MODIFY_ID=#{modifyId}, MODIFY_DATETIME=NOW()
    WHERE
    SVC_NUM = #{serviceNumber}
    AND API_GRP_NUM = #{apiGroupNumber}
    AND SVC_POLICY_CD = #{policyCode}
    AND SVC_GRADE_CD = #{gradeCode}
    """)
    int updateCapacity(ServicePolicy servicePolicy);

    @Update("""
        UPDATE MC_SVC_POLICY_HIST
        SET
            HIST_END_DATETIME = NOW(), MODIFY_ID=#{modifyId}, MODIFY_DATETIME=NOW()
        WHERE
            SVC_NUM = #{serviceNumber}
            AND API_GRP_NUM = #{apiGroupNumber}
            AND SVC_POLICY_CD = #{policyCode}
            AND SVC_GRADE_CD = #{gradeCode}
			AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    """)
    int updateHistCapacity(ServicePolicy servicePolicy);

    @Insert("""
       INSERT INTO MC_SVC_POLICY_HIST
            (SVC_NUM, API_GRP_NUM, SVC_POLICY_CD, SVC_GRADE_CD,
            HIST_END_DATETIME, HIST_BEGIN_DATETIME, SVC_LIMIT_QTY,
            SVC_CRITERIA_CD, SVC_POLICY_DELETE_YN,
            CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID)
        SELECT
            SVC_NUM
            ,API_GRP_NUM
            ,SVC_POLICY_CD
            ,SVC_GRADE_CD
            ,STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
            ,NOW()
            ,SVC_LIMIT_QTY
            ,SVC_CRITERIA_CD
            ,SVC_POLICY_DELETE_YN
            ,CREATE_DATETIME
            ,CREATE_ID
            ,MODIFY_DATETIME
            ,MODIFY_ID
        FROM MC_SVC_POLICY
        WHERE
            SVC_NUM = #{serviceNumber}
            AND API_GRP_NUM = #{apiGroupNumber}
            AND SVC_POLICY_CD = #{policyCode}
            AND SVC_GRADE_CD = #{gradeCode}
    """)
    int insertHistCapacity(ServicePolicy servicePolicy);


/**
 * 개발(test)용 업데이트
 * */
    @Update("""
        UPDATE MC_SVC_POLICY
    SET SVC_LIMIT_QTY = #{testLimitQuantity}, SVC_CRITERIA_CD = #{testCriteriaCode}, MODIFY_ID=#{modifyId}, MODIFY_DATETIME=NOW()
    WHERE
    SVC_NUM = #{serviceNumber}
    AND API_GRP_NUM = #{apiGroupNumber}
    AND SVC_POLICY_CD = #{policyCode}
    AND SVC_GRADE_CD = #{testGradeCode}
    """)
    int updateCapacityTest(ServicePolicy servicePolicy);

    @Update("""
        UPDATE MC_SVC_POLICY_HIST
        SET
            HIST_END_DATETIME = NOW(), MODIFY_ID=#{modifyId}, MODIFY_DATETIME=NOW()
        WHERE
            SVC_NUM = #{serviceNumber}
            AND API_GRP_NUM = #{apiGroupNumber}
            AND SVC_POLICY_CD = #{policyCode}
            AND SVC_GRADE_CD = #{testGradeCode}
			AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    """)
    int updateHistCapacityTest(ServicePolicy servicePolicy);

    @Insert("""
        INSERT INTO MC_SVC_POLICY_HIST
            (SVC_NUM, API_GRP_NUM, SVC_POLICY_CD, SVC_GRADE_CD,
            HIST_END_DATETIME, HIST_BEGIN_DATETIME, SVC_LIMIT_QTY,
            SVC_CRITERIA_CD, SVC_POLICY_DELETE_YN,
            CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID)
        SELECT
            SVC_NUM
            ,API_GRP_NUM
            ,SVC_POLICY_CD
            ,SVC_GRADE_CD
            ,STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
            ,NOW()
            ,SVC_LIMIT_QTY
            ,SVC_CRITERIA_CD
            ,SVC_POLICY_DELETE_YN
            ,CREATE_DATETIME
            ,CREATE_ID
            ,MODIFY_DATETIME
            ,MODIFY_ID
        FROM MC_SVC_POLICY
        WHERE
            SVC_NUM = #{serviceNumber}
            AND API_GRP_NUM = #{apiGroupNumber}
            AND SVC_POLICY_CD = #{policyCode}
            AND SVC_GRADE_CD = #{testGradeCode}
    """)
    int insertHistCapacityTest(ServicePolicy servicePolicy);

    /**
     * Lee
     * 서비스 등록시 생성되는 CAPACITI
     * */
    @Insert("""
        INSERT INTO MC_SVC_POLICY
                ( SVC_NUM
                , API_GRP_NUM
                , SVC_POLICY_CD
                , SVC_GRADE_CD
                , SVC_POLICY_DELETE_YN
                , SVC_CRITERIA_CD
                , CREATE_DATETIME
                , CREATE_ID
                , MODIFY_DATETIME
                , MODIFY_ID )
          VALUES (
                  #{serviceNumber}
                , #{apiGroupNumber}
                , #{policyCode}
                , #{gradeCode}
                , 'N'
                , 'MC_SVC_CRITERIA_01'
                , NOW()
                , #{createId}
                , NOW()
                , #{modifyId}
                 )
    """)
    int insertServiceCapacity(ServicePolicy servicePolicy);

    @Insert("""
        INSERT INTO MC_SVC_POLICY_HIST
                  ( SVC_NUM
                  , API_GRP_NUM
                  , SVC_POLICY_CD
                  , SVC_GRADE_CD
                  , HIST_END_DATETIME
                  , HIST_BEGIN_DATETIME
                  , SVC_LIMIT_QTY
                  , SVC_CRITERIA_CD
                  , SVC_POLICY_DELETE_YN
                  , CREATE_DATETIME
                  , CREATE_ID
                  , MODIFY_DATETIME
                  , MODIFY_ID )
            SELECT
                    SVC_NUM
                  , API_GRP_NUM
                  , SVC_POLICY_CD
                  , SVC_GRADE_CD
                  , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                  , NOW(6)
                  , SVC_LIMIT_QTY
                  , SVC_CRITERIA_CD
                  , SVC_POLICY_DELETE_YN
                  , CREATE_DATETIME
                  , CREATE_ID
                  , MODIFY_DATETIME
                  , MODIFY_ID
              FROM MC_SVC_POLICY
              WHERE SVC_NUM = #{serviceNumber}
                AND API_GRP_NUM = #{apiGroupNumber}
                AND SVC_POLICY_CD = 'MC_SVC_POLICY_01'
    """)
    int insertServiceCapacityHistory(ServicePolicy servicePolicy);

    @Update("""
        UPDATE MC_SVC_POLICY_HIST
        SET
           HIST_END_DATETIME = NOW(6)
        WHERE SVC_NUM = #{serviceNumber}
          AND API_GRP_NUM = #{apiGroupNumber}
          AND SVC_POLICY_CD = 'MC_SVC_POLICY_01'
          AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    """)
    public int updateServiceCapacityHistory(ServicePolicy servicePolicy);

    /**
     * Lee
     * 서비스 수정시 POLICY 여부 따라 생성 또는 수정 CAPACITI
     * */
    @Update("""
        INSERT INTO MC_SVC_POLICY
                ( SVC_NUM
                , API_GRP_NUM
                , SVC_POLICY_CD
                , SVC_GRADE_CD
                , SVC_POLICY_DELETE_YN
                , SVC_CRITERIA_CD
                , CREATE_DATETIME
                , CREATE_ID
                , MODIFY_DATETIME
                , MODIFY_ID )
          VALUES (
                  #{serviceNumber}
                , #{apiGroupNumber}
                , #{policyCode}
                , #{gradeCode}
                , 'N'
                , 'MC_SVC_CRITERIA_01'
                , NOW()
                , #{createId}
                , NOW()
                , #{modifyId}
                 )
          ON DUPLICATE KEY UPDATE
                  SVC_POLICY_DELETE_YN = 'N'
                , MODIFY_DATETIME = NOW()
                , MODIFY_ID = #{modifyId}
    """)
    int updateServiceCapacity(ServicePolicy servicePolicy);

    @Update("""
        UPDATE MC_SVC_POLICY
        SET
                SVC_POLICY_DELETE_YN = 'Y'
              , MODIFY_DATETIME = NOW()
              , MODIFY_ID = #{modifyId}
        WHERE SVC_NUM = #{serviceNumber}
          AND API_GRP_NUM = #{apiGroupNumber}
          AND SVC_POLICY_CD = 'MC_SVC_POLICY_01'
    """)
    public int updateServiceCapacityDelete(ServicePolicy servicePolicy);

    @Update("""
        UPDATE MC_SVC_POLICY
        SET
                SVC_POLICY_DELETE_YN = 'Y'
              , MODIFY_DATETIME = NOW()
              , MODIFY_ID = #{modifyId}
        WHERE SVC_NUM = #{serviceNumber}
    """)
    public int updateServicePolicyDelete(ServicePolicy servicePolicy);

    @Update("""
        UPDATE MC_SVC_POLICY_HIST
        SET
           HIST_END_DATETIME = NOW(6)
        WHERE SVC_NUM = #{serviceNumber}
          AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    """)
    public int updateServicePolicyTotalHistory(long serviceNumber);

    @Insert("""
        INSERT INTO MC_SVC_POLICY_HIST
                  ( SVC_NUM
                  , API_GRP_NUM
                  , SVC_POLICY_CD
                  , SVC_GRADE_CD
                  , HIST_END_DATETIME
                  , HIST_BEGIN_DATETIME
                  , SVC_LIMIT_QTY
                  , SVC_CRITERIA_CD
                  , SVC_POLICY_DELETE_YN
                  , CREATE_DATETIME
                  , CREATE_ID
                  , MODIFY_DATETIME
                  , MODIFY_ID )
            SELECT
                    SVC_NUM
                  , API_GRP_NUM
                  , SVC_POLICY_CD
                  , SVC_GRADE_CD
                  , STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
                  , NOW(6)
                  , SVC_LIMIT_QTY
                  , SVC_CRITERIA_CD
                  , SVC_POLICY_DELETE_YN
                  , CREATE_DATETIME
                  , CREATE_ID
                  , MODIFY_DATETIME
                  , MODIFY_ID
              FROM MC_SVC_POLICY
              WHERE SVC_NUM = #{serviceNumber}
    """)
    int insertServicePolicyTotalHistory(Long serviceNumber);
}