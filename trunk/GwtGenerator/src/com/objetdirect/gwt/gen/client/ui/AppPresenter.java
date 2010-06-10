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

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.event.ChangeDiagramButtonsStateEvent;
import com.objetdirect.gwt.gen.client.event.ChangeDiagramButtonsStateEvent.ChangeDiagramButtonsStateEventHandler;
import com.objetdirect.gwt.gen.client.event.GenerateEvent;
import com.objetdirect.gwt.gen.client.event.NewProjectEvent;
import com.objetdirect.gwt.gen.client.event.SaveDiagramEvent;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.services.GeneratorService;
import com.objetdirect.gwt.gen.client.services.GeneratorServiceAsync;
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
		
		HasWidgets getDiagramsListContainer();
		
		HasWidgets getContentContainer();
		
		HasClickHandlers getNewProjectButton();
		
		HasClickHandlers getSaveButton();
		
		HasClickHandlers getGenerateButton();
		
		/**
		 * Set whether the buttons dedicated to the diagram are enabled or disabled.
		 * This buttons should been in the enabled state only when a diagram is currently displayed in the drawer.
		 * @param state true to enabled the buttons
		 */
		void setDiagramButtonsEnabled(boolean state);
		
		/**
		 * Set the href for the sign out anchor;
		 * @param href
		 */
		void setSignOutHref(String href);
	}
	
	private final HandlerManager eventBus;
	
	private final Display display;
	
	private final DiagramServiceAsync diagramService = GWT.create(DiagramService.class);
	private final ProjectServiceAsync projectService = GWT.create(ProjectService.class);
	private final GeneratorServiceAsync generatorService = GWT.create(GeneratorService.class);
	
	private DiagramsListPresenter diagramsListPresenter;
	private ContentPresenter contentPresenter;
	
	/**
	 * Manage the state (true = enabled, false = disabled) of the buttons specific of the diagram displayed. 
	 */
	private boolean diagramButtonsState;
	
	public AppPresenter(HandlerManager eventBus, Display view, LoginInfo loginInfo) {
		this.eventBus = eventBus;
		this.display = view;
		
		display.setSignOutHref(loginInfo.getLogoutUrl());
		
		addClickHandlers();
		bindEventBusHandlers();
		
		fillPanels();
		diagramButtonsState = false;
	}

	private void bindEventBusHandlers() {
		eventBus.addHandler(ChangeDiagramButtonsStateEvent.TYPE, new ChangeDiagramButtonsStateEventHandler() {
			@Override
			public void onChangeDiagramButtonsStateEvent(ChangeDiagramButtonsStateEvent event) {
				Log.trace("AppPresenter::BindEventBusHandlers changeDiagramButtonsStateEvent received with state " + event.getState());
				diagramButtonsState = event.getState();
				display.setDiagramButtonsEnabled(diagramButtonsState);
			}
		});
	}

	private void addClickHandlers() {
		display.getNewProjectButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new NewProjectEvent());
			}
		});
		
		display.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (diagramButtonsState == true) // Only fire events when the buttons are activated.
					eventBus.fireEvent(new SaveDiagramEvent());
			}
		});
		
		display.getGenerateButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (diagramButtonsState == true) // Only fire events when the buttons are activated.
					eventBus.fireEvent(new GenerateEvent());
			}
		});
	}

	/**
	 * Fill the applications panels with the widgets.
	 */
	private void fillPanels() {
		diagramsListPresenter = new DiagramsListPresenter(eventBus, new DiagramsListView(), diagramService, projectService);
		diagramsListPresenter.go(display.getDiagramsListContainer());
		
		contentPresenter = new ContentPresenter(eventBus, new ContentView(), diagramService, generatorService);
		contentPresenter.go(display.getContentContainer());
	}

	public void go(HasWidgets container) {
		container.add(display.asWidget());
	}
}
