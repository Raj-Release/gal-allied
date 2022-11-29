package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;


import javax.ejb.EJB;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.ui.OMPMenuPresenter;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;

/**
 * @author Panneer Selvam M
 *
 */
public class OMPEarlierAcknowledgementDetailsPageTable extends GBaseTable<OMPEarlierAcknowledgementDetailsPageTableDTO>{

	@EJB
	private OMPIntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	/*
	 * private String acknowledgementNumber;
	private String rODNumber;
	private String documentReceivedFrom;
	private String documentReceivedDate;
	private String modeOfReceipt;
	private String Classification;
	private String subClassification;
	private String category;
	private String finalApprovedAmount;
	private String finalApprovedAmountINR;
	private String status;
	private String viewDocumentsLink;
	 */
	private final static Object[] VISIBLE_COL_ORDER = new Object[]{
		"acknowledgementNumber","rODNumber", "documentReceivedFrom","documentReceivedDate","modeOfReceipt",
		"classification", "subClassification", "category", "finalApprovedAmount", "finalApprovedAmountINR","status","viewDocumentsLink"}; 

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

		table.removeAllItems();

	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<OMPEarlierAcknowledgementDetailsPageTableDTO>(
				OMPEarlierAcknowledgementDetailsPageTableDTO.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setHeight("335px");
		
		/*table.removeGeneratedColumn("viewDocumentsLink");
		table.addGeneratedColumn("viewDocumentsLink",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, Object itemId,
							Object columnId) {
						Button button = new Button("View Documents");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								
								Claim claim = claimSerivice.getClaimByClaimKey(claimKey);
								getViewDocument(claim.getIntimation().getIntimationId());
								
								Object currentItemId = source.getData();
								OMPEarlierAcknowledgementDetailsPageTableDTO calculationViewTableDTO =  (OMPEarlierAcknowledgementDetailsPageTableDTO) currentItemId;
								if(calculationViewTableDTO != null){
									getViewDocument(calculationViewTableDTO.getOmpIntimation() , calculationViewTableDTO.getInsured() );
								}
								System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHAAAAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIII");
								System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHAAAAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIII");
								System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHAAAAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIII");
								System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHAAAAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIII");
								
								
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});*/
		
		table.removeGeneratedColumn("viewDocumentsLink");
		table.addGeneratedColumn("viewDocumentsLink",
				new Table.ColumnGenerator() {
					@SuppressWarnings("serial")
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						final Button viewIntimationDetailsButton = new Button("View Document Details");
						viewIntimationDetailsButton.setData(itemId);
						final OMPEarlierAcknowledgementDetailsPageTableDTO ompEarlierAcknowledgementDetailsPageTableDTO = ((OMPEarlierAcknowledgementDetailsPageTableDTO) itemId)/*.getKey()*/;

						/*Claim a_claim = claimService
								.getClaimforIntimation(intimationKey);*/

						viewIntimationDetailsButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {
										if(ompEarlierAcknowledgementDetailsPageTableDTO != null){
											
											System.out.println("HAIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
											getViewDocument(ompEarlierAcknowledgementDetailsPageTableDTO.getIntimationNumber());
										}
										/*Intimation intimation = intimationService.getIntimationByKey(intimationKey);
										viewUploadedDocumentDetails(intimation.getIntimationId());*/
									}
								});
						viewIntimationDetailsButton
								.addStyleName(BaseTheme.BUTTON_LINK);
						return viewIntimationDetailsButton;
					}
				});

	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=10){
			table.setPageLength(10);
		}

	}

	@Override
	public void tableSelectHandler(OMPEarlierAcknowledgementDetailsPageTableDTO t) {
		// TODO Auto-generated method stub
		fireViewEvent(OMPMenuPresenter.OMP_PROCESS_ACK_DETAIL_PAGE, t);
		//setCompleteLayout(t);

	}


	@Override
	public String textBundlePrefixString() {
		return "ompearlieracknowledgementdocumentsreceived-";
	}
	
	public void getViewDocument(String intimationNo) {

		OMPIntimation intimation = intimationService
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
