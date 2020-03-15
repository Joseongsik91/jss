package com.oneplat.oap.mgmt.setting.system.service;

import java.util.List;
import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.setting.system.model.Adaptor;

public interface AdaptorService {

	public List<Adaptor> adaptorList(SearchRequest searchRequest);

	public Adaptor getAdaptorInfo(Long adaptorNumber);

	public Object beanIdCheck(String adaptorBeanId);

	public Object createAdaptor(Adaptor adaptor);

	public Adaptor modifyAdaptor(Adaptor adaptor);

	public void deleteAdaptor(Long adaptorNumber);
}
