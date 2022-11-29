package com.shaic.claim.coordinator.view;

import java.util.HashMap;

import javax.ejb.EJB;
import javax.inject.Inject;






import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.fileUpload.MultipleUploadDocumentPageUI;
import com.shaic.domain.preauth.Coordinator;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.Page;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewCoOrdinatorReplyTable extends GBaseTable<ViewCoOrdinatorDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Object[] COLUMN_HEADER = new Object[] {"serialNumber",
			"requestedDate", "repliedDate", "requestType","requestorRole", "requestroNameId",
			"requestorRemarks", "viewFile","coOrdinatorRepliedId","coOrdinatorRemarks" };
	
	@EJB
	private ViewCoOrdinatorReplyService coordinatorService;
	
	@Inject
	private MultipleUploadDocumentPageUI multipleDocumentPageUI;
	
	@Inject
	private UploadedFileViewUI fileViewUI;

	@Override
	public void removeRow() {

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewCoOrdinatorDTO>(
				ViewCoOrdinatorDTO.class));
		table.setVisibleColumns(COLUMN_HEADER);
		table.setWidth("100%");
		table.setHeight("125px");
		table.removeGeneratedColumn("viewFile");
		table.addGeneratedColumn("viewFile",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						Button button = new Button("View File");
						ViewCoOrdinatorDTO coordinatorDTO = (ViewCoOrdinatorDTO) itemId;
						final Long key = coordinatorDTO.getKey();
						final Coordinator coordinator = setFileName(key, button);
						button.addClickListener(new Button.ClickListener()
						{
							@Override
							public void buttonClick(ClickEvent event) {
//								if (null != coordinator.getFileName()){
								Window popup = new com.vaadin.ui.Window();
								popup.setWidth("75%");
								popup.setHeight("50%");
								
//								fileViewUI.init(popup,coordinator.getFileName(), coordinator.getFileToken());
//								fileViewUI.setHeight("70%");
								
								multipleDocumentPageUI.init(SHAConstants.REFER_TO_COORDINATOR, coordinator.getKey(), true);
								Page page = getUI().getPage();
								multipleDocumentPageUI.setCurrentPage(page);
								
								
								popup.setContent(multipleDocumentPageUI);
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
								
//								}else{
//									getErrorMessage("File not Available");
//								}
								
					
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
	}
	
	public Coordinator setFileName(Long key,Button button){
		
		Coordinator coordinator = coordinatorService.getCoordinator(key);
		if(coordinator != null){
//		button.setCaption(coordinator.getFileName());
		return coordinator;
		}
		else{
			return null;
		}
		
	}
	
	public void viewFiles(ViewCoOrdinatorDTO coordinatorDTO){
		
//		if(coordinatorDTO.getFileName().endsWith(".JPG"))
//		{
//			String imageUrl = SHAFileUtils.viewFileByToken(this.bean.getDmsDocToken());
//		    imageViewer.setSource(new ExternalResource(imageUrl));
//		    imageViewer.setVisible(true);  
//		    imageViewer.setHeight("500px");
//		    Panel imagePanel = new Panel();
//		    imagePanel.setContent(imageViewer);
//		    hsplitPanel.setFirstComponent(imagePanel);
//			
//		}
//		else if(coordinatorDTO.getFileName().endsWith(".PDF"))
//		{
//			final String imageUrl = SHAFileUtils.viewFileByToken(this.bean.getDmsDocToken());
//			Button saveExcel = new Button();
//			Resource res = new FileResource(new File(imageUrl));
//			FileDownloader fd = new FileDownloader(res);
//			fd.extend(saveExcel);
//			final String url = System.getProperty("jboss.server.data.dir") + "/"
//					+ bean.getFileName();
//	        Embedded e = new Embedded();
//	        e.setSizeFull();
//	        e.setType(Embedded.TYPE_BROWSER);
//			StreamResource.StreamSource s = new StreamResource.StreamSource() {
//
//				public FileInputStream getStream() {
//					try {
//						File f = new File(url);
//						FileInputStream fis = new FileInputStream(f);
//						return fis;
//					} catch (Exception e) {
//						e.printStackTrace();
//						return null;
//					}
//				}
//			};
//			StreamResource r = new StreamResource(s, bean.getFileName());
//	        r.setMIMEType("application/pdf");
//	        e.setSource(r);
//			hsplitPanel.setFirstComponent(e);
//		}
	}

	@Override
	public void tableSelectHandler(ViewCoOrdinatorDTO t) {

	}

	@Override
	public String textBundlePrefixString() {
		return "coordinator-reply-";
	}
	
	  public void getErrorMessage(String eMsg) {

			/*Label label = new Label(eMsg, ContentMode.HTML);
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
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
		}

}
