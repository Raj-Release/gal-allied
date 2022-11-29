package com.shaic.reimbursement.rod.allowReconsideration.search;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import net.sf.jasperreports.engine.xml.JRPenFactory.Bottom;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.manageclaim.reopenclaim.pageRodLevel.ReOpenRodLevelClaimPresenter;
import com.shaic.reimbursement.rod.cancelAcknowledgment.search.SearchCancelAcknowledgementPresenter;
import com.shaic.reimbursement.rod.cancelAcknowledgment.search.SearchCancelAcknowledgementTable;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODFormDTO;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODTableDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class SearchAllowReconsiderationViewImpl extends AbstractMVPView implements SearchAllowReconsideration {
	
	@Inject
	private SearchAllowReconsiderationForm searchAllowReconsiderForm;
	
	@Inject
	private SearchAllowReconsiderationTable searchAllowReconsiderTable;
	
	private VerticalLayout mainPanel;
	
	private TextArea rejectionRemarks;
	
	private FormLayout dynamicLayout;
	
	private HorizontalLayout buttonHorLayout;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private String remarks = null;
	
	private SearchAllowReconsiderationTableDTO reconsiderTableDetailsDto = null;
	
	private Boolean allowedReconsideration = false;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchAllowReconsiderForm.init();
		searchAllowReconsiderTable.init("", false, true);
		dynamicLayout = new FormLayout();
		dynamicLayout.setVisible(false);
		
		
		submitBtn=new Button("Submit");
		cancelBtn=new Button("Cancel");
        
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
        buttonHorLayout=new HorizontalLayout(submitBtn,cancelBtn);
        buttonHorLayout.setSpacing(true);
        buttonHorLayout.setMargin(false);
        
		
		mainPanel = new VerticalLayout(searchAllowReconsiderForm,searchAllowReconsiderTable,dynamicLayout,buttonHorLayout);
		mainPanel.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		mainPanel.setSpacing(true);
		setCompositionRoot(mainPanel);
		searchAllowReconsiderTable.addSearchListener(this);
		searchAllowReconsiderForm.addSearchListener(this);
		resetView();
		addListener();
	}
	
	@Override
	public void doSearch() {
		// TODO Auto-generated method stub

		
		SearchAllowReconsiderationDTO searchDTO = searchAllowReconsiderForm.getSearchDTO();
//		Pageable pageable = searchAllowReconsiderTable.getPageable();
		if(searchDTO.getIntimationNo() == null && searchDTO.getRodNo() == null || (searchDTO.getIntimationNo().isEmpty() && searchDTO.getRodNo().isEmpty())){
			showErrorPopup("<b>Please Select Any One Field is Mandatory for Search");
		} else {
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(SearchAllowReconsiderationPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		}
	
		
	}
	
	public void addListener() {
		


		
		cancelBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	resetSearchResultTableValues();
				                	reconsiderTableDetailsDto = null;
				                	fireViewEvent(MenuItemBean.ALLOW_RECONSIDERATION, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
				dialog.setClosable(false);
			}
		});
		submitBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatePage()){
//					List<SearchAllowReconsiderationTableDTO> selectReconsiderListDTO = searchAllowReconsiderTable.getValues();
					if(reconsiderTableDetailsDto != null && rejectionRemarks.getValue() != null && !rejectionRemarks.getValue().isEmpty()){
						reconsiderTableDetailsDto.setUnCheckRemarks(rejectionRemarks.getValue());
						fireViewEvent(SearchAllowReconsiderationPresenter.SUBMIT_RECONSIDERATION, reconsiderTableDetailsDto);
						resetSearchResultTableValues();
						resetView();
					}
						resetSearchResultTableValues();
						resetView();
					}					
			}
		});
		
	
	}
	
	public void setallowedRejectionReconsider(SearchAllowReconsiderationTableDTO reconsiderTableDto){
		
		if(reconsiderTableDto.getAllowReconsiderFlag() != null && reconsiderTableDto.getAllowReconsiderFlag().equalsIgnoreCase("Y")){
			dynamicLayout.removeAllComponents();
			
			rejectionRemarks = new TextArea("Reconsideration Uncheck Remarks");
			dynamicLayout.addComponent(rejectionRemarks);
			dynamicLayout.setComponentAlignment(rejectionRemarks, Alignment.MIDDLE_CENTER);
			dynamicLayout.setVisible(true);
			buttonHorLayout.setVisible(true);
//			buttonHorLayout.setVisible(true);
			 reconsiderTableDetailsDto = new SearchAllowReconsiderationTableDTO();
			 reconsiderTableDetailsDto = reconsiderTableDto;
			allowedReconsideration = Boolean.TRUE;
		}else {
			dynamicLayout.setVisible(false);
			buttonHorLayout.setVisible(false);
		}
		
	}
	
	

	
	public Boolean validatePage() {
		return true;
		
	}

	@Override
	public void resetSearchResultTableValues() {
		
		searchAllowReconsiderTable.getPageable().setPageNumber(1);
		searchAllowReconsiderTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchAllowReconsiderationTable)
			{
				((SearchAllowReconsiderationTable) comp).removeRow();
			}
		}
		
		dynamicLayout.setVisible(false);
		dynamicLayout.removeAllComponents();

	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		searchAllowReconsiderForm.refresh();
	}

	@Override
	public void list(Page<SearchAllowReconsiderationTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchAllowReconsiderTable.setTableList(tableRows);
//			searchAllowReconsiderTable.tablesize();
			searchAllowReconsiderTable.setHasNextPage(tableRows.isHasNext());
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Home");
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
					resetSearchResultTableValues();
					fireViewEvent(MenuItemBean.ALLOW_RECONSIDERATION, null);
					
				}
			});
		}
		
		searchAllowReconsiderForm.enableButtons();
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
	
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'>Rejection Reconsideration Unchecked Successfully!!!</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Home");
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
				fireViewEvent(MenuItemBean.ALLOW_RECONSIDERATION, null);

			}
		});
	}

}
