package com.shaic.claim.OMPViewDetails.view;

import java.util.List;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.DocumentCheckListMaster;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OMPViewPreviousClaimDetailTable extends GBaseTable<PreviousClaimsTableDTO>{
	
	@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME",  type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;
	
	
	private String intimationNo;
	
	@EJB
	private OMPIntimationService intimationService;
	
	@EJB
	private OMPClaimService claimService;

	private static final long serialVersionUID = -7771166917702155656L;
	private static final Object[] NATURAL_COL_ORDER= new Object[]{"policyNumber","policyYear","claimNumber","claimType","intimationNumber","insuredPatientName"
			,"ailment","admissionDate","","claimStatus","claimAmount","approvedAmount","hospitalName"};
	
	

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void initTable() {
		
		table.removeAllItems();
		table.setWidth("100%");
		//Vaadin8-setImmediate() table.setImmediate(false);
        table.setContainerDataSource(new BeanItemContainer<PreviousClaimsTableDTO>(PreviousClaimsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);	
//		table.setColumnHeader("policyNumber","Policy Number");
//		table.setColumnHeader("claimNumber", "Claim Number");
//		table.setColumnHeader("claimStatus", "Claim Status");
//		table.setColumnHeader("claimAmount", "Claim Amount");
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		table.setFooterVisible(true);
		
		
		table.removeGeneratedColumn("viewClaimStatus");
		table.addGeneratedColumn("viewClaimStatus",
				new Table.ColumnGenerator() {

			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {
				Button button = new Button("View Claim Status");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						
						PreviousClaimsTableDTO previousClaimDto = (PreviousClaimsTableDTO)itemId;
                        if(previousClaimDto.getIntimation() != null){
                        	
                        	getClaimStatus(previousClaimDto.getIntimation().getIntimationId());
                        	
                        }
					}

				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});


table.removeGeneratedColumn("viewDocumentDetailss");
table.addGeneratedColumn("viewDocumentDetailss",
		new Table.ColumnGenerator() {

			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {
				Button button = new Button("View Documents");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						
						
						PreviousClaimsTableDTO previousClaimDto = (PreviousClaimsTableDTO)itemId;
                        if(previousClaimDto.getIntimation() != null){
                        	
//                        	getviewdocumentDetails(previousClaimDto.getIntimation().getIntimationId());
                        	
                        }

							}
						});

						
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});


}
	
	public void getClaimStatus(String intimationNo){
		if(intimationNo!=null){
		Long intimationKey = intimationService.getIntimationByNo(
				intimationNo).getKey();
		OMPClaim claim = intimationService
				.getClaimforIntimation(intimationKey);
		String claimNo = claim.getClaimId();
		Long claimKey = claim.getKey();
//		Long oPhealthCheckupKey = processOPRequestService
//				.getOpHealthByClaimKey(claimKey).getKey();
//		
//		List<OPHCDetails> oPHCDetailsList= processOPRequestService.getOpHCDetails(oPhealthCheckupKey);
//		oPClaimStatus.init(intimationNo,oPHCDetailsList);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Status of Claim No"+"  "+claimNo);
		popup.setWidth("75%");
		popup.setHeight("75%");
//		popup.setContent(oPClaimStatus);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		}else{
			getErrorMessage("Claim Status is not available at Register Claim stage");
		}
		
		
	}
	


	public void getErrorMessage(String eMsg) {

		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
		
//	public DocumentCheckListDTO getviewdocumentDetails(String intimationNo) {
//		
//		if(intimationNo != null){
//		// TODO Auto-generated method stub
//		Long claimkey=claimService.getClaimsByIntimationNumber(intimationNo).getKey();
//		OPHealthCheckup opHealthCheckup=processOPRequestService.getOpHealthByClaimKey(claimkey);
//		if(opHealthCheckup.getKey() !=null){
//		List<OPDocumentList> opDocumentList = processOPRequestService.getOPDocumentListByClaimKey(opHealthCheckup.getKey());
//		}
//		
//		
//		 
//		//List<OPHealthCheckup> opHealthACheckupList=new ArrayList<OPHealthCheckup>();
//		List<ViewDocumentDetailsDTO> viewDocumentDetailsDTOsList=new ArrayList<ViewDocumentDetailsDTO>();
//		
//		ViewDocumentDetailsDTO viewDocumentDetailsDTO=new ViewDocumentDetailsDTO();
//		
//		MastersValue documentReceivedFromId = opHealthCheckup.getDocumentReceivedFromId();
//		
//		MastersValue modeOfReceipt=opHealthCheckup.getModeOfReceipt();
//		
//		SelectValue selecDocumentReceivedFromId=new SelectValue();
//		selecDocumentReceivedFromId.setValue(documentReceivedFromId.getValue());
//		viewDocumentDetailsDTO.setReceivedFrom(selecDocumentReceivedFromId);
//		SelectValue selectModeOfReciept2=new SelectValue();
//		selectModeOfReciept2.setValue(modeOfReceipt.getValue());
//		viewDocumentDetailsDTO.setModeOfReceipt(selectModeOfReciept2);
//		viewDocumentDetailsDTO.setDocumentReceivedDate(opHealthCheckup.getDocumentReceivedDate());
//		viewDocumentDetailsDTO.setPersonemailID(opHealthCheckup.getPersonEmailId());
//		viewDocumentDetailsDTO.setPersonContactNumber(opHealthCheckup.getPersonContactNumber());
//		viewDocumentDetailsDTO.setAdditionalRemarks(opHealthCheckup.getAdditionalRemarks());
//		viewDocumentDetailsDTOsList.add(viewDocumentDetailsDTO);
//		
//			
//			Query query = entityManager.createNamedQuery("OPDocumentList.findByHealthCheckupKey");
//			query.setParameter("healthCheckupKey", opHealthCheckup.getKey());
//			@SuppressWarnings("unchecked")
//			List<OPDocumentList> resultList =  query.getResultList();
//			List<DocumentCheckListDTO> documentCheckListDTOList = new ArrayList<DocumentCheckListDTO>();
//			int i=1;
//			if(resultList != null && !resultList.isEmpty()){
//			
//			for(OPDocumentList opDocument : resultList){
//			
//				//entityManager.refresh(opDocument);
//				DocumentCheckListDTO dto = new DocumentCheckListDTO();
//				dto.setSlNo(i);
//			
//			SelectValue receiveStatus = new SelectValue(opDocument.getReceivedStatusId().getKey(), opDocument.getReceivedStatusId().getValue());
//			dto.setReceivedStatus(receiveStatus);
//			dto.setNoOfDocuments(opDocument.getNumberOfDocuments());
//			dto.setRemarks(opDocument.getRemarks());
//			Long documentTypeId = opDocument.getDocumentTypeId();
//			
//			DocumentCheckListMaster documentCheckListMaster = getDocumentCheckListMaster(documentTypeId);
//			if(documentCheckListMaster != null){
//				dto.setValue(documentCheckListMaster.getValue());
//				if(documentCheckListMaster.getMandatoryDocFlag() != null && documentCheckListMaster.getMandatoryDocFlag().equalsIgnoreCase("Y")){
//					dto.setMandatoryDocFlag("Yes");
//				}else{
//					dto.setMandatoryDocFlag("No");
//				}
//				
//				dto.setRequiredDocType(documentCheckListMaster.getRequiredDocType());
//				
//			}
//			documentCheckListDTOList.add(dto);
//			i++;
//			
//			}
//			
//			}
//			
//		
//		if(null!=viewDocumentDetailsDTOsList && null!=documentCheckListDTOList){
////		viewDocumentDetailsPage.init(viewDocumentDetailsDTOsList,documentCheckListDTOList);
//		}
//		popup = new com.vaadin.ui.Window();
//		popup.setCaption("View Document Details");
//		popup.setWidth("75%");
//		popup.setHeight("85%");
////		popup.setContent( viewDocumentDetailsPage);
//		popup.setClosable(true);
//		popup.center();
//		popup.setResizable(false);
//		popup.addCloseListener(new Window.CloseListener() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void windowClose(CloseEvent e) {
//				System.out.println("Close listener called");
//			}
//		});
//
//		popup.setModal(true);
//		UI.getCurrent().addWindow(popup);
//		}
//		else {
//			getErrorMessage("DocumentDetails is not available in Register Claim");
//		}
//		
//		return null;
//	}
		
	
	public DocumentCheckListMaster getDocumentCheckListMaster(Long key){
		
	Query query = entityManager.createNamedQuery("DocumentCheckListMaster.findByKey");
	query.setParameter("primaryKey", key);
	
	List<DocumentCheckListMaster> resultList =  (List<DocumentCheckListMaster>)query.getResultList();
	
	if(resultList != null && ! resultList.isEmpty()){
		return resultList.get(0);
	}
	return null;
}
	
	@Override
	public void tableSelectHandler(PreviousClaimsTableDTO vto) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public String textBundlePrefixString() {
		
		return "omppreviousclaim-details-";
	}
	
	protected void setTableSize(){
		table.setPageLength(table.size());
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
}
