package com.shaic.arch;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.tokenfield.TokenField;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class GTokenField extends TokenField{
	
	public GTokenField(String caption)
	{
		super(caption);
	}
	
	public void setContainer(BeanItemContainer<SelectValue> tokens)
	{
		this.setContainerDataSource(tokens);
        this.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);

	}
	private static final long serialVersionUID = 4522985678928037706L;

	private GTokenFieldListener gTokenListener;
	
	public GTokenFieldListener getgTokenListener() {
		return gTokenListener;
	}
	
	public String getValueStr()
	{
		Set<SelectValue> value2 = (Set<SelectValue>) this.getValue();
		String value = StringUtils.EMPTY;
		StringBuffer val = new StringBuffer();
		for (SelectValue selectValue : value2) {
			val.append(selectValue).append(","); 
		}
		
		value = StringUtils.removeEndIgnoreCase(val.toString(), ",");
		
		return value;
	}

	public void addGTokenListener(GTokenFieldListener tokenListener) {
		this.gTokenListener = tokenListener;
	}

	@Override
	protected void onTokenClick(final Object tokenId) 
	{
		if (tokenId instanceof SelectValue)
			UI.getCurrent().addWindow(new RemoveWindow((SelectValue) tokenId, this));
		
		if (gTokenListener != null)
		{
			
			System.out.println(getValueStr());
			gTokenListener.tokenClicked(tokenId);
		}
	}	
	
	@Override
    protected void onTokenInput(Object tokenId) {
		if (tokenId instanceof String)
		{
			if (gTokenListener != null)
			{
				gTokenListener.newValueAdded(tokenId);
			}
		}
		else
		{
			SelectValue tokens = ((SelectValue) tokenId);
			super.onTokenInput(tokens);
		}
    }
	
}
/**
 * This is the window used to confirm removal
 */
class RemoveWindow extends Window {

    private static final long serialVersionUID = -7140907025722511460L;
    
    private final Window self;

    RemoveWindow(final SelectValue c, final TokenField f) {
        super("Remove " + c.getValue() + "?");
        self = this;
        setStyleName("gray");
        setResizable(false);
        center();
        setModal(true);
        setWidth("250px");
        setClosable(false);

        // layout buttons horizontally
        HorizontalLayout hz = new HorizontalLayout();
        this.setContent(hz);
        hz.setSpacing(true);
        hz.setWidth("100%");

        Button cancel = new Button("Cancel", new Button.ClickListener() {

            private static final long serialVersionUID = 7675170261217815011L;

            public void buttonClick(ClickEvent event) {
            	 UI.getCurrent().removeWindow(self);
            }
        });
        hz.addComponent(cancel);
        hz.setComponentAlignment(cancel, Alignment.MIDDLE_LEFT);

        Button remove = new Button("Remove", new Button.ClickListener() {

            private static final long serialVersionUID = 5004855711589989635L;

            public void buttonClick(ClickEvent event) {
                f.removeToken(c);
                UI.getCurrent().removeWindow(self);
            }
        });
        hz.addComponent(remove);
        hz.setComponentAlignment(remove, Alignment.MIDDLE_RIGHT);
    }
}
