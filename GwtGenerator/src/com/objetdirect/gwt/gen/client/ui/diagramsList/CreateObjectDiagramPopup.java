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
package com.objetdirect.gwt.gen.client.ui.diagramsList;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.objetdirect.gwt.gen.client.ui.diagramsList.DiagramsListPresenter.DisplayPopupDiagram;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;

/**
 * Passive View : A simple popup to create a new object diagram.
 *
 */
class CreateObjectDiagramPopup extends SimplePanel implements DisplayPopupDiagram {
	private static CreateDiagramPopupUiBinder uiBinder = GWT.create(CreateDiagramPopupUiBinder.class);
	
	@UiTemplate("CreateObjectDiagramPopup.ui.xml")
	interface CreateDiagramPopupUiBinder extends UiBinder<PopupPanel, CreateObjectDiagramPopup> {}
	
	private final PopupPanel popuPanel;
	
	@UiField
	TextBox diagramNameTb;
	
	@UiField
	Button createDiagramButton;
	
	@UiField
	ListBox classDiagramsList;
	
	@UiField
	Label message;
	
	
	public CreateObjectDiagramPopup(List<DiagramDto> classDiagramsDto) {
		popuPanel = uiBinder.createAndBindUi(this);
		
		addDiagramInList(classDiagramsDto);
		setWidget(popuPanel);
	}
	
	// Seems like logic written in the display ...
	private void addDiagramInList(List<DiagramDto> classDiagramsDto) {
		if (classDiagramsDto.size() == 0) {
			createDiagramButton.setEnabled(false);
			classDiagramsList.setEnabled(false);
			message.setText("You must create a class diagram before to create an object diagram.");
		} else {
			for(DiagramDto classDiagram : classDiagramsDto) {
				classDiagramsList.addItem(classDiagram.getName(), classDiagram.getKey());
			}
		}
		
	}

	@Override
	public HasClickHandlers getCreateButton() {
		return createDiagramButton;
	}
	
	@Override
	public String getDiagramName() {
		return diagramNameTb.getValue();
	}
	
	@Override
	public void hide() {
		popuPanel.hide();
	}
	
	@Override
	public void show() {
		popuPanel.center();
		diagramNameTb.setFocus(true);
	}

	/**
	 * @return the Class Diagram Key Selected
	 */
	public String getClassDiagramKeySelected() {
		return classDiagramsList.getValue(classDiagramsList.getSelectedIndex());
	}
}