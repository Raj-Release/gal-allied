package com.shaic.claim.pedrequest.view;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.pcc.SearchProcessPCCRequestMapper;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.pcc.dto.ViewPCCRemarksDTO;
import com.shaic.claim.pcc.views.ViewPCCRemarksDetailsPage;
import com.shaic.claim.preauth.view.ViewPostCashlessRemarkTable;
import com.shaic.claim.preauth.wizard.dto.ViewPccRemarksDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.PreauthService;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewPostCashlessRemarkUI extends ViewComponent{
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ClaimService claimService;
	
	@Inject
	private ViewPostCashlessRemarkTable viewPostCashlessRemark;
	
	private VerticalLayout mainLayout;
	
	@SuppressWarnings("deprecation")
	public void init(List<ViewPccRemarksDTO> pccRemarksDTOHistoryDetails){
		
		viewPostCashlessRemark.init("", false, false);
		if(pccRemarksDTOHistoryDetails != null && ! pccRemarksDTOHistoryDetails.isEmpty()){
			viewPostCashlessRemark.setTableList(pccRemarksDTOHistoryDetails);
		}
		mainLayout = new VerticalLayout(viewPostCashlessRemark);
		mainLayout.setMargin(true);
		mainLayout.setHeight("100%");
		setHeight("100%");
		setCompositionRoot(mainLayout);
	}

	public VerticalLayout getLayout(){
		return mainLayout;
	}
	
	public void setClearReferenceData(){
    	if(mainLayout!=null){
    		mainLayout.removeAllComponents();
    	}
    	
    	
    	
    }

}
