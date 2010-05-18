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
package com.objetdirect.gwt.gen.client.ui.explorer;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.DrawerPanel;
import com.objetdirect.gwt.gen.client.GwtGenerator;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.ui.explorer.directoryList.DirectoryListPresenter;
import com.objetdirect.gwt.gen.client.ui.explorer.directoryList.DirectoryListView;
import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLDiagram;


/**
 * Panel to display the saved diagrams for a loged user and create new diagrams
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class ExplorerPanel extends LayoutPanel {

	private static ExplorerPanelUiBinder uiBinder = GWT
			.create(ExplorerPanelUiBinder.class);

	interface ExplorerPanelUiBinder extends UiBinder<Widget, ExplorerPanel> {
	}
	
	private final HandlerManager eventBus;
	
	private final DiagramServiceAsync diagramService = GWT.create(DiagramService.class);
	
	@UiField
	SpanElement nameSpan;
	
	@UiField
	Anchor signOut;
	
	@UiField
	FlowPanel westPanel;
	
	@UiField
	LayoutPanel content;
	
	private DirectoryListPresenter directoryListPresenter;
	
	public ExplorerPanel(HandlerManager eventBus) {
		if (!GwtGenerator.loginInfo.isLoggedIn()) {
			throw new GWTGeneratorException("A user should have been logged before construct the explorer panel");
		}
		
		this.add(uiBinder.createAndBindUi(this));
		this.eventBus = eventBus;

		nameSpan.setInnerText( GwtGenerator.loginInfo.getNickname());
		signOut.setHref(GwtGenerator.loginInfo.getLogoutUrl());
	}
	
	public void go(HasWidgets container) {
		container.clear();
		populateWestPanel();
		
		container.add(this);
		fetchContent();
	}
	
	private void fetchContent() {
		DrawerPanel drawer = new DrawerPanel(UMLDiagram.Type.getUMLDiagramFromIndex(0)); // Class diagram by default
		drawer.addWelcomeClass();

		content.add(drawer);
	}

	private void populateWestPanel() {
		directoryListPresenter = new DirectoryListPresenter(eventBus, new DirectoryListView(), diagramService);
		directoryListPresenter.go(westPanel);
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		
		Log.debug("ExplorerPanel::onLoad() content size = " + content.getOffsetHeight() + "  " + content.getOffsetWidth());
	}
	
	
}
