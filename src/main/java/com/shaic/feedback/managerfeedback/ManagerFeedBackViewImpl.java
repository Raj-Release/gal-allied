package com.shaic.feedback.managerfeedback;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.VerticalLayout;

public class ManagerFeedBackViewImpl extends AbstractMVPView implements ManagerFeedBackView {
	
	@Inject
	private ManagerFeedBackUI managerFeedbackUI;
	
	@Inject
	private BranchManagerFeedbackHomePage branchManagerFeedbackHomePage;
	
	public void init() {
		addStyleName("view");
		managerFeedbackUI.initView();  
		 VerticalLayout mainvLayout= new VerticalLayout();
		 mainvLayout.addComponent(managerFeedbackUI);
		 mainvLayout.addStyleName("myonestyle");
	     setCompositionRoot(mainvLayout);
	        
	}
	
	@Override
	public void initHomePage(BranchManagerFeedbackhomePageDto homePageDto) {
//		addStyleName("view");
		 branchManagerFeedbackHomePage.initView(homePageDto);  
		 VerticalLayout mainvLayout= new VerticalLayout();
		 mainvLayout.addComponent(branchManagerFeedbackHomePage);
//		 mainvLayout.addStyleName("myonestyle");
		 mainvLayout.setSizeFull();
	     setCompositionRoot(mainvLayout);
	     setSizeFull();
	        
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		 setSizeFull();
	}

	@Override
	public void buildSuccessLayout() {
		// TODO Auto-generated method stub
		managerFeedbackUI.buildSuccessLayout();
	}
	
	public void setValuesForCompletedCase(Long dailycount,Long overallcount){
		
		managerFeedbackUI.setValuesForCompletedCase(dailycount,overallcount);
	}

	public void setDropDownValues(BeanItemContainer<SelectValue> branchName) {
		// TODO Auto-generated method stub
		managerFeedbackUI.setDropDownValues(branchName);
	}

	@Override
	public void setHomePageDropDownValues(BeanItemContainer<SelectValue> branchNameContainer) {

		branchManagerFeedbackHomePage.setDropDownValues(branchNameContainer);
	}
	
	@Override
	public void showRevisedTableValues(BranchManagerFeedbackhomePageDto homePageDto){
		
		branchManagerFeedbackHomePage.buildTableDetails(homePageDto);
	}
	
	@Override
	public void buildReviewReplySuccessLayout() {
		// TODO Auto-generated method stub
		managerFeedbackUI.buildReviewReplySuccessLayout();
	}
}
