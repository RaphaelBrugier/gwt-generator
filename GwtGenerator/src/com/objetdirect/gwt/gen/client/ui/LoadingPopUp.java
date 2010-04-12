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


import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.objetdirect.gwt.gen.client.ui.resources.ImageResources;


/**
 * Loading popup
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class LoadingPopUp {

	private static PopupPanel popupPanel;
	
	private static Label label;
	
	public LoadingPopUp() {
		if (popupPanel == null) {
			popupPanel = new PopupPanel(false, true);
			label = new Label();
			popupPanel.setTitle("Loading");
		
			FlowPanel panel = new FlowPanel();
		
			Image ajaxLoader = new Image(ImageResources.INSTANCE.ajaxLoader());
			
			panel.add(ajaxLoader);
			panel.add(label);
			
			popupPanel.add(panel);
		}
	}
	
	public void startProcessing() {
		startProcessing("Please wait...");
	}
	
	public void startProcessing(String message) {
		label.setText(message);
		popupPanel.center();
	}
	
	public void stopProcessing() {
		popupPanel.hide();
	}
}
