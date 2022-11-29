package com.shaic.claim.linkedPolicyDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;


public class ViewLinkedPolicyDetails extends ViewComponent{

	@Inject
	private ViewLinkedPolicyTable viewLinkedPolicyTable;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private InsuredService insuredService;

	@EJB
	private CreateRODService createRodService;

	@EJB
	private IntimationService intimationService;
	
	private VerticalLayout vertLayout;
	
	private Panel mainLayout;
	
	private static Window popup;
	
	public void init(PreauthDTO preauthDto,Window parent){
		
		popup = parent;
		Intimation intimationDetails = createRodService.getIntimationByNo(preauthDto.getNewIntimationDTO().getIntimationId());
	
		Insured insuredDetails = insuredService.getInsuredByInsuredKey(intimationDetails.getInsured().getKey());
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<String, String> linkPolicyDetails = dbCalculationService.getLinkedPolicyDetails(preauthDto.getPolicyDto().getPolicyNumber(), insuredDetails.getLinkEmpNumber());
		
		TextField corporateName = new TextField();
		corporateName.setCaption("Name of the Corporate");
		corporateName.setValue(linkPolicyDetails.get(SHAConstants.PROPOSER_NAME));
		corporateName.setWidth("100%");
		corporateName.setReadOnly(true);
		corporateName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		corporateName.setNullRepresentation("-");
		
		TextField insuredName = new TextField();
		insuredName.setCaption("Name of the Insured");
		insuredName.setValue(insuredDetails.getInsuredName());
		insuredName.setWidth("100%");
		insuredName.setReadOnly(true);
		insuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		insuredName.setNullRepresentation("-");
		
		TextField policyNumber = new TextField();
		policyNumber.setCaption("Policy Number");
		policyNumber.setValue(linkPolicyDetails.get(SHAConstants.POLICY_NUMBER));
		policyNumber.setWidth("100%");
		policyNumber.setReadOnly(true);
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		policyNumber.setNullRepresentation("-");
		
		TextField mainMemberName = new TextField();
		mainMemberName.setCaption("Name of Main Member");
		mainMemberName.setValue(linkPolicyDetails.get(SHAConstants.INSURED_NAME));
		mainMemberName.setWidth("100%");
		mainMemberName.setReadOnly(true);
		mainMemberName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		mainMemberName.setNullRepresentation("-");
		
		TextField mainMemberId = new TextField();
		mainMemberId.setCaption("Main Member ID");
		mainMemberId.setValue(linkPolicyDetails.get(SHAConstants.EMPLOYEE_ID));
		mainMemberId.setWidth("100%");
		mainMemberId.setReadOnly(true);
		mainMemberId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		mainMemberId.setNullRepresentation("-");
		
		FormLayout linkedPolicyDetails = new FormLayout(corporateName,insuredName,policyNumber,mainMemberName,mainMemberId);
		linkedPolicyDetails.setSpacing(true);
		linkedPolicyDetails.setMargin(true);
		linkedPolicyDetails.setStyleName("layoutDesign");
		
		final Button btnClose = new Button();
		btnClose.setCaption("OK");
		btnClose.addStyleName(ValoTheme.BUTTON_DANGER);
		btnClose.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
//				Window parentWindow = (Window)btnClose.getData();
				UI.getCurrent().removeWindow(popup);
//				parentWindow.close();
			}
		});
		
		HorizontalLayout closebutLayout = new HorizontalLayout(btnClose);
		closebutLayout.setSizeFull();
		closebutLayout.setComponentAlignment(btnClose, Alignment.BOTTOM_CENTER);
		
		VerticalLayout vLayout = new VerticalLayout(linkedPolicyDetails,closebutLayout);
		vLayout.setSpacing(true);
		setCompositionRoot(vLayout);
	}
	
	/*public void init(PreauthDTO preauthDto,Window parent){
		
		viewLinkedPolicyTable.init("", false, false);
		
		List<ViewLinkedPolicyTableDTO> details = getLinkedPolcyDetails(preauthDto);
		viewLinkedPolicyTable.setTableList(details);
		vertLayout = new VerticalLayout();
		vertLayout.addComponent(viewLinkedPolicyTable);
//		mainLayout = new Panel(viewLinkedPolicyTable);
//		mainLayout.setWidth("100%");
		
		final Button okBtn = new Button("OK");
		okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		okBtn.setData(parent);
		okBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window parentWindow = (Window)okBtn.getData();		
				parentWindow.close();
			}
		});
		
		vertLayout.addComponent(okBtn);
		vertLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);
		setCompositionRoot(vertLayout);
		
	}*/
	
	private List<ViewLinkedPolicyTableDTO> getLinkedPolcyDetails(PreauthDTO preauthDto){
		List<ViewLinkedPolicyTableDTO> linkedPolicy = new ArrayList<ViewLinkedPolicyTableDTO>();
		
		ViewLinkedPolicyTableDTO policyDto = new ViewLinkedPolicyTableDTO();
		policyDto.setNameOftheCorporate(preauthDto.getPolicyDto().getProposerFirstName());
		policyDto.setPolicyNumber(preauthDto.getPolicyDto().getPolicyNumber());
		policyDto.setMainMemberName(preauthDto.getNewIntimationDTO().getGmcMainMemberName());
		policyDto.setMainMemberId(preauthDto.getNewIntimationDTO().getGmcMainMemberId().toString());
		linkedPolicy.add(policyDto);
		
		return linkedPolicy;
	}
}
