package com.shaic.claim.reimbursement.rawanalysis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

public class ProcessRawRequestPage extends ViewComponent {

	@Inject
	private Instance<ProcessRawInitiatedTable> processRawIntiatedTableInst;

	private ProcessRawInitiatedTable processRawIntiatedTable;

	@Inject
	private RequestRepliedByRawTable repliedRawTable;

	private Button submitBtn;

	private Button cancelBtn;

	private SearchProcessRawRequestTableDto bean;

	private BeanFieldGroup<SearchProcessRawRequestTableDto> binder;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private SearchProcessRawRequestTableDto dto;

	@PostConstruct
	protected void initView() {

	}

	public void init(SearchProcessRawRequestTableDto dto) {
		
		if(dto.getPreauthDTO().getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), dto.getPreauthDTO().getIsSuspicious(), dto.getPreauthDTO().getClmPrcsInstruction());
	    }
		
		if(SHAConstants.YES_FLAG.equalsIgnoreCase(dto.getIntimationDto().getInsuredDeceasedFlag())) {
			
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}

		this.dto = dto;
		this.bean = new SearchProcessRawRequestTableDto();
		this.binder = new BeanFieldGroup<SearchProcessRawRequestTableDto>(
				SearchProcessRawRequestTableDto.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

		processRawIntiatedTable = processRawIntiatedTableInst.get();
		processRawIntiatedTable.init(this.bean.getPreauthDTO());
		fireViewEvent(ProcessRawRequestPresenter.SET_TABLE_DATA, dto);

		repliedRawTable.init("Request Replied by RAW", false, false);
		fireViewEvent(ProcessRawRequestPresenter.SET_REPLIED_DATA, dto);

		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");

		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");

		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");

		FormLayout dummyForm = new FormLayout();
		dummyForm.setWidth("50%");
		dummyForm.setHeight("45px");

		VerticalLayout vLayout = new VerticalLayout();
		//Vaadin8-setImmediate() vLayout.setImmediate(false);
		
		HorizontalLayout formHor = new HorizontalLayout(dummyForm,vLayout);
		
		HorizontalLayout buttonHor = new HorizontalLayout(submitBtn,cancelBtn);

		buttonHor.setSpacing(true);
		buttonHor.setMargin(false);

		VerticalLayout mainVerticalOne = new VerticalLayout(formHor,
				processRawIntiatedTable);
		mainVerticalOne.setSpacing(true);

		VerticalLayout middleLayout = new VerticalLayout(repliedRawTable);
		middleLayout.setSpacing(true);

		VerticalLayout mainLayoutTwo = new VerticalLayout(formHor,
				middleLayout, dummyForm, buttonHor);
		mainLayoutTwo.setComponentAlignment(buttonHor, Alignment.BOTTOM_CENTER);

		VerticalLayout mainVertical = new VerticalLayout(mainVerticalOne,
				mainLayoutTwo);

		showOrHideValidation(false);

		addListener();

		setCompositionRoot(mainVertical);

	}

	public void addListener() {

		submitBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (validatePage()) {
					List<RawInitiatedRequestDTO> rawIntiatedDtls = processRawIntiatedTable
							.getValues();
					String userName = (String) getUI().getSession()
							.getAttribute(BPMClientContext.USERID);
					fireViewEvent(ProcessRawRequestPresenter.SUBMIT_REPLY_DATA,
							rawIntiatedDtls, userName);
				} else {

				}
			}
		});
		cancelBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
						"Confirmation", "Are you sure you want to cancel ?",
						"No", "yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (dialog.isCanceled()
										&& !dialog.isConfirmed()) {
									// UI.getCurrent().removeWindow(popup);
									fireViewEvent(
											MenuItemBean.PROCESS_RAW_REQUEST,
											true);
								} else {
									// User did not confirm
									dialog.close();
								}
							}
						});

				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);

			}
		});

	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	@SuppressWarnings("unused")
	public void setTableValues(List<RawInitiatedRequestDTO> intiatedDtls) {
		if (this.processRawIntiatedTable != null) {
			/*
			 * List<RawInitiatedRequestDTO> rawIntiatedDtls = intiatedDtls; for
			 * (RawInitiatedRequestDTO rawInitiatedRequestDTO : rawIntiatedDtls)
			 * { processRawIntiatedTable.addBeanToList(rawInitiatedRequestDTO);
			 * }
			 */
			processRawIntiatedTable.setTableList(intiatedDtls);
		}
	}

	public void setRepliedRawTableValues(
			List<RawInitiatedRequestDTO> repliedDtls) {
		repliedRawTable.setTableList(repliedDtls);
	}

	public void setResolutionRaw(BeanItemContainer<SelectValue> resolutionRaws) {
		processRawIntiatedTable.setRawResolutions(resolutionRaws);
	}

	public Boolean validatePage() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();

		if (this.processRawIntiatedTable.getValues() != null) {
			List<RawInitiatedRequestDTO> rawDetails = this.processRawIntiatedTable
					.getValues();
			if (rawDetails != null && !rawDetails.isEmpty()) {
				for (RawInitiatedRequestDTO rawInitiatedRequestDTO : rawDetails) {
					if (rawInitiatedRequestDTO.getResolutionfromRaw() != null
							&& rawInitiatedRequestDTO.getRemarksfromRaw() != null) {
						hasError = false;
					} else {
						hasError = true;
						if (rawInitiatedRequestDTO.getResolutionfromRaw() == null) {
							eMsg.append("Reply is mandatory for all the request raised </br> ");
						} else {
							eMsg.append("Please Enter Remarks </br> ");
						}
						break;
					}
				}
			}
		}

		// New code addition for CR2019023
		if (null != this.dto.getPreauthDTO()) {
			PreauthDTO preAuth = this.dto.getPreauthDTO();

			if (!preAuth.getIsScheduleClicked()
					&& (null != this.dto.getIntimationDto()
							&& null != this.dto.getIntimationDto()
									.getIsTataPolicy() && !this.dto
							.getIntimationDto().getIsTataPolicy())) {
				hasError = true;
				eMsg.append("Please Verify View Policy Schedule Button.</br>");
			}
		}

		if (hasError) {

			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		} else {
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}

	}

}
