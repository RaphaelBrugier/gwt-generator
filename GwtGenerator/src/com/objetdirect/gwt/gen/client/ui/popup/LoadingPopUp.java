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


import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.objetdirect.gwt.gen.client.ui.resources.ImageResources;


/**
 * A simple pop up for displaying a loading message and an "web 2.0 gif loader" 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class LoadingPopUp {
	
	public static LoadingPopUp getInstance() {
		if (INSTANCE == null)
			INSTANCE = new LoadingPopUp();
		
		return INSTANCE;
	}
	
	private static LoadingPopUp INSTANCE;
	
	private PopupPanel popupPanel;
	
	private Label label;
	
	private boolean processing;
	
	/** Constructor of the popup. */
	private LoadingPopUp() {
		popupPanel = new PopupPanel(false, true);
		label = new Label();
		popupPanel.setTitle("Loading");
		popupPanel.setGlassEnabled(true);
	
		FlowPanel panel = new FlowPanel();
	
		Image ajaxLoader = new Image(ImageResources.INSTANCE.ajaxLoader());
		
		panel.add(ajaxLoader);
		panel.add(label);
		
		popupPanel.add(panel);
		processing = false;
	}
	
	/**
	 * Show the pop up with a default loading message : "Please wait ..."
	 */
	public void startProcessing() {
		startProcessing("Please wait...");
	}
	
	/**
	 * Show the pop up with a given message.
	 * @param message the message.
	 */
	public void startProcessing(String message) {
		if(! processing) {
			label.setText(message);
			popupPanel.center();
			processing = true;
		}
	}
	
	/**
	 * Hide the popup.
	 */
	public void stopProcessing() {
		processing = false;
		popupPanel.hide();
	}
}
