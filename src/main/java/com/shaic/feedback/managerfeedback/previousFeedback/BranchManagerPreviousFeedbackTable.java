package com.shaic.feedback.managerfeedback.previousFeedback;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.rod.citySearchCriteria.CitySearchCriteriaViewImpl;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.feedback.managerfeedback.ManagerFeedBackUIPresenter;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class BranchManagerPreviousFeedbackTable extends GBaseTable<BranchManagerPreviousFeedbackTableDTO>{
	
	@EJB
	MasterService masterService;
	
	@Inject
	private PreviousFeedbackView previousFeedbackView;
	
	private final static Object[] POLICY_COL_ORDER = new Object[]{"serialNumber", "Link","feedBackDate","feedBack","feedbackType", "feedbackStatus",
		"fbCategory","feedbackRemarksOverall", "technicalTeamReply"};
	
	private final static Object[] PROPOSAL_COL_ORDER = new Object[]{"serialNumber", "Link","feedBackDate","feedBack","feedbackType", "feedbackStatus",
		"fbCategory","feedbackRemarksOverall", "corpTeamReply"};
	
	private final static Object[] CLAIM_COL_ORDER = new Object[]{"serialNumber", "Link","feedBackDate","feedBack","feedbackType", "feedbackStatus",
		"fbCategory","feedbackRemarksOverall", "claimsDeptReply"};

	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}

	public void initTable() {
		table.removeGeneratedColumn("Link");
		table.addGeneratedColumn("Link", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button searchButton = new Button();
				final BranchManagerPreviousFeedbackTableDTO previousDto =  (BranchManagerPreviousFeedbackTableDTO) itemId;
				searchButton.setCaption("View");			
				searchButton.setData(previousDto);
				searchButton.setStyleName(ValoTheme.BUTTON_LINK);
				searchButton.setData(itemId);
				searchButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						showPreviousFeedbackView(previousDto);
						searchButton.setEnabled(true);
					}
				});

				return searchButton;
			}
		});
		table.setColumnHeader("Link", "");
		
		table.removeGeneratedColumn("feedbackRemarksOverall");
		table.addGeneratedColumn("feedbackRemarksOverall", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				BranchManagerPreviousFeedbackTableDTO branchManagerFeedbackTableDTO =  (BranchManagerPreviousFeedbackTableDTO) itemId;
				final TextArea remarks = new TextArea();
				final Button moreBtn;				
				remarks.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
				remarks.setData(branchManagerFeedbackTableDTO);	
				remarks.setWidth("330px");
				remarks.setRows(2);
				remarks.setMaxLength(110);
				BranchManagerPreviousFeedbackTableDTO data = (BranchManagerPreviousFeedbackTableDTO) itemId;
				if(null != data){
					remarks.setReadOnly(Boolean.FALSE);
					remarks.setValue(data.getFeedbackRemarksOverall());
					remarks.setReadOnly(Boolean.TRUE);
				}
				
				moreBtn = new Button("MORE");
				moreBtn.setEnabled(true);
				moreBtn.setData(itemId);
				moreBtn.setWidth("60px");
				remarks.setDescription("Overall");
				moreBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		    	
		    	HorizontalLayout remarksLayout = new HorizontalLayout();
		    	remarksLayout.addComponents(remarks,moreBtn);
		    	remarksLayout.setSpacing(Boolean.FALSE);
		    	
		    	moreBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						getRemarks(remarks);
			        	} 
			    });
				
				return remarksLayout;
		}
	});
		table.removeGeneratedColumn("technicalTeamReply");
		table.addGeneratedColumn("technicalTeamReply", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				BranchManagerPreviousFeedbackTableDTO branchManagerFeedbackTableDTO =  (BranchManagerPreviousFeedbackTableDTO) itemId;
				final TextArea remarks = new TextArea();
				final Button moreBtn;				
				remarks.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
				remarks.setData(branchManagerFeedbackTableDTO);	
				remarks.setWidth("340px");
				remarks.setRows(2);
				remarks.setMaxLength(110);
				remarks.setDescription("Reply");
				BranchManagerPreviousFeedbackTableDTO data = (BranchManagerPreviousFeedbackTableDTO) itemId;
				if(null != data){
					remarks.setReadOnly(Boolean.FALSE);
					if(null != data.getTechnicalTeamReply()){
						remarks.setValue(data.getTechnicalTeamReply());
					}
					remarks.setReadOnly(Boolean.TRUE);
				}
				
				moreBtn = new Button("MORE");
				moreBtn.setEnabled(true);
				moreBtn.setData(itemId);
				moreBtn.setWidth("60px");
				moreBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		    	
		    	HorizontalLayout remarksLayout = new HorizontalLayout();
		    	remarksLayout.addComponents(remarks,moreBtn);
		    	remarksLayout.setSpacing(Boolean.FALSE);
		    	
		    	moreBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						getRemarks(remarks);
			        	} 
			    });
				
				return remarksLayout;
		}
	});
		table.removeGeneratedColumn("corpTeamReply");
		table.addGeneratedColumn("corpTeamReply", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				BranchManagerPreviousFeedbackTableDTO branchManagerFeedbackTableDTO =  (BranchManagerPreviousFeedbackTableDTO) itemId;
				final TextArea remarks = new TextArea();
				final Button moreBtn;				
				remarks.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
				remarks.setData(branchManagerFeedbackTableDTO);	
				remarks.setWidth("360px");
				remarks.setRows(2);
				remarks.setMaxLength(110);
				remarks.setDescription("Reply");
				BranchManagerPreviousFeedbackTableDTO data = (BranchManagerPreviousFeedbackTableDTO) itemId;
				if(null != data){
					remarks.setReadOnly(Boolean.FALSE);
					if(null != data.getCorpTeamReply()){
						remarks.setValue(data.getCorpTeamReply());
					}
					remarks.setReadOnly(Boolean.TRUE);
				}
				
				moreBtn = new Button("MORE");
				moreBtn.setEnabled(true);
				moreBtn.setData(itemId);
				moreBtn.setWidth("60px");
				moreBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		    	
		    	HorizontalLayout remarksLayout = new HorizontalLayout();
		    	remarksLayout.addComponents(remarks,moreBtn);
		    	remarksLayout.setSpacing(Boolean.FALSE);
		    	
		    	moreBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						getRemarks(remarks);
			        	} 
			    });
				
				return remarksLayout;
		}
	});
		table.removeGeneratedColumn("claimsDeptReply");
		table.addGeneratedColumn("claimsDeptReply", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				BranchManagerPreviousFeedbackTableDTO branchManagerFeedbackTableDTO =  (BranchManagerPreviousFeedbackTableDTO) itemId;
				final TextArea remarks = new TextArea();
				final Button moreBtn;				
				remarks.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
				remarks.setData(branchManagerFeedbackTableDTO);	
				remarks.setWidth("360px");
				remarks.setRows(2);
				remarks.setMaxLength(110);
				remarks.setDescription("Reply");
				BranchManagerPreviousFeedbackTableDTO data = (BranchManagerPreviousFeedbackTableDTO) itemId;
				if(null != data){
					remarks.setReadOnly(Boolean.FALSE);
					if(null != data.getClaimsDeptReply()){
						remarks.setValue(data.getClaimsDeptReply());
					}
					remarks.setReadOnly(Boolean.TRUE);
				}
				
				moreBtn = new Button("MORE");
				moreBtn.setEnabled(true);
				moreBtn.setData(itemId);
				moreBtn.setWidth("60px");
				moreBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		    	
		    	HorizontalLayout remarksLayout = new HorizontalLayout();
		    	remarksLayout.addComponents(remarks,moreBtn);
		    	remarksLayout.setSpacing(Boolean.FALSE);
		    	
		    	moreBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						getRemarks(remarks);
			        	} 
			    });
				
				return remarksLayout;
		}
	});
		
		table.removeGeneratedColumn("feedbackType");
		table.addGeneratedColumn("feedbackType", new Table.ColumnGenerator() {
			

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
			//	final FormLayout image = new FormLayout();
			//	image.setMargin(false);
			//	image.setSpacing(true);
				BranchManagerPreviousFeedbackTableDTO branchManagerFeedbackTableDTO =  (BranchManagerPreviousFeedbackTableDTO) itemId;
				Image imageSrc = null;
				if(branchManagerFeedbackTableDTO.getFeedbackType().equals(ReferenceTable.VERY_GOOD_FEEDBACK)){
					imageSrc = new Image("", new ThemeResource("images/veryGoodFeedback.png"));
					
				}else if(branchManagerFeedbackTableDTO.getFeedbackType().equals(ReferenceTable.GOOD_FEEDBACK)){
					imageSrc = new Image("", new ThemeResource("images/goodFeedback.png"));
				}else if(branchManagerFeedbackTableDTO.getFeedbackType().equals(ReferenceTable.AVERAGE_FEEDBACK)) {
					imageSrc = new Image("", new ThemeResource("images/averageFeedback.png"));
				}else if(branchManagerFeedbackTableDTO.getFeedbackType().equals(ReferenceTable.SATISFACTORY_FEEDBACK)){
					imageSrc = new Image("", new ThemeResource("images/satisfactoryFeedback.png"));
				}else if(branchManagerFeedbackTableDTO.getFeedbackType().equals(ReferenceTable.BELOW_AVERAGE_FEEDBACK)){
					imageSrc = new Image("", new ThemeResource("images/belowAvgeFeedBack.png"));
				}
			//	image.addComponent(imageSrc);
			//	image.setData(branchManagerFeedbackTableDTO);
				return imageSrc;
			}
		});
		
		
		table.setColumnWidth("serialNumber", 40);
		table.setColumnWidth("Link", 50);
		table.setColumnWidth("feedBackDate", 100);
		table.setColumnWidth("feedbackType", 60);
		table.setColumnWidth("feedbackStatus", 80);
		table.setColumnWidth("feedbackRemarksOverall", 400);
		table.setContainerDataSource(new BeanItemContainer<BranchManagerPreviousFeedbackTableDTO>(BranchManagerPreviousFeedbackTableDTO.class));
		table.setHeight("320px");
	}

	public void setVisibleColumnsForPolicy(){
		 table.setVisibleColumns(POLICY_COL_ORDER);
		 localize(null);
	 }
	
	public void setVisibleColumnsForProposal(){
		 table.setVisibleColumns(PROPOSAL_COL_ORDER);
		 localize(null);
	 }
	
	public void setVisibleColumnsForClaim(){
		 table.setVisibleColumns(CLAIM_COL_ORDER);
		 localize(null);
	 }
	
	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "branch-manager-previous-feedback-";
	} 
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
	}

	@Override
	public void tableSelectHandler(BranchManagerPreviousFeedbackTableDTO t) {
		// TODO Auto-generated method stub
		
	}
	
	private void showPreviousFeedbackView(
			BranchManagerPreviousFeedbackTableDTO previousFeedbackTableDTO) {
		Window popup = new com.vaadin.ui.Window();
		fireViewEvent(BranchManagerPreviousFeedbackPresenter.LOAD_PREVIOUS_FEED_BACK_VIEW_VALUE, previousFeedbackTableDTO,previousFeedbackView);
		previousFeedbackView.init(previousFeedbackTableDTO);
		previousFeedbackView.setPopUp(popup);
		
		popup.setWidth("75%");
		popup.setHeight("90%");
		popup.setContent(previousFeedbackView);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
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
	
	public void getRemarks(final TextArea txtFld)
	{
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setReadOnly(Boolean.TRUE);
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
						/*SearchHoldMonitorScreenTableDTO mainDto = (SearchHoldMonitorScreenTableDTO)txtFld.getData();
						if(null != mainDto){
							mainDto.setHoldRemarks(txtFld.getValue());
						}*/
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();
				String strCaption = new String();
				if(txtFld != null && txtFld.getDescription() != null && txtFld.getDescription().equalsIgnoreCase("Overall")){
					strCaption = "Remarks";
				}else if(txtFld != null && txtFld.getDescription() != null && txtFld.getDescription().equalsIgnoreCase("Reply")){
					strCaption = "Reply Remarks";
				}
				
				
				dialog.setCaption(strCaption);
				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	

	}
}
