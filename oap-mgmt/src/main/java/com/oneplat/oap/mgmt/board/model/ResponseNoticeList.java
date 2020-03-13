package com.oneplat.oap.mgmt.board.model;

import com.oneplat.oap.core.model.AbstractPagableResponse;
import com.oneplat.oap.core.model.PageInfo;
import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@NoArgsConstructor
public class ResponseNoticeList extends AbstractPagableResponse<Board> {

	/**
	 * @param data
	 * @param pageInfo
	 */
	public ResponseNoticeList(List<Board> data, PageInfo pageInfo) {
		super(data, pageInfo);
	}

}
