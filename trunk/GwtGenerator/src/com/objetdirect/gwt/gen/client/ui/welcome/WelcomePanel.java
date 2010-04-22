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
package com.objetdirect.gwt.gen.client.ui.welcome;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.event.DesignAsAGuestEvent;

/**
 * Panel displaying a welcome message and a link to log the user with his google account.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class WelcomePanel extends Composite {

	private static WelcomePanelUiBinder uiBinder = GWT
			.create(WelcomePanelUiBinder.class);

	interface WelcomePanelUiBinder extends UiBinder<Widget, WelcomePanel> {
	}
	
	@UiField
	Anchor signIn;
	
	@UiField
	InlineHyperlink asAGuest;
	
	private HandlerManager eventBus;
	
	/**
	 * @param parent The parent controller of the panel.
	 * @param loginUrl the url to log the user with his google account.
	 */
	public WelcomePanel(HandlerManager eventBus, String loginUrl) {
		initWidget(uiBinder.createAndBindUi(this));
		this.eventBus = eventBus;
		
		signIn.setHref(loginUrl);
	}
	
	@UiHandler(value="asAGuest")
	void onAsAGuestClick(ClickEvent event) {
		eventBus.fireEvent(new DesignAsAGuestEvent());
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(this);
	}
}
