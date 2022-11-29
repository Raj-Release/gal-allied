package com.shaic.claim.viewEarlierRodDetails;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewAcknowledgementPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPAacknowledgementPage;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpReimbursement;
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


public class ViewAcknowledgementTable extends GBaseTable<ViewDocumentDetailsDTO> {

	private static final long serialVersionUID = 1L;
	
	////private static Window popup;

	
	private List<ViewDocumentDetailsDTO> setTableList;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private ClaimService claimSerivice;
	
	@EJB
	private AcknowledgementDocumentsReceivedService acknowledgementService;
	
	private ViewDocumentDetailsDTO viewDocumentDetailsDto;
	
	@Inject
	private ViewAcknowledgementPage viewAcknowledgementPage;
	
	@Inject
	private ViewPAacknowledgementPage viewPAacknowledgementPage;
	
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;

	
	@EJB
	private HospitalService hospitalService;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sno","acknowledgeNumber","receivedFromValue","documentReceivedDate","modeOfReceipt"
		,"billClassification"};*/
	
	/*public static final Object[] NATURAL_COL_ORDER_PA = new Object[] {"sno","acknowledgeNumber","receivedFromValue","documentReceivedDate","modeOfReceipt"
		,"benefits"};*/
	
	private Long claimKey;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	
	public void  setPAColumnsForACK()
	{
		Object[] NATURAL_COL_ORDER_PA = new Object[] {"sno","acknowledgeNumber","receivedFromValue","documentReceivedDate","modeOfReceipt"
			,"benefits"};
		table.setVisibleColumns(NATURAL_COL_ORDER_PA);
		table.setColumnHeader("benefits", "Benefit/Cover");
		
		table.removeGeneratedColumn("viewAcknowledgement");
		table.addGeneratedColumn("viewAcknowledgement",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, Object itemId,
							Object columnId) {
						
						final ViewDocumentDetailsDTO documentDTO = (ViewDocumentDetailsDTO)itemId;
						
						Button button = new Button("View Acknowledgement");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								
								DocAcknowledgement docAcknowledgementBasedOnKey = acknowledgementService.getDocAcknowledgementBasedOnKey(documentDTO.getKey());
								
								if(docAcknowledgementBasedOnKey != null){
									viewPAacknowledgementPage.init(docAcknowledgementBasedOnKey.getClaim().getIntimation(),
											docAcknowledgementBasedOnKey.getKey());
									
									Window popup = new com.vaadin.ui.Window();
									popup.setCaption("View Acknowledgement Details");
									popup.setWidth("75%");
									popup.setHeight("90%");
									popup.setContent(viewPAacknowledgementPage);
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
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
	    table.setColumnHeader("viewAcknowledgement", "View Acknowledgement");
		

		
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
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<ViewDocumentDetailsDTO>(
				ViewDocumentDetailsDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"sno","acknowledgeNumber","receivedFromValue","documentReceivedDate","modeOfReceipt"
			,"billClassification"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");

		
		table.removeGeneratedColumn("viewAcknowledgement");
		table.addGeneratedColumn("viewAcknowledgement",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, Object itemId,
							Object columnId) {
						
						final ViewDocumentDetailsDTO documentDTO = (ViewDocumentDetailsDTO)itemId;
						
						Button button = new Button("View Acknowledgement");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								
								DocAcknowledgement docAcknowledgementBasedOnKey = acknowledgementService.getDocAcknowledgementBasedOnKey(documentDTO.getKey());
								
								if(docAcknowledgementBasedOnKey != null){
									viewAcknowledgementPage.init(docAcknowledgementBasedOnKey.getClaim().getIntimation(),
											docAcknowledgementBasedOnKey.getKey());
									
									Window popup = new com.vaadin.ui.Window();
									popup.setCaption("View Acknowledgement Details");
									popup.setWidth("75%");
									popup.setHeight("90%");
									popup.setContent(viewAcknowledgementPage);
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
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
	    table.setColumnHeader("viewAcknowledgement", "View Acknowledgement");
		

		
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
	
	public void setTableValues(List<ViewDocumentDetailsDTO> setTableList){
		
		this.setTableList = setTableList;
		
	}
	
	public ViewTmpClaimPayment setPaymentDetails(ViewClaimStatusDTO bean,
			ViewTmpClaim claim) {

		try {
			ViewTmpClaimPayment reimbursementForPayment = null;
			
			ViewTmpReimbursement settledReimbursement = reimbursementService.getLatestViewTmpReimbursementSettlementDetails(claim.getKey());
			if(settledReimbursement != null){
				
			     reimbursementForPayment = reimbursementService.getClaimPaymentByRodNumber(settledReimbursement.getRodNumber());
				
			}else{
				reimbursementForPayment = reimbursementService
						.getRimbursementForPayment(claim.getClaimId());
			}
			if(reimbursementForPayment != null){
			bean.setBankName(reimbursementForPayment.getBankName());
			bean.setTypeOfPayment(reimbursementForPayment.getPaymentType());
			bean.setAccountName(reimbursementForPayment.getAccountNumber());
			bean.setBranchName(reimbursementForPayment.getBranchName());
			bean.setChequeNumber(reimbursementForPayment.getChequeDDNumber());
			if(reimbursementForPayment.getPaymentType() != null && reimbursementForPayment.getPaymentType().equalsIgnoreCase(SHAConstants.NEFT_TYPE)){
				
				if(reimbursementForPayment.getChequeDDDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
					bean.setNeftDate(formatDate);
				}
			 bean.setChequeNumber(null);
			}else{
				if(reimbursementForPayment.getChequeDDDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
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
			Hospitals hospitals) {
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		ViewClaimStatusDTO hospitalDetails = instance.gethospitalDetails(hospitals);
		intimationDetails.setState(hospitalDetails.getState());
		intimationDetails.setCity(hospitalDetails.getCity());
		intimationDetails.setArea(hospitalDetails.getArea());
		intimationDetails.setHospitalAddress(hospitals.getAddress());
		intimationDetails.setHospitalName(hospitalDetails.getHospitalName());
		intimationDetails.setHospitalTypeValue(hospitalDetails.getHospitalTypeValue());
		intimationDetails.setHospitalIrdaCode(hospitalDetails.getHospitalIrdaCode());
		intimationDetails.setHospitalInternalCode(hospitalDetails.getHospitalInternalCode());
		EarlierRodMapper.invalidate(instance);
	}

	@Override
	public String textBundlePrefixString() {
		
		return "document-view-details-";
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

		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		String strPolicyNo = intimation.getPolicy().getPolicyNumber();
		Long insuredKey = intimation.getInsured().getKey();
		Insured	insuredByKey = intimationService.getInsuredByKey(insuredKey);
		//String strPolicyNo = "P/181219/01/2014/001945";
		getViewDocumentByPolicyNo(strPolicyNo,insuredByKey);

	//	UI.getCurrent().addWindow(viewDocuments);
		
	}
	
	public void getViewDocumentByPolicyNo(String strPolicyNo,Insured insured) {
		VerticalLayout vLayout = new VerticalLayout();
		String strDmsViewURL = null;
		Policy policyObj = null;
		BrowserFrame browserFrame = null;
		
		if (strPolicyNo != null) {
			policyObj = policyService.getByPolicyNumber(strPolicyNo);
			if (policyObj != null) {
				if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					strDmsViewURL = BPMClientContext.BANCS_POLICY_DOCUMENT_URL;
					strDmsViewURL = strDmsViewURL.replace("POLICY", strPolicyNo);
					if(ReferenceTable.getGMCProductList().containsKey(policyObj.getProduct().getKey())){
						strDmsViewURL = strDmsViewURL.replace("MEMBER", insured!=null?String.valueOf(insured.getSourceRiskId()!=null?insured.getSourceRiskId():""):"");		
					}else{
						strDmsViewURL = strDmsViewURL.replace("MEMBER", "");
					}
					getUI().getPage().open(strDmsViewURL, "_blank",1200,650,BorderStyle.NONE);
//					browserFrame = new BrowserFrame("",new ExternalResource(strDmsViewURL));
				}else{
					strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
					String dmsToken = intimationService.createDMSToken(strPolicyNo);
					getUI().getPage().open(strDmsViewURL+dmsToken, "_blank",1200,650,BorderStyle.NONE);
//					browserFrame = new BrowserFrame("",new ExternalResource(strDmsViewURL+dmsToken));
				}
			}
		}
		/*String strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
		BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));
		
		
		BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
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

}