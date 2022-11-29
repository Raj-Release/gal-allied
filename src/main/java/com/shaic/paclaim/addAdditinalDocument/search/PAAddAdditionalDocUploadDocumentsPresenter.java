package com.shaic.paclaim.addAdditinalDocument.search;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(PAAddAdditionalDocUploadDocumentsView.class)
public class PAAddAdditionalDocUploadDocumentsPresenter extends AbstractMVPPresenter<PAAddAdditionalDocUploadDocumentsView>{
	
	public static final String ADD_ADDITIONAL_UPLOAD_DOC_SETUP_DROPDOWN_VALUES = "add_addl_upload_doc_setup_dropdown_values_PA";
	
	public static final String ADD_ADDITIONAL_SUBMIT_UPLOADED_DOCUMENTS = "add_addl_submit_uploaded_documents_PA";
	
	public static final String ADD_ADDITIONAL_DELETE_UPLOADED_DOCUMENTS = "add_addl_delete_uploaded_documents_PA";	
	
	public static final String ADD_ADDITIONAL_EDIT_UPLOADED_DOCUMENTS = "add_addl_edit_uploaded_documents_PA";
	
	@EJB
	private MasterService masterService;
	
	/*@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;*/

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setUpDropDownValues(
			@Observes @CDIEvent(ADD_ADDITIONAL_UPLOAD_DOC_SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {
		/*ReceiptOfDocumentsDTO bean = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		
		BeanItemContainer<SelectValue> beanContainer = masterService.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE);
		
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		
		{
			List<SelectValue> selectValueList = beanContainer.getItemIds();
			List<SelectValue> finalBillTypeList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> billTypeContainer = null;
			if(null != selectValueList && !selectValueList.isEmpty())
			{
				billTypeContainer = new BeanItemContainer<SelectValue>(
							SelectValue.class);
				for (SelectValue selectValue : selectValueList) {
					{
						if((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.YES_FLAG).equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationFlag()))
						{
							if(null != selectValue.getValue() && (selectValue.getValue().endsWith(SHAConstants.BYPASS_BILL_TYPE_FOR_CASHLESS)))
							{
								finalBillTypeList.add(selectValue);
							}
						}
						else
						{
							if(null != selectValue.getValue() && !(selectValue.getValue().endsWith(SHAConstants.BYPASS_BILL_TYPE_FOR_CASHLESS)))
							{
								finalBillTypeList.add(selectValue);

							}
						}
					}
					
				}
				billTypeContainer.addAll(finalBillTypeList);
			}
			referenceDataMap.put("fileType", billTypeContainer);
		}
		else
		{
			referenceDataMap.put("fileType", beanContainer);
		}
		view.setUpDropDownValues(referenceDataMap);
		
		view.setUpDropDownValues(referenceDataMap);*/
			Map<String, Object> referenceDataMap = new HashMap<String, Object>();
			/*referenceDataMap.put("fileType", masterService
					.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE));*/
			BeanItemContainer<SelectValue> uploadDocFileTypeValues;
			BeanItemContainer<SelectValue> tempUploadDocFileTypeValues;
			uploadDocFileTypeValues = masterService.getSelectValueContainer(ReferenceTable.PA_ROD_UPLOAD_DOC_TABLE_FILE_TYPE);
			tempUploadDocFileTypeValues = masterService.getSelectValueContainer(ReferenceTable.INVESTIGATION_CLAIM_REPOPT_TABLE_FILE_TYPE);
			
			for(int i = 0; i < tempUploadDocFileTypeValues.size(); i++) {
				uploadDocFileTypeValues.addBean(tempUploadDocFileTypeValues.getIdByIndex(i));
				System.out.println("tempUploadDocFileTypeValues.getIdByIndex(i).getValue() = " + tempUploadDocFileTypeValues.getIdByIndex(i).getValue());
			}
			System.out.println("uploadDocFileTypeValues.size() = " + uploadDocFileTypeValues.size());
			uploadDocFileTypeValues.sort(new Object[] { "value" }, new boolean[] { true });
			referenceDataMap.put("fileType", uploadDocFileTypeValues);
			view.setUpDropDownValues(referenceDataMap);
		
		}
	
	
	public void submitUploadedDocuments(
			@Observes @CDIEvent(ADD_ADDITIONAL_SUBMIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		//ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();
		//List<UploadDocumentDTO> uploadDocLst = (List<UploadDocumentDTO>) parameters.getPrimaryParameter();
		UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
		//view.loadUploadedDocsTableValues(uploadDocLst);
		view.loadUploadedDocsTableValues(uploadDocDTO);
		
	}
	
	public void deleteUploadedDocumentDetails(
			@Observes @CDIEvent(ADD_ADDITIONAL_DELETE_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.deleteUploadDocumentDetails(uploadDTO);
		
	}
	
	public void editUploadedDocumentDetails(
			@Observes @CDIEvent(ADD_ADDITIONAL_EDIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.editUploadDocumentDetails(uploadDTO);
		
	}
}
