/**
 * 
 */
package com.shaic.arch.components;

import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ActionManager;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;


/**
 * @author ntv.vijayar
 *
 */
public class PopUpTextField extends TextField  implements Handler{

	private BillEntryDetailsDTO bean;
	
	private VerticalLayout vLayout = new VerticalLayout();
	
	protected ActionManager actionManager = new ActionManager();
	
	public PopUpTextField()
	{
		
	}
	
	public PopUpTextField(BillEntryDetailsDTO bean) {
		this.bean = bean;
	}
	
	public void initHandler()
	{
		actionManager.addActionHandler(this);
	}
	
	@Override
	public Action[] getActions(Object target, Object sender) {
		 return new Action[] {new ShortcutAction("remarks", ShortcutAction.KeyCode.F8, null)};
	}

	@Override
	public void handleAction(Action action, Object sender, Object target) {
		// TODO Auto-generated method stub
		
		TextArea txtArea = new TextArea();
		txtArea.setNullRepresentation("");
		txtArea.setMaxLength(200);
		
		txtArea.setValue(bean.getDeductibleOrNonPayableReason());
		//txtArea.setValue(txtFld.getValue());
		txtArea.addValueChangeListener(new ValueChangeListener() {
			
			/*@Override
			public void valueChange(ValueChangeEvent event) {
				
				// TODO Auto-generated method stub
				
			}*/

			@Override
			public void valueChange(
					com.vaadin.v7.data.Property.ValueChangeEvent event) {
				TextArea txt = (TextArea)event.getProperty();
				setValue(txt.getValue());
				setDescription(txt.getValue());
				//txtFld.setValue(txt.getValue());
				//txtFld.setDescription(txt.getValue());
				
			}
		});
		
		bean.setDeductibleOrNonPayableReason(txtArea.getValue());
	//	txtFld.setDescription(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
		//txtFld.setDescription(txtArea.getValue());
		Button okBtn = new Button("OK");
		okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		vLayout.addComponent(txtArea);
		vLayout.addComponent(okBtn);
		vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
		
		
		
		final Window dialog = new Window();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(vLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		//dialog.show(getUI().getCurrent(), null, true);
	
		getUI().getCurrent().addWindow(dialog);
		
		okBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});	
	}
	
	

}
