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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

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

	
	@UiField
	LayoutPanel contentPanel;
	
	@UiField
	Button saveButton;
	
	@UiField
	Button generateButton;

	public ContentView() {
		initWidget(uiBinder.createAndBindUi(this));
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
}
