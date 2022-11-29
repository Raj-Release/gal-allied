package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.BillEntryRODReconsiderRequestTable;
import com.shaic.claim.rod.wizard.tables.RODQueryDetailsTable;
import com.shaic.claims.reibursement.addaditionaldocuments.SelectRODtoAddAdditionalDocumentsTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;

public class ReceivedPhysicalDocumentDetailsPage extends ViewComponent{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BeanFieldGroup<DocumentDetailsDTO> binder;

	@Inject
	private ReceiptOfDocumentsDTO bean;

	@Inject
	private RODQueryDetailsTable rodQueryDetails;

	/*@Inject
	private DocumentCheckListTable documentCheckList;*/

	@Inject
	private BillEntryRODReconsiderRequestTable reconsiderRequestDetails;



	private VerticalLayout documentDetailsPageLayout;
	

	@SuppressWarnings("unused")
	private GWizard wizard;

	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	@SuppressWarnings("unused")
	private List<DocumentDetailsDTO> docsDetailsList;

	@SuppressWarnings("unused")
	private DocumentDetailsDTO docDTO;

	@Inject
	private SelectRODtoAddAdditionalDocumentsTable selectRODtoAddAdditionalDocumentsTable;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	public Boolean isNext = false;

	@PostConstruct
	public void init() {

	}

	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<DocumentDetailsDTO>(
				DocumentDetailsDTO.class);
		this.binder.setItemDataSource(this.bean.getDocumentDetails());
	}

	public Component getContent() {

		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		documentDetailsPageLayout = new VerticalLayout();
		documentDetailsPageLayout.setSpacing(false);
		documentDetailsPageLayout.setMargin(true);	

		selectRODtoAddAdditionalDocumentsTable.init("", false, true, this.bean);
		
		documentDetailsPageLayout = new VerticalLayout(
				selectRODtoAddAdditionalDocumentsTable);	
		
		showOrHideValidation(false);
		return documentDetailsPageLayout;
	}	

	protected Collection<Boolean> getReadioButtonOptions() {

		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}


	@SuppressWarnings("unused")
	private HorizontalLayout buildQueryDetailsLayout() {
		List<RODQueryDetailsDTO> rodQueryDetailsList = this.bean
				.getRodQueryDetailsList();
		rodQueryDetails.init("", false, false);
		if (null != rodQueryDetailsList && !rodQueryDetailsList.isEmpty()) {
			rodQueryDetails.setTableList(rodQueryDetailsList);
		}

		HorizontalLayout queryDetailsLayout = new HorizontalLayout(
				rodQueryDetails);
		queryDetailsLayout.setCaption("View Query Details");
		queryDetailsLayout.setSpacing(true);
		queryDetailsLayout.setMargin(true);
		return queryDetailsLayout;

	}

	@SuppressWarnings("unused")
	private HorizontalLayout buildReconsiderRequestLayout() {
		List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList = this.bean
				.getReconsiderRodRequestList();
		reconsiderRequestDetails.init("", false, false);
		if (null != reconsiderRODRequestList
				&& !reconsiderRODRequestList.isEmpty()) {
			reconsiderRequestDetails.setTableList(reconsiderRODRequestList);
		}

		HorizontalLayout reconsiderRequestLayout = new HorizontalLayout(
				reconsiderRequestDetails);
		reconsiderRequestLayout
				.setCaption("Select Earlier Request to be Reconsidered");
		reconsiderRequestLayout.setSpacing(true);
		reconsiderRequestLayout.setMargin(true);
		return reconsiderRequestLayout;

	}


	@SuppressWarnings("unchecked")
	public void loadContainerDataSources(Map<String, Object> referenceDataMap) {	
	
		this.docsDetailsList = (List<DocumentDetailsDTO>) referenceDataMap
				.get("validationDocList");
		this.docDTO = (DocumentDetailsDTO) referenceDataMap
				.get("billClaissificationDetails");		
	}

	@SuppressWarnings("unused")
	private void unbindField(List<Field<?>> field) {
		if (null != field && !field.isEmpty()) {
			for (Field<?> field2 : field) {
				if (field2 != null) {
					Object propertyId = this.binder.getPropertyId(field2);				
					if (field2 != null && propertyId != null) {
						this.binder.unbind(field2);
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void unbindField(Field<?> field) {
		if (field != null) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field != null && propertyId != null) {
				this.binder.unbind(field);
			}
		}
	}



	public boolean validatePage() {

		Boolean hasError = false;
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
		
		if(null != selectRODtoAddAdditionalDocumentsTable)
		{
			Boolean isValid = selectRODtoAddAdditionalDocumentsTable.isValid();
			if(!isValid)
			{
				hasError = true;
				String error = this.selectRODtoAddAdditionalDocumentsTable.getErrors();
				eMsg.append(error).append("</br>");
			}
		}
		 

		if (hasError) {
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			getUI();
			dialog.show(UI.getCurrent(), null, true);

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();				
				
			} catch (CommitException e) {
				e.printStackTrace();
			}
			return true;
		}
	}	

	public void setTableValuesToDTO() {
		/**
		 * Get the list of DTO's first. Loop it and get individual object. And
		 * then assign them to dto and set this dto to list. This final list
		 * will be set in bean again.
		 * 
		 * */
				
		List<RODQueryDetailsDTO> rodQueryDetailsDTO = this.rodQueryDetails
				.getValues();
		if (null != rodQueryDetailsDTO && !rodQueryDetailsDTO.isEmpty()) {
			for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {
				if (null != rodQueryDetailsDTO2.getReplyStatus()
						&& ("Yes").equals(rodQueryDetailsDTO2.getReplyStatus()
								.trim())) {
					this.bean.setRodqueryDTO(rodQueryDetailsDTO2);
					break;
				}
			}
		}
	}
	

	public void saveReconsideRequestTableValue(
			ReconsiderRODRequestTableDTO dto,
			List<UploadDocumentDTO> uploadDocsDTO) {
		this.bean.setReconsiderRODdto(dto);
		this.bean.setUploadDocsList(uploadDocsDTO);
	}
	

	public void setDocumentDetailsListForValidation(
			List<DocumentDetailsDTO> documentDetailsDTO) {
		this.docsDetailsList = documentDetailsDTO;
	}
	
	public void isNextClicked()
	{
		isNext = true;
	}
	

}
