package com.shaic.reimbursement.acknowledgeinvestigationcompleted;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.investigation.ackinvestivationcompleted.search.SearchAckInvestigationCompletedTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class InvestigationCompletionDetailsUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Label lblInvestigationCompletionDetails;

	private PopupDateField dateOfCompletion;

	private ComboBox cmbConfirmedBy;

	private TextArea txtInvestigationCompletionRemarks;

	private Button btnSubmit;

	private Button btnCancel;

	private FormLayout investigationCompletionDetailsFormLayout;

	private VerticalLayout mainLayout;

	private Long investigationKey;
	
	private SearchAckInvestigationCompletedTableDTO tableDto;

	public void init(Long investigationKey,
			BeanItemContainer<SelectValue> confirmedBy,SearchAckInvestigationCompletedTableDTO tableDto) {
		this.tableDto = tableDto;
		this.investigationKey = investigationKey;
		lblInvestigationCompletionDetails = new Label(
				"Investigation Completion Details");
		lblInvestigationCompletionDetails.setStyleName(Reindeer.LABEL_H2);

		dateOfCompletion = new PopupDateField("Date of Completion");
		dateOfCompletion.setDateFormat("dd/MM/yyyy");
		cmbConfirmedBy = new ComboBox("Confirmed By");
		if (confirmedBy != null) {
			cmbConfirmedBy.setContainerDataSource(confirmedBy);
		}
		txtInvestigationCompletionRemarks = new TextArea(
				"Investigation Completion Remarks");
		txtInvestigationCompletionRemarks.setMaxLength(4000);
		txtInvestigationCompletionRemarks.setRequired(true);

		btnSubmit = new Button("Submit");
		btnCancel = new Button("Cancel");
		HorizontalLayout buttonLayout = new HorizontalLayout(btnSubmit,
				btnCancel);
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);

		investigationCompletionDetailsFormLayout = new FormLayout(
				dateOfCompletion, cmbConfirmedBy,
				txtInvestigationCompletionRemarks);
		investigationCompletionDetailsFormLayout.setMargin(true);
		investigationCompletionDetailsFormLayout.setHeight("220px");
		mainLayout = new VerticalLayout(lblInvestigationCompletionDetails,
				investigationCompletionDetailsFormLayout, buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		addListener();
		setCompositionRoot(mainLayout);
	}

	private void addListener() {
		btnSubmit.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (dateOfCompletion.getValue() != null
						&& cmbConfirmedBy.getValue() != null
						&& txtInvestigationCompletionRemarks.getValue() != null
						&& !txtInvestigationCompletionRemarks.getValue()
								.equals("")) {
					InvestigationCompletionDetailsDTO investigationCompletionDetailsDTO = new InvestigationCompletionDetailsDTO();
					investigationCompletionDetailsDTO
							.setComfirmedBy((SelectValue) cmbConfirmedBy
									.getValue());
					investigationCompletionDetailsDTO
							.setDateOfCompletion(dateOfCompletion.getValue());
					investigationCompletionDetailsDTO
							.setInvestigationCompletionRemarks(txtInvestigationCompletionRemarks
									.getValue());
					investigationCompletionDetailsDTO.setUserName(tableDto.getUsername());
					investigationCompletionDetailsDTO.setPassWord(tableDto.getPassword());
					//investigationCompletionDetailsDTO.setHumanTask(tableDto.getHumanTaskDTO());
					investigationCompletionDetailsDTO.setRodKey(tableDto.getRodKey());
					fireViewEvent(
							AcknowledgeInvestigationCompletedPresenter.SUBMIT_CLICK,
							investigationKey, investigationCompletionDetailsDTO);
				}else{
					Notification.show("Please enter the required Fields");
				}
			}
		});

		btnCancel.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
						"Are you sure you want to cancel ??",
						new ConfirmDialog.Listener() {

							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									
									VaadinSession session = getSession();
									SHAUtils.releaseHumanTask(tableDto.getUsername(), tableDto.getPassword(), tableDto	.getTaskNumber(),session);
									
									fireViewEvent(
											MenuItemBean.SEARCH_ACKNOWLEDGE_INVESTIGATION_COMPLETED,
											true);
								} else {
									dialog.close();
								}
							}
						});
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
	}

}
