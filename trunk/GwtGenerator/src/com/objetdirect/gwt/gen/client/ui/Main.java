package com.objetdirect.gwt.gen.client.ui;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.objetdirect.gwt.gen.client.GwtGenerator;
import com.objetdirect.gwt.gen.client.ui.design.Design;
import com.objetdirect.gwt.gen.client.ui.explorer.ExplorerPanel;
import com.objetdirect.gwt.gen.client.ui.popup.LoadingPopUp;
import com.objetdirect.gwt.gen.client.ui.welcome.WelcomePanel;

public class Main  {

	private SimplePanel mainContainer;
	
	private WelcomePanel welcomePanel;
	private ExplorerPanel explorerPanel; 
	
	public Main() {
		mainContainer = new SimplePanel();
//		initWidget(mainContainer);
		
		goToHomeScreen();
	}

	public void createClassDiagram() {
		Log.debug("creatClassDiagram");
		mainContainer.clear();
		
		LoadingPopUp loadingPopUp = new LoadingPopUp();
		loadingPopUp.startProcessing("The designer is loading, please wait...");
		Design d = new Design(this);
		// DesignPanel can not be attached in the container and should be insert directly to root of the document
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(d); 
		loadingPopUp.stopProcessing();
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
