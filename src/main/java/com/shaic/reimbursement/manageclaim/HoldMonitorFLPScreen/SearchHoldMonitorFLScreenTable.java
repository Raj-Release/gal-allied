package com.shaic.reimbursement.manageclaim.HoldMonitorFLPScreen;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.bcel.generic.GETSTATIC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.search.autoallocation.SearchPreAuthAutoAllocationTable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthFormDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.MasUserAutoAllocation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;
import com.shaic.reimbursement.rod.allowReconsideration.search.SearchAllowReconsiderationPresenter;
import com.shaic.reimbursement.rod.allowReconsideration.search.SearchAllowReconsiderationTableDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

public class SearchHoldMonitorFLScreenTable extends GBaseTable<SearchHoldMonitorScreenTableDTO>{
	
	private final Logger log = LoggerFactory.getLogger(SearchPreAuthAutoAllocationTable.class);
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNumber","reqDate","type","leg","holdBy","holdDate","holdRemarks"};
	private String screenName;
	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SearchHoldMonitorScreenTableDTO>(SearchHoldMonitorScreenTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
//		table.setHeight("300px");
		table.setPageLength(10);
		table.addStyleName("generateColumnTable");
		table.removeGeneratedColumn("holdRemarks");
		table.removeGeneratedColumn("actions");
		table.addGeneratedColumn("holdRemarks", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button moreBtn;
				final TextArea holdRemarks;
				
				holdRemarks = new TextArea();
				holdRemarks.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
				holdRemarks.setData(itemId);				
				holdRemarks.setWidth("250px");
				holdRemarks.setRows(2);
				holdRemarks.setMaxLength(110);
				SearchHoldMonitorScreenTableDTO data = (SearchHoldMonitorScreenTableDTO) itemId;
				if(null != data){
					holdRemarks.setReadOnly(Boolean.FALSE);
					holdRemarks.setValue(data.getHoldRemarks());
					holdRemarks.setReadOnly(Boolean.TRUE);
				}
				
				moreBtn = new Button("MORE");
				moreBtn.setEnabled(true);
				moreBtn.setData(itemId);
				moreBtn.setWidth("60px");
				moreBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		    	
		    	HorizontalLayout remarksLayout = new HorizontalLayout();
		    	remarksLayout.addComponents(holdRemarks,moreBtn);
		    	remarksLayout.setSpacing(Boolean.FALSE);
		    	
		    	moreBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
							getHoldRemarks(holdRemarks);
			        	} 
			    });
		        return remarksLayout;
		      }
		    });
		
		table.addGeneratedColumn("actions", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button releaseBtn;
				releaseBtn = new Button("RELEASE");
		    	releaseBtn.setData(itemId);
		    	releaseBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
							Object currentItemId = event.getButton().getData();
				        	SearchHoldMonitorScreenTableDTO data = (SearchHoldMonitorScreenTableDTO) itemId;
				        	if(data !=null){
				        		fireViewEvent(SearchMonitorFLPScreenPresenter.RELEASE_BUTTON,data);
				        		table.removeItem(currentItemId);
				        	}
			        } 
			    });
		    	releaseBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		        return releaseBtn;
		      }
		    });
	}

	@Override
	public void tableSelectHandler(SearchHoldMonitorScreenTableDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "hold-moniter-screen-";
	}
	
	public void getHoldRemarks(final TextArea txtFld)
	{
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
			//	txtArea.setStyleName("Boldstyle"); 
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
						SearchHoldMonitorScreenTableDTO mainDto = (SearchHoldMonitorScreenTableDTO)txtFld.getData();
						if(null != mainDto){
							mainDto.setHoldRemarks(txtFld.getValue());
						}
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
