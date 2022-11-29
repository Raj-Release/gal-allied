package com.shaic.claims.reibursement.addaditionaldocuments;

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
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.srikanthp
 *
 */
@ViewInterface(UploadDocumentsView.class)
public class UploadDocumentsPresenter  extends AbstractMVPPresenter<UploadDocumentsView>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static final String BILL_ENTRY_UPLOAD_DOC_SETUP_DROPDOWN_VALUES = "bill_entry_upload_doc_setup_dropdown_values_add_additional_documents";
	
	public static final String BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS = "bill_entry_submit_uploaded_documents_add_additional_documents";
	
	public static final String BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS = "bill_entry_delete_uploaded_documents_add_additional_documents";	
	
	public static final String BILL_ENTRY_EDIT_UPLOADED_DOCUMENTS = "bill_entry_edit_uploaded_documents_add_additional_documents";
	
	@EJB
	private MasterService masterService;
	
	/*@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;*/

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setUpDropDownValues(
			@Observes @CDIEvent(BILL_ENTRY_UPLOAD_DOC_SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {		

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
				BeanItemContainer<SelectValue> tempBeanContainer = masterService.getSelectValueContainer(ReferenceTable.INVESTIGATION_CLAIM_REPOPT_TABLE_FILE_TYPE);
				List<SelectValue> tempSelectValueList = tempBeanContainer.getItemIds();
				if(null != tempSelectValueList && !tempSelectValueList.isEmpty()) {
					for (SelectValue tempSelectValue : tempSelectValueList) {
						finalBillTypeList.add(tempSelectValue);
					}
				}
				billTypeContainer.addAll(finalBillTypeList);
				billTypeContainer.sort(new Object[] { "value" }, new boolean[] { true });
			}
			referenceDataMap.put("fileType", billTypeContainer);
		}
		
		view.setUpDropDownValues(referenceDataMap);		
		
		}
	
	
	public void submitUploadedDocuments(
			@Observes @CDIEvent(BILL_ENTRY_SUBMIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {	
	
		UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();		
		view.loadUploadedDocsTableValues(uploadDocDTO);
		
	}
	
	public void deleteUploadedDocumentDetails(
			@Observes @CDIEvent(BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.deleteUploadDocumentDetails(uploadDTO);
		
	}
	
	public void editUploadedDocumentDetails(
			@Observes @CDIEvent(BILL_ENTRY_EDIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.editUploadDocumentDetails(uploadDTO);
		
	}
}

