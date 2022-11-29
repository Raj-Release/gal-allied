package com.shaic.claim.userproduct.document.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
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
import com.vaadin.ui.themes.ValoTheme;

public class UserManagementViewImpl extends AbstractMVPView implements UserManagementView{
	
	@Inject
	private UserManagementUI searchDoctorForm;
	
	@Inject 
	private UserManagementDoctorDetails searchDoctorDetails;
	
	
	
	private UserManagementDTO bean;
	
	private VerticalSplitPanel mainPanel;
	

	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchDoctorForm.init();
		searchDoctorDetails.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchDoctorForm);
		mainPanel.setSecondComponent(searchDoctorDetails);
		mainPanel.setSplitPosition(32);
		mainPanel.setSizeFull();
		setHeight("570px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchDoctorDetails.addSearchListener(this);
		searchDoctorForm.addSearchListener(this);
		resetView();
	}
	
	@Override
	public void resetView() {
		searchDoctorForm.refresh(); 
		
	}

	@Override
	public void getResultList(Page<SearchDoctorDetailsTableDTO> tableRows) {

		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchDoctorDetails.setTableList(tableRows);
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("User Management Home");
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
					fireViewEvent(MenuItemBean.USER_MANAGEMENT, null);
					
				}
			});
		}
		searchDoctorForm.enableButtons();
	
	}

	@Override
	public void setDoctorDetails(
			SearchDoctorDetailsTableDTO viewSearchCriteriaDTO) {
		searchDoctorForm.setDoctorNameValue(viewSearchCriteriaDTO);
		
	}

	@Override
	public void doSearch() {
		SearchDoctorNameDTO searchDTO = searchDoctorForm.getSearchDTO();
		if(searchDTO.getDoctorName() != null && !searchDTO.getDoctorName().isEmpty()) {
			char validateUserId[] = searchDTO.getDoctorName().toCharArray();
			if(validateUserId.length<3 && (searchDTO.getDoctorName().contains(".") || searchDTO.getDoctorName().contains(" ") || searchDTO.getDoctorName().contains("@") || searchDTO.getDoctorName().contains("&")
					|| searchDTO.getDoctorName().contains("#") || searchDTO.getDoctorName().contains("*") || searchDTO.getDoctorName().contains("$")
					|| searchDTO.getDoctorName().contains("!") || searchDTO.getDoctorName().contains("%"))) {
				showErrorMessage("Please enter a valid User Id /User Name");
			}else
			if(validateUserId.length<3) {
				for(int i=0;i<validateUserId.length;i++) {
					
					if(Character.isDigit(validateUserId[i]) || Character.isDigit(validateUserId[i+1]) || Character.isWhitespace(validateUserId[i])) {
						showErrorMessage("User ID or Name must be atleast 3 characters long");
						break;
					}else{
						showErrorMessage("User ID or Name must be atleast 3 characters long");
						break;
					}
				}
				
			}else{
				fireViewEvent(UserManagementPresenter.SEARCH_BUTTON_CLICK, searchDTO);
			}
		
		}else {
			showErrorMessage("Please Enter Doctor Name");
			resetView();
			resetSearchResultTableValues();
		}
	}

	
	private void showErrorMessage(String eMsg) {
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
	}

	@Override
	public void resetSearchResultTableValues() {
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof UserManagementDoctorDetails)
			{
				((UserManagementDoctorDetails) comp).removeRow();
			}
		}
		
	}

	

	

}
