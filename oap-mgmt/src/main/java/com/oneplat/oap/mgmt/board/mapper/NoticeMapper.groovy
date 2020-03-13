package com.oneplat.oap.mgmt.board.mapper

import com.oneplat.oap.core.model.SearchRequest
import com.oneplat.oap.mgmt.board.model.Board
import org.apache.ibatis.annotations.*

/**
 * Created by LSH on 2016. 12. 19..
 */
@Mapper
interface NoticeMapper {

    @Results(id="noticeResult", value=[
            @Result(property = "noticeNumber", column = "NOTICE_NUM"),
            @Result(property = "emailSendYn", column = "EMAIL_SEND-YN"),
            @Result(property = "siteCode", column = "SITE_CD"),
            @Result(property = "title", column = "TITLE"),
            @Result(property = "contents", column = "CONTENTS"),
            @Result(property = "exposureYn", column = "EXPOSURE_YN"),
            @Result(property = "exposureBeginDateTime", column = "EXPOSURE_BEGIN_DATETIME"),
            @Result(property = "exposureEndDateTime", column = "EXPOSURE_END_DATETIME"),
            @Result(property = "inquiryCount", column = "INQUIRY_CNT"),
            @Result(property = "topFixYn", column = "TOP_FIX_YN"),
    ])
    @Select("""
    <script>
        SELECT
            NOTICE_NUM, SITE_CD, TITLE, CONTENTS,
            EXPOSURE_YN, EXPOSURE_BEGIN_DATETIME,
            EXPOSURE_END_DATETIME, INQUIRY_CNT,
            TOP_FIX_YN, EMAIL_SEND_YN, DELETE_YN,
            CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME,
            MODIFY_ID
        FROM
            DC_NOTICE
        WHERE
            DELETE_YN='N'
            <if test="query!=null">
                <if test="query.searchWord!=null and query.searchWord!=''">
                <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode=='TITLE'">
                    AND TITLE LIKE CONCAT('%',#{query.searchWord},'%')
                </if>
                <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode=='CONTENTS'">
                    AND CONTENTS LIKE CONCAT('%',#{query.searchWord},'%')
                </if>
                </if>
                <if test="query.searchExposure!=null and query.searchExposure != ''">
                    AND EXPOSURE_YN =  #{query.searchExposure}
                </if>
				<if test="query.searchFixation!=null and query.searchFixation != ''">
                    AND TOP_FIX_YN =  #{query.searchFixation}
                </if>
                <if test="query.searchSiteCode!=null and query.searchSiteCode != ''">
                    AND SITE_CD =  #{query.searchSiteCode}
                </if>
                <if test="query.startDate!=null and query.startDate!=''">
                    AND CREATE_DATETIME <![CDATA[>=]]> STR_TO_DATE(CONCAT(#{query.startDate}, '000000'), "%Y-%m-%d%H%i%S")
                </if>
                <if test="query.endDate!=null and query.endDate!=''">
                    AND CREATE_DATETIME <![CDATA[<=]]> STR_TO_DATE(CONCAT(#{query.endDate}, '235959'), "%Y-%m-%d%H%i%S")
                </if>

            </if>
        ORDER BY CREATE_DATETIME DESC
    </script>
    """)
    List<Board> selectNoticeList(SearchRequest searchRequest);

    @Select("""
    <script>
        SELECT COUNT(*) FROM DC_NOTICE
        WHERE DELETE_YN='N'
        <if test="query!=null">
            <if test="query.searchWord!=null and query.searchWord!=''">
            <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode=='TITLE'">
                AND TITLE LIKE CONCAT('%',#{query.searchWord},'%')
            </if>
            <if test="query.searchWordTypeCode!=null and query.searchWordTypeCode=='CONTENTS'">
                AND CONTENTS LIKE CONCAT('%',#{query.searchWord},'%')
            </if>
            </if>
            <if test="query.searchExposure!=null and query.searchExposure != ''">
                AND EXPOSURE_YN =  #{query.searchExposure}
            </if>
            <if test="query.searchSiteCode!=null and query.searchSiteCode != ''">
                AND SITE_CD =  #{query.searchSiteCode}
            </if>
            <if test="query.startDate!=null and query.startDate!=''">
                AND CREATE_DATETIME <![CDATA[>=]]> STR_TO_DATE(CONCAT(#{query.startDate}, '000000'), "%Y-%m-%d%H%i%S")
            </if>
            <if test="query.endDate!=null and query.endDate!=''">
                AND CREATE_DATETIME <![CDATA[<=]]> STR_TO_DATE(CONCAT(#{query.endDate}, '235959'), "%Y-%m-%d%H%i%S")
            </if>
         </if>
    </script>
    """)
    Integer selectNoticeListTotal(SearchRequest searchRequest);
    @ResultMap("noticeResult")
    @Select("""
    <script>
        SELECT
            NOTICE_NUM, SITE_CD, TITLE, CONTENTS,
            EXPOSURE_YN, EXPOSURE_BEGIN_DATETIME,
            EXPOSURE_END_DATETIME, INQUIRY_CNT,
            TOP_FIX_YN, EMAIL_SEND_YN, DELETE_YN,
            CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME,
            MODIFY_ID
        FROM DC_NOTICE
        WHERE NOTICE_NUM = #{noticeNumber}
    </script>
    """)
    Board selectNoticeDetail(Long noticeNumber);

    @Insert("""
    <script>
        INSERT INTO DC_NOTICE(
            NOTICE_NUM, SITE_CD, TITLE, CONTENTS,
            EXPOSURE_YN, EXPOSURE_BEGIN_DATETIME,
            EXPOSURE_END_DATETIME, INQUIRY_CNT,
            TOP_FIX_YN, EMAIL_SEND_YN, DELETE_YN,
            CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME,
            MODIFY_ID
        ) VALUES (
            #{noticeNumber},
            #{siteCode},
            #{title},
            #{contents},
            #{exposureYn},
            #{exposureBeginDateTime},
            #{exposureEndDateTime},
            #{inquiryCount},
            #{topFixYn},
            #{emailSendYn},
            'N', -- noticeDeleteYn
            NOW(),
            #{createId},
            NOW(),
            #{modifyId}
        )
    </script>
    """)
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="noticeNumber", before=false, resultType=Long.class)
    int insertNotice(Board notice);

    @Update("""
    <script>
        UPDATE DC_NOTICE
        SET
            TITLE = #{title},
            CONTENTS = #{contents},
            SITE_CD = #{siteCode},
            EXPOSURE_YN = #{exposureYn},
            EXPOSURE_BEGIN_DATETIME = #{exposureBeginDateTime},
            EXPOSURE_END_DATETIME = #{exposureEndDateTime},
            TOP_FIX_YN = #{topFixYn},
            EMAIL_SEND_YN = #{emailSendYn},
            MODIFY_DATETIME = NOW(),
            MODIFY_ID = #{modifyId}
        WHERE
            NOTICE_NUM = #{noticeNumber};
    </script>
    """)
    int updateNotice(Board notice);

    @Delete("""
    <script>
        UPDATE
            DC_NOTICE
        SET
          DELETE_YN = 'Y'
          ,MODIFY_DATETIME = NOW()
          ,MODIFY_ID = #{modifyId}
        WHERE NOTICE_NUM = #{noticeNumber}
    </script>
    """)
    int deleteNotice(Board notice);

    @Insert("""
    <script>
        INSERT INTO DC_NOTICE_HIST (
            NOTICE_NUM, HIST_END_DATETIME, HIST_BEGIN_DATETIME,
            SITE_CD, TITLE, CONTENTS,
            EXPOSURE_YN, EXPOSURE_BEGIN_DATETIME, EXPOSURE_END_DATETIME,
            INQUIRY_CNT, TOP_FIX_YN, EMAIL_SEND_YN,
            DELETE_YN, CREATE_DATETIME, CREATE_ID,
            MODIFY_DATETIME, MODIFY_ID)
        SELECT
            NOTICE_NUM, STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f'), NOW(),
            SITE_CD, TITLE, CONTENTS,
            EXPOSURE_YN, EXPOSURE_BEGIN_DATETIME, EXPOSURE_END_DATETIME,
            INQUIRY_CNT, TOP_FIX_YN, EMAIL_SEND_YN,
            DELETE_YN, CREATE_DATETIME, CREATE_ID,
            MODIFY_DATETIME, MODIFY_ID
        FROM DC_NOTICE WHERE NOTICE_NUM = #{noticeNumber}
    </script>
    """)
    void insertNoticeHistory(Board notice);

    @Update("""
    <script>
        UPDATE DC_NOTICE_HIST
        SET HIST_END_DATETIME = NOW()
            , MODIFY_DATETIME = NOW()
            , MODIFY_ID = #{modifyId}
        WHERE NOTICE_NUM = #{noticeNumber}
          AND HIST_END_DATETIME = STR_TO_DATE('9999-12-31 23:59:59.999999', '%Y-%m-%d %H:%i:%s.%f')
    </script>
    """)
    void updateNoticeHistory(Board notice)
}