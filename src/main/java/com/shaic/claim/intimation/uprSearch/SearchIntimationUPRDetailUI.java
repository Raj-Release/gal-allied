package com.shaic.claim.intimation.uprSearch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.intimation.CashLessTableDetails;
import com.shaic.claim.intimation.CashlessTable;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.claim.intimation.search.SearchIntimationPresenter;
import com.shaic.claim.intimation.viewdetails.search.SearchViewDetailIntimationTable;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.newcode.wizard.dto.ClaimStatusDto;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.navigator.View;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@UIScoped
@CDIView
public class SearchIntimationUPRDetailUI extends ViewComponent implements View {
	
	private List<Component> mandatoryFields = new ArrayList<Component>();

	private Panel searchPanel;

	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();

	private VerticalLayout verticalLayout_1;
	
	private AbsoluteLayout btnAbsLayout;

	private Button resetButton;

	private Button searchButton;

	private TextField uprNumber;

	private TextField intimationNumber;

	private VerticalLayout searchTableLayout;
	
	private FormLayout leftForm;
	
	private FormLayout rightForm;
	
	private HorizontalLayout mainHorizantal;

	private static final long serialVersionUID = 1L;
	
	private SearchIntimationFormDto searchIntimationFormDto;
	
	@Inject
	protected SearchIntimationUPRViewDetailTable searchViewDetailResultTable;

	@PostConstruct
	public void init() {

		setSizeFull();
		setHeight("650px");
		searchPanel = buildMainLayout();
		mainPanel.setFirstComponent(searchPanel);
		mainPanel.setSplitPosition(26);
		mainPanel.setSizeFull();
		
		searchViewDetailResultTable.init("", false, false);
		setCompositionRoot(mainPanel);

	}
	
	public VerticalSplitPanel getMainPanel(){
		return mainPanel;
	}

	public void buildSearchIntimationTable(

	Page<PaymentProcessCpuPageDTO> newIntimationDtoPagedContainer) {

		if (newIntimationDtoPagedContainer != null) {
			
			searchTableLayout = new VerticalLayout(searchViewDetailResultTable);

			searchTableLayout.setWidth("100%");
			mainPanel.setSecondComponent(searchViewDetailResultTable);			
		}
		else {
			// empty result

			clearsearchViewDetailResultTable();
		}
	}

	private Panel buildMainLayout() {
		searchPanel = buildSearchPanel();
		searchPanel.setWidth("100%");
		searchPanel.addStyleName("g-search-panel");
		return searchPanel;
	}

	private Panel buildSearchPanel() {

		// common part: create layout
		searchPanel = new Panel();
		searchPanel.setCaption("Search UTR Details");

		searchPanel.setWidth("90.0%");
		searchPanel.setHeight("160px");
		searchPanel.addStyleName("panelHeader");
	
		verticalLayout_1 = buildVerticalLayout_1();

		searchPanel.setContent(verticalLayout_1);

		return searchPanel;
	}

	private VerticalLayout buildVerticalLayout_1() {

		// common part: create layout
		verticalLayout_1 = new VerticalLayout();
		verticalLayout_1.setWidth("100.0%");
		verticalLayout_1.setSpacing(true);

		 mainHorizantal = buildSearchGridLayout();
		 verticalLayout_1.addComponent(mainHorizantal);
		 
		btnAbsLayout = buildCmdButtonLayout();
		verticalLayout_1.addComponent(btnAbsLayout);
		verticalLayout_1.setComponentAlignment(btnAbsLayout,
				Alignment.MIDDLE_CENTER);
		 
		return verticalLayout_1;
	}

	public void clearsearchViewDetailResultTable() {

		if (mainPanel.getSecondComponent() != null) {
			Page a_page = new Page();
			a_page.setPageNumber(1);
			if(searchViewDetailResultTable != null){
				searchViewDetailResultTable.getPageable().setPageNumber(0);
				searchViewDetailResultTable.removeRow();
			}
			
		}
		
	}

	@SuppressWarnings("deprecation")
	 private HorizontalLayout buildSearchGridLayout() {
		
		// intimationNumber
		intimationNumber = new TextField("Intimation Number");
		intimationNumber.setWidth("160px");
		
		intimationNumber.setTabIndex(1);
		intimationNumber.setHeight("-1px");
		intimationNumber.setMaxLength(30);
		CSValidator intimationNumValidator = new CSValidator();
		intimationNumValidator.extend(intimationNumber);
		intimationNumValidator.setPreventInvalidTyping(true);
		intimationNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");

		uprNumber = new TextField("UTR / Cheque / DD Number");
		uprNumber.setWidth("160px");
		uprNumber.setTabIndex(9);
		uprNumber.setHeight("-1px");
		uprNumber.setMaxLength(25);

		CSValidator claimNumValidator = new CSValidator();
		claimNumValidator.extend(uprNumber);
		claimNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		claimNumValidator.setPreventInvalidTyping(true);

		leftForm = new FormLayout(intimationNumber);
		leftForm.setMargin(true);
		rightForm =new FormLayout(uprNumber);
		rightForm.setMargin(true);
		mainHorizantal = new HorizontalLayout(leftForm,rightForm);
		mainHorizantal.setSpacing(true);

		return mainHorizantal;
	}
	
	@SuppressWarnings("deprecation")
	 private AbsoluteLayout buildCmdButtonLayout(){
	
		btnAbsLayout = new AbsoluteLayout();
		btnAbsLayout.setHeight("30px");
	
		// searchButton
		searchButton = new Button();
		searchButton.setCaption("Search");
		searchButton.setWidth("-1px");
		searchButton.setTabIndex(12);
		searchButton.setHeight("-1px");
		searchButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		searchButton.setDisableOnClick(true);
		searchButton.addClickListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {

				clearsearchViewDetailResultTable();

				searchButton.setEnabled(true);
				searchIntimationFormDto = new SearchIntimationFormDto();
				Map<String, Object> filters = new HashMap<String, Object>();
				
				if(isValid()){

					if (intimationNumber.getValue() != null
							&& intimationNumber.getValue() != "") {
						filters.put("intimationNumber",
								intimationNumber.getValue());
					}
					if (uprNumber.getValue() != null
							&& uprNumber.getValue() != "") {
						filters.put("uprNumber",
								uprNumber.getValue());
					}
					
					filters.put("pageable", searchViewDetailResultTable.getPageable());

					searchIntimationFormDto.setFilters(filters);
					clearsearchViewDetailResultTable();

					fireViewEvent(SearchIntimationUPRDetailPresenter.SUBMIT_UPR_SEARCH,
							searchIntimationFormDto);
				
				}else{
					showErrorPopup("<b>Intimation or UTR/Cheque/DD Number is mandatory for search<br></b>");
				}
			}
		});
		
		btnAbsLayout.addComponent(searchButton, "left: 280px; top: -1px;");

		// resetButton
		resetButton = new Button();
		resetButton.setCaption("Reset");
		resetButton.setWidth("-1px");
		resetButton.setHeight("-1px");
		resetButton.setTabIndex(13);
		resetButton.setStyleName(ValoTheme.BUTTON_DANGER);
		resetButton.addClickListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {

				fireViewEvent(SearchIntimationUPRDetailPresenter.RESET_UPR_SEARCH_VIEW, null);
			}
		});
		
		btnAbsLayout.addComponent(resetButton, "left: 390px; top: -1px;");
		
		return btnAbsLayout;

	}
	
	public Boolean isValid(){
		Boolean isFieldEntered = Boolean.FALSE;
		if(intimationNumber != null && intimationNumber.getValue() != null && ! intimationNumber.getValue().equalsIgnoreCase("")){
			isFieldEntered = Boolean.TRUE;
		}
		if(uprNumber != null && uprNumber.getValue() != null && ! uprNumber.getValue().equalsIgnoreCase("")){
			isFieldEntered = Boolean.TRUE;
		}
		
		return isFieldEntered;
	}

	public void showErrorPopup(String errorMessage) {
		Label label = new Label(errorMessage, ContentMode.HTML);
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

		return;
	}
	public void resetSearchIntimationFields() {
		
		VerticalLayout wholeLayout = verticalLayout_1;
		Iterator<Component> i = wholeLayout.getComponentIterator();
		Component c = i.next();
		 Component comp1 = null;
		for (int count = 0; count < wholeLayout.getComponentCount(); count++) {

			 if(c instanceof HorizontalLayout){
				 
				 Iterator<Component> horizontalIter = ((HorizontalLayout) c).getComponentIterator();
					
				 for(int index = 0; index<((HorizontalLayout) c).getComponentCount();index++){
					 comp1 = (Component) horizontalIter.next();
					 
					 if (comp1 instanceof FormLayout) {
						 
						 Iterator<Component> formIter = ((FormLayout) comp1).getComponentIterator();
						 Component field = (Component) formIter.next();
						 
						for(int pos = 0; pos<((FormLayout)comp1).getComponentCount();pos++){
							
							field.setEnabled(true);
							
							if (field instanceof TextField) {
								((TextField) field).setValue("");
								field.setEnabled(true);
							}
		
		
							}
						}
						
				 }
				 if(horizontalIter.hasNext())
				 {
					 comp1 = (Component) horizontalIter.next();
				 }
					 
			 }
		}
		searchViewDetailResultTable.removeRow();
	}

	public void refresh() {
		System.out.println("---inside the refresh----");
		resetSearchIntimationFields();
	}

	public void searchButtonDisable() {
		searchButton.setEnabled(false);
	}	
}