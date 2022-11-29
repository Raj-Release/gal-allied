package com.shaic.main.navigator.ui;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;

public class MagazineGallery  extends GBaseTable<MasMagazineDocument>{

	private final static Object COLUM_HEADER_MAGAZINE_GALLERY[] = new Object[] {"createdDate"};
	@EJB
	private MasterService masterService;
	
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<MasMagazineDocument>(MasMagazineDocument.class));
		table.setHeight("200px");
		table.setCaptionAsHtml(true);
		table.setCaption("<b style='font-size:18px'>                             Magazine Gallery </b>");
		//table.setSizeFull();
		table.setWidth("400px");
		table.setVisibleColumns(COLUM_HEADER_MAGAZINE_GALLERY);
		table.removeGeneratedColumn("action");
		table.addGeneratedColumn("action",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(final Table source,
						final Object itemId, Object columnId) {
					Button buttonLink = new Button("View");
					buttonLink.setData(itemId);
					buttonLink.addClickListener(new Button.ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
						   
							MasMagazineDocument claimMagazine = (MasMagazineDocument) itemId;
							String docViewURL = SHAFileUtils.viewFileByTokenService(String.valueOf(masterService.getMagazineDocumentKey(claimMagazine.getMagazineCode())));
							if(docViewURL!=null){
							getUI().getPage().open(docViewURL+"#toolbar=0", "_blank",1200,780,BorderStyle.NONE);
							}else{
								showErrorMessage("No Magazine Available");

							}
						}
					});
					buttonLink.addStyleName(BaseTheme.BUTTON_LINK);
					return buttonLink;
				}
				});
		
	
	}

	@Override
	public String textBundlePrefixString() {
		
		return "magazinegallery-";
	}

	@Override
	public void tableSelectHandler(MasMagazineDocument t) {
		// TODO Auto-generated method stub
		
	}
	private void showErrorMessage(String eMsg) {

		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

}
