package com.oneplat.oap.mgmt.setting.system.model;

import java.util.List;
import com.oneplat.oap.core.model.AbstractPagableResponse;
import com.oneplat.oap.core.model.PageInfo;
import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;

@ApiModel
@NoArgsConstructor
public class ResponseAdaptorList extends AbstractPagableResponse<Adaptor> {

	public ResponseAdaptorList(List<Adaptor> data, PageInfo pageInfo) {
		// TODO Auto-generated constructor stub
		super(data, pageInfo);
	}
}