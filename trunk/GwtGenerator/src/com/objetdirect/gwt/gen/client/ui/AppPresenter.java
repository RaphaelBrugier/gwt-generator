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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.services.ProjectService;
import com.objetdirect.gwt.gen.client.services.ProjectServiceAsync;
import com.objetdirect.gwt.gen.client.ui.content.ContentPresenter;
import com.objetdirect.gwt.gen.client.ui.content.ContentView;
import com.objetdirect.gwt.gen.client.ui.diagramsList.DiagramsListPresenter;
import com.objetdirect.gwt.gen.client.ui.diagramsList.DiagramsListView;
import com.objetdirect.gwt.gen.shared.dto.LoginInfo;

/**
 * Presenter : Application.
 * Instantiate and give the position to each part of the application.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class AppPresenter {

	public interface Display {
		/**
		 * @return The view as a widget.
		 */
		Widget asWidget();
		
		/**
		 * @return The field where is added the name of the logged user.
		 */
		HasText getNameField();
		
		/**
		 * @return Get the link to sign out of the application.
		 */
		Anchor getSignOutAnchor();
		
		HasWidgets getDiagramsListContainer();
		
		HasWidgets getContentContainer();
	}
	
	private final HandlerManager eventBus;
	
	private final Display display;
	
	private final DiagramServiceAsync diagramService = GWT.create(DiagramService.class);
	private final ProjectServiceAsync projectService = GWT.create(ProjectService.class);
	
	
	private DiagramsListPresenter diagramsListPresenter;
	private ContentPresenter contentPresenter;
	
	
	public AppPresenter(HandlerManager eventBus, Display view, LoginInfo loginInfo) {
		this.eventBus = eventBus;
		this.display = view;
		
		doFillView(loginInfo);
		
		fillPanels();
	}
	
	
	/**
	 * Fill the view fields with the informations got from the logged user
	 * @param loginInfo
	 */
	private void doFillView(LoginInfo loginInfo) {
		display.getNameField().setText(loginInfo.getNickname());
		display.getSignOutAnchor().setHref(loginInfo.getLogoutUrl());
	}


	/**
	 * Fill the applications panels with the widgets.
	 */
	private void fillPanels() {
		diagramsListPresenter = new DiagramsListPresenter(eventBus, new DiagramsListView(), diagramService, projectService);
		diagramsListPresenter.go(display.getDiagramsListContainer());
		
		contentPresenter = new ContentPresenter(eventBus, new ContentView(), diagramService);
		contentPresenter.go(display.getContentContainer());
	}


	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}
}
