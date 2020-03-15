package com.oneplat.oap.mgmt.application.service;

import java.util.List;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.application.model.DashboardApplications;
import com.oneplat.oap.mgmt.application.model.DcApplication;
import com.oneplat.oap.mgmt.application.model.DcApplicationKey;
import com.oneplat.oap.mgmt.application.model.DcApplicationScope;
import com.oneplat.oap.mgmt.application.model.DcApplicationService;
import com.oneplat.oap.mgmt.application.model.DcApplicationSla;
import com.oneplat.oap.mgmt.application.model.DcApplicationSlaRequest;
import com.oneplat.oap.mgmt.application.model.DcDeveloper;

public interface ApplicationService {

    /**
     * 애플리케이션 등록
     * 
     * @param application
     */
    public DcApplication createApplication(DcApplication application);
    
    /**
     * 애플리케이션 수정
     * 
     * @param application
     */
    public DcApplication modifyApplication(DcApplication application);
    
    /**
     * 애플리케이션 조회
     * 
     * @param searchRequest
     * @return
     */
    public List<DcApplication> getApplicationList(SearchRequest searchRequest);
    
    /**
     * 애플리케이션 기본정보 조회
     * 
     * @param applicationNumber
     * @return
     */
    public DcApplication getApplicationInfo(Long applicationNumber);
    
    /**
     * 애플리케이션 키 등록
     * 
     * @param applicationKey
     */
    public DcApplication createApplicationKey(DcApplicationKey applicationKey);

    /**
     * 애플리케이션 키 편집
     * 
     * @param applicationKey
     */
    public DcApplication modifyApplicationKey(DcApplicationKey applicationKey);
    
    /**
     * 애플리케이션 키 재발급
     * 
     * @param applicationKey
     * @return
     */
    public DcApplication reissueApplicationKey(DcApplicationKey applicationKey);
    
    /**
     * 애플리케이션 키 삭제
     * 
     * @param applicationKey
     * @return
     */
    public DcApplication removeApplicationKey(DcApplicationKey applicationKey);
    
    /**
     * 애플리케이션 SLA 등록
     * 
     * @param applicationSla
     */
    public void createApplicationSla(DcApplicationSla applicationSla);
    
    /**
     * [미사용]
     * 애플리케이션 SLA 변경 요청
     * 
     * @param applicationSlaRequest
     */
    public void createApplicationSlaRequest(DcApplicationSlaRequest applicationSlaRequest);
    
    /**
     * 개발자 등록
     * 
     * @param developer
     */
    public void createDeveloper(DcDeveloper developer);

    /**
     * 애플리케이션 상세 조회(기본정보 | 키정보 | SLA정보)
     * 
     * @param applicationNumber
     * @return
     */
    public DcApplication getApplicationDetail(Long applicationNumber);

    /**
     * 검색조건 서비스 목록
     * @param searchRequest
     * @return
     */
    public List<DcApplicationService> getSearchServiceList(SearchRequest searchRequest);

    
    /**
     * 애플리케이션 SLA 변경
     * 
     * @param applicationSla
     */
    public DcApplication modifyApplicationSla(DcApplicationSla applicationSla);

    /**
     * 애플리케이션 이력 조회
     * 
     * @param applicationNumber
     * @return
     */
    public List<DcApplication> getApplicationDetailHistoryList(Long applicationNumber);

    /**
     * 애플리케이션 SLA 이력 조회
     * 
     * @param applicationSla
     * @return
     */
    public List<DcApplicationSla> getApplicationSlaHistoryList(DcApplicationSla applicationSla);

    /**
     * 애플리케이션 삭제
     * 
     * @param application
     * @return
     */
    public DcApplication removeApplication(DcApplication application);

    /**
     * 대시보드 애플리케이션 정보
     * 
     * @param searchRequest
     * @return
     */
    public DashboardApplications getDashboardApplications(SearchRequest searchRequest);

    /**
     * 스코프 목록 조회
     * 
     * @param applicationNumber
     * @return
     */
    public List<DcApplicationScope> getSearchScopeList(Long applicationNumber);

    /**
     * 애플리케이션 시크릿 키 재발급
     *
     * @param applicationScope
     * @return
     */
    public DcApplication reissueApplicationClientSecret(DcApplicationScope applicationScope);

    /**
     * 애플리케이션 스코프 등록
     * 
     * @param applicationScope
     * @return
     */
    public DcApplication createApplicationScope(DcApplicationScope applicationScope);

    /**
     * 애플리케이션 스코프 수정
     *
     * @param applicationScope
     * @return
     */
    public DcApplication modifyApplicationScope(DcApplicationScope applicationScope);

    /**
     * 애플리케이션 시크릿 키 재발급
     *
     * @param applicationKey
     * @return
     */
    public DcApplication reissueMessageEncryptionKey(DcApplicationKey applicationKey);


}
