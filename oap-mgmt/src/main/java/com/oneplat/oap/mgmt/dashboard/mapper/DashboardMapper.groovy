package com.oneplat.oap.mgmt.dashboard.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

/**
 *
 * DashboardMapper
 *
 * Created by chungyeol.kim on 2017-02-06.
 */
@Mapper
interface DashboardMapper {

    @Select("""
        SELECT COUNT(*) AS cnt
          FROM MC_OPR OPR
         WHERE OPR.OPR_STATE_CD = 'MC_OPR_STATE_01'
    """)
    public int selectOperatorApprovalCount();

    @Select("""
    <script>
        SELECT COUNT(*) AS cnt
          FROM MC_SCHED_LOG SCH
         WHERE SCH.SUCCESS_YN = 'N'
         <![CDATA[
           AND (DATE_FORMAT(DATE_ADD(NOW(), interval-1 week), '%Y-%m-%d') <= DATE_FORMAT(SCH.CREATE_DATETIME, '%Y-%m-%d'))
         ]]>
    </script>
    """)
    public int selectScheduleFailCount();
}