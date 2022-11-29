package com.shaic.claim.pcc.reassignHRM;

import java.util.HashMap;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.hrmCoordinator.ProcessPCCHrmCoordinatorRequestPresenter;
import com.shaic.claim.pcc.hrmCoordinator.ProcessPCCHrmCoordinatorRequestWizardView;
import com.shaic.claim.pcc.hrmCoordinator.ProcessPccHRMCoordinatorRequestPage;
import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class ProcessPCCReassignHrmCoordinatorRequestWizardViewImpl extends
AbstractMVPView implements ProcessPCCReassginHrmCoordinatorRequestWizardView {


	
	@Inject
	private Instance<ProcessPccReassignHRMCoordinatorRequestPage> processPccReassignHRMCoordinatorRequestPageInst;
	
	private ProcessPccReassignHRMCoordinatorRequestPage processPccReassignHRMCoordinatorRequestPage;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	@Inject
	private ViewDetails viewDetails;
	
	@EJB
	private PolicyService policyService;
	
	private PccDetailsTableDTO bean;
	
	private String presenterString;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	@EJB
	private SearchProcessPCCRequestService pccRequestService;
	
	public void initView(PccDetailsTableDTO searchDTO){
		this.bean = searchDTO;
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(searchDTO.getIntimationDto(),searchDTO.getClaimDto(),"");

		processPccReassignHRMCoordinatorRequestPage = processPccReassignHRMCoordinatorRequestPageInst.get();
		processPccReassignHRMCoordinatorRequestPage.init(searchDTO,presenterString);
		
		submitBtn = new Button("Submit");
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn = new Button("Cancel");		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		HorizontalLayout buttonHor = new HorizontalLayout(submitBtn,cancelBtn);
		buttonHor.setSpacing(true);
		buttonHor.setMargin(false);
		
		VerticalLayout mainVertical = new VerticalLayout(intimationDetailsCarousel,commonButtonLayout(),processPccReassignHRMCoordinatorRequestPage,buttonHor);
		mainVertical.setComponentAlignment(processPccReassignHRMCoordinatorRequestPage, Alignment.BOTTOM_CENTER);
		mainVertical.setComponentAlignment(buttonHor, Alignment.BOTTOM_RIGHT);
		mainVertical.setSpacing(true);
		addListener();
		setCompositionRoot(mainVertical);
		
	}

	

	public VerticalLayout commonButtonLayout(){
		
		viewDetails.initView(bean.getIntimationDto().getIntimationId(),null, ViewLevels.PREAUTH_MEDICAL,"Re-Assign HRM");
		
		HorizontalLayout componentsHLayout = new HorizontalLayout(viewDetails);
		componentsHLayout.setSpacing(true);
		componentsHLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		componentsHLayout.setWidth("100%");
		
		VerticalLayout vLayout = new VerticalLayout(componentsHLayout);
		vLayout.setSpacing(true);
		
		vLayout.setWidth("100%");
		
		return vLayout;
	}

	@Override 
	public void resetView() {
		
	}

	@Override
	public void buildSuccessLayout() {
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("Submited successfully", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MenuItemBean.REASSIGN_HRM, null);

			}
		});
	}
	
	private void addListener() {


		
		submitBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (processPccReassignHRMCoordinatorRequestPage.validatePage()) {
					PccDTO pccDTO = processPccReassignHRMCoordinatorRequestPage.getvalues();
					if(pccDTO !=null){
						String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
						fireViewEvent(ProcessPCCReassignHrmCoordinatorRequestPresenter.SUBMIT_REASSIGN_HRM_COORDINATOR_DETAILS,pccDTO,userName,bean);
					}			
				} 
			}
		});
		
		cancelBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
				buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createConfirmationbox("Are you sure you want to cancel ?", buttonsNamewithType); 
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES.toString());
				Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO.toString());
				homeButton.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						pccRequestService.lockandUnlockIntimation(bean.getPccKey(),"UNLOCK",null);
						fireViewEvent(MenuItemBean.REASSIGN_HRM, true);															
					}
				});
			}
		});		
	}

	/*@Override
	public void addUserDetails(BeanItemContainer<SelectValue> userDetailsContainer) {
		processPccReassignHRMCoordinatorRequestPage.addUserDetails(userDetailsContainer);
		
	}*/


}
