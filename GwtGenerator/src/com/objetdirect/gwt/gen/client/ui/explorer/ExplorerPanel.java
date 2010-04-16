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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.GwtGenerator;
import com.objetdirect.gwt.gen.client.ui.Main;
import com.objetdirect.gwt.gen.shared.GWTGeneratorException;


/**
 * Panel to display the saved diagrams for a loged user and create new diagrams
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class ExplorerPanel extends Composite {

	private static ExplorerPanelUiBinder uiBinder = GWT
			.create(ExplorerPanelUiBinder.class);

	interface ExplorerPanelUiBinder extends UiBinder<Widget, ExplorerPanel> {
	}
	
	@UiField
	SpanElement nameSpan;
	
	@UiField
	Anchor signOut;
	
	@UiField
	Button createClassDiagram;
	
	private Main parent;

	public ExplorerPanel(Main parent) {
		if (!GwtGenerator.loginInfo.isLoggedIn()) {
			throw new GWTGeneratorException("A user should have been logged before construct the explorer panel");
		}
		
		initWidget(uiBinder.createAndBindUi(this));
		this.parent = parent;

		nameSpan.setInnerText( GwtGenerator.loginInfo.getEmailAddress());
		signOut.setHref(GwtGenerator.loginInfo.getLogoutUrl());
	}

	@UiHandler(value="createClassDiagram")
	void onClickCreateClassDiagram(ClickEvent event) {
		parent.createClassDiagram();
	}
	
}
