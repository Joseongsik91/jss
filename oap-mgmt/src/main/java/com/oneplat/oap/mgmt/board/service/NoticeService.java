package com.oneplat.oap.mgmt.board.service;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.board.model.Board;

import java.util.List;

/**
 * Created by LSH on 2016. 12. 19..
 */
public interface NoticeService {
    public List<Board> getNoticeList(SearchRequest searchRequest);
}
