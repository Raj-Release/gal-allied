package com.shaic.claim.rod.searchCriteria;

import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;










import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.wizard.forms.AddBankDetailsTable;
import com.shaic.claim.rod.wizard.forms.AddBanksDetailsTableDTO;
import com.shaic.claim.rod.wizard.forms.BankDetailsTable;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailTableDTO;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class ViewSearchCriteriaViewImpl  extends AbstractMVPView implements ViewSearchCriteriaView{

	
	/**
	 * 
	 */
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ViewSearchCriteriaForm  searchForm;
	
	@Inject
	private ViewSearchCriteriaTable searchResultTable;
	
	
	private VerticalSplitPanel mainPanel;
	
	private Window popup;
	
	private String presenterString;
	
	private AddBankDetailsTable bankDetailsObj;
	
	private EditPaymentDetailsView editPaymentDetailsView;

	private UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO;
	
	private CreateAndSearchLotTableDTO createAndSearchLotTableDTO;
	
	private AddBanksDetailsTableDTO addBanksDetailsTableDTO;

	
	private PreauthDTO preauthDto;
	
	private NomineeDetailsDto nomineeDto;
	
	private LegalHeirDTO legalHeirDto;
	
	
	public void initView() {
		addStyleName("view");
		setSizeFull();
		/*if((SHAConstants.EDIT_PAYMENT_DETAILS).equalsIgnoreCase(presenterString))
		{
			popup = new Window();
			addPopupProperties(popup);
		}
		*/
		searchForm.setWindowObject(popup);
		searchForm.init();
		searchResultTable.setWindowObject(popup);
		searchResultTable.initPresenter(presenterString,editPaymentDetailsView,updatePaymentDetailTableDTO,createAndSearchLotTableDTO,addBanksDetailsTableDTO);
		searchResultTable.setBankDetailsObj(bankDetailsObj);
		searchResultTable.init("", false, false);
		searchResultTable.setNomineeDto(nomineeDto);
		searchResultTable.setPreauthDto(preauthDto);

		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSplitPosition(46);
		setHeight("680px");
		mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
		
	}
	
	
	/*
	private void addPopupProperties(Window popup)
	{
		popup.setWidth("75%");
		popup.setHeight("90%");
		//popup.setContent(viewSearchCriteriaWindow);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}*/
	
	
	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}

	@Override
	public void doSearch() {
		try
		{
		ViewSearchCriteriaFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		fireViewEvent(ViewSearchCriteriaPresenter.SEARCH_BUTTON_CLICK, searchDTO);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void resetSearchResultTableValues() {
		
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof ViewSearchCriteriaTable)
			{
				((ViewSearchCriteriaTable) comp).removeRow();
			}
		}
	
		
	}


	@Override
	public void list(Page<ViewSearchCriteriaTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows.getPageItems());
			searchResultTable.tablesize();
		}
		else
		{
			
			/*Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Search Criteria");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Search Criteria");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("<b style = 'color: black;'>No Records found.</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					searchForm.refresh();
					resetView();
					
				}
			});
		}
		
	}
	public void setWindowObject(Window popup){
		this.popup = popup;
	}
	
	public void setPresenterString(String presenterString)
	{
		this.presenterString = presenterString;
	}
	
	public void setbankDetailsObj(AddBankDetailsTable bankDetail){
		this.bankDetailsObj = bankDetail;
	}
	
	public void setPresenterString(String presenterString,EditPaymentDetailsView view)
	{
		this.presenterString = presenterString;
		this.editPaymentDetailsView = view;
	}


	public void setPresenterString(String presenterString,UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO) {
		this.presenterString = presenterString;
		this.updatePaymentDetailTableDTO = updatePaymentDetailTableDTO;
		
	}
	
	public void setPresenterString(String presenterString,CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {
		this.presenterString = presenterString;
		this.createAndSearchLotTableDTO = updatePaymentDetailTableDTO;
		
	}
	
	public void setPresenterString(String presenterString,AddBanksDetailsTableDTO addBanksDetailsTableDTO) {
		this.presenterString = presenterString;
		this.addBanksDetailsTableDTO = addBanksDetailsTableDTO;
		
	}


	public PreauthDTO getPreauthDto() {
		return preauthDto;
	}


	public void setPreauthDto(PreauthDTO preauthDto) {
		this.preauthDto = preauthDto;
		searchResultTable.setPreauthDto(preauthDto);
	}


	public LegalHeirDTO getLegalHeirDto() {
		return legalHeirDto;		
	}


	public void setLegalHeirDto(LegalHeirDTO legalHeirDto) {
		this.legalHeirDto = legalHeirDto;
	}	
	
	public NomineeDetailsDto getNomineeDto() {
		return this.nomineeDto;
	}

	public void setNomineeDto(NomineeDetailsDto nomineeDto) {
		this.nomineeDto = nomineeDto;
	}	
	
}