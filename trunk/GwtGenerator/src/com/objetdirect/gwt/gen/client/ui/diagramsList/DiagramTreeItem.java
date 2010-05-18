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

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TreeItem;
import com.objetdirect.gwt.gen.client.ui.diagramsList.DiagramsListView.DiagramsListResources;
import com.objetdirect.gwt.gen.client.ui.resources.TreeProjectsResources;

/**
 * Passive view : Represent a diagram item in the tree.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class DiagramTreeItem extends TreeItem {
	
	
	private Image diagramIcon; 
	
	private InlineLabel diagramName;
	
	private Image editDiagramIcon;
	
	private Image deleteDiagramIcon;
	
	public DiagramTreeItem(String diagramName) {
		super();
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		
		diagramIcon = new Image(TreeProjectsResources.INSTANCE.diagramIcon());
		
		
		this.diagramName = new InlineLabel(diagramName);
		this.diagramName.addStyleName(DiagramsListResources.INSTANCE.css().itemText());
		
		editDiagramIcon = new Image(TreeProjectsResources.INSTANCE.editDiagramIcon());
		editDiagramIcon.addStyleName(DiagramsListResources.INSTANCE.css().actionIcon());
		editDiagramIcon.setTitle("Edit diagram");
		
		deleteDiagramIcon = new Image(TreeProjectsResources.INSTANCE.deleteIcon());
		deleteDiagramIcon.addStyleName(DiagramsListResources.INSTANCE.css().actionIcon());
		deleteDiagramIcon.setTitle("Delete the diagram");
		
		panel.add(diagramIcon);
		panel.add(this.diagramName);
		panel.add(editDiagramIcon);
		panel.add(deleteDiagramIcon);
		
		this.setWidget(panel);
	}


	public HasClickHandlers getEditDiagramButton() {
		return editDiagramIcon;
	}
	
	public HasClickHandlers getDeleteDiagramButton() {
		return deleteDiagramIcon;
	}
	
	public HasClickHandlers getDiagramName() {
		return this.diagramName;
	}
	
	public HasClickHandlers getDiagramIcon() {
		return diagramIcon;
	}
}
