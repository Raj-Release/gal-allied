package com.shaic.claim.processdatacorrectionhistorical.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationFormDTO;
import com.shaic.domain.MasUser;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class SearchProcessDataCorrectionHistoricalForm extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8200083919150077622L;

	private BeanFieldGroup<SearchProcessTranslationFormDTO> binder;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	private TextField txtIntimationNo;

	private Button getNextBtn;

	private Searchable searchable;
	
	private Button resetBtn;
	
	private Button searchBtn;

	private VerticalLayout buildSearchDataCorrectionLayuoutLayout ;
	
	private Boolean manualCoadingFlag = false;
	
	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}
	
	public SearchProcessTranslationFormDTO getSearchDTO() {
		try {
			this.binder.commit();
			SearchProcessTranslationFormDTO bean = this.binder.getItemDataSource().getBean();
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	@PostConstruct
	public void init() {
		
		initBinder();
		buildSearchDataCorrectionLayuoutLayout  = new VerticalLayout();
		Panel processRejectionPanel	= new Panel();
		processRejectionPanel.setWidth("100%");
		processRejectionPanel.setCaption("Data Validation Historical");
		processRejectionPanel.addStyleName("panelHeader");
		processRejectionPanel.addStyleName("g-search-panel");
		processRejectionPanel.setContent(buildDataCorrectionSearchLayuout(manualCoadingFlag));
		buildSearchDataCorrectionLayuoutLayout.addComponent(processRejectionPanel);
		buildSearchDataCorrectionLayuoutLayout.setComponentAlignment(processRejectionPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildSearchDataCorrectionLayuoutLayout);
		//addListener();

	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<SearchProcessTranslationFormDTO>(SearchProcessTranslationFormDTO.class);
		this.binder.setItemDataSource(new SearchProcessTranslationFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		MasUser masUser = masterService.getMasUserByID(userId);
		if(masUser !=null && masUser.getManualCodingFlag() !=null 
				&& masUser.getManualCodingFlag().equals("Y")){
			manualCoadingFlag =true;
		}
	}

	private VerticalLayout buildDataCorrectionSearchLayuout(Boolean manualCoadingFlag) {

		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setWidth("100.0%");
		verticalLayout.setMargin(false);		 
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("100px");	

		getNextBtn = new Button();
		getNextBtn.setCaption("Get Next");
		getNextBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		getNextBtn.setWidth("-1px");
		getNextBtn.setHeight("-10px");
		getNextBtn.setDisableOnClick(true);
		absoluteLayout_3.addComponent(getNextBtn, "top:27.0px;left:20.0px;");
//		if(manualCoadingFlag){
//			
//			txtIntimationNo = new TextField("Intimation No");
//			txtIntimationNo.setNullRepresentation("");
//
//			FormLayout formLayout1 = new FormLayout(txtIntimationNo);
//			HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1);
//			searchFormLayout.setMargin(true);
//			searchFormLayout.setWidth("100%");
//			absoluteLayout_3.addComponent(searchFormLayout, "left: 85px;right: 0px;");
//
//			searchBtn = new Button();
//			searchBtn.setCaption("Search");
//			searchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//			searchBtn.setWidth("-1px");
//			searchBtn.setHeight("-10px");
//			absoluteLayout_3.addComponent(searchBtn, "top:27.0px;left:410.0px;");
//
//			resetBtn = new Button();
//			resetBtn.setCaption("Reset");
//			resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
//			resetBtn.setWidth("-1px");
//			resetBtn.setHeight("-10px");
//			absoluteLayout_3.addComponent(resetBtn, "top:27.0px;left:490.0px;");
//		}
		addListener();
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("600px");

		return verticalLayout; 

	}

	
	public void addListener() {
		
		getNextBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				getNextBtn.setEnabled(true);
				searchable.doSearch();
			}
		});

/*		if(manualCoadingFlag){
			searchBtn.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					if(txtIntimationNo.getValue() != null && !txtIntimationNo.isEmpty()) {
						String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
						fireViewEvent(SearchProcessDataCorrectionHistoricalPresenter.SEARCH_INTIMATION_DATA_CORRECTION, txtIntimationNo.getValue(),userName);
					}else {
						Label label = new Label("Please enter Intimation No.", ContentMode.HTML);
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
			});
			resetBtn.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					System.out.println("==before---reset---");
					resetAlltheValues();
				}
			});
		}*/
		
	}
	
	public void resetAlltheValues() 
	{
		
		Iterator<Component> componentIterator = buildSearchDataCorrectionLayuoutLayout.iterator();
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
					//Method to reset search table.
					removeTableFromLayout();
				}
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
