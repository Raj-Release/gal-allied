/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;





import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GBaseTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryDocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.pages.CreateRODDocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.pages.DocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.QueryDetailsViewUI;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.ReimbursementQuery;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 * 
 * The data fetched from Query details table
 * is populated in the below table. This 
 * table is displayed in Acknowledge Document
 * Received page.
 *
 */
public class RODQueryDetailsTable extends GBaseTable<RODQueryDetailsDTO>{
//public class RODQueryDetailsTable extends GEditableTable<RODQueryDetailsDTO>{
//public class RODQueryDetailsTable extends GEditableTable<RODQueryDetailsDTO>{
	
/*	public RODQueryDetailsTable() {
		super(RODQueryDetailsTable.class);
		setUp();
		// TODO Auto-generated constructor stub
	}*/



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	////private static Window popup;
	
	@EJB
	private AcknowledgementDocumentsReceivedService documentReceivedService;
	
	private QueryDetailsViewUI queryDetailsObj;
	
	@EJB
	private HospitalService hospitalService;
	
	private List<String> errorMessages =  new ArrayList<String>();

	
	@Inject
	private Instance<QueryDetailsViewUI> queryDetailsInstance;
	
	private BeanItemContainer<SelectValue> dropDownValuesReq;

	private String presenterString;
	
/*	@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;*/

	private static final Object[] NATURAL_COL_ORDER = new Object[] {
			//"queryNo", "rodNo", "billClassification", "diagnosis",""
			"sno","rodNo", "billClassification", "diagnosis",
			"claimedAmount", "queryRaisedRole", "queryRaisedDate", "queryStatus" };
	
	public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();


	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		//table.rem
		table.removeAllItems();
	}
	
static {
		
		fieldMap.put("rodNo", new TableFieldDTO("rodNo",TextField.class, String.class, false));
		fieldMap.put("billClassification", new TableFieldDTO("billClassification",TextField.class, String.class, false));
		fieldMap.put("diagnosis", new TableFieldDTO("diagnosis",TextField.class, String.class, false));
		fieldMap.put("claimedAmount", new TableFieldDTO("claimedAmount", TextField.class,Double.class, false));
		fieldMap.put("queryRaisedRole", new TableFieldDTO("queryRaisedRole", DateField.class,String.class, false));
		fieldMap.put("queryRaisedDate", new TableFieldDTO("queryRaisedDate", TextField.class,Date.class, false));
		fieldMap.put("queryStatus", new TableFieldDTO("queryStatus", TextField.class,String.class, false));
	}

	
	
	
	public void initpresenterString(String presenterString)
	{
		this.presenterString = presenterString;
	}
	
	public void init(String tableCaption, Boolean isNeedAddButton, Boolean showPagerFlag,BeanItemContainer<SelectValue> dropDownValuesReq) {
		this.dropDownValuesReq = dropDownValuesReq;
		super.init(tableCaption, isNeedAddButton, showPagerFlag);
		
	}
	

	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<RODQueryDetailsDTO>(
				RODQueryDetailsDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		table.setPageLength(3);
		//table.setEnabled(true);
		table.removeGeneratedColumn("viewdetails");
		table.addGeneratedColumn("viewdetails", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
					
				
				Button button = new Button("View Details");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						

						Window popup = new com.vaadin.ui.Window();
						RODQueryDetailsDTO viewQueryDto = (RODQueryDetailsDTO)itemId;
						if(viewQueryDto.getReimbursementQueryKey() != null){					
							queryDetails(viewQueryDto);	
						}
						
						popup.setCaption("");
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
					

						/**
						 * The button click event needs to be added post the view details
						 * page is developed. Once that is done, the same can be injected and
						 * re used. Below commented code can be uncommented and with slight
						 * modification , view details button should work. 
						 * This will changed later. 
						 * **/
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
		
		
		/*table.removeGeneratedColumn("Query Reply Received");
		table.addGeneratedColumn("Query Reply Received", new Table.ColumnGenerator() {
		
		*/
		table.removeGeneratedColumn("queryreplyreceived");
		table.addGeneratedColumn("queryreplyreceived", new Table.ColumnGenerator() {
		
			
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				GComboBox queryReplyRecBox = new GComboBox();
				String replyStatus = "";
				
				queryReplyRecBox.setContainerDataSource(dropDownValuesReq);
				queryReplyRecBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				queryReplyRecBox.setItemCaptionPropertyId("value");
				
				//RODQueryDetailsDTO queryValue = (RODQueryDetailsDTO) itemId;
				
				
//				 RODQueryDetailsDTO queryDetails1 = (RODQueryDetailsDTO) itemId;
//				 if(null != queryDetails1.getReplyStatus() && !queryDetails1.getReplyStatus().isEmpty())
//				 {
//					 if(("N").equalsIgnoreCase(queryDetails1.getReplyStatus()))
//					 {
//						 replyStatus = "NO";
//					 }
//					 else if(("Y").equalsIgnoreCase(queryDetails1.getReplyStatus()))
//					 {
//						 replyStatus = "YES ";
//					 }
//					 for(int i = 0 ; i<dropDownValuesReq.size() ; i++)
//					 	{
//							if (replyStatus.equalsIgnoreCase(dropDownValuesReq.getIdByIndex(i).getValue()))
//							{
//								queryReplyRecBox.setValue(dropDownValuesReq.getIdByIndex(i));
//							}
//						}
//				 }
				
				if((SHAConstants.BILL_ENTRY).equalsIgnoreCase(presenterString) || (SHAConstants.VIEW_ACKNOWLEDGEMENT_DETAILs).equalsIgnoreCase(presenterString))
				{
					queryReplyRecBox.setEnabled(false);
				}
				else
				{
					queryReplyRecBox.setEnabled(true);
				}
				
				queryReplyRecBox.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						
						if(presenterString != null && presenterString.equals(SHAConstants.VIEW_ACKNOWLEDGEMENT_DETAILs)){
							//only for view
						}else{
						SelectValue value = (SelectValue) event.getProperty().getValue();
						final RODQueryDetailsDTO queryDetails = (RODQueryDetailsDTO) itemId;
						if(null != value)
						{
							Boolean previousStatus = true;
							queryDetails.setReplyStatus(value.getValue());
							if(("NO").equalsIgnoreCase(value.getValue()))
							{
								List<RODQueryDetailsDTO> itemIds = (List<RODQueryDetailsDTO>) table.getItemIds();
								for (RODQueryDetailsDTO dto : itemIds) {
									if(queryDetails.getReimbursementQueryKey() != null && ! queryDetails.getReimbursementQueryKey().equals(dto.getReimbursementQueryKey())){
										if(dto.getReplyStatus() != null && dto.getReplyStatus().equalsIgnoreCase("Yes")){
											previousStatus = false;
										}
									}
								}
								
								if(previousStatus){
								if(! queryDetails.getOnPageLoad()){
								/*Label successLabel = new Label("<b style = 'color: red;'>A query is available, are you sure these documents are not related/ reply to query.</b>", ContentMode.HTML);
								
	//							Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
								
								Button homeButton = new Button("OK");
								homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
								HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
								horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
								horizontalLayout.setMargin(true);
								
								VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
								layout.setSpacing(true);
								layout.setMargin(true);
								HorizontalLayout hLayout = new HorizontalLayout(layout);
								hLayout.setMargin(true);
								
								final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("");
								dialog.setClosable(false);
								dialog.setContent(hLayout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
										.createAlertBox("A query is available, are you sure these documents are not related/ reply to query.</b>", buttonsNamewithType);
								Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
										.toString());
								
								
								homeButton.addClickListener(new ClickListener() {
									private static final long serialVersionUID = 7396240433865727954L;
	
									@Override
									public void buttonClick(ClickEvent event) {
										//fireViewEvent(DocumentDetailsPresenter.RESET_DOC_REC_STATUS_FOR_QUERY_REPLY, null);
										
										
										queryDetails.setReplyStatus("No");
										if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString))
										{
											fireViewEvent(DocumentDetailsPresenter.SET_DOC_REC_STATUS_FOR_QUERY_REPLY, queryDetails);
										}
										else if (SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString))
										{
											fireViewEvent(CreateRODDocumentDetailsPresenter.ROD_SET_DOC_REC_STATUS_FOR_QUERY_REPLY, queryDetails);
											
										}
										//Long docAckKey = queryDetails.getAcknowledgementKey();
										
										
										//dialog.close();
										
										//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
									}
								});
								}else{
									queryDetails.setOnPageLoad(false);
									queryDetails.setReplyStatus("No");
									if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString))
									{
										fireViewEvent(DocumentDetailsPresenter.SET_DOC_REC_STATUS_FOR_QUERY_REPLY, queryDetails);
									}
									else if (SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString))
									{
										fireViewEvent(CreateRODDocumentDetailsPresenter.ROD_SET_DOC_REC_STATUS_FOR_QUERY_REPLY, queryDetails);
									
									}
									
								}
								
			
								}else{
									if(!queryDetails.getOnPageLoad()){
										queryDetails.setReplyStatus("No");
										
									}else{
										queryDetails.setOnPageLoad(false);
										showAlertMessage();
									}
								}
										
							}
							else if(("Yes").equalsIgnoreCase(value.getValue().trim()))
							{
								queryDetails.setReplyStatus("Yes");
								//Long docAckKey = queryDetails.getAcknowledgementKey();
								if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString))
								{
									fireViewEvent(DocumentDetailsPresenter.SET_DOC_REC_STATUS_FOR_QUERY_REPLY, queryDetails);
								}
								else if (SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString))
								{
									fireViewEvent(CreateRODDocumentDetailsPresenter.ROD_SET_DOC_REC_STATUS_FOR_QUERY_REPLY, queryDetails);
									
								}
								else if (SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
								{
									fireViewEvent(BillEntryDocumentDetailsPresenter.BILL_ENTRY_SET_DOC_REC_STATUS_FOR_QUERY_REPLY, queryDetails);
									
								}
							}
							/*else if(("No").equalsIgnoreCase(value.getValue().trim()))
							{
								queryDetails.setQueryReplyStatus("No");
								//Long docAckKey = queryDetails.getAcknowledgementKey();
								if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString))
								{
									fireViewEvent(DocumentDetailsPresenter.SET_DOC_REC_STATUS_FOR_QUERY_REPLY, queryDetails);
								}
								else if (SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString))
								{
									fireViewEvent(CreateRODDocumentDetailsPresenter.ROD_SET_DOC_REC_STATUS_FOR_QUERY_REPLY, queryDetails);
									
								}
							}*/
						}
						}
						
					}
					
				});
				
				RODQueryDetailsDTO queryDetails1 = (RODQueryDetailsDTO) itemId;
				 if(null != queryDetails1.getReplyStatus() && !queryDetails1.getReplyStatus().isEmpty())
				 {
					 if(("N").equalsIgnoreCase(queryDetails1.getReplyStatus()))
					 {
						 replyStatus = "NO";
					 }else if(("NO").equalsIgnoreCase(queryDetails1.getReplyStatus())){
						 replyStatus = "NO";
					 }
					 else if(("Y").equalsIgnoreCase(queryDetails1.getReplyStatus()))
					 {
						 replyStatus = "YES";
					 }else if(("Yes").equalsIgnoreCase(queryDetails1.getReplyStatus())){
						 replyStatus = "YES";
					 }
					 for(int i = 0 ; i<dropDownValuesReq.size() ; i++)
					 	{
							if (replyStatus.equalsIgnoreCase(dropDownValuesReq.getIdByIndex(i).getValue()))
							{
								queryReplyRecBox.setValue(dropDownValuesReq.getIdByIndex(i));
							}
						}
				 }
				
				return queryReplyRecBox;
			}
		});
		
		
		/**
		 * Siva said, this column can be removed and instead of it, we will
		 * have a complete row selected like search screen. But during development
		 * have implemented as per screen. 
		 * *//*
		table.removeGeneratedColumn("queryreplyreceived");
		table.addGeneratedColumn("queryreplyreceived", new Table.ColumnGenerator() {
			
			ComboBox queryReplyRecBox = new ComboBox();
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				
				return null;
			}
		});*/
		
	}
	
	public void showAlertMessage(){
		
		/*Label successLabel = new Label("<b style = 'color: red;'>A query is available, are you sure these documents are not related/ reply to query.</b>", ContentMode.HTML);
		
		//							Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
									
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
		horizontalLayout.setMargin(true);
									
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
									
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
									
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("A query is available, are you sure these documents are not related/ reply to query.</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());						
									
									
	   homeButton.addClickListener(new ClickListener() {
	   private static final long serialVersionUID = 7396240433865727954L;
		
	    @Override
		public void buttonClick(ClickEvent event) {

													//dialog.close();
											
										}
									});
	}
	
	
	public void setQueryDropDownValues(final BeanItemContainer<SelectValue> dropDownValuesReq)
	{
		this.dropDownValuesReq = dropDownValuesReq;
	}
	
	
	/*public void generateDropDown( final BeanItemContainer<SelectValue> dropDownValuesReq)
	{
		//this.dropDownValuesReq = dropDownValuesReq;
		
		*//**
		 * Siva said, this column can be removed and instead of it, we will
		 * have a complete row selected like search screen. But during development
		 * have implemented as per screen. 
		 * *//*
		table.removeGeneratedColumn("queryreplyreceived");
		table.addGeneratedColumn("queryreplyreceived", new Table.ColumnGenerator() {
		table.removeGeneratedColumn("Query Reply Received");
		table.addGeneratedColumn("Query Reply Received", new Table.ColumnGenerator() {
			
			ComboBox queryReplyRecBox = new ComboBox();
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				queryReplyRecBox.setContainerDataSource(dropDownValuesReq);
				queryReplyRecBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				queryReplyRecBox.setItemCaptionPropertyId("value");
				
				queryReplyRecBox.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						
						SelectValue value = (SelectValue) event.getProperty().getValue();
						RODQueryDetailsDTO queryDetails = (RODQueryDetailsDTO) itemId;
						if(null != value)
						{
							queryDetails.setReplyStatus(value.getValue());
							if(("NO").equalsIgnoreCase(value.getValue()))
							{
								
								Label successLabel = new Label("<b style = 'color: red;'>A query is available, are you sure these documents are not related/ reply to query.</b>", ContentMode.HTML);
								
	//							Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
								
								Button homeButton = new Button("OK");
								homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
								HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
								horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
								horizontalLayout.setMargin(true);
								
								VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
								layout.setSpacing(true);
								layout.setMargin(true);
								HorizontalLayout hLayout = new HorizontalLayout(layout);
								hLayout.setMargin(true);
								
								final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("");
								dialog.setClosable(false);
								dialog.setContent(hLayout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);
								
								
								
								homeButton.addClickListener(new ClickListener() {
									private static final long serialVersionUID = 7396240433865727954L;
	
									@Override
									public void buttonClick(ClickEvent event) {
										fireViewEvent(DocumentDetailsPresenter.RESET_DOC_REC_STATUS_FOR_QUERY_REPLY, null);
										dialog.close();
										
										//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
									}
								});
										Label label = new Label("A query is available, are you sure these documents are not related/reply to query", ContentMode.HTML);
										label.setStyleName("errMessage");
										VerticalLayout layout = new VerticalLayout();
										layout.setMargin(true);
										layout.addComponent(label);
										ConfirmDialog dialog = new ConfirmDialog();
										dialog.setCaption("Errors");
										dialog.setClosable(true);
										dialog.setContent(layout);
										dialog.setResizable(true);
										dialog.setModal(true);
										dialog.show(getUI().getCurrent(), null, true);
										
							}
							else if(("Yes").equalsIgnoreCase(value.getValue().trim()))
							{
								fireViewEvent(DocumentDetailsPresenter.SET_DOC_REC_STATUS_FOR_QUERY_REPLY, null);
							}
						}
						
					}
				});
				return queryReplyRecBox;
			}
		});
		
	}*/

	@Override
	public void tableSelectHandler(RODQueryDetailsDTO t) {
	}

	@Override
	public String textBundlePrefixString() {
		return "rod-query-details-";
	}	

//	private void queryDetails(Long key){
//		
//		ReimbursementQuery reimbursementQuery = documentReceivedService.getReimbursementQuery(key);
//		ViewQueryDTO viewDetails = EarlierRodMapper.getviewDetailsQuery(reimbursementQuery);
//		if(viewDetails.getIntimationNo() != null){
//			Long hospitalId = reimbursementQuery.getReimbursement().getClaim().getIntimation().getHospital();
//			
//			Hospitals hospitals = hospitalService.getHospitalById(hospitalId);
//			if(hospitals != null){
//			viewDetails.setHospitalName(hospitals.getName());
//			viewDetails.setHospitalCity(hospitals.getCity());
//			viewDetails.setHospitalType(hospitals.getHospitalTypeName());
//			}
//		}
//		queryDetailsObj = queryDetailsInstance.get();
//		queryDetailsObj.init(viewDetails);
//	}
	
    private void queryDetails(RODQueryDetailsDTO viewQueryDto){
		
		ReimbursementQuery reimbursementQuery = documentReceivedService.getReimbursementQuery(viewQueryDto.getReimbursementQueryKey());
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		ViewQueryDTO viewDetails = instance.getviewDetailsQuery(reimbursementQuery);
		
		if(reimbursementQuery != null){
			String billClassification = getBillClassification(reimbursementQuery.getReimbursement().getDocAcknowLedgement());
			viewDetails.setBillClassification(billClassification);
			
//			Reimbursement reimbursement = documentReceivedService.getReimbursement(reimbursementQuery.getReimbursement().getKey());
//			if(reimbursement != null){
				viewDetails.setQueryStatus(reimbursementQuery.getStatus().getProcessValue());
//			}
		}
		
		viewDetails.setDiagnosis(viewQueryDto.getDiagnosis());
		viewDetails.setQueryRaiseRole(viewQueryDto.getQueryRaisedRole());
		viewDetails.setQueryRaised(viewQueryDto.getQueryRaisedRole());
		
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
	
	
	public List<RODQueryDetailsDTO> getValues() 
	{
		Collection<RODQueryDetailsDTO> coll = (Collection<RODQueryDetailsDTO>) table.getItemIds();
		List list;
		if (coll instanceof List){
			list = (List)coll;
		}
		else{
			list = new ArrayList(coll);
		}
		return list;
	}
	
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<RODQueryDetailsDTO> itemIds = (Collection<RODQueryDetailsDTO>) table.getItemIds();
		Integer queryCount = 0;
		if(null != itemIds && !itemIds.isEmpty())
		{
	/*	Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();*/
		for (RODQueryDetailsDTO bean : itemIds) {
			
		//if(null != bean && ( null != bean.getReceivedStatus() || null != bean.getNoOfDocuments() || null != bean.getRemarks()))
				{
					if(null == bean.getReplyStatus() || (null != bean.getReplyStatus() && ("").equalsIgnoreCase(bean.getReplyStatus())))// && ("").equalsIgnoreCase(bean.getReplyStatus()))	
					{
						hasError = true;
						errorMessages.add("Please select query reply received");
					}
					
				}
				if(null == bean.getReplyStatus() || (null != bean.getReplyStatus() && ("Yes").equalsIgnoreCase(bean.getReplyStatus()))){
					queryCount++;
				}
		 }
		}
		
		    if(queryCount>1){
		    	hasError= true;
		    	errorMessages.add("Please select only one query reply status YES");
		    }
		
			return !hasError;
	}



	/*@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		// TODO Auto-generated method stub
		 return fieldMap;
	}



	@Override
	public void deleteRow(Object itemId) {
		// TODO Auto-generated method stub
		
	}*/

}
