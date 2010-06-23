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
package com.objetdirect.gwt.gen.client.ui.popup;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.objetdirect.tatami.client.Toaster;

/**
 * Display an error message at the bottom of the page for few seconds.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class ErrorToaster {

	private static Toaster INSTANCE;
	
	private ErrorToaster() {
	}
	
	
	/**
	 * Instantiate the toaster and attach it to page.
	 * This method should be call each time after the RootLayoutPanel has been cleared.
	 */
	public static void intantiateAndAttach() {
		if (INSTANCE == null) {
			INSTANCE =  new  Toaster("error"); 
		}
		
		// If not attached to the root panel.
		if (RootLayoutPanel.get().getWidgetIndex(INSTANCE) == -1) {
			RootLayoutPanel.get().add(INSTANCE);
		}
	}
	
	/**
	 * Show an informative message for 2 seconds at the bottom of the page.
	 * @param message
	 */
	public static void show(String message) {
		INSTANCE.setDuration(2000);
		
		StringBuilder html = new StringBuilder();
		html.append("<div class=\"errorToaster\">");
		html.append(message);
		html.append("</div>");
		
		INSTANCE.setMessage(html.toString());
		INSTANCE.show();
	}
}
