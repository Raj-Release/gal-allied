package com.shaic.newcode.wizard.pages;

import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.tokenfield.TokenField;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings({"serial", "unchecked", "rawtypes" })
public class ReasonForAdmissionUI extends ViewComponent {

	private TokenField tokenField;
	
	@PostConstruct
    public void init() {
		VerticalLayout mainLayout = new VerticalLayout();
        Panel panel = new Panel();
        mainLayout.addComponent(panel);
        panel = new Panel();
        panel.setWidth("200px");
        panel.setHeight("150px");
        panel.setStyleName("gray");
        mainLayout.addComponent(panel);

        // generate container
        Container tokens = generateTestContainer();
        this.tokenField = new TokenField(){
        	@Override
        	protected void onTokenClick(final Object tokenId) 
        	{
        		if (tokenId instanceof SelectValue)
        			UI.getCurrent().addWindow(new RemoveWindow((SelectValue) tokenId, this));
        	}	
        	
        	@Override
            protected void onTokenInput(Object tokenId) {
        		if (tokenId instanceof String)
        		{
//        			SelectValue addNewDiseases = addNewDiseases((String) tokenId);
//        			super.onTokenInput(addNewDiseases);
        			fireViewEvent(IntimationDetailsPresenter.ADD_REASON_FOR_ADMISSION, tokenId);
        		}
        		else
        		{
        			SelectValue tokens = ((SelectValue) tokenId);
        			super.onTokenInput(tokens);
        		}
            }
        };
        tokenField.setContainerDataSource(tokens);
        tokenField.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
        panel.setContent(tokenField);
        VerticalLayout lo = new VerticalLayout();
        lo.setSpacing(true);
        setCompositionRoot(mainLayout);
    }
	protected SelectValue addNewDiseases(String tokenId) {
		Long id = 100l;
		SelectValue selectValue = new SelectValue(id, tokenId);
		//TODO: Code to insert new diseases
		System.out.println("Code to insert new diseases");
		return selectValue;
	}
	
	
    private static final String[] diseases = new String[] { "Argyria",
    	"Arthritis",
    	"Aseptic meningitis",
    	"Asthenia",
    	"Asthma",
    	"Astigmatism",
    	"Atherosclerosis",
    	"Athetosis",
    	"Atrophy",
    	"Calculi",
    	"Campylobacter infection",
    	"Cancer",
    	"Candidiasis",
    	"Carbon monoxide poisoning",
    	"Celiacs disease",
    	"Cerebral palsy",
    	"Chagas disease",
    	"Chalazion",
    	"Chancroid",
    	"Dengue",
    	"Diabetes mellitus",
    	"Diphtheria",
    	"Dehydration",
    	"Ear infection",
    	"Ebola",
    	"Encephalitis",
    	"Emphysema",
    	"Epilepsy",
    	"Erectile dysfunctions" };
    private Container generateTestContainer() {
        BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);

        HashSet<String> log = new HashSet<String>();
        for (int i = 0; i < diseases.length;) {
            String name = diseases[i];
            
            if (!log.contains(name)) {
                log.add(name);
                container.addBean(new SelectValue(Long.valueOf("" + i), name));
                i++;
            }
        }
        return container;
    }
    
    public void addToken(SelectValue value)
    {
    	this.tokenField.addToken(value);
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
}