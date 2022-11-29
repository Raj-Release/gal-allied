package com.shaic.reimbursement.investigationin_direct_assigment.search;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.GMVPView;
import com.shaic.claim.cpuskipzmr.SkipZMRListenerTable;
import com.shaic.claim.cpuskipzmr.SkipZMRUI;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class InvestigationDirectAssignmentViewImpl extends AbstractMVPView
		implements GMVPView, InvestigationDirectAssignmentView {
	
	
	@Inject
	private Instance<InvestigationDirectAssignmentUI> idaUIInstance;
	private InvestigationDirectAssignmentUI investigationDirectAssignmentUI;	 
	
	@Inject
	private Instance<InvestigationDirectAssignmentTable> idatable;
	
	private InvestigationDirectAssignmentTable idaTableObj;
	
//	@Inject
//	private InvestigationDirectAssignmentTable investigationDirectAssignmentTable;
	

	
	
	private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	public void init() {
		addStyleName("view");
	        
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		if(investigationDirectAssignmentUI != null) {
			investigationDirectAssignmentUI.init();
		}

	}
	
	

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setSizeFull();
		investigationDirectAssignmentUI = idaUIInstance.get();
		investigationDirectAssignmentUI.initView();         
	     setCompositionRoot(investigationDirectAssignmentUI);
		
	}

	@Override
	public void buildSuccessLayout() {
		// TODO Auto-generated method stub

		Label successLabel = new Label(
				"<b style = 'color: green;'> Record Updated Successfully.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
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
				fireViewEvent(MenuItemBean.INVESTIGATION_DIRECT_ASSIGNEMENT, null);
			}
		});
		
	
		
	}

	@Override
	public void buildFailureLayout(String message) {
		// TODO Auto-generated method stub

		Label successLabel = new Label("<b style = 'color: black;'>"+message+"</b>", ContentMode.HTML);			
		Button homeButton = new Button("Process Claim Request Home");
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
				fireViewEvent(MenuItemBean.INVESTIGATION_DIRECT_ASSIGNEMENT, null);
				
			}
		});
	
		
	}

	@Override
	public void generateTableForPolicyStatus(List<InvestigationDirectAssignmentTableDTO>policyNo) {
		// TODO Auto-generated method stub
		if(investigationDirectAssignmentUI != null) {
			investigationDirectAssignmentUI.generateTableForPolicyStatus(policyNo);
		}

		}

	
	
		
		
		
	

	

}
