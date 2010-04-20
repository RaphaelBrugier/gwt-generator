package com.objetdirect.gwt.gen.client.ui;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.objetdirect.gwt.gen.client.GwtGenerator;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.ui.design.Design;
import com.objetdirect.gwt.gen.client.ui.explorer.ExplorerPanel;
import com.objetdirect.gwt.gen.client.ui.popup.LoadingPopUp;
import com.objetdirect.gwt.gen.client.ui.welcome.WelcomePanel;
import com.objetdirect.gwt.gen.shared.dto.DiagramInformations.Type;

public class Main  {

	private SimplePanel mainContainer;
	
	private WelcomePanel welcomePanel;
	private ExplorerPanel explorerPanel; 
	
	private final DiagramServiceAsync diagramService = GWT.create(DiagramService.class);
	
	public Main() {
		mainContainer = new SimplePanel();
		goToHomeScreen();
	}

	public void createDiagram(String name, String type) {
		Log.debug("creatClassDiagram");
		mainContainer.clear();
		
		final LoadingPopUp loadingPopUp = new LoadingPopUp();
		loadingPopUp.startProcessing("The designer is loading, please wait...");
		
		diagramService.createDiagram(Type.valueOf(type), name, new 
				AsyncCallback<Long>() {
					
					@Override
					public void onSuccess(Long result) {
						Log.debug("Creation with succes id = " + result);
						
						Design d = new Design(Main.this);
						// DesignPanel can not be attached in the container and should be insert directly to root of the document
						RootLayoutPanel.get().clear();
						RootLayoutPanel.get().add(d); 
						loadingPopUp.stopProcessing();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Log.debug(caught.getMessage());
					}
				});
	}
	
	/** Request to return to the home screen.
	 * Home screens means welcome screen if the user is not logged or 
	 * explorer screen if the user is already logged
	 */
	public void goToHomeScreen() {
		mainContainer.clear();
		if (GwtGenerator.loginInfo.isLoggedIn()) {
			if(explorerPanel == null){
				explorerPanel = new ExplorerPanel(this);
			}
			mainContainer.add(explorerPanel);
		} else {
			if (welcomePanel == null) {
				welcomePanel = new WelcomePanel(this, GwtGenerator.loginInfo.getLoginUrl());
			}
			mainContainer.add(welcomePanel);
		}
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(mainContainer);
	}
}
