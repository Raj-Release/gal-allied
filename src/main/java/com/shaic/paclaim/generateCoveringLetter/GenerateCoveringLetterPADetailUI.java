package com.shaic.paclaim.generateCoveringLetter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.carousel.PARevisedCarousel;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.StreamResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class GenerateCoveringLetterPADetailUI extends ViewComponent  {

		/**
	 * 
	 */
	private static final long serialVersionUID = -5178558368608211098L;

		@Inject
		private ViewDetails viewDetails;
		
		@EJB
		private PolicyService policyService;

		private Panel mainPanel = new Panel();

		private VerticalLayout mainLayout;

		private FormLayout dynamicFrmLayout;
		
		private ComboBox currencyNameSelect;

		private TextArea registrationRemarksTxta;

		private TextArea suggestRejectionTxta;

		private TextField claimedAmtTxt;

		private VerticalLayout dynamicFieldsLayout;

		private TextField provisionalAmt;
		
		private OptionGroup incedentOption;
		
		private PopupDateField dateOfAccidentDeath;
		
		private DateField dateOfAccident;
		
		private DateField dateOfDeath;
		
		
		private TextArea injuryDetailsTxt;

		private Button generatePdfBtn;

		private Button homePageButton;

		private GenerateCoveringLetterSearchTableDto coveringLetterBean;

		private Claim claim = null;

		private ClaimDto claimDto = new ClaimDto();

		private NewIntimationDto newIntimationDto = new NewIntimationDto();
		
		private List<SelectValue> docContainerList = new ArrayList<SelectValue>();
		
		BeanItemContainer<SelectValue> paDocContainter = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		private List<DocumentCheckListDTO> docTableListDto;

		private BeanItemContainer<SelectValue> currencyMasterContainer;

		@Inject
		private Instance<PARevisedCarousel> carouselInstance;

		private PARevisedCarousel paIntimationDetailCarousel;

		@EJB
		private MasterService masterService;

		private HorizontalLayout registerBtnLayout;
		
		@Inject
		private PACoveringLetterDocTable paCoveringLetterDocTableObject;
		
		private GWizard wizard;
		
		private TextArea coveringLetterRemarksTxta;
		
		@PostConstruct
		public void init() {

		}
		
		public void init(GenerateCoveringLetterSearchTableDto bean, GWizard wizard) {
			this.coveringLetterBean = bean;
			claimDto = bean.getClaimDto();
			claimDto.setRegistrationRemarks("");
			coveringLetterBean.getClaimDto().setRegistrationRemarks("");
			newIntimationDto = bean.getClaimDto().getNewIntimationDto(); 
			claimDto.setNewIntimationDto(newIntimationDto);
			this.docTableListDto = bean.getPaDocChecklist();
			this.docContainerList = bean.getPaDocContainerList();
			paDocContainter.addAll(docContainerList);
			paCoveringLetterDocTableObject.init("", true,true); 
			
			Map<String,Object> referenceData = new HashMap<String, Object>();
			
			referenceData.put("particulars", paDocContainter);			
			
			paCoveringLetterDocTableObject.setReference(referenceData);
			paCoveringLetterDocTableObject.setTableList(this.docTableListDto);
			
			this.wizard = wizard;
			wizard.getNextButton().setEnabled(false);			
		}		

		public Panel getWoleLayout(){
			Panel buildMainLayout = buildMainLayout();
			return buildMainLayout ;
		}
		
		private Panel buildMainLayout() {
			mainLayout = new VerticalLayout();
			mainLayout.setMargin(true);
			
			VerticalLayout registrationLayout = buildRegDetailsLayout();
			mainLayout.addComponent(registrationLayout);
			
			mainLayout.setComponentAlignment(registrationLayout, Alignment.TOP_RIGHT);
			mainLayout.addComponent(buildBasicInfoLayout(claimDto));
			mainLayout.addComponent(paCoveringLetterDocTableObject);
			
			coveringLetterRemarksTxta = new TextArea("Covering Letter Remarks");
			coveringLetterRemarksTxta.setWidth("590px");
			coveringLetterRemarksTxta.setValue(claimDto.getRegistrationRemarks());
			mainLayout.addComponent(coveringLetterRemarksTxta);

			mainPanel.setWidth("100%");
			mainPanel.setHeight("620px");
			mainPanel.setContent(mainLayout);

			return mainPanel;
		}

		private VerticalLayout buildRegDetailsLayout() {
			// common part: create layout
			
			VerticalLayout vlayout = new VerticalLayout();
			viewDetails.initView(newIntimationDto.getIntimationId(), ViewLevels.INTIMATION, false,"Generate covering letter");
			vlayout.setStyleName("policygridinfo");
			vlayout.setWidth("100%");

			return vlayout;
		}

		public void setCurrencyMaster(BeanItemContainer<SelectValue> currencyMaster) {
			currencyMasterContainer = currencyMaster;
		}

		private Boolean validateClaimdeAmount() {
			boolean hasError = false;
			
			if (claimedAmtTxt != null
					&& (claimedAmtTxt.getValue() == null
							|| claimedAmtTxt.getValue() == "" || !(Integer
							.valueOf(claimedAmtTxt.getValue()) > 0))) {
				claimedAmtTxt.setValidationVisible(true);
				hasError = true;
			}
			return hasError;
		}

		private HorizontalLayout buildBasicInfoLayout(ClaimDto claimdto) {
			String value = "";
			if (claimdto != null) {

				if (claimdto.getCurrencyId() != null) {
					value = claimdto.getCurrencyId().getValue();
				}

			}

			HorizontalLayout claimDetailsLayout = new HorizontalLayout();
			FormLayout formFLayout1 = new FormLayout();
			
			currencyNameSelect = new ComboBox();
			currencyNameSelect.setCaption("Currency Name");
			currencyNameSelect.setWidth("70px");
			currencyNameSelect.setHeight("-1px");
			currencyNameSelect.addItem(value);
			currencyNameSelect.setNullSelectionAllowed(false);
			formFLayout1.addComponent(currencyNameSelect);
			currencyNameSelect.setValue(currencyNameSelect.getContainerDataSource()
					.getItemIds().toArray()[0]);
			currencyNameSelect.setReadOnly(true);

			claimedAmtTxt = new TextField();
			claimedAmtTxt.setCaption("Amount Claimed " + value);
			claimedAmtTxt.setWidth("70px");
			claimedAmtTxt.setHeight("-1px");
			if (claimdto != null) {
				claimedAmtTxt.setValue(claimdto.getClaimedAmount() != null ? claimdto
						.getClaimedAmount().toString() : "");
			} else {
				claimedAmtTxt.setValue("0");
			}

			claimedAmtTxt.setReadOnly(true);
			claimedAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			formFLayout1.addComponent(claimedAmtTxt);

			provisionalAmt = new TextField();
			provisionalAmt.setCaption("Provision Amount ");
			provisionalAmt.setWidth("70px");
			provisionalAmt.setHeight("-1px");
			if (claimDto != null) {
				provisionalAmt.setValue(claimDto.getProvisionAmount() != null ? claimDto
						.getProvisionAmount().toString() : "");
			}
			provisionalAmt.setReadOnly(true);
			provisionalAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			formFLayout1.addComponent(provisionalAmt);
			
			FormLayout accidentFrmLayout = new FormLayout();
			//Vaadin8-setImmediate() accidentFrmLayout.setImmediate(true);
			accidentFrmLayout.setMargin(true);
			accidentFrmLayout.setSpacing(true);
			
			incedentOption = new OptionGroup("Accident / Death");
			
			Collection<Boolean> paClaimTypeValues = new ArrayList<Boolean>(2);
			paClaimTypeValues.add(true);
			paClaimTypeValues.add(false);

			incedentOption.addItems(paClaimTypeValues);
			incedentOption.setItemCaption(true, "Accident");
			incedentOption.setItemCaption(false, "Death");
			incedentOption.setStyleName("horizontal");
			//Vaadin8-setImmediate() incedentOption.setImmediate(true);
			incedentOption.setValue(claimDto.getIncidenceFlag());
			incedentOption.setReadOnly(true);
					
			accidentFrmLayout.addComponent(incedentOption);
			
			dateOfAccidentDeath = new PopupDateField("Date of Accident / Death");
			dateOfAccidentDeath.setDateFormat("dd/MM/yyyy");
			dateOfAccidentDeath.setValue(claimDto.getIncidenceDate()); 
			dateOfAccidentDeath.setReadOnly(true);
			
			//accidentFrmLayout.addComponent(dateOfAccidentDeath);	
			
			dateOfAccident = new PopupDateField("Date of Accident");
			dateOfAccident.setDateFormat("dd/MM/yyyy");
			dateOfAccident.setValue(claimDto.getAccidentDate()); 
			dateOfAccident.setReadOnly(true);
			
			accidentFrmLayout.addComponent(dateOfAccident);	
			
			dateOfDeath = new PopupDateField("Date of Death");
			dateOfDeath.setDateFormat("dd/MM/yyyy");
			dateOfDeath.setValue(claimDto.getDeathDate()); 
			dateOfDeath.setReadOnly(true);
			
			accidentFrmLayout.addComponent(dateOfDeath);	
			
			FormLayout injuryDetailLayout = new FormLayout();
			//Vaadin8-setImmediate() injuryDetailLayout.setImmediate(true);
			injuryDetailLayout.setMargin(true);
			injuryDetailLayout.setSpacing(true);
			
			injuryDetailsTxt = new TextArea();
			injuryDetailsTxt.setCaption("Injury / Loss Details");
			injuryDetailsTxt.setValue(claimDto.getInjuryRemarks() != null ? claimDto.getInjuryRemarks() :"");
			injuryDetailsTxt.setReadOnly(true);
			injuryDetailLayout.addComponent(injuryDetailsTxt);
			
			claimDetailsLayout.addComponents(formFLayout1,accidentFrmLayout,injuryDetailLayout);
			addListener();
			return claimDetailsLayout;
//			return formFieldsLayout;
		}

		public void openPdfFileInWindow(final String filepath) {
			
			Window window = new Window();
			// ((VerticalLayout) window.getContent()).setSizeFull();
			window.setResizable(true);
			window.setCaption("PA Claim Form Covering Letter PDF");
			window.setWidth("800");
			window.setHeight("600");
			window.setModal(true);
			window.center();

			Path p = Paths.get(filepath);
			String fileName = p.getFileName().toString();
			StreamResource.StreamSource s = SHAUtils.getStreamResource(filepath);

			/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

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
			};*/

			StreamResource r = new StreamResource(s, fileName);
			Embedded e = new Embedded();
			e.setSizeFull();
			e.setType(Embedded.TYPE_BROWSER);
			r.setMIMEType("application/pdf");
			e.setSource(r);
			SHAUtils.closeStreamResource(s);
			window.setContent(e);
			UI.getCurrent().addWindow(window);
		}
		
		public boolean validatePage(){
			boolean clickAction = true;
			
			if(coveringLetterRemarksTxta.getValue() != null && !coveringLetterRemarksTxta.getValue().isEmpty()){
				
				this.coveringLetterBean.getClaimDto().setRegistrationRemarks(coveringLetterRemarksTxta.getValue());
				this.claimDto.setRegistrationRemarks(coveringLetterRemarksTxta.getValue());
			}
			
			if(clickAction){
					
				if(!paCoveringLetterDocTableObject.isValid()){
						showErrorMessage("Please Select Atleast One Document for Letter");
						return !clickAction;
				}	
				else{
					this.coveringLetterBean.getClaimDto().setDocumentCheckListDTO(paCoveringLetterDocTableObject.getValues());
					this.claimDto.setDocumentCheckListDTO(paCoveringLetterDocTableObject.getValues());
					this.docTableListDto = paCoveringLetterDocTableObject.getValues();
					this.claimDto.setRegistrationRemarks(coveringLetterRemarksTxta.getValue());
					this.coveringLetterBean.getClaimDto().setRegistrationRemarks(coveringLetterRemarksTxta.getValue());
				}
					
					this.claimDto.setCreatedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
				}					
			return clickAction;									
		}
		private void showErrorMessage(String eMsg) {
			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
		}
		
		public List<DocumentCheckListDTO> getUpdatedList(){
		
			return paCoveringLetterDocTableObject.getTableList();
		}
		
		
		public void addListener()
		{
			dateOfAccident.addValueChangeListener(new Property.ValueChangeListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void valueChange(ValueChangeEvent event) {
					Policy policy = (Policy) ((DateField) event
							.getProperty()).getData();

					Date enteredDate = (Date) ((DateField) event
							.getProperty()).getValue();
					if (enteredDate != null) {

						try {
							dateOfAccident.validate();
							enteredDate = (Date) event.getProperty()
									.getValue();
						} catch (Exception e) {
							dateOfAccident.setValue(null);
							showErrorMessage("Please Enter a valid Accident Date");
							// Notification.show("Please Enter a valid Date");
							return;
						}
					}

					Date currentDate = new Date();
					Date policyFrmDate = null;
					Date policyToDate = null;
					if (policy != null) {
						policyFrmDate = policy.getPolicyFromDate();
						policyToDate = policy.getPolicyToDate();
					}
					if (enteredDate != null && policyFrmDate != null
							&& policyToDate != null) {
						if (!enteredDate.after(policyFrmDate)
								|| enteredDate.compareTo(policyToDate) > 0) {
							event.getProperty().setValue(null);
						
							showErrorMessage("Accident Date is not in range between Policy From Date and Policy To Date.");
						}
					}
				}
			});		
			
			dateOfDeath.addValueChangeListener(new Property.ValueChangeListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void valueChange(ValueChangeEvent event) {
					Policy policy = (Policy) ((DateField) event
							.getProperty()).getData();

					Date enteredDate = (Date) ((DateField) event
							.getProperty()).getValue();
					if (enteredDate != null) {

						try {
							dateOfDeath.validate();
							enteredDate = (Date) event.getProperty()
									.getValue();
						} catch (Exception e) {
							dateOfDeath.setValue(null);
							showErrorMessage("Please Enter a valid Death Date");
							// Notification.show("Please Enter a valid Date");
							return;
						}
					}

					Date currentDate = new Date();
					Date policyFrmDate = null;
					Date policyToDate = null;
					if (policy != null) {
						policyFrmDate = policy.getPolicyFromDate();
						policyToDate = policy.getPolicyToDate();
					}
					/*if (enteredDate != null && policyFrmDate != null
							&& policyToDate != null) {
						if (!enteredDate.after(policyFrmDate)
								|| enteredDate.compareTo(policyToDate) > 0) {
							event.getProperty().setValue(null);
						
							showErrorMessage("Death Date is not in range between Policy From Date and Policy To Date.");
						}
					}*/
					if(null != enteredDate)
					{
						Date accidentDate = new Date();
						if(null != dateOfAccident.getValue()){
							accidentDate = dateOfAccident.getValue();
						}
						if (accidentDate != null && null != enteredDate) {
							
							Long diffDays = SHAUtils.getDaysBetweenDate(accidentDate,enteredDate);
							
							if(null != diffDays && diffDays>365)
							{
								showErrorMessage("The date of death captured is beyond 12 months from the date of accident");
							}
						}
					}
					
					
					
				}
			});						

		}
}
