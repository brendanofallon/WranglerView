package wranglerView.client;

import wranglerView.shared.AuthToken;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginPanel {

	private VerticalPanel mainPanel;
	private TextBox usernameField;
	private PasswordTextBox passwordField;
	private WranglerView mainView;
	
	public LoginPanel(WranglerView mainView) {
		this.mainView = mainView;
		initComponents();
	}
	
	private void initComponents() {
		mainPanel= new VerticalPanel();
		mainPanel.add(new HTML("<h2>Please log in</h2>"));
		
		HorizontalPanel usernamePanel = new HorizontalPanel();
		usernameField = new TextBox();
		usernameField.setHeight("14px");
		HTML usernameText = new HTML("<b>User name:<b>"); 
		usernameText.addStyleName("vertaligncenter");
		usernameText.setWidth("100px");
		usernamePanel.add(usernameText);
		usernamePanel.add(usernameField);
		mainPanel.add(usernamePanel);
		
		passwordField = new PasswordTextBox();
		passwordField.setHeight("14px");
		HorizontalPanel passwdPanel = new HorizontalPanel();
		HTML passwdText = new HTML("<b>Password:</b>");
		passwdText.addStyleName("vertaligncenter");
		passwdText.setWidth("100px");
		passwdPanel.add(passwdText);
		passwdPanel.add(passwordField);
		
		mainPanel.add(passwdPanel);
		
		Button goButton = new Button("Log in");
		goButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				tryLogin(usernameField.getText(), passwordField.getText());
			}
		});
		goButton.setFocus(true);
		
		mainPanel.add(goButton);
	}

	protected void tryLogin(final String username, final String password) {
		
		authService.authenticate(username, password, new AsyncCallback<AuthToken>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Server failure when authenticating user: " + username);	
			}

			@Override
			public void onSuccess(AuthToken token) {
				if (token != null) {
					mainView.showJobSubmissionPanel(token);
				}
				else {
					mainPanel.add(accessDeniedLabel);
				}
			}
			
		});
	}

	public Panel getWidget() {
		return mainPanel;
	}
	
	
	private HTML accessDeniedLabel = new HTML("<b>Incorrect username / password, please try again</b>");
	private final AuthServiceAsync authService = GWT.create(AuthService.class);
}
