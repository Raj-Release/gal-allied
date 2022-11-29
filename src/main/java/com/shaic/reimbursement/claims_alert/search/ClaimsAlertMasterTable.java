package com.shaic.reimbursement.claims_alert.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPUploadDocumentsPage;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.reimbursement.topup_policy_master.search.TopUpPolicyMasterTableDTO;
import com.shaic.reimbursement.topup_policy_master.search.TopUpPolicyMasterTable.ImmediateFieldFactory;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.Table.Align;

public class ClaimsAlertMasterTable extends ViewComponent{

	
	private static final long serialVersionUID = 2735912669873415354L;

	private Button addAlert;

	private Table table;

	private Map<ClaimsAlertTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ClaimsAlertTableDTO,HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ClaimsAlertTableDTO> data = new BeanItemContainer<ClaimsAlertTableDTO>(ClaimsAlertTableDTO.class);

	private Map<String, Object> referenceData;
	
	private static Validator validator;
	
	private TextField viewIntitmationNo;
	
	@Inject
	private Instance<ClaimsAlertUploadDocumentPageUI> claimsAlertDocsViewUI;
	
	private ClaimsAlertUploadDocumentPageUI claimsAlertDocsView;
	
	@EJB
	private ClaimsAlertMasterService alertMasterService;
	
	private boolean isVisible; 
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	public void init() {
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		
		
		viewIntitmationNo = new TextField("Intimation No");
		viewIntitmationNo.setNullRepresentation("");
		if(referenceData !=null &&
				referenceData.get(SHAConstants.INTIMATION_NUMBER) !=null){
			String intimationNo = (String) referenceData.get(SHAConstants.INTIMATION_NUMBER);
			viewIntitmationNo.setValue(intimationNo);
			viewIntitmationNo.setEnabled(false);
		}

		FormLayout srchFrm = new FormLayout(viewIntitmationNo);
		srchFrm.setMargin(false);
		addAlert = new Button();
		addAlert.setStyleName("link");
		addAlert.setIcon(new ThemeResource("images/addbtn.png"));

		HorizontalLayout btnLayout = new HorizontalLayout(srchFrm,addAlert);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(srchFrm, Alignment.BOTTOM_LEFT);
		btnLayout.setComponentAlignment(addAlert, Alignment.MIDDLE_RIGHT);

		VerticalLayout layout = new VerticalLayout();
		initTable(layout);
		table.setWidth("100%");
		table.setHeight("350px");
		table.setPageLength(table.getItemIds().size());
		addListener();
		layout.addComponent(btnLayout);
		layout.addComponent(table);
		setCompositionRoot(layout);
	}


	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout) {

		data.removeAllItems();
		table = new Table("",data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		table.setVisibleColumns(new Object[] {"slNo","alertCategory","remarks","enable","disable"});	

		table.setColumnHeader("slNo", "S.No");
		table.setColumnHeader("alertCategory", "Category");
		table.setColumnHeader("remarks", "Remarks");
		table.setColumnHeader("enable", "Enable");
		table.setColumnHeader("disable", "Disable");
		table.setColumnHeader("documents", "Documents");
		
		table.setColumnWidth("slNo",80);
		table.setColumnWidth("alertCategory",160);
		table.setColumnWidth("intimationNo",240);
		table.setColumnWidth("remarks",400);
		
		table.setColumnAlignment("slNo", Align.CENTER);
		table.setColumnAlignment("alertCategory", Align.CENTER);
		table.setColumnAlignment("remarks", Align.CENTER);
		table.setColumnAlignment("enable", Align.CENTER);
		table.setColumnAlignment("disable", Align.CENTER);
		table.setColumnAlignment("Delete", Align.CENTER);
		table.setColumnAlignment("documents", Align.CENTER);

		table.addGeneratedColumn("documents", new Table.ColumnGenerator() {

			private static final long serialVersionUID = -1597325054203615475L;

			@Override
			public Object generateCell(final Table source, final Object itemId,Object columnId) {

				final Button uploadFile = new Button("File Upload");
				uploadFile.setData(itemId);
				uploadFile.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = -4239391193714770396L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						ClaimsAlertTableDTO alertTableDTO = (ClaimsAlertTableDTO) currentItemId;
						if(alertTableDTO.getAlertCategory() != null
								&& alertTableDTO.getAlertCategory().getValue() != null){
							uploadDocument(alertTableDTO);
						}
						else{
							Notification.show("Error", "" + "Please select the Category", Type.ERROR_MESSAGE);
						}
					}
				});
				uploadFile.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				return uploadFile;
			}
		});
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {

			private static final long serialVersionUID = -1597325054203615475L;

			@Override
			public Object generateCell(final Table source, final Object itemId,Object columnId) {

				final Button deleteButton = new Button("Delete");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = -4239391193714770396L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);
						ClaimsAlertTableDTO alertTableDTO = (ClaimsAlertTableDTO) currentItemId;
						if(alertTableDTO.getClaimsAlertKey() != null){
							alertMasterService.deleteClaimsAlertDetails(alertTableDTO);
						}
					}
				});
				deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return deleteButton;
			}
		});

		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	@SuppressWarnings("deprecation")
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,Object propertyId, Component uiContext) {

			ClaimsAlertTableDTO entryDTO = (ClaimsAlertTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null){
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
			tableRow = tableItem.get(entryDTO);	
			if ("slNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(50);
				field.setWidth("40px");
				tableRow.put("slNo", field);
				field.setData(entryDTO);		
				return field;
			}else if("alertCategory".equals(propertyId)) {
				isVisible =false;
				GComboBox box = new GComboBox();
				tableRow.put("alertCategory", box);
				box.setWidth("120px");
				box.setData(entryDTO);		
				addCategoryListener(box);
				addCategoryValues(box);
				if(referenceData !=null &&
						referenceData.get(SHAConstants.INTIMATION_NUMBER) !=null){
					String intimationNo = (String) referenceData.get(SHAConstants.INTIMATION_NUMBER);
					entryDTO.setIntimationNo(intimationNo);
				}
				if(entryDTO !=null
						&& entryDTO.getClaimsAlertKey() !=null){
					isVisible = alertMasterService.validateoncreate(entryDTO);
					if(isVisible){
						box.setEnabled(false);
					}
				}
				generateSlNo();
				return box;
			}else if("remarks".equals(propertyId)) {
				TextArea textarea = new TextArea();
				textarea.setNullRepresentation("");
				textarea.setData(entryDTO);
				tableRow.put("remarks", textarea);
				textarea.setWidth("350px");
				textarea.setHeight("50px");
				textarea.setMaxLength(500);
				textarea.setData(entryDTO);
				addremarksListener(textarea);
				textarea.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(textarea,null,getUI(),SHAConstants.CLAIMS_ALERT);
				if(isVisible){
					textarea.setEnabled(false);
				}
				return textarea;
			} else if("enable".equals(propertyId)) {
				CheckBox box = new CheckBox();
				box.setData(entryDTO);
				tableRow.put("enable", box);
				addEnableListener(box);
				box.setValue(entryDTO.getEnable() != null ? (entryDTO.getEnable() ? true: false) : false);
				return box;
			} else if("disable".equals(propertyId)) {
				CheckBox box = new CheckBox();
				box.setData(entryDTO);
				tableRow.put("disable", box);
				addDisableListener(box);
				if(entryDTO.getDisable() == null && entryDTO.getEnable() == null){
					box.setEnabled(Boolean.FALSE);
				}
				box.setValue(entryDTO.getDisable() != null ? (entryDTO.getDisable() ? true : false ) : false);
				return box;	
			} 
			else {

				Field<?> field = super.createField(container, itemId,propertyId, uiContext);
				if (field instanceof TextField)
					field.setEnabled(false);
				field.setWidth("100%");
				return field;
			}
		}
	}


	public List<ClaimsAlertTableDTO> getValues() {

		List<ClaimsAlertTableDTO> itemIds = (List<ClaimsAlertTableDTO>) this.table.getItemIds() ;
		return itemIds;
	}


	public void addToList(ClaimsAlertTableDTO idaTableDTO) {
		data.addItem(idaTableDTO);
	}

	private void addListener()
	{	
		addAlert.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 3917704885928200648L;

			@Override
			public void buttonClick(ClickEvent event) {
				ClaimsAlertTableDTO claimsAlertTableDTO = new ClaimsAlertTableDTO();
				BeanItem<ClaimsAlertTableDTO> addItem = data.addItem(claimsAlertTableDTO);
			}
		});
	}

	public void addCategoryValues(ComboBox comboBox) {

		BeanItemContainer<SelectValue> categoryContainer = 
				(BeanItemContainer<SelectValue>) referenceData.get(SHAConstants.CLAIMS_ALERT_CATEGORY_CONTAINER);

		comboBox.setContainerDataSource(categoryContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");
	}
	
	private void generateSlNo()
	{
		Collection<ClaimsAlertTableDTO> itemIds = (Collection<ClaimsAlertTableDTO>) table.getItemIds();

		int i = 0;
		for (ClaimsAlertTableDTO alertTableDTO : itemIds) {
			i++;
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(alertTableDTO);
			if(null != hashMap && !hashMap.isEmpty())
			{
				TextField itemNoFld = (TextField)hashMap.get("slNo");
				if(null != itemNoFld)
				{
					itemNoFld.setEnabled(true);
					itemNoFld.setValue(String.valueOf(i)); 
					itemNoFld.setEnabled(false);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void addremarksListener(final TextArea txtField)
	{		
		txtField.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					String value = (String) event.getProperty().getValue();
					ClaimsAlertTableDTO tableDTO = (ClaimsAlertTableDTO)txtField.getData();
					tableDTO.setRemarks(value);
				}
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	private void addEnableListener(final CheckBox chkBox)
	{	
		chkBox.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue()){  

					boolean value = (Boolean) event.getProperty().getValue();
					ClaimsAlertTableDTO tableDTO = (ClaimsAlertTableDTO)chkBox.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);

					if(hashMap != null){
						CheckBox disableCheckBox = (CheckBox) hashMap.get("disable");
						if(value){
							if(disableCheckBox != null){
								if(disableCheckBox.isEnabled())
									disableCheckBox.setValue(Boolean.FALSE);
							}
						}else{
							if(disableCheckBox != null){
								if(disableCheckBox.isEnabled())
									disableCheckBox.setValue(Boolean.TRUE);
							}
						}
					}									
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void addDisableListener(final CheckBox chkBox)
	{	
		chkBox.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue()){  

					boolean value = (Boolean) event.getProperty().getValue();
					ClaimsAlertTableDTO tableDTO = (ClaimsAlertTableDTO)chkBox.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);
					if(hashMap != null){
						CheckBox enableCheckBox = (CheckBox) hashMap.get("enable");						
						if(value){
							if(enableCheckBox != null){
								enableCheckBox.setValue(Boolean.FALSE);
							}							
						}else{
							if(enableCheckBox != null){
								enableCheckBox.setValue(Boolean.TRUE);
							}
						}						
					}															
				}
			}
		});
	}
	
	private void uploadDocument(ClaimsAlertTableDTO alertTableDTO) {

		
		claimsAlertDocsView = claimsAlertDocsViewUI.get();
		claimsAlertDocsView.init(alertTableDTO,false);
		claimsAlertDocsView.setCurrentPage(getUI().getPage());
		
	
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("File Upload");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(claimsAlertDocsView);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	@SuppressWarnings("unused")
	private void addCategoryListener(final GComboBox categoryCombo) {
		if (categoryCombo != null) {
			categoryCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;
				@Override
				public void componentEvent(Event event) {
					GComboBox component = (GComboBox) event.getComponent();
					ClaimsAlertTableDTO  tableDTO= (ClaimsAlertTableDTO) component.getData();
					
					if(tableDTO !=null
							&& tableDTO.getAlertCategory() !=null
							&& tableDTO.getClaimsAlertKey() == null){
						String hasError = alertMasterService.validateonClickCat(tableDTO);
						if(hasError !=null){
							component.clear();
							Notification.show("Error",hasError, Type.ERROR_MESSAGE);	
						}
					}
				}
			});
		}

	}

}
