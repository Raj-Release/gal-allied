package com.shaic.claim.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.table.Searchable;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
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

@UIScoped
public class SearchClaimForm extends ViewComponent {

	@Inject
	private SearchClaimTable searchClaimTable;
	private BeanFieldGroup<SearchClaimFormDTO> binder;

	TextField txtIntimationNo;

	TextField txtPolicyNo;
	
	TextField txtClaimNo;

	Button claimSearchBtn;
	
	Button resetBtn;
	
	private Searchable searchable;
	
	private VerticalLayout buildClaimSearchLayout;
	

	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}
	
	
	public  SearchClaimFormDTO getSearchDTO() {
		try {
			this.binder.commit();
			SearchClaimFormDTO bean = this.binder
					.getItemDataSource().getBean();
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
	}
	


	@PostConstruct
	public void init() {
		initBinder();
		//buildPreauthSearchLayout = buildPreauthSearchLayout();
		buildClaimSearchLayout  = new VerticalLayout();
		Panel searchClaimPanel	= new Panel();
		//Vaadin8-setImmediate() searchClaimPanel.setImmediate(false);
		searchClaimPanel.setWidth("100%");
		searchClaimPanel.setHeight("50%");
		searchClaimPanel.setCaption("Search Claim");
		searchClaimPanel.addStyleName("panelHeader");
		searchClaimPanel.addStyleName("g-search-panel");
		searchClaimPanel.setContent(buildSearchClaimLayout());
		buildClaimSearchLayout.addComponent(searchClaimPanel);
		buildClaimSearchLayout.setComponentAlignment(searchClaimPanel, Alignment.MIDDLE_LEFT);
		Panel mainPanel = new Panel(buildClaimSearchLayout);
		setCompositionRoot(mainPanel);
		
		//refresh();
		
		addListener();

	}

	private  void initBinder(){
		this.binder = new BeanFieldGroup<SearchClaimFormDTO>(
				SearchClaimFormDTO.class);
		this.binder
		.setItemDataSource(new SearchClaimFormDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}
	
	
	private VerticalLayout buildSearchClaimLayout() {
		
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("173px");

			txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
					TextField.class);
			
			txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
					TextField.class);
			
			txtClaimNo = binder.buildAndBind("Claim No", "claimNo",
					TextField.class);
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo,txtPolicyNo);
		FormLayout formLayout2 =new FormLayout(txtClaimNo);
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1 ,  formLayout2);
		searchFormLayout.setSpacing(true);
		//searchFormLayout.setComponentAlignment(formLayout1, Alignment.MIDDLE_RIGHT);
		//searchFormLayout.setComponentAlignment(formLayout2, Alignment.MIDDLE_LEFT);
		
		
		//HorizontalLayout searchFormLayout = new HorizontalLayout(new FormLayout(txtIntimationNo,cpuCodeCmb),new FormLayout(txtPolicyNo));
		searchFormLayout.setMargin(true);
		searchFormLayout.setWidth("700px");
		absoluteLayout_3.addComponent(searchFormLayout);
		
		claimSearchBtn = new Button();
		claimSearchBtn.setCaption("Search");
		//Vaadin8-setImmediate() claimSearchBtn.setImmediate(true);
		claimSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		claimSearchBtn.setWidth("-1px");
		claimSearchBtn.setHeight("-10px");
		claimSearchBtn.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(claimSearchBtn, "top:120.0px;left:230.0px;");
		//Vaadin8-setImmediate() claimSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:120.0px;left:320.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("700px");
		return verticalLayout; 
	}

	public void addListener() {

		claimSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				/*SearchClaimFormDTO searchProcessRejectionFormDTO = new SearchClaimFormDTO();
				fireViewEvent(SearchClaimPresenter.SEARCH_BUTTON_CLICK,
						searchProcessRejectionFormDTO);*/
				if(txtPolicyNo != null && txtClaimNo != null && txtIntimationNo != null &&
						((txtPolicyNo.getValue() != null && ! txtPolicyNo.getValue().equals(""))
								||(txtClaimNo.getValue() != null && ! txtClaimNo.getValue().equals(""))
								|| (txtIntimationNo.getValue() != null && ! txtIntimationNo.getValue().equals("")))){
				claimSearchBtn.setEnabled(true);
				searchable.doSearch();
				searchClaimTable.tablesize();
				}else{
					getErrorMessage("Please Enter Intimation Number or Claim No or Policy No");
					claimSearchBtn.setEnabled(true);
				}

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

	/**
	 * Method to reset all form values 
	 *
	 * */
	
	public void resetAlltheValues() 
	{
		
		Iterator<Component> componentIterator = buildClaimSearchLayout.iterator();
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

