package com.shaic.branchmanagerfeedback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;




import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;


import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class BranchManagerFeedbackUI extends SearchComponent<TechnicalDepartmentFeedbackDTO>{
	private static final long serialVersionUID = 1L;
	//protected BeanFieldGroup<TechnicalDepartmentFeedbackDTO> binder;
	private ComboBox feedback;
	private ComboBox zone;
	private ComboBox branch;
	private ComboBox feedbackStatus;
	private ComboBox feedbackType;
	private DateField fromDate;
	private DateField toDate;
	private VerticalLayout mainVerticalLayout;
	private Panel mainPanel;
	private VerticalLayout generatedLayout;
	BeanItemContainer<SelectValue> branchValue;
	
	@Inject
	private TechnicalTeamReplyOnFeedback technicalTeamReplyOnFeedback;
	
	@PostConstruct
	public void init() {
		initBinder();
		mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Reply To Branch Manager Feedback");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout() {
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(false);
		mainVerticalLayout = new VerticalLayout();
		feedback = binder.buildAndBind("Feedback","feedbackValue",ComboBox.class);
		
		//Vaadin8-setImmediate() feedback.setImmediate(true);
		zone = binder.buildAndBind("Zone","zoneValue",ComboBox.class);
		zone.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox zoneCombo = (ComboBox) event.getProperty();
				if(zoneCombo.getValue()!=null){
					SelectValue zoneSelect = (SelectValue) zoneCombo.getValue();
					fireViewEvent(BranchManagerFeedbackPresenter.ZONE_CODE_BASED_BRANCH, zoneSelect.getId());
				}else {
					defaultBranchContainer();
				}
			}
		});
		branch = binder.buildAndBind("Branch","branchValue",ComboBox.class);
		feedbackStatus = binder.buildAndBind("Feedback Status","feedbackStatusValue",ComboBox.class);
		feedbackType = binder.buildAndBind("Feedback Type","feedbackTypeValue",ComboBox.class);
		fromDate = binder.buildAndBind("From Date","fromDate",DateField.class);
		toDate = binder.buildAndBind("To Date","toDate",DateField.class);
		FormLayout formLayoutLeft1 = new FormLayout(feedback,feedbackStatus,fromDate);
		formLayoutLeft1.setSpacing(true);
		formLayoutLeft1.setMargin(false);
		FormLayout formLayoutLeft2 = new FormLayout(zone,feedbackType,toDate);
		formLayoutLeft2.setSpacing(true);
		formLayoutLeft2.setMargin(false);
		FormLayout formLayoutLeft3 = new FormLayout(branch);
		formLayoutLeft3.setSpacing(true);
		formLayoutLeft3.setMargin(false);
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft1,formLayoutLeft2,formLayoutLeft3);
		fieldLayout.setMargin(true);
		fieldLayout.setSizeFull();		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:110.0px;left:320.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:110.0px;left:390.0px;");
		
		btnReset.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				resetAllValues();
				
			}
		});
		 mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		// mainVerticalLayout.setWidth("800px");
		 mainVerticalLayout.setMargin(false);
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("150px");
		 addListener();
		 
		return mainVerticalLayout;
		
	}
	
	
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<TechnicalDepartmentFeedbackDTO>(TechnicalDepartmentFeedbackDTO.class);
		this.binder.setItemDataSource(new TechnicalDepartmentFeedbackDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setDropDownValues(BeanItemContainer<SelectValue> feedbackValue,
			BeanItemContainer<SelectValue> zoneValue, BeanItemContainer<SelectValue> branchValue,
			BeanItemContainer<SelectValue> feedbackStatusValue, BeanItemContainer<SelectValue> feedbackTypeValue) 
	{
		this.branchValue = branchValue;
		List<SelectValue> filterList = new ArrayList<SelectValue>();
		ImsUser imsUser = (ImsUser)UI.getCurrent().getSession().getAttribute(BPMClientContext.USER_OBJECT);
		if(imsUser != null && imsUser.getUserRoleList() != null){
		
			String[] userRoleList = imsUser.getUserRoleList();
			 List<String> userRoles = Arrays.asList(userRoleList);  
			List<SelectValue> itemIds = feedbackValue.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(selectValue.getId().equals(ReferenceTable.POLICY_FEEDBACK_TYPE_KEY) && userRoles.contains(SHAConstants.FEEDBACK_POLICY_ROLE)){
					filterList.add(selectValue);
				}else if(selectValue.getId().equals(ReferenceTable.PROPOSAL_FEEDBACK_TYPE_KEY) && userRoles.contains(SHAConstants.FEEDBACK_PROPOSAL_ROLE)){
					filterList.add(selectValue);
				}else if(selectValue.getId().equals(ReferenceTable.CLAIM_FEEDBACK_TYPE_KEY) && userRoles.contains(SHAConstants.FEEDBACK_CLAIM_ROLE)){
					filterList.add(selectValue);
				}
			}
		}
		feedbackValue.removeAllItems();
		feedbackValue.addAll(filterList);

		feedback.setContainerDataSource(feedbackValue);
		feedback.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		feedback.setItemCaptionPropertyId("value");
		feedback.setEnabled(false);
		feedback.setId("notAllowedReset");
		
		if(! filterList.isEmpty()){
			feedback.setValue(filterList.get(0));
		}
		
		zone.setContainerDataSource(zoneValue);
		zone.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		zone.setItemCaptionPropertyId("value");
		
		branch.setContainerDataSource(branchValue);
		branch.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		branch.setItemCaptionPropertyId("value");
		
		feedbackStatusValue.sort(new Object[] {"value"}, new boolean[] {true});
		feedbackStatus.setContainerDataSource(feedbackStatusValue);
		feedbackStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		feedbackStatus.setItemCaptionPropertyId("value");
		Collection<SelectValue> feedBackStatusIds = (Collection<SelectValue>) feedbackStatus.getContainerDataSource().getItemIds();
	    if(feedBackStatusIds != null && !feedBackStatusIds.isEmpty()) {
	    	feedbackStatus.setValue(feedBackStatusIds.toArray()[0]);
	    	feedbackStatus.setNullSelectionAllowed(false);
		}
		
		feedbackTypeValue.sort(new Object[] {"value"}, new boolean[] {true});
		feedbackType.setContainerDataSource(feedbackTypeValue);
		feedbackType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		feedbackType.setItemCaptionPropertyId("value");
		Collection<SelectValue> feedBackTypeIds = (Collection<SelectValue>) feedbackType.getContainerDataSource().getItemIds();
	    if(feedBackTypeIds != null && !feedBackTypeIds.isEmpty()) {
		    for (SelectValue selectValue : feedBackTypeIds) {
		    	if(null != selectValue.getId() && ReferenceTable.FEEDBACK_STATUS_TYPE_ALL.equals(selectValue.getId())){
			    	feedbackType.setValue(selectValue);
			    	feedbackType.setNullSelectionAllowed(false);
			    	
				}		
			}	
		   }
				
	}	
	
	public void showPopupContent(BranchManagerFeedbackTableDTO tableDTO){
		String branchManagerName[] = tableDTO.getBranchDetails().split(",");
		tableDTO.setBranchManagerName(branchManagerName[0]);
		tableDTO.setBranchName(branchManagerName[1]);
		
			Window popup = new com.vaadin.ui.Window();
			
			technicalTeamReplyOnFeedback.init(tableDTO);
			popup.setWidth("75%");
			popup.setHeight("95%");
			popup.setContent(technicalTeamReplyOnFeedback);
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
	
public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

public void refresh()
{
	System.out.println("---inside the refresh----");
	if(mainVerticalLayout != null) {
		SHAUtils.resetComponent(mainVerticalLayout);
		//searchable.resetSearchResultTableValues();
	}
	
}

public String validate()
{
	StringBuffer err = new StringBuffer();
	
		if(feedback.getValue() == null)
		{
			err.append("Please select Feedback<br>");
		}		
		else if(fromDate.getValue() != null && toDate.getValue() == null)
		{
			err.append("Date Fields are Mandatory, Please provide To Date Value<br>");
		}
		else if(fromDate.getValue() == null && toDate.getValue() != null)
		{
			err.append("Date Fields are Mandatory, Please provide From Date Value<br>");
		}
		else if(fromDate.getValue()!=null && toDate.getValue()!=null)
		{
			 if(toDate.getValue().before(fromDate.getValue()))
			 {
				 err.append("Enter Valid To Date<br>");
			}
		}
	return err.toString();
}

protected void resetAllValues() {
	// TODO Auto-generated method stub

	
	Iterator<Component> componentIterator = mainPanel.iterator();
		while(componentIterator.hasNext()) 
		{
			Component searchScrnComponent = componentIterator.next() ;
			if(searchScrnComponent instanceof  VerticalLayout )
			{	
				System.out.println("---inside the if block---");
				VerticalLayout panel = (VerticalLayout)searchScrnComponent;
				Iterator<Component> searchScrnCompIter = panel.iterator();
				while (searchScrnCompIter.hasNext())
				{
					Component verticalLayoutComp = searchScrnCompIter.next();
					AbsoluteLayout vLayout = (AbsoluteLayout)verticalLayoutComp;
					Iterator<Component> vLayoutIter = vLayout.iterator();
					while(vLayoutIter.hasNext())
					{
						Component absoluteComponent = vLayoutIter.next();
						
						if(absoluteComponent instanceof HorizontalLayout){
							HorizontalLayout absLayout = (HorizontalLayout)absoluteComponent;
							Iterator<Component> absLayoutIter = absLayout.iterator();
							while(absLayoutIter.hasNext())
							{
								Component horizontalComp = absLayoutIter.next();
								if(horizontalComp instanceof FormLayout)
								{
									FormLayout hLayout = (FormLayout)horizontalComp;
									Iterator<Component> formLayComp = hLayout.iterator();
									while(formLayComp.hasNext())
									{
										/*Component formComp = formLayComp.next();
										FormLayout fLayout = (FormLayout)formComp;
										Iterator<Component> formComIter = fLayout.iterator();*/
									
										/*while(formLayComp.hasNext())
										{*/
											Component indivdualComp = formLayComp.next();
											if(indivdualComp != null) 
											{
												if(indivdualComp instanceof Label) 
												{
													continue;
												}	
												if(indivdualComp instanceof TextField) 
												{
													TextField field = (TextField) indivdualComp;
													field.setValue("");
												} 
												if(indivdualComp instanceof ComboBox)
												{
													ComboBox field = (ComboBox) indivdualComp;
													if(field.getId() == null){
														field.setValue(null);
													}
												}
												if (indivdualComp instanceof DateField) {
													((DateField) indivdualComp).setValue(null);
													
												}
									// Remove the table if exists..	
									//removeTableFromLayout();
											}
										//}
									}
								}
							}
						}
						
					}
				}//Method to reset search table.
				removeTableFromLayout();
			}
		}

	
}

public void removeTableFromLayout(){
	searchable.resetSearchResultTableValues();
}

public void loadBranchDetails(BeanItemContainer<SelectValue> branchValueContainer){
	branch.setContainerDataSource(branchValueContainer);
	branch.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	branch.setItemCaptionPropertyId("value");
}
public void defaultBranchContainer() {
	branch.setContainerDataSource(this.branchValue);
	branch.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	branch.setItemCaptionPropertyId("value");

}
}

