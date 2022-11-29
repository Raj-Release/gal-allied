package com.shaic.feedback.managerfeedback.previousFeedback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import com.google.gwt.user.client.ui.MenuItem;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.feedback.managerfeedback.FeedbackStatsDto;
import com.shaic.feedback.managerfeedback.ManagerFeedBackPolicyTableDto;
import com.shaic.feedback.managerfeedback.ManagerFeedBackUIPresenter;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class BranchManagerPreviousFeedbackUI extends SearchComponent<BranchManagerPreviousFeedbackSearchDTO> {

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
	private BranchManagerPreviousFeedbackSearchDTO previousFeedbackDTO = new BranchManagerPreviousFeedbackSearchDTO();
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	private Button homePageButton;
		
	@PostConstruct
	public void init() {
		initBinder();
		mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		//mainPanel.setCaption("Previous Feedback");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout() {
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(false);
		//btnReset.setCaption(SearchComponent.QUICK_SEARCH_RESET_CAPTION);
		mainVerticalLayout = new VerticalLayout();
		homePageButton = new Button("Home");
		homePageButton.setStyleName("backgroundColour");
	//	homePageButton.setIcon(new ThemeResource("images/homeIcon.png"));
		HorizontalLayout homePageLayout = new HorizontalLayout(homePageButton);
		homePageButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Collection<Window> windows = UI.getCurrent().getWindows();
				for (Window component : windows) {
					component.close();
				}
				
				fireViewEvent(MenuPresenter.SHOW_BRANCH_MANAGER_FEEDBACK_HOME_PAGE, null);
				
				
				
			}
		});
		homePageLayout.setMargin(false);
		homePageLayout.setSpacing(false);
		
		feedback = binder.buildAndBind("Feedback","feedbackValue",ComboBox.class);
		//Vaadin8-setImmediate() feedback.setImmediate(true);
		zone = binder.buildAndBind("Zone","zoneValue",ComboBox.class);
		branch = binder.buildAndBind("Branch","branchValue",ComboBox.class);
		feedbackStatus = binder.buildAndBind("Feedback Status","feedbackStatusValue",ComboBox.class);
		feedbackType = binder.buildAndBind("Feedback Type","feedbackTypeValue",ComboBox.class);
		fromDate = binder.buildAndBind("From Date","fromDate",DateField.class);
		toDate = binder.buildAndBind("To Date","toDate",DateField.class);
		FormLayout formLayoutLeft1 = new FormLayout(feedback,fromDate);
		formLayoutLeft1.setSpacing(true);
		formLayoutLeft1.setMargin(false);
		FormLayout formLayoutLeft2 = new FormLayout(feedbackStatus,toDate);
		formLayoutLeft2.setSpacing(true);
		formLayoutLeft2.setMargin(false);
		FormLayout formLayoutLeft3 = new FormLayout(feedbackType);
		formLayoutLeft3.setSpacing(true);
		formLayoutLeft3.setMargin(false);		
		FormLayout formLayoutLeft4 = new FormLayout(homePageButton);
		formLayoutLeft4.setComponentAlignment(homePageButton, Alignment.TOP_RIGHT);
		formLayoutLeft4.setSpacing(true);
		formLayoutLeft4.setMargin(false);
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft1,formLayoutLeft2,formLayoutLeft3,formLayoutLeft4);		
		fieldLayout.setMargin(false);
		fieldLayout.setSizeFull();	
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.addComponents(fieldLayout);
		vlayout.setMargin(false);
		//vlayout.setComponentAlignment(homePageLayout, Alignment.TOP_RIGHT);
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.addComponent(vlayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:75.0px;left:520.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:75.0px;left:599.0px;");
		btnReset.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				resetAllValues();
				
				
				
			}
		});
		
		 mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		// mainVerticalLayout.setWidth("800px");
		 mainVerticalLayout.setMargin(true);
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("105px");
		 
		 mandatoryFields.add(feedback);
		 showOrHideValidation(false);
		 addListener();				
		return mainVerticalLayout;
		
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
														field.setValue(null);
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

	private void removeTableFromLayout() {
		// TODO Auto-generated method stub
		if(null != searchable)
		{
			searchable.resetSearchResultTableValues();
		}
		
	}

	private void initBinder()
	{
		this.binder = new BeanFieldGroup<BranchManagerPreviousFeedbackSearchDTO>(BranchManagerPreviousFeedbackSearchDTO.class);
		this.binder.setItemDataSource(new BranchManagerPreviousFeedbackSearchDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setDropDownValues(BeanItemContainer<SelectValue> feedbackValue,
			BeanItemContainer<SelectValue> feedbackStatusValue, BeanItemContainer<SelectValue> feedbackTypeValue) 
	{
		feedbackValue.sort(new Object[] {"value"}, new boolean[] {true});
		feedback.setContainerDataSource(feedbackValue);
		feedback.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		feedback.setItemCaptionPropertyId("value");
		Collection<SelectValue> feedbackIds = (Collection<SelectValue>) feedback.getContainerDataSource().getItemIds();
	    if(feedbackIds != null && !feedbackIds.isEmpty()) {
	    	feedback.setValue(feedbackIds.toArray()[0]);
	    	feedback.setNullSelectionAllowed(false);
		}
		/*zone.setContainerDataSource(zoneValue);
		zone.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		zone.setItemCaptionPropertyId("value");*/
		
		/*branch.setContainerDataSource(branchValue);
		branch.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		branch.setItemCaptionPropertyId("value");*/
	    feedbackStatusValue.sort(new Object[] {"value"}, new boolean[] {true});
		feedbackStatus.setContainerDataSource(feedbackStatusValue);
		feedbackStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		feedbackStatus.setItemCaptionPropertyId("value");
		Collection<SelectValue> feedBackStatusIds = (Collection<SelectValue>) feedbackStatus.getContainerDataSource().getItemIds();
	    if(feedBackStatusIds != null && !feedBackStatusIds.isEmpty()) {
	    	feedbackStatus.setValue(feedBackStatusIds.toArray()[0]);
	    	feedbackStatus.setNullSelectionAllowed(false);
		}
		
	    feedbackTypeValue.sort(new Object[] {"id"}, new boolean[] {true});
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
	
	public String validate()
	{
		StringBuffer err = new StringBuffer();
		
		if(feedback.getValue() == null)
		{
			err.append("Please select Feedback<br>");
		}	
		
		else 
		{
			if(fromDate.getValue() != null && toDate.getValue() == null){
			err.append("Date Fields are Mandatory, Please provide To Date Value<br>");
			}else if(fromDate.getValue() == null && toDate.getValue() != null){
				err.append("Date Fields are Mandatory, Please provide From Date Value<br>");
			}
			else
			{
				if(fromDate.getValue()!=null && toDate.getValue()!=null)
				{
				 if(toDate.getValue().before(fromDate.getValue()))
				 {
					 err.append("Enter Valid To Date<br>");
				}
				}
			}
		}
		return err.toString();
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			//field.setValidationVisible(isVisible);
		}
	}
	
	
public void buildReviewReplySuccessLayout(final Window popup) {
		
		Label successLabel = new Label("<b style = 'color: green;'>Feedback Submitted Successfully</b>", ContentMode.HTML);
		Button homeButton = new Button("FeedBack Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		//getSession().setAttribute(SHAConstants.TOKEN_ID, null);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				popup.close();
				/*fireViewEvent(MenuItemBean.MANAGER_FEEDBACK, null);
	    		VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);*/
			}
		});
	}

public void showErrorMessage(String errMsg) {
	
	Label successLabel = new Label("<b style = 'color: red;'>" + errMsg + "</b>", ContentMode.HTML);
	VerticalLayout layout = new VerticalLayout(successLabel);
	layout.setSpacing(true);
	layout.setWidth("100%");
	layout.setMargin(true);
	layout.setStyleName("borderLayout");
	
	final ConfirmDialog dialog = new ConfirmDialog();
	dialog.setCaption("");
	dialog.setClosable(true);
	dialog.setContent(layout);
	dialog.setResizable(false);
	dialog.setWidth("12%");
	dialog.setModal(true);
	dialog.show(getUI().getCurrent(), null, true);
}

public void setDropDownValuesHomePage(BeanItemContainer<SelectValue> feedbackValue,
		BeanItemContainer<SelectValue> feedbackStatusValue, BeanItemContainer<SelectValue> feedbackTypeValue,FeedbackStatsDto fbStatusDTO, Long fbStatus) 
{
	feedbackValue.sort(new Object[] {"value"}, new boolean[] {true});
	feedback.setContainerDataSource(feedbackValue);
	feedback.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	feedback.setItemCaptionPropertyId("value");
	Collection<SelectValue> feedbackIds = (Collection<SelectValue>) feedback.getContainerDataSource().getItemIds();
    if(feedbackIds != null && !feedbackIds.isEmpty()) {
    	for (SelectValue selectValue : feedbackIds) {
			if(null != fbStatusDTO && null != fbStatusDTO.getFeedbackAreaKey() && (selectValue.getId().equals(fbStatusDTO.getFeedbackAreaKey()))){
				feedback.setValue(selectValue);
		    	feedback.setNullSelectionAllowed(false);
			}
		}
	}
	
    feedbackStatusValue.sort(new Object[] {"value"}, new boolean[] {true});
	feedbackStatus.setContainerDataSource(feedbackStatusValue);
	feedbackStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	feedbackStatus.setItemCaptionPropertyId("value");
	Collection<SelectValue> feedBackStatusIds = (Collection<SelectValue>) feedbackStatus.getContainerDataSource().getItemIds();
    if(feedBackStatusIds != null && !feedBackStatusIds.isEmpty())
		for (SelectValue selectValue : feedBackStatusIds) {
			if(null != fbStatus && selectValue.getId().equals(fbStatus)){
				feedbackStatus.setValue(selectValue);
		    	feedbackStatus.setNullSelectionAllowed(false);
			}
		}
	
    feedbackTypeValue.sort(new Object[] {"id"}, new boolean[] {true});
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
}
