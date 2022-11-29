/**
 * 
 */
package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class UploadedDocumentsForOMP extends GEditableTable<UploadDocumentDTO> {
	
	//private List<String> errorMessages;
	
	private String presenterString;
	
	public UploadedDocumentsForOMP() {
		super(UploadedDocumentsForOMP.class);
		setUp();
	}

/*	public static final Object[] VISIBLE_COLUMNS = new Object[] { "sNo", "fileTypeValue","fileName","updatedOn" };*/

	private List<UploadDocumentDTO> deletedList = new ArrayList<UploadDocumentDTO>();
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	//Have a map for storing container value

	static {
		
		fieldMap.put("sNo", new TableFieldDTO("sNo",TextField.class, String.class, false));
		fieldMap.put("fileTypeValue", new TableFieldDTO("fileTypeValue",TextField.class, String.class, false));
		fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));
		fieldMap.put("updatedOn", new TableFieldDTO("updatedOn",TextField.class, String.class, false));
	}*/

	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}
	
	public void removeRow(UploadDocumentDTO uploadDocsDTO) {
		table.removeItem(uploadDocsDTO);
	}

	@Override
	public void initTable() {
	//	errorMessages = new ArrayList<String>();
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentDTO>(
				UploadDocumentDTO.class));
		 Object[] VISIBLE_COLUMNS = new Object[] { "sNo", "fileTypeValue","documentTypeValue","docReceivedDate","receivStatusValue","noOfItems","remarks","uploadedBy","updatedOn" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		//table.setWidth("80%");
		//Adding the height for procedure table.
		table.setHeight("140px");
		table.setWidth("90%");
		table.removeGeneratedColumn("view");
		table.addGeneratedColumn("view", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button editButton = new Button("View");
		    	editButton.setData(itemId);
		    	
		    	UploadDocumentDTO uploadDTOForEdit = (UploadDocumentDTO) itemId;
		    /*	if(null != uploadDTOForEdit && null != uploadDTOForEdit.getFileTypeValue() && ("Cashless Settlement Bill").equalsIgnoreCase(uploadDTOForEdit.getFileTypeValue()))
		    	{
		    		editButton.setEnabled(false);
		    	}
		    	else
		    	{
		    		editButton.setEnabled(true);
		    	}*/
		    	
		    	editButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						UploadDocumentDTO uploadDTOForEdit = (UploadDocumentDTO) itemId;
						if(uploadDTOForEdit!=null && uploadDTOForEdit.getRodKey()!=null){
							
							BPMClientContext bpmClientContext = new BPMClientContext();
							Long dummyno = 1l;
							Map<String,String> tokenInputs = new HashMap<String, String>();
							 tokenInputs.put("intimationNo", uploadDTOForEdit.getIntimationNo());
							 tokenInputs.put("ompdoc", dummyno.toString());
							 String intimationNoToken = null;
							  try {
								  intimationNoToken = createJWTTokenForClaimStatusPages(tokenInputs);
							  } catch (NoSuchAlgorithmException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  } catch (ParseException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  }
							  tokenInputs = null;
							String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
							/*Below code commented due to security reason
							String url = bpmClientContext.getGalaxyDMSUrl() + uploadDTOForEdit.getIntimationNo();*/
						//	getUI().getPage().open(url, "_blank");
							getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
						}
						/*
						Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);
						UploadDocumentDTO uploadDTO = (UploadDocumentDTO) itemId;

						uploadDTO.setIsEdit(true);

						if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
							fireViewEvent(OMPUploadDocumentsPresenter.OMP_EDIT_UPLOADED_DOCUMENTS, uploadDTO);
						else if (SHAConstants.ADD_ADDITIONAL_DOCUMENTS.equalsIgnoreCase(presenterString))
							fireViewEvent(OMPUploadDocumentsPresenter.OMP_EDIT_UPLOADED_DOCUMENTS, uploadDTO);

						
			        	Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);
			        */} 
			    });
		    	editButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		        return editButton;
		      }
		    });
		
		table.removeGeneratedColumn("delete");
		table.addGeneratedColumn("delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button deleteButton = new Button("Delete");
		    	deleteButton.setData(itemId);
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(final ClickEvent event) {

						ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
								"Are you sure you want to delete ?",
								"No", "Yes", new ConfirmDialog.Listener() {
						
									private static final long serialVersionUID = 1L;
									public void onClose(ConfirmDialog dialog) {
										if (dialog.isCanceled() && !dialog.isConfirmed()) {Object currentItemId = event.getButton().getData();
										
										UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO) currentItemId;
										uploadDocsDTO.setIsEdit(false);
										//BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) currentItemId;
										if(null != uploadDocsDTO.getDocSummaryKey())
										{
											//billEntryDetailsDTO.setDeletedFlag("Y");
											deletedList.add(uploadDocsDTO);
										}
										table.removeItem(currentItemId);
										tableSelectHandler(uploadDocsDTO);}
									}
								});
						
						dialog.setStyleName(Reindeer.WINDOW_BLACK);
			        } 
			    });
		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		    });
		
		//table.setPageLength(table.getItemIds().size());
	}
	
	public void initPresenter(String presenterString)
	{
		this.presenterString = presenterString;
	}

	@Override
	public String textBundlePrefixString() {
		return "omp-uploaded-documents-";
	}

	@Override
	protected void newRowAdded() {

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		
		Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();	
			
			fieldMap.put("sNo", new TableFieldDTO("sNo",TextField.class, String.class, false));
			fieldMap.put("fileTypeValue", new TableFieldDTO("fileTypeValue",TextField.class, String.class, false));
			fieldMap.put("documentTypeValue", new TableFieldDTO("documentTypeValue",TextField.class, String.class, false));
			fieldMap.put("docReceivedDate", new TableFieldDTO("docReceivedDate", DateField.class,Date.class, false));
			fieldMap.put("receivStatusValue", new TableFieldDTO("receivStatusValue",TextField.class, String.class, false));
			fieldMap.put("noOfItems", new TableFieldDTO("noOfItems",TextField.class, String.class, false));
			
			fieldMap.put("remarks", new TableFieldDTO("remarks",TextField.class, String.class, false,false,650,250));
			fieldMap.put("uploadedBy", new TableFieldDTO("uploadedBy",TextField.class, String.class, false));
			fieldMap.put("updatedOn", new TableFieldDTO("updatedOn",TextField.class, String.class, false));
		return fieldMap;
	}

	public void validateFields() {
		Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
		}

	}

	public void setReference(Map<String, Object> referenceData) {
		super.setReference(referenceData);
	}
	

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}
	
	/*protected void addProcedureNameChangeListener()
	{
		Item item = table.getItem("procedureNameValue");
		ComboBox cmb = (ComboBox)item.
		
		item.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != procedureTableObj)
				{
					String diagnosisValue = (String)event.getProperty().getValue();
					if(!procedureTableObj.diagnosisList.contains(diagnosisValue))
					{
						procedureTableObj.diagnosisList.add(diagnosisValue);
					}
				System.out.println("---the diagnosisListValue----"+procedureTableObj.diagnosisList);
				}
			}
			
			
		});
		
	}*/
	/*public boolean isValidProcedure()
	{
		boolean hasError = false;
		errorMessages.removeAll(getProcedureErrors());
		Collection<ProcedureDTO> itemIds = (Collection<ProcedureDTO>) table.getItemIds();
		for (ProcedureDTO bean : itemIds) {
			if(bean.getProcedureNameValue() == null || (null != bean.getProcedureNameValue() && bean.getProcedureNameValue().length() <= 0)) {
				hasError = true;
				errorMessages.add("Please Enter New Procedure Name");
			}
			
			if(bean.getProcedureCodeValue() == null || (null != bean.getProcedureCodeValue() && bean.getProcedureCodeValue().length() <= 0)) {
				hasError = true;
				errorMessages.add("Please Enter New Procedure Code");
			}
		}
		return !hasError;
	}*/
	
/*
	public List<String> getProcedureErrors() {
		return this.errorMessages;
	}
*/
	@Override
	public void tableSelectHandler(UploadDocumentDTO t) {
		// TODO Auto-generated method stub
		if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString)){
			fireViewEvent(OMPUploadDocumentsPresenter.OMP_DELETE_UPLOADED_DOCUMENTS,t);
		}else{
			fireViewEvent(OMPUploadDocumentsPresenter.OMP_DELETE_UPLOADED_DOCUMENTS,t);
		}
		
		
	}
	
	public List<UploadDocumentDTO> getDeletedDocumentList()
	{
		return deletedList;
	}
	
	public String createJWTTokenForClaimStatusPages(Map<String, String> userMap) throws NoSuchAlgorithmException, ParseException{	
		String token = "";	
		try {	
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");	
			keyGenerator.initialize(1024);	
		
			KeyPair kp = keyGenerator.genKeyPair();	
			RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();	
			RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();	
		
		
			JWSSigner signer = new RSASSASigner(privateKey);	
		
			JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();	
			claimsSet.issuer("galaxy");	
			if(userMap.get("intimationNo") != null){
				claimsSet.claim("intimationNo", userMap.get("intimationNo"));	
			}
			if(userMap.get("reimbursementkey") != null){
				claimsSet.claim("reimbursementkey", userMap.get("reimbursementkey"));
			}
			if(userMap.get("acknowledgementNo") !=null){
				claimsSet.claim("acknowledgementNo", userMap.get("acknowledgementNo"));
			}
			if(userMap.get("ompdoc") != null){
				claimsSet.claim("ompdoc", userMap.get("ompdoc"));
			}
			if(userMap.get("cashlessDoc") != null){
				claimsSet.claim("cashlessDoc", userMap.get("cashlessDoc"));
			}
			if(userMap.get("lumenKey") != null){
				claimsSet.claim("lumenKey", userMap.get("lumenKey"));
			}
			claimsSet.expirationTime(new Date(new Date().getTime() + 1000*60*10));	
		
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet.build());	
		
			signedJWT.sign(signer);	
			token = signedJWT.serialize();	
			signedJWT = SignedJWT.parse(token);	
		
			JWSVerifier verifier = new RSASSAVerifier(publicKey);	
				
			return token;	
		} catch (JOSEException ex) {	
				
		}	
		return null;	
		
	 }
	
}


