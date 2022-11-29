package com.shaic.claim.omp.createintimation;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.imageio.ImageIO;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.OMPMenuPresenter;
import com.shaic.starfax.simulation.PremiaPullService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ValoTheme;


public class OMPCreateIntimationDetailTable extends GBaseTable<OMPCreateIntimationTableDTO>{

	private static final long serialVersionUID = -3502494454120278002L;
	
	private final static Object[] NATURAL_HDCOL_CREATE_ORDER = new Object[]{"add","serialNumber","policyNo","proposername",
		"insuredName","productCodeOrName","sumInsured","plan","passportNo","policyCoverPeriodFromDate","policyCoverPeriodToDate"
		}; 

	@EJB
	private PolicyService policyService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PremiaPullService premiaPullService;
	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}
	@SuppressWarnings("serial")
	@Override
	public void initTable() {
			table.setContainerDataSource(new BeanItemContainer<OMPCreateIntimationTableDTO>(OMPCreateIntimationTableDTO.class));
			
			table.removeGeneratedColumn("add");
			table.addGeneratedColumn("add",
			new Table.ColumnGenerator() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public Object generateCell(final Table source, final Object itemId, Object columnId) {
						final Button addIntimation = new Button();{
								addIntimation.setCaption("Add Intimation");
								addIntimation.addClickListener(new Button.ClickListener() {
								Map<String, Object> intimationDetailsParams = new HashMap<String, Object>();
										public void buttonClick(ClickEvent event) {
											OMPCreateIntimationTableDTO intimationDtoObj  = (OMPCreateIntimationTableDTO) itemId;
											OMPCreateIntimationTableDTO intimationDto = new OMPCreateIntimationTableDTO();
											intimationDtoObj.setIntimationKey(null);
											String policyNumber = intimationDtoObj.getPolicyNo();
											Policy policyRecord = checkPolicyNumber(policyNumber);
											if(policyRecord == null){
												
												PremPolicyDetails policyDetails = premiaPullService.fetchOMPPolicyDetailsFromPremia(policyNumber);
												policyRecord = premiaPullService.populatePolicyToAddOMPIntimation(policyDetails);
											}
											intimationDtoObj.setPolicy(policyRecord);
											policyRecord.setInsured(premiaPullService.getInsuredListByPolicyNo(policyRecord.getPolicyNumber()));
											intimationDto.setPolicy(policyRecord);
											fireViewEvent(MenuItemBean.NEW_OMP_INTIMATION, intimationDto);							
							         }
								});
							}
							addIntimation.addStyleName(BaseTheme.BUTTON_LINK);
							return addIntimation;
						}
					});
			    table.setColumnHeader("add", "Add Intimation");
				table.setSizeFull();
		}	
	
	public Policy checkPolicyNumber(String policyNumber){
		
		return premiaPullService.getPolicyByPolicyNubember(policyNumber);
		 
	}
	
	 public static BufferedImage  byteArrayToImage(byte[] bytes){  
	        BufferedImage bufferedImage=null;
	        try {
	            InputStream inputStream = new ByteArrayInputStream(bytes);
	            bufferedImage = ImageIO.read(inputStream);
	        } catch (IOException ex) {
	            System.out.println(ex.getMessage());
	        }
	        return bufferedImage;
	 }
	
	
	 public void alertMessage(final OMPCreateIntimationTableDTO t, String message) {

	   		Label successLabel = new Label(
					"<b style = 'color: red;'>"+ message + "</b>",
					ContentMode.HTML);

			// Label noteLabel = new
			// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
			// ContentMode.HTML);

			Button homeButton = new Button("ok");
			homeButton.setData(t);
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");

			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
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
					 fireViewEvent(OMPMenuPresenter.OMP_SEARCHINTIMATION_CREATE, t);
				}
			});
		}


	 
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}		
	}
/*
	public void setDraftTableHeader(){
		table.setVisibleColumns(COLUM_HEADER_DRAFT_INTIMATION);
		table.removeGeneratedColumn("Action");
		table.removeGeneratedColumn("viewDocument");
		table.removeGeneratedColumn("viewTrails");
	}
*/
	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_CREATE_ORDER);
		table.setColumnHeader("add", "Add Intimation");
	}
	
	
	
@Override
public void tableSelectHandler(OMPCreateIntimationTableDTO t) {
	// TODO Auto-generated method stub
	
}
@Override
public String textBundlePrefixString() {
	return "ompcreateIntimation-";
}
}
