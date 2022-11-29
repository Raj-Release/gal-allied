package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.OMPClaimPayment;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPViewDetails.view.OMPViewClaimStatusMapper;
import com.shaic.claim.claimhistory.view.ompView.OMPEarlierRodMapper;
import com.shaic.claim.ompviewroddetails.OMPIntimationAndViewDetailsUI;
import com.shaic.claim.ompviewroddetails.OMPViewClaimStatusDto;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.HospitalService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPHospitals;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OMPDocumentDetailsViewTable extends GBaseTable<ViewDocumentDetailsDTO> {

	private static final long serialVersionUID = 1L;
	
	private Instance<OMPIntimationAndViewDetailsUI> rodIntimationDetailsInstance;
	
	private OMPIntimationAndViewDetailsUI rodIntimationDetailsObj;
	
	private List<ViewDocumentDetailsDTO> setTableList;
	
	@EJB
	private OMPIntimationService intimationService;
	
	@EJB
	private OMPClaimService claimSerivice;
	
	private ViewDocumentDetailsDTO viewDocumentDetailsDto;
	
	@EJB
	private OMPProcessRODBillEntryService reimbursementService;
	
	@EJB
	private HospitalService hospitalService;
	
	@Inject
	private Instance<OMPClaimCalculationViewTable> ompClaimCalcViewTableInstance;
	
	private OMPClaimCalculationViewTable ompClaimCalcViewTableObj;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sno","rodNumber","receivedFromValue","documentReceivedDate","modeOfReceipt",
		"eventCode","classification","subclassification","approvedAmount","status"};*/
	
	private Long claimKey;

	private Double currencyRate;
	
	private Long reimbKey;
	
	private WeakHashMap<String, Object> referenceDataMap;
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<ViewDocumentDetailsDTO>(
				ViewDocumentDetailsDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"sno","rodNumber","receivedFromValue","documentReceivedDate","modeOfReceipt",
			"eventCode","classification","subclassification","approvedAmount","status"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");

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

								ViewDocumentDetailsDTO tableDto = (ViewDocumentDetailsDTO)itemId;
								OMPDocAcknowledgement docAcknowledgment = reimbursementService.getDocAcknowledgementBasedOnKey(tableDto.getKey());
								
								Window popup = new com.vaadin.ui.Window();
								popup.setCaption("");
								popup.setWidth("75%");
								popup.setHeight("85%");
								rodIntimationDetailsObj = rodIntimationDetailsInstance.get();
								
								OMPClaim claim = claimSerivice.getClaimByKey(claimKey);
								OMPIntimation intimation = intimationService.getIntimationByKey(claim.getIntimation().getKey());
								ViewClaimStatusDTO intimationDetails = OMPEarlierRodMapper.getInstance().getViewClaimStatusDto(intimation);
								OMPViewClaimStatusDto claimStatusDetails = OMPViewClaimStatusMapper.getInstance().getOMPViewClaimStatusDto(intimationDetails);
								OMPHospitals hospitals = hospitalService.getOMPHospitalById(intimation.getHospital());
								getHospitalDetails(intimationDetails, hospitals);
								intimationDetails.setClaimKey(claim.getKey());
								OMPReimbursement ompReimbursement = reimbursementService.getLatestReimbursementDetailsByclaimKey(claim.getKey());
								intimationDetails.setReceiptOfDocumentValues(setTableList);
								OMPClaimPayment setPaymentDetails = setPaymentDetails(intimationDetails,claim);
								rodIntimationDetailsObj.init(claimStatusDetails, ompReimbursement != null ? ompReimbursement.getKey() : 0l);
								popup.setContent(rodIntimationDetailsObj);
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
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	//Vaadin8-setImmediate() button.setImmediate(true);
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		table.removeGeneratedColumn("viewDocument");
		table.addGeneratedColumn("viewDocument",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, Object itemId,
							Object columnId) {
						Button button = new Button("View Documents");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								
								OMPClaim claim = claimSerivice.getClaimByKey(claimKey);
								getViewDocument(claim.getIntimation().getIntimationId());
								
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
		table.removeGeneratedColumn("viewBillingWorksheet");
		table.addGeneratedColumn("viewBillingWorksheet", new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						Button button = new Button("View Billing Worksheet");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								Window popup = new com.vaadin.ui.Window();
								
								
								ompClaimCalcViewTableObj =  ompClaimCalcViewTableInstance.get();
								OMPClaimProcessorDTO bean = new OMPClaimProcessorDTO();
								bean.setCurrencyRate(currencyRate);
								ompClaimCalcViewTableObj.init(bean);
								ompClaimCalcViewTableObj.setReferenceData(referenceDataMap);
								reimbursementService.getOMPBenefitCoverByKey(reimbKey);
								
								ompClaimCalcViewTableObj.setTableList(bean.getClaimCalculationViewTable());
								ompClaimCalcViewTableObj.setEnabled(false);
								popup.setCaption("Billing Worksheet");
								popup.setWidth("75%");
								popup.setHeight("90%");
								popup.setClosable(true);
								popup.setContent(ompClaimCalcViewTableObj);
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
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
		table.removeGeneratedColumn("documentReceivedDate");
		table.addGeneratedColumn("documentReceivedDate", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 
		    	  ViewDocumentDetailsDTO documentDTO = (ViewDocumentDetailsDTO)itemId;
		    	  
		    	  String formatDate = SHAUtils.formatDate(documentDTO.getDocumentReceivedDate());
		    	  return formatDate;
		      }
		});
		
		
	}

	@Override
	public void tableSelectHandler(ViewDocumentDetailsDTO t) {
		
		
	}
	
	public void setClaimKey(Long claimKey){
		
		this.claimKey = claimKey;
		
	}
	public void setCurrRate(Double currRate){
		this.currencyRate = currRate;
	}
	
	public void setReferenceData(WeakHashMap<String, Object> referenceData){
		this.referenceDataMap = referenceData;
	}
	
	public void setTableValues(List<ViewDocumentDetailsDTO> setTableList){
		
		this.setTableList = setTableList;
		
	}
	
	public OMPClaimPayment setPaymentDetails(ViewClaimStatusDTO bean,
			OMPClaim claim) {

		try {
			OMPClaimPayment reimbursementForPayment = null;
			
			OMPReimbursement settledReimbursement = reimbursementService.getLatestReimbursementDetailsByclaimKey(claim.getKey());
			if(settledReimbursement != null){
				
//			     reimbursementForPayment = reimbursementService.getClaimPaymentByRodNumber(settledReimbursement.getRodNumber());
				
			}else{
				reimbursementForPayment = reimbursementService
						.getRimbursementForPayment(claim.getClaimId());
			}
			if(reimbursementForPayment != null){
			bean.setBankName(reimbursementForPayment.getBankName());
			bean.setTypeOfPayment(reimbursementForPayment.getPaymentType().getValue());
			bean.setAccountName(reimbursementForPayment.getPayeeName());
			bean.setBranchName(reimbursementForPayment.getBranchName());
			bean.setChequeNumber(reimbursementForPayment.getChequeDdNumber());
			if(reimbursementForPayment.getPaymentType() != null && reimbursementForPayment.getPaymentType().getValue().equalsIgnoreCase(SHAConstants.NEFT_TYPE)){
				
				if(reimbursementForPayment.getChequeDdDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDdDate());
					bean.setNeftDate(formatDate);
				}
			 bean.setChequeNumber(null);
			}else{
				if(reimbursementForPayment.getChequeDdDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDdDate());
					bean.setChequeDate(formatDate);
				}
			}			

			return reimbursementForPayment;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
	
//  public ViewTmpReimbursement setPaymentDetailsfromTmpReimbursement(ViewClaimStatusDTO bean,Long claimKey){
//		
//		try{
//		Reimbursement reimbursementForPayment = reimbursementService.getRimbursementForPayment(claimKey);
//		Long bankId = reimbursementForPayment.getBankId();
//		bankId = 8l;
//		if(bankId != null){
//			BankMaster bankMaster = reimbursementService.getBankDetails(bankId);
//			if(bankMaster != null){
//				bean.setBankName(bankMaster.getBankName());
//				if (reimbursementForPayment.getPaymentModeId() != null) {
//					if (reimbursementForPayment.getPaymentModeId().equals(
//							ReferenceTable.PAYMENT_MODE_CHEQUE_DD)) {
//						bean.setTypeOfPayment("Cheque DD");
//					} else {
//						bean.setTypeOfPayment("Bank Transfer");
//					}
//				}
//				bean.setAccountName(reimbursementForPayment.getPayeeName());
//				bean.setBranchName(bankMaster.getBankName());
//			}
//			
//		}
//		return reimbursementForPayment;
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return null;
//		
//	}
	
	
	
	private void getHospitalDetails(
			ViewClaimStatusDTO intimationDetails,
			OMPHospitals hospitals) {
		if(hospitals != null){
			ViewClaimStatusDTO hospitalDetails = OMPEarlierRodMapper.getInstance().gethospitalDetails(hospitals);
			intimationDetails.setState(hospitalDetails.getState());
			intimationDetails.setCity(hospitalDetails.getCity());
			intimationDetails.setArea(hospitalDetails.getArea());
			intimationDetails.setHospitalAddress(hospitals.getAddress());
			intimationDetails.setHospitalName(hospitalDetails.getHospitalName());
			intimationDetails.setHospitalIrdaCode(hospitalDetails.getHospitalIrdaCode());
			intimationDetails.setHospitalInternalCode(hospitalDetails.getHospitalInternalCode());
		}
	}

	@Override
	public String textBundlePrefixString() {
		
		return "omp-earlier-rod-view-details-";
	}
	
	public void setRowColor(Long key){
		ArrayList<Object> itemIds = new ArrayList<Object>(table.getItemIds()); 
		final Object selectedRowId = getSelectedRowId(itemIds , key);
		System.out.print(";;;;;;;;;;;;;;;;;; selected id = "  + selectedRowId);		
		table.setCellStyleGenerator(new CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				viewDocumentDetailsDto = (ViewDocumentDetailsDTO)selectedRowId;
				if(viewDocumentDetailsDto != null){
				long key1 = viewDocumentDetailsDto.getKey();
				viewDocumentDetailsDto = (ViewDocumentDetailsDTO)itemId;
				long key2 = viewDocumentDetailsDto.getKey();
				if(key1==key2){
				return "select";
				}else{
					return "none";
				}
			
			}
				else{
					return "none";
				}
			}
	
		});
		
	}

	private Object getSelectedRowId( ArrayList<Object> ids,Long key){
		
		for(Object id:ids){
			viewDocumentDetailsDto = (ViewDocumentDetailsDTO)id;
			Long key1 = new Long(viewDocumentDetailsDto.getKey());
			if(key1.equals(key)){
			 return id;
			}
		}
		
		return null;
		
	}
	
	public void getViewDocument(String intimationNo) {

		OMPIntimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		String strPolicyNo = intimation.getPolicy().getPolicyNumber();
		//String strPolicyNo = "P/181219/01/2014/001945";
		getViewDocumentByPolicyNo(strPolicyNo);

	//	UI.getCurrent().addWindow(viewDocuments);
		
	}
	
	public void getViewDocumentByPolicyNo(String strPolicyNo) {
		VerticalLayout vLayout = new VerticalLayout();
		String strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
		/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
		
		String dmsToken = intimationService.createDMSToken(strPolicyNo);
		getUI().getPage().open(strDmsViewURL+dmsToken, "_blank",1200,650,BorderStyle.NONE);
		/*BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL+dmsToken));
		//browserFrame.setWidth("600px");
		//browserFrame.setHeight("400px");
		browserFrame.setSizeFull();
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
		vLayout.setSizeFull();
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("");
		popup.setWidth("100%");
		popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);*/
	}

}
