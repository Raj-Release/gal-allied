/**
 * 
 */
package com.shaic.paclaim.reminder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.queryrejection.generateremainder.search.SearchGenerateRemainderFormDTO;
import com.shaic.reimbursement.queryrejection.generateremainder.search.SearchGenerateReminderTableDTO;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class SearchGeneratePARemainderViewImpl extends AbstractMVPView implements SearchGeneratePARemainderView{

	
	@Inject
	private SearchGeneratePARemainderForm  searchForm;
	
	@Inject
	private SearchGeneratePARemainderTable searchResultTable;
	
	@Inject
	private ShowPAReminderLetterPage showReminderLetterPage;
	
	
	private VerticalSplitPanel mainPanel;
	private SearchGenerateReminderTableDTO reminderLetterDto;
	
	private Button closeButton;
	private Button excelButton;
	private ExcelExport excelExport;
	private Button clearButton;
	private VerticalLayout btnLayout; 
	private VerticalLayout vLayout;
	private HorizontalLayout buttonsLayout;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		
		vLayout = new VerticalLayout(searchResultTable);
		addFooterButtons();
		
		mainPanel.setSecondComponent(vLayout);
		mainPanel.setSplitPosition(42);
		setHeight("670");
		mainPanel.setHeight("650");
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
		SearchGenerateRemainderFormDTO searchDTO = searchForm.getSearchFilters();
		searchDTO.setLobId(ReferenceTable.PA_LOB_KEY);
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(SearchGeneratePARemainderPresenter.SEARCH_PA_BUTTON_CLICK, searchDTO,userName,passWord);
		
	}

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.removeRow();
		searchResultTable.resetTable();
//		Iterator<Component> componentIter = mainPanel.getComponentIterator();
//		while(componentIter.hasNext())
//		{
//			Component comp = (Component)componentIter.next();
//			
//			
//			
//			if(comp instanceof SearchGenerateRemainderTable)
//			{
//				((SearchGenerateRemainderTable) comp).removeRow();
//			}
//		}
	
		
	}

	@Override
	public void list(Page<SearchGenerateReminderTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Generate Reminder Letter Home");
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
					fireViewEvent(MenuItemBean.GENERATE_PA_REMINDER_LETTER_CLAIM_WISE, null);
					
				}
			});
		}
		searchForm.enableButtons();
	}

	@Override
	public void init(Map<String, Object> parameter) {
		searchForm.setDropDownValues(parameter);
		
	}
	
	public void generateReminderLetter(SearchGenerateReminderTableDTO reminderLetterDto)
	{
		this.reminderLetterDto = reminderLetterDto;
		ReportDto reportDto = new ReportDto();
		DocumentGenerator docGenarator = new DocumentGenerator();
		String fileUrl = null;
		
		if(reminderLetterDto != null && reminderLetterDto.getQueryKey() != null){
			ReimbursementQueryDto queryDto = reminderLetterDto.getReimbQueryDto();
			List<ReimbursementQueryDto> queryDtoList = new ArrayList<ReimbursementQueryDto>();
			queryDtoList.add(queryDto);	
			reportDto.setBeanList(queryDtoList);
			reportDto.setClaimId(queryDto.getClaimId());
			
			fileUrl = docGenarator.generatePdfDocument("ReimburseQueryReminderLetter", reportDto);
		}
		
		else if(reminderLetterDto != null && reminderLetterDto.getPreauthKey() == null && reminderLetterDto.getQueryKey() == null){
			ClaimDto claimDto = reminderLetterDto.getClaimDto();
			List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
			claimDtoList.add(claimDto);
			reportDto.setBeanList(claimDtoList);
			reportDto.setClaimId(claimDto.getClaimId());
			
			fileUrl = docGenarator.generatePdfDocument("ReimburseClaimReminderLetter", reportDto);
			
		}
		else if(reminderLetterDto != null && reminderLetterDto.getPreauthKey() != null){
			ClaimDto claimDto = reminderLetterDto.getClaimDto();
			List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
			claimDtoList.add(claimDto);
			reportDto.setBeanList(claimDtoList);
			reportDto.setClaimId(claimDto.getClaimId());
			
			fileUrl = docGenarator.generatePdfDocument("CashlessReminderLetter", reportDto);
			
		}		
		
		
		if(!ValidatorUtils.isNull(fileUrl))
		{
			reminderLetterDto.setFileUrl(fileUrl);
			openPdfFileInWindow(reminderLetterDto);
			
			//TODO IMPLEMENTEATION OF BPM OUT PUT AND DB UPDATION NEED TO BE DONE
		}
		else
		{
			//Exception while PDF Letter Generation
		}	
		
	}
	
	public void openPdfFileInWindow(SearchGenerateReminderTableDTO reminderLetterDto) {
		
		final Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("Reminder Letter PDF");
		window.setWidth("800");
		window.setHeight("600");
		window.setModal(true);
		window.setClosable(true);
		window.center();
		window.addCloseListener(new Window.CloseListener() {
			@Override
			public void windowClose(CloseEvent e) {
				window.close();
			}
		});
		VerticalLayout letterLayout = new VerticalLayout();
		letterLayout.setSizeFull();
		showReminderLetterPage.initView(this, reminderLetterDto.getFileUrl()); 
		letterLayout.addComponent(showReminderLetterPage);
		window.setContent(letterLayout);
		UI.getCurrent().addWindow(window);
	}
	
	public void submitReminderLetter(){
		Collection<Window> windows = UI.getCurrent().getWindows();
		for (Window window : windows) {
			window.close();
		}
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		reminderLetterDto.setUsername(userName);
		reminderLetterDto.setPassword(passWord);
		fireViewEvent(SearchGeneratePARemainderPresenter.SUBMIT_PA_LETTER, reminderLetterDto);
	}
	
	
	@Override
	public void clearReminderLetterSearch() {
		searchForm.resetFields();
//		clearSearchForm();
		resetSearchResultTableValues();
	}

	public void addFooterButtons(){
		
		buttonsLayout = new HorizontalLayout();
		
		closeButton = new Button("Close");
		closeButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(SearchGeneratePARemainderPresenter.CLEAR_PA_SEARCH_FORM,null);
				fireViewEvent(MenuItemBean.GENERATE_PA_REMINDER_LETTER_CLAIM_WISE,null);				
			}
		});
		
		excelButton = new Button("Excel");
		excelButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(searchResultTable.getTable() != null &&  searchResultTable.getTable().getContainerDataSource() != null && searchResultTable.getTable().getContainerDataSource().size() > 0)
				{	
					excelExport = new ExcelExport(searchResultTable.getTable());
					excelExport.setReportTitle("Reminder Letter Search Details");
					excelExport.setDisplayTotals(false);
					excelExport.export();
				}
			}
		});
		
		clearButton = new Button("Clear");
		clearButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(SearchGeneratePARemainderPresenter.RESET_PA_SEARCH_FORM,null);
				
			}
		});
		
		buttonsLayout.addComponents(closeButton,excelButton,clearButton);
		buttonsLayout.setSpacing(true);
		btnLayout = new VerticalLayout(buttonsLayout);
		btnLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
		
		vLayout.addComponent(btnLayout);
		
	}
	
//	public void clearSearchForm(){
//		
//		if(btnLayout != null){
//			btnLayout.removeAllComponents();
//			vLayout.removeComponent(btnLayout);			
//		}
//	}
	
	
	@Override
	public void resetReminderLetterSearch() {

		searchForm.resetFields();
//		clearSearchForm();
		resetSearchResultTableValues();
	}

}
