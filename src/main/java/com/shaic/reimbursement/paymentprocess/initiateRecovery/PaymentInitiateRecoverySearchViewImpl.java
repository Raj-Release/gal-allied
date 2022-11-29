package com.shaic.reimbursement.paymentprocess.initiateRecovery;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.table.Page;
import com.shaic.claim.misc.updatesublimit.SearchUpdateSublimitTableDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class PaymentInitiateRecoverySearchViewImpl extends AbstractMVPView implements PaymentInitiateRecoverySearchView{
	
	@Inject
	private PaymentInitiateRecoverySearchForm searchForm;
	
	@Inject
	private PaymentInitiateRecoveryResultantTable searchRecoveryTable; 
	
	private VerticalSplitPanel splitPanel;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
//		searchForm.setViewImp(this);
		searchRecoveryTable.init("", false,false);
		splitPanel = new VerticalSplitPanel();
		splitPanel.setFirstComponent(searchForm);
		splitPanel.setSecondComponent(searchRecoveryTable);
		splitPanel.setSplitPosition(27);
		splitPanel.setSizeFull();
		setHeight("680px");
		setCompositionRoot(splitPanel);
		setWidth("100%");
		setHeight("910px");

		searchForm.addSearchListener(this);
		searchRecoveryTable.addSearchListener(this);
		resetView();
	}

	@Override
	public void doSearch() {
		// TODO Auto-generated method stub
		System.out.println("Calling do search Method.....");
		PaymentInitiateRecoverySearchFormDTO searchDTO = searchForm.getSearchDTO();
		String err = searchForm.validate(searchDTO);
		if(null == err){
			//Pageable pageable = PaymentReprocessTable.getPageable();
			//searchDTO.setPageable(pageable);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(PaymentInitiateRecoverySearchPresenter.PAYMENT_INITIATE_RECOVERY_SEARCH, searchDTO,userName,passWord);
		}else{
			showErrorMessage(err);
		}
		
	}
	
	private void showErrorMessage(String err) {
		// TODO Auto-generated method stub
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(err, buttonsNamewithType);
		
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resultantTable(Page<PaymentInitiateRecoveryTableDTO> searchDTO) {
		

		if(null != searchDTO && null != searchDTO.getPageItems() && 0!= searchDTO.getPageItems().size())
		{	
			searchRecoveryTable.setTableList(searchDTO);
		}
		else
		{

			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox("No Records found.", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;
				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(MenuItemBean.PAYMENT_INITIATE_RECOVERY, null);
				}
			});
		}
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: black;'> Submitted Successfully !!!.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Home Page");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
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
				fireViewEvent(MenuItemBean.PAYMENT_INITIATE_RECOVERY, null);

			}
		});
	}

	@Override
	public void resetValues() {
		// TODO Auto-generated method stub
		searchForm.resetAlltheValues();
		searchRecoveryTable.resetTable();
		
	}

}