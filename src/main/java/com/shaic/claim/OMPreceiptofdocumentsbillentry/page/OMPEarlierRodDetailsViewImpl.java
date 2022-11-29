package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

import java.util.List;
import java.util.WeakHashMap;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.vaadin.ui.Panel;

public class OMPEarlierRodDetailsViewImpl extends AbstractMVPView implements OMPEarlierRodDetailsView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<OMPDocumentDetailsViewTable> documentDetailsViewTableInstance;
	
	private OMPDocumentDetailsViewTable documentDetailsViewObj;
	
	private Panel mainPanel = new Panel();
	
	private Double currencyRate;
	
	WeakHashMap<String, Object> referenceData;

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	
	public void init(Long claimKey,Long rodKey){
		
		documentDetailsViewObj = documentDetailsViewTableInstance.get();
		documentDetailsViewObj.init("Document Details",false, false);
		
		if (claimKey != null) {
			fireViewEvent(OMPEarlierRodDetailsPresenter.SET_OMP_TABLE_VALUE, claimKey,rodKey);
			documentDetailsViewObj.setClaimKey(claimKey);
		}
		
		mainPanel.setContent(documentDetailsViewObj);
		
		setCompositionRoot(mainPanel);
		
	}
	
	@Override
	public void setReferenceData(WeakHashMap<String, Object> referenceDataMap){
		this.referenceData = referenceDataMap;
	}

	@Override
	public void setTableList(List<ViewDocumentDetailsDTO> setTableList, Double currRate) {
		currencyRate = currRate;
		documentDetailsViewObj.setCurrRate(currencyRate);
		documentDetailsViewObj.setReferenceData(this.referenceData);
		
		int i =1;
		for (ViewDocumentDetailsDTO documentDetailsDTO : setTableList) {
			documentDetailsDTO.setSno(i);
			documentDetailsViewObj.addBeanToList(documentDetailsDTO);
			i++;
			
		}
		
		if(! setTableList.isEmpty()){
		documentDetailsViewObj.setRowColor(setTableList.get(0).getLatestKey());
		}
		
		documentDetailsViewObj.setTableValues(setTableList);
		
	}

}
