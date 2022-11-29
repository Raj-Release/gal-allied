package com.shaic.claim.userreallocation;

import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.domain.AutoAllocationDetails;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class EditReallocationCountDetailsViewImpl extends AbstractMVPView implements EditReallocationCountDetailsView {

	private Window countPopup;
	
	@Inject
	private Instance<AutoAllocationAssignedDetailsTable> autoAllocationAssignedDetailsTableInstance;
	
	@Inject
	private Instance<AutoAllocationCompletedDetailsTable> autoAllocationCompletedDetailsTableInstance;
	
	@Inject
	private Instance<AutoAllocationPendingDetailsTable> autoAllocationPendingDetailsTableInstance;
	
	@Inject
	private Instance<EditReallocationCountDetailsForm> reallocationFormInstance;
	
	private EditReallocationCountDetailsForm reallocationForm;
	
	public void init(SearchReallocationDoctorDetailsTableDTO bean,
			Window popUp, String countType,
			List<AutoAllocationDetailsTableDTO> assignedList,
			List<AutoAllocationDetailsTableDTO> completedList,
			List<AutoAllocationDetailsTableDTO> pendingList) {

		countPopup = popUp;
		this.reallocationForm = reallocationFormInstance.get();
		
		this.reallocationForm.init(bean, popUp, countType, assignedList, completedList, pendingList);
		this.reallocationForm.initIntimationTable(autoAllocationAssignedDetailsTableInstance, autoAllocationCompletedDetailsTableInstance, autoAllocationPendingDetailsTableInstance);
		Panel mainPanel = new Panel();
		//Vaadin8-setImmediate() mainPanel.setImmediate(true);
		mainPanel.setContent(reallocationForm);
		
		setCompositionRoot(mainPanel);
	
	}
	
	public void initIntimationTable(
			Instance<AutoAllocationAssignedDetailsTable> assignedInstance,
			Instance<AutoAllocationCompletedDetailsTable> completedInstance,
			Instance<AutoAllocationPendingDetailsTable> pendingInstance, Instance<EditReallocationCountDetailsForm> formInstance) {
		autoAllocationAssignedDetailsTableInstance = assignedInstance;
		autoAllocationCompletedDetailsTableInstance = completedInstance;
		autoAllocationPendingDetailsTableInstance = pendingInstance;
		reallocationFormInstance = formInstance;
	}
	
	
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserIntimationDetails(List<AutoAllocationDetails> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitValues(Boolean isUpdated) {
		// TODO Auto-generated method stub
		Label label;
		if(isUpdated){
		label = new Label(
				"<b style = 'color: green;'> Intimations selected for Release / Re-allocation are completed successfully!!!</b>",
				ContentMode.HTML);
		}else{
			label = new Label(
					"<b style = 'color: green;'>No Records to update.</b>",
					ContentMode.HTML);
		}

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(label, homeButton);
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
			}
		});
	
	}

}
