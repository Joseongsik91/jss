package com.oneplat.oap.mgmt.api.service;

import com.oneplat.oap.mgmt.api.model.ApiConsoleRequest;
import com.oneplat.oap.mgmt.api.model.ApiInfo;
import com.oneplat.oap.mgmt.util.RestMultipart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * ConsoleService
 *
 * Created by Hong Gi Seok 2016-12-08
 */
public interface ConsoleService {

    ApiInfo selectApiInfo(Long apiNumber);

    Map<String, Object> consoleTest(ApiConsoleRequest apiConsoleRequest, List<MultipartFile> files, RestMultipart restMultipart, HttpServletRequest request);

    Map<String, Object> selectApiServiceList();

    Map<String, Object> selectApiServiceApiList(long serviceNumber);

    Map<String, Object> selectApplicationConsole();
}
