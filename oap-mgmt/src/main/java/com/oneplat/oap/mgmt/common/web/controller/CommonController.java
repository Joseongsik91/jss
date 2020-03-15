package com.oneplat.oap.mgmt.common.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oneplat.oap.core.exception.UnauthorizedException;
import com.oneplat.oap.mgmt.login.model.LoginUser;

@RestController
@RequestMapping(value = "/common/")
public class CommonController {

    @Autowired
    private Environment env;
    final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/healthCheck", method = RequestMethod.GET)
    public @ResponseBody Object getHealthCheck() {
        return "OK";
    }
    @RequestMapping(value = "/sessionUserInfo", method = RequestMethod.GET)
    public @ResponseBody Object getHealthCheck(@AuthenticationPrincipal LoginUser loginUser) {
        if(loginUser == null)
            throw new UnauthorizedException();
        return loginUser;
    }

}
