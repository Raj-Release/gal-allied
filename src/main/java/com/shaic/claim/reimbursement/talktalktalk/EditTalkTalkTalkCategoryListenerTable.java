package com.shaic.claim.reimbursement.talktalktalk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.talktalktalk.TalkTalkTalkCategoryListenerTable.ImmediateFieldFactory;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.zybnet.autocomplete.server.AutocompleteField;

public class EditTalkTalkTalkCategoryListenerTable extends ViewComponent {


	private Map<InitiateTalkTalkTalkDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<InitiateTalkTalkTalkDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<InitiateTalkTalkTalkDTO> container = new BeanItemContainer<InitiateTalkTalkTalkDTO>(InitiateTalkTalkTalkDTO.class);

	private Table table;

	private Button btnAdd;
	
	private Button callBtn  = null;
	
	private Button holdBtn  = null;

	private Button endCallBtn  = null;

	private Button removeHold  = null;

	private Map<String, Object> referenceData;

	private List<String> errorMessages;

	private static Validator validator;

	public static TextField dateDiffFld = new TextField();

	private Map dateMap;

	private Map duplicateMap;

	private String presenterString;
	
	private PreauthDTO preauthDTO;
	
	private String userName;

	private List<InitiateTalkTalkTalkDTO> initiateTalkTalkTalkDTOList ;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@Inject
	private DialerLoginPageUI diallerLogin;

	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}

	public void init(Boolean isAddBtnRequired,PreauthDTO bean) {
		// container.removeAllItems();
		this.preauthDTO =  bean;		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		dateMap = new HashMap<Date, String>();
		duplicateMap = new HashMap<Date, String>();
		initiateTalkTalkTalkDTOList = new ArrayList<InitiateTalkTalkTalkDTO>();
		this.errorMessages = new ArrayList<String>();
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));

		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		initTable();
		table.setWidth("100%");
		table.setHeight("200px");
		table.setPageLength(table.getItemIds().size());
		addListener();
		if (isAddBtnRequired) {
			layout.addComponent(btnLayout);
		}
		layout.addComponent(table);

		setCompositionRoot(layout);
	}


	public void setTableList(List<InitiateTalkTalkTalkDTO> list) {
		table.removeAllItems();
		for (InitiateTalkTalkTalkDTO bean : list) {
			//table.addItem(bean);
			container.addItem(bean);
		}
		//table.sort();
	}

	void initTable() {
		table = new Table("", container);
		// container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		generateColumn();

		table.setVisibleColumns(new Object[] { "slNo","typeOfCommunication","talkSpokento","talkSpokenDate","talkMobto","callOptions","remarks","processingUserName"});

		table.setColumnHeader("slNo", "S.No");
		table.setColumnHeader("typeOfCommunication", "Type of Communication");
		table.setColumnHeader("talkSpokento", "Spoken To (Name)");
		table.setColumnHeader("talkSpokenDate", "Date and Time of Call");
		table.setColumnHeader("talkMobto", "Contact Number of the person");
		table.setColumnHeader("callOptions", "Call Options");
		table.setColumnHeader("remarks", "Remarks");
		table.setColumnHeader("processingUserName", "Processing User Name");
		table.setColumnWidth("slNo", 50);

		table.setEditable(true);

		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button deleteButton = new Button("Delete");
				InitiateTalkTalkTalkDTO talkDTO = (InitiateTalkTalkTalkDTO) itemId;
				deleteButton.setData(itemId);
				if(talkDTO.getKey()!=null){
					deleteButton.setEnabled(false);
				}
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						// dateDiffFld.setValue(null);
						table.removeItem(currentItemId);
					}
				});
				deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return deleteButton;
			}
		});

/*		BeanItem<InitiateTalkTalkTalkDTO> addItem = container
				.addItem(new InitiateTalkTalkTalkDTO());*/
		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO = new InitiateTalkTalkTalkDTO();
				Integer size = container.size();
				String userID = (String) getUI().getSession().getAttribute(BPMClientContext.USERID);
				String userName  = masterService.getEmployeeByName(userID);
				initiateTalkTalkTalkDTO.setProcessingUserName(userID.toUpperCase()+"-"+userName);
				initiateTalkTalkTalkDTO.setSlNo(size.longValue()+1);
				container.addItem(initiateTalkTalkTalkDTO);
			}
		});
	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8967055486309269929L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			InitiateTalkTalkTalkDTO talkDTO = (InitiateTalkTalkTalkDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(talkDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(talkDTO, new HashMap<String, AbstractField<?>>());
			} // else {
			tableRow = tableItem.get(talkDTO);
			// }

			if ("slNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("45px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setMaxLength(50);
				tableRow.put("slNo", field);
				return field;
			} else if ("typeOfCommunication".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("100%");
				tableRow.put("typeOfCommunication", box);
				box.setData(talkDTO);
				addTypeofCommunicationValues(box);
				final TextField txt = (TextField) tableRow.get("slNo");
				generateSlNo(txt);
				if(talkDTO.getKey()!=null){
					box.setEnabled(false);
				}
				addCommunicationListener(box);
				return box;
			}else if("talkSpokento".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setMaxLength(75);	
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z .]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("talkSpokento", field);
				if(talkDTO.getKey()!=null){
					field.setEnabled(false);
				}
				return field;
			}else if("talkSpokenDate".equals(propertyId)) {
				PopupDateField calBackDate = new PopupDateField();
				calBackDate.setDateFormat("dd/MM/yyyy hh:mm a");
				calBackDate.setInputPrompt("DD/MM/YYYY HH:MM A");
				calBackDate.setLocale(new Locale("en", "EN"));
				calBackDate.setResolution(PopupDateField.RESOLUTION_HOUR);
				calBackDate.setResolution(PopupDateField.RESOLUTION_MIN);
				calBackDate.setData(talkDTO);
				Date oldcurrentDate = new Date();
				calBackDate.setRangeEnd(oldcurrentDate);
				calBackDate.setWidth("100%");
				tableRow.put("talkSpokenDate", calBackDate);
				addpopupDateListener(calBackDate);
				if(talkDTO.getKey()!=null){
					calBackDate.setEnabled(false);
				}
				calBackDate.setEnabled(true);
				return calBackDate;
			}else if("talkMobto".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(talkDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("talkMobto", field);
				if(talkDTO.getKey()!=null){
					field.setEnabled(false);
				}
				return field;
			}else if("remarks".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setMaxLength(4000);
				field.setNullRepresentation("");
				tableRow.put("remarks", field);
				field.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(field,null,getUI(),SHAConstants.TALK_TALK_TALK_REMARK);
				if(talkDTO.getKey()!=null){
					field.setEnabled(false);
				}
				return field;
			}else if("processingUserName".equals(propertyId)) {
				TextField field = new TextField();
				field.setMaxLength(200);
				field.setNullRepresentation("");
				if(talkDTO.getProcessingUserName() !=null){
					field.setValue(talkDTO.getProcessingUserName());
				}
				field.setEnabled(false);
				if(talkDTO.getKey()!=null){
					field.setEnabled(false);
				}
				tableRow.put("processingUserName", field);
				return field;
			}else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	private void generateColumn(){
		table.removeGeneratedColumn("callOptions");
		table.addGeneratedColumn("callOptions", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
				InitiateTalkTalkTalkDTO talkDTO = (InitiateTalkTalkTalkDTO) itemId;
				VerticalLayout vLayout = new VerticalLayout();
		    	callBtn  = new Button("Place a Call");
		    	holdBtn  = new Button("Place on Hold");
		    	holdBtn.setEnabled(false);
		    	endCallBtn  = new Button("End Call");
		    	endCallBtn.setEnabled(false);
		    	removeHold  = new Button("Remove Hold");
		    	removeHold.setEnabled(false);
		    	HorizontalLayout frstLayout = new HorizontalLayout(callBtn,holdBtn);
		    	frstLayout.setSpacing(true);
		    	HorizontalLayout secndLayout = new HorizontalLayout(endCallBtn,removeHold);
		    	secndLayout.setSpacing(true);
		    	vLayout.addComponents(frstLayout,secndLayout);
		    	vLayout.setSpacing(true);
		    	
		    	callBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();	
						InitiateTalkTalkTalkDTO talkDTO = (InitiateTalkTalkTalkDTO) itemId;
						String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);;
						talkDTO.setStrUserName(userName);
						HashMap<String, AbstractField<?>> hashMap = tableItem
								.get(talkDTO);
						System.out.println("--the hashMap----" + hashMap);
						PopupDateField spokenDate = (PopupDateField) hashMap
								.get("talkSpokenDate");
						spokenDate.setEnabled(true);
						Date currentDate = new Date();
						spokenDate.setValue(currentDate);
						spokenDate.setEnabled(false);
						String extnCode = (String) getUI().getSession().getAttribute(SHAConstants.EXTN_CODE);
						String leadId = (String) getUI().getSession().getAttribute(SHAConstants.LEAD_ID);
						String refGlobId = (String) getUI().getSession().getAttribute(SHAConstants.REF_ID);
						if(extnCode != null){
							talkDTO.setExtnCode(extnCode);
							if(leadId != null){
								talkDTO.setConvoxid(leadId);
								talkDTO.setReferenceId(refGlobId);
								DialerEndCallResponse endCallResponse = intimationService.dialerEndCall(talkDTO);
								if(endCallResponse != null && endCallResponse.getSTATUS() != null && endCallResponse.getSTATUS().equalsIgnoreCase("EC000")){
									System.out.println("Call Ended Successfully : "+leadId);
								}
							}
							String refId = dbCalculationService.generateReminderBatchId(SHAConstants.DIALER_REF_SEQUENCE_NAME);
							talkDTO.setReferenceId(refId);
							DialerLoginLogoutResponse loginResponse = intimationService.dialerLogin(talkDTO);
							if(loginResponse != null){
								if(loginResponse.getSTATUS() != null && (loginResponse.getSTATUS().equalsIgnoreCase("LI000") || loginResponse.getSTATUS().equalsIgnoreCase("LI015")
										|| loginResponse.getSTATUS().equalsIgnoreCase("LI0014"))){
									Timer timer = new Timer();
									Date sysdate = new Date();
									System.out.println("Before Place Call Date and Time "+sysdate);
									Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
									calendar.add(Calendar.SECOND, 3);
									Date date = calendar.getTime();
									timer.schedule(new DialerTask(intimationService,talkDTO,getSession()),date);
									fireViewEvent(InitiateTalkTalkTalkWizardPresenter.ENABLE_DIALER_BUTTONS,talkDTO);
									/*DialerPlaceCallResponse placeCallResponse = intimationService.dialerPlaceCall(talkDTO);
									if(placeCallResponse != null){
										if(placeCallResponse.getSTATUS() != null && placeCallResponse.getSTATUS().equalsIgnoreCase("CL000")){
											if(placeCallResponse.getLEAD_ID() != null){
												talkDTO.setConvoxid(placeCallResponse.getLEAD_ID());
												fireViewEvent(InitiateTalkTalkTalkWizardPresenter.ENABLE_DIALER_BUTTONS,talkDTO);
												getSession().setAttribute(SHAConstants.LEAD_ID, placeCallResponse.getLEAD_ID());
												getSession().setAttribute(SHAConstants.REF_ID, placeCallResponse.getRefno());
											}
										}else {
											showErrorMessage(placeCallResponse.getMESSAGE());
										}
									}*/
								} else {
									showErrorMessage(loginResponse.getMESSAGE());
								}
							}
						
							/*String refId = dbCalculationService.generateReminderBatchId(SHAConstants.DIALER_REF_SEQUENCE_NAME);
							talkDTO.setReferenceId(refId);
							DialerPlaceCallResponse placeCallResponse = intimationService.dialerPlaceCall(talkDTO);
							if(placeCallResponse != null){
								if(placeCallResponse.getSTATUS() != null && placeCallResponse.getSTATUS().equalsIgnoreCase("CL000")){
									endCallBtn.setEnabled(true);
									if(placeCallResponse.getLEAD_ID() != null){
										talkDTO.setConvoxid(placeCallResponse.getLEAD_ID());
									}
								} else {
									endCallBtn.setEnabled(true);
								}
							}*/
						}
						else {
							Window popup = new com.vaadin.ui.Window();
							popup.setWidth("55%");
							popup.setHeight("70%");
							diallerLogin.initView(popup,talkDTO);
							popup.setContent(diallerLogin);
							popup.setClosable(false);
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
									popup.close();
								}
							});
							
							popup.setModal(true);
							UI.getCurrent().addWindow(popup);
						}
						
			        } 
			    });
				
		    	endCallBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();	
						InitiateTalkTalkTalkDTO talkDTO = (InitiateTalkTalkTalkDTO) itemId;
						DialerEndCallResponse endCallResponse = intimationService.dialerEndCall(talkDTO);
						if(endCallResponse != null){
							if(endCallResponse.getSTATUS() != null && endCallResponse.getSTATUS().equalsIgnoreCase("EC000")){
								DialerLoginLogoutResponse logoutresponse = intimationService.dialerLogOut(talkDTO);
								if(logoutresponse != null){
									if(logoutresponse.getSTATUS() != null && logoutresponse.getSTATUS().equalsIgnoreCase("LO000")){
										callBtn.setEnabled(true);
										endCallBtn.setEnabled(false);
										holdBtn.setEnabled(false);
										removeHold.setEnabled(false);
										talkDTO.setEndCallRefId(logoutresponse.getRefno());
										talkDTO.setEndCallConvoxId(talkDTO.getConvoxid());
										getSession().setAttribute(SHAConstants.LEAD_ID, null);
										getSession().setAttribute(SHAConstants.REF_ID, null);
									}
									talkDTO.setReferenceId(null);
									talkDTO.setConvoxid(null);
								}

							} else {
								showErrorMessage(endCallResponse.getMESSAGE());
							}
						}
						
			        } 
			    });
		    	
		    	holdBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();	
						InitiateTalkTalkTalkDTO talkDTO = (InitiateTalkTalkTalkDTO) itemId;
						DialerHoldCallResponse holdCallResponse = intimationService.dialerHoldCall(talkDTO);
						if(holdCallResponse != null){
							if(holdCallResponse.getSTATUS() != null && holdCallResponse.getSTATUS().equalsIgnoreCase("HOLD000")){
								System.out.println("Call in Hold Mode");
								removeHold.setEnabled(true);
								endCallBtn.setEnabled(true);
								holdBtn.setEnabled(false);
							} else {
//								removeHold.setEnabled(true);
							}
						}
						
			        } 
			    });
		    	
		    	removeHold.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();	
						InitiateTalkTalkTalkDTO talkDTO = (InitiateTalkTalkTalkDTO) itemId;
						DialerUnholdResponse unholdCallResponse = intimationService.dialerUnHold(talkDTO);
						if(unholdCallResponse != null){
							if(unholdCallResponse.getSTATUS() != null && unholdCallResponse.getSTATUS().equalsIgnoreCase("UNHOLD000")){
								System.out.println("Call is UnHold");
								endCallBtn.setEnabled(true);
								holdBtn.setEnabled(true);
								removeHold.setEnabled(false);
							}
						}
						
			        } 
			    });
		    	
		        return vLayout;
		}
	}); 
		
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void addTypeofCommunicationValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> billClassificationContainer = masterService.getMasterValueByCode(ReferenceTable.TALKCOMM);
		comboBox.setContainerDataSource(billClassificationContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}
	
	/*public void setProcessingUserDetails(TextField txtField) {
		if(null != txtField){
			
			Collection<InitiateTalkTalkTalkDTO> itemIds = (Collection<InitiateTalkTalkTalkDTO>) table.getItemIds();
			//int i = 0;
			for (InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO : itemIds) {
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(initiateTalkTalkTalkDTO);
				if(null != hashMap && !hashMap.isEmpty())
				{
					TextField itemNoFld = (TextField)hashMap.get("processingUserName");
				if(null != itemNoFld)
				{
					itemNoFld.setValue(this.preauthDTO.getInitiateTalkTalkTalkDTO().getEmployeeID()+"-"+this.preauthDTO.getInitiateTalkTalkTalkDTO().getEmployeeName() ); 
					itemNoFld.setEnabled(false);
					}
				}
				//i++;
			}
		}
	}*/



	private void generateSlNo(TextField txtField)
	{

		Collection<InitiateTalkTalkTalkDTO> itemIds = (Collection<InitiateTalkTalkTalkDTO>) table.getItemIds();

		int i = 0;
		for (InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO : itemIds) {
			i++;
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(initiateTalkTalkTalkDTO);
			if(null != hashMap && !hashMap.isEmpty())
			{
				TextField itemNoFld = (TextField)hashMap.get("slNo");
				if(null != itemNoFld)
				{
					itemNoFld.setValue(String.valueOf(i)); 
					itemNoFld.setEnabled(false);
				}
			}
		}

	}





	public void valueChangeLisenerForDate(final DateField total,
			final String fldName) {

		if (null != total) {
			total.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					//	calculateDateDiff(total, fldName);
					// findDuplicateDate(total);

				}
			});
		}
	}

	public void addFileTypeValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> fileTypeContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("fileType");
		comboBox.setContainerDataSource(fileTypeContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}

	public void addBeanToList(InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO) {
		container.addItem(initiateTalkTalkTalkDTO);
	}


	public List<InitiateTalkTalkTalkDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<InitiateTalkTalkTalkDTO> itemIds = new ArrayList<InitiateTalkTalkTalkDTO>();
		if (this.table != null) {
			itemIds = (List<InitiateTalkTalkTalkDTO>) this.table.getItemIds();
			for (InitiateTalkTalkTalkDTO extraEmployeeEffortDTO : itemIds) {
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(extraEmployeeEffortDTO);
				if(null != hashMap)
				{
					TextField txtEmpIdFld = (TextField) hashMap.get("employeeId");
					AutocompleteField<ExtraEmployeeEffortDTO> empName = (AutocompleteField<ExtraEmployeeEffortDTO>) hashMap.get("employeeNameDTO");
				}

			}
		}

		return itemIds;
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}


	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<InitiateTalkTalkTalkDTO> itemIds = (Collection<InitiateTalkTalkTalkDTO>) table.getItemIds();
		if(null != itemIds)
		{
			if(itemIds.isEmpty()) {
				hasError = true;
				errorMessages.add("Please Enter Atleast one Talk Talk Talk Details to Proceed further.");

			}
			else{
				for (InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO : itemIds) {
					if(!(null != initiateTalkTalkTalkDTO.getTypeOfCommunication() && null != initiateTalkTalkTalkDTO.getTypeOfCommunication().getValue()))
					{
						hasError = true;
						errorMessages.add("Please select Type of communication type");
					}
					if(initiateTalkTalkTalkDTO.getTalkSpokento() == null || initiateTalkTalkTalkDTO.getTalkSpokento().isEmpty()){
						hasError = true;
						errorMessages.add("Please Enter Spoken To");
					}
					if(initiateTalkTalkTalkDTO.getTalkSpokenDate() == null){
						hasError = true;
						errorMessages.add("Please Select Date and Time");
					}
					if(initiateTalkTalkTalkDTO.getTalkMobto() == null){
						hasError = true;
						errorMessages.add("Please Enter Contact Number");
					}
					if(initiateTalkTalkTalkDTO.getTalkMobto() != null && initiateTalkTalkTalkDTO.getTalkMobto().toString().length() != 10){
						hasError = true;
						errorMessages.add("Please Enter Valid Contact Number");
					}
					if(initiateTalkTalkTalkDTO.getRemarks() == null || initiateTalkTalkTalkDTO.getRemarks().isEmpty()){
						hasError = true;
						errorMessages.add("Please Enter Remarks");
					}
				}
			}
		}
		return !hasError;
	}

	public void invalidate(){
		//SHAUtils.setCleaEEETableItem(tableItem);
		SHAUtils.setClearReferenceData(referenceData);
		errorMessages = null;
		validator = null;
		presenterString = null;
		initiateTalkTalkTalkDTOList = null;
		dateMap = null;
		duplicateMap = null;
		masterService = null;
	}
	
	private void addpopupDateListener(final PopupDateField calBackDate){
		calBackDate.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				//calBackDate.setValue(calBackDate.getValue());
				Date selected = (Date) event.getProperty().getValue();
				Date currentDate = new Date();
				try {
					SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
					if(fmt.format(selected).equals(fmt.format(currentDate))){
						Calendar selectedCal = Calendar.getInstance();
						selectedCal.setTime(selected);
						Calendar currentCal = Calendar.getInstance();
						currentCal.setTime(currentDate);
						if (selectedCal.get(Calendar.AM_PM) == currentCal.get(Calendar.AM_PM)) {
							if(selectedCal.get(Calendar.HOUR_OF_DAY) > currentCal.get(Calendar.HOUR_OF_DAY)){
								event.getProperty().setValue(currentDate);
							}else if(selectedCal.get(Calendar.HOUR_OF_DAY) == currentCal.get(Calendar.HOUR_OF_DAY)
									&& selectedCal.get(Calendar.MINUTE) > currentCal.get(Calendar.MINUTE)){
								event.getProperty().setValue(currentDate);
							}
						}else if(currentCal.get(Calendar.AM_PM) == Calendar.AM && selectedCal.get(Calendar.AM_PM) == Calendar.PM){
							event.getProperty().setValue(currentDate);
						}
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	private void addCommunicationListener(
			final GComboBox communicationCombo) {
		if (communicationCombo != null) {
			communicationCombo
			.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					GComboBox component = (GComboBox) event
							.getProperty();
					BeanItemContainer<SelectValue> applicableContainer = (BeanItemContainer<SelectValue>) component
							.getContainerDataSource();
					InitiateTalkTalkTalkDTO initiateTalkDTO = (InitiateTalkTalkTalkDTO) component
							.getData();

					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(initiateTalkDTO);
					System.out.println("--the hashMap----" + hashMap);
					TextField mobileNo = (TextField) hashMap
							.get("talkMobto");
					if(initiateTalkDTO != null && initiateTalkDTO.getTypeOfCommunication().getId() != null){
						if(initiateTalkDTO.getTypeOfCommunication().getId().equals(11212l)){
							Intimation intimationDtls = intimationService.getIntimationByNo(preauthDTO.getIntimationNumber());
							Hospitals hosp = intimationService.getHospitalByKey(intimationDtls.getHospital());
							if(initiateTalkDTO.getTalkMobto() == null && hosp.getMobileNumber() != null){
								mobileNo.setValue(hosp.getMobileNumber());
							}
						} else {
							Intimation intimationDtls = intimationService.getIntimationByNo(preauthDTO.getIntimationNumber());
							if(initiateTalkDTO.getTalkMobto() == null && intimationDtls.getPolicy().getRegisteredMobileNumber() != null){
								mobileNo.setValue(intimationDtls.getPolicy().getRegisteredMobileNumber());
							}
							
						}
					}

				}
			});
		}
	}
	
	public void enableDialerButtons(){
		if(endCallBtn != null){
			endCallBtn.setEnabled(true);
		}
		if(holdBtn != null){
			holdBtn.setEnabled(true);
		}
		if(callBtn != null){
			callBtn.setEnabled(false);
		}
	}
	
	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}


}



