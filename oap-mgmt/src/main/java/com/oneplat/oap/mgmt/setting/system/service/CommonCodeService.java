package com.oneplat.oap.mgmt.setting.system.service;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.setting.system.model.CommonCode;
import com.oneplat.oap.mgmt.setting.system.model.CommonCode.ModifyCommonCode;
import com.oneplat.oap.mgmt.setting.system.model.CommonCode.ModifyCommonCodeSort;
import com.oneplat.oap.mgmt.setting.system.model.CommonGroupCode;
import com.oneplat.oap.mgmt.setting.system.model.CommonGroupCode.ModifyCommonGroupCode;
import com.oneplat.oap.mgmt.setting.system.model.ResponseCommonCode;
import com.oneplat.oap.mgmt.setting.system.model.ResponseCommonGroupCode;

public interface CommonCodeService {
	public ResponseCommonGroupCode searchCommonGroupCodeList(SearchRequest searchRequest);

	public ResponseCommonCode searchCommonCodeList(SearchRequest searchRequest);

	public int getDuplicateCount(SearchRequest searchRequest);

	public Object createCommonGroupCode(CommonGroupCode commonGroupCode);

	public Object modifyCommonGroupCode(ModifyCommonGroupCode commonGroupCode);

	public Object createCommonCode(CommonCode commonCode);

	public Object modifyCommonCode(ModifyCommonCode commonCode);

	int modifyCommonCodeSort(ModifyCommonCodeSort sortModel);
}
