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
import com.oneplat.oap.mgmt.setting.system.model.Schedule
import com.oneplat.oap.mgmt.setting.system.model.ScheduleLog
import com.oneplat.oap.mgmt.setting.system.model.Schedule.CreateSchedule
import com.oneplat.oap.mgmt.setting.system.model.Schedule.DeleteSchedule
import com.oneplat.oap.mgmt.setting.system.model.Schedule.ModifySchedule

@Mapper
public interface SchedulerMapper {

    @Results(id="schedules", value=[
        @Result(property="scheduleNumber", column="SCHED_NUM"),
        @Result(property="scheduleName", column="SCHED_NM"),
        @Result(property="scheduleDescribe", column="SCHED_DESC"),
        @Result(property="className", column="SCHED_CLASS_NM"),
        @Result(property="inputSectionCode", column="INPUT_SECTION_CD"),
        @Result(property="executionCycle", column="EXEC_CYCLE"),
        @Result(property="useYn", column="USE_YN"),
        @Result(property="createDateTime", column="CREATE_DATETIME"),
        @Result(property="createId", column="CREATE_ID"),
        @Result(property="modifyDateTime", column="MODIFY_DATETIME"),
        @Result(property="modifyId", column="MODIFY_ID"),
        @Result(property="sortNumber", column="SORT_NUMBER")
    ])
    @Select("""<script>
     SELECT * FROM
      MC_SCHED
       <if test="query!=null">
        <where>
         <if test="query.yesNoCode!=null and query.yesNoCode!=''">
          USE_YN=#{query.yesNoCode}
         </if>
         <if test="query.startTime!=null and query.startTime!=''">
          <if test="query.dateSectionCode=='create'">
           AND CREATE_DATETIME <![CDATA[>=]]> #{query.startTime}
          </if>
          <if test="query.dateSectionCode=='modify'">
           AND MODIFY_DATETIME <![CDATA[>=]]> #{query.startTime}
          </if>
         </if>
        <if test="query.endTime!=null and query.endTime!=''">
         <if test="query.dateSectionCode=='create'">
          AND CREATE_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endTime},INTERVAL 1 DAY)
         </if>
         <if test="query.dateSectionCode=='modify'">
          AND MODIFY_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endTime},INTERVAL 1 DAY)
         </if>
        </if>
       <if test="query.scheduleName!=null and query.scheduleName!=''">
         AND SCHED_NM LIKE CONCAT('%',#{query.scheduleName},'%')
       </if>
         AND DELETE_YN='N'
       </where>
      </if>
    <if test="pageInfo!=null">
      <if test="query.sortSection!=null and query.sortSection!=''">
        <if test="query.sortSection=='scheduleNumber'">
         <if test="query.sort=='desc'">
          ORDER BY SCHED_NUM DESC
         </if>
         <if test="query.sort!='desc'">
          ORDER BY SCHED_NUM
         </if>
        </if>
        <if test="query.sortSection=='scheduleName'">
         <if test="query.sort=='desc'">
          ORDER BY SCHED_NM DESC
         </if>
         <if test="query.sort!='desc'">
          ORDER BY SCHED_NM
         </if>
        </if>
        <if test="query.sortSection=='scheduleDescribe'">
         <if test="query.sort=='desc'">
          ORDER BY SCHED_DESC DESC
         </if>
         <if test="query.sort!='desc'">
          ORDER BY SCHED_DESC
         </if>
        </if>
        <if test="query.sortSection=='executionCycle'">
         <if test="query.sort=='desc'">
          ORDER BY EXEC_CYCLE DESC
         </if>
         <if test="query.sort!='desc'">
          ORDER BY EXEC_CYCLE
         </if>         
        </if>
        <if test="query.sortSection=='createDateTime'">
         <if test="query.sort=='desc'">
          ORDER BY CREATE_DATETIME DESC
         </if>
         <if test="query.sort!='desc'">
          ORDER BY CREATE_DATETIME
         </if> 
        </if>
        <if test="query.sortSection=='useYn'">
         <if test="query.sort=='desc'">
          ORDER BY USE_YN DESC
         </if>
         <if test="query.sort!='desc'">
          ORDER BY USE_YN
         </if>
        </if>
      </if>
      LIMIT #{pageInfo.startRowNum}, #{pageInfo.size}
    </if>
    </script>""")
    public List<Schedule> scheduleList(SearchRequest searchRequest);

    @Select("""<script>
    SELECT COUNT(*)
    FROM MC_SCHED
    <if test="query!=null">   
      <where>
         <if test="query.yesNoCode!=null and query.yesNoCode!=''">
          USE_YN=#{query.yesNoCode}
         </if>
         <if test="query.startTime!=null and query.startTime!=''">
          <if test="query.dateSectionCode=='create'">
           AND CREATE_DATETIME <![CDATA[>=]]> #{query.startTime}
          </if>
          <if test="query.dateSectionCode=='modify'">
           AND MODIFY_DATETIME <![CDATA[>=]]> #{query.startTime}
          </if>
         </if>
        <if test="query.endTime!=null and query.endTime!=''">
         <if test="query.dateSectionCode=='create'">
          AND CREATE_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endTime},INTERVAL 1 DAY)
         </if>
         <if test="query.dateSectionCode=='modify'">
          AND MODIFY_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endTime},INTERVAL 1 DAY)
         </if>
        </if>
       <if test="query.scheduleName!=null and query.scheduleName!=''">
         AND SCHED_NM LIKE CONCAT('%',#{query.scheduleName},'%')
       </if>
         AND DELETE_YN='N'
      </where>
    </if>
    </script>""")
    public int scheduleListTotalConunt(SearchRequest searchRequest);
    
    @Insert("""<script>
    INSERT INTO MC_SCHED(
    SCHED_NM
    ,INPUT_SECTION_CD
    ,SCHED_CLASS_NM
    ,EXEC_CYCLE
    ,SCHED_DESC
    ,USE_YN
    ,DELETE_YN
    ,CREATE_DATETIME
    ,CREATE_ID
    ,MODIFY_DATETIME
    ,MODIFY_ID)
    VALUES(
    #{scheduleName}
    ,#{inputSectionCode}
    ,#{className}
    ,#{executionCycle}
    ,#{scheduleDescribe}
    ,#{useYn}
    ,'N'
    ,NOW()
    ,#{createId}
    ,NOW()
    ,#{createId})
    </script>""")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="scheduleNumber", before=false, resultType=Long.class)
    public int createSchedule(CreateSchedule createSchedule);

	@Results(id="scheduleInfoResult", value=[
		@Result(property="scheduleNumber", column="SCHED_NUM"),
		@Result(property="scheduleName", column="SCHED_NM"),
		@Result(property="inputSectionCode", column="INPUT_SECTION_CD"),
		@Result(property="className", column="SCHED_CLASS_NM"),
		@Result(property="executionCycle", column="EXEC_CYCLE"),
		@Result(property="scheduleDescribe", column="SCHED_DESC"),
		@Result(property="useYn", column="USE_YN"),
		@Result(property="createDateTime", column="CREATE_DATETIME"),
		@Result(property="createId", column="CREATE_ID"),
		@Result(property="modifyDateTime", column="MODIFY_DATETIME"),
		@Result(property="modifyId", column="MODIFY_ID")
	])
    @Select("""<script>
	SELECT
		SCHED_NUM,
		SCHED_NM,
		INPUT_SECTION_CD,
		SCHED_CLASS_NM,
		EXEC_CYCLE,
        SCHED_DESC,
		USE_YN,
		CREATE_DATETIME,
		CREATE_ID,
		MODIFY_DATETIME,
		MODIFY_ID
	FROM MC_SCHED
	WHERE SCHED_NUM=#{scheduleNumber}
    </script>""")
    public Schedule getScheduleInfo(Long scheduleNumber);

    @Update("""<script>
    UPDATE MC_SCHED SET
    SCHED_NM=#{scheduleName},
    INPUT_SECTION_CD=#{inputSectionCode},
    SCHED_CLASS_NM=#{className},
    EXEC_CYCLE=#{executionCycle},
    SCHED_DESC=#{scheduleDescribe},
    USE_YN=#{useYn},
    MODIFY_ID=#{modifyId},
    MODIFY_DATETIME=NOW()
    WHERE SCHED_NUM=#{scheduleNumber}
    </script>""")
    public void modfifySchedule(ModifySchedule modifySchedule)

    @Update("""<script>
    UPDATE MC_SCHED SET
    DELETE_YN='Y',
    MODIFY_ID=#{modifyId},
    MODIFY_DATETIME=NOW()
    WHERE SCHED_NUM=#{scheduleNumber}
    </script>""")
    public void deleteSchedule(DeleteSchedule deleteSchedule);
    
    @Select("""<script>
    SELECT SCHED_NM FROM MC_SCHED WHERE SCHED_NM=#{scheduleName}
    </script>""")
    public String scheduleNameCheck(String scheduleName);

    @Insert("""<script>
    INSERT INTO MC_SCHED_LOG(
    SCHED_NUM, LOG, SUCCESS_YN, CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME, MODIFY_ID)
    VALUES(
    #{scheduleNumber}
    ,#{log}
    ,#{successYn}
    ,NOW()
    ,#{createId}
    ,NOW()
    ,#{createId})
    </script>""")
    public int insertSchedulerHst(ScheduleLog scheduleLog);
    
    @Results(id="scheduleLogResult", value=[
        @Result(property="scheduleLogNumber", column="SCHED_LOG_NUM"),
        @Result(property="scheduleName", column="SCHED_NM"),
        @Result(property="log", column="LOG"),
        @Result(property="successYn", column="SUCCESS_YN"),
        @Result(property="modifyDateTime", column="MODIFY_DATETIME"),
    ])
    @Select("""<script>
     SELECT 
       SCHED.SCHED_NM , SCHEDLOG.SCHED_LOG_NUM ,
       SCHEDLOG.SUCCESS_YN , SCHEDLOG.MODIFY_DATETIME
     FROM MC_SCHED SCHED
     JOIN MC_SCHED_LOG SCHEDLOG ON SCHED.SCHED_NUM = SCHEDLOG.SCHED_NUM
     <where>     
      <if test="query!=null">
         <if test="query.successCode!=null and query.successCode!=''">
          SCHEDLOG.SUCCESS_YN = #{query.successCode}
         </if>
         <if test="query.startTime!=null and query.startTime!=''">
          AND SCHEDLOG.MODIFY_DATETIME <![CDATA[>=]]> #{query.startTime}
         </if>
        <if test="query.endTime!=null and query.endTime!=''">
          AND SCHEDLOG.MODIFY_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endTime},INTERVAL 1 DAY)
        </if>
       <if test="query.scheduleName!=null and query.scheduleName!=''">
          AND SCHED.SCHED_NM LIKE CONCAT('%',#{query.scheduleName},'%')
       </if>
      </if>
     </where>
     <if test="pageInfo!=null">
      <if test="query.sortSection!=null and query.sortSection!=''">
        
        <if test="query.sortSection=='scheduleName'">
         <if test="query.sort=='desc'">
          ORDER BY SCHED.SCHED_NM DESC
         </if>
         <if test="query.sort!='desc'">
          ORDER BY SCHED.SCHED_NM
         </if>
        </if>
        
        <if test="query.sortSection=='modifyDateTime'">
         <if test="query.sort=='desc'">
          ORDER BY SCHEDLOG.MODIFY_DATETIME DESC
         </if>
         <if test="query.sort!='desc'">
          ORDER BY SCHEDLOG.MODIFY_DATETIME
         </if> 
        </if>

        <if test="query.sortSection=='default'">
         <if test="query.sort=='DESC'">
          ORDER BY SCHEDLOG.SCHED_LOG_NUM DESC
         </if>
         <if test="query.sort!='DESC'">
          ORDER BY SCHEDLOG.SCHED_LOG_NUM
         </if> 
        </if>

      </if>
      <if test="query.sortSection==null or query.sortSection==''">
	      ORDER BY SCHEDLOG.SCHED_LOG_NUM DESC
      </if>

      LIMIT #{pageInfo.startRowNum}, #{pageInfo.size}
     </if>
    </script>""")
    public List<ScheduleLog> scheduleLogList(SearchRequest searchRequest);
    
    @Select("""<script>
     SELECT 
       COUNT(*)
     FROM MC_SCHED SCHED
     JOIN MC_SCHED_LOG SCHEDLOG ON SCHED.SCHED_NUM = SCHEDLOG.SCHED_NUM
     <where>     
      <if test="query!=null">
         <if test="query.successCode!=null and query.successCode!=''">
          SCHEDLOG.SUCCESS_YN = #{query.successCode}
         </if>
         <if test="query.startTime!=null and query.startTime!=''">
          AND SCHEDLOG.MODIFY_DATETIME <![CDATA[>=]]> #{query.startTime}
         </if>
        <if test="query.endTime!=null and query.endTime!=''">
          AND SCHEDLOG.MODIFY_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endTime},INTERVAL 1 DAY)
        </if>
       <if test="query.scheduleName!=null and query.scheduleName!=''">
          AND SCHED.SCHED_NM LIKE CONCAT('%',#{query.scheduleName},'%')
       </if>
      </if>
     </where>
     <if test="pageInfo!=null">
      <if test="query.sortSection!=null and query.sortSection!=''">
        
        <if test="query.sortSection=='scheduleName'">
         <if test="query.sort=='DESC'">
          ORDER BY SCHED.SCHED_NM DESC
         </if>
         <if test="query.sort!='DESC'">
          ORDER BY SCHED.SCHED_NM
         </if>
        </if>
        
        <if test="query.sortSection=='modifyDateTime'">
         <if test="query.sort=='DESC'">
          ORDER BY SCHEDLOG.MODIFY_DATETIME DESC
         </if>
         <if test="query.sort!='DESC'">
          ORDER BY SCHEDLOG.MODIFY_DATETIME
         </if> 
        </if>

        <if test="query.sortSection=='default'">
         <if test="query.sort=='DESC'">
          ORDER BY SCHEDLOG.SCHED_LOG_NUM DESC
         </if>
         <if test="query.sort!='DESC'">
          ORDER BY SCHEDLOG.SCHED_LOG_NUM
         </if> 
        </if>

      </if>
     </if>
    </script>""")
    public int scheduleLogListTotalCount(SearchRequest searchRequest);
    
    @ResultMap("scheduleLogResult")
    @Select("""<script>
     SELECT
       SCHED.SCHED_NM , SCHEDLOG.SUCCESS_YN , SCHEDLOG.LOG , SCHEDLOG.MODIFY_DATETIME 
     FROM MC_SCHED_LOG SCHEDLOG
     JOIN MC_SCHED SCHED ON SCHEDLOG.SCHED_NUM = SCHED.SCHED_NUM
     WHERE SCHEDLOG.SCHED_LOG_NUM = #{scheduleLogNumber}
    </script>""")
    public ScheduleLog scheduleLogDetail(Long scheduleLogNumber);
}