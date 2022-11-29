package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

import java.util.List;
import java.util.WeakHashMap;

import com.shaic.arch.GMVPView;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;

public interface OMPEarlierRodDetailsView extends GMVPView {
	
	public void setTableList(List<ViewDocumentDetailsDTO> setTableList,Double currRate);
	
	public void setReferenceData(WeakHashMap<String, Object> referenceDataMap);
	

}
