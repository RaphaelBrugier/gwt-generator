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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TreeItem;
import com.objetdirect.gwt.gen.client.ui.explorer.projectList.DirectoryListPresenter.ProjectListResources;
import com.objetdirect.gwt.gen.client.ui.resources.TreeProjectsResources;
import com.objetdirect.gwt.gen.shared.entities.Project;

/**
 * Passive View : Represent a project item in the tree.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class ProjectTreeItem extends TreeItem {

	private Project project;

	private Image addDirectoryIcon;

	private Image deleteProjectIcon;

	public ProjectTreeItem(Project project) {
		super();
		this.project = project;

		FlowPanel panel = new FlowPanel();

		Image projectIcon = new Image(TreeProjectsResources.INSTANCE.projectIcon());
		InlineLabel projectName = new InlineLabel(project.getName());
		addDirectoryIcon = new Image(TreeProjectsResources.INSTANCE.addIcon());
		addDirectoryIcon.addStyleName(ProjectListResources.INSTANCE.css().actionIcon());
		addDirectoryIcon.setTitle("Add a directory in this project");
		
		deleteProjectIcon = new Image(TreeProjectsResources.INSTANCE.deleteIcon());
		deleteProjectIcon.addStyleName(ProjectListResources.INSTANCE.css().actionIcon());
		deleteProjectIcon.setTitle("Delete this project");
		
		panel.add(projectIcon);
		panel.add(projectName);
		panel.add(addDirectoryIcon);
		panel.add(deleteProjectIcon);
		
		this.setWidget(panel);
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}
	
	public HasClickHandlers getAddDirectoryButton() {
		return addDirectoryIcon;
	}
	
	public HasClickHandlers getDeleteProjectButton() {
		return deleteProjectIcon;
	}
}
