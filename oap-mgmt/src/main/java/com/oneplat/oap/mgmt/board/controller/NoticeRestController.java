package com.oneplat.oap.mgmt.board.controller;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.board.model.Board;
import com.oneplat.oap.mgmt.board.model.ResponseNoticeList;
import com.oneplat.oap.mgmt.board.service.NoticeService;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by LSH on 2016. 12. 19..
 */

@Api(tags="/notice",  description="공지사항 관리", produces = "application/json")
@RestController
@RequestMapping(value = "/notice")
public class NoticeRestController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    AuthenticationInjector authenticationInjector;
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeRestController.class);

    /**
     * Name : NoticeBoardList
     * */
    @ApiOperation(value = "공지사항 리스트 조회", notes = "공지사항 리스트 조회", response = ResponseNoticeList.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "리스트 query",
                    defaultValue = "{\"searchWordTypeCode\":\"\",\"searchWord\":\"\",\"startDateTime\":\"\",\"endDateTime\":\"\"}"),
            @ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "현재 페이지", defaultValue = "1"),
            @ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "페이지 사이즈", defaultValue = "20")
    })
    @RequestMapping(value = "/notices", method = RequestMethod.GET)
    public Object getNoticeList(@ApiIgnore SearchRequest searchRequest){
        LOGGER.debug("SearchRequet : {}", searchRequest);
        return new ResponseNoticeList(noticeService.getNoticeList(searchRequest), searchRequest.getPageInfo());
    }

    /**
     * Name : NoticeBoardDetail
     * */
    @ApiOperation(value = "공지사항 상세 조회", notes = "공지사항 상세 조회", response = Board.class)
    @RequestMapping(value = "/{noticeNumber}", method = RequestMethod.GET)
    public Object getNoticeDetail(@ApiParam(value="noticeNumber", defaultValue="123")
                                              @PathVariable Long noticeNumber){

        return noticeService.getNoticeDetail(noticeNumber);
    }

    /**
     * Name : NoticeBoardCreate
     * */
    @ApiOperation(value = "공지사항 생성", notes = "공지사항 생성", response = Board.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object createNotice(
            @ApiParam(value="공지사항 데이타") @RequestBody Board notice
            ){
        authenticationInjector.setAuthentication(notice);
        Board selectNotice = noticeService.createNotice(notice);
        return selectNotice;
    }

    /**
     * Name : NoticeBoardModify
     * */
    @ApiOperation(value = "공지사항 수정", notes = "공지사항 수정", response = Board.class)
    @RequestMapping(value = "/{noticeNumber}", method = RequestMethod.PUT)
    public Object modifyNotice(
            @ApiParam(value="공지사항 데이타") @RequestBody Board notice,
            @ApiParam(value="공지사항 번호") @PathVariable Long noticeNumber
    ){
        authenticationInjector.setAuthentication(notice);
        notice.setNoticeNumber(noticeNumber);
        noticeService.modifyNotice(notice);
        return notice;
    }

    /**
     * Name : NoticeBoardDelete
     * */
    @ApiOperation(value = "공지사항 삭제", notes = "공지사항 삭제", response = Board.class)
    @RequestMapping(value = "/{noticeNumber}", method = RequestMethod.DELETE)
    public Object deleteNotice(
            @ApiParam(value="공지사항 데이타") @RequestBody Board notice,
            @ApiParam(value="공지사항 번호") @PathVariable(value = "noticeNumber") Long noticeNumber
    ){
        authenticationInjector.setAuthentication(notice);
        notice.setNoticeNumber(noticeNumber);
        noticeService.removeNotice(notice);
        return notice;
    }


}
