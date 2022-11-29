package com.shaic.claim.medical.opinion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.utils.Props;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.annotations.Theme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
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

@Theme(Props.THEME_NAME)
public class OpinionValidationViewImpl extends AbstractMVPView implements OpinionValidationView {

	private static final long serialVersionUID = 1L;

	@Inject
	private OpinionValidationSearchForm opinionValidationSearchForm;	
	
	@Inject
	private OpinionValidationTable searchResultTable;
	
	@EJB
	private OpinionValidationService opinionValidationService;
	
	private OpinionValidationFormDTO dto =  new OpinionValidationFormDTO();
	
	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();
	
	private Button btnSubmit;
	
	private Button btnCancel;
	
	private String screenName;
	
	private String userName;
	
	@Inject
	private OpinionValidationCompletedCasesTable opinionValidationCompletedCasesTable;
	
	@EJB
	private MasterService masterService;
	
	private BeanItemContainer<SelectValue> doctorNames;
	
	@PostConstruct
	protected void initView() {
		
		addStyleName("view");
		setSizeFull();
		opinionValidationSearchForm.initView(dto);
		searchResultTable.init("", false, false);
		mainPanel.setFirstComponent(opinionValidationSearchForm);
		VerticalLayout tableLayout = new VerticalLayout();
		HorizontalLayout buttonLayout = buildOpinionTableButtonLayout();
		tableLayout.addComponent(searchResultTable);
		tableLayout.addComponent(buttonLayout);
		tableLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		mainPanel.setSecondComponent(tableLayout);
		mainPanel.setSplitPosition(35);
		setHeight("650px");
		setCompositionRoot(mainPanel);
		opinionValidationSearchForm.addSearchListener(this);
		resetView();
	}

	@Override
	public void resetView() {		
	}
	
	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof VerticalLayout)
			{
				Iterator<Component> vLayoutIter = ((VerticalLayout) comp).getComponentIterator();
				while(vLayoutIter.hasNext()) {
					Component comp1 = (Component)vLayoutIter.next();
					if(comp1 instanceof OpinionValidationTable)
					{
						((OpinionValidationTable) comp1).removeRow();
					}
				}
					
				
			}
		}
		opinionValidationSearchForm.setDefaultValues();
		btnSubmit.setVisible(false);
		btnCancel.setVisible(false);
	}

	@Override
	public void doSearch() {
		OpinionValidationFormDTO searchDTO = opinionValidationSearchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		fireViewEvent(OpinionValidationPresenter.SEARCH_BUTTON_CLICK, searchDTO, doctorNames);
	}

	@Override
	public void init(BeanItemContainer<SelectValue> cpuCode) {
		userName = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		Set<String> employeeIds = null;
		BeanItemContainer<SelectValue> docNames = null;
		if (userName != null) {
			employeeIds = opinionValidationService.getEmployeeIdsByAssignedUser(userName);
			if (employeeIds != null && employeeIds.size() > 0) {
				docNames = masterService.getEmpNameByEmpIds(employeeIds);
			}
			this.doctorNames = docNames;
		}
		opinionValidationSearchForm.setDropDownValues(cpuCode, userName, docNames);
		
	}

	@Override
	public void list(Page<OpinionValidationTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
			
			for (OpinionValidationTableDTO tableDto : tableRows.getPageItems()) {
				if(tableDto.getColorCode() != null && !tableDto.getColorCode().isEmpty()){
					searchResultTable.setRowColor(tableDto);
					
				}
				if(tableDto.getColorCodeCell() != null && !tableDto.getColorCodeCell().isEmpty()){
					searchResultTable.setRowColor(tableDto);
				}
			}
			btnSubmit.setVisible(true);
			btnCancel.setVisible(true);
		} else {
			Label successLabel = new Label("<b style = 'color: black;'>No Records Found.</b>", ContentMode.HTML);
			Button homeButton = new Button("Opinion Validation Home");
			if(null != screenName && (screenName.equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
				homeButton.setCaption("Waiting For Input Home");
			}
			
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();

					if(null != screenName && (SHAConstants.WAIT_FOR_INPUT_SCREEN.equalsIgnoreCase(screenName))){
						
						fireViewEvent(MenuItemBean.OPINION_VALIDATION, null);
					}
					else {
						fireViewEvent(MenuItemBean.OPINION_VALIDATION, null);
					}
					resetSearchResultTableValues();
					
				}
			});
		}
	}
	
	@SuppressWarnings("deprecation")
	private HorizontalLayout buildOpinionTableButtonLayout(){
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		//Vaadin8-setImmediate() horizontalLayout.setImmediate(true);
		
		btnSubmit = new Button();
		btnSubmit.setCaption("Submit");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true); 
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		btnCancel = new Button();
		btnCancel.setCaption("Cancel");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		
		if (searchResultTable.getTable().size() <= 0) {
			btnSubmit.setVisible(false);
			btnCancel.setVisible(false);
		}
		
		addOpinionStatusListener();
		horizontalLayout.addComponents(btnSubmit,btnCancel);
		horizontalLayout.setSpacing(true);
		return horizontalLayout;
	
	}
	
	public void addOpinionStatusListener() {

		btnSubmit.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				btnSubmit.setEnabled(true);
				HashMap<Long, Boolean> opinionStatusMap = searchResultTable.getOpinionStatusValue();
				HashMap<Long, String> opinionStatusRemarks = searchResultTable.getOpinionStatusRemarks();
				Boolean result = isValidData(opinionStatusMap, opinionStatusRemarks);
				Label responseLabel = null;
				if (result) {
					fireViewEvent(OpinionValidationPresenter.SUBMIT_BUTTON_CLICK, opinionStatusMap, opinionStatusRemarks, userName);				
					responseLabel = new Label("<b style = 'color: black;'>Records updated successfully</b>", ContentMode.HTML);
					Button successBtn = new Button("Opinion Validation Home");
					if(null != screenName && (screenName.equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
						successBtn.setCaption("Waiting For Input Home");
					}				
					successBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					VerticalLayout layout = new VerticalLayout(responseLabel, successBtn);
					layout.setComponentAlignment(successBtn, Alignment.BOTTOM_CENTER);
					layout.setSpacing(true);
					layout.setMargin(true);
					
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setClosable(false);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
					
					successBtn.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							dialog.close();

							if(null != screenName && (SHAConstants.WAIT_FOR_INPUT_SCREEN.equalsIgnoreCase(screenName))){
								
								fireViewEvent(MenuItemBean.OPINION_VALIDATION, null);
							}
							else {
								fireViewEvent(MenuItemBean.OPINION_VALIDATION, null);
							}
							resetSearchResultTableValues();						
						}
					});
				} else {
					if (opinionStatusMap.size() > 0) {
						responseLabel = new Label("<b style = 'color: black;'>Remarks field is mandatory for disagreed opinion </b>", ContentMode.HTML);
					} else {
						responseLabel = new Label("<b style = 'color: black;'>Please select agree/disagree before submit </b>", ContentMode.HTML);
					}
					Button successBtn = new Button("Ok");
					if(null != screenName && (screenName.equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
						successBtn.setCaption("Waiting For Input Home");
					}				
					successBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					VerticalLayout layout = new VerticalLayout(responseLabel, successBtn);
					layout.setComponentAlignment(successBtn, Alignment.BOTTOM_CENTER);
					layout.setSpacing(true);
					layout.setMargin(true);
					
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setClosable(false);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
					
					successBtn.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							dialog.close();
						}
					});
				}
			}

		});
		
		btnCancel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				searchResultTable.cancelOpinionStatusValue();
				doSearch();
			}
		});
	}

	@Override
	public void completedCaseList(Page<OpinionValidationTableDTO> tableRows) {
		if (null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size()) {	
			opinionValidationCompletedCasesTable.setTableList(tableRows);
			opinionValidationCompletedCasesTable.tablesize();
			opinionValidationCompletedCasesTable.setHasNextPage(tableRows.isHasNext());
			
			for (OpinionValidationTableDTO tableDto : tableRows.getPageItems()) {
				if (tableDto.getColorCode() != null && !tableDto.getColorCode().isEmpty()) {
					opinionValidationCompletedCasesTable.setRowColor(tableDto);
				}
				opinionValidationCompletedCasesTable.setData(tableDto);
				if (tableDto.getColorCodeCell() != null && !tableDto.getColorCodeCell().isEmpty()) {
					opinionValidationCompletedCasesTable.setRowColor(tableDto);
				}
			}
		} else {
			Label successLabel = new Label("<b style = 'color: black;'>No Records Found.</b>", ContentMode.HTML);
			Button okButton = new Button("Ok");
			if(null != screenName && (screenName.equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
				okButton.setCaption("Waiting For Input");
			}
			
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, okButton);
			layout.setComponentAlignment(okButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					opinionValidationCompletedCasesTable.getPageable().setPageNumber(1);
					opinionValidationCompletedCasesTable.resetTable();
				}
			});
		}
		
	}
	
	private boolean isValidData(HashMap<Long, Boolean> opinionStatusMap, HashMap<Long, String> opinionStatusRemarks) {
		if (opinionStatusMap != null && opinionStatusMap.size() > 0) {
			Iterator<?> it = opinionStatusMap.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry opinionStatus = (Map.Entry)it.next();
	            if (opinionStatus.getValue() != null && opinionStatus.getValue().equals(Boolean.FALSE)) {
	            	if (opinionStatusRemarks != null && opinionStatusRemarks.size() > 0) {
	            		String remarks = opinionStatusRemarks.get(opinionStatus.getKey());
	            		if(remarks == null && StringUtils.isBlank(remarks)) {
	            			return false;
	            		} 
	            	} else {
	            		return false;
	            	}
	            }
	        }
		} else {
			return false;
		}
		return true;
	}
	

}
