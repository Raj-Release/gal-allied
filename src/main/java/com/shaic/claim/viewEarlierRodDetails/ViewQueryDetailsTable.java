package com.shaic.claim.viewEarlierRodDetails;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.view.DiagnosisService;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.ReimbursementQuery;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewQueryDetailsTable extends GBaseTable<ViewQueryDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	////private static Window popup;
	
	@Inject
	private Instance<QueryDetailsViewUI> queryDetailsInstance;
	
	private QueryDetailsViewUI queryDetailsObj;
	
	@EJB
	private AcknowledgementDocumentsReceivedService documentReceivedService;
	
	private Claim claim;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private DiagnosisService diagnosisService;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","acknowledgementNo","rodNumber","diagnosis","queryRemarks","queryRaised"
		,"designation","queryRaisedDate","queryStatus"};*/

/*	public static final Object[] VISIBLE_COLUMN = new Object[] {"sno","acknowledgementNo","rodNumber","receivedFrom","billClassification"
		,"queryRaised","designation","queryRaisedDateStr","queryStatus"};*/

	/*public static final Object[] DRAFT_QUERY_DETAIL_VISIBLE_COLUMN = new Object[] {"select","serialNumber","acknowledgementNo","rodNumber","diagnosis","receivedFrom","billClassification"
		,"queryRaised"};*/
	
	/*public static final Object[] VIEW_QUERY_DETAILS_TABLE = new Object[]{"sno","hospitalName","hospitalCity","diagnosis","queryRemarks","queryRaised"
		,"designation","queryRaisedDate","queryStatus"};*/
	
	/*public static final Object[] VIEW_PA_QUERY_DETAILS_TABLE = new Object[]{"sno", "acknowledgementNo", "rodNumber", "diagnosis", "receivedFrom", "benefitCover", "queryType", "claimedAmt", "queryRaised","queryRaisedDate","queryStatus"};*/
	
	@PostConstruct 
	public void init(){
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<ViewQueryDTO>(
				ViewQueryDTO.class));
	}
	
	@Override
	public void initTable() {
		table.removeAllItems();
		 Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","acknowledgementNo","rodNumber","diagnosis","opnQryTyp","queryRemarks","queryRaised"
			,"designation","queryRaisedDate","queryStatus"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		generateViewDetailColumn();
		
	}
	
	private void queryDetails(ViewQueryDTO viewQueryDto){
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		ReimbursementQuery reimbursementQuery = documentReceivedService.getReimbursementQuery(viewQueryDto.getKey());
		ViewQueryDTO viewDetails = instance.getviewDetailsQuery(reimbursementQuery);
		
		
		if(reimbursementQuery != null){
			String billClassification = getBillClassification(reimbursementQuery.getReimbursement().getDocAcknowLedgement());
			viewDetails.setBillClassification(billClassification);
//			Reimbursement reimbursement = documentReceivedService.getReimbursement(reimbursementQuery.getReimbursement().getKey());
//			if(reimbursement != null){
			if(reimbursementQuery.getStatus() != null)
				viewDetails.setQueryStatus(reimbursementQuery.getStatus().getProcessValue());
//			}
		}
		
		viewDetails.setDiagnosis(viewQueryDto.getDiagnosis());
		
		
//		viewDetails.setQueryRaiseRole(viewQueryDto.getQueryRaiseRole());
//		viewDetails.setQueryRaised(viewQueryDto.getQueryRaised());
		
		if(viewDetails.getQueryRaisedDate()!=null){
			Date tempDate = SHAUtils.formatTimestamp(viewDetails.getQueryRaisedDate().toString());
			viewDetails.setQueryRaisedDateStr(SHAUtils.formatDate(tempDate));
		}
		
		if(viewDetails.getAdmissionDate()!=null){
			Date tempDate = SHAUtils.formatTimestamp(viewDetails.getAdmissionDate());
			viewDetails.setAdmissionDate(SHAUtils.formatDate(tempDate));
		}
		
		if(viewDetails.getQueryDraftedDate()!=null){
			Date tempDate = SHAUtils.formatTimestamp(viewDetails.getQueryDraftedDate());
			viewDetails.setQueryDraftedDate(SHAUtils.formatDate(tempDate));
		}
		
		if(viewDetails.getApprovedRejectedDate()!=null){
			Date tempDate = SHAUtils.formatTimestamp(viewDetails.getApprovedRejectedDate());
			viewDetails.setApprovedRejectedDate(SHAUtils.formatDate(tempDate));
		}
		
		if(viewDetails.getIntimationNo() != null){
			Long hospitalId = reimbursementQuery.getReimbursement().getClaim().getIntimation().getHospital();
			
			Hospitals hospitals = hospitalService.getHospitalById(hospitalId);
			if(hospitals != null){
			viewDetails.setHospitalName(hospitals.getName());
			viewDetails.setHospitalCity(hospitals.getCity());
			viewDetails.setHospitalType(hospitals.getHospitalTypeName());
			}
		}
		EarlierRodMapper.invalidate(instance);
		queryDetailsObj = queryDetailsInstance.get();
		queryDetailsObj.init(viewDetails);
	}
	
	public String getBillClassification(DocAcknowledgement docAcknowledgement){
		
		String classification="";
    	if(docAcknowledgement.getPreHospitalisationFlag() != null){
    		if(docAcknowledgement.getPreHospitalisationFlag().equals("Y")){
    			if(classification.equals("")){
    				classification ="Pre-Hospitalisation";
    			}
    			else{
    			classification =classification+","+"Pre-Hospitalisation";
    			}
    		}
    	}
    	if(docAcknowledgement.getHospitalisationFlag() != null){
    		if(docAcknowledgement.getHospitalisationFlag().equals("Y")){

    			if(classification.equals("")){
    				classification ="Hospitalisation";
    			}
    			else{
    			classification =classification+","+" Hospitalisation";
    			}
    		}
    	}
		if (docAcknowledgement.getPostHospitalisationFlag() != null) {

			if (docAcknowledgement.getPostHospitalisationFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Post-Hospitalisation";
				} else {
					classification = classification + ","
							+ " Post-Hospitalisation";
				}
			}
		}
		
		 if(docAcknowledgement.getHospitalCashFlag() != null){
				
				if (docAcknowledgement.getHospitalCashFlag().equals("Y")) {

					if (classification.equals("")) {
						classification = "Add on Benefits (Hospital cash)";
					} else {
						classification = classification + ","
								+ "Add on Benefits (Hospital cash)";
					}
				}
			}
	         
	         if(docAcknowledgement.getLumpsumAmountFlag() != null){
	 			
	 			if (docAcknowledgement.getLumpsumAmountFlag().equals("Y")) {

	 				if (classification.equals("")) {
	 					classification = "Lumpsum Amount";
	 				} else {
	 					classification = classification + ","
	 							+ "Lumpsum Amount";
	 				}
	 			}
	 		}
	         
	         if(docAcknowledgement.getPartialHospitalisationFlag() != null){
	  			
	  			if (docAcknowledgement.getPartialHospitalisationFlag().equals("Y")) {

	  				if (classification.equals("")) {
	  					classification = "Partial Hospitalisation";
	  				} else {
	  					classification = classification + ","
	  							+ "Partial Hospitalisation";
	  				}
	  			}
	  		}
	         
	         if(docAcknowledgement.getOtherBenefitsFlag() != null){
		       		if(docAcknowledgement.getOtherBenefitsFlag().equals("Y")){

		       			if(classification.equals("")){
		       				classification ="Other Benefit";
		       			}
		       			else{
		       			classification =classification+","+" Other Benefit";
		       			}
		       		}
		       	}
	         
	         return classification;
	}
	
	public void setClaim(Claim claim){
		this.claim = claim;
	}

	@Override
	public void tableSelectHandler(ViewQueryDTO t) {		
		table.select(t);		
	}
	
	public void setQueryDetaqilsVisibleColumns(){
	
		Object[] DRAFT_QUERY_DETAIL_VISIBLE_COLUMN = new Object[] {"select","serialNumber","acknowledgementNo","rodNumber","diagnosis","receivedFrom","billClassification"
			,"queryRaised"};
		table.setVisibleColumns(DRAFT_QUERY_DETAIL_VISIBLE_COLUMN);
		table.removeGeneratedColumn("select");
		table.addGeneratedColumn("select",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						
						return ((ViewQueryDTO)itemId).getSelect();
					}
		});
		table.removeGeneratedColumn("Query Raised Date");
		table.addGeneratedColumn("Query Raised Date",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
		                if(((ViewQueryDTO)itemId).getQueryRaisedDate() != null){
						return new SimpleDateFormat("dd/MM/yyyy").format(((ViewQueryDTO)itemId).getQueryRaisedDate());
		                }else{
		                	return null;
		                }
						
					}
		});
		table.removeGeneratedColumn("Query Status");
		table.addGeneratedColumn("Query Status",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
		
						return ((ViewQueryDTO)itemId).getQueryStatus();
						
					}
		});				
		table.setColumnHeader("select", "Select");
		table.setColumnHeader("receivedFrom", "Document Received From");
		table.setColumnHeader("billClassification", "Bill Classification");
		table.setColumnHeader("queryStatus", "Query status");
		table.setColumnHeader("viewDetails", "View Details");
		generateViewDetailColumn();
		table.setSelectable(true);
		
	}
	
	private void generateViewDetailColumn(){
		table.removeAllItems();
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		
		table.removeGeneratedColumn("viewDetails");
		table.addGeneratedColumn("viewDetails",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						Button button = new Button("View Details");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								Window popup = new com.vaadin.ui.Window();
								ViewQueryDTO viewQueryDto = (ViewQueryDTO)itemId;
								if(viewQueryDto.getKey() != null){					
									queryDetails(viewQueryDto);	
								}
								popup.setCaption("View Query Details");
								popup.setWidth("75%");
								popup.setHeight("85%");
								popup.setContent(queryDetailsObj);
								popup.setClosable(true);
								popup.center();
								popup.setResizable(false);
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
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
		});
	}
	public void setViewQueryDetailsColumn(){
		table.removeAllItems();
	 Object[] VIEW_QUERY_DETAILS_TABLE = new Object[]{"sno","hospitalName","hospitalCity","diagnosis","opnQryTyp","queryRemarks","queryRaised"
			,"designation","queryRaisedDate","queryStatus"};
		table.setVisibleColumns(VIEW_QUERY_DETAILS_TABLE);
	}
	
	public void setViewPAQueryDetialsColumn(){
		table.removeAllItems();
		Object[] VIEW_PA_QUERY_DETAILS_TABLE = new Object[]{"sno", "acknowledgementNo", "rodNumber", "diagnosis", "receivedFrom", "benefitCover", "queryType", 
			"claimedAmt", "queryRaised","queryRaisedDate","queryStatus"};
		table.setVisibleColumns(VIEW_PA_QUERY_DETAILS_TABLE);
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("benefitCover", "Benefit / Cover");
		table.setColumnHeader("queryType", "Query Type");
		table.setColumnHeader("claimedAmt", "Claimed Amount");
		table.setColumnHeader("receivedFrom", "Document Received From");
		
		table.removeGeneratedColumn("viewDetails");
		table.addGeneratedColumn("viewDetails",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						Button button = new Button("View Details");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								Window popup = new com.vaadin.ui.Window();
								ViewQueryDTO viewQueryDto = (ViewQueryDTO)itemId;
								if(viewQueryDto.getKey() != null){					
									queryDetails(viewQueryDto);	
								}
								popup.setCaption("View Query Details");
								popup.setWidth("75%");
								popup.setHeight("85%");
								popup.setContent(queryDetailsObj);
								popup.setClosable(true);
								popup.center();
								popup.setResizable(false);
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
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
		});
		
		table.setColumnHeader("viewDetails", "View Details");
	}
	
	
	
	public void setViewQueryVisibleColumn(){
		table.removeAllItems();
		 Object[] VISIBLE_COLUMN = new Object[] {"sno","acknowledgementNo","rodNumber","receivedFrom","billClassification"
			,"queryRaised","designation","queryRaisedDateStr","queryStatus"};
		table.setVisibleColumns(VISIBLE_COLUMN);
		
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("queryRaisedDateStr", "Query Raised Date");
		table.setColumnHeader("receivedFrom", "Document Recieved");
		table.setColumnHeader("billClassification", "Bill Classification");
		
		table.removeGeneratedColumn("viewDetails");
		table.addGeneratedColumn("viewDetails",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						Button button = new Button("View Details");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								Window popup = new com.vaadin.ui.Window();
								ViewQueryDTO viewQueryDto = (ViewQueryDTO)itemId;
								if(viewQueryDto.getKey() != null){					
									queryDetails(viewQueryDto);	
								}
								popup.setCaption("View Query Details");
								popup.setWidth("75%");
								popup.setHeight("85%");
								popup.setContent(queryDetailsObj);
								popup.setClosable(true);
								popup.center();
								popup.setResizable(false);
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
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
		});
		
		table.setColumnHeader("viewDetails", "View Details");
	}

	@Override
	public String textBundlePrefixString() {
		return "view-query-details-table-";
	}
	
	public void clearQueryDetailsPopup() {
    	if(this.table != null){
    		this.table.clear();
    	}
	}
	

}
