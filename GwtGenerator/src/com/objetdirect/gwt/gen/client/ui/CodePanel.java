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

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget for displaying the generated code.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 *
 */
public class CodePanel extends Composite {
	private static CodePanelUiBinder uiBinder = GWT
			.create(CodePanelUiBinder.class);

	interface CodePanelUiBinder extends UiBinder<Widget, CodePanel> {
	}

	@UiField
	TabLayoutPanel tabPanel;
	
	public CodePanel() {
		initWidget(uiBinder.createAndBindUi(this));
		tabPanel.setSize("100%", "750px");
	}
	
	public void addClassCode(List<String> code, String className) {
		StringBuilder sb = new StringBuilder();
		for (String s : code) {
			sb.append(s).append("\n");
		}
		TextArea ta = new TextArea();
		ta.setText(sb.toString());
		ta.setReadOnly(false);
//		ta.setCharacterWidth(360);
//		ta.setVisibleLines(45);
		ta.setHeight("100%");
		ta.setWidth("100%");
		
		// Wrapp the textArea, it allows to display the scrollbar 
		SimplePanel wrapper = new SimplePanel();
		wrapper.add(ta);
		
		tabPanel.add(wrapper, className);
	}
	
	public void cleanAllCode() {
		tabPanel.clear();
	}

	public void goToFirstClass() {
		tabPanel.selectTab(0);
	}

}
