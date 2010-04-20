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

import java.util.Collection;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.GwtGenerator;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.ui.Main;
import com.objetdirect.gwt.gen.client.ui.resources.ImageResources;
import com.objetdirect.gwt.gen.shared.dto.DiagramInformations;
import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;


/**
 * Panel to display the saved diagrams for a loged user and create new diagrams
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class ExplorerPanel extends Composite {

	private static ExplorerPanelUiBinder uiBinder = GWT
			.create(ExplorerPanelUiBinder.class);

	interface ExplorerPanelUiBinder extends UiBinder<Widget, ExplorerPanel> {
	}
	
	private final DiagramServiceAsync diagramService = GWT.create(DiagramService.class);
	
	@UiField
	SpanElement nameSpan;
	
	@UiField
	Anchor signOut;
	
	@UiField
	Button createClassDiagram;
	
	@UiField
	FlowPanel westPanel;
	
	@UiField
	FlowPanel content;
	
	
	private Main parent;

	public ExplorerPanel(Main parent) {
		if (!GwtGenerator.loginInfo.isLoggedIn()) {
			throw new GWTGeneratorException("A user should have been logged before construct the explorer panel");
		}
		
		initWidget(uiBinder.createAndBindUi(this));
		this.parent = parent;

		nameSpan.setInnerText( GwtGenerator.loginInfo.getNickname());
		signOut.setHref(GwtGenerator.loginInfo.getLogoutUrl());
		
		populateWestPanel();
		populateContentPanel();
	}
	
	

	private void populateContentPanel() {
		Image loader = new Image(ImageResources.INSTANCE.ajaxLoader());
		content.add(loader);
		
		diagramService.getDiagrams(new AsyncCallback<Collection<DiagramInformations>>() {
			
			@Override
			public void onSuccess(Collection<DiagramInformations> result) {
				if (result.size()==0) {
					content.clear();
					content.add(new Label("No diagram found."));
				} else {
					for (DiagramInformations d : result) {
						content.clear();
						HTML text = new HTML();
						text.setText("Type = " + d.getType() + " name = " + d.getName());
						content.add(text);
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.debug(caught.getMessage());
				Window.alert("Unable to retrieve diagrams from the base.");
			}
		});
	}

	private void populateWestPanel() {
		TreeItem item1 = new TreeItem("Class diagrams");
		TreeItem item2 = new TreeItem("Object diagrams");
		TreeItem item3 = new TreeItem("Sequence diagrams");
		
		TreeItem project1 = new TreeItem("Project 1");
		project1.addItem(item1);
		project1.addItem(item2);
		project1.addItem(item3);
		project1.setState(true);
		
		TreeItem project2 = new TreeItem("Project 2");
		project2.addItem(new TreeItem("Class diagrams"));
		project2.addItem(new TreeItem("Object diagrams"));
		project2.addItem(new TreeItem("Sequence diagrams"));
		
		TreeItem root = new TreeItem("Projects");
		root.addItem(project1);
		root.addItem(project2);
		
		Tree tree = new Tree();
		tree.addItem(root);
		
		westPanel.clear();
		westPanel.add(tree);
		
		root.setState(true);
	}


	@UiHandler(value="createClassDiagram")
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
				parent.createDiagram(nameTb.getValue(), listbox.getValue(listbox.getSelectedIndex()));
			}
		}); 
		
		buttonPanel.add(closeButton);
		buttonPanel.add(createButton);
		
		vpanel.add(buttonPanel);
		
		dialogBox.add(vpanel);
		dialogBox.center();
	}
}
