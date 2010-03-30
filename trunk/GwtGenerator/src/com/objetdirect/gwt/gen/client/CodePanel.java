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
package com.objetdirect.gwt.gen.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.objetdirect.gwt.umlapi.client.helpers.OptionsManager;

/**
 * Panel for displaying the generated code.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class CodePanel extends VerticalPanel {
	
	TabPanel tabpanel; 
	
	public CodePanel() {
		this.setWidth(OptionsManager.get("Width").toString());
		this.setHeight(OptionsManager.get("Height").toString());
		tabpanel = new TabPanel();
		tabpanel.setWidth("100%");
		tabpanel.setHeight("780px");
		this.add(tabpanel);
		
		Button backToDiagramButton = new Button("Back");
		
		backToDiagramButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				HistoryManager.applicationPanel.clear();
				HistoryManager.applicationPanel.add(HistoryManager.drawerPanel);
			}
		});
		
		this.add(backToDiagramButton);
	}
	
	public void addClassCode(String[] code, String className) {
		StringBuilder sb = new StringBuilder();
		for (String s : code) {
			sb.append(s).append("\n");
		}
		TextArea ta = new TextArea();
		ta.setText(sb.toString());
		ta.setReadOnly(false);
		ta.setHeight("750px");
		ta.setWidth("800px");
		
		SimplePanel panel = new SimplePanel();
		panel.setHeight("750px");
		panel.setWidth("800px");
		
		panel.add(ta);
		tabpanel.add(panel, className);
	}
	
	public void clearAllClassesCode() {
		tabpanel.clear();
	}
}
