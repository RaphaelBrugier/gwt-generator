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
package com.objetdirect.gwt.gen.client.ui.content;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.ui.resources.ImageResources;

/**
 * View : Manage the content panel
 * The content panel displays the modeler and the generated code.
 * It also displays a navigation bar to do some simple actions on the current diagram displayed.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class ContentView extends ResizeComposite implements ContentPresenter.Display {

	private static ContentViewUiBinder uiBinder = GWT
			.create(ContentViewUiBinder.class);

	interface ContentViewUiBinder extends UiBinder<Widget, ContentView> {
	}

	public interface ContentStyle extends CssResource {
		String horizontalAlignCenter();
		String loadingMessage();
	}
	
	public interface ContentResources extends ClientBundle {
		public ContentResources INSTANCE = GWT.create(ContentResources.class);
		
		@Source("ContentStyle.css")
		ContentStyle css();
	}
	
	@UiField
	LayoutPanel contentPanel;
	
	@UiField
	Button saveButton;
	
	@UiField
	Button generateButton;

	public ContentView() {
		initWidget(uiBinder.createAndBindUi(this));
		ContentResources.INSTANCE.css().ensureInjected();
	}


	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public LayoutPanel getModelerContainer() {
		return contentPanel;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}
	
	@Override
	public HasClickHandlers getGenerateButton() {
		return generateButton;
	}

	@Override
	public void displayLoadingMessage() {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(css().horizontalAlignCenter());
		
		Image ajaxLoader = new Image(ImageResources.INSTANCE.ajaxLoader());
		ajaxLoader.addStyleName(css().loadingMessage());
		Label loadingMessage = new Label("Loading the diagram");
		
		panel.add(ajaxLoader);
		panel.add(loadingMessage);
		contentPanel.clear();
		contentPanel.add(panel);
	}

	@Override
	public void clearAllMessages() {
		contentPanel.clear();
	}
	
	/**
	 * Helper Method to access to the css ressources
	 * @return
	 */
	private ContentStyle css() {
		return ContentResources.INSTANCE.css();
	}
}
