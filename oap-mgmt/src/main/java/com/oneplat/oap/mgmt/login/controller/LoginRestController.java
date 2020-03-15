package com.oneplat.oap.mgmt.login.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oneplat.oap.mgmt.login.model.LoginUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags="/auth_check",  description="로그인")
@RestController
public class LoginRestController {
    
    @ApiOperation(value = "로그인 인증처리", notes = "로그인 인증처리", response = LoginUser.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "loginId", required = true, dataType = "String", paramType = "query", value = "로그인 아이디", defaultValue = "TEST"),
        @ApiImplicitParam(name = "password", required = true, dataType = "String", paramType = "query", value = "로그인 패스워드", defaultValue = "TEST"),
        @ApiImplicitParam(name = "insideYn", required = true, dataType = "YesNoType.class", paramType = "query", value = "내부망 접속 여부", defaultValue = "Y"),
        })
    @RequestMapping(value = "/auth_check", method = RequestMethod.POST)
    public Object loginPage(@ApiParam("loginId")String loginId, @ApiParam("password")String password) {
//        String url ="/j_spring_security_check?j_username="+loginId+"&j_password="+password;
//        String redirectUrl = "redirect:" + url;
//        return new ModelAndView(redirectUrl);
        return null;
    }
}
