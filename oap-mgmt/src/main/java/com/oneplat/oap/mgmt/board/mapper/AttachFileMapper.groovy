package com.oneplat.oap.mgmt.board.mapper;

import com.oneplat.oap.mgmt.board.model.AttachFile
//import org.apache.ibatis.annotations.Delete
//import org.apache.ibatis.annotations.Insert
//import org.apache.ibatis.annotations.Mapper
//import org.apache.ibatis.annotations.Result
//import org.apache.ibatis.annotations.Results
//import org.apache.ibatis.annotations.Select
//import org.apache.ibatis.annotations.SelectKey
//import org.apache.ibatis.annotations.Update

/**
 * Created by LSH on 2016. 12. 19..
 */
@Mapper
interface AttachFileMapper {

    @Results(id="attachFileResult", value=[
            @Result(property = "attachFileNumber", column = "ATTACH_FILE_NUM"),
            @Result(property = "noticeNumber", column = "NOTICE_NUM"),
            @Result(property = "fileUrl", column = "ATTACH_FILE_URL"),
            @Result(property = "fileSize", column = "ATTACH_FILE_SIZE"),
            @Result(property = "fileName", column = "ATTACH_FILE_NM")
    ])
    @Select("""
    <script>
        SELECT
            ATTACH_FILE_NUM, NOTICE_NUM, ATTACH_FILE_URL,
            ATTACH_FILE_SIZE, DELETE_YN, ATTACH_FILE_NM,
            CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME,
            MODIFY_ID
        FROM
            DC_NOTICE_ATTACH_FILE
        WHERE
            NOTICE_NUM = #{noticeNumber}
            AND DELETE_YN = 'N';
    </script>
    """)
    List<AttachFile> selectAttachFileList(AttachFile attachFile);

    @Insert("""
    <script>
        INSERT INTO DC_NOTICE_ATTACH_FILE(
            ATTACH_FILE_NUM, NOTICE_NUM, ATTACH_FILE_URL,
            ATTACH_FILE_SIZE, DELETE_YN, ATTACH_FILE_NM,
            CREATE_DATETIME, CREATE_ID, MODIFY_DATETIME,
            MODIFY_ID
        )
        VALUES(
            #{attachFileNumber}
            ,#{noticeNumber}
            ,#{fileUrl}
            ,#{fileSize}
            ,'N'
            ,#{fileName}
            ,NOW()
            ,(SELECT CREATE_ID FROM DC_NOTICE WHERE NOTICE_NUM = #{noticeNumber})
            ,NOW()
            ,#{modifyId}
        )
    </script>
    """)
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="attachFileNumber", before=false, resultType=Long.class)
    void insertAttachFile(AttachFile fileInfo);

    @Delete("""
        DELETE FROM DC_NOTICE_ATTACH_FILE WHERE NOTICE_NUM = #{noticeNumber}
    """)
    void deleteAttachFile(AttachFile tmpFileInfo);
}