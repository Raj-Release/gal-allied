package com.shaic.reimbursement.fraudidentification;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reports.fraudIdentificationReport.FraudIdentificationReportTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class FraudIdentificationViewImpl extends AbstractMVPView
implements GMVPView, FraudIdentificationView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7240822575624311820L;
	@Inject
	private Instance<FraudIdentificationUI> fraudIdentificationUIInstance;
	private FraudIdentificationUI fraudIdentificationUI;	

	/*@Inject
	private Instance<FraudIdentificationReportTable> fraudIdentificationReportTableInstance;
	private FraudIdentificationReportTable fraudIdentificationReportTable;*/
	
	@PostConstruct
	public void init() {
		addStyleName("view");
	        
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		if(fraudIdentificationUI != null) {
			fraudIdentificationUI.init();
		}
		//fraudIdentificationReportTableInstance.get().resetAlltheValues();

	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setSizeFull();
		fraudIdentificationUI = fraudIdentificationUIInstance.get();
		fraudIdentificationUI.initView();   
		fraudIdentificationUI.init();
		//fraudIdentificationReportTable.init("", false, false);
	    setCompositionRoot(fraudIdentificationUI);
	    //setCompositionRoot(fraudIdentificationReportTableInstance.get());
	}

	
	public void loadParameterDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer) {
		// TODO Auto-generated method stub
		fraudIdentificationUI.loadParameterDropDownValues(mastersValueContainer)	;
		
	}
	
	@Override
	public void buildSuccessLayout() {
		// TODO Auto-generated method stub

		Label successLabel = new Label(
				"<b style = 'color: green;'> Record Saved Successfully.</b>",
				ContentMode.HTML);

		Button homeButton = new Button("OK");
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
				fireViewEvent(MenuItemBean.FRAUD_IDENTIFICATION, null);
			}
		});
		
	
		
	}

	@Override
	public void buildFailureLayout(String message,final String parameterType) {
		

		// TODO Auto-generated method stub

		Label successLabel = new Label("<b style = 'color: black;'>"+message+"</b>", ContentMode.HTML);			
		Button homeButton = new Button("Insert New Record");
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
				fireViewEvent(FraudIdentificationPresenter.INSERT_NEW, parameterType);
				
			}
		});
	
}

	@Override
	public void generateTableForFraudIdentificationView(List<FraudIdentificationTableDTO> dtoList) {

		if(fraudIdentificationUI != null) {
			fraudIdentificationUI.generateTableForFraudIdentification(dtoList);
		}

				
	}

	@Override
	public void insertNewRecord(FraudIdentificationTableDTO dto) {
		if(fraudIdentificationUI != null) {
			fraudIdentificationUI.generateTableForFraudIdentification(dto);
		}

	}
	
	@Override
	public void showDetails(FraudIdentificationTableDTO fraudIdentificationTableDTO) {
		
		//fraudIdentificationReportTable.get().showTableResult(fraudIdentificationTableDTO);
		
	}

}
