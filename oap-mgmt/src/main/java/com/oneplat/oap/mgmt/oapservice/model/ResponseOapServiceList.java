package com.oneplat.oap.mgmt.oapservice.model;

import com.oneplat.oap.core.model.AbstractPagableResponse;
import com.oneplat.oap.core.model.PageInfo;
import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Response oap service list.
 *
 * @author lee
 * @date 2016 -12-01
 */
@ApiModel
@NoArgsConstructor
public class ResponseOapServiceList extends AbstractPagableResponse<OapService>{

    /**
     * Instantiates a new Response oap service list.
     *
     * @param data     the data
     * @param pageInfo the page info
     */
    public ResponseOapServiceList(List<OapService> data, PageInfo pageInfo) {
        super(data, pageInfo);
    }
}
