package com.objetdirect.gwt.gen.client.ui;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.objetdirect.gwt.gen.client.GwtGenerator;
import com.objetdirect.gwt.gen.client.event.BackToHomeEvent;
import com.objetdirect.gwt.gen.client.event.CreateDiagramEvent;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.ui.design.Design;
import com.objetdirect.gwt.gen.client.ui.explorer.ExplorerPanel;
import com.objetdirect.gwt.gen.client.ui.popup.LoadingPopUp;
import com.objetdirect.gwt.gen.client.ui.welcome.WelcomePanel;
import com.objetdirect.gwt.gen.shared.dto.DiagramInformations;
import com.objetdirect.gwt.gen.client.event.CreateDiagramEvent.CreateDiagramEventHandler;
import com.objetdirect.gwt.gen.client.event.BackToHomeEvent.BackToHomeEventHandler;

public class Main  {

	private SimplePanel mainContainer;
	
	private WelcomePanel welcomePanel;
	private ExplorerPanel explorerPanel;
	private Design designController;
	
	private HandlerManager eventBus;
	
	private final DiagramServiceAsync diagramService = GWT.create(DiagramService.class);
	
	public Main() {
		mainContainer = new SimplePanel();
		eventBus = new HandlerManager(null);
		designController = new Design(eventBus);
		bind();
		doGoToHomeScreen();
	}

	private void bind() {
		
//		eventBus.addHandler(CreateDiagramEvent.TYPE, new CreateDiagramEventHandler() {
//			@Override
//			public void onCreateDiagramEvent(CreateDiagramEvent event) {
//				doCreateDiagram(event.getDiagramInformations());
//			}
//		});
		
		eventBus.addHandler(BackToHomeEvent.TYPE, new BackToHomeEventHandler() {
			@Override
			public void onBackToHomeEventEvent(BackToHomeEvent event) {
				doGoToHomeScreen();
			}
		});
		
	}

//	private void doCreateDiagram(DiagramInformations diagramInformations) {
//		Log.debug("creatClassDiagram");
////		mainContainer.clear();
//		final LoadingPopUp loadingPopUp = new LoadingPopUp();
//		loadingPopUp.startProcessing("The designer is loading, please wait...");
//		
//		diagramService.createDiagram(diagramInformations.getType(), diagramInformations.getName(), new 
//			AsyncCallback<Long>() {
//				@Override
//				public void onSuccess(Long result) {
//					Log.debug("Creation with succes id = " + result);
//					
//					Design d = new Design(eventBus);
//					// DesignPanel can not be attached in the container and should be insert directly to root of the document
//					RootLayoutPanel.get().clear();
//					RootLayoutPanel.get().add(d); 
//					loadingPopUp.stopProcessing();
//				}
//				
//				@Override
//				public void onFailure(Throwable caught) {
//					Log.debug(caught.getMessage());
//				}
//			});
//	}
	
	/** Request to return to the home screen.
	 *  Home screens means welcome screen if the user is not logged or 
	 *  explorer screen if the user is already logged
	 */
	private void doGoToHomeScreen() {
		mainContainer.clear();
		if (GwtGenerator.loginInfo.isLoggedIn()) {
			if(explorerPanel == null){
				explorerPanel = new ExplorerPanel(eventBus);
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
