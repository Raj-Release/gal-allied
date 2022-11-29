package com.shaic.claim.withdrawPostProcessWizard;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.cashlessprocess.withdrawpreauthpostprocess.SearchWithdrawCashLessPostProcessTableDTO;
import com.shaic.claim.cashlessprocess.withdrawpreauthpostprocess.WithDrawPostProcessBillDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

	/**
	 * @author ntv.vijayar
	 *
	 */
public class UpdateBillClassificationPostProcessPage  extends ViewComponent implements WizardStep<WithdrawPreauthPostProcessPageDTO>{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UpdateBillClassificationListenerTable updateBillClassificationListenerTable;
		
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private HorizontalLayout hLayout;

	private SearchWithdrawCashLessPostProcessTableDTO tableDTO;
		
	private WithdrawPreauthPostProcessPageDTO bean;
		
	@PostConstruct
	public void initView(){
			
	}

	@Override
	public String getCaption() {
		return "Update Bill Classification";
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

		private boolean validatePage(Boolean hasError) {
			//Boolean hasError = false;
			showOrHideValidation(true);
			String eMsg = "";
			if (hasError) {
				setRequired(true);
				Label label = new Label("Select Reason For Conversion", ContentMode.HTML);
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

				hasError = true;
				return !hasError;
			} 
			showOrHideValidation(false);
			return true;
		}

		private void setRequired(Boolean isRequired) {

			if (!mandatoryFields.isEmpty()) {
				for (int i = 0; i < mandatoryFields.size(); i++) {
					AbstractField<?> field = (AbstractField<?>) mandatoryFields
							.get(i);
					field.setRequired(isRequired);
				}
			}
		}

	
		public void init(WithdrawPreauthPostProcessPageDTO bean, SearchWithdrawCashLessPostProcessTableDTO tableDTO) {
			// TODO Auto-generated method stub
			this.bean = bean;
			hLayout = new HorizontalLayout();
			updateBillClassificationListenerTable.init();
			updateBillClassificationListenerTable.addBeanToList(tableDTO.getBillClassificationTableDTO());
			updateBillClassificationListenerTable.setTableList(tableDTO.getBillClassificationTableDTO());
			updateBillClassificationListenerTable.setBean(tableDTO.getPreauthDto());

		}

		@Override
		public Component getContent()
		{			
			hLayout.addComponent(updateBillClassificationListenerTable);
			return hLayout;	
		}

		@Override
		public void setupReferences(Map<String, Object> referenceData) {
			// TODO Auto-generated method stub
			updateBillClassificationListenerTable.setReferenceData(referenceData);

		}
		
	

		@Override
		public boolean onAdvance() {
			Boolean isHospitalizationselected = validateBillClassifationForHospitalisation();
			
			 if (isHospitalizationselected){
					List<WithDrawPostProcessBillDetailsDTO> billClassificationDlts = updateBillClassificationListenerTable.getValues();
					List<UploadDocumentDTO> billClasifications = new ArrayList<UploadDocumentDTO>();
					for (WithDrawPostProcessBillDetailsDTO withDrawPostProcessBillDetailsDTO : billClassificationDlts) {
						List<UploadDocumentDTO> uploadList = withDrawPostProcessBillDetailsDTO.getUploadDocList();
						List<UploadDocumentDTO> list = new ArrayList<UploadDocumentDTO>();
						for (UploadDocumentDTO uploadDocumentDTO : uploadList) {
							uploadDocumentDTO.setHospitalizationFlag(withDrawPostProcessBillDetailsDTO.getPreauthDto().getHospitalizaionFlag() ? "Y" :"N");
							uploadDocumentDTO.setPostHospitalizationFlag(withDrawPostProcessBillDetailsDTO.getPreauthDto().getPostHospitalizaionFlag() ? "Y" : "N");
							uploadDocumentDTO.setPreHospitalizationFlag(withDrawPostProcessBillDetailsDTO.getPreauthDto().getPreHospitalizaionFlag() ? "Y" : "N");
							list.add(uploadDocumentDTO);
						} 
						billClasifications.addAll(list);
					}
					bean.setUpdateBillClassificationValues(billClasifications);
					return true;
				}
			 
//			if(isHospitalizationselected && validateBillClassifation()){
//				List<UploadDocumentDTO> billClassificationDlts = updateBillClassificationListenerTable.getValues();
//				bean.setUpdateBillClassificationValues(billClassificationDlts);
//				return true;
//			} 
				
			else {
				return false;
			}
		}

		@Override
		public boolean onBack() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean onSave() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void init(WithdrawPreauthPostProcessPageDTO bean) {
			// TODO Auto-generated method stub
			
		}
		
		public void setBillClassificationBills(UploadDocumentDTO uploadDTO) {
			updateBillClassificationListenerTable.loadBillEntryValuesforbill(uploadDTO);

		}
		
		public void setCategoryValues(
				BeanItemContainer<SelectValue> selectValueContainer) {
			if (updateBillClassificationListenerTable != null) {
				this.updateBillClassificationListenerTable
						.setupCategoryValues(selectValueContainer);
			}

		}
		
		public void setBillEntryFinalStatus(UploadDocumentDTO uploadDTO) {
			updateBillClassificationListenerTable.setBillEntryStatus(uploadDTO);
			/*if (updateBillClassificationListenerTable != null) {
				List<WithDrawPostProcessBillDetailsDTO> uploadWithDrawDoc = updateBillClassificationListenerTable
						.getValues();
				List<UploadDocumentDTO> uploadDoc = new ArrayList<UploadDocumentDTO>();
				for (WithDrawPostProcessBillDetailsDTO uploadWithDrawDocumentDTO : uploadWithDrawDoc) {
					UploadDocumentDTO uploadedDoc = new UploadDocumentDTO();
					uploadDoc.uploadWithDrawDocumentDTO.getUploadDocList();
				}
				List<UploadDocumentDTO> uploadList = new ArrayList<UploadDocumentDTO>();
				for (UploadDocumentDTO uploadDocumentDTO : uploadDoc) {
					if (null != uploadDocumentDTO.getFileType()
							&& null != uploadDocumentDTO.getFileType().getValue()) {
						if (uploadDocumentDTO.getFileType().getValue()
								.contains("Bill")) {
							
							 * if(uploadDocumentDTO.getBillNo().equalsIgnoreCase(
							 * uploadDTO.getBillNo())) { uploadList.add(uploadDTO);
							 * } else { uploadList.add(uploadDocumentDTO); }
							 

							*//**
							 * Sequence number is an internal parameter maintained
							 * for updating the uploadlistener table. This is
							 * because the row for which the bill is entered should
							 * only get updated. Rest of rows should be the same.
							 * Earlier this was done with bill no. But there are
							 * chance that even bill no can be duplicate. Hence
							 * removed this and added validation based on seq no.
							 * *//*
							if (uploadDocumentDTO.getSeqNo().equals(
									uploadDTO.getSeqNo())) {
								//uploadList.add(uploadDTO);
							} else {
								uploadList.add(uploadDocumentDTO);
							}

						} else {
							uploadList.add(uploadDocumentDTO);
						}
					}

				}
				uploadList.add(uploadDTO);
				updateBillClassificationListenerTable.updateTable(uploadList);
			}*/
		}
		
//		public Boolean validateBillClassifation(){
//			Boolean hasError = false;
//			List<UploadDocumentDTO> billClassificationDlts = updateBillClassificationListenerTable.getValues();
//			for (UploadDocumentDTO uploadDocumentDTO : billClassificationDlts) {
//				if(uploadDocumentDTO.getPreauthDto() != null && uploadDocumentDTO.getPreauthDto().getPartialHospitalizaionFlag() != null 
//						&& uploadDocumentDTO.getPreauthDto().getPartialHospitalizaionFlag().equals(Boolean.TRUE)){
//					hasError = true;
//					MessageBox.createError()
//			    	.withCaptionCust("Errors").withHtmlMessage("Please Select Bill Classification Details")
//			        .withOkButton(ButtonOption.caption("OK")).open();
//					break;
//				}
//			}
//			
//			return !hasError;
//		}
		
		public Boolean validateBillClassifationForHospitalisation(){
			Boolean hasError = false;
			int i = 1;
			List<WithDrawPostProcessBillDetailsDTO> billClassificationDlts = updateBillClassificationListenerTable.getValues();
			for (WithDrawPostProcessBillDetailsDTO uploadDocumentDTO : billClassificationDlts) {
				
				if(uploadDocumentDTO.getPreauthDto() != null && uploadDocumentDTO.getPreauthDto().getHospitalizaionFlag().equals(Boolean.FALSE) && (uploadDocumentDTO.getBillClassification() != null && 
						uploadDocumentDTO.getBillClassification().contains("Partial-Hospitalization,"))){
					hasError = true;
					MessageBox.createError()
			    	.withCaptionCust("Errors").withHtmlMessage("Please Select Hospitalisation Bill Classification")
			        .withOkButton(ButtonOption.caption("OK")).open();
					break;
				} else if(uploadDocumentDTO.getPreauthDto() != null && uploadDocumentDTO.getPreauthDto().getHospitalizaionFlag().equals(Boolean.TRUE) && ! uploadDocumentDTO.getIsReconsiderationRequest()){
	
					if(i>1) {
						hasError = true;
						MessageBox.createError()
				    	.withCaptionCust("Errors").withHtmlMessage("Hospitalisation is selected for multiple RODs. Please select hospitalisation for only one ROD")
				        .withOkButton(ButtonOption.caption("OK")).open();
						break;
					}
					i++;
				}
			}
			
			return !hasError;
		}
		
		
		public void setBillClassificationBillEntries(List<UploadDocumentDTO> uploadDTO) {
			updateBillClassificationListenerTable.setBillClassificationBillEntries(uploadDTO);

		}

}


