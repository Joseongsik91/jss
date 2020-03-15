package com.oneplat.oap.mgmt.board.service.impl;

import com.oneplat.oap.core.exception.NotFoundException;
import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.board.controller.NoticeRestController;
import com.oneplat.oap.mgmt.board.mapper.AttachFileMapper;
import com.oneplat.oap.mgmt.board.mapper.NoticeMapper;
import com.oneplat.oap.mgmt.board.model.AttachFile;
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
    @Autowired
    private AttachFileMapper attachFileMapper;

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

    @Override
    public Board getNoticeDetail(Long noticeNumber) {
        Board result = noticeMapper.selectNoticeDetail(noticeNumber);

        AttachFile attachFile = new AttachFile();
        attachFile.setNoticeNumber(noticeNumber);
        result.setAttachFileList(attachFileMapper.selectAttachFileList(attachFile));
        if(result == null){
            throw new NotFoundException();
        }
        return result;
    }

    @Override
    @Transactional
    public Board createNotice(Board notice) {
        int result =noticeMapper.insertNotice(notice);
        LOGGER.debug("=======================================notice:{}", notice);
        if(result == 0){
            throw new NotFoundException();
        }
        //file insert
        try{
            if(notice.getAttachFileList() != null && notice.getAttachFileList().size()>0){
                for(AttachFile fileInfo : notice.getAttachFileList()){
                    fileInfo.setModifyId(notice.getModifyId());
                    if( !"".equals(fileInfo.getFileName())){
                        fileInfo.setNoticeNumber(notice.getNoticeNumber());
                        attachFileMapper.insertAttachFile(fileInfo);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //등록 결과
        Board selectNotice = null;
        if(notice.getNoticeNumber() != 0){
            selectNotice = noticeMapper.selectNoticeDetail(notice.getNoticeNumber());
        }

        //히스토리추가
        noticeMapper.insertNoticeHistory(notice);

        return selectNotice;
    }

    @Override
    @Transactional
    public void modifyNotice(Board notice) {

        //modify
        int result =noticeMapper.updateNotice(notice);
        if(result == 0){
            throw new NotFoundException();
        }

        //fileDelete -> fileSave
        try{
            if(notice.getAttachFileList() != null && notice.getAttachFileList().size()>0){
                AttachFile tmpFileInfo = new AttachFile();
                tmpFileInfo.setNoticeNumber(notice.getNoticeNumber());
                attachFileMapper.deleteAttachFile(tmpFileInfo);
                for(AttachFile fileInfo : notice.getAttachFileList()){
                    fileInfo.setModifyId(notice.getModifyId());
                    if( !"".equals(fileInfo.getFileName())){
                        fileInfo.setNoticeNumber(notice.getNoticeNumber());
                        attachFileMapper.insertAttachFile(fileInfo);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //히스토리수정, 추가
        noticeMapper.updateNoticeHistory(notice);
        noticeMapper.insertNoticeHistory(notice);
    }

    @Override
    @Transactional
    public void removeNotice(Board notice) {

        //file Delete
        AttachFile tmpFileInfo = new AttachFile();
        tmpFileInfo.setNoticeNumber(notice.getNoticeNumber());
        attachFileMapper.deleteAttachFile(tmpFileInfo);

        int result = noticeMapper.deleteNotice(notice);
        if(result == 0){
            throw new NotFoundException();
        }

        //히스토리수정, 추가
        noticeMapper.updateNoticeHistory(notice);
        noticeMapper.insertNoticeHistory(notice);
    }

}
