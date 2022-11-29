package com.shaic.claim.registration.convertClaimToReimbursement.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimFormDto;
import com.shaic.domain.PreauthService;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SearchConvertReimbursementPage extends ViewComponent {

	/**
	 * 
	 */
	
	@Inject
	private SearchConvertReimbursementTable searchResultTable;	
	
	@EJB
	private PreauthService preauthService;
	
	private static final long serialVersionUID = 1L;
	
	private TextField txtIntimationNo;
	
	private TextField txtPolicyNo;
	
	private ComboBox cpuCodeCmb;
	
	private Button convertClaimSearchBtn;
	private Button resetBtn;
	
	private BeanFieldGroup<SearchConvertClaimFormDto> binder;
	
	private VerticalLayout buildConvertClaimSearchLayout;

	private Searchable searchable;
	
	public void addSearchListener(Searchable searable) {
		this.searchable = searable;
	}
	
	public SearchConvertClaimFormDto getSearchDTO() {
		try {
			
			this.binder.commit();
			SearchConvertClaimFormDto bean = this.binder.getItemDataSource()
					.getBean();
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostConstruct
	public void init()
	{
		initBinder();
		//buildPreauthSearchLayout = buildPreauthSearchLayout();
		buildConvertClaimSearchLayout  = new VerticalLayout();
		Panel convertClaimPanel	= new Panel();
		//Vaadin8-setImmediate() convertClaimPanel.setImmediate(false);
		convertClaimPanel.setWidth("100%");
		convertClaimPanel.setHeight("50%");
		convertClaimPanel.setCaption("Convert Claim type to Reimbursement(Search Based)");
		convertClaimPanel.addStyleName("panelHeader");
		convertClaimPanel.addStyleName("g-search-panel");
		convertClaimPanel.setContent(buildConvertClaimLayout());
		buildConvertClaimSearchLayout.addComponent(convertClaimPanel);
		buildConvertClaimSearchLayout.setComponentAlignment(convertClaimPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildConvertClaimSearchLayout);
		addListener();
	}
	
	/*public void init(BeanItemContainer<TmpCPUCode> parameter) {
		initBinder();
		this.cpuCodeValues = parameter;
		VerticalLayout buildConvertClaimForm = buildConvertClaim();
		buildConvertClaimForm.setWidth("100%");
		buildConvertClaimForm.setHeight("100%");
		buildConvertClaimForm.setSpacing(true);
		buildConvertClaimForm.setMargin(true);
		Panel preauthPanel = new Panel("Convert Claim type to Reimbursement");
		preauthPanel.setContent(buildConvertClaimForm);
		setCompositionRoot(preauthPanel);		
		addListener();
	}*/
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<SearchConvertClaimFormDto>(
				SearchConvertClaimFormDto.class);
		this.binder
				.setItemDataSource(new SearchConvertClaimFormDto());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private VerticalLayout buildConvertClaimLayout() {
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("167px");

		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNumber",
				TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNumber",
				TextField.class);
		cpuCodeCmb = binder.buildAndBind("CPU Code", "cpuCode",
				ComboBox.class);
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo,cpuCodeCmb);
		FormLayout formLayout2 = new FormLayout( txtPolicyNo);
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1 ,  formLayout2);
		//searchFormLayout.setComponentAlignment(formLayout1, Alignment.MIDDLE_RIGHT);
		//searchFormLayout.setComponentAlignment(formLayout2, Alignment.MIDDLE_LEFT);

		//HorizontalLayout searchFormLayout = new HorizontalLayout(new FormLayout(txtIntimationNo,cpuCodeCmb),new FormLayout(txtPolicyNo));
		searchFormLayout.setMargin(true);
		searchFormLayout.setWidth("100%");
		absoluteLayout_3.addComponent(searchFormLayout);
		
		convertClaimSearchBtn = new Button();
		convertClaimSearchBtn.setCaption("Search");
		//Vaadin8-setImmediate() convertClaimSearchBtn.setImmediate(true);
		convertClaimSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		convertClaimSearchBtn.setWidth("-1px");
		convertClaimSearchBtn.setHeight("-10px");
		convertClaimSearchBtn.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(convertClaimSearchBtn, "top:120.0px;left:230.0px;");
		//Vaadin8-setImmediate() convertClaimSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:120.0px;left:339.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("650px");
		//verticalLayout.setHeight("250px");
		
		return verticalLayout; 

	}

	public void addListener() {

		convertClaimSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {	
				if(validatePage()){
					convertClaimSearchBtn.setEnabled(true);
					searchable.doSearch();
					searchResultTable.tablesize();
				}else {
					
					showErrorMessage("Please Enter Intimation Number Or Policy Number");
					convertClaimSearchBtn.setEnabled(true);
					
				}
//				fireViewEvent(MenuPresenter.SHOW_CONVERT_CLAIM, tableDto);      //to testing purpose by yosuva
				
				
			}
		});
		
		resetBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//searable.doSearch();
				resetAlltheValues();
			}
		});
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
	
	public void setCpuCode(BeanItemContainer<SelectValue> parameter){		
		//cpuCodeCmb.setContainerDataSource(parameter);
		cpuCodeCmb.setContainerDataSource(parameter);
		cpuCodeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		//cpuCodeCmb.setItemCaptionPropertyId("description");
		cpuCodeCmb.setItemCaptionPropertyId("value");
		
		parameter.sort(new Object[] {"value"}, new boolean[] {true});
		
	}
	
	public Boolean validatePage(){
		Boolean isValid = true;
	
		if( (txtPolicyNo.getValue() == null || txtPolicyNo.getValue().equals("")) 
				&&  (txtIntimationNo.getValue() == null || txtIntimationNo.getValue().equals(""))){
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * Method to reset all form values 
	 *
	 * */
	
	public void resetAlltheValues() 
	{
		
		Iterator<Component> componentIterator = buildConvertClaimSearchLayout.iterator();
			while(componentIterator.hasNext()) 
			{
				Component searchScrnComponent = componentIterator.next() ;
				if(searchScrnComponent instanceof  Panel )
				{	
					Panel panel = (Panel)searchScrnComponent;
					Iterator<Component> searchScrnCompIter = panel.iterator();
					while (searchScrnCompIter.hasNext())
					{
						Component verticalLayoutComp = searchScrnCompIter.next();
						VerticalLayout vLayout = (VerticalLayout)verticalLayoutComp;
						Iterator<Component> vLayoutIter = vLayout.iterator();
						while(vLayoutIter.hasNext())
						{
							Component absoluteComponent = vLayoutIter.next();
							AbsoluteLayout absLayout = (AbsoluteLayout)absoluteComponent;
							Iterator<Component> absLayoutIter = absLayout.iterator();
							while(absLayoutIter.hasNext())
							{
								Component horizontalComp = absLayoutIter.next();
								if(horizontalComp instanceof HorizontalLayout)
								{
									HorizontalLayout hLayout = (HorizontalLayout)horizontalComp;
									Iterator<Component> formLayComp = hLayout.iterator();
									while(formLayComp.hasNext())
									{
										Component formComp = formLayComp.next();
										FormLayout fLayout = (FormLayout)formComp;
										Iterator<Component> formComIter = fLayout.iterator();
									
										while(formComIter.hasNext())
										{
											Component indivdualComp = formComIter.next();
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
												else if(indivdualComp instanceof ComboBox)
												{
													ComboBox field = (ComboBox) indivdualComp;
													field.setValue(null);
												}	 
									// Remove the table if exists..	
									//removeTableFromLayout();
											}
										}
									}
								}
							}
						}
					}
				}
				//Method to reset search table.
				removeTableFromLayout();
			}
		}
	

	private void removeTableFromLayout()
	{
		if(null != searchable)
		{
			searchable.resetSearchResultTableValues();
		}
	}

	public void refresh()
	{
		System.out.println("---inside the refresh----");
		resetAlltheValues();
	}
}

