package com.shaic.claim.intimation.uprSearch;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.claim.intimation.viewdetails.search.SearchViewDetailIntimationTable;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewPaymentTrailTableDTO;
import com.shaic.domain.IntimationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation.StopPaymentTrackingTable;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation.StopPaymentTrackingTableDTO;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SearchIntimationUPRDetailViewImpl extends AbstractMVPView implements SearchIntimationUPRDetailView{
	
	
	@Inject
	private Instance<SearchIntimationUPRDetailUI> searchIntimation;

	@Inject
	private SearchIntimationUPRViewDetailTable searchResultTable;
	
	private SearchIntimationFormDto searchDto;
	

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		
		setSizeFull();
		searchIntimation.get().init();
		setCompositionRoot(searchIntimation.get());
		searchResultTable.setSizeFull();
		searchResultTable=searchIntimation.get().searchViewDetailResultTable;
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.addSearchListener(this);
		
		resetView();
	}
	
	@Override
	public void showIntimationDetailsView(ClickEvent event) {
//	IntimationBean a_intimation =  (IntimationBean) event.getButton().getData();
//	ViewIntimation intimationDetailsView = new ViewIntimation(a_intimation);
//	UI.getCurrent().addWindow(intimationDetailsView);
	}
	
	@Override
	public void showSearchViewDetailIntimationTable(SearchIntimationFormDto searchDto) {
		Map<String,Object> params = searchDto.getFilters();
		this.searchDto = searchDto;
		if(!params.containsKey("intimationNumber") && params.get("intimationNumber") == null
				&& !params.containsKey("uprNumber") && params.get("uprNumber") == null)	
		{
			searchIntimation.get().showErrorPopup("<b>Intimation or UTR/Cheque/DD Number is mandatory for search");
			searchIntimation.get().clearsearchViewDetailResultTable();;
			return;
		}
		if(!params.isEmpty())
		{
		     doSearch();
		}
		else
		{
			Notification.show("Error","Intimation or UTR/Cheque/DD Number is mandatory for search",Notification.TYPE_ERROR_MESSAGE);
			return;
		}
	}

	@Override
	public void resetSearchIntimationView() {
		searchIntimation.get().resetSearchIntimationFields();
	}



	private void noRecordFound(){
		Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
		Button homeButton = new Button("Search UTR Details Home");
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
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.INTIMATION_VIEW_UPR_DETAILS, null);
			}
		});
	}

	@Override
	public void resetView() {
		if(searchIntimation.get() != null) {
			//searchIntimation.get().init();
			searchIntimation.get().refresh();
		}
	}

	@Override
	public void doSearch() {
		
		Pageable pageable = searchResultTable.getPageable();
		 searchDto.setPageable(pageable);
		
		 fireViewEvent(SearchIntimationUPRDetailPresenter.GET_UPR_SEARCH_RESULT, searchDto);
		 
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void showActionClickView(ViewClaimStatusDTO intimationDetails){
//		searchResultTable.showClickActionView(intimationDetails);
	}
	
	@Override
	public void showSearchResultViewDetail(Page<PaymentProcessCpuPageDTO> newIntimationDtoPagedContainer) {
		 
		if(newIntimationDtoPagedContainer == null || 
				newIntimationDtoPagedContainer.getPageItems() == null || 
				newIntimationDtoPagedContainer.getPageItems().isEmpty()) 
		{
			noRecordFound(); 
			searchIntimation.get().buildSearchIntimationTable(null);		
		}
		else{
			searchResultTable.setTableList(newIntimationDtoPagedContainer.getPageItems());
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(newIntimationDtoPagedContainer.isHasNext());
			searchIntimation.get().buildSearchIntimationTable(newIntimationDtoPagedContainer);		 
		}
	}
	
	@Override
	public void setPaymentTrials(List<ViewPaymentTrailTableDTO> viewPaymentTrailTableList){
		searchResultTable.setPaymentTrials(viewPaymentTrailTableList);
	}
	
	@Override
	public void setPaymentCancelDetails(List<ClmPaymentCancelDto> chqCancelListDto,
			List<ClmPaymentCancelDto> neftCancelListDto){
		searchResultTable.showPaymentCancelDetailsTable(chqCancelListDto, neftCancelListDto);
	}
	
	@Override
	public void setSettlementDetails(PaymentProcessCpuPageDTO paymentDto){
		searchResultTable.showSettlementDetails(paymentDto);
	}

	@Override
	public void showTrackingTrails(
			List<StopPaymentTrackingTableDTO> viewStopPaymentTrackingTableList) {
		
		searchResultTable.showStopPaymentTracking(viewStopPaymentTrackingTableList);
	}
}