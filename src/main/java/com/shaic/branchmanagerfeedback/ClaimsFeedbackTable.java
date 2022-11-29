package com.shaic.branchmanagerfeedback;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ClaimsFeedbackTable extends GBaseTable<BranchManagerFeedbackTableDTO>{
	
	
	@EJB
	MasterService masterService;	
	@Inject
	private ManagerFeedbackReplyView replyFeedbackView;
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber", "Link", "branchDetails", "reportedDate","feedBack", "feedbackType", "feedbackStatus",
		"claimNumber","fbCategory","feedbackRemarksOverall", "claimsDepartmentReply"};

	
	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {
		table.removeGeneratedColumn("Link");
		table.addGeneratedColumn("Link", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button searchButton = new Button();
				BranchManagerFeedbackTableDTO branchManagerFeedbackTableDTO =  (BranchManagerFeedbackTableDTO) itemId;
				if(branchManagerFeedbackTableDTO.getFeedbackStatusId().equals(ReferenceTable.FEEDBACK_REPORTED_KEY)){
					searchButton.setCaption("Reply");
				}else {
					searchButton.setCaption("View");

				}
				searchButton.setData(branchManagerFeedbackTableDTO);
				searchButton.setStyleName(ValoTheme.BUTTON_LINK);
				searchButton.setData(itemId);
				searchButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						BranchManagerFeedbackTableDTO currentItemId = (BranchManagerFeedbackTableDTO) event.getButton().getData();
						
						if(currentItemId.getFeedbackStatusId().equals(ReferenceTable.FEEDBACK_REPORTED_KEY)){
							
							MastersValue masterData = masterService.getMaster(currentItemId.getFeedbackKey());
							currentItemId.setFeedbackValue(masterData.getValue());
							fireViewEvent(BranchManagerFeedbackPresenter.CLAIM_REPLY_FEEDBACK, currentItemId);
						}else {
							// view to be implemented
							showPreviousFeedbackView(currentItemId);
							searchButton.setEnabled(true);
						}					
						
						searchButton.setEnabled(true);
						//showIfscPopup(updatePaymentDetailTableDTO);
					}
				});

				return searchButton;
			}
		});
		
		table.removeGeneratedColumn("feedbackType");
		table.addGeneratedColumn("feedbackType", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
//				final FormLayout image = new FormLayout();
				//	image.setMargin(false);
				//	image.setSpacing(true);
					BranchManagerFeedbackTableDTO branchManagerFeedbackTableDTO =  (BranchManagerFeedbackTableDTO) itemId;
					Image imageSrc = null;
					if(branchManagerFeedbackTableDTO.getFeedbackType().equals(ReferenceTable.VERY_GOOD_FEEDBACK)){
						imageSrc = new Image("", new ThemeResource("images/VeryGoodFortable.png"));
						
					}else if(branchManagerFeedbackTableDTO.getFeedbackType().equals(ReferenceTable.GOOD_FEEDBACK)){
						imageSrc = new Image("", new ThemeResource("images/GoodFortable.png"));
					}else if(branchManagerFeedbackTableDTO.getFeedbackType().equals(ReferenceTable.AVERAGE_FEEDBACK)) {
						imageSrc = new Image("", new ThemeResource("images/AverageFortable.png"));
					}else if(branchManagerFeedbackTableDTO.getFeedbackType().equals(ReferenceTable.SATISFACTORY_FEEDBACK)){
						imageSrc = new Image("", new ThemeResource("images/SatisfactoryFortable.png"));
					}else if(branchManagerFeedbackTableDTO.getFeedbackType().equals(ReferenceTable.BELOW_AVERAGE_FEEDBACK)){
						imageSrc = new Image("", new ThemeResource("images/BelowAverageFortable.png"));
					}
				//	image.addComponent(imageSrc);
				//	image.setData(branchManagerFeedbackTableDTO);
					return imageSrc;
			}
		});
		table.setColumnHeader("Link", "");
		table.removeGeneratedColumn("feedbackRemarksOverall");
		table.addGeneratedColumn("feedbackRemarksOverall", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				BranchManagerFeedbackTableDTO branchManagerFeedbackTableDTO =  (BranchManagerFeedbackTableDTO) itemId;
				final TextArea remarks = new TextArea();
				final Button moreBtn;				
				remarks.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
				remarks.setData(branchManagerFeedbackTableDTO);	
				remarks.setWidth("330px");
				remarks.setRows(2);
				remarks.setMaxLength(110);
				BranchManagerFeedbackTableDTO data = (BranchManagerFeedbackTableDTO) itemId;
				if(null != data){
					remarks.setReadOnly(Boolean.FALSE);
					remarks.setValue(data.getFeedbackRemarksOverall());
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
		table.removeGeneratedColumn("claimsDepartmentReply");
		table.addGeneratedColumn("claimsDepartmentReply", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				BranchManagerFeedbackTableDTO branchManagerFeedbackTableDTO =  (BranchManagerFeedbackTableDTO) itemId;
				final TextArea remarks = new TextArea();
				final Button moreBtn;				
				remarks.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
				remarks.setData(branchManagerFeedbackTableDTO);	
				remarks.setWidth("360px");
				remarks.setRows(3);
				remarks.setMaxLength(230);
				BranchManagerFeedbackTableDTO data = (BranchManagerFeedbackTableDTO) itemId;
				if(null != data){
					remarks.setReadOnly(Boolean.FALSE);
					if(null != data.getClaimsDepartmentReply()){
						remarks.setValue(data.getClaimsDepartmentReply());
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
		table.setColumnHeader("Link", "");
		

		table.setColumnWidth("serialNumber", 40);
		table.setColumnWidth("Link", 60);
		table.setColumnWidth("feedBackDate", 100);
		table.setColumnWidth("feedbackType", 60);
		table.setColumnWidth("feedbackStatus", 80);
		table.setColumnWidth("feedbackRemarksOverall", 400);
		table.setColumnWidth("branchDetails", 150);	
		table.setContainerDataSource(new BeanItemContainer<BranchManagerFeedbackTableDTO>(BranchManagerFeedbackTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("340px");
	}


	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "branch-manager-claims-feedback-";
	} 
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
	}

	@Override
	public void tableSelectHandler(BranchManagerFeedbackTableDTO t) {
		// TODO Auto-generated method stub
		
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
				
				String strCaption = "Remarks";
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
	private void showPreviousFeedbackView(
			BranchManagerFeedbackTableDTO feedbackTableDTO) {
		Window popup = new com.vaadin.ui.Window();
		fireViewEvent(BranchManagerFeedbackPresenter.LOAD_PREVIOUS_FEED_BACK_VIEW_VALUE, feedbackTableDTO,replyFeedbackView);
		replyFeedbackView.init(feedbackTableDTO);
		replyFeedbackView.setPopUp(popup);
		
		popup.setWidth("75%");
		popup.setHeight("90%");
		popup.setContent(replyFeedbackView);
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


}
