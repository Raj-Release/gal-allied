package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

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
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.srikanthp
 *
 */
@ViewInterface(OMPUploadDocumentsView.class)
public class OMPUploadDocumentsPresenter  extends AbstractMVPPresenter<OMPUploadDocumentsView>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String OMP_UPLOAD_DOC_SETUP_DROPDOWN_VALUES = "omp_upload_doc_setup_dropdown_values";
	
	public static final String OMP_SUBMIT_UPLOADED_DOCUMENTS = "omp_entry_submit_uploaded_documents";
	
	public static final String OMP_DELETE_UPLOADED_DOCUMENTS = "omp_entry_delete_uploaded_documents";	
	
	public static final String OMP_EDIT_UPLOADED_DOCUMENTS = "omp_entry_edit_uploaded_documents";
	
	public static final String OMP_SAVE_UPLOADED_DOCUMENTS = "omp_entry_edit_uploaded_documents";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private OMPProcessRODBillEntryService rodBillEntryService;
	
	
	/*@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;*/

	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setUpDropDownValues(
			@Observes @CDIEvent(OMP_UPLOAD_DOC_SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {
		//Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		/*referenceDataMap.put("fileType", masterService
				.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE));
		view.setUpDropDownValues(referenceDataMap);*/

		OMPClaimCalculationViewTableDTO bean = (OMPClaimCalculationViewTableDTO) parameters.getPrimaryParameter();
		
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
						if((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(bean.getReceiptOfDocumentsDTO().getClaimDTO().getClaimTypeValue()) && (SHAConstants.YES_FLAG).equalsIgnoreCase(bean.getReceiptOfDocumentsDTO().getDocumentDetails().getHospitalizationFlag()))
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
				UploadDocumentDTO uploadDocumentsDTO = bean.getReceiptOfDocumentsDTO().getUploadDocumentsDTO();
				uploadDocumentsDTO.setFileTypeContainer(billTypeContainer);
			}
			referenceDataMap.put("fileType", billTypeContainer);
		}
		/*else
		{
			referenceDataMap.put("fileType", beanContainer);
		}*/
		//view.setUpDropDownValues(referenceDataMap);
		
		//view.setUpDropDownValues(referenceDataMap);
		
		
		
		}
	
	
	public void submitUploadedDocuments(
			@Observes @CDIEvent(OMP_SUBMIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		//ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();
		//List<UploadDocumentDTO> uploadDocLst = (List<UploadDocumentDTO>) parameters.getPrimaryParameter();
		UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
		//view.loadUploadedDocsTableValues(uploadDocLst);
		view.loadUploadedDocsTableValues(uploadDocDTO);
		
	}
	
	public void deleteUploadedDocumentDetails(
			@Observes @CDIEvent(OMP_DELETE_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.deleteUploadDocumentDetails(uploadDTO);
		
	}
	
	public void editUploadedDocumentDetails(
			@Observes @CDIEvent(OMP_EDIT_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDTO = (UploadDocumentDTO)parameters.getPrimaryParameter();
		view.editUploadDocumentDetails(uploadDTO);
		
	}
	
	public void saveUploadedDocuments(
			@Observes @CDIEvent(OMP_SAVE_UPLOADED_DOCUMENTS) final ParameterDTO parameters) {
		
		UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO) parameters.getPrimaryParameter();
		
		rodBillEntryService.submitSearchOrUploadDocumentsForAckNotReceived(uploadDocDTO);
		view.resetPage();
	}
}

