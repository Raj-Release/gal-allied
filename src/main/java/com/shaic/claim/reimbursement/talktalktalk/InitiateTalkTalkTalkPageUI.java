package com.shaic.claim.reimbursement.talktalktalk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.adviseonped.AdviseOnPEDPresenter;
import com.shaic.claim.cashlessprocess.downsize.wizard.DownSizePreauthWizardPresenter;
import com.shaic.claim.cashlessprocess.downsizeRequest.page.DownsizePreauthRequestWizardPresenter;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.fieldVisitPage.FieldVisitPagePresenter;
import com.shaic.claim.fileUpload.fileUploadPresenter;
import com.shaic.claim.hospitalCommunication.AcknowledgeHospitalPresenter;
import com.shaic.claim.negotiation.NegotiationPreauthRequestPresenter;
import com.shaic.claim.pedquery.PEDQueryPresenter;
import com.shaic.claim.pedrequest.approve.PEDRequestDetailsApprovePresenter;
import com.shaic.claim.pedrequest.process.PEDRequestDetailsProcessPresenter;
import com.shaic.claim.pedrequest.teamlead.PEDRequestDetailsTeamLeadPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.processRejectionPage.ProcessRejectionPresenter;
import com.shaic.claim.registration.ClaimRegistrationPresenter;
import com.shaic.claim.registration.convertClaimPage.ConvertClaimPagePresenter;
import com.shaic.claim.registration.convertClaimToReimbursement.convertReimbursementPage.ConvertReimbursementPagePresenter;
import com.shaic.claim.reimbursement.billing.wizard.BillingWizardPresenter;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.financialapproval.wizard.FinancialWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.wizard.MedicalApprovalZonalReviewWizardPresenter;
import com.shaic.claim.reimbursement.pa.medicalapproval.processclaimrequest.wizard.PAClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestWizardPresenter;
import com.shaic.claim.reimbursement.submitSpecialist.SubmitSpecialistAdvisePresenter;
import com.shaic.claim.rod.wizard.pages.AcknowledgeDocReceivedWizardPresenter;
import com.shaic.claim.rod.wizard.pages.BillEntryWizardPresenter;
import com.shaic.claim.rod.wizard.pages.CreateRODWizardPresenter;
import com.shaic.claim.rod.wizard.tables.ExtraEffortEmployeeListenerTable;
import com.shaic.claim.rod.wizard.tables.QuantumReductionDetailsListenerTable;
import com.shaic.claim.rod.wizard.tables.RRCRequestCategoryListenerTable;
import com.shaic.claim.submitSpecialist.SubmitSpecialistPagePresenter;
import com.shaic.claim.withdrawPostProcessWizard.WithdrawPreauthPostProcessWizardPresenter;
import com.shaic.claim.withdrawWizard.WithdrawPreauthWizardPresenter;
import com.shaic.claims.reibursement.addaditionaldocuments.AddAditionalDocumentsPresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.paclaim.billing.processclaimbilling.page.PABillingWizardPresenter;
import com.shaic.paclaim.cashless.downsize.wizard.PADownSizePreauthWizardPresenter;
import com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles.PAPreauthEnhancemetWizardPresenter;
import com.shaic.paclaim.cashless.fle.wizard.wizardfiles.PAPremedicalEnhancementWizardPresenter;
import com.shaic.paclaim.cashless.flp.wizard.wizardFiles.PAPreMedicalPreauthWizardPresenter;
import com.shaic.paclaim.cashless.preauth.wizard.wizardfiles.PAPreauthWizardPresenter;
import com.shaic.paclaim.cashless.processdownsize.wizard.PADownsizePreauthRequestWizardPresenter;
import com.shaic.paclaim.cashless.withdraw.wizard.PAWithdrawPreauthWizardPresenter;
import com.shaic.paclaim.convertClaimToReimb.ConvertPAClaimPagePresenter;
import com.shaic.paclaim.convertClaimToReimbursement.convertReimbursementPage.PAConvertReimbursementPagePresenter;
import com.shaic.paclaim.financial.claimapproval.nonhosiptalpage.PAClaimAprNonHosWizardPresenter;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page.PANonHospFinancialWizardPresenter;
import com.shaic.paclaim.health.reimbursement.billing.wizard.wizardfiles.PAHealthBillingWizardPresenter;
import com.shaic.paclaim.health.reimbursement.financial.wizard.PAHealthFinancialWizardPresenter;
import com.shaic.paclaim.health.reimbursement.medicalapproval.wizard.PAHealthClaimRequestWizardPresenter;
import com.shaic.paclaim.processRejectionPage.PAProcessRejectionPresenter;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAAcknowledgeDocumentWizardPresenter;
import com.shaic.paclaim.rod.createrod.search.PACreateRODWizardPresenter;
import com.shaic.paclaim.rod.enterbilldetails.search.PAEnterBillDetailsWizardPresenter;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.AcknowledgeInvestigationCompletedPresenter;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigationPresenter;
import com.shaic.reimbursement.draftinvesigation.DraftInvestigationPresenter;
import com.shaic.reimbursement.processi_investigationi_initiated.ProcessInvestigationInitiatedPresenter;
import com.shaic.reimbursement.uploadTranslatedDocument.UploadTranslatedDocumentPresenter;
import com.shaic.reimbursement.uploadrodreports.UploadInvestigationReportPresenter;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import com.zybnet.autocomplete.server.AutocompleteField;

public class InitiateTalkTalkTalkPageUI extends ViewComponent {
		
		
		private Panel rrcViewPanel;
		
		
		private TextField txtEmployeeName;
		
		private	TextField txtEmployeeId;
		
		private Button btnSubmit;
		
		private Button btnCancel;
		
		private String presenterString;
		
		private PreauthDTO bean;
		
		private Window popup;
		
		private BeanFieldGroup<InitiateTalkTalkTalkDTO> binder;
		
		private Map<String, Object> containerMap;
		
		private Boolean isTableDisable = false;
		
		@Inject
		private Instance<TalkTalkTalkCategoryListenerTable> talkTalkTalkListenerTableObj;
		
		private TalkTalkTalkCategoryListenerTable talkTalkTalkListenerTable;
		
		@Inject
		private Instance<EditTalkTalkTalkCategoryListenerTable> editTalkTalkTalkListenerTableObj;
		
		private EditTalkTalkTalkCategoryListenerTable editTalkTalkTalkListenerTable;
		
//		private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
		
		//private VerticalLayout verticalRRCLayout;
		
		
		public void initPresenter(String presenterString) {
			this.presenterString = presenterString;
		}
		
		public void init(PreauthDTO preauthDTO, Window popup)
		{
			this.bean = preauthDTO;
			this.popup = popup;
			this.containerMap = preauthDTO.getRrcDTO().getDataSourcesMap();
		/*	
			if(preauthDTO.getNewIntimationDTO() == null && (preauthDTO.getRrcDTO() != null && preauthDTO.getRrcDTO().getNewIntimationDTO() != null)){
	            preauthDTO.setNewIntimationDTO(preauthDTO.getRrcDTO().getNewIntimationDTO());
			}
			
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				DBCalculationService dbCalculationService = new DBCalculationService();
				Double sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
						preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				if(sumInsured != null){
					bean.getRrcDTO().setSumInsured(sumInsured);
				}
			}*/

			this.binder = new BeanFieldGroup<InitiateTalkTalkTalkDTO>(InitiateTalkTalkTalkDTO.class);
			this.binder.setItemDataSource(this.bean.getInitiateTalkTalkTalkDTO());
			binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
			
			
			VerticalLayout verticalRRCLayout = new VerticalLayout( talkTalkTalkDetailsPanel(),editTalkTalkTalkDetailsPanel(), buildButtonLayout());
		
			verticalRRCLayout.setSpacing(true);
			verticalRRCLayout.setMargin(true);
			
			setTableValues();
			addListener();
			
			setCompositionRoot(verticalRRCLayout);
			
		}
		
		private void setTableValues()
		{
			if(null != talkTalkTalkListenerTable)
			{
				List<InitiateTalkTalkTalkDTO> intiateTalkTalkList = this.bean.getInitiateTalkTalkTalkDTOList();
				if(null != intiateTalkTalkList && !intiateTalkTalkList.isEmpty())
				{ 
					talkTalkTalkListenerTable.setTableList(intiateTalkTalkList);
					//talkTalkTalkListenerTable.setEnabled(false);
					isTableDisable = true;
					editTalkTalkTalkListenerTable.setEnabled(true);
				}
				/*else
				{
					talkTalkTalkListenerTable = talkTalkTalkListenerTableObj.get();
					talkTalkTalkListenerTable.initPresenter(this.presenterString);
					talkTalkTalkListenerTable.setReferenceData(this.containerMap);
					talkTalkTalkListenerTable.init(true,bean);
				}*/
			}
		}
		private void setTableValuesDTO()
		{
			if(null != talkTalkTalkListenerTable &&  !isTableDisable)
			{
				List<InitiateTalkTalkTalkDTO> categoryList = talkTalkTalkListenerTable.getValues();
				if(null != categoryList && !categoryList.isEmpty())
				{
					bean.setInitiateTalkTalkTalkDTOList(categoryList);
				}
			}
			if(null != editTalkTalkTalkListenerTable)
			{
				List<InitiateTalkTalkTalkDTO> categoryList = editTalkTalkTalkListenerTable.getValues();
				if(null != categoryList && !categoryList.isEmpty())
				{
					bean.setInitiateTalkTalkTalkDTOList(categoryList);
				}
			}
			
		}
		
		
		private HorizontalLayout talkTalkTalkDetailsPanel()
		{
			/*String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			bean.setStrUserName(userName);*/
			talkTalkTalkListenerTable = talkTalkTalkListenerTableObj.get();
			talkTalkTalkListenerTable.initPresenter(this.presenterString);
			talkTalkTalkListenerTable.setReferenceData(this.containerMap);
			talkTalkTalkListenerTable.init(true,bean);
			
			VerticalLayout vReductionLayout = new VerticalLayout(talkTalkTalkListenerTable);
			vReductionLayout.setWidth("100%");
			vReductionLayout.setSpacing(true);
			vReductionLayout.setMargin(true);
			
			HorizontalLayout hTalkLayout = new HorizontalLayout(vReductionLayout);
			hTalkLayout.setWidth("100%");
			hTalkLayout.setSpacing(true);
			hTalkLayout.setMargin(true);
			
			
			return hTalkLayout;
			
		}
		
		private HorizontalLayout editTalkTalkTalkDetailsPanel()
		{
			
			editTalkTalkTalkListenerTable = editTalkTalkTalkListenerTableObj.get();
			editTalkTalkTalkListenerTable.initPresenter(this.presenterString);
			editTalkTalkTalkListenerTable.setReferenceData(this.containerMap);
			editTalkTalkTalkListenerTable.init(true,bean);
			editTalkTalkTalkListenerTable.setEnabled(false);
			
			VerticalLayout vReductionLayout = new VerticalLayout(editTalkTalkTalkListenerTable);
			vReductionLayout.setWidth("100%");
			vReductionLayout.setSpacing(true);
			vReductionLayout.setMargin(true);
			
			HorizontalLayout hTalkLayout = new HorizontalLayout(vReductionLayout);
			hTalkLayout.setWidth("100%");
			hTalkLayout.setSpacing(true);
			hTalkLayout.setMargin(true);
			
			
			return hTalkLayout;
			
		}
		
		private void addStyleForTextfield(FormLayout layout)
		{
			int iLayoutSize = layout.getComponentCount();
			for (int i = 0; i < iLayoutSize; i++) {
				TextField txtFld = (TextField) layout.getComponent(i);
				txtFld.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				txtFld.setNullRepresentation("");
			}
		}
		
		
		
		
		
		
		private HorizontalLayout buildButtonLayout()
		{
			btnSubmit = new Button("Submit");
			btnSubmit = new Button();
			btnSubmit.setCaption("Submit");
			btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			btnSubmit.setWidth("-1px");
			btnSubmit.setHeight("-10px");
			
			btnCancel = new Button();
			btnCancel.setCaption("Cancel");
			btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
			btnCancel.setWidth("-1px");
			btnCancel.setHeight("-10px");

			HorizontalLayout hBtnLayout = new HorizontalLayout(btnSubmit,btnCancel);
			hBtnLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_RIGHT);
			hBtnLayout.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
			hBtnLayout.setSpacing(true);
			hBtnLayout.setMargin(true);
			hBtnLayout.setWidth("100%");
			return hBtnLayout;
		}

		public void loadRRCRequestDropDownValues(
				BeanItemContainer<SelectValue> mastersValueContainer) {

			
		}
		
		public void addListener() {

			btnSubmit.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
						//binder.commit();
					if(validatePage())
					{
						setTableValuesDTO();
						String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
						bean.setStrUserName(userName);
						fireViewEvent(InitiateTalkTalkTalkWizardPresenter.SUBMIT_TALK_TALK_TALK_VALUES,bean);
					
					}
				}
				
			});
			
			btnCancel.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 5677998363425252239L;

				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog dialog = ConfirmDialog
							.show(getUI(),
									"Confirmation",
									"Are you sure you want to cancel ?",
									"No", "Yes", new ConfirmDialog.Listener() {

										public void onClose(ConfirmDialog dialog) {
											if (!dialog.isConfirmed()) {
												fireViewEvent(MenuItemBean.INITIATE_TALK_TALK_TALK,
														null);
											} else {
												// User did not confirm
											}
										}
									});

					dialog.setClosable(false);
					dialog.setStyleName(Reindeer.WINDOW_BLACK);
				}
			
			});
		}

		public void builTalkTalkTalkSuccessLayout() {
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
					fireViewEvent(MenuItemBean.INITIATE_TALK_TALK_TALK, null);

				}
			});
		}
			
		
		public boolean validatePage() {
			
			Boolean hasError = false;
			Boolean errorMsg = Boolean.FALSE;
			StringBuffer eMsg = new StringBuffer();
			if (!this.binder.isValid()) {

				for (Field<?> field : this.binder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						eMsg.append(errMsg.getFormattedHtmlMessage());
					}
					hasError = true;
				}
			}
			
			if(null != this.talkTalkTalkListenerTable && !isTableDisable)
			{
				Boolean isValid = talkTalkTalkListenerTable.isValid();
				if (!isValid) {
					hasError = true;
					List<String> errors = this.talkTalkTalkListenerTable.getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
				}
			}
			
			if(null != this.editTalkTalkTalkListenerTable && isTableDisable)
			{
				Boolean isValid = editTalkTalkTalkListenerTable.isValid();
				if (!isValid) {
					hasError = true;
					List<String> errors = this.editTalkTalkTalkListenerTable.getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
				}
			}
			
			if (hasError) {
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
				hasError = true;
				btnSubmit.setDisableOnClick(false);
				errorMsg = Boolean.FALSE;
				//return !hasError;
			} 
			else 
			{
				btnSubmit.setDisableOnClick(true);
				try {
					this.binder.commit();
				} catch (CommitException e) {
					e.printStackTrace();
				}
//				showOrHideValidation(false);
				errorMsg = Boolean.TRUE;
				//return true;
			}	
			
			return errorMsg;
		}
		
		public void invalidate(){
			if(talkTalkTalkListenerTable != null){
				talkTalkTalkListenerTable.invalidate();
			}
		}
		
		public void enableDialerButtons() {
			if(talkTalkTalkListenerTable != null){
				talkTalkTalkListenerTable.enableDialerButtons();
			}
			if(editTalkTalkTalkListenerTable != null){
				editTalkTalkTalkListenerTable.enableDialerButtons();
			}
		}
		

	}

