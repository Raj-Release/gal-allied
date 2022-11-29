package com.shaic.claim.legal.processconsumerforum.page.ombudsman;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.LegalOmbudsman;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SearchProcessOmbudsmanViewImpl extends AbstractMVPView implements SearchProcessOmbudsmanView{

	@Inject
	private SearchProcessOmbudsmanForm searchForm;
	
	private VerticalLayout mainPanel;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		mainPanel = new VerticalLayout();
		mainPanel.addComponent(searchForm);
		//mainPanel.setSplitPosition(47);
		//setHeight("590px");
	//	mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		//searchForm.addSearchListener(this);
		resetView();
	}
	
	
	@Override
	public void doSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void list(Page<ConsumerForumDTO> tableRows) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(BeanItemContainer<SelectValue> repudiationMasterValueByCode,BeanItemContainer<SelectValue> statusoftheCase,
			BeanItemContainer<SelectValue> ombudsmanDetailsByDesc,
			BeanItemContainer<SelectValue> addDays,
			BeanItemContainer<SelectValue> pendingLevel,
			BeanItemContainer<SelectValue> hearingStatus,
			BeanItemContainer<SelectValue> awardStatus,
			BeanItemContainer<SelectValue> compromiseStatus,
			BeanItemContainer<SelectValue> decision,
			BeanItemContainer<SelectValue> recievedFrom,BeanItemContainer<SelectValue> movedTO, BeanItemContainer<SelectValue> grievanceOutcome) {
		// TODO Auto-generated method stub
		searchForm.init();
		searchForm.setCBXValue(repudiationMasterValueByCode,statusoftheCase,
				ombudsmanDetailsByDesc,addDays,pendingLevel,hearingStatus,awardStatus,compromiseStatus,decision,recievedFrom,movedTO,grievanceOutcome);
	}


	@Override
	public void populateFiledValues(Claim claimsByIntimationNumber,LegalOmbudsman legalOmbudsman,String diagnosis) {
		// TODO Auto-generated method stub
		searchForm.populateFieldValues(claimsByIntimationNumber,legalOmbudsman,diagnosis);
	}
	
	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Legal Ombudsman Record Saved Successfully.</b>",
				ContentMode.HTML);
		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Legal Ombudsman");
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

				searchForm.init();
				dialog.close();
				fireViewEvent(MenuItemBean.OMBUDSMAN, null);

			}
		});
	}


	@Override
	public void setOmbudsmanContact(String contactOmbudsman) {
		// TODO Auto-generated method stub
		searchForm.setOmbudsmanContact(contactOmbudsman);
	}

}
