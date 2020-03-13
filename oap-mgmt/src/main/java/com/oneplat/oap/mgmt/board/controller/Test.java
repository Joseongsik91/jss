package com.oneplat.oap.mgmt.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oneplat.oap.mgmt.board.mapper.NoticeMapper;
 
@RestController
public class Test {
	@Autowired
    private NoticeMapper noticeMapper;
    
    @RequestMapping("/")
    public String root_test() throws Exception{
        return "hello";
    }
 
    @RequestMapping("/demo")
    public String demo_test() throws Exception{
        return "Hello demo(/demo)";
    }
 
}

