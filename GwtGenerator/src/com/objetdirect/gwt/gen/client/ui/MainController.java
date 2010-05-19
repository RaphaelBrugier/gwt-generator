/*
 * This file is part of the Gwt-Generator project and was written by Raphaël Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2010 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */
package com.objetdirect.gwt.gen.client.ui;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.objetdirect.gwt.gen.client.GwtGenerator;
import com.objetdirect.gwt.gen.client.event.BackToHomeEvent;
import com.objetdirect.gwt.gen.client.event.BackToHomeEvent.BackToHomeEventHandler;
import com.objetdirect.gwt.gen.client.ui.popup.ErrorToaster;
import com.objetdirect.gwt.gen.client.ui.popup.MessageToaster;
import com.objetdirect.gwt.gen.client.ui.welcome.WelcomePanel;

/**
 * Main controller.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class MainController  {

	
	private WelcomePanel welcomePanel;
	private AppPresenter appPresenter;
	
	private HandlerManager eventBus;
	
	public MainController() {
		eventBus = new HandlerManager(null);
		bind();
		doGoToHomeScreen();
	}

	/** Add handlers to the event bus. */
	private void bind() {
		eventBus.addHandler(BackToHomeEvent.TYPE, new BackToHomeEventHandler() {
			@Override
			public void onBackToHomeEvent(BackToHomeEvent event) {
				doGoToHomeScreen();
			}
		});
		
	}

	/** Request to return to the home screen.
	 *  Home screens means welcome screen if the user is not logged or 
	 *  explorer screen if the user is already logged
	 */
	private void doGoToHomeScreen() {
		RootLayoutPanel.get().clear();
		MessageToaster.intantiateAndAttach();
		ErrorToaster.intantiateAndAttach();
		
		if (GwtGenerator.loginInfo.isLoggedIn()) {
			if(appPresenter == null){
				appPresenter = new AppPresenter(eventBus, new AppView(), GwtGenerator.loginInfo);
			}
			appPresenter.go(RootLayoutPanel.get());
		} else {
			if (welcomePanel == null) {
				welcomePanel = new WelcomePanel(eventBus, GwtGenerator.loginInfo.getLoginUrl());
			}
			welcomePanel.go(RootLayoutPanel.get());
		}
		
	}
}
