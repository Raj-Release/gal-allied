package com.shaic.claim.intimation.create;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.table.Page;
import com.shaic.claim.lumen.LumenQueryDTO;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class CallRecordingsUI extends ViewComponent{

	@Inject
	private CallRecordingDetailTable recodingTable;
	
	private VerticalLayout mainLayout;
	private Button closeButton;
	private Window popup;
	
	public Window getPopup() {
		return popup;
	}

	public void setPopup(Window popup) {
		this.popup = popup;
	}

	public void init(boolean isError, CallRecordingResponse argResponseObj) throws ParseException {
		mainLayout = new VerticalLayout();
		closeButton = new Button("Close");
		closeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		closeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				getPopup().close();
			}
		});
		if(!isError){
			List<CallRecordingTableDTO> listOfRows = null;
			recodingTable.init("", false, false);
			if(argResponseObj != null){
				if(argResponseObj.getTvcaudiodtl() != null && !argResponseObj.getTvcaudiodtl().isEmpty()){
					listOfRows = new ArrayList<>();
					CallRecordingTableDTO tableDto =  null;
					int i = 1;
					for(TVCAudioDetails row : argResponseObj.getTvcaudiodtl()){
						tableDto = new CallRecordingTableDTO();
						tableDto.setSno(i);
						if(!StringUtils.isBlank(row.getCalldate())){
							String dateStr = row.getCalldate().substring(0, row.getCalldate().length() -2);
							String actualDatepattern = "yyyy-MM-dd HH:mm:ss";//2017-08-07 16:02:54
							DateFormat srcDf = new SimpleDateFormat(actualDatepattern);
							Date date = srcDf.parse(dateStr);
							DateFormat destDf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
							String desDate = destDf.format(date);
							Date finalDate = destDf.parse(desDate);
							tableDto.setCallDateTime(desDate);
							tableDto.setCallDate(finalDate);
						}else{
							tableDto.setCallDateTime(null);
							tableDto.setCallDate(null);
						}
						tableDto.setAudioUrl(row.getAudio_url());
						listOfRows.add(tableDto);
						i++;
					}
					Collections.sort(listOfRows, new Comparator<CallRecordingTableDTO>() {
					    public int compare(CallRecordingTableDTO m1, CallRecordingTableDTO m2) {
					        return m1.getCallDate().compareTo(m2.getCallDate());
					    }
					});
					
					Page<CallRecordingTableDTO> page = new Page<CallRecordingTableDTO>();
					page.setPageItems(listOfRows);
					page.setTotalRecords(listOfRows.size());
					page.setTotalList(listOfRows);
					recodingTable.setTableList(page.getTotalList());
					
					recodingTable.setSubmitTableHeader();
					recodingTable.setWidth("65%");
					HorizontalLayout tblholder = new HorizontalLayout();
					tblholder.addComponent(recodingTable);
					tblholder.setComponentAlignment(recodingTable, Alignment.MIDDLE_CENTER);
					tblholder.setWidth("100%");
					
					mainLayout.addComponent(tblholder);
					mainLayout.addComponent(closeButton);
					
					mainLayout.setComponentAlignment(tblholder, Alignment.MIDDLE_CENTER);
					mainLayout.setComponentAlignment(closeButton, Alignment.MIDDLE_CENTER);
					
					mainLayout.setWidth("100%");
					mainLayout.setHeight(340, Unit.PIXELS);
				}
			}else{
				Label lbl = new Label("<b style = 'color: red;'>No Data Found!!! </b>", ContentMode.HTML);
				HorizontalLayout fl = new HorizontalLayout();
				fl.addComponent(lbl);
				fl.setMargin(true);
				fl.setSpacing(true);
				
				mainLayout.addComponent(fl);
				mainLayout.addComponent(closeButton);
				
				mainLayout.setComponentAlignment(fl, Alignment.MIDDLE_CENTER);
				mainLayout.setComponentAlignment(closeButton, Alignment.MIDDLE_CENTER);
				
				mainLayout.setWidth("100%");
				mainLayout.setHeight(320, Unit.PIXELS);
			}
			
		}else{
			Label lbl = new Label("<b style = 'color: red;'>No Data Found!!! </b>", ContentMode.HTML);
			HorizontalLayout fl = new HorizontalLayout();
			fl.addComponent(lbl);
			fl.setMargin(true);
			fl.setSpacing(true);
			
			mainLayout.addComponent(fl);
			mainLayout.addComponent(closeButton);
			
			mainLayout.setComponentAlignment(fl, Alignment.MIDDLE_CENTER);
			mainLayout.setComponentAlignment(closeButton, Alignment.MIDDLE_CENTER);
			
			mainLayout.setWidth("100%");
			mainLayout.setHeight(320, Unit.PIXELS);
		}
		setCompositionRoot(mainLayout);
		setSizeFull();
	}
}
