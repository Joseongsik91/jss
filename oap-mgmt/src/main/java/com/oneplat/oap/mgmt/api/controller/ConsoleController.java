package com.oneplat.oap.mgmt.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneplat.oap.mgmt.api.model.ApiConsoleRequest;
import com.oneplat.oap.mgmt.api.service.ConsoleService;
import com.oneplat.oap.mgmt.util.RestMultipart;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 *
 * ConsoleController
 *
 * Created by Hong Gi Seok 2016-12-08
 */
@Api(description="API 관리", produces = "application/json")
@RestController
@RequestMapping(value = "/console")
public class ConsoleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleController.class);

    /** timeout */
    private static final int TIMEOUT = 40000;

    @Autowired
    private ConsoleService consoleService;

    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/{apiNumber}", method = RequestMethod.GET)
    public Object getConsoleApi(@PathVariable (value = "apiNumber") long apiNumber) {
        return consoleService.selectApiInfo(apiNumber);
    }

    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/service", method = RequestMethod.GET)
    public Object getConsoleService() {
        return consoleService.selectApiServiceList();
    }

    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/service/{serviceNumber}", method = RequestMethod.GET)
    public Object getConsoleServiceApi(@PathVariable(value = "serviceNumber") long serviceNumber) {
        return consoleService.selectApiServiceApiList(serviceNumber);
    }


    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/application", method = RequestMethod.GET)
    public Object getConsoleApplication() {
        return consoleService.selectApplicationConsole();
    }


    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public @ResponseBody Object getConsoleApi(
            @RequestParam("data") String data,
            @RequestParam("files") List<MultipartFile> files, HttpServletRequest request) {

        ApiConsoleRequest apiConsoleRequest = new ApiConsoleRequest();
        LOGGER.debug(request.getContentType());

        ObjectMapper mapper = new ObjectMapper();
        try {
            apiConsoleRequest = mapper.readValue(data, ApiConsoleRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RestMultipart restMultipart = new RestMultipart();
        LOGGER.debug("-----{}", apiConsoleRequest);

        return consoleService.consoleTest(apiConsoleRequest, files, restMultipart, request);
    }

}
