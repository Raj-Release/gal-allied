package com.shaic.claim.viewEarlierRodDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.domain.MasterService;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class EsclateToRawTable extends ViewComponent{


	private Map<EsclateToRawTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<EsclateToRawTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<EsclateToRawTableDTO> data = new BeanItemContainer<EsclateToRawTableDTO>(EsclateToRawTableDTO.class);

	private Table table;
	private Button btnAdd;	
	private List<EsclateToRawTableDTO> deletedList = null;
	private EsclateToRawTableDTO dto;	
	private Map<String, Object> referenceData;
	@Inject
	private MasterService masterService;
	public void init() {

		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("700px");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		deletedList = new ArrayList<EsclateToRawTableDTO>();
		dto = new EsclateToRawTableDTO();
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(btnLayout);
		layout.setMargin(true);
		initTable(layout);
		table.setWidth("900px");
		table.setHeight("200px");
		table.setPageLength(table.getItemIds().size());
		addListener();
		layout.addComponent(table);
		setSizeFull();
		setCompositionRoot(layout);
	}
	
	void initTable(VerticalLayout layout) {
		table = new Table("Escalate To RAW", data);
		data.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());
		table.setVisibleColumns(new Object[] { "rollNo", "esclateCategory","esclateSubCategory","esclateToRawRemarks",});
		table.setColumnHeader("rollNo", "S.No");
		table.setColumnHeader("esclateCategory", "Category");
		table.setColumnHeader("esclateSubCategory", "SubCategory");
		table.setColumnHeader("esclateToRawRemarks", "Remarks for Escalation to RAW");
		table.setEditable(true);
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	Button deleteButton = new Button("Delete");
		    	dto = (EsclateToRawTableDTO)itemId;   		    		    	
		    	deleteButton.setData(itemId);
		    	if(null != dto.getRecordType() && SHAConstants.COMPLETED_CASE.equalsIgnoreCase(dto.getRecordType())){
		    		deleteButton.setEnabled(false);
		    	}
		    	deleteButton.addClickListener(new Button.ClickListener() {
			        /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						dto = (EsclateToRawTableDTO) currentItemId;
						deletedList.add(dto);
						table.removeItem(currentItemId);
			        } 
			    });
		    	return deleteButton;
		      };
		});

		table.setTableFieldFactory(new ImmediateFieldFactory());
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {

		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			EsclateToRawTableDTO esclateRawDTO = (EsclateToRawTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if(tableItem.get(esclateRawDTO) == null)
			{
				tableItem.put(esclateRawDTO,
						new HashMap<String, AbstractField<?>>());
			}else{
				tableRow = tableItem.get(esclateRawDTO);
				if(tableRow.get(propertyId) != null)
				return tableRow.get(propertyId);
			}
			tableRow = tableItem.get(esclateRawDTO);

			if (tableItem.get(esclateRawDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(esclateRawDTO, new HashMap<String, AbstractField<?>>());
			} 
			
			tableRow = tableItem.get(esclateRawDTO);

			 if("rollNo".equals(propertyId)) {
					TextField box = new TextField();
					box.setWidth("20px");
					box.setNullRepresentation("");
					box.setEnabled(true);
					box.setData(esclateRawDTO);
					tableRow.put("rollNo", box);
					box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					return box;
				}
			 else if("esclateCategory".equals(propertyId)) {
				 	GComboBox box = new GComboBox();
				 	box.setData(esclateRawDTO);
					setEsclateCategoryValues(box);
					tableRow.put("esclateCategory", box);
					box.setNullSelectionAllowed(false);
					if(null != esclateRawDTO.getRecordType() && SHAConstants.COMPLETED_CASE.equalsIgnoreCase(esclateRawDTO.getRecordType())){
				 		box.setEnabled(false);
				 	}
					loadSubCategoryBasedOnCategory(box);
					esclateCategoryValidation(box);
					return box;
				}
			 else if("esclateSubCategory".equals(propertyId)) {
				 	GComboBox box = new GComboBox();
					box.setData(esclateRawDTO);
					box.setNullSelectionAllowed(false);
					tableRow.put("esclateSubCategory", box);
					if(null != esclateRawDTO.getRecordType() && SHAConstants.COMPLETED_CASE.equalsIgnoreCase(esclateRawDTO.getRecordType())){
				 		box.setEnabled(false);
				 	}
					/*if(esclateRawDTO.getEsclateCategory() != null && esclateRawDTO.getEsclateCategory().getId() != null){
						setSubCategoryValue(esclateRawDTO, box, esclateRawDTO.getEsclateCategory().getId());
					}*/
					esclateSubCategoryValidation(box);
					return box;
				}
			 else  if("esclateToRawRemarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("300px");
				field.setMaxLength(1000);
				field.setNullRepresentation("");
				field.setData(esclateRawDTO);			
				tableRow.put("esclateToRawRemarks", field);
				if(null != esclateRawDTO.getRecordType() && SHAConstants.COMPLETED_CASE.equalsIgnoreCase(esclateRawDTO.getRecordType())){
					field.setEnabled(false);
			 	}
				addDetailPopupForEsclateRemarks(field, null);
				field.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				
				final TextField txt = (TextField) tableRow.get("rollNo");
				generateSlNo(txt);
				
				return field;
			} 
		 
		 else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
				return field;
			}
		}

		
	}
	
	 public void addBeanToList(EsclateToRawTableDTO benefitsdto) {
		 data.addItem(benefitsdto);

	    }
	 
	 @SuppressWarnings("unchecked")
		public List<EsclateToRawTableDTO> getValues() {
			List<EsclateToRawTableDTO> itemIds = (List<EsclateToRawTableDTO>) this.table.getItemIds() ;
			dto.setDeletedList(deletedList);
	    	return itemIds;
		}
	 
		public void setTableList(final List<EsclateToRawTableDTO> list) {
			table.removeAllItems();
			for (final EsclateToRawTableDTO bean : list) {
				if(null != bean.getEsclateCategory() && null != bean.getEsclateCategory().getId()){
					Long categoryId = bean.getEsclateCategory().getId();
					setSubCategoryValue(bean, null,categoryId);
					}
				table.addItem(bean);
			}
		}
		
		private void addDetailPopupForEsclateRemarks(TextField txtFld, final  Listener listener) {
			  ShortcutListener enterShortCut = new ShortcutListener(
				        "esclateToRawRemarks", ShortcutAction.KeyCode.F8, null) {
					
				      private static final long serialVersionUID = 1L;
				      @Override
				      public void handleAction(Object sender, Object target) {
				        ((ShortcutListener) listener).handleAction(sender, target);
				      }
				    };
				    handleShortcutForTriggerRemarks(txtFld, getShortCutListenerForTriggerRemarks(txtFld));
				    
				  }
		
		public  void handleShortcutForTriggerRemarks(final TextField textField, final ShortcutListener shortcutListener) {
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
		
		
		private ShortcutListener getShortCutListenerForTriggerRemarks(final TextField txtFld)
		{
			ShortcutListener listener =  new ShortcutListener("Description",KeyCodes.KEY_F8,null) {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void handleAction(Object sender, Object target) {
					EsclateToRawTableDTO  searchPedDto = (EsclateToRawTableDTO) txtFld.getData();
					VerticalLayout vLayout =  new VerticalLayout();
					
					vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
					vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
					vLayout.setMargin(true);
					vLayout.setSpacing(true);
					TextArea txtArea = new TextArea();
					txtArea.setMaxLength(1000);
					txtArea.setNullRepresentation("");
					txtArea.setValue(txtFld.getValue());
					
					txtArea.addValueChangeListener(new ValueChangeListener() {
						
						@Override
						public void valueChange(ValueChangeEvent event) {
							TextArea txt = (TextArea)event.getProperty();
							txtFld.setValue(txt.getValue());
						}
					});
					
					
					txtArea.setSizeFull();
					txtArea.setWidth("100%");
					txtArea.setRows(25);
					searchPedDto.setEsclateToRawRemarks(txtArea.getValue());
					
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
					
					final Window dialog = new Window();
					dialog.setCaption("Remarks for Esclation to Raw");
					dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
					dialog.setWidth("45%");
					dialog.setClosable(false);
					
					dialog.setContent(vLayout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.setDraggable(true);
					
					if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
						dialog.setPositionX(250);
						dialog.setPositionY(100);
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

	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				data.addItem(new EsclateToRawTableDTO());

				
			}
		});
	}
	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<EsclateToRawTableDTO> itemIds = (Collection<EsclateToRawTableDTO>) table.getItemIds();
		
		int i = 0;
		 for (EsclateToRawTableDTO esclateRawDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(esclateRawDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("rollNo");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setValue(String.valueOf(i)); 
					 itemNoFld.setEnabled(false);
				 }
			 }
		 }
	}
		
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	private void setEsclateCategoryValues(GComboBox box) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> esclateCategory = (BeanItemContainer<SelectValue>) referenceData.get("category");
		box.setContainerDataSource(esclateCategory);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
	}
	
	private void loadSubCategoryBasedOnCategory(GComboBox categoryBox){
		if(categoryBox != null){
			categoryBox.addListener(new Listener(){
				@Override
				public void componentEvent(Event event) {
					
					GComboBox component = (GComboBox) event.getComponent();
					EsclateToRawTableDTO esclateToRawDTO = (EsclateToRawTableDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(esclateToRawDTO);
					ComboBox subCategoryBox = (ComboBox) hashMap.get("esclateSubCategory");
					if (null != component && null != component.getValue()) {
						SelectValue category = (SelectValue) component.getValue();
						Long categoryId = category.getId();
						setSubCategoryValue(esclateToRawDTO, subCategoryBox,
								categoryId);
					}
				
				}

				
				
			});
		}
	}
	
	private void setSubCategoryValue(
			EsclateToRawTableDTO esclateToRawDTO,
			ComboBox subCategoryBox, Long categoryId) {
		if(null != categoryId){
			BeanItemContainer<SelectValue> esclateSubCategory  = masterService.getRawSubCategoryByRawCategoryKey(categoryId);
			if(null != esclateSubCategory && null != esclateSubCategory.getItemIds() && !esclateSubCategory.getItemIds().isEmpty()){
				esclateToRawDTO.setIsSubCategoryAvailable(Boolean.TRUE);
			}
			else{
				esclateToRawDTO.setIsSubCategoryAvailable(Boolean.FALSE);
			}
			if(null != subCategoryBox){
			subCategoryBox.setContainerDataSource(esclateSubCategory);
			subCategoryBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			subCategoryBox.setItemCaptionPropertyId("value");
			}
		}
	}
	
	private void esclateSubCategoryValidation(GComboBox categoryBox)
	{
		if(categoryBox != null){
		categoryBox.addListener(new Listener(){
		@Override
		public void componentEvent(Event event) {
			boolean hasError = false;
			String eMsg = "";
			@SuppressWarnings("unchecked")
			GComboBox component = (GComboBox) event.getComponent();
			List<EsclateToRawTableDTO> itemIds = (List<EsclateToRawTableDTO>) table.getItemIds();
			Map<Long, String> duplicateItemMap = new HashMap<Long, String>();
			if(null != itemIds && !itemIds.isEmpty())
			{
				for (EsclateToRawTableDTO bean : itemIds) {
					if(null != bean.getEsclateSubCategory()) 
					{
						if(null != duplicateItemMap && !duplicateItemMap.isEmpty() && null != bean.getEsclateSubCategory() && null !=bean.getEsclateSubCategory().getId())
						{
							if(duplicateItemMap.containsKey(bean.getEsclateSubCategory().getId()))
							{
								hasError = true;
								{
									eMsg += bean.getEsclateSubCategory().getValue() +" is already selected";
									component.setValue(null);
								}
								break;
							}
						}
						duplicateItemMap.put(bean.getEsclateSubCategory().getId(), bean.getEsclateSubCategory().getValue());		
					}
				}
			}
				if (hasError) 
				{
			/*		Label label = new Label(eMsg, ContentMode.HTML);
					label.setStyleName("errMessage");
					VerticalLayout layout = new VerticalLayout();
					layout.setMargin(true);
					layout.addComponent(label);
			
					ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Alert");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);*/
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					GalaxyAlertBox.createAlertBox(eMsg, buttonsNamewithType);
					hasError = false;
				} 
			}
		});
	}
}
	private void esclateCategoryValidation(GComboBox categoryBox)
	{
		if(categoryBox != null){
		categoryBox.addListener(new Listener(){
		@Override
		public void componentEvent(Event event) {
			boolean hasError = false;
			String eMsg = "";
			@SuppressWarnings("unchecked")
			GComboBox component = (GComboBox) event.getComponent();
			List<EsclateToRawTableDTO> itemIds = (List<EsclateToRawTableDTO>) table.getItemIds();
			Map<Long, String> duplicateItemMap = new HashMap<Long, String>();
			if(null != itemIds && !itemIds.isEmpty())
			{
				for (EsclateToRawTableDTO bean : itemIds) {
					if(null != bean.getEsclateCategory()) 
					{
						if(null != duplicateItemMap && !duplicateItemMap.isEmpty())
						{
							if(duplicateItemMap.containsKey(bean.getEsclateCategory().getId()) && !bean.getIsSubCategoryAvailable())
							{
								hasError = true;
								{
									eMsg += bean.getEsclateCategory().getValue() + " is already selected";
									component.setValue(null);
								}
								break;
							}
						}
						duplicateItemMap.put(bean.getEsclateCategory().getId(), bean.getEsclateCategory().getValue());		
					}
				}
			}
				if (hasError) 
				{
					/*Label label = new Label(eMsg, ContentMode.HTML);
					label.setStyleName("errMessage");
					VerticalLayout layout = new VerticalLayout();
					layout.setMargin(true);
					layout.addComponent(label);
			
					ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Alert");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);*/
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					GalaxyAlertBox.createAlertBox(eMsg, buttonsNamewithType);
					hasError = false;
				} 
			}
		});
	}
}
	public List<EsclateToRawTableDTO> getDeltedEsclateClaimList() {
		return deletedList;
	}
}
