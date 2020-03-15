package com.oneplat.oap.mgmt.setting.system.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.oneplat.oap.core.exception.ValidationException;
import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.common.cache.CommonCodeCache;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.setting.system.mapper.CommonCodeMapper;
import com.oneplat.oap.mgmt.setting.system.model.CommonCode;
import com.oneplat.oap.mgmt.setting.system.model.CommonCode.ModifyCommonCode;
import com.oneplat.oap.mgmt.setting.system.model.CommonCode.ModifyCommonCodeSort;
import com.oneplat.oap.mgmt.setting.system.model.CommonGroupCode;
import com.oneplat.oap.mgmt.setting.system.model.CommonGroupCode.ModifyCommonGroupCode;
import com.oneplat.oap.mgmt.setting.system.model.ResponseCommonCode;
import com.oneplat.oap.mgmt.setting.system.model.ResponseCommonGroupCode;
import com.oneplat.oap.mgmt.setting.system.service.CommonCodeService;

@Service
public class CommonCodeServiceImpl implements CommonCodeService {

	@Autowired
	CommonCodeMapper commonCodeMapper;

	@Autowired
	AuthenticationInjector authenticationInjector;
	
	@Autowired
	CommonCodeCache commonCodeCache;
	
	@Override
	public ResponseCommonGroupCode searchCommonGroupCodeList(SearchRequest searchRequest) {
		// TODO Auto-generated method stub
		ResponseCommonGroupCode response = new ResponseCommonGroupCode();
		List<CommonGroupCode> list = commonCodeMapper.selectCommonGroupCodeList(searchRequest);
		response.setData(list);

		if (searchRequest.getPageInfo() != null) {
			searchRequest.getPageInfo()
					.setTotalCount(list == null ? 0 : commonCodeMapper.getCommonGroupCodeListCount(searchRequest));
			searchRequest.getPageInfo().setResultCount(list == null ? 0 : list.size());
		}

		return response;
	}

	@Override
	public ResponseCommonCode searchCommonCodeList(SearchRequest searchRequest) {
		// TODO Auto-generated method stub
		ResponseCommonCode response = new ResponseCommonCode();
		List<CommonCode> list = commonCodeMapper.selectCommonoCodeList(searchRequest);
		response.setData(list);

		if (searchRequest.getPageInfo() != null) {
			searchRequest.getPageInfo()
					.setTotalCount(list == null ? 0 : commonCodeMapper.getCommonoCodeListCount(searchRequest));
			searchRequest.getPageInfo().setResultCount(list == null ? 0 : list.size());
		}

		return response;
	}

	@Override
	public int getDuplicateCount(SearchRequest searchRequest) {
		int count = 0;
		HashMap<String, Object> query = searchRequest.getQuery();
		if (query == null || StringUtils.isEmpty(query.get("codeType"))) {
			// exception
		} else {
			count = commonCodeMapper.getDuplicateCount(searchRequest);
		}
		return count;
	}

	@Override
	@Transactional
	public Object createCommonGroupCode(CommonGroupCode commonGroupCode) {
		// TODO Auto-generated method stub

		String result = null;
		boolean pass = true;

		CommonGroupCode validationCheckModel = null;

		if (!StringUtils.isEmpty(commonGroupCode.getGroupCode())) {
			if (!StringUtils.isEmpty(commonGroupCode.getGroupCodeName())) {
				validationCheckModel = new CommonGroupCode();
				validationCheckModel.setGroupCodeName(commonGroupCode.getGroupCodeName());
				if (commonCodeMapper.getCommonGroupCodeCount(validationCheckModel) > 0) {
					result = "groupCodeName";
					pass = false;
				}
			}
		}

		if (pass) {
			validationCheckModel = new CommonGroupCode();
			validationCheckModel.setGroupCode(commonGroupCode.getGroupCode());
			if (commonCodeMapper.getCommonGroupCodeCount(validationCheckModel) > 0) {
				result = "groupCode";
				pass = false;
			}
		}

		if (pass) {
			authenticationInjector.setAuthentication(commonGroupCode);
			commonCodeMapper.insertCommonGroupCode(commonGroupCode);
		}
		
		return result;
	}

	@Override
	@Transactional
	public Object modifyCommonGroupCode(ModifyCommonGroupCode modifyCommonGroupCode) {
		// TODO Auto-generated method stub

		CommonGroupCode commonGroupCode = null;
		String result = null;
		boolean pass = true;

		if (!modifyCommonGroupCode.getGroupCodeName().equals(modifyCommonGroupCode.getOldGroupCodeName())) {
			commonGroupCode = new CommonGroupCode();
			commonGroupCode.setGroupCodeName(modifyCommonGroupCode.getGroupCodeName());
			if (commonCodeMapper.getCommonGroupCodeCount(commonGroupCode) > 0) {
				result = "groupCodeName";
				pass = false;
			}
		}

		if (pass) {
			authenticationInjector.setAuthentication(modifyCommonGroupCode);
			commonCodeMapper.updateCommonGroupCode(modifyCommonGroupCode);
		}
		
		return result;
	}

	@Override
	@Transactional
	public Object createCommonCode(CommonCode commonCode) {
		// TODO Auto-generated method stub

		CommonCode validationCheckModel = null;
		String result = null;
		boolean pass = true;

		// 그룹코드 존재여부 확인
		if (!StringUtils.isEmpty(commonCode.getGroupCode())) {
			CommonGroupCode commonGroupCode = new CommonGroupCode();
			commonGroupCode.setGroupCode(commonCode.getGroupCode());
			if (commonCodeMapper.getCommonGroupCodeCount(commonGroupCode) == 0) {
				result = "groupCode";
				pass = false;
			}
		}

		if (pass) {
			if (!StringUtils.isEmpty(commonCode.getCodeName())) {
				validationCheckModel = new CommonCode();
				validationCheckModel.setGroupCode(commonCode.getGroupCode());
				validationCheckModel.setCodeName(commonCode.getCodeName());
				if (commonCodeMapper.getCommonCodeCount(validationCheckModel) > 0) {
					result = "codeName";
					pass = false;
				}
			}
		}

		if (pass) {
			if (!StringUtils.isEmpty(commonCode.getCode())) {
				validationCheckModel = new CommonCode();
				validationCheckModel.setCode(commonCode.getCode());
				if (commonCodeMapper.getCommonCodeCount(validationCheckModel) > 0) {
					result = "code";
					pass = false;
				}
			}
		}

		if (pass) {
			authenticationInjector.setAuthentication(commonCode);
			commonCodeMapper.insertCommonCode(commonCode);
		}
		
		//공통 코드 캐쉬 정보를 업데이트 해준다. 
        commonCodeCache.update();
        
		return result;
	}

	@Override
	@Transactional
	public Object modifyCommonCode(ModifyCommonCode modifyCommonCode) {
		// TODO Auto-generated method stub

		CommonCode commonCode = null;
		String result = null;
		boolean pass = true;

		if (!modifyCommonCode.getCodeName().equals(modifyCommonCode.getOldCodeName())) {
			commonCode = new CommonCode();
			commonCode.setGroupCode(modifyCommonCode.getGroupCode());
			commonCode.setCodeName(modifyCommonCode.getCodeName());
			if (commonCodeMapper.getCommonCodeCount(commonCode) > 0) {
				result = "codeName";
				pass = false;
			}
		}

		if (pass) {
			authenticationInjector.setAuthentication(modifyCommonCode);
			commonCodeMapper.updateCommonCode(modifyCommonCode);
		}
		
		//공통 코드 캐쉬 정보를 업데이트 해준다. 
        commonCodeCache.update();

		return result;
	}

	@Override
	@Transactional
	public int modifyCommonCodeSort(ModifyCommonCodeSort sortModel) {
		authenticationInjector.setAuthentication(sortModel);
		// TODO Auto-generated method stub
		// int beforeSortNumber = sortModel.getBeforeSortNum();
		// int afterSortNumber = sortModel.getAfterSortNum();
		// sortModel.setStartNum(beforeSortNumber<afterSortNumber?beforeSortNumber:afterSortNumber);
		// sortModel.setEndNum(beforeSortNumber>afterSortNumber?beforeSortNumber:afterSortNumber);
		// return commonCodeMapper.updateSortNumber(sortModel);
		int result = 0;
		if ("CD".equals(sortModel.getCodeType())) {
			commonCodeMapper.updateCodeSortNumber(sortModel);
		} else {
			commonCodeMapper.updateGroupCodeSortNumber(sortModel);
		}
		return result;

	}

}
