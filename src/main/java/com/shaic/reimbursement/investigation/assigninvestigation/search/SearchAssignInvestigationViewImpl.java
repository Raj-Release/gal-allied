/**
 * 
 */
package com.shaic.reimbursement.investigation.assigninvestigation.search;

import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
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

/**
 * @author ntv.narenj
 *
 */
public class SearchAssignInvestigationViewImpl extends AbstractMVPView implements SearchAssignInvestigationView{

	
	@Inject
	private SearchAssignInvestigationForm  searchForm;
	
	@Inject
	private SearchAssignInvestigationTable searchResultTable;
	
	
	private VerticalSplitPanel mainPanel;
	
	@EJB
	private HospitalService hospitalService;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSplitPosition(45);
		setHeight("570px");
	//	mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}
	
	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}

	@Override
	public void doSearch() {
		SearchAssignInvestigationFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		
		 if((searchDTO.getInvstigationState() != null) && ((searchDTO.getFromDate() == null && searchDTO.getEndDate() != null ) || (searchDTO.getFromDate() != null && searchDTO.getEndDate() == null ) || (searchDTO.getFromDate() == null && searchDTO.getEndDate() == null )))
			{
				
				showErrorPopup("<b>Please Select both Intimation from date and to date for Search");
				searchForm.refresh();
			 }
			else if((searchDTO.getInvstigationState() != null) && ((searchDTO.getFromDate() != null  && searchDTO.getEndDate() != null) && (((Date)searchDTO.getEndDate()).before((Date)searchDTO.getFromDate()))) )
			 {
				 
		    	 showErrorPopup("<b>Invalid Date Range, <br>Please Select Intimation End date greater than Intimation From date");
		    	 searchForm.refresh();
			 }
	 
			else if((searchDTO.getInvstigationState() == null) && ((searchDTO.getFromDate() != null  || searchDTO.getEndDate() != null)))
			 {
				 
		    	 showErrorPopup("<b>Please Select Investigation State");
		    	 searchForm.refresh();
			 } 
			else {
				fireViewEvent(SearchAssignInvestigationPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
			}
		 
//		fireViewEvent(SearchAssignInvestigationPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		
	}

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchAssignInvestigationTable)
			{
				((SearchAssignInvestigationTable) comp).removeRow();
			}
		}
	
		
	}

	@Override
	public void list(Page<SearchAssignInvestigationTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
			
			for (SearchAssignInvestigationTableDTO searchDTO : tableRows.getPageItems()) {
				DBCalculationService dbService = new DBCalculationService();
				Intimation intimationByNo = hospitalService.getIntimationByNo(searchDTO.getIntimationNo());
				Long hospital = intimationByNo.getHospital();
				Hospitals hospitalById = hospitalService.getHospitalById(hospital);
				Boolean fraudFlag = dbService.getFraudFlag(searchDTO.getIntimationNo(),
						intimationByNo.getPolicy().getPolicyNumber(),hospitalById.getHospitalCode(),
						hospitalById.getHospitalIrdaCode(),intimationByNo.getPolicy().getAgentCode());

				if(fraudFlag != null && fraudFlag){
					searchDTO.setColorCode("RED");
				}
			}
			
			for (SearchAssignInvestigationTableDTO tableDto : tableRows.getPageItems()) {
				if(tableDto.getColorCodeCell() != null && !tableDto.getColorCodeCell().isEmpty()){
					searchResultTable.setRowColor(tableDto);
				}
				
				if(tableDto.getColorCode() != null && !tableDto.getColorCode().isEmpty()){
					searchResultTable.setRowColor(tableDto);
				}
			}
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Assign Investigation Home");
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
					fireViewEvent(MenuItemBean.SHOW_ASSIGN_INVESTIGATION, null);
					
				}
			});
		}
		searchForm.enableButtons();
	}

	@Override
	public void init(BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> claimTypeContainer, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> statusByInvestigationState) {
		searchForm.setCPUCode(cpuCode,claimTypeContainer,selectValueForPriority,statusByStage,statusByInvestigationState);
		
	}
	
	public void showErrorPopup(String errorMessage) {
        Label label = new Label(errorMessage, ContentMode.HTML);
        label.setStyleName("errMessage");
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.addComponent(label);

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setCaption("Error");
        dialog.setClosable(true);
        dialog.setContent(layout);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.show(getUI().getCurrent(), null, true);

        return;
	}

}