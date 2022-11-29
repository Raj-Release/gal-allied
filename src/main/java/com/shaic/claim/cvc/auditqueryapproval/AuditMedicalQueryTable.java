package com.shaic.claim.cvc.auditqueryapproval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AuditTeam;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;


public class AuditMedicalQueryTable extends ViewComponent {
		private static final long serialVersionUID = 1L;

		@EJB
		private MasterService masterService;

		@EJB
		private PreMedicalService premedicalService;

		@EJB
		private DBCalculationService dBCalculationService;

		private Map<SearchCVCAuditClsQryTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<SearchCVCAuditClsQryTableDTO, HashMap<String, AbstractField<?>>>();

		BeanItemContainer<SearchCVCAuditClsQryTableDTO> data = new BeanItemContainer<SearchCVCAuditClsQryTableDTO>(
				SearchCVCAuditClsQryTableDTO.class);

		private Table table;

		private Map<String, Object> referenceData;

		private StringBuffer errorMessages;

		private Validator validator;

		private String presenterString;

		private Button btnAdd;
		
		private HorizontalLayout btnLayout;
		private BeanItemContainer<SelectValue> replySelectContainer;
		

		private SearchCVCAuditActionTableDTO bean;
		
		private boolean queryNotAcceptedForMA = true;
		
		public List<SearchCVCAuditClsQryTableDTO> getValues() {
			
			if(table.getItemIds() != null && !table.getItemIds().isEmpty()) {
				return (List<SearchCVCAuditClsQryTableDTO>)table.getItemIds();
			}
			
			return new ArrayList<SearchCVCAuditClsQryTableDTO>();
		}

		public void init(String presenterString, SearchCVCAuditActionTableDTO dto) {
			this.presenterString = presenterString;
			bean = dto;
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			this.errorMessages = new StringBuffer("");			
			replySelectContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			replySelectContainer.addBean(new SelectValue(1L,"Error Accepted - Claim Pulled Back"));
			replySelectContainer.addBean(new SelectValue(2L,"Error Not Accepted"));

			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);

			btnAdd = new Button();
			btnAdd.setStyleName("link");
			btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
			btnAdd.setData(dto);
			btnLayout = new HorizontalLayout(btnAdd);
			btnLayout.setWidth("100%");
			btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
			initTable();
			if(dto.isClmAuditHeadUser()){
				if(presenterString.equalsIgnoreCase(SHAConstants.AUDIT_ACTION_SCREEN) 
						&& dto.getRemediationStatus() != null
						&& dto.getRemediationStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED)){
					 boolean isVisible = false;
					 List<SearchCVCAuditClsQryTableDTO> tableList = dto.getMedicalQryList();
					 if(tableList != null && !tableList.isEmpty()){
						 for (SearchCVCAuditClsQryTableDTO searchCVCAuditClsQryTableDTO : tableList) {
							if(!(searchCVCAuditClsQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_APPROVAL_PENDING) || (searchCVCAuditClsQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING)))){
								isVisible = true;
							}else{
								isVisible = false;
							}
						}
					 }
					 if(isVisible){						 
						 layout.addComponent(btnLayout);
						 addListener();
					 }
				
				}else if(presenterString.equalsIgnoreCase(SHAConstants.AUDIT_ACTION_SCREEN) 
						&& dto.getRemediationStatus() != null
						&&  dto.getRemediationStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED_SAVED)){
					 boolean isVisible = false;
					 List<SearchCVCAuditClsQryTableDTO> tableList = dto.getMedicalQryList();
					 if(tableList != null && !tableList.isEmpty()){
						 for (SearchCVCAuditClsQryTableDTO searchCVCAuditClsQryTableDTO : tableList) {
							if(!(searchCVCAuditClsQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_APPROVAL_PENDING) || (searchCVCAuditClsQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING)))){
								isVisible = true;
							}else{
								isVisible = false;
							}
						}
					 }
					 if(isVisible){						 
						 layout.addComponent(btnLayout);
						 addListener();
					 }
				}
			}	
			else {
				layout.removeComponent(btnLayout);
			}
			if(presenterString.equalsIgnoreCase(SHAConstants.AUDIT_ACTION_SCREEN)  && dto.getTabStatus() != null && dto.getTabStatus().equalsIgnoreCase("pending")){
				layout.removeComponent(btnLayout);
			}
			
			table.setWidth("100%");
			table.setPageLength(table.getItemIds().size());
			layout.addComponent(table);

			setCompositionRoot(layout);
			queryNotAcceptedForMA = true;
		}

		private void addListener() {
			
			btnAdd.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 5852089491794014554L;

				@Override
				public void buttonClick(ClickEvent event) {
					SearchCVCAuditClsQryTableDTO qryTableDTO = new SearchCVCAuditClsQryTableDTO();
					SearchCVCAuditActionTableDTO dto = (SearchCVCAuditActionTableDTO) btnAdd.getData();
					if(dto.getRemediationStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED) || dto.getRemediationStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED_SAVED) ){
					generateDeleteBtnColumn();
					btnLayout.setVisible(false);
					qryTableDTO.setQryStatus(dto.getRemediationStatus());
					qryTableDTO.setClmAuditHeadUser(dto.isClmAuditHeadUser());
					qryTableDTO.setsNo(data.getItemIds().size() + 1); 
					BeanItem<SearchCVCAuditClsQryTableDTO> addItem = data
							.addItem(qryTableDTO);
					}
				}
			});
		}

		public void setReferenceData(Map<String, Object> referenceData) {
			this.referenceData = referenceData;
		}

		@SuppressWarnings("deprecation")
		void initTable() {
			// Create a data source and bind it to a table
			table = new Table("Query Remarks (Medical)", data);
			table.addStyleName("generateColumnTable");
			table.setWidth("100%");
			table.setPageLength(table.getItemIds().size());

			// Added for table height..
			table.setHeight("160px");

		/*	private String medicalAuditQryRemarks;
			private String medicalAuditQueryRaisedDtStr;
			private String medicalAuditQryReplyRemarks;
			private String medicalAuditQryReplyBy;
			private String medicalAuditQryReplyRole;
			private String medicalAuditQryReplyDtStr;*/
			

			table.setVisibleColumns(new Object[] { "sNo", "medicalAuditQryRemarks","medicalAuditQueryRaisedDtStr","claimsReplySelect",
					"medicalAuditQryReplyRemarks","medicalAuditQryReplyBy","medicalAuditQryReplyRole","medicalAuditQryReplyDtStr" });
			
				

			table.setColumnHeader("sNo", "S.No.");
			table.setColumnHeader("medicalAuditQryRemarks", "Query Remarks (Medical)");
			table.setColumnHeader("medicalAuditQueryRaisedDtStr", "Query Raised Dt & Time");
			table.setColumnHeader("claimsReplySelect", "Claims Reply");
			table.setColumnHeader("medicalAuditQryReplyRemarks", "Claims Reply Remarks");
			table.setColumnHeader("medicalAuditQryReplyBy", "Reply By");
			table.setColumnHeader("medicalAuditQryReplyRole", "Reply Role");
			table.setColumnHeader("medicalAuditQryReplyDtStr", "Query Reply Date");
			
			table.setColumnWidth("claimsReplySelect", 285);
			table.setColumnWidth("medicalAuditQryReplyBy",250);
			
			table.setEditable(true);
			table.setSelectable(true);
			table.setTableFieldFactory(new ImmediateFieldFactory());

		}

		public class ImmediateFieldFactory extends DefaultFieldFactory {
			private static final long serialVersionUID = -2192723245525925990L;

			@Override
			public Field<?> createField(Container container, Object itemId,
					Object propertyId, Component uiContext) {
				final SearchCVCAuditClsQryTableDTO tableDto = (SearchCVCAuditClsQryTableDTO) itemId;
				
				Map<String, AbstractField<?>> tableRow = null;

				if (tableItem.get(tableDto) == null) {
					tableRow = new HashMap<String, AbstractField<?>>();
					tableItem.put(tableDto, new HashMap<String, AbstractField<?>>());
				} else {
					tableRow = tableItem.get(tableDto);
					if (tableRow.get(propertyId) != null)
						return tableRow.get(propertyId);
				}
				tableRow = tableItem.get(tableDto);
		
				if ("medicalAuditQryRemarks".equals(propertyId)) {

					TextArea claimAuditQueryRemarks = new TextArea();
					if(tableDto.getQryKey() == null || tableDto.getQryKey() == 0){
						claimAuditQueryRemarks.setId("medicalQry1");
						claimAuditQueryRemarks.setReadOnly(false);
					}
					else{
						claimAuditQueryRemarks.setId("medicalQry");						
						claimAuditQueryRemarks.setValue(tableDto.getMedicalAuditQryRemarks());
						claimAuditQueryRemarks.setReadOnly(true);
						setEnabledDisabledFields(claimAuditQueryRemarks,tableDto);
					}
					claimAuditQueryRemarks.setData(tableDto);
					claimAuditQueryRemarks.setNullRepresentation("");
					claimAuditQueryRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
					handleTextAreaPopup(claimAuditQueryRemarks,null);
					tableRow.put("medicalAuditQryRemarks", claimAuditQueryRemarks);
					return claimAuditQueryRemarks;					 				 
					
				}
				else if ("medicalAuditQueryRaisedDtStr".equals(propertyId)) {
					TextField qryRaiseDt = new TextField();
					qryRaiseDt.setData(tableDto);
					qryRaiseDt.setWidth("100%");
					qryRaiseDt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					qryRaiseDt.setNullRepresentation("");
					qryRaiseDt.setValue(tableDto.getMedicalAuditQueryRaisedDtStr());
					qryRaiseDt.setEnabled(false);
					tableRow.put("medicalAuditQueryRaisedDtStr", qryRaiseDt);
					return qryRaiseDt;
				} 
				
				else if ("medicalAuditQryReplyRemarks".equals(propertyId)) {
					TextArea claimAuditQueryRplyRemarks = new TextArea();
					claimAuditQueryRplyRemarks.setId("qryRplRemarks");
					claimAuditQueryRplyRemarks.setData(tableDto);
					claimAuditQueryRplyRemarks.setNullRepresentation("");
					claimAuditQueryRplyRemarks.setValue(tableDto.getMedicalAuditQryReplyRemarks());
					claimAuditQueryRplyRemarks.setReadOnly(true);
					setEnabledDisabledFields(claimAuditQueryRplyRemarks,tableDto);
					claimAuditQueryRplyRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
					handleTextAreaPopup(claimAuditQueryRplyRemarks,null);
					tableRow.put("claimAuditQueryRemarks", claimAuditQueryRplyRemarks);

					return claimAuditQueryRplyRemarks;					 
					
				} else if ("medicalAuditQryReplyBy".equals(propertyId)) {
					TextField queryRplyBy = new TextField();
					queryRplyBy.setWidth("200px");
					queryRplyBy.setEnabled(false);
					queryRplyBy.setData(tableDto);
					queryRplyBy.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					queryRplyBy.setNullRepresentation("");
					queryRplyBy.setValue(tableDto.getMedicalAuditQryReplyBy());
					queryRplyBy.setEnabled(false);
					tableRow.put("medicalAuditQryReplyBy", queryRplyBy);
					return queryRplyBy;					 
					
				}
				else if ("medicalAuditQryReplyRole".equals(propertyId)) {
					TextField queryRplyRole = new TextField();
					queryRplyRole.setId("cashlessQry");
					queryRplyRole.setEnabled(false);
					queryRplyRole.setData(tableDto);
					queryRplyRole.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					queryRplyRole.setNullRepresentation("");
					queryRplyRole.setValue(tableDto.getMedicalAuditQryReplyRole());
					queryRplyRole.setEnabled(false);
					tableRow.put("medicalAuditQryReplyRole", queryRplyRole);
					return queryRplyRole;					 
					
				} 
				else if ("medicalAuditQryReplyDtStr".equals(propertyId)) {
					TextField qryRepyDt = new TextField();
					qryRepyDt.setData(tableDto);
					qryRepyDt.setEnabled(false);
					qryRepyDt.setWidth("100%");
					qryRepyDt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					qryRepyDt.setNullRepresentation("");
					qryRepyDt.setValue(tableDto.getMedicalAuditQryReplyDtStr());
					tableRow.put("medicalAuditQryReplyDtStr", qryRepyDt);
					return qryRepyDt;			 
				}
				else if ("sNo".equals(propertyId)) {
			
					TextField sNo = new TextField();
					sNo.setData(tableDto);
					sNo.setEnabled(false);
					sNo.setWidth("100%");
					sNo.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sNo.setValue(String.valueOf(tableDto.getsNo()));
					tableRow.put("sNo", sNo);
					return sNo;			 
				}
				else if ("claimsReplySelect".equals(propertyId)) {
					GComboBox claimAuditQueryReplySelect = new GComboBox();
					claimAuditQueryReplySelect.setWidth("275px");
					if(presenterString.equalsIgnoreCase(SHAConstants.AUDIT_CLS_QRY_REPLY_SCREEN) 
							|| presenterString.equalsIgnoreCase(SHAConstants.AUDIT_MEDICAL_QRY_REPLY_SCREEN) 
							|| presenterString.equalsIgnoreCase(SHAConstants.AUDIT_BILLING_FA_QRY_REPLY_SCREEN)){
						claimAuditQueryReplySelect.setEnabled(true);

					}else{
						
						claimAuditQueryReplySelect.setEnabled(false);
					}
				
					claimAuditQueryReplySelect.setContainerDataSource(replySelectContainer);
					claimAuditQueryReplySelect.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					claimAuditQueryReplySelect.setItemCaptionPropertyId("value");
					if(replySelectContainer!=null && !replySelectContainer.getItemIds().isEmpty()){
						for(int i=0;i<replySelectContainer.getItemIds().size();i++){
							if (tableDto.getClaimsReply() != null && (tableDto.getClaimsReply()).equalsIgnoreCase(replySelectContainer.getIdByIndex(i).getValue())){
								tableDto.setClaimsReplySelect((SelectValue)replySelectContainer.getIdByIndex(i));
								break;
							}
						}
					}
					if(tableDto.getClaimsReplySelect() != null){
						claimAuditQueryReplySelect.setValue(tableDto.getClaimsReplySelect());
					}else{
						claimAuditQueryReplySelect.setValue(null);
					}
					claimAuditQueryReplySelect.setData(tableDto);
					claimAuditQueryReplySelect.addValueChangeListener(new ValueChangeListener() {
						
						@Override
						public void valueChange(ValueChangeEvent event) {
							// TODO Auto-generated method stub
							GComboBox claimAuditQueryReplySelect = (GComboBox) event.getProperty();
							SearchCVCAuditClsQryTableDTO tableDto = (SearchCVCAuditClsQryTableDTO) claimAuditQueryReplySelect.getData();
							SelectValue replySelect = claimAuditQueryReplySelect.getValue() != null ? (SelectValue)claimAuditQueryReplySelect.getValue() : null;
							if(replySelect != null){
								tableDto.setClaimsReply(replySelect.getValue());
							}
							
						}
					});
					tableRow.put("claimsReplySelect", claimAuditQueryReplySelect);
					return claimAuditQueryReplySelect;					 
					
				}
				else {
					Field<?> field = super.createField(container, itemId,
							propertyId, uiContext);

					if (field instanceof TextField)
						field.setWidth("100%");
					field.setEnabled(false);
					return field;
				}
			}
		}

		
		private void showErrorPopup(ComboBox field, VerticalLayout layout) {
	
			MessageBox.createError().withCaptionCust("Errors")
					.withTableMessage(layout)
					.withOkButton(ButtonOption.caption("OK")).open();
		}


		public void removeAllItems() {
			table.removeAllItems();
		}

		public void addBeanToList(SearchCVCAuditClsQryTableDTO medicalqryDTO) {
			data.addItem(medicalqryDTO);
		}

		public boolean isValid() {
			boolean hasError = false;
			errorMessages= new StringBuffer("");
			@SuppressWarnings("unchecked")
			Collection<SearchCVCAuditClsQryTableDTO> itemIds = (Collection<SearchCVCAuditClsQryTableDTO>) table
					.getItemIds();

			for (SearchCVCAuditClsQryTableDTO bean : itemIds) {

				if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.AUDIT_QRY_APPROVAL_SCREEN)) {
					if(bean.getQryFinalStatus() != null && SHAConstants.AUDIT_QUERY_ACCEPTED.equalsIgnoreCase(bean.getQryFinalStatus())) {
						if(bean.getQryFinalRemarks() == null || (bean.getQryFinalRemarks() != null && bean.getQryFinalRemarks().isEmpty())) {
							errorMessages.append("Please Enter Query Not Accepted Remarks<br>");
							hasError = true;
						}	
					}
					
					if(bean.getQryFinalStatus() != null && SHAConstants.AUDIT_QUERY_NOT_ACCEPTED.equalsIgnoreCase(bean.getQryFinalStatus())) {
						if(bean.getQryFinalRemarks() == null || (bean.getQryFinalRemarks() != null && bean.getQryFinalRemarks().isEmpty())) {
							errorMessages.append("Please Enter Query Not Accepted Remarks<br>");
							hasError = true;
						}	
					}
					if(bean.getQryFinalStatus() == null ) {
						errorMessages.append("Please click Query Accept or Not Accept button<br>");
						hasError = true;
					}
					
				}
				
				if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.CVC_AUDIT_SCREEN)) {
					if(bean.getQryStatus() != null && SHAConstants.AUDIT_QUERY_APPROVAL_PENDING.equalsIgnoreCase(bean.getQryStatus())) {
						if(bean.getMedicalAuditQryRemarks() == null || (bean.getMedicalAuditQryRemarks() != null && bean.getMedicalAuditQryRemarks().isEmpty())) {
							errorMessages.append("Please Enter Medical Query Remarks<br>");
							hasError = true;
						}
					}	
				}
				
				
				if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.AUDIT_MEDICAL_QRY_REPLY_SCREEN)) {
					if(bean.getQryStatus() != null && SHAConstants.AUDIT_QUERY_REPLY_PENDING.equalsIgnoreCase(bean.getQryStatus())) {
						if(bean.getMedicalAuditQryReplyRemarks() == null || (bean.getMedicalAuditQryReplyRemarks() != null && bean.getMedicalAuditQryReplyRemarks().isEmpty())) {
							errorMessages.append("Please Enter Claims Reply Remarks<br>");
							hasError = true;
						}	
					}	
				}
			}
				
			return !hasError;
		}

		public String getErrors() {
			return this.errorMessages.toString();
		}

		public void clearObject() {
			SHAUtils.setClearAuditQryTableItem(tableItem);
			SHAUtils.setClearReferenceData(referenceData);
			masterService = null;
			premedicalService = null;
			data = null;
			errorMessages = null;
			validator = null;
			presenterString = null;
		}

		public void clearTableItems() {
			if (tableItem != null) {
				tableItem.clear();
			}
		}
		
		@SuppressWarnings("unused")
		public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {
		
			ShortcutListener enterShortCut = new ShortcutListener(
					"ShortcutForRemarks", ShortcutAction.KeyCode.F8, null) {
		
				private static final long serialVersionUID = 1L;
				@Override
				public void handleAction(Object sender, Object target) {
					((ShortcutListener) listener).handleAction(sender, target);
				}
			};
			handleShortcutForRemarks(searchField, getShortCutListenerForRemarks(searchField));
		
		}

		public  void handleShortcutForRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
			textField.addFocusListener(new FocusListener() {
		
				@Override
				public void focus(FocusEvent event) {
					textField.addShortcutListener(shortcutListener);
		
				}
			});
			textField.addBlurListener(new BlurListener() {
		
				@Override
				public void blur(BlurEvent event) {
		
					textField.removeShortcutListener(shortcutListener);
		
				}
			});
		}

		private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
		{
			ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {
		
				private static final long serialVersionUID = 1L;
		
				@SuppressWarnings({ "static-access", "deprecation" })
				@Override
				public void handleAction(Object sender, Object target) {
					VerticalLayout vLayout =  new VerticalLayout();
		
					vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
					vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
					vLayout.setMargin(true);
					vLayout.setSpacing(true);
					final TextArea txtArea = new TextArea();
					txtArea.setStyleName("Boldstyle"); 
					txtArea.setValue(txtFld.getValue());
					txtArea.setNullRepresentation("");
					txtArea.setSizeFull();
					txtArea.setWidth("100%");
					txtArea.setMaxLength(4000);
					txtArea.setReadOnly(false);
					txtArea.setRows(25);
		
					txtArea.addValueChangeListener(new ValueChangeListener() {
		
						@Override
						public void valueChange(ValueChangeEvent event) {
							txtFld.setValue(((TextArea)event.getProperty()).getValue());
						}
					});
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
		
					final Window dialog = new Window();
		
					String strCaption = "Remarks";
		
					if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("medicalQry")) {
						strCaption = "Query Remarks (Medical)";
					}			
					else if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("qryRplRemarks")) {
						strCaption = "Claims Reply Remarks";
					}					
					else if(txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("adlQryRpl")) {
						strCaption = "Claims Reply for Additional Query";
					}
					else if (txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("accptRemarks")) {
						strCaption = "Query Accepted Remarks";
					}else if (txtFld.getId() != null && txtFld.getId().equalsIgnoreCase("notAccptRemarks")) {
						strCaption = "Query Not Accepted Remarks";
					} 
					
					dialog.setCaption(strCaption);
		
					dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
					dialog.setWidth("45%");
					dialog.setClosable(true);
		
					dialog.setContent(vLayout);
					dialog.setResizable(true);
					dialog.setModal(true);
					dialog.setDraggable(true);
					dialog.setData(txtFld);
		
					dialog.addCloseListener(new Window.CloseListener() {
						@Override
						public void windowClose(CloseEvent e) {
							dialog.close();
						}
					});
		
					if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
						dialog.setPositionX(450);
						dialog.setPositionY(500);
					}
					getUI().getCurrent().addWindow(dialog);
					okBtn.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 1L;
						@Override
						public void buttonClick(ClickEvent event) {
							dialog.close();
						}
					});	
				}
			};
		
			return listener;
		}
		
		public void generateAcceptNotAcceptBtn(){
			table.removeGeneratedColumn("Accept");
			table.addGeneratedColumn("Accept", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
					SearchCVCAuditClsQryTableDTO dto = (SearchCVCAuditClsQryTableDTO) itemId;
					final Button btnQryAccpt = new Button ("Query Accepted");
					btnQryAccpt.setData(dto);
					btnQryAccpt.setIcon(new ThemeResource("images/yesstatus.png"));
					btnQryAccpt.setStyleName(ValoTheme.BUTTON_BORDERLESS);					
					
					
					btnQryAccpt.addClickListener(new ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							
							SearchCVCAuditClsQryTableDTO dto = (SearchCVCAuditClsQryTableDTO)event.getButton().getData();
							dto.setQryAccptFlag(SHAConstants.YES_FLAG);
							table.removeGeneratedColumn("Query Not Accepted Remarks");
							generateAcceptNotAcceptRemarkColumn("Query Accepted Remarks");
							dto.setQryStatus(SHAConstants.AUDIT_QUERY_REPLY_PENDING);
							bean.setMedicaQryStatus(SHAConstants.AUDIT_QUERY_REPLY_PENDING);
							queryNotAcceptedForMA = false;
						}
					});
					
					return btnQryAccpt;
				}
			});
			table.setColumnWidth("Accept", -1);
			table.removeGeneratedColumn("NotAccept");
			table.addGeneratedColumn("NotAccept", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
					SearchCVCAuditClsQryTableDTO dto = (SearchCVCAuditClsQryTableDTO) itemId;
					final Button btnQryNotAccpt = new Button ("Query Not Accepted");
					btnQryNotAccpt.setData(dto);
					btnQryNotAccpt.setIcon(new ThemeResource("images/cross_mark_black_update.png"));
					btnQryNotAccpt.setStyleName(ValoTheme.BUTTON_BORDERLESS);
					
					btnQryNotAccpt.addClickListener(new ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							
							SearchCVCAuditClsQryTableDTO dto = (SearchCVCAuditClsQryTableDTO)event.getButton().getData();
							dto.setQryAccptFlag(SHAConstants.N_FLAG);
							table.removeGeneratedColumn("Query Accepted Remarks");
							generateAcceptNotAcceptRemarkColumn("Query Not Accepted Remarks");
							dto.setQryStatus(SHAConstants.AUDIT_QUERY_COMPLETED);
							bean.setMedicaQryStatus(SHAConstants.AUDIT_QUERY_COMPLETED);
							queryNotAcceptedForMA = true;
						}
					});
					
					return btnQryNotAccpt;
				}
			});

			table.setColumnWidth("NotAccept", -1);
		}
		public void generateAcceptNotAcceptRemarkColumn(String caption){
			
			
			table.removeGeneratedColumn(caption);
			table.addGeneratedColumn(caption, new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
					TextArea clsQryApprovalRemarks = new TextArea();
					SearchCVCAuditClsQryTableDTO dto = (SearchCVCAuditClsQryTableDTO) itemId;
					dto.setQryFinalRemarks(dto.getMedicalAuditQryRemarks());
					if(caption.equalsIgnoreCase("Query Not Accepted Remarks")){
						clsQryApprovalRemarks.setId("accptRemarks");
						dto.setQryFinalStatus(SHAConstants.AUDIT_QUERY_NOT_ACCEPTED);
					}else{
						clsQryApprovalRemarks.setId("notAccptRemarks");
						dto.setQryFinalStatus(SHAConstants.AUDIT_QUERY_ACCEPTED);
					}
					clsQryApprovalRemarks.setValue(dto.getMedicalAuditQryRemarks());
					clsQryApprovalRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
					clsQryApprovalRemarks.addValueChangeListener(new ValueChangeListener() {
						
						@Override
						public void valueChange(ValueChangeEvent event) {
							dto.setQryFinalRemarks(event.getProperty().getValue() != null ? String.valueOf(event.getProperty().getValue()) : "");
						}
					});
					handleTextAreaPopup(clsQryApprovalRemarks,null);
					return clsQryApprovalRemarks;
				}
			});
			
		}
		
		public void generateDeleteBtnColumn() {
			table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
					final Button deleteButton = new Button("Delete");
					SearchCVCAuditClsQryTableDTO dto = (SearchCVCAuditClsQryTableDTO) itemId;
					deleteButton.setEnabled(true);
					deleteButton.setData(itemId);
					if(dto.getQryKey() == null || dto.getQryKey() == 0) {
						deleteButton.setEnabled(true);
					}
					else {
						deleteButton.setEnabled(false);
					}
					deleteButton.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;

						public void buttonClick(ClickEvent event) {
							final SearchCVCAuditClsQryTableDTO currentItemId = (SearchCVCAuditClsQryTableDTO) event
									.getButton().getData();

							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(
									GalaxyButtonTypesEnum.YES.toString(), "Yes");
							buttonsNamewithType.put(
									GalaxyButtonTypesEnum.NO.toString(), "No");
							HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
									.createConfirmationbox(
											"Do you want to Delete ?",
											buttonsNamewithType);
							Button yesButton = messageBoxButtons
									.get(GalaxyButtonTypesEnum.YES.toString());
							Button noButton = messageBoxButtons
									.get(GalaxyButtonTypesEnum.NO.toString());
							yesButton.addClickListener(new ClickListener() {
								private static final long serialVersionUID = 7396240433865727954L;

								@Override
								public void buttonClick(ClickEvent event) {

									// Confirmed to continue
									SearchCVCAuditClsQryTableDTO dto = (SearchCVCAuditClsQryTableDTO) currentItemId;
									if (dto.getKey() == null) {
										btnLayout.setVisible(true);
										table.removeItem(currentItemId);
									}						

								}
							});
						noButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = 7396240433865727954L;

							@Override
							public void buttonClick(ClickEvent event) {

							}
						});

						}
					});
					return deleteButton;
				}
			});
		}
		
		public void setEnabledDisabledFields(Component comp,SearchCVCAuditClsQryTableDTO tableDto){
			
			if(presenterString.equalsIgnoreCase(SHAConstants.AUDIT_ACTION_SCREEN) 
					|| presenterString.equalsIgnoreCase(SHAConstants.AUDIT_QRY_APPROVAL_SCREEN)){
				
				if(tableDto != null && tableDto.isClmAuditHeadUser()){

					if(tableDto.getQryKey() != null && comp instanceof TextArea && comp.getId() != null && (comp.getId().equalsIgnoreCase("medicalQry") ||  comp.getId().equalsIgnoreCase("qryRplRemarks") )){
						comp.setEnabled(true);
						((TextArea)comp).setReadOnly(true);
					}
					
					if(tableDto.getQryStatus() != null && (tableDto.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_APPROVAL_PENDING))){
			
						if(comp instanceof TextArea && comp.getId() != null && (comp.getId().equalsIgnoreCase("accptRemarks") || comp.getId().equalsIgnoreCase("notAccptRemarks"))){
							comp.setEnabled(true);
							((TextArea)comp).setReadOnly(false);
						}
						else if(comp instanceof TextArea) {
							comp.setEnabled(true);
							((TextArea)comp).setReadOnly(true);
						}
						else {
							comp.setEnabled(false);
						}
					}
					if(tableDto.getQryStatus() != null && (tableDto.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED))){
						if(comp instanceof TextArea 
								&& comp.getId() != null 
								&& comp.getId().equalsIgnoreCase("medicalQry1")
								&& tableDto.getQryKey() == null) {
							comp.setEnabled(true);
							((TextArea)comp).setReadOnly(false);
						}						
					}					
					
				}else if(tableDto != null && !tableDto.isClmAuditHeadUser() ) {
					if(tableDto.getQryStatus() != null 
							&& (tableDto.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_APPROVAL_PENDING)
									|| tableDto.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING)
									|| tableDto.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED)
									|| tableDto.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED))){
						if(comp instanceof TextArea && comp.getId() != null && (comp.getId().equalsIgnoreCase("medicalQry") || comp.getId().equalsIgnoreCase("qryRplRemarks"))){
							comp.setEnabled(true);
							((TextArea)comp).setReadOnly(true);
						}
						else if(comp instanceof TextArea) {
							comp.setEnabled(true);
							((TextArea)comp).setReadOnly(true);
						}
						else {
							comp.setEnabled(false);
						}
					}
				}else {
					comp.setEnabled(false);
				}
			}
			else if(presenterString.equalsIgnoreCase(SHAConstants.AUDIT_CLS_QRY_REPLY_SCREEN) 
					|| presenterString.equalsIgnoreCase(SHAConstants.AUDIT_MEDICAL_QRY_REPLY_SCREEN) 
					|| presenterString.equalsIgnoreCase(SHAConstants.AUDIT_BILLING_FA_QRY_REPLY_SCREEN) ){
				
				if(comp instanceof TextArea && comp.getId() != null && (comp.getId().equalsIgnoreCase("qryRplRemarks") && !tableDto.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING))){
					comp.setEnabled(true);
					((TextArea)comp).setReadOnly(true);
				}else if(comp instanceof TextArea && comp.getId() != null && (comp.getId().equalsIgnoreCase("medicalQry") )){
					 comp.setEnabled(true);
					((TextArea)comp).setReadOnly(true);
				}
				if(tableDto.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING) && (comp.getId().equalsIgnoreCase("qryRplRemarks"))){
					 comp.setEnabled(true);
					((TextArea)comp).setReadOnly(false);
				}
			}
		}
		
		//added for jira ID IMSSUPPOR-31174
		public boolean isQueryAcceptedOrNotMA() {
			
			return queryNotAcceptedForMA;
		}
}
	
