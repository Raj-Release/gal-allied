package com.shaic.claim;

import javax.ejb.EJB;

import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

public class ElearnContentTable extends GBaseTable<ELearnDto>{
	
	@EJB
	private MasterService masterService;

	
	private final static Object COLUM_HEADER[] = new Object[] {
		"sno","docName"
	};
	
	@Override
	public void removeRow() {
		
	}

	@Override
	public void initTable() {
	
		table.setContainerDataSource(new BeanItemContainer<ELearnDto>(ELearnDto.class));
		table.setHeight("330px");
		table.setVisibleColumns(COLUM_HEADER);
		table.removeGeneratedColumn("viewDocument");
		table.addGeneratedColumn("viewDocument",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						final Button viewDocDetailsButton = new Button("View Document Details");
						final Long docToken = ((ELearnDto) itemId)
								.getDocToken();	
						viewDocDetailsButton.setData(docToken);
						
						viewDocDetailsButton.addClickListener(new Button.ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								
								Long edocToken = (Long) viewDocDetailsButton.getData(); 
								
								if(edocToken != null)
								{								
									String docView = SHAFileUtils.viewFileByToken(edocToken.toString());
									getUI().getPage().open(docView, "_blank",1200,780,BorderStyle.NONE);
								}										
							}
						});
						viewDocDetailsButton
								.addStyleName(BaseTheme.BUTTON_LINK);
						return viewDocDetailsButton;
					}
				});
	}

	@Override
	public void tableSelectHandler(ELearnDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "viewELearnDoc-";
		
	}
}
