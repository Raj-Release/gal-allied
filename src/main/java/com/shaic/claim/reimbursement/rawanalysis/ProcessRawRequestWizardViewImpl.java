package com.shaic.claim.reimbursement.rawanalysis;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableBancs;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPaayasPolicyDetailsPdfPage;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ZUATopViewQueryTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ProcessRawRequestWizardViewImpl extends AbstractMVPView implements ProcessRawRequestWizard {
	
	@Inject
	private Instance<ProcessRawRequestPage> processRawRequestPageInst;
	
	private ProcessRawRequestPage processRawRequestPage;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private ZUAViewQueryHistoryTable zuaViewQueryHistoryTable;
	
	@Inject
	private ZUATopViewQueryTable zuaTopViewQueryTable;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	@Inject
	private ViewPaayasPolicyDetailsPdfPage pdfPageUI;
	
	@EJB
	private PolicyService policyService;
	
	private SearchProcessRawRequestTableDto bean;
	
	@Inject
	private ZUAViewQueryHistoryTableBancs zuaTopViewQueryTableBancs;
	
	@Override
	public void init(SearchProcessRawRequestTableDto searchDTO){
		this.bean = searchDTO;
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(searchDTO.getIntimationDto(),searchDTO.getClaimDto(),"");

		processRawRequestPage = processRawRequestPageInst.get();
		processRawRequestPage.init(searchDTO);
		
		VerticalLayout mainVertical = new VerticalLayout(intimationDetailsCarousel,commonButtonLayout(),processRawRequestPage);
		mainVertical.setComponentAlignment(processRawRequestPage, Alignment.BOTTOM_CENTER);
		mainVertical.setSpacing(true);
		
		setCompositionRoot(mainVertical);
		
	}

	public VerticalLayout commonButtonLayout(){
		
		TextField txtClaimCount = new TextField("Claim Count");
		txtClaimCount.setValue(bean.getPreauthDTO().getClaimCount().toString());
		txtClaimCount.setReadOnly(true);
		TextField dummyField = new TextField();
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		FormLayout firstForm = new FormLayout(txtClaimCount,dummyField);
		dummyField.setReadOnly(true);
//		firstForm.setWidth(txtClaimCount.getWidth(), txtClaimCount.getWidthUnits());
		Panel claimCount = new Panel(firstForm);
		claimCount.setWidth("130px");
		txtClaimCount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.setHeight("30px");
		firstForm.setMargin(false);
		firstForm.setComponentAlignment(txtClaimCount, Alignment.TOP_LEFT);
//		txtClaimCount.addStyleName("fail");
//		claimCount.setWidth(txtClaimCount.getWidth(),txtClaimCount.getWidthUnits());
//		
		if(bean.getPreauthDTO().getClaimCount() > 1 && bean.getPreauthDTO().getClaimCount() <=2){
			claimCount.addStyleName("girdBorder1");
		}else if(bean.getPreauthDTO().getClaimCount() >2){
			claimCount.addStyleName("girdBorder2");
		}
		
		Button btnViewPolicySchedule = new Button("View Policy Schedule");
		btnViewPolicySchedule.setStyleName(ValoTheme.BUTTON_DANGER);
		btnViewPolicySchedule.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getViewPolicySchedule(bean.getIntimationDto().getIntimationId());
//				bean.setIsScheduleClicked(true);
				//New code addition for CR2019023
				if (bean.getPreauthDTO() != null){
					bean.getPreauthDTO().setIsScheduleClicked(true);
				}
				Button button = (Button)event.getSource();
				button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			}
		});
		
		
		Button btnZUAAlert = new Button("View ZUA History");
		btnZUAAlert.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				List<ZUAViewQueryHistoryTableDTO> setViewTopZUAQueryHistoryTableValues = SHAUtils.setViewTopZUAQueryHistoryTableValues(
						bean.getIntimationDto().getPolicy().getPolicyNumber(),policyService);
				

				List<ZUAViewQueryHistoryTableDTO> setViewZUAQueryHistoryTableValues = SHAUtils.setViewZUAQueryHistoryTableValues(
						bean.getIntimationDto().getPolicy().getPolicyNumber(),policyService);
				
				
				if(null != setViewZUAQueryHistoryTableValues && !setViewZUAQueryHistoryTableValues.isEmpty())
				{
					
				Policy policyObj = null;
				VerticalLayout verticalLayout = null;
				if (bean.getIntimationDto().getPolicy().getPolicyNumber() != null) {
					policyObj = policyService.getByPolicyNumber(bean.getIntimationDto().getPolicy().getPolicyNumber());
					if (policyObj != null) {
						if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
							zuaTopViewQueryTableBancs.init(" ",false,false);
							zuaTopViewQueryTableBancs.setTableList(setViewZUAQueryHistoryTableValues);//pending
							VerticalLayout verticalZUALayout = new VerticalLayout();
							
							verticalZUALayout.addComponent(zuaTopViewQueryTableBancs);
							
							verticalLayout = new VerticalLayout();
					    	verticalLayout.addComponents(verticalZUALayout);
						}
						else{
							zuaTopViewQueryTable.init(" ",false,false);
							zuaTopViewQueryTable.initTable();						
							
							zuaViewQueryHistoryTable.init(" ", false, false); 
							
							
							zuaViewQueryHistoryTable.setTableList(setViewZUAQueryHistoryTableValues);
							zuaTopViewQueryTable.setTableList(setViewTopZUAQueryHistoryTableValues);
							VerticalLayout verticalTopZUALayout = new VerticalLayout();
							VerticalLayout verticalZUALayout = new VerticalLayout();
							
							verticalTopZUALayout.addComponent(zuaTopViewQueryTable);
							verticalZUALayout.addComponent(zuaViewQueryHistoryTable);
							
							verticalLayout = new VerticalLayout();
					    	verticalLayout.addComponents(verticalTopZUALayout,verticalZUALayout);
					    	verticalLayout.setComponentAlignment(verticalTopZUALayout,Alignment.TOP_CENTER );  
						}
					}
				}
			
				
				Window popup = new com.vaadin.ui.Window();
				popup.setWidth("70%");
				popup.setHeight("70%");
				popup.setContent(verticalLayout);
				popup.setCaption("ZUA History");
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.setDraggable(true);
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
				else
				{
					getErrorMessage("ZUA History is not available");
				}
			}
		});
		
		Button elearnBtn = viewDetails.getElearnButton();
		
		HorizontalLayout forLayout = SHAUtils.newImageCRM(bean.getPreauthDTO());
		
		HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(bean.getPreauthDTO());
		HorizontalLayout crmLayout = new HorizontalLayout(forLayout,hopitalFlag);
		crmLayout.setSpacing(true);
		
		Button btnViewPackage = new Button("View Package");
		btnViewPackage.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(null != bean.getIntimationDto().getIsPaayasPolicy() && bean.getIntimationDto().getIsPaayasPolicy()){
					pdfPageUI.init(bean.getIntimationDto());
					if(pdfPageUI.isAttached()){
						pdfPageUI.detach();
					}
					UI.getCurrent().addWindow(pdfPageUI);
				}
				else if(bean.getIntimationDto() != null && bean.getIntimationDto().getHospitalDto() != null && bean.getIntimationDto().getHospitalDto().getRegistedHospitals() != null){

				if(bean.getIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalCode() != null){
					BPMClientContext bpmClientContext = new BPMClientContext();
					String url = bpmClientContext.getHospitalPackageDetails() + bean.getIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalCode();
					//getUI().getPage().open(url, "_blank");
					getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
				}


				}
				else{
					getErrorMessage("Package Not Available for the selected Hospital");
				}
			}
			
		});
		
		
		Button btnViewDoctorRemarks = new Button("Doctor Remarks");
		btnViewDoctorRemarks.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getDoctorRemarks(bean.getIntimationDto().getIntimationId());
			}
			
		});
		
		//Additional button for displaying Hospital scoring - Serious Deficiency
		
				Button btnViewSeriousDeficiency  = new Button("Serious Deficiency");
				btnViewSeriousDeficiency.addClickListener(new ClickListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
//						String hospitalCode = bean.getHospitalCode();
//						viewDetails.getDoctorRemarks(bean.getIntimationDto().getIntimationId());
						viewDetails.getSeriousDeficiencyByHospitalCode(bean.getHospitalCode());

					}
					
				});
				
		HorizontalLayout Layout1 = null;
		
			Layout1 = new HorizontalLayout(elearnBtn,btnViewPolicySchedule,btnViewDoctorRemarks,btnZUAAlert,btnViewPackage,btnViewSeriousDeficiency);
		
			Layout1.setSpacing(true);
		HorizontalLayout crmLayout1 = new HorizontalLayout(firstForm,crmLayout);
		firstForm.setMargin(false);
		crmLayout1.setWidth("40%");
		crmLayout1.setMargin(false);
		viewDetails.initView(bean.getIntimationDto().getIntimationId(),bean.getKey(), ViewLevels.PREAUTH_MEDICAL,"Process RAW");
		
		HorizontalLayout componentsHLayout = new HorizontalLayout(crmLayout1, viewDetails);
		componentsHLayout.setSpacing(true);
		componentsHLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		componentsHLayout.setWidth("100%");
		
		VerticalLayout vLayout = new VerticalLayout(componentsHLayout,Layout1);
		vLayout.setSpacing(true);
		
		vLayout.setWidth("100%");
		
		return vLayout;
	}
	
	 public void getErrorMessage(String eMsg){
			
			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
		}
	 
	@Override
	public void resetView() {
		
	}

	@Override
	public void setTableValues(List<RawInitiatedRequestDTO> intiatedDtls) {
		processRawRequestPage.setTableValues(intiatedDtls);
	}

	@Override
	public void setRepliedRawTableValues(
			List<RawInitiatedRequestDTO> repliedDtls) {
		processRawRequestPage.setRepliedRawTableValues(repliedDtls);
	}

	@Override
	public void setResolutionData(BeanItemContainer<SelectValue> resolutionRaws) {
		processRawRequestPage.setResolutionRaw(resolutionRaws);
	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'>Record updated successfully.</b>",
				ContentMode.HTML);

		Button homeButton = new Button("Home Page");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

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
				fireViewEvent(MenuItemBean.PROCESS_RAW_REQUEST, null);

			}
		});
	}

}
