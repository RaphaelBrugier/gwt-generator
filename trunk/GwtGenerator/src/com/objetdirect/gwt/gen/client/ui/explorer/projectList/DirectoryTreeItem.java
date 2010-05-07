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
package com.objetdirect.gwt.gen.client.ui.explorer.projectList;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TreeItem;
import com.objetdirect.gwt.gen.client.ui.explorer.projectList.DirectoryListPresenter.ProjectListResources;
import com.objetdirect.gwt.gen.client.ui.resources.TreeProjectsResources;
import com.objetdirect.gwt.gen.shared.entities.Directory;

/**
 * Passive view : Represent a directory item in the tree.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class DirectoryTreeItem extends TreeItem {
	
	private Directory directory;
	
	private Image addDiagramIcon;
	
	private Image deleteDirectoryIcon;
	
	public DirectoryTreeItem(Directory directory) {
		super();

		HorizontalPanel panel = new HorizontalPanel();
		Image projectIcon = new Image(TreeProjectsResources.INSTANCE.directoryIcon());
		InlineLabel projectName = new InlineLabel(directory.getName());
		addDiagramIcon = new Image(TreeProjectsResources.INSTANCE.createDiagramIcon());
		addDiagramIcon.addStyleName(ProjectListResources.INSTANCE.css().actionIcon());
		addDiagramIcon.setTitle("Create a new diagram");
		
		deleteDirectoryIcon = new Image(TreeProjectsResources.INSTANCE.deleteIcon());
		deleteDirectoryIcon.addStyleName(ProjectListResources.INSTANCE.css().actionIcon());
		deleteDirectoryIcon.setTitle("Delete this directory");
		
		panel.add(projectIcon);
		panel.add(projectName);
		panel.add(addDiagramIcon);
		panel.add(deleteDirectoryIcon);
		
		this.setWidget(panel);
	}

	/**
	 * @return the directory
	 */
	public Directory getDirectory() {
		return directory;
	}

	public HasClickHandlers getAddDiagramButton() {
		return addDiagramIcon;
	}
	
	public HasClickHandlers getDeleteDirectoryButton() {
		return deleteDirectoryIcon;
	}
}
