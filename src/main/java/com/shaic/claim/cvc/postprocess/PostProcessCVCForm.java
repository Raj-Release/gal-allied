package com.shaic.claim.cvc.postprocess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.cvc.SearchCVCFormDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class PostProcessCVCForm extends ViewComponent{
	
	private BeanFieldGroup<SearchCVCFormDTO> binder;
	
	private SearchCVCFormDTO bean;
	
	private VerticalLayout searchLayuout;
	
	private TextField txtIntimationNumber;
	
	private Button pickBtn;
	
	private ComboBox year;
	
	private Searchable searchable;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	public void addSearchListener(Searchable searable)
	{
		this.searchable = searable;
	}
	
	public SearchCVCFormDTO getSearchDTO()
	{
		try {
			this.binder.commit();
			//SearchCVCFormDTO bean = this.binder.getItemDataSource().getBean();
			return  bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@PostConstruct
	public void init() {
		initBinder();
		bean = new SearchCVCFormDTO();
		searchLayuout  = new VerticalLayout();
		Panel preauthPremedicalPanel	= new Panel();
		//Vaadin8-setImmediate() preauthPremedicalPanel.setImmediate(false);
		preauthPremedicalPanel.setCaption("Post Claim Process Audit");
		preauthPremedicalPanel.addStyleName("panelHeader");
		preauthPremedicalPanel.addStyleName("g-search-panel");
		preauthPremedicalPanel.setContent(buildPreauthSearchLayout());
		preauthPremedicalPanel.setHeight("250px");
		preauthPremedicalPanel.setWidth("100.0%");
		searchLayuout.addComponent(preauthPremedicalPanel);
		searchLayuout.setMargin(false);
		setCompositionRoot(searchLayuout);
		addListener();
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchCVCFormDTO>(SearchCVCFormDTO.class);
		this.binder.setItemDataSource(new SearchCVCFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	private HorizontalLayout buildPreauthSearchLayout() 
	{
		VerticalLayout verticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		verticalLayout.setWidth("100.0%");
		verticalLayout.setMargin(false);	
		verticalLayout.setSpacing(false);
		
		txtIntimationNumber = new TextField("Intimation Number");
		bean.setIntimationNo(txtIntimationNumber.getValue());
		
		pickBtn = new Button();
		pickBtn.setCaption("Pick");
		pickBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		pickBtn.setWidth("-1px");
		pickBtn.setHeight("-10px");
		
		year = new ComboBox("Year");
		BeanItemContainer<SelectValue> policyYearValues = getPolicyYearValues();
		year.setContainerDataSource(policyYearValues);
		year.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		year.setItemCaptionPropertyId("value");
		year.setId("year");
		List<SelectValue> itemIds2 = policyYearValues.getItemIds();
		if(itemIds2 != null && ! itemIds2.isEmpty()){
			SelectValue selectValue = itemIds2.get(0);
			year.setValue(selectValue);
		}
		bean.setYear(year.getValue().toString());
		
//		HorizontalLayout intimationLayout = new HorizontalLayout(txtIntimationNumber,txtReason,pickBtn); 
		VerticalLayout layout1 = new VerticalLayout(txtIntimationNumber,year);
		layout1.setSizeFull();
		layout1.setSpacing(true);
		HorizontalLayout dummy1 = new HorizontalLayout();
		HorizontalLayout dummy2 = new HorizontalLayout();
		VerticalLayout layout2 = new VerticalLayout();
		layout2.setSizeFull();
		layout2.setSpacing(true);
		FormLayout dummy = new FormLayout();
		VerticalLayout layout3 = new VerticalLayout(layout1,layout2, pickBtn);
		layout3.setSizeFull();
		layout3.setSpacing(true);
		HorizontalLayout finalLayout = new HorizontalLayout(layout3);

		finalLayout.setSpacing(true);
		finalLayout.setMargin(true);
		verticalLayout.setHeight("130px");
		finalLayout.addStyleName("g-search-panel");
		
		setRequiredAndValidation(txtIntimationNumber);
		setRequiredAndValidation(year);
		mandatoryFields.remove(txtIntimationNumber);
		mandatoryFields.add(txtIntimationNumber);
		mandatoryFields.add(year);
		
		return finalLayout; 
		
	}
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}

	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {
	
		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
	
	public BeanItemContainer<SelectValue> getPolicyYearValues(){
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		Calendar instance = Calendar.getInstance();
		//ADDED FOR FY APR - MAR
		int month = instance.get(Calendar.MONTH);
		if(month >= 3){
			instance.add(Calendar.YEAR, 1);
		}
		/*instance.add(Calendar.YEAR, 1);*/
		int intYear = instance.get(Calendar.YEAR);
		Long year = Long.valueOf(intYear);
		for(Long i= year;i>=year-13;i--){
			SelectValue selectValue = new SelectValue();
			Long j = i-1;
			selectValue.setId(j);
			selectValue.setValue(""+i.intValue());
			selectValueList.add(selectValue);
		}
		container.addAll(selectValueList);
		
		return container;
	}
	
	@SuppressWarnings("deprecation")
	public void addListener() {
		
		pickBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatePage())
				{
					if(null != txtIntimationNumber && txtIntimationNumber.getValue() != null){
						if (year != null && year.getValue() != null && year.getValue() != "") {
							SelectValue selectYear = (SelectValue) year.getValue();
						    if(selectYear!=null && selectYear.getValue()!=null){
						    	bean.setYear(selectYear.getValue());
						      }
						  }
						bean.setIntimationNo(txtIntimationNumber.getValue());
						String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
						searchable.doSearch();
					}	
				}

			}
		});
		
	}
	
	public boolean validatePage() {
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
	
		if (txtIntimationNumber != null
				&& txtIntimationNumber.getValue() == null
				|| (txtIntimationNumber.getValue() != null && txtIntimationNumber
						.getValue().length() == 0)) {
			hasError = true;
			eMsg.append("Please Enter Intimation Number </br>");
		}
		
		if (year != null
				&& year.getValue() == null) {
			hasError = true;
			eMsg.append("Please Select Year </br>");
		}

		if (hasError) {
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			
			Button btn = new Button("Ok");
			btn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			btn.setWidth("-1px");
			btn.setHeight("-10px");
			layout.addComponent(btn);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			btn.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					resetAlltheValues();
					dialog.close();
				}
			});

			hasError = true;
			return !hasError;
		} 
		return true;
				
	}

	public void resetAlltheValues(){
		txtIntimationNumber.setValue("");
		bean.setIntimationNo("");
	}

}
