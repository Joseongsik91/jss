package com.oneplat.oap.mgmt.setting.system.mapper

import java.util.List;
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectKey
import org.apache.ibatis.annotations.Update
import com.oneplat.oap.core.model.SearchRequest
import com.oneplat.oap.mgmt.setting.system.model.Adaptor
import com.oneplat.oap.mgmt.setting.system.model.Schedule

@Mapper
interface AdaptorMapper {

	@Results(id="adaptorList", value=[
		@Result(property="adaptorNumber", column="ADAPTOR_NUM"),
		@Result(property="adaptorBeanId", column="ADAPTOR_BEAN_ID"),
		@Result(property="adaptorProtocolCode", column="ADAPTOR_PROTOCOL_CD"),
		@Result(property="createDateTime", column="CREATE_DATETIME"),
		@Result(property="modifyDateTime", column="MODIFY_DATETIME"),
		@Result(property="adaptorUseYn", column="ADAPTOR_USE_YN")
	])
	@Select("""<script>
     SELECT ADAPTOR.* FROM
     	MC_ADAPTOR ADAPTOR
     	LEFT OUTER JOIN MC_OPR OPR ON ADAPTOR.CREATE_ID = OPR.LOGIN_ID

        <where>
         ADAPTOR.ADAPTOR_DELETE_YN='N'

         <if test="query!=null">
         <if test="query.adaptorUseYnCode!=null and query.adaptorUseYnCode!=''">
          AND ADAPTOR.ADAPTOR_USE_YN = #{query.adaptorUseYnCode}
         </if>
         <if test="query.startTime!=null and query.startTime!=''">
          <if test="query.dateSectionCode=='create'">
           AND ADAPTOR.CREATE_DATETIME <![CDATA[>=]]> #{query.startTime}
          </if>
          <if test="query.dateSectionCode=='modify'">
           AND ADAPTOR.MODIFY_DATETIME <![CDATA[>=]]> #{query.startTime}
          </if>
         </if>
        <if test="query.endTime!=null and query.endTime!=''">
         <if test="query.dateSectionCode=='create'">
          AND ADAPTOR.CREATE_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endTime},INTERVAL 1 DAY)
         </if>
         <if test="query.dateSectionCode=='modify'">
          AND ADAPTOR.MODIFY_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endTime},INTERVAL 1 DAY)
         </if>
        </if>


       <if test="query.searchWord!=null and query.searchWord!=''">
		 <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode!=''">
			<if test="query.searchWordTypeCode=='adaptorName'">
				AND ADAPTOR.ADAPTOR_BEAN_ID LIKE CONCAT('%',#{query.searchWord},'%')
			</if>
			<if test="query.searchWordTypeCode=='createName'">
				AND OPR.OPR_NM LIKE CONCAT('%',#{query.searchWord},'%')
			</if>
		 </if>
       </if>

	   <if test="query.sortSection!=null and query.sortSection!=''">
         ORDER BY
		 	<if test="query.sortSection=='adaptorBeanId'">
				ADAPTOR_BEAN_ID
			</if>
			<if test="query.sortSection=='adaptorProtocolCode'">
				ADAPTOR_PROTOCOL_CD
			</if>
			<if test="query.sortSection=='createDateTime'">
				CREATE_DATETIME
			</if>
			<if test="query.sortSection=='modifyDateTime'">
				MODIFY_DATETIME
			</if>
		 		<if test="query.sort!=null and query.sort!=''">
					\${query.sort}
				</if>
				<if test="query.sort==null or query.sort==''">
					DESC
				</if>
	   </if>

	   <if test="query.sortSection==null or query.sortSection==''">
         ORDER BY ADAPTOR.ADAPTOR_NUM DESC
	   </if>



       </if>
       </where>
      

    <if test="pageInfo!=null">
      LIMIT #{pageInfo.startRowNum}, #{pageInfo.size}
    </if>
    </script>""")
	public List<Adaptor> adaptorList(SearchRequest searchRequest);
	
	@Select("""<script>
     SELECT COUNT(*) FROM
     	MC_ADAPTOR ADAPTOR
     	LEFT OUTER JOIN MC_OPR OPR ON ADAPTOR.CREATE_ID = OPR.LOGIN_ID

        <where>
         ADAPTOR.ADAPTOR_DELETE_YN='N'

         <if test="query!=null">
         <if test="query.adaptorUseYnCode!=null and query.adaptorUseYnCode!=''">
          AND ADAPTOR.ADAPTOR_USE_YN = #{query.adaptorUseYnCode}
         </if>
         <if test="query.startTime!=null and query.startTime!=''">
          <if test="query.dateSectionCode=='create'">
           AND ADAPTOR.CREATE_DATETIME <![CDATA[>=]]> #{query.startTime}
          </if>
          <if test="query.dateSectionCode=='modify'">
           AND ADAPTOR.MODIFY_DATETIME <![CDATA[>=]]> #{query.startTime}
          </if>
         </if>
        <if test="query.endTime!=null and query.endTime!=''">
         <if test="query.dateSectionCode=='create'">
          AND ADAPTOR.CREATE_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endTime},INTERVAL 1 DAY)
         </if>
         <if test="query.dateSectionCode=='modify'">
          AND ADAPTOR.MODIFY_DATETIME <![CDATA[<]]> DATE_ADD(#{query.endTime},INTERVAL 1 DAY)
         </if>
        </if>


       <if test="query.searchWord!=null and query.searchWord!=''">
		 <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode!=''">
			<if test="query.searchWordTypeCode=='adaptorName'">
				AND ADAPTOR.ADAPTOR_BEAN_ID LIKE CONCAT('%',#{query.searchWord},'%')
			</if>
			<if test="query.searchWordTypeCode=='createName'">
				AND OPR.OPR_NM LIKE CONCAT('%',#{query.searchWord},'%')
			</if>
		 </if>
       </if>
       </if>
       </where>
    </script>""")
	public int adaptorListTotalCount(SearchRequest searchRequest);
	
	@Results(id="getAdaptorInfo", value=[
		@Result(property="adaptorNumber", column="ADAPTOR_NUM"),
		@Result(property="adaptorBeanId", column="ADAPTOR_BEAN_ID"),
		@Result(property="adaptorDescribe", column="ADAPTOR_DESC"),
		@Result(property="saaUri", column="SAA_URI"),
		@Result(property="adaptorProtocolCode", column="ADAPTOR_PROTOCOL_CD"),
		@Result(property="adaptorUseYn", column="ADAPTOR_USE_YN"),
		@Result(property="createId", column="CREATE_ID"),
		@Result(property="createDateTime", column="CREATE_DATETIME"),
		@Result(property="modifyId", column="MODIFY_ID"),
		@Result(property="modifyDateTime", column="MODIFY_DATETIME")
	])
	@Select("""<script>
	SELECT
		ADAPTOR_NUM,
		ADAPTOR_BEAN_ID,
		ADAPTOR_DESC,
		SAA_URI,
		ADAPTOR_PROTOCOL_CD,
		ADAPTOR_USE_YN,
		(SELECT CONCAT(OPR.OPR_NM,'(',OPR.LOGIN_ID,')') FROM MC_OPR OPR WHERE OPR.LOGIN_ID = MC_ADAPTOR.CREATE_ID) AS CREATE_ID,
		CREATE_DATETIME,
		(SELECT CONCAT(OPR.OPR_NM,'(',OPR.LOGIN_ID,')') FROM MC_OPR OPR WHERE OPR.LOGIN_ID = MC_ADAPTOR.MODIFY_ID) AS MODIFY_ID,
		MODIFY_DATETIME
    FROM
		MC_ADAPTOR
	WHERE
		ADAPTOR_NUM = #{adaptorNumber}
    </script>""")
	public Adaptor getAdaptorInfo(Long adaptorNumber);
	
	@Select("""<script>
	SELECT
		ADAPTOR_NUM
    FROM
		MC_ADAPTOR
	WHERE
		ADAPTOR_BEAN_ID = #{adaptorBeanId}
    </script>""")
	public Object beanIdCheck(String adaptorBeanId);
	
	@Insert("""<script>
    INSERT INTO MC_ADAPTOR(
    	ADAPTOR_BEAN_ID,
		ADAPTOR_DESC,
		SAA_URI,
		ADAPTOR_PROTOCOL_CD,
		ADAPTOR_USE_YN,
		ADAPTOR_DELETE_YN,
		CREATE_DATETIME,
		CREATE_ID,
		MODIFY_DATETIME,
    	MODIFY_ID)
    VALUES(
    	#{adaptorBeanId},
		#{adaptorDescribe},
		#{saaUri},
		#{adaptorProtocolCode},
		#{adaptorUseYn},
		'N',
    	NOW(6),
    	#{createId},
    	NOW(6),
    	#{createId})
    </script>""")
	@SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="adaptorNumber", before=false, resultType=Long.class)
	public void createAdaptor(Adaptor adaptor);
	
	@Update("""<script>
    UPDATE MC_ADAPTOR SET
    	ADAPTOR_PROTOCOL_CD = #{adaptorProtocolCode},
		ADAPTOR_DESC = #{adaptorDescribe},
		ADAPTOR_USE_YN = #{adaptorUseYn},
		MODIFY_ID = #{modifyId},
    	MODIFY_DATETIME = NOW(6)
    WHERE ADAPTOR_NUM = #{adaptorNumber}
    </script>""")
	public void modifyAdaptor(Adaptor adaptor);
	
	@Update("""<script>
    UPDATE MC_ADAPTOR SET
		ADAPTOR_DELETE_YN = 'Y',
		MODIFY_ID = #{modifyId},
    	MODIFY_DATETIME = NOW(6)
    WHERE ADAPTOR_NUM = #{adaptorNumber}
    </script>""")
	public void deleteAdaptor(Adaptor adaptor);
	
	@Insert("""<script>
        INSERT INTO 
			MC_ADAPTOR_HIST(
				ADAPTOR_NUM, 
				HIST_END_DATETIME, 
				HIST_BEGIN_DATETIME,
				ADAPTOR_BEAN_ID,
				ADAPTOR_DESC,
				SAA_URI,
				ADAPTOR_PROTOCOL_CD,
				ADAPTOR_USE_YN,
				ADAPTOR_DELETE_YN,
          		CREATE_DATETIME,
				CREATE_ID, 
				MODIFY_DATETIME,
          		MODIFY_ID)
        SELECT 
			ADAPTOR_NUM, 
			STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f'), 
			NOW(6),
          	ADAPTOR_BEAN_ID,
			ADAPTOR_DESC,
			SAA_URI,
			ADAPTOR_PROTOCOL_CD,
			ADAPTOR_USE_YN,
			ADAPTOR_DELETE_YN,
        	CREATE_DATETIME,
			CREATE_ID, 
			MODIFY_DATETIME,
        	MODIFY_ID
        FROM MC_ADAPTOR
        WHERE ADAPTOR_NUM = #{adaptorNumber}
    </script>""")
	public void createAdaptorHist(Long adaptorNumber);
	
	@Update("""<script>
        UPDATE MC_ADAPTOR_HIST
        SET HIST_END_DATETIME = NOW(6)
            , MODIFY_DATETIME = NOW(6)
            , MODIFY_ID = #{modifyId}
        WHERE ADAPTOR_NUM = #{adaptorNumber}
            AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>""")
	public void modifyAdaptorHist(Adaptor adaptor);
}
