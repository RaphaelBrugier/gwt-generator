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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.GwtGenerator;
import com.objetdirect.gwt.gen.client.event.CreateDiagramEvent;
import com.objetdirect.gwt.gen.client.ui.explorer.projectList.DirectoryListPresenter;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto.Type;
import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;


/**
 * Panel to display the saved diagrams for a loged user and create new diagrams
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class ExplorerPanel extends SimplePanel {

	private static ExplorerPanelUiBinder uiBinder = GWT
			.create(ExplorerPanelUiBinder.class);

	interface ExplorerPanelUiBinder extends UiBinder<Widget, ExplorerPanel> {
	}
	
	final private HandlerManager eventBus;
	
	@UiField
	SpanElement nameSpan;
	
	@UiField
	Anchor signOut;
	
	@UiField
	Button createDiagramButton;
	
	@UiField
	FlowPanel westPanel;
	
	@UiField
	FlowPanel content;
	
	DiagramList diagramList;
	
	public ExplorerPanel(HandlerManager eventBus) {
		if (!GwtGenerator.loginInfo.isLoggedIn()) {
			throw new GWTGeneratorException("A user should have been logged before construct the explorer panel");
		}
		
		this.add(uiBinder.createAndBindUi(this));
		this.eventBus = eventBus;

		nameSpan.setInnerText( GwtGenerator.loginInfo.getNickname());
		signOut.setHref(GwtGenerator.loginInfo.getLogoutUrl());
		diagramList = new DiagramList(eventBus);
	}
	
	public void go(HasWidgets container) {
		container.clear();
		populateWestPanel();
		fetchContent();
		
		container.add(this);
	}

	private void fetchContent() {
		diagramList.go(content);
	}

	private void populateWestPanel() {
		westPanel.add(new DirectoryListPresenter(eventBus));
	}


	@UiHandler(value="createDiagramButton")
	void onClickCreateClassDiagram(ClickEvent event) {
		
		// Construct a dialogBox to get the type and the name of the diagram.
		final DialogBox dialogBox = new DialogBox(false, true);
		dialogBox.setGlassEnabled(true);
		dialogBox.setText("Create a new diagram");
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("250px");
		
		HorizontalPanel namePanel = new HorizontalPanel();
		Label labelname = new Label("Name : ");
		namePanel.add(labelname);
		final TextBox nameTb = new TextBox();
		namePanel.add(nameTb);
		vpanel.add(namePanel);
		
		HorizontalPanel typePanel = new HorizontalPanel();
		typePanel.add(new Label("Type : "));
		final ListBox listbox = new ListBox();
		listbox.addItem("CLASS");
		listbox.addItem("OBJECT");
		listbox.addItem("HYBRYD");
		listbox.addItem("SEQUENCE");
		typePanel.add(listbox);
		vpanel.add(typePanel);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		
		Button closeButton = new Button("Close", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		
		Button createButton = new Button("Create", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				String diagramName = nameTb.getValue();
				Type diagramType = Type.valueOf(listbox.getValue(listbox.getSelectedIndex()));
				DiagramDto diagramInformations = new DiagramDto(diagramName, diagramType);
				
				eventBus.fireEvent(new CreateDiagramEvent(diagramInformations));
			}
		}); 
		
		buttonPanel.add(closeButton);
		buttonPanel.add(createButton);
		
		vpanel.add(buttonPanel);
		
		dialogBox.add(vpanel);
		dialogBox.center();
	}
}
