package com.shaic.claim.hospitalscoring;

import java.util.Iterator;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import org.vaadin.dialogs.ConfirmDialog;
import com.shaic.claim.policy.search.ui.SearchPolicyUI;
import com.shaic.claim.hospitalscoring.HospitalScoringDetailsView;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
//import com.shaic.claim.cashlessprocess.withdrawpreauth.SearchWithdrawCashLessProcessForm;
//import com.shaic.claim.cashlessprocess.withdrawpreauth.SearchWithdrawCashLessProcessFormDTO;
//import com.shaic.claim.cashlessprocess.withdrawpreauth.SearchWithdrawCashLessProcessPresenter;
//import com.shaic.claim.cashlessprocess.withdrawpreauth.SearchWithdrawCashLessProcessTable;
//import com.shaic.claim.cashlessprocess.withdrawpreauth.SearchWithdrawCashLessProcessTableDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class HospitalScoringDetailsViewImpl  extends AbstractMVPView implements HospitalScoringDetailsView {

	private static final long serialVersionUID = 1934939436987293748L;
	@Inject
	private HospitalScoringDetailsForm searchForm;

	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		// searchResultTable.setWidth("1395px");
		// searchResultTable.setHeight("380px");

		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSplitPosition(30);
		mainPanel.setWidth("100.0%");
		setHeight("100.0%");
		setHeight("600px");
		setCompositionRoot(mainPanel);

//		searchForm.addSearchListener(this);
		resetView();

	}

	@Override
	public void resetView() {
		System.out.println("---tinside the reset view");

		searchForm.refresh();

	}

	/*public void doSearch() {
		HospitalScoringDetailsDTO searchDTO = searchForm.getSearchDTO();

		String userName = (String) getUI().getSession().getAttribute(
				BPMClientContext.USERID);
		String passWord = (String) getUI().getSession().getAttribute(
				BPMClientContext.PASSWORD);

		fireViewEvent(
				SearchWithdrawCashLessProcessPresenter.SEARCH_BUTTON_CLICK,
				searchDTO, userName, passWord);
	}*/

	public void resetSearchResultTableValues() {

	}
}
