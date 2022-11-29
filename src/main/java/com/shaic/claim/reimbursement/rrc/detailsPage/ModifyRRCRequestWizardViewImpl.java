/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.detailsPage;

/**
 * @author ntv.vijayar
 *
 */


import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPaayasPolicyDetailsPdfPage;
import com.shaic.domain.HospitalService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.IWizard;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;



/**
 * @author ntv.vijayar
 *
 */
public class ModifyRRCRequestWizardViewImpl extends AbstractMVPView implements ModifyRRCRequestDataExtractionWizard {
	
	@Inject
	private Instance<ModifyRRCRequestDataExtractionView> modifyRRCRequestDataExtractionObj;
	private ModifyRRCRequestDataExtractionView modifyRRCRequestDataExtraction;
	
	
	@Inject
	private Instance<IWizard> iWizard;
	
	
	//private VerticalSplitPanel mainPanel;
	private Panel mainPanel;
	
	private IWizard wizard;
	
	private FieldGroup binder;
	
	private RRCDTO bean;
	
	@Inject
	private ClaimWiseRRCHistoryPage claimWiseRRCHistoryPage;
	
	@Inject
	private ViewClaimWiseRRCHistoryPage viewRRCRequestHistoryPage;
	
	
	@Inject
	private ViewDetails viewDetails; 
	
	private Window popup;
	
	@EJB
	private HospitalService hospitalService;
	
	@Inject
	private ViewPaayasPolicyDetailsPdfPage pdfPageUI;

	
	
	private void initBinder() {
		
		wizard.getFinishButton().setCaption("Submit");
		this.binder = new FieldGroup();
		BeanItem<RRCDTO> item = new BeanItem<RRCDTO>(bean);
	//	item.addNestedProperty("rrcDTO");
		/*item.addNestedProperty("rrcDTO.employeeName");
		item.addNestedProperty("rrcDTO.employeeId");
		item.addNestedProperty("rrcDTO.employeeZone");
		item.addNestedProperty("rrcDTO.employeeDept");
		item.addNestedProperty("rrcDTO.policyNo");
		item.addNestedProperty("rrcDTO.intimationNo");
		item.addNestedProperty("rrcDTO.productName");
		item.addNestedProperty("rrcDTO.duration");
		item.addNestedProperty("rrcDTO.sumInsured");
		item.addNestedProperty("rrcDTO.hospitalName");
		item.addNestedProperty("rrcDTO.hospitalCity");
		item.addNestedProperty("rrcDTO.hospitalZone");
		item.addNestedProperty("rrcDTO.dateOfAdmission");
		item.addNestedProperty("rrcDTO.dateOfDischarge");
		item.addNestedProperty("rrcDTO.insuredName");
		item.addNestedProperty("rrcDTO.insuredAge");
		item.addNestedProperty("rrcDTO.sex");
		item.addNestedProperty("rrcDTO.claimType");
		item.addNestedProperty("rrcDTO.processingStage");
		item.addNestedProperty("rrcDTO.significantClinicalInformation");
		item.addNestedProperty("rrcDTO.significantClinicalInformationValue");
		
		item.addNestedProperty("rrcDTO.statusKey");
		item.addNestedProperty("rrcDTO.stageKey");
		item.addNestedProperty("rrcDTO.requestorStageKey");
		item.addNestedProperty("rrcDTO.rrcRequestType");
		item.addNestedProperty("rrcDTO.eligibility");
		item.addNestedProperty("rrcDTO.savedAmount");
		item.addNestedProperty("rrcDTO.eligibilityRemarks");
		item.addNestedProperty("rrcDTO.requestOnHoldRemarks");*/
		

		

/*		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");*/
		this.binder.setItemDataSource(item);	
	}
	
	public void initView(RRCDTO preauthDTO)
	{
		this.bean = preauthDTO;
		mainPanel = new Panel();
		//this.wizard = iWizard.get();
		this.wizard = new IWizard();
		initBinder();	
		modifyRRCRequestDataExtraction = modifyRRCRequestDataExtractionObj.get();
		modifyRRCRequestDataExtraction.init(bean,wizard);
		wizard.addStep(this.modifyRRCRequestDataExtraction,"Modify RRC Request");
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);	
		/*PreauthIntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Acknowledge Receipt of Documents");
		mainPanel.setFirstComponent(intimationDetailsCarousel);*/
		
		//VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout());
		
		
	/*	Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
		panel1.setHeight("50px");*/
		//VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		/*VerticalLayout commonBtnLayout = commonButtonsLayout();
		VerticalLayout wizardLayout2 = new VerticalLayout(commonBtnLayout, wizard);
		wizardLayout2.setComponentAlignment(commonBtnLayout, Alignment.MIDDLE_RIGHT);
		wizardLayout2.setSpacing(true);
		mainPanel.setContent(wizardLayout2);*/
		

		viewDetails.initView(bean.getClaimDTO().getNewIntimationDto().getIntimationId(),0l, ViewLevels.PREAUTH_MEDICAL,"");
		VerticalLayout commonBtnLayout = commonButtonsLayout();
		HorizontalLayout hLayout = new HorizontalLayout(commonBtnLayout,viewDetails);
		hLayout.setWidth("100%");
		hLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		hLayout.setComponentAlignment(commonBtnLayout, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout wizardLayout2 = new VerticalLayout(hLayout, wizard);
		
		mainPanel.setContent(wizardLayout2);

		
		
		mainPanel.setSizeFull();
		//mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		
		setCompositionRoot(mainPanel);			
		}

	
	private VerticalLayout commonButtonsLayout()
	{
		TextField rrcRequestNo = new TextField("RRC Request No");
		if(null != this.bean.getRrcRequestNo())
		{
			rrcRequestNo.setValue(String.valueOf(this.bean.getRrcRequestNo()));
		}
		else
		{
			rrcRequestNo.setValue("");
		}
		//Vaadin8-setImmediate() rrcRequestNo.setImmediate(true);
		rrcRequestNo.setWidth("250px");
		rrcRequestNo.setHeight("20px");
		rrcRequestNo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rrcRequestNo.setReadOnly(true);
	//	rrcRequestNo.setEnabled(false);
		rrcRequestNo.setNullRepresentation("");
		
		TextField rrcRequestType = new TextField("RRC Request Type");
		
		if(null != this.bean.getRrcRequestType())
		{
			rrcRequestType.setValue(this.bean.getRrcRequestType());
		}
		else
		{
			rrcRequestType.setValue("");
		}
		
		//rrcRequestType.setValue(String.valueOf(this.bean.getRrcRequestType()));
		//Vaadin8-setImmediate() rrcRequestType.setImmediate(true);
		rrcRequestType.setWidth("250px");
		rrcRequestType.setHeight("20px");
		rrcRequestType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rrcRequestType.setReadOnly(true);
		rrcRequestType.setEnabled(false);
		rrcRequestType.setNullRepresentation("");
		
		FormLayout hLayout = new FormLayout (rrcRequestNo);
		FormLayout hLayout1 = new FormLayout( rrcRequestType);
		//VerticalLayout vLayout = new VerticalLayout(hLayout,hLayout1);
		HorizontalLayout vLayout = new HorizontalLayout(hLayout, hLayout1);
		//vLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_RIGHT);
		VerticalLayout vMainLayout = new VerticalLayout();
		Button viewClaimWiseRRCHistory = new Button("View Claim Wise RRC History");
		viewClaimWiseRRCHistory.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popup = new com.vaadin.ui.Window();
				
				
				
				claimWiseRRCHistoryPage.init(bean,popup);
			//	viewClaimWiseRRCHistoryPage.initPresenter(SHAConstants.SEARCH_RRC_REQUEST);
				claimWiseRRCHistoryPage.getContent();
				
				popup.setCaption("View Claim Wise RRC History");
				popup.setWidth("75%");
				popup.setHeight("85%");
				popup.setContent(claimWiseRRCHistoryPage);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
			
		});
		
		Button viewRRCRequestHistory = new Button("View RRC Request History");
		viewRRCRequestHistory.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popup = new com.vaadin.ui.Window();
				
				
				
				viewRRCRequestHistoryPage.init(bean,popup,bean.getRrcRequestNo());
				viewRRCRequestHistoryPage.initPresenter(SHAConstants.VIEW_RRC_REQUEST);
				viewRRCRequestHistoryPage.getContent();
				
				popup.setCaption("View RRC History");
				popup.setWidth("75%");
				popup.setHeight("85%");
				popup.setContent(viewRRCRequestHistoryPage);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
			
		});
		
		Button viewClaimStatus = new Button("View Claim Status");
		viewClaimStatus.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popup = new com.vaadin.ui.Window();
				
				
				
				viewDetails.viewClaimStatusUpdated(bean.getNewIntimationDTO().getIntimationId());
			}
			//	viewClaimWiseRRCHistoryPage.initPresenter(SHAConstants.SEARCH_RRC_REQUEST);
				//viewRRCRequestHistoryPage.getContent();
				
				/*popup.setCaption("View Claim Status");
				popup.setWidth("75%");
				popup.setHeight("85%");
				popup.setContent(viewDetails);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					*//**
					 * 
					 *//*
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}*/
				});

				/*popup.setModal(true);
				UI.getCurrent().addWindow(popup);*/
			//}
		
		Button btnViewPackage = new Button("View Package");
		btnViewPackage.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(null != bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsPaayasPolicy()){
					pdfPageUI.init(bean.getNewIntimationDTO());
					if(pdfPageUI.isAttached()){
						pdfPageUI.detach();
					}
					UI.getCurrent().addWindow(pdfPageUI);
				}
				else if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto() != null && bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null){
					
					if(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode() != null){
						BPMClientContext bpmClientContext = new BPMClientContext();
						String url = bpmClientContext.getHospitalPackageDetails() + bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode();
						//getUI().getPage().open(url, "_blank");
						getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
					}
					
//				HospitalPackageRatesDto packageRatesDto = hospitalService.getHospitalPackageRates(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode());
//					if(packageRatesDto != null){
//						
//					ReportDto reportDto = new ReportDto();
//					reportDto.setClaimId(bean.getNewIntimationDTO().getIntimationId());
//					List<HospitalPackageRatesDto> beanList = new ArrayList<HospitalPackageRatesDto>();
//					beanList.add(packageRatesDto);
//					reportDto.setBeanList(beanList);
//					DocumentGenerator docGen = new DocumentGenerator();
//					String fileUrl = docGen.generatePdfDocument("HospitalPackageRates", reportDto);
//					openPdfFileInWindow(fileUrl);
//					}
//					else{
//						getErrorMessage("Package Not Available for the selected Hospital");
//					}
				}
				else{
					getErrorMessage("Package Not Available for the selected Hospital");
				}
			}
			
		});
			
			
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(viewClaimWiseRRCHistory,viewClaimStatus,viewRRCRequestHistory,btnViewPackage);
		horizontalLayout.setSpacing(true);
		
		vMainLayout.addComponent(horizontalLayout);
		vMainLayout.addComponent(vLayout);
		/*vMainLayout.addComponent(vLayout);
		vMainLayout.addComponent(viewClaimWiseRRCHistory);
		vMainLayout.addComponent(viewClaimStatus);
		vMainLayout.addComponent(viewRRCRequestHistory);
		//vMainLayout.addComponent(vLayout);
		vMainLayout.setComponentAlignment(vLayout, Alignment.MIDDLE_RIGHT);
		vMainLayout.setComponentAlignment(viewClaimWiseRRCHistory, Alignment.MIDDLE_RIGHT);
		vMainLayout.setComponentAlignment(viewClaimStatus, Alignment.MIDDLE_RIGHT);
		vMainLayout.setComponentAlignment(viewRRCRequestHistory, Alignment.MIDDLE_RIGHT);*/
		
		return vMainLayout;
	}
	

	private void openPdfFileInWindow(final String filepath) {
		
		Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("Hospital Package Rate");
		window.setWidth("800");
		window.setHeight("600");
		window.setModal(true);
		window.center();

		Path p = Paths.get(filepath);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(filepath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		window.setContent(e);
		UI.getCurrent().addWindow(window);
	 }
  
  public void getErrorMessage(String eMsg){

		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Alert");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
 }

	
	
	@PostConstruct
	public void initView() {
		addStyleName("view");
		
		setSizeFull();			
	}
	

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardSave(GWizardEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event) {
		if(modifyRRCRequestDataExtraction.onAdvance())
		{
			fireViewEvent(ModifyRRCRequestPresenter.SUBMIT_RRC_REQUEST_FOR_MODIFY, bean);
		}
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		// TODO Auto-generated method stub
		releaseHumanTask();
		fireViewEvent(MenuItemBean.MODIFY_RRC_REQUEST, null);
		clearObjects();
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void buildSuccessLayout(String rrcRequestNo) {
		String successMessage = "RRC RequestNo" + " " + rrcRequestNo + " Successfully submitted for processing !!!";
		Label successLabel = new Label(
				"<b style = 'color: green;'>" + successMessage + "</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.MODIFY_RRC_REQUEST, null);
				clearObjects();
				
			}
		});
		
		
	}

	@Override
	public void init(PreauthDTO bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}
	
	 private void releaseHumanTask(){
			
			Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
	     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
	 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
	 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

	 		if(existingTaskNumber != null){
	 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
	 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
	 		}
	 		
	 		if(wrkFlowKey != null){
	 			DBCalculationService dbService = new DBCalculationService();
	 			dbService.callUnlockProcedure(wrkFlowKey);
	 			getSession().setAttribute(SHAConstants.WK_KEY, null);
	 		}
		}
	 
	 @Override
	 public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
		 this.modifyRRCRequestDataExtraction.setsubCategoryValues(selectValueContainer, subCategory, value);
	 }
	 @Override
	 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
		 this.modifyRRCRequestDataExtraction.setsourceValues(selectValueContainer, source, value);
	 }
	 
	 public void clearObjects(){
		 SHAUtils.setClearRRCDTO(bean);
		 this.modifyRRCRequestDataExtraction.invalidate();
	 }
}
