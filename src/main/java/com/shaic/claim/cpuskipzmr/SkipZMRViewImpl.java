package com.shaic.claim.cpuskipzmr;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
@Alternative
public class SkipZMRViewImpl extends AbstractMVPView implements SkipZMRView, GMVPView{

	private static final long serialVersionUID = 1180651477021369571L;


	@Inject
	private Instance<SkipZMRUI> skipZMRUIInstance;	 
	
	private SkipZMRUI skipZMRUI;
	
	@PostConstruct
	public void init() {
		addStyleName("view");
	        
	}
	
	@Override
	public void resetView() {
		if(skipZMRUI != null) {
			skipZMRUI.init();
		}
		
	}

	@Override
	public void initView(BeanItemContainer<SelectValue> cpuCodeContainer) {
		 setSizeFull();
		 skipZMRUI = skipZMRUIInstance.get();
		 skipZMRUI.initView(cpuCodeContainer);         
	     setCompositionRoot(skipZMRUI);
	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Record Updated Successfully.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("OK");
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
				fireViewEvent(MenuItemBean.CPU_SKIP_ZMR, null);
			}
		});
		
	}

	@Override
	public void buildFailureLayout(String message) {
		Label successLabel = new Label("<b style = 'color: black;'>"+message+"</b>", ContentMode.HTML);			
		Button homeButton = new Button("Process Claim Request Home");
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
				fireViewEvent(MenuItemBean.CPU_SKIP_ZMR, null);
				
			}
		});
	}

	@Override
	public void generateTableForCpuCode(List<SkipZMRListenerTableDTO> masCpuCode) {
		if(skipZMRUI != null) {
			skipZMRUI.generateTableForCpuCode(masCpuCode);
		}
	}
	

}
