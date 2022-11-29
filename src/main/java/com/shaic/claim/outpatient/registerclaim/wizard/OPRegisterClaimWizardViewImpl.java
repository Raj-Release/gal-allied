package com.shaic.claim.outpatient.registerclaim.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.ReportDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.outpatient.OutpatientRegiserClaimCarousel;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.main.navigator.ui.Toolbar;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class OPRegisterClaimWizardViewImpl extends AbstractMVPView implements OPRegisterClaimWizard {
	private static final long serialVersionUID = -8044167858989544230L;

//	@Inject
//	private Instance<IWizard> iWizard;
//	
//	private IWizardPartialComplete wizard;
	
	private FieldGroup binder;
	
	private OutPatientDTO bean;

//	private VerticalSplitPanel mainPanel;
	
	private VerticalLayout mainLayout;
	
//	@Inject
//	private Instance<ClaimAndDocumentDetailsPageViewImpl> claimAndDocumentDetailsPageViewImpl;
//	
//	@Inject
//	private Instance<OPRODAndBillEntryPageViewImpl> opRODAndBillEntryPageViewImpl;
//	
//	private ClaimAndDocumentDetailsPageViewImpl claimAndDocumentDetailsPageViewImplObj;
//	
//	private OPRODAndBillEntryPageViewImpl opRODAndBillEntryPageViewImplObj;
	
	@Inject
	private Instance<OutpatientRegiserClaimCarousel> commonCarouselInstance;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private OPRegisterClaimView registerClaimPageObj; 
	
	@Inject
	private Toolbar toolBar;
	
	@Override
	public void resetView() {
//		if(null != this.wizard && !this.wizard.getSteps().isEmpty())
//		{
//			this.wizard.clearWizardMap("Claim And Document Details");
//			this.wizard.clearWizardMap("Create ROD, Upload Docs & Bill Entry");
//			this.wizard.clearCurrentStep();
//		}
//		if(null != opRODAndBillEntryPageViewImplObj)
//		{
//			opRODAndBillEntryPageViewImplObj.resetView();
//		}
		
	}
	
	public void initView(OutPatientDTO bean) {
		this.bean = bean;
		//mainPanel = new VerticalSplitPanel();
		mainLayout = new VerticalLayout();
//		this.wizard = new IWizardPartialComplete();
//		initBinder();
//		claimAndDocumentDetailsPageViewImplObj = claimAndDocumentDetailsPageViewImpl.get();
//		claimAndDocumentDetailsPageViewImplObj.init(this.bean , wizard);
//		wizard.addStep(claimAndDocumentDetailsPageViewImplObj,"Claim And Document Details");
//		
//		opRODAndBillEntryPageViewImplObj = opRODAndBillEntryPageViewImpl.get();
//		opRODAndBillEntryPageViewImplObj.init(this.bean, wizard);
//		wizard.addStep(opRODAndBillEntryPageViewImplObj,"Create ROD, Upload Docs & Bill Entry");
		
//		wizard.setStyleName(ValoTheme.PANEL_WELL);
//		wizard.setSizeFull();
//		wizard.addListener(this);
		
		OutpatientRegiserClaimCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.bean.getPolicy(), bean.getPioName());
//		mainPanel.setFirstComponent(intimationDetailsCarousel);
		mainLayout.addComponent(intimationDetailsCarousel);
		
		HorizontalLayout commonButtonsLayout = commonButtonsLayout();
		
		VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout);
		wizardLayout1.setComponentAlignment(commonButtonsLayout, Alignment.TOP_RIGHT);
		wizardLayout1.setSpacing(true);
		
//		Panel panel1 = new Panel();
//		panel1.setContent(wizardLayout1);
//		panel1.setHeight("60px");
//		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
//		wizardLayout2.setSpacing(true);
		
//		mainPanel.setSecondComponent(wizardLayout2);
//		mainPanel.setSizeFull();
//		mainPanel.setSplitPosition(18, Unit.PERCENTAGE);
//		mainPanel.setHeight("700px");
//		mainPanel.setStyleName("policyinfogrid");
		
		mainLayout.addComponent(wizardLayout1);
		registerClaimPageObj.init(bean);
		registerClaimPageObj.setViewDetails(viewDetails);
		mainLayout.addComponent(registerClaimPageObj);
		setCompositionRoot(mainLayout);
	}
	
	private HorizontalLayout commonButtonsLayout() {
		FormLayout viewDetailsForm = new FormLayout();
		Boolean oPflag;
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
//		viewDetails.initView(bean.getPolicy().getKey(),
//				ViewLevels.OUPATIENT,true);
		oPflag=true;
		if(bean.getHealthCheckupFlag()){
			oPflag=false;
		}
		
//		claimAndDocumentDetailsPageViewImplObj.setInsuredKey();
		
		viewDetails.initViewForRegisterClaim(bean,bean.getPolicy().getPolicyNumber(),bean.getPolicy().getKey(),bean.getPolicyDto().getHealthCardNumber(), ViewLevels.OUPATIENT,true,null,oPflag); //claimAndDocumentDetailsPageViewImplObj
		
		viewDetailsForm.addComponent(viewDetails);
		HorizontalLayout horizontalLayout1 = new HorizontalLayout(viewDetailsForm);
//		horizontalLayout1.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		horizontalLayout1.setWidth("100%");
		horizontalLayout1.setSizeUndefined();
		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
		horizontalLayout1.setSpacing(true);
		horizontalLayout1.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		return horizontalLayout1;
	}

	private void initBinder() {
//		wizard.getFinishButton().setCaption("Submit");
//		this.binder = new FieldGroup();
//		BeanItem<OutPatientDTO> item = new BeanItem<OutPatientDTO>(bean);
//		item.addNestedProperty("documentDetails");
//		item.addNestedProperty("opBillEntryDetails");
//	
//		item.addNestedProperty("documentDetails.claimType");
//		item.addNestedProperty("documentDetails.insuredPatientName");
//		item.addNestedProperty("documentDetails.amountClaimed");
//		item.addNestedProperty("documentDetails.provisionAmt");
//		item.addNestedProperty("documentDetails.documentReceivedFrom");
//		item.addNestedProperty("documentDetails.documentReceivedDate");
//		item.addNestedProperty("documentDetails.modeOfReceipt");
//		item.addNestedProperty("documentDetails.acknowledgementContactNumber");
//		item.addNestedProperty("documentDetails.emailID");
//		item.addNestedProperty("documentDetails.additionalRemarks");
//		item.addNestedProperty("documentDetails.paymentModeFlag");
//		item.addNestedProperty("documentDetails.payeeName");
//		item.addNestedProperty("documentDetails.payeeEmailId");
//		item.addNestedProperty("documentDetails.panNo");
//		item.addNestedProperty("documentDetails.approvalRemarks");
//		item.addNestedProperty("documentDetails.rejectionRemarks");
//		item.addNestedProperty("documentDetails.payableAt");
//		item.addNestedProperty("documentDetails.accountNo");
//		item.addNestedProperty("documentDetails.ifscCode");
//		item.addNestedProperty("documentDetails.branch");
//		item.addNestedProperty("documentDetails.bankName");
//		item.addNestedProperty("documentDetails.city");
//		this.binder.setItemDataSource(item);	
	}

//	@Override
//	public void wizardSave(GWizardEvent event) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void activeStepChanged(WizardStepActivationEvent event) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void stepSetChanged(WizardStepSetChangedEvent event) {
//		// TODO Auto-generated method stub
//		
//	}

//	@Override
//	public void wizardCompleted(WizardCompletedEvent event) {
//		fireViewEvent(OPRegisterClaimWizardPresenter.OP_SUBMITTED_EVENT, this.bean);
//	}
//
//	@Override
//	public void wizardCancelled(WizardCancelledEvent event) {
//		ConfirmDialog dialog = ConfirmDialog
//				.show(getUI(),
//						"Confirmation",
//						"Are you sure you want to cancel ?",
//						"No", "Yes", new ConfirmDialog.Listener() {
//
//							public void onClose(ConfirmDialog dialog) {
//								if (!dialog.isConfirmed()) {
//									// Confirmed to continue
//									fireViewEvent(MenuItemBean.REGISTER_CLAIM_OP,
//											null);
//								} else {
//									// User did not confirm
//								}
//							}
//						});
//
//		dialog.setClosable(false);
//		dialog.setStyleName(Reindeer.WINDOW_BLACK);
//	}
//
//	@Override
//	public void initData(WizardInitEvent event) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Register Claim processed successfully !!!</b>",
				ContentMode.HTML);
		
		Label intimationLabel = new Label(
				"<b style = 'color: green;'> Intimation No :" + bean.getIntimationId()+"</b>",
				ContentMode.HTML);
		
		Label claimLabel = new Label(
				/*"<b style = 'color: green;'> Claim No :" + bean.getClaimId()+" Registered Successfully!!!</b>",*/
				"<b style = 'color: blue;'> Claim No: " + bean.getClaimId()+" Registered Successfully!!!</b>",
				ContentMode.HTML);


		/*HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());	*/
		
		Button homeButton = new Button("Home Page");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		Button processClaim = new Button("Process Claim");
		processClaim.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		Button acknowledgeLetterBtn = new Button("View Acknowledgement Letter");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,processClaim); //acknowledgeLetterBtn,
		horizontalLayout.setSpacing(true);
		
		VerticalLayout layout = new VerticalLayout(claimLabel, horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
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
				toolBar.opcountTool();
				fireViewEvent(MenuItemBean.REGISTER_CLAIM_OP, null);

			}
		});
		
		processClaim.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				toolBar.opcountTool();
//				fireViewEvent(MenuItemBean.PORCESS_CLAIM_OP, null);
				fireViewEvent(MenuPresenter.SHOW_DIRECT_PROCESS_OP_CLAIM, bean);

			}
		});
		
		acknowledgeLetterBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@SuppressWarnings("static-access")
			@Override
			public void buttonClick(ClickEvent event) {
				DocumentGenerator docGen = new DocumentGenerator();
				ReportDto reportDto = new ReportDto();
				
				
				List<OutPatientDTO> rodDTOList = new ArrayList<OutPatientDTO>();
				rodDTOList.add(bean);		
				reportDto.setClaimId(bean.getClaimId());
				reportDto.setBeanList(rodDTOList);
				final String filePath = docGen.generatePdfDocument("OPAckReceipt", reportDto);	
				Path p = Paths.get(filePath);
				
				String fileName = p.getFileName().toString();

				StreamResource.StreamSource s = new StreamResource.StreamSource() {

					public FileInputStream getStream() {
						try {

							File f = new File(filePath);
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
				//e.setSizeFull();
				e.setWidth("100%");
				e.setHeight("100%");
				
				e.setType(Embedded.TYPE_BROWSER);
				r.setMIMEType("application/pdf");
				e.setSource(r);
				
				com.vaadin.ui.Panel vPanel = new com.vaadin.ui.Panel();
				vPanel.setHeight("100%");
				vPanel.setContent(e);
				VerticalLayout vLayout = new VerticalLayout();
				vLayout.setWidth("100%");
				vLayout.setHeight("400px");
				//vLayout.setHeight("100%");
				vLayout.addComponent(vPanel);
				/*horizontalLayout = new HorizontalLayout(e);
				horizontalLayout.setWidth("100%");
				horizontalLayout.setHeight("100%");*/
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(true);
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);

			}
		});
		
	}

	@Override
	public void cancelIntimation() {
		registerClaimPageObj.cancelIntimation();
	}
	
	public void setValueToBalanceSumInsured(Integer opBalanceSumInsured){
		registerClaimPageObj.setValeBalanceSumInsured(opBalanceSumInsured);
	}
}