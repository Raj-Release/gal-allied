package com.shaic.claim.cpuskipzmr;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class SkipZMRUI extends ViewComponent {

	private static final long serialVersionUID = 1L;
	
	private ComboBox cmbCPUCode;
	
	private Button btnSearch;
	
	private Button btnSubmit;
	
	private Button btnCancel;

	private VerticalLayout mainLayout;
	
	private VerticalLayout generatedLayout;

	private Panel mainPanel;
	
	@Inject
	private Instance<SkipZMRListenerTable> skipZMRListenerTable;
	
	private SkipZMRListenerTable skipZMRListenerTableObj;
	
	@PostConstruct
	public void init() {

	}

	public void initView(BeanItemContainer<SelectValue> cpuCodeContainer) {
		mainLayout = new VerticalLayout();
		mainPanel = new Panel();
		
		generatedLayout = new VerticalLayout();
		
		cmbCPUCode =new ComboBox("CPU Code");
		
		FormLayout cpuForm = new FormLayout(cmbCPUCode);
		
		cmbCPUCode.setContainerDataSource(cpuCodeContainer);
		cmbCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCPUCode.setItemCaptionPropertyId("value");
		
		
		btnSearch = new Button("Search");
		btnSearch.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		mainLayout.addComponent(cpuForm);
		mainLayout.addComponent(btnSearch);
		
		mainLayout.addComponent(generatedLayout);
		
		
		mainLayout.setComponentAlignment(btnSearch, Alignment.MIDDLE_CENTER);

		addClickListener();
		mainPanel.setWidth("100%");
		mainPanel.setHeight("620px");
		mainPanel.setContent(mainLayout);
		setCompositionRoot(mainPanel);

	}

	private void addClickListener() {
		btnSearch.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5677998363425252239L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(cmbCPUCode.getValue() != null ) {
					SelectValue value = (SelectValue) cmbCPUCode.getValue();
					fireViewEvent(SkipZMRPresenter.SEARCH_SKIP_ZMR, value.getId());
				} else {
					Label label = new Label("Please choose CPU Code.", ContentMode.HTML);
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
			}
		});
		
	}
	
	
	private void addButonClickListener() {
		btnSubmit.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5677998363425252239L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(SkipZMRPresenter.SUBMIT_SKIP_ZMR, skipZMRListenerTableObj.getValues());
			}
		});
		
		
		
	}
	
	private void addCancelButtonListener() {
		btnCancel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5677998363425252239L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure you want to cancel ?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											fireViewEvent(MenuItemBean.CPU_SKIP_ZMR,
													null);
										} else {
											// User did not confirm
										}
									}
								});

				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
	}

	public void generateTableForCpuCode(List<SkipZMRListenerTableDTO> masCpuCode) {
		generatedLayout.removeAllComponents();
		if(masCpuCode != null && !masCpuCode.isEmpty()) {
			if(skipZMRListenerTableObj == null) {
				skipZMRListenerTableObj = skipZMRListenerTable.get();
			}
			skipZMRListenerTableObj.init();
//			skipZMRListenerTableObj.setReferenceData( new HashMap<String, Object>());
			for (SkipZMRListenerTableDTO tmpCPUCode : masCpuCode) {
				skipZMRListenerTableObj.addBeanToList(tmpCPUCode);
			}
			
			if(btnSubmit == null) {
				btnSubmit = new Button("Submit");
				btnSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				addButonClickListener();
			}
			if(btnCancel == null) {
				btnCancel = new Button("Cancel");
				addCancelButtonListener();
			}
			
			
			generatedLayout.addComponent(skipZMRListenerTableObj);
			generatedLayout.setSpacing(true);
			HorizontalLayout hLayout = new HorizontalLayout(btnSubmit, btnCancel);
			hLayout.setSpacing(true);
			generatedLayout.addComponent(hLayout);
			generatedLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
			
		}
		
	}
	
	
	
	
	
	
}
