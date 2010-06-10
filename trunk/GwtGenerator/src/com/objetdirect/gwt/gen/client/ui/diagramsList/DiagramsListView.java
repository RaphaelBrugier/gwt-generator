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
package com.objetdirect.gwt.gen.client.ui.diagramsList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.ui.resources.BaseCss;
import com.objetdirect.gwt.gen.client.ui.resources.ImageResources;

/**
 * View for the explorer list.
 * Contains the Popup views.
 * 
 * Presenter and its associated view are inspired by the mvp pattern.
 * @see http://code.google.com/intl/fr/webtoolkit/articles/mvp-architecture.html
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class DiagramsListView extends Composite implements DiagramsListPresenter.Display {

	private static Binder uiBinder = GWT.create(Binder.class);

	@UiTemplate("DiagramsList.ui.xml")
	interface Binder extends UiBinder<Widget, DiagramsListView> {}
	
	public interface DiagramsListStyle extends BaseCss {
		/* Explorer tree styles */
		String noProjectWrapper();
		String noProjectTitle();
		String noProjectText();
		String loadingProjectsWrapper();
		
		/* Tree items styles */
		String actionIcon();
		String itemIcon();
		String itemText();

		/* Common styles for the popups */
		String createButton();
		String popupTitle();
		String popupContent();
		String label();
	}
	
	public interface DiagramsListResources extends ClientBundle {
		public DiagramsListResources INSTANCE = GWT.create(DiagramsListResources.class);
		
		@Source({"../resources/base.css", "DiagramsListStyle.css"})
		DiagramsListStyle css();
	}

	@UiField
	SimplePanel mainContainer;
	
	public DiagramsListView() {
		initWidget(uiBinder.createAndBindUi(this));
		DiagramsListResources.INSTANCE.css().ensureInjected();
	}
	
	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public HasWidgets getContainer() {
		return mainContainer;
	}

	@Override
	public Widget getLoadingProjectsWidget() {
		FlowPanel loadingProjectsWrapper = new FlowPanel();
		loadingProjectsWrapper.addStyleName(css().loadingProjectsWrapper());

		Image ajaxLoadingImage =  new Image(ImageResources.INSTANCE.circleAjaxLoader());

		loadingProjectsWrapper.add(ajaxLoadingImage);
		return loadingProjectsWrapper;
	}

	@Override
	public Widget getNoProjectWidget() {
		FlowPanel noProjectWrapper = new FlowPanel();
		noProjectWrapper.addStyleName(css().noProjectWrapper());
		
		Label noProjectTitle = new Label("There is no project to display.");
		noProjectTitle.addStyleName(css().noProjectTitle());
		
		Label noProjectText = new Label("Create a new project and then create a directory to add new diagrams.");
		noProjectText.addStyleName(css().noProjectText());
		
		noProjectWrapper.add(noProjectTitle);
		noProjectWrapper.add(noProjectText);
		return noProjectWrapper;
	}
	
	@Override
	public DiagramTreeItem createDiagramTreeItem(String diagramName) {
		return new DiagramTreeItem(diagramName);
	}
	
	/**
	 * @return Helper access to the css String;
	 */
	private DiagramsListStyle css() {
		return DiagramsListResources.INSTANCE.css();
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
			projectNameTb.setFocus(true);
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
		
		
		public CreateDiagramPopup() {
			popuPanel = uiBinder.createAndBindUi(this);
			setWidget(popuPanel);
		}
		
		public HasClickHandlers getCreateButton() {
			return createDiagramButton;
		}
				
		public String getDiagramName() {
			return diagramNameTb.getValue();
		}
		
		public void hide() {
			popuPanel.hide();
		}
		
		public void show() {
			popuPanel.center();
			diagramNameTb.setFocus(true);
		}
	}
}
