package com.shaic.claim.pcc.zonalMedicalHead;

import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.dto.PCCUploadDocumentsDTO;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.uploadrodreports.UploadedDocumentsDTO;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class ProcessPCCZonalMedicalHeadRequestWizardViewImpl extends
		AbstractMVPView implements ProcessPCCZonalMedicalHeadRequestWizardView {
	
	@Inject
	private Instance<ProcessPccZonalMedicalHeadRequestPage> processPccZonalMedicalHeadRequestPageInst;
	
	private ProcessPccZonalMedicalHeadRequestPage processPccZonalMedicalHeadRequestPage;
	
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
	
	public void initView(PccDetailsTableDTO pccDetailsTableDTO){
		this.bean = pccDetailsTableDTO;
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(pccDetailsTableDTO.getIntimationDto(),pccDetailsTableDTO.getClaimDto(),"");

		processPccZonalMedicalHeadRequestPage = processPccZonalMedicalHeadRequestPageInst.get();
		processPccZonalMedicalHeadRequestPage.init(pccDetailsTableDTO,presenterString);
		
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
		
		VerticalLayout mainVertical = new VerticalLayout(intimationDetailsCarousel,commonButtonLayout(),processPccZonalMedicalHeadRequestPage,buttonHor);
		mainVertical.setComponentAlignment(processPccZonalMedicalHeadRequestPage, Alignment.BOTTOM_CENTER);
		mainVertical.setComponentAlignment(buttonHor, Alignment.BOTTOM_RIGHT);
		mainVertical.setSpacing(true);
		addListener();
		setCompositionRoot(mainVertical);
		
	}


	public VerticalLayout commonButtonLayout(){
		
		viewDetails.initView(bean.getIntimationDto().getIntimationId(),null, ViewLevels.PREAUTH_MEDICAL,"Zonal Medical Head");
		
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
				fireViewEvent(MenuItemBean.ZONAL_MEDICAL_HEAD_PCC_PROCESS, null);

			}
		});
	}


	@Override
	public void generateResponseLayout() {
		processPccZonalMedicalHeadRequestPage.generateResponseLayout();
		
	}

	@Override
	public void generateAssignLayout() {
		processPccZonalMedicalHeadRequestPage.generateAssignLayout();
		
	}

	@Override
	public void generateFieldsBasedOnNegotiation(Boolean isChecked) {
		processPccZonalMedicalHeadRequestPage.generateNegotiation(isChecked);
	}
	

	@Override
	public void editPCCUploadDocumentDetails(PCCUploadDocumentsDTO dto) {
		processPccZonalMedicalHeadRequestPage.editPCCUploadDocumentDetails(dto);
		
	}
	
	@Override
	public void deletePCCUploadedDocumentDetails(PCCUploadDocumentsDTO dto) {
		processPccZonalMedicalHeadRequestPage.deletePCCUploadedDocumentDetails(dto);
		
	}
	
	@Override
	public void updateTableValues(List<PCCUploadDocumentsDTO> uploadedDocList) {

		processPccZonalMedicalHeadRequestPage.setUploadTableValues(uploadedDocList);

	}
	
	@Override
	public List<PCCUploadDocumentsDTO> getUploadedDocDtoList(){
		return processPccZonalMedicalHeadRequestPage.getUploadedTableValues();
	}
	
	@Override
	public void addUserDetails(BeanItemContainer<SelectValue> userDetailsContainer) {
		processPccZonalMedicalHeadRequestPage.addUserDetails(userDetailsContainer);
	}
	
	
    private void addListener() {

		
		submitBtn.addClickListener(new Button.ClickListener() {
 
			@Override
			public void buttonClick(ClickEvent event) {
				if (processPccZonalMedicalHeadRequestPage.validatePage()) {
					PccDTO pccDTO = processPccZonalMedicalHeadRequestPage.getvalues();
					if(pccDTO !=null){
						String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
						fireViewEvent(ProcessPCCZonalMedicalHeadRequestPresenter.SUBMIT_ZONAL_MEDICAL_HEAD_DETAILS,pccDTO,userName);
					}		
				} 
			}
		});
		
		cancelBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
						"Confirmation",
						"Are you sure you want to cancel ?",
						"No", "Yes", new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (!dialog.isConfirmed()) {
							pccRequestService.lockandUnlockIntimation(bean.getPccKey(),"UNLOCK",null);
							fireViewEvent(MenuItemBean.ZONAL_MEDICAL_HEAD_PCC_PROCESS, true);
						}
					}
				});
				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
	
		
	}


	


}
