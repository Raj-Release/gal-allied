package com.shaic.branchmanagerfeedback;

import java.util.List;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PolicyFeedbackReply  extends GBaseTable<BranchManagerFeedbackTableDTO> {
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber", "intimationNo", "policyRemarks", "technicalTeamReply"};
	

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeGeneratedColumn("policyRemarks");
		table.addGeneratedColumn("policyRemarks", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				BranchManagerFeedbackTableDTO branchManagerFeedbackTableDTO =  (BranchManagerFeedbackTableDTO) itemId;
				final TextArea remarks = new TextArea();
				final Button moreBtn;				
				remarks.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
				remarks.setData(branchManagerFeedbackTableDTO);	
				remarks.setWidth("430px");
				remarks.setRows(3);
				remarks.setMaxLength(230);
				BranchManagerFeedbackTableDTO data = (BranchManagerFeedbackTableDTO) itemId;
				if(null != data){
					remarks.setReadOnly(Boolean.FALSE);
					if(null != data.getPolicyRemarks() ){
						remarks.setValue(data.getPolicyRemarks());
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
		table.removeGeneratedColumn("technicalTeamReply");
		table.addGeneratedColumn("technicalTeamReply", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				BranchManagerFeedbackTableDTO branchManagerFeedbackTableDTO =  (BranchManagerFeedbackTableDTO) itemId;
				final TextArea textarea = new TextArea();
				textarea.setWidth("300px");
				textarea.setHeight("50px");
				textarea.setMaxLength(4000);
				textarea.setData(branchManagerFeedbackTableDTO);
				addListener(textarea);
				SHAUtils.handlePopUpFeedbackRemarks(textarea,null,getUI(),SHAConstants.FEEDBACK_POLICY);
				textarea.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				return textarea;
		}
	});
		table.setColumnWidth("policyRemarks", 500);
		table.setContainerDataSource(new BeanItemContainer<BranchManagerFeedbackTableDTO>(BranchManagerFeedbackTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);		
		table.setSizeFull();
		table.setHeight("250px");
			
	}
	
	

	@Override
	public void tableSelectHandler(BranchManagerFeedbackTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "policy-feedback-reply-";
	}

	private void addListener(final TextArea txtField)
	{	
		txtField
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					String value = (String) event.getProperty().getValue();
					BranchManagerFeedbackTableDTO tableDTO = (BranchManagerFeedbackTableDTO)txtField.getData();
					tableDTO.setTechnicalTeamReply(value);
				}
			}
		});
	}
	
	 public List<BranchManagerFeedbackTableDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<BranchManagerFeedbackTableDTO> itemIds = (List<BranchManagerFeedbackTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
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
					
					String strCaption = "Hold Remarks";
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
