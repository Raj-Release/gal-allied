//package com.shaic.claim.OMPprocessrejection.wizard.pages;
//
//import javax.annotation.PostConstruct;
//import javax.enterprise.inject.Instance;
//import javax.inject.Inject;
//
//import org.vaadin.addon.cdimvp.AbstractMVPView;
//import org.vaadin.dialogs.ConfirmDialog;
//import org.vaadin.teemu.wizards.event.GWizardEvent;
//import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
//import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
//import org.vaadin.teemu.wizards.event.WizardInitEvent;
//import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
//import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;
//
//import com.shaic.claim.ViewDetails;
//import com.shaic.claim.OMPclaimratechangeandOsUpdation.search.OMPClaimRateChangeAndOsUpdationPresenter;
//import com.shaic.claim.ViewDetails.ViewLevels;
//import com.shaic.claim.omp.carousel.OMPProcessRejectionRevisedCarousel;
//import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
//import com.shaic.claim.reimbursement.dto.RRCDTO;
//import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
//import com.shaic.claim.rod.wizard.pages.AcknowledgeDocReceivedWizardPresenter;
//import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
//import com.shaic.ims.carousel.RevisedCarousel;
//import com.shaic.main.navigator.domain.MenuItemBean;
//import com.shaic.newcode.wizard.IWizard;
//import com.shaic.newcode.wizard.IWizardPartialComplete;
//import com.vaadin.v7.data.fieldgroup.FieldGroup;
//import com.vaadin.v7.data.util.BeanItem;
//import com.vaadin.server.Sizeable.Unit;
//import com.vaadin.v7.shared.ui.label.ContentMode;
//import com.vaadin.ui.Alignment;
//import com.vaadin.ui.Button;
//import com.vaadin.v7.ui.ComboBox;
//import com.vaadin.ui.FormLayout;
//import com.vaadin.v7.ui.HorizontalLayout;
//import com.vaadin.v7.ui.Label;
//import com.vaadin.v7.ui.OptionGroup;
//import com.vaadin.ui.Panel;
//import com.vaadin.v7.ui.TextArea;
//import com.vaadin.v7.ui.TextField;
//import com.vaadin.ui.UI;
//import com.vaadin.v7.ui.VerticalLayout;
//import com.vaadin.ui.VerticalSplitPanel;
//import com.vaadin.ui.Window;
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.ui.Button.ClickListener;
//import com.vaadin.ui.Window.CloseEvent;
//import com.vaadin.v7.ui.themes.Reindeer;
//import com.vaadin.ui.themes.ValoTheme;
//
//public class OMPProcessRejectionWizardViewImpl extends AbstractMVPView implements OMPProcessRejectionWizardView{
//	
//	@Inject
//	private Instance<IWizard> iWizard;
//	
//	@Inject
//	private ViewDetails viewDetails;
//	
//	@Inject
//	private Instance<OMPProcessRejectionRevisedCarousel> commonCarouselInstance;
//	
//	private VerticalLayout mainPanel;
//	
//	private IWizardPartialComplete wizard;
//	
//	private FieldGroup binder;
//	private ReceiptOfDocumentsDTO bean;
//	private RRCDTO rrcDTO;	
//
//	private void initBinder() {
//		
//		wizard.getFinishButton().setCaption("Submit");;
//		this.binder = new FieldGroup();
//		/*
//		BeanItem<ReceiptOfDocumentsDTO> item = new BeanItem<ReceiptOfDocumentsDTO>(bean);
//		item.addNestedProperty("documentDetails");
//		item.addNestedProperty("documentDetails.documentsReceivedFrom");
//		item.addNestedProperty("documentDetails.acknowledgmentContactNumber");
//		item.addNestedProperty("documentDetails.documentsReceivedDate");
//		item.addNestedProperty("documentDetails.emailId");
//		item.addNestedProperty("documentDetails.modeOfReceipt");
//		item.addNestedProperty("documentDetails.reconsiderationRequest");
//		item.addNestedProperty("documentDetails.hospitalization");
//		item.addNestedProperty("documentDetails.preHospitalization");
//		item.addNestedProperty("documentDetails.postHospitalization");
//		item.addNestedProperty("documentDetails.partialHospitalization");
//		item.addNestedProperty("documentDetails.lumpSumAmount");
//		item.addNestedProperty("documentDetails.addOnBenefitsHospitalCash");
//		item.addNestedProperty("documentDetails.addOnBenefitsPatientCare");
//		item.addNestedProperty("documentDetails.hospitalizationClaimedAmount");
//		item.addNestedProperty("documentDetails.preHospitalizationClaimedAmount");
//		item.addNestedProperty("documentDetails.postHospitalizationClaimedAmount");
///*		item.addNestedProperty("documentDetails.documentCheckList");
//		item.addNestedProperty("documentDetails.documentCheckList");
//		item.addNestedProperty("documentDetails.documentCheckList");
//		item.addNestedProperty("documentDetails.documentCheckList");
//		item.addNestedProperty("documentDetails.documentCheckList");
//		item.addNestedProperty("documentDetails.documentCheckList");
//		item.addNestedProperty("documentDetails.documentCheckList");*/
////		this.binder.setItemDataSource(item);
//	//	*/
//	}
//	
//
//	@PostConstruct
//	public void initView() {
//		addStyleName("view");
//		
//		setSizeFull();			
//	}
//	
//	
//	public void initView(ReceiptOfDocumentsDTO rodDTO)
//	{
//		this.bean = rodDTO;
//		this.rrcDTO = rodDTO.getRrcDTO();
//		 
//		mainPanel = new VerticalLayout();
//		//this.wizard = iWizard.get();
//		this.wizard = new IWizardPartialComplete();
//		initBinder();	
//	//	documentDetailsView = documentDetailsViewImpl.get();
//	//	documentDetailsView.init(rodDTO,wizard);
//	//	acknowledgeReceiptViewImpl.init(rodDTO);
//		
//	//	preauthDTO = new PreauthDTO();
//	//	preauthDTO.setRrcDTO(rrcDTO);
//		//preauthDTO.setRodHumanTask(rrcDTO.getHumanTask());
//		
//	//	wizard.addStep(documentDetailsView,DOCUMENT_DETAILS);
//	//	wizard.addStep(this.acknowledgeReceiptViewImpl,"Acknowledgement Receipt");
//	//	wizard.setStyleName(ValoTheme.PANEL_WELL);
//	//	wizard.setSizeFull();
//	//	wizard.addListener(this);	
//		OMPProcessRejectionRevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Process Rejection ");
//		mainPanel.addComponent(intimationDetailsCarousel);
//		viewDetails.initView(bean.getClaimDTO().getNewIntimationDto().getIntimationId(),0l, ViewLevels.PREAUTH_MEDICAL,"Process Rejection");
//		VerticalLayout vLayout = new VerticalLayout(commonButtonsLayout(),viewDetails);
//		vLayout.setWidth("100%");
//		vLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
//		VerticalLayout wizardLayout1 = new VerticalLayout(vLayout);
//		
//		Panel panel1 = new Panel();
//		panel1.setContent(wizardLayout1);
//		//panel1.setHeight("90px");
//		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
//		wizardLayout2.setSpacing(true);
//		mainPanel.addComponent(wizardLayout2);
//		addFooterButtons();
//		mainPanel.setSizeFull();
//	//	mainPanel.setSplitPosition(30, Unit.PERCENTAGE);
//		mainPanel.setHeight("600px");
//		
//		setCompositionRoot(mainPanel);			
//		}
//	
//	private VerticalLayout commonButtonsLayout()
//	{
//		OptionGroup  optionGroup = new OptionGroup ();
//		optionGroup.setNullSelectionAllowed(true);
//		optionGroup.addItems("Cashless","Reimbursement");
//		optionGroup.addStyleName("horizontal");
//						
//		OptionGroup  optionGroup1 = new OptionGroup ();
//		optionGroup1.setNullSelectionAllowed(true);
//		optionGroup1.addItems("Hospitalisation","Non Hospitalisation");
//		optionGroup1.addStyleName("horizontal");
//		
//		
//		TextArea suggestedRejectionRemarks = new TextArea("Suggested Rejection Remarks");
//	//	FormLayout hLayout = new FormLayout (suggestedRejectionRemarks);
//	//	FormLayout hLayout1 = new FormLayout(optionGroup,optionGroup1);
//		
//		TextField intialProvision = new TextField("Initial Provision Amt($)");
//		TextField inrConversionRate = new TextField("INR Conversion Rate*");
//		TextField InrValue = new TextField("INR Value");
//		ComboBox  eventCode = new ComboBox("Event Code");
//		TextField hospitalName = new TextField("Hospital Name");
//		TextField hospitalCity = new TextField("Hospital City");
//		TextField hospitalCounty = new TextField("Hospital County");
//		TextField ailmentLoss = new TextField("Ailment/Loss");
//		
//		
//		FormLayout formleft = new FormLayout(optionGroup,intialProvision,inrConversionRate,InrValue,eventCode);
//		FormLayout formright = new FormLayout(suggestedRejectionRemarks,optionGroup1,hospitalName,hospitalCity,hospitalCounty,ailmentLoss);
//		
////		HorizontalLayout optionHLayout = new HorizontalLayout(optionGroup, optionGroup1);
//	//	optionHLayout.setSpacing(true);
//		HorizontalLayout hLayout1 = new HorizontalLayout(formleft,formright);
//		
////		VerticalLayout vLayout = new VerticalLayout(optionHLayout,hLayout1);
//		//VerticalLayout vLayout1 = new VerticalLayout(suggestedRejectionRemarks,optionGroup1,hLayout3);
////		VerticalLayout mvLayout = new VerticalLayout(optionHLayout ,  vLayout);	
//	//	HorizontalLayout vHLayout = new HorizontalLayout(vLayout,vLayout1);
//		
//		Button btnConfirmRejection = new Button("Confirm Rejection");
//		btnConfirmRejection.addClickListener(new Button.ClickListener() {
//			
//			TextArea txtAsuggsted = new TextArea("Suggested Rejection Remarks *");
//		
//		      public void buttonClick(ClickEvent event) {
//		    	  mainPanel.addComponent(txtAsuggsted);
//		    	  
//		    	  mainPanel.addComponent(new TextArea("Confirm Rejection  Remarks*"));
//		    	  
//		      }
//		   });
//		Button btnWaiveRejection = new Button("Waive Rejection Remarks ");
//		btnWaiveRejection.addClickListener(new Button.ClickListener() {
//		      public void buttonClick(ClickEvent event) {
//		    	  mainPanel.addComponent(new TextArea("Waive Rejection Remarks *"));
//		    	  
//		    	  	    	  
//		      }
//		   });
//		
//		FormLayout buttonhLayout4 = new FormLayout(btnConfirmRejection,btnWaiveRejection);
//		
//		VerticalLayout secondvLayout = new VerticalLayout(hLayout1,buttonhLayout4);
////		HorizontalLayout alignmentHLayout = new HorizontalLayout(secondvLayout);
//		
//				
//			return secondvLayout;
//			
//		}
//
//	public void addFooterButtons(){
//		
//		HorizontalLayout buttonsLayout = new HorizontalLayout();
//			
//		Button	closeButton = new Button("Submit");
//			closeButton.addClickListener(new Button.ClickListener() {
//				
//				@Override
//				public void buttonClick(ClickEvent event) {
//					
//					fireViewEvent(OMPProcessRejectionWizardViewPresenter.CLEAR_SEARCH_FORM,null);
//					fireViewEvent(MenuItemBean.OMP_PROCESS_REJECTION,null);				
//				}
//			});
//		Button	clearButton = new Button("Clear");
//			clearButton.addClickListener(new Button.ClickListener() {
//				
//				@Override
//				public void buttonClick(ClickEvent event) {
//					fireViewEvent(OMPProcessRejectionWizardViewPresenter.RESET_SEARCH_FROM,null);
//					
//				}
//			});
//			
//			buttonsLayout.addComponents(closeButton,clearButton);
//			buttonsLayout.setSpacing(true);
//			VerticalLayout	btnLayout = new VerticalLayout(buttonsLayout);
//			btnLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
//			
//			mainPanel.addComponent(btnLayout);
//			
//		}
//	
//	
//	@Override
//	public void buildSuccessLayout() {
//		Label successLabel = new Label("<b style = 'color: green;'> Claim record saved successfully !!! </b>", ContentMode.HTML);
//		
////		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
//		
//		Button homeButton = new Button("OK");
//		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
//		horizontalLayout.setMargin(true);
//		
//		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
//		layout.setSpacing(true);
//		layout.setMargin(true);
//		HorizontalLayout hLayout = new HorizontalLayout(layout);
//		hLayout.setMargin(true);
//		hLayout.setStyleName("borderLayout");
//		
//		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("");
//		dialog.setClosable(false);
//		dialog.setContent(hLayout);
//		dialog.setResizable(false);
//		dialog.setModal(true);
//		dialog.show(getUI().getCurrent(), null, true);
//		
//		homeButton.addClickListener(new ClickListener() {
//			private static final long serialVersionUID = 7396240433865727954L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				dialog.close();
//
//				fireViewEvent(MenuItemBean.OMP_PROCESS_REJECTION, null);
//				
//			}
//		});
//	}
//	@Override
//	public void resetView() {
//		
//		/*if(null != this.wizard && !this.wizard.getSteps().isEmpty())
//		{
//			this.wizard.clearWizardMap(DOCUMENT_DETAILS);
//			this.wizard.clearWizardMap("Acknowledgement Receipt");
//		//	this.wizard.clearWizardMap(DOCUMENT_DETAILS);
//			this.wizard.clearCurrentStep();
//		}*/
//	}
//	
//
//	@Override
//	public void wizardCompleted(WizardCompletedEvent event) {
//		// TODO Auto-generated method stub
//	//	fireViewEvent(AcknowledgeDocReceivedWizardPresenter.SUBMIT_ACKNOWLEDGE_DOC_RECEIVED, this.bean);
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
//	
//	@Override
//	public void wizardCancelled(WizardCancelledEvent event) {
//		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
//		        "No", "yes", new ConfirmDialog.Listener() {
//			
//		            public void onClose(ConfirmDialog dialog) {
//		                if (!dialog.isConfirmed()) {
//		                    
//		                	fireViewEvent(MenuItemBean.OMP_PROCESS_REJECTION, null);
//		                } else {
//		                    // User did not confirm
//		                }
//		            }
//		        });
//		
//		dialog.setClosable(false);
//		dialog.setStyleName(Reindeer.WINDOW_BLACK);
//		
//	}
//	
//	@Override
//	public void initData(WizardInitEvent event) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	@Override
//	public void wizardSave(GWizardEvent event) {
////		fireViewEvent(AcknowledgeDocReceivedWizardPresenter.SUBMIT_ACKNOWLEDGE_DOC_RECEIVED, this.bean);
//
//		
//	}
//
//	
//}
