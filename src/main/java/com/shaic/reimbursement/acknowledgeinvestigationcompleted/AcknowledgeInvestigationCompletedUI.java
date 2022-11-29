package com.shaic.reimbursement.acknowledgeinvestigationcompleted;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.domain.Investigation;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.reimbursement.investigation.ackinvestivationcompleted.search.SearchAckInvestigationCompletedTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class AcknowledgeInvestigationCompletedUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;

	@Inject
	private RevisedCarousel preauthIntimationDetailsCarousel;

	@Inject
	private InvestigationDetailsTableUI investigationDetailsTableUI;

	@Inject
	private InvestigationCompletionDetailsUI investigationCompletionDetailsUI;

	@Inject
	private Investigation investigation;

	@Inject
	private ClaimDto claimDto;
	
	@Inject
	private ViewDetails viewDetails;	

	private List<Investigation> investigationList;
	
	private BeanItemContainer<SelectValue> confirmedBy;
	
	private SearchAckInvestigationCompletedTableDTO bean;
	
	private RRCDTO rrcDTO;
	

	PreauthDTO preauthDTO = null;


	public void initView(SearchAckInvestigationCompletedTableDTO investigationKey) {
		bean = investigationKey;
		
		
		
		fireViewEvent(AcknowledgeInvestigationCompletedPresenter.SET_REFERENCE,
				bean.getInvestigationKey());
	}
	
	public void initView(SearchAckInvestigationCompletedTableDTO investigationKey,PreauthDTO preauthDTO) {
		bean = investigationKey;
		this.preauthDTO = preauthDTO;
		
		
		fireViewEvent(AcknowledgeInvestigationCompletedPresenter.SET_REFERENCE,
				bean.getInvestigationKey());
	}

	@SuppressWarnings("unchecked")
	public void setReference(Map<String, Object> referenceObj) {
		this.investigation = (Investigation) referenceObj
				.get(AcknowledgeInvestigationCompletedPresenter.INVESTIGATION_OBJECT);
		this.claimDto = (ClaimDto) referenceObj
				.get(AcknowledgeInvestigationCompletedPresenter.CLAIM_OBJECT);
		this.investigationList = (List<Investigation>) referenceObj
				.get(AcknowledgeInvestigationCompletedPresenter.INVESTIGATION_LIST_OBJECT);
		this.confirmedBy = (BeanItemContainer<SelectValue>) referenceObj.get(AcknowledgeInvestigationCompletedPresenter.CONFIRMED_BY);

	}

	public void setLayout() {
		if (investigation != null && claimDto != null) {
			preauthIntimationDetailsCarousel.init(
					claimDto.getNewIntimationDto(), this.claimDto,
					"Acknowledge Investigation Completed");
			viewDetails.initView(investigation.getIntimation().getIntimationId(),bean.getRodKey(), ViewLevels.INTIMATION, false,"Acknowledge Investigation Completed");
			investigationDetailsTableUI.initView(investigation.getKey(), investigationList);
			investigationCompletionDetailsUI.init(investigation.getKey(), this.confirmedBy,bean);
		/*	mainLayout = new VerticalLayout(preauthIntimationDetailsCarousel, viewDetails, investigationDetailsTableUI,
					investigationCompletionDetailsUI);*/
			HorizontalLayout hLayout = new HorizontalLayout(commonButtonsLayout(),viewDetails);
			hLayout.setWidth("100%");
			hLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
			mainLayout = new VerticalLayout(preauthIntimationDetailsCarousel,hLayout,investigationDetailsTableUI,
					investigationCompletionDetailsUI);
			mainLayout.setSpacing(true);
		//	mainLayout.setMargin(true);
			setCompositionRoot(mainLayout);
		}
	}
	
	public void setReferenceForDto(List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList){
		investigationDetailsTableUI.setReferenceForDto(investigationDetailsReimbursementTableDTOList);
	}
	
	public void setCompleteLayout(){
		investigationDetailsTableUI.setCompleteLayout();
	}
	
	public HorizontalLayout commonButtonsLayout()
	{
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
		
		HorizontalLayout rrcBtnLayout = new HorizontalLayout(btnRRC);
		//VerticalLayout vLayout = new VerticalLayout(viewDetails,dummyLable,rrcBtnLayout);
		
		//vLayout.setComponentAlignment(rrcBtnLayout, Alignment.TOP_RIGHT);
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(
				rrcBtnLayout);
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(AcknowledgeInvestigationCompletedPresenter.VALIDATE_ACKNOWLEDGE_INVESTIGATION_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
	}

	
}
