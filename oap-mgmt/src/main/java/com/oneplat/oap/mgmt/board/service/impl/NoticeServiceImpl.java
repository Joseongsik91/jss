package com.oneplat.oap.mgmt.board.service.impl;

import com.oneplat.oap.core.exception.NotFoundException;
import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.board.controller.NoticeRestController;
import com.oneplat.oap.mgmt.board.mapper.NoticeMapper;
import com.oneplat.oap.mgmt.board.model.Board;
import com.oneplat.oap.mgmt.board.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by LSH on 2016. 12. 19..
 */
@Service
public class NoticeServiceImpl implements NoticeService{

    @Autowired
    private NoticeMapper noticeMapper;
    
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Override
    public List<Board> getNoticeList(SearchRequest searchRequest) {
        searchRequest.setData();
        List<Board> list = noticeMapper.selectNoticeList(searchRequest);
        if(list == null){
            throw new NotFoundException();
        }

        if(searchRequest.getPageInfo() != null){
            searchRequest.getPageInfo().setTotalCount(noticeMapper.selectNoticeListTotal(searchRequest));
            searchRequest.getPageInfo().setResultCount(list.size());
        }
        return list;
    }

}
