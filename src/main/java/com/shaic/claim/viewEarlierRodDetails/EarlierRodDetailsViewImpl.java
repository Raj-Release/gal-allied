package com.shaic.claim.viewEarlierRodDetails;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.SHAUtils;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.ui.Panel;


public class EarlierRodDetailsViewImpl extends AbstractMVPView implements EarlierRodDetailsView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<DocumentDetailsViewTable> documentDetailsViewTableInstance;
	
	private DocumentDetailsViewTable documentDetailsViewObj;
	
	private Panel mainPanel = new Panel();

	@EJB
	private ClaimService claimService;
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	
	public void init(Long claimKey,Long rodKey){
		
		documentDetailsViewObj = documentDetailsViewTableInstance.get();
		documentDetailsViewObj.init("Document Details",false, false);
		
		if (claimKey != null) {
			
			Claim claim = claimService.getClaimByClaimKey(claimKey);
			if(claim.getIntimation().getLobId()!= null && ((ReferenceTable.PA_LOB_KEY).equals(claim.getIntimation().getLobId().getKey()) || 
					((ReferenceTable.PACKAGE_MASTER_VALUE).equals(claim.getIntimation().getLobId().getKey()) && claim.getIntimation().getProcessClaimType().equalsIgnoreCase("P"))))
			{
				documentDetailsViewObj.setPAColumns();
			}
			
			fireViewEvent(EarlierRodDetailsPresenter.SET_TABLE_VALUE, claimKey,rodKey);	
			documentDetailsViewObj.setClaimKey(claimKey);
		}
		
		mainPanel.setContent(documentDetailsViewObj);
		
		setCompositionRoot(mainPanel);
		
	}

	@Override
	public void setTableList(List<ViewDocumentDetailsDTO> setTableList) {
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

	 public void setClearReferenceData(){
	    	
	 if(this.documentDetailsViewObj!=null){
	    this.documentDetailsViewObj.removeRow();
	    this.documentDetailsViewObj=null;
	    }
	  }
	
}
