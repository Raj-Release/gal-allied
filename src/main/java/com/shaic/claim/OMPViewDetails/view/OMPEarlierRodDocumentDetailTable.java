package com.shaic.claim.OMPViewDetails.view;

import javax.ejb.EJB;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
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
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OMPEarlierRodDocumentDetailTable extends GBaseTable<ViewDocumentDetailsDTO> {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sno","acknowledgeNumber","rodNumber","receivedFromValue","documentReceivedDate","modeOfReceipt"
		,"billClassification","approvedAmount","status"};*/
	
	private Long claimKey;
	
	@EJB
	private OMPIntimationService intimationService;
	
	@EJB
	private OMPClaimService claimSerivice;

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
		 Object[] NATURAL_COL_ORDER = new Object[] {"sno","acknowledgeNumber","rodNumber","receivedFromValue","documentReceivedDate","modeOfReceipt"
			,"billClassification","approvedAmount","status"};
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
//								DocAcknowledgement docAcknowledgment = reimbursementService.getDocAcknowledgment(tableDto.getKey());
								
								Window popup = new com.vaadin.ui.Window();
								popup.setCaption("");
								popup.setWidth("75%");
								popup.setHeight("85%");
//								rodIntimationDetailsObj = rodIntimationDetailsInstance.get();
								
							/*	ViewTmpClaim claim = claimSerivice.getTmpClaimByClaimKey(claimKey);
								ClaimStatusRegistrationDTO registrationDetails = EarlierRodMapper.getInstance().getRegistrationDetails(claim);
								Intimation intimation = intimationService.getIntimationByKey(claim.getIntimation().getKey());
								ViewClaimStatusDTO intimationDetails = EarlierRodMapper.getInstance().getViewClaimStatusDto(intimation);
								Hospitals hospitals = hospitalService.getHospitalById(intimation.getHospital());
								getHospitalDetails(intimationDetails, hospitals);
								intimationDetails.setClaimStatusRegistrionDetails(registrationDetails);
								intimationDetails.setClaimKey(claim.getKey());
								ViewTmpReimbursement tmpReimbursement = reimbursementService.getLatestViewTmpReimbursementDetails(claim.getKey());
								intimationDetails.setReceiptOfDocumentValues(setTableList);
								ViewTmpClaimPayment setPaymentDetails = setPaymentDetails(intimationDetails,claim);
								if(setPaymentDetails != null && docAcknowledgment.getHospitalisationFlag() != null && ! docAcknowledgment.getHospitalisationFlag().equals("Y")){
								rodIntimationDetailsObj.init(intimationDetails,tmpReimbursement.getKey());
								}else
								{
								 rodIntimationDetailsObj.init(intimationDetails, null);
								}*/
//								popup.setContent(rodIntimationDetailsObj);
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
								
//								uploadDocumentViewImpl.initPresenter(SHAConstants.BILLING_WORKSHEET);
								
								PreauthDTO bean = new PreauthDTO();
								ViewDocumentDetailsDTO tableDto = (ViewDocumentDetailsDTO)itemId;
								bean.setKey(tableDto.getReimbursementKey());
//								uploadDocumentViewImpl.init(bean,popup);
								popup.setCaption("Billing Worksheet");
								popup.setWidth("75%");
								popup.setHeight("90%");
								popup.setClosable(true);
//								popup.setContent(uploadDocumentViewImpl);
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


	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return null;
	}

}
