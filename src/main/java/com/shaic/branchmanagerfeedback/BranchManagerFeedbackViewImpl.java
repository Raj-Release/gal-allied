package com.shaic.branchmanagerfeedback;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.preauth.search.SearchPreAuthList;
import com.shaic.claim.reimbursement.rrc.services.SearchReviewRRCRequestFormDTO;
import com.shaic.claim.reimbursement.rrc.services.SearchReviewRRCRequestPresenter;
import com.shaic.domain.HospitalService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class BranchManagerFeedbackViewImpl extends AbstractMVPView implements BranchManagerFeedbackView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ClaimsFeedbackTable claimsFeedbackTable;
	
	@Inject
	private BranchManagerFeedbackUI branchManagerFeedbackUI;
	
	@Inject
	private PolicyFeedbackTable policyFeedbackTable;
	
	@Inject
	private ProposalTeamFeedbackTable proposalTeamFeedbackTable;
	
	@Inject
	private TechnicalTeamReplyOnFeedback technicalTeamReplyOnFeedback;
	
	
	private VerticalSplitPanel mainPanel;
	
	private Window popup;
	private Label totalCount;
	
	@PostConstruct
	public void initView() {
		addStyleName("view");
		setSizeFull();
		branchManagerFeedbackUI.init();
		policyFeedbackTable.init("", false, false);
		proposalTeamFeedbackTable.init("", false, false);
		claimsFeedbackTable.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(branchManagerFeedbackUI);
		mainPanel.setSecondComponent(policyFeedbackTable);
		mainPanel.setSplitPosition(32);
		setHeight("570px");
		policyFeedbackTable.addSearchListener(this);
		branchManagerFeedbackUI.addSearchListener(this);
	//	mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		
	
		//resetView();
	}
	
	/*public void init() {
		addStyleName("view");
	        
	}*/

	@Override
	public void resetView() {
		branchManagerFeedbackUI.refresh();
	}
	
	@Override
	public void doSearch() {
		String err = branchManagerFeedbackUI.validate();
		if(err == null || err.isEmpty())
		{
			TechnicalDepartmentFeedbackDTO searchDTO = branchManagerFeedbackUI.getSearchDTO();
			
			if(searchDTO.getFeedbackValue()==null){
				getErrorMessage("Please select Feedback");
			}else 
			if(searchDTO != null && (searchDTO.getFeedbackValue() != null || searchDTO.getFeedbackValue().getValue() != null)){
				fireViewEvent(BranchManagerFeedbackPresenter.BRANCH_TYPE_SEARCH, searchDTO, null);
				SelectValue feedbackValue = searchDTO.getFeedbackValue();
				if(feedbackValue.getValue()!=null && feedbackValue.getValue().equals("Policy")) {
					mainPanel.setSecondComponent(policyFeedbackTable);
					setCompositionRoot(mainPanel);
					
				}
				else if(feedbackValue.getValue()!=null && feedbackValue.getValue().equals("Proposal")) {
					mainPanel.setSecondComponent(proposalTeamFeedbackTable);
					setCompositionRoot(mainPanel);
				}else if(feedbackValue.getValue()!=null && feedbackValue.getValue().equals("Claim")) {
					mainPanel.setSecondComponent(claimsFeedbackTable);
					setCompositionRoot(mainPanel);
				}
			}
		}
		else
		{
			showErrorMessage(err);
		}
		
	}

	@Override
	public void resetSearchResultTableValues() {

		// TODO Auto-generated method stub
		
		policyFeedbackTable.getPageable().setPageNumber(1);
		policyFeedbackTable.resetTable();
		
		proposalTeamFeedbackTable.getPageable().setPageNumber(1);
		proposalTeamFeedbackTable.resetTable();
		
		claimsFeedbackTable.getPageable().setPageNumber(1);
		claimsFeedbackTable.resetTable();
		
		branchManagerFeedbackUI.refresh();
		
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof PolicyFeedbackTable)
			{
				((PolicyFeedbackTable) comp).removeRow();
			}
			
			if(comp instanceof ProposalTeamFeedbackTable)
			{
				((ProposalTeamFeedbackTable) comp).removeRow();
			}
			if(comp instanceof ClaimsFeedbackTable)
			{
				((ClaimsFeedbackTable) comp).removeRow();
			}
		}
		policyFeedbackTable.init("", false, false);
		proposalTeamFeedbackTable.init("", false, false);
		claimsFeedbackTable.init("", false, false);
		
	}
	
	public void setDropDownValues(BeanItemContainer<SelectValue> feedbackValue,
			BeanItemContainer<SelectValue> zoneValue, BeanItemContainer<SelectValue> branchValue,
			BeanItemContainer<SelectValue> feedbackStatusValue, BeanItemContainer<SelectValue> feedbackTypeValue) {
		branchManagerFeedbackUI.setDropDownValues(feedbackValue,zoneValue,branchValue,feedbackStatusValue,feedbackTypeValue);
		TechnicalDepartmentFeedbackDTO searchDTO = branchManagerFeedbackUI.getSearchDTO();
		if(searchDTO.getFeedbackValue() != null){
			
			if(searchDTO.getFeedbackValue().getId().equals(ReferenceTable.PROPOSAL_FEEDBACK_TYPE_KEY)){
				mainPanel.removeComponent(policyFeedbackTable);
				mainPanel.setSecondComponent(proposalTeamFeedbackTable);
			}
			if(searchDTO.getFeedbackValue().getId().equals(ReferenceTable.CLAIM_FEEDBACK_TYPE_KEY)){
				mainPanel.removeComponent(policyFeedbackTable);
				mainPanel.setSecondComponent(claimsFeedbackTable);
			}
		}
	}

	@Override
	public void setTableList(Page<BranchManagerFeedbackTableDTO> resultList) {
		
		if(resultList != null){
		
		policyFeedbackTable.setTableList(resultList);
		claimsFeedbackTable.setTableList(resultList);
		proposalTeamFeedbackTable.setTableList(resultList);}
		else{
			branchManagerFeedbackUI.resetAllValues();
			getErrorMessage("No Records Found");
		}
		
	}

	@Override
	public void showFeedbaclReply() {
		
		mainPanel.setFirstComponent(technicalTeamReplyOnFeedback);
		
	}

	@Override
	public void showPolicyPopup(BranchManagerFeedbackTableDTO tableDTO) {
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String branchManagerName[] = tableDTO.getBranchDetails().split(",");
		tableDTO.setBranchManagerName(branchManagerName[0]);
		tableDTO.setBranchName(branchManagerName[1]);
		tableDTO.setUsername(userName);
		
			popup = new com.vaadin.ui.Window();
			
			technicalTeamReplyOnFeedback.init(tableDTO);
			technicalTeamReplyOnFeedback.setPopUp(popup);
			popup.setWidth("75%");
			popup.setHeight("65%");
			popup.setContent(technicalTeamReplyOnFeedback);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});
			popup.setModal(true);
			
			UI.getCurrent().addWindow(popup);
		}
	

	@Override
	public void showProposalPopup(BranchManagerFeedbackTableDTO tableDTO) {
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String branchManagerName[] = tableDTO.getBranchDetails().split(",");
		tableDTO.setBranchManagerName(branchManagerName[0]);
		tableDTO.setBranchName(branchManagerName[1]);
		tableDTO.setUsername(userName);
			popup = new com.vaadin.ui.Window();
			
			technicalTeamReplyOnFeedback.init(tableDTO);
			technicalTeamReplyOnFeedback.setPopUp(popup);
			popup.setWidth("75%");
			popup.setHeight("95%");
			popup.setContent(technicalTeamReplyOnFeedback);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});
			popup.setModal(true);
			
			UI.getCurrent().addWindow(popup);
	}

	@Override
	public void showClaimPopup(BranchManagerFeedbackTableDTO tableDTO) {
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String branchManagerName[] = tableDTO.getBranchDetails().split(",");
		tableDTO.setBranchManagerName(branchManagerName[0]);
		tableDTO.setBranchName(branchManagerName[1]);
		tableDTO.setUsername(userName);
		
			popup = new com.vaadin.ui.Window();
			
			technicalTeamReplyOnFeedback.init(tableDTO);
			technicalTeamReplyOnFeedback.setPopUp(popup);
			popup.setWidth("75%");
			popup.setHeight("65%");
			popup.setContent(technicalTeamReplyOnFeedback);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});
			popup.setModal(true);
			
			UI.getCurrent().addWindow(popup);
	
	}

	@Override
	public void showResult(){
		Label successLabel = new Label("<b style = 'color: black;'>Records Saved Successfully.</b>", ContentMode.HTML);			
		Button homeButton = new Button("Reply to Branch Manager's Feedback Home");
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

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(popup != null){
					popup.close();
				}
				fireViewEvent(MenuItemBean.MANAGER_FEEDBACK, null);
				
			}
		});
	}
	
	public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
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
	public void loadBranchDetails(
			BeanItemContainer<SelectValue> branchValueContainer) {
		branchManagerFeedbackUI.loadBranchDetails(branchValueContainer);
		
	}
}
