package com.shaic.claim.cvc.postprocess;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.cvc.SearchCVCFormDTO;
import com.shaic.claim.cvc.SearchCVCTableDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchPostProcessCVCViewImpl extends AbstractMVPView implements SearchPostProcessCVCView,Searchable {
	
	@Inject
	private PostProcessCVCForm searchForm;
	
	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();
	
	@PostConstruct
	protected void initView() {
		
		addStyleName("view");
		setSizeFull();
		searchForm.init();		
		mainPanel.setFirstComponent(searchForm);	
	
		mainPanel.setSplitPosition(50);
		mainPanel.setWidth("100.0%");
		setHeight("100.0%");
		setHeight("550px");
		setCompositionRoot(mainPanel);
		searchForm.addSearchListener(this);
		resetView();
	
		}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSearch() {
		
		SearchCVCFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = new Pageable();
		searchDTO.setPageable(pageable);

		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		ImsUser imsUser = (ImsUser)getUI().getSession().getAttribute(BPMClientContext.USER_OBJECT);
		searchDTO.setImsUser(imsUser);
		if(searchDTO != null && searchDTO.getIntimationNo() != null 
				&& searchDTO.getIntimationNo().contains("C")){
			fireViewEvent(SearchPostClaimProcessCVCPresenter.SEARCH_BUTTON_CLICK_POST_PROCESS, searchDTO,userName,passWord,imsUser);
		} else {
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Post Claim Process Audit Home");
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
					fireViewEvent(MenuItemBean.POST_PROCESS_CVC,null);
				}
			});
		}
		searchForm.resetAlltheValues();
	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void list(SearchCVCTableDTO bean) {
		// TODO Auto-generated method stub
		if(null != bean && bean.getIntimationKey() != null) {
			fireViewEvent(MenuPresenter.POST_PROCESS_CVC_WIZARD,bean);
		} else
			{
					Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
					Button homeButton = new Button("Post Claim Process Audit Home");
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
							fireViewEvent(MenuItemBean.POST_PROCESS_CVC,null);
						}
					});
			}
	}


}
