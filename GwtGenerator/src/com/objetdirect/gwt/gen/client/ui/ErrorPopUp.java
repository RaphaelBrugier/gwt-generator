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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Popup for displaying an error message from an exception.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class ErrorPopUp {

	private Command closedCommand;
	private PopupPanel popUpPanel;
	
	public ErrorPopUp(Throwable cause) {
		this(cause.getMessage());
	}
	
	public ErrorPopUp(String message) {
		closedCommand = null;
		
		popUpPanel = new  PopupPanel();
		FlowPanel panel = new FlowPanel();
		
		
		Button button = new Button("Close");
		button.addClickHandler(new  ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				closedCommand.execute();
				popUpPanel.hide();
			}
		});
		
		panel.add(button);
		panel.add(new HTML(message));
		popUpPanel.add(panel);
	}
	
	/** Show the popup. */
	public void show() {
		popUpPanel.center();
	}
	
	/** Hide the popup. */
	public void hide() {
		closedCommand.execute();
		popUpPanel.hide();
	}
	
	/**
	 * Add a command to exectude before closing the popup.
	 * @param closedCommand
	 * @return this.
	 */
	public ErrorPopUp addClosedCommand(Command closedCommand) {
		this.closedCommand = closedCommand;
		return this;
	}
}
