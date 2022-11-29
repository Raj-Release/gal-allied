package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(UploadReceivedPhysicalDocumentsView.class)
public class UploadReceivedPhysicalDocumentsPresenter extends AbstractMVPPresenter<UploadReceivedPhysicalDocumentsView>{

	/**
	 * 
	 */	
	
	public static final String UPLOAD_RECEIVED_PHYSICAL_DOC_SETUP_DROPDOWN_VALUES = "Upload Received Physical Doc Setup Drop Down Values";
	
	public static final String SUBMIT_RECEIVED_PHYSICAL_UPLOADED_DOCUMENTS = "Submit Upload Received Physical";
	
	public static final String DELETE_RECEIVED_PHYSICAL_UPLOADED_DOCUMENTS = "Delete Upload Received Physical";	
	
	public static final String EDIT_RECEIVED_PHYSICAL_UPLOADED_DOCUMENTS = "Edit Upload Received Physical";
	
	public static final String SAVE_RECEIVED_PHYSICAL_UPLOADED_DOCUMENTS = "Save Upload Received Physical";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private CreateRODService createRodService;

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setUpDropDownValues(
			@Observes @CDIEvent(UPLOAD_RECEIVED_PHYSICAL_DOC_SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {
		//Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		/*referenceDataMap.put("fileType", masterService
				.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		view.setUpDropDownValues(referenceDataMap);*/

		ReceiptOfDocumentsDTO bean = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		
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
							if(null != selectValue.getValue() && ((selectValue.getValue().endsWith(SHAConstants.BYPASS_BILL_TYPE_FOR_CASHLESS)) || ((SHAConstants.CASHLESS_SETTLEMENT_BILL).equalsIgnoreCase(selectValue.getValue()))))
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
		/*else
		{
			referenceDataMap.put("fileType", beanContainer);
		}*/
		view.setUpDropDownValues(referenceDataMap);
		
		//view.setUpDropDownValues(referenceDataMap);
		
		
		
		}
	
	
	public void submitUploadedDocuments(
			@Observes @CDIEvent(SUBMIT_RECEIVED_PHYSICAL_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {		
		
		UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();	
		view.loadUploadedDocsTableValues(uploadDocDTO);
		
	}
	
	public void deleteUploadedDocumentDetails(
			@Observes @CDIEvent(DELETE_RECEIVED_PHYSICAL_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.deleteUploadDocumentDetails(uploadDTO);
		
	}
	
	public void editUploadedDocumentDetails(
			@Observes @CDIEvent(EDIT_RECEIVED_PHYSICAL_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.editUploadDocumentDetails(uploadDTO);
		
	}

	public void saveUploadedDocuments(
			@Observes @CDIEvent(SAVE_RECEIVED_PHYSICAL_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {		
		
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();		
		if(null != rodDTO)
		{
			UploadDocumentDTO uploadDto = rodDTO.getUploadDocumentsDTO();
			uploadDto.setUploadDocsList(rodDTO.getUploadDocsList());			
			createRodService.saveReceivedPhysicalDocumens(uploadDto);
			view.buildSuccessLayout();
		}
	}
	
	

}
