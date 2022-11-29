package com.shaic.claim.userproduct.document;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.userproduct.document.search.SearchDoctorDetailsTableDTO;
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

public class ProductAndDocumentTypeViewImpl extends AbstractMVPView implements ProductAndDocumentTypeView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProductAndDocumentPageUI productDocumentPage;
	
	private ProductAndDocumentTypeDTO bean;
	
	//private Button editBtn;
	
	//private Button submitBtn;
	
	//private Button cancelBtn;
	
	
	@PostConstruct
	public void init(){
		
	}
	
	@Override
	public void init(ProductAndDocumentTypeDTO bean, BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> userTypeContainer) {
		
		this.bean = bean;
		
		productDocumentPage.init(this.bean, selectValueContainer, userTypeContainer);
		productDocumentPage.setCaption("User - Product & Document Type Mapping");
		
//		editBtn = new Button("Edit");
//		submitBtn = new Button("Submit");
//		cancelBtn = new Button("Cancel");
//		
//		editBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//		editBtn.setWidth("-1px");
//		editBtn.setHeight("-10px");
//		
//		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//		submitBtn.setWidth("-1px");
//		submitBtn.setHeight("-10px");
//		
//		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
//		cancelBtn.setWidth("-1px");
//		cancelBtn.setHeight("-10px");
//		
//		HorizontalLayout buttonHor = new HorizontalLayout(editBtn,submitBtn,cancelBtn);
//		buttonHor.setSpacing(true);
		
		//VerticalLayout dummyLayout = new VerticalLayout();
		
		VerticalLayout mainVertical = new VerticalLayout(productDocumentPage);
		//mainVertical.setComponentAlignment(buttonHor, Alignment.BOTTOM_CENTER);
		mainVertical.setSpacing(true);
		
		
		setCompositionRoot(mainVertical);
		 
		
	}
	
	
	
	
	@Override
	public void submitValues() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Document Mapped Successfully</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Home");
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
				fireViewEvent(MenuItemBean.USER_ACCESS_ALLOCATION,true);

			}
		});
	}

	public boolean validatePage() {
		Boolean hasError = false;
		
		String eMsg = "";		
		
		if (hasError) {
			Label label = new Label(eMsg, ContentMode.HTML);
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
	
			hasError = true;
			return !hasError;
		} else {	
			return true;
		}
}
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setDoctorDetails(SearchDoctorDetailsTableDTO viewSearchCriteriaDTO) {
		productDocumentPage.setDoctorNameValue(viewSearchCriteriaDTO);
	}

	@Override
	public void setUpReference(ProductAndDocumentTypeDTO userClaimDTO) {
		productDocumentPage.setUpReference(userClaimDTO);
	}
	
}
