/**
 * 
 */
package com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.domain.ClaimPaymentCancel;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Reimbursement;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

/**
 * @author ntv.narenj
 *
 */
public class SearchAcknowledgementDocumentReceiverForm extends SearchComponent<SearchAcknowledgementDocumentReceiverFormDTO> {
	
	@Inject
	private SearchAcknowledgementDocumentReceiverTable searchAcknowledgementDocumentReceiverTable;
	
	@Inject
	private SearchAcknowledgementDocumentPendingTable pendingTable;
	
	@Inject
	private SearchAcknowledgementDocumentReceiverService paymentService;
	
/*	@Inject
	private SearchAcknowledgementDocumentPendingTableDTO pendingTableDto;*/
	
	private Window popup;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private TextField txtHealthCardIdNo;
	private Button ackPending;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Acknowledge Receipt of Documents");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		txtHealthCardIdNo = binder.buildAndBind("Health Card ID No","healthCardIdNo",TextField.class);
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		
		ackPending = new Button("Pending Cases (Acknowledgements to be created) ");
		FormLayout ackPendingLayout = new FormLayout(ackPending);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,txtHealthCardIdNo);
		FormLayout formLayoutReight = new FormLayout(txtPolicyNo);	
		
	    HorizontalLayout ackfieldLayout = new HorizontalLayout(ackPendingLayout);
	  //  ackfieldLayout.setComponentAlignment(ackPendingLayout, alignment);
	    ackfieldLayout.setMargin(false);
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
	
		fieldLayout.setMargin(false);
		fieldLayout.setWidth("110%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:120.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:120.0px;left:329.0px;");
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("200px");
		
		mainVerticalLayout.addComponent(ackfieldLayout);
		mainVerticalLayout.setComponentAlignment(ackfieldLayout, Alignment.TOP_RIGHT);
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
	//	 mainVerticalLayout.setHeight("340px");
		 mainVerticalLayout.setWidth("650px");
		 mainVerticalLayout.setMargin(false);		 
		
		addListener();
		addListenerForAckPendingButton();
		
		return mainVerticalLayout;
	}
	
	
	
	  public void addListenerForAckPendingButton()
	     {
	    	 
	
		  ackPending.addClickListener(new Button.ClickListener() {

	    		 @Override
	    		 public void buttonClick(ClickEvent event) {	    			 
	    			 
	    			 
	    			 pendingTable.init("", false, true);
	    			 List<SearchAcknowledgementDocumentPendingTableDTO> pendingTableDTOList = new ArrayList<SearchAcknowledgementDocumentPendingTableDTO>();
	    			 
	    			// ClaimPaymentCancel claimPaymentDetail = paymentService.getpaymentCancelDetail();
	    			 List<ClaimPaymentCancel> claimPaymentDetailList = paymentService.getpaymentCancelDetailList();
	    			 
	    			 if(null != claimPaymentDetailList && !claimPaymentDetailList.isEmpty())
	    			 {
	    				 for (ClaimPaymentCancel claimPaymentDetail : claimPaymentDetailList) {
	    					 SearchAcknowledgementDocumentPendingTableDTO pendingTableDto = new SearchAcknowledgementDocumentPendingTableDTO();
	    					 pendingTableDto.setClaimNo(claimPaymentDetail.getClaimNumber());
	    	    			 pendingTableDto.setIntimationNo(claimPaymentDetail.getIntimationNumber());
	    	    			 pendingTableDto.setPolicyNo(claimPaymentDetail.getPolicyNumber());
	    	    			 pendingTableDto.setRodNo(claimPaymentDetail.getRodNumber());
	    	    			 if(pendingTableDto != null)
	    	    			 {
	    	    			  Intimation intimationObj = paymentService.getIntimationObject(pendingTableDto.getIntimationNo());
	    	    			 if(intimationObj != null)
	    	    					    				 
	    	    			 {
	    	    			 Hospitals hospitalDetail = paymentService.getHospitalDetail(intimationObj.getHospital());
	    	    			 if(null != hospitalDetail)
	    	    			 {
	    	    				 pendingTableDto.setHospitalCity(hospitalDetail.getCity()); 
	    	    				 pendingTableDto.setHospitalAddress(hospitalDetail.getAddress());
	    	    				 pendingTableDto.setHospitalName(hospitalDetail.getName());
	    	    				 pendingTableDto.setCpuCode(intimationObj.getCpuCode().getCpuCode());
	    	    				 pendingTableDto.setInsuredPatientName(intimationObj.getInsured().getInsuredName());
	    	    			 }
	    	    			 }
	    	    			 }
	    	    			 
	    	    			 Reimbursement rodDetail = paymentService.getRodDetail(pendingTableDto.getIntimationNo());
	    	    			 if(rodDetail != null)
	    	    			 {
	    	    				 pendingTableDto.setDateofAdmission(rodDetail.getDateOfAdmission());
	    	    				 pendingTableDto.setRodStatus(rodDetail.getStatus().getProcessValue());
	    	    			 }
	    	    			 
	    	    			 pendingTable.addBeanToList(pendingTableDto);
	    				 }
						}
	    				 
	    			
    			 
	    			    popup = new com.vaadin.ui.Window();
	    				popup.setCaption("Pending Cases");
	    				popup.setWidth("75%");
	    				popup.setHeight("75%");
	    				popup.setContent(pendingTable);
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
	     }
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchAcknowledgementDocumentReceiverFormDTO>(SearchAcknowledgementDocumentReceiverFormDTO.class);
		this.binder.setItemDataSource(new SearchAcknowledgementDocumentReceiverFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}	
}