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
package com.objetdirect.gwt.gen.client.ui.explorer.projectList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for the explorer list.
 * Contains the Popup views.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class DirectoryListView extends Composite implements DirectoryListPresenter.Display {

	private static Binder uiBinder = GWT.create(Binder.class);

	@UiTemplate("DirectoryList.ui.xml")
	interface Binder extends UiBinder<Widget, DirectoryListView> {}
	
	public interface DirectoryListStyle extends CssResource {
		/* Tree items styles */
		String actionIcon();
		String itemIcon();
		String itemText();

		/* Popup styles */
		String createButton();
		String popupTitle();
		String popupContent();
		String label();
	}
	
	public interface ProjectListResources extends ClientBundle {
		public ProjectListResources INSTANCE = GWT.create(ProjectListResources.class);
		
		@Source("DirectoryListStyle.css")
		DirectoryListStyle css();
	}
	
	@UiField
	Anchor createProjectButton;

	@UiField
	SimplePanel treeContainer;
	
	public DirectoryListView() {
		initWidget(uiBinder.createAndBindUi(this));
		ProjectListResources.INSTANCE.css().ensureInjected();
	}
	
	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public HasWidgets getContainer() {
		return treeContainer;
	}

	@Override
	public HasClickHandlers getCreateProjectButton() {
		return createProjectButton;
	}

	@Override
	public Widget getLoadingWidget() {
		//TODO
		return new Label("loading");
	}

	@Override
	public Widget getNoProjectWidget() {
		//TODO
		return new Label("no project to display");
	}
	
	
	/**
	 * Passive view : A simple popup that displays a textbox and a button to create a new Project.
	 */
	static class CreateProjectPopup extends SimplePanel {
		private static CreateProjectPopupUiBinder uiBinder = GWT.create(CreateProjectPopupUiBinder.class);
		
		@UiTemplate("CreateProjectPopup.ui.xml")
		interface CreateProjectPopupUiBinder extends UiBinder<PopupPanel, CreateProjectPopup> {}
		
		private final PopupPanel popuPanel;
		
		@UiField
		TextBox projectNameTb;
		
		@UiField
		Button createProject;
		
		public CreateProjectPopup() {
			popuPanel = uiBinder.createAndBindUi(this);
			setWidget(popuPanel);
		}

		public HasClickHandlers getCreateButton() {
			return createProject;
		}
				
		public String getProjectName() {
			return projectNameTb.getValue();
		}
		
		public void hide() {
			popuPanel.hide();
		}
		
		public void show() {
			popuPanel.center();
		}
	}
	

	/**
	 * Passive View : A simple popup that displays a textbox and a button to create a new Directory.
	 */
	static class CreateDirectoryPopup extends SimplePanel {
		private static CreateDiretoryPopupUiBinder uiBinder = GWT.create(CreateDiretoryPopupUiBinder.class);
		
		@UiTemplate("CreateDirectoryPopup.ui.xml")
		interface CreateDiretoryPopupUiBinder extends UiBinder<PopupPanel, CreateDirectoryPopup> {}
		
		private final PopupPanel popuPanel;
		
		@UiField
		TextBox directoryNameTb;
		
		@UiField
		Button createDirectory;
		
		public CreateDirectoryPopup() {
			popuPanel = uiBinder.createAndBindUi(this);
			setWidget(popuPanel);
		}
		
		public HasClickHandlers getCreateButton() {
			return createDirectory;
		}
				
		public String getDirectoryName() {
			return directoryNameTb.getValue();
		}
		
		public void hide() {
			popuPanel.hide();
		}
		
		public void show() {
			popuPanel.center();
		}
	}
	
	/**
	 * Passive View : A simple popup that displays a textbox, a list of diagram type and a button to create a new Diagram.
	 */
	static class CreateDiagramPopup extends SimplePanel {
		private static CreateDiagramPopupUiBinder uiBinder = GWT.create(CreateDiagramPopupUiBinder.class);
		
		@UiTemplate("CreateDiagramPopup.ui.xml")
		interface CreateDiagramPopupUiBinder extends UiBinder<PopupPanel, CreateDiagramPopup> {}
		
		private final PopupPanel popuPanel;
		
		@UiField
		TextBox diagramNameTb;
		
		@UiField
		Button createDiagramButton;
		
		@UiField
		ListBox typeList;
		
		public CreateDiagramPopup() {
			popuPanel = uiBinder.createAndBindUi(this);
			setWidget(popuPanel);
			typeList.addItem("Class");
			typeList.addItem("Object");
			typeList.addItem("Hybrid");
			typeList.addItem("Sequence");
		}
		
		public HasClickHandlers getCreateButton() {
			return createDiagramButton;
		}
				
		public String getDiagramName() {
			return diagramNameTb.getValue();
		}
		
		public String getDiagramType() {
			return typeList.getItemText(typeList.getSelectedIndex());
		}
		
		public void hide() {
			popuPanel.hide();
		}
		
		public void show() {
			popuPanel.center();
		}
	}
}
