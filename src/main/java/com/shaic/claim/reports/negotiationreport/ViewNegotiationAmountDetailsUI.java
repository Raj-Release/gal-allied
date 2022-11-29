package com.shaic.claim.reports.negotiationreport;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.preauth.view.ViewPostCashlessRemarkTable;
import com.shaic.claim.preauth.wizard.dto.ViewPccRemarksDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.PreauthService;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewNegotiationAmountDetailsUI extends ViewComponent{
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private PreauthService preauthService;
	
	@Inject
	private ViewNegotiationAmountDetailsTable viewNegotiationAmountDetailsTable;
	
	private VerticalLayout mainLayout;
	
	@SuppressWarnings("deprecation")
	public void init(Intimation intimation){
		viewNegotiationAmountDetailsTable.init("", false, false);
		
		if(intimation != null
				&& intimation.getKey() !=null){
			List<NegotiationAmountDetailsDTO> negotiationAmountDetailsDTOs = 
					preauthService.getNegotiationAmountDetailsByIntimationKey(intimation.getKey());
			if(negotiationAmountDetailsDTOs !=null
					&& !negotiationAmountDetailsDTOs.isEmpty()){
				viewNegotiationAmountDetailsTable.setTableList(negotiationAmountDetailsDTOs);
			}
		}
		mainLayout = new VerticalLayout(viewNegotiationAmountDetailsTable);
		mainLayout.setMargin(true);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		//this.setHeight("500px");
		//this.setWidth("1000px");
		 
		setCompositionRoot(mainLayout);
	}
	
	public VerticalLayout getLayout(){
		return mainLayout;
	}
}
