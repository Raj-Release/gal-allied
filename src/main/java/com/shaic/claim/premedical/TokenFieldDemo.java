package com.shaic.claim.premedical;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.tokenfield.TokenField;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;


@SuppressWarnings({"serial", "unchecked", "rawtypes" })
public class TokenFieldDemo extends ViewComponent {

	@PostConstruct
    public void init() {

        /*
         * This is the most basic use case using all defaults; it's empty to
         * begin with, the user can enter new tokens.
         */
    	VerticalLayout mainLayout = new VerticalLayout();
        Panel p = new Panel("Basic");
        mainLayout.addComponent(p);

        TokenField f = new TokenField("Add tags");
        p.setContent(f);
        
        final TokenFieldDemo thisForm = this;
        
        /*
         * Interpretes "," as token separator
         */
        p = new Panel("Comma separated");
        mainLayout.addComponent(p);

        f = new TokenField() {

            @Override
            protected void onTokenInput(Object tokenId) {
                String[] tokens = ((String) tokenId).split(",");
                for (int i = 0; i < tokens.length; i++) {
                    String token = tokens[i].trim();
                    if (token.length() > 0) {
                        super.onTokenInput(token);
                    }
                }
            }

        };
        f.setInputPrompt("tag, another, yetanother");
        p.setContent(f);

        /*
         * In this example, most features are exercised. A container with
         * generated contacts is used. The input has filtering (a.k.a
         * suggestions) enabled, and the added token button is configured so
         * that it is in the standard "Name <email>" -format. New contacts can
         * be added to the container ('address book'), or added as-is (in which
         * case it's styled differently).
         */

        p = new Panel("Full featured example");
        p.setStyleName("black");
        mainLayout.addComponent(p);

        // generate container
        Container tokens = generateTestContainer();

        // we want this to be vertical
        VerticalLayout lo = new VerticalLayout();
        lo.setSpacing(true);

        f = new TokenField(lo) {

            private static final long serialVersionUID = 5530375996928514871L;

            // dialog if not in 'address book', otherwise just add
            protected void onTokenInput(Object tokenId) {
                Set<Object> set = (Set<Object>) getValue();
                Contact c = new Contact("", tokenId.toString());
                if (set != null && set.contains(c)) {
                    // duplicate
                    Notification.show(getTokenCaption(tokenId) + " is already added");
                } else {
                    if (!cb.containsId(c)) {
                        // don't add directly,
                        // show custom "add to address book" dialog
                    	Window editContactWindow = new EditContactWindow(tokenId.toString(), this);
                    	UI.getCurrent().addWindow(editContactWindow);
                    } else {
                        // it's in the 'address book', just add
                        addToken(tokenId);
                    }
                }
            }

            // show confirm dialog
            protected void onTokenClick(final Object tokenId) {
            	UI.getCurrent().addWindow(new RemoveWindow((Contact) tokenId, this));
            }

            // just delete, no confirm
            protected void onTokenDelete(Object tokenId) {
                this.removeToken(tokenId);
            }

            // custom caption + style if not in 'address book'
            protected void configureTokenButton(Object tokenId, Button button) {
                super.configureTokenButton(tokenId, button);
                // custom caption
                button.setCaption(getTokenCaption(tokenId) + " <" + tokenId
                        + ">");
                // width
                button.setWidth("100%");

                if (!cb.containsId(tokenId)) {
                    // it's not in the address book; style
                    button.addStyleName(TokenField.STYLE_BUTTON_EMPHAZISED);
                }
            }
        };
        p.setContent(f);
        // This would turn on the "fake tekstfield" look:
        f.setStyleName(TokenField.STYLE_TOKENFIELD);
        f.setWidth("100%");
        f.setInputWidth("100%");
        f.setContainerDataSource(tokens); // 'address book'
        f.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS); // suggest
        f.setTokenCaptionPropertyId("name"); // use name in input
        f.setInputPrompt("Enter contact name or new email address");
        f.setRememberNewTokens(false); // we'll do this via the dialog
        // Pre-add a few:
        Iterator it = f.getTokenIds().iterator();
        f.addToken(it.next());
        f.addToken(it.next());
        f.addToken(new Contact("", "thatnewguy@example.com"));
        
        setCompositionRoot(mainLayout);

    }
    /* Used to generate example contents */
    private static final String[] firstnames = new String[] { "John", "Mary",
            "Joe", "Sarah", "Jeff", "Jane", "Peter", "Marc", "Robert", "Paula",
            "Lenny", "Kenny", "Nathan", "Nicole", "Laura", "Jos", "Josie",
            "Linus" };
    private static final String[] lastnames = new String[] { "Torvalds",
            "Smith", "Adams", "Black", "Wilson", "Richards", "Thompson",
            "McGoff", "Halas", "Jones", "Beck", "Sheridan", "Picard", "Hill",
            "Fielding", "Einstein" };

    private Container generateTestContainer() {
        BeanItemContainer<Contact> container = new BeanItemContainer<Contact>(
                Contact.class);

        HashSet<String> log = new HashSet<String>();
        Random r = new Random(5);
        for (int i = 0; i < 20;) {
            String fn = firstnames[(int) (r.nextDouble() * firstnames.length)];
            String ln = lastnames[(int) (r.nextDouble() * lastnames.length)];
            String name = fn + " " + ln;
            String email = fn.toLowerCase() + "." + ln.toLowerCase()
                    + "@example.com";

            if (!log.contains(email)) {
                log.add(email);
                container.addBean(new Contact(name, email));
                i++;
            }

        }
        return container;
    }

    /**
     * Example Contact -bean, mostly generated setters/getters.
     */
    public class Contact {
        private String name;
        private String email;

        public Contact(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String toString() {
            return email;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Contact) {
                return email.equals(((Contact) obj).getEmail());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return email.hashCode();
        }

    }

    /**
     * This is the window used to confirm removal
     */
    class RemoveWindow extends Window {

        private static final long serialVersionUID = -7140907025722511460L;
        
        private final Window self;

        RemoveWindow(final Contact c, final TokenField f) {
            super("Remove " + c.getName() + "?");
            self = this;
            setStyleName("black");
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

    /**
     * This is the window used to add new contacts to the 'address book'. It
     * does not do proper validation - you can add weird stuff.
     */
    class EditContactWindow extends Window {
    	
    	private final Window self;
    	
    	private Contact contact;
        
        EditContactWindow(final String t, final TokenField f) {
        	super("New Contact");
        	self = this;
            if (t.contains("@")) {
                contact = new Contact("", t);
            } else {
                contact = new Contact(t, "");
            }
            setModal(true);
            center();
            setWidth("250px");
            setStyleName("black");
            setResizable(false);

            // Just bind a Form to the Contact -pojo via BeanItem
            FieldGroup form = new FieldGroup();
            form.setItemDataSource(new BeanItem<Contact>(contact));

            // layout buttons horizontally
            HorizontalLayout hz = new HorizontalLayout();
            this.setContent(hz);
            hz.setSpacing(true);
            hz.setWidth("100%");

            Button dont = new Button("Don't add", new Button.ClickListener() {

                private static final long serialVersionUID = -1198191849568844582L;

                public void buttonClick(ClickEvent event) {
                    if (contact.getEmail() == null
                            || contact.getEmail().length() < 1) {
                        contact.setEmail(contact.getName());
                    }
                    f.addToken(contact);
                    UI.getCurrent().removeWindow(self);
                }
            });
            hz.addComponent(dont);
            hz.setComponentAlignment(dont, Alignment.MIDDLE_LEFT);

            Button add = new Button("Add to contacts",
                    new Button.ClickListener() {

                        private static final long serialVersionUID = 1L;

                        public void buttonClick(ClickEvent event) {
                            if (contact.getEmail() == null
                                    || contact.getEmail().length() < 1) {
                                contact.setEmail(contact.getName());
                            }
                            ((BeanItemContainer) f.getContainerDataSource())
                                    .addBean(contact);
                            f.addToken(contact);
                            UI.getCurrent().removeWindow(self);
                        }
                    });
            hz.addComponent(add);
            hz.setComponentAlignment(add, Alignment.MIDDLE_RIGHT);

        }
    }

}