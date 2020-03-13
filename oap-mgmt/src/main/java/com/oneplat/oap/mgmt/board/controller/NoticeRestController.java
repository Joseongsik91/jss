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
}
