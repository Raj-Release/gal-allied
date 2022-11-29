package com.shaic.claim.policy.search.ui.opsearch;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpDTO;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchSettlementLetterProcessOPClaimRequestViewImpl extends AbstractMVPView implements SearchSettlementLetterProcessOPClaimRequestView {


	private static final long serialVersionUID = 2298996714857952907L;

	@Inject
	private SearchSettlementLetterProcessOPClaimRequestForm  searchForm;

	@Inject
	private SearchSettlementLetterProcessOPClaimRequestTable searchResultTable;

	private VerticalSplitPanel mainPanel;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init();
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSplitPosition(31);
		setHeight("480px");
		mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}

	@Override
	public void resetView() {
		searchForm.refresh(); 

	}

	@Override
	public void doSearch() {
		CreateBatchOpDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(SearchSettlementLetterProcessOPClaimRequestPresenter.CREATE_SETTLEMENT_LETTER_OP, searchDTO,userName,passWord);
	}

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.resetTable();
		searchResultTable.removeRow();
		//		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		//		while(componentIter.hasNext()) {
		//			Component comp = (Component)componentIter.next();
		//			if(comp instanceof SearchProcessOPClaimRequestTable) {
		//				((SearchProcessOPClaimRequestTable) comp).removeRow();
		//			}
		//		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void list(Page<CreateBatchOpTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows.getPageItems());
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
		}
		else
		{
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Process Settlement Request");
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
					fireViewEvent(MenuItemBean.CREATE_SETTLEMENT_BATCH_OP, null);

				}
			});
		}
		searchForm.enableButtons();
	}

//	@Override
//	public void init(BeanItemContainer<SelectValue> claimType) {
//		searchForm.setDropDownValues(claimType);
//
//	}

//	@Override
//	public void list(Page<CreateBatchOpTableDTO> tableRows) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void init(BeanItemContainer<SelectValue> type,
			BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> claimant,
			BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> paymentStatus,
			BeanItemContainer<SpecialSelectValue> product,
			BeanItemContainer<SelectValue> docVerified,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> pioCode) {
		// TODO Auto-generated method stub
		searchForm.setDropDownValues(pioCode);
		
	}

	@Override
	public void buildSuccessLayout(Map<String, Object> createLotMapper,
			Window popUp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildResultantTableLayout(SelectValue layoutType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showClaimsDMS(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO,
			EditPaymentDetailsView editPaymentView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUpIFSCDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPaymentCpu(CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void buildSuccessLayout(String strRecMessage) {

		Label successLabel = new Label("<b style = 'color: green;'>"  + strRecMessage + "</b>",
				ContentMode.HTML);
		
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		layout = new VerticalLayout(successLabel, horizontalLayout);
		horizontalLayout.setMargin(true);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
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

				fireViewEvent(MenuItemBean.CREATE_SETTLEMENT_BATCH_OP, null, null);
				
			}
		});
		
	}

	@Override
	public void listForQuick(Page<CreateBatchOpTableDTO> search) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUpPaymentCpuCodeDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUpPayeeNameDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUpPayableDetails(String payableName,
			CreateBatchOpTableDTO tableDto) {
		// TODO Auto-generated method stub
		
	}




}
