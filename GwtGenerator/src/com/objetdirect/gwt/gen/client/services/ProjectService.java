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
package com.objetdirect.gwt.gen.client.services;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.objetdirect.gwt.gen.shared.entities.Project;

/**
 * Service to operate on the stored projects.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@RemoteServiceRelativePath("project")
public interface ProjectService extends RemoteService {

	/**
	 * Create a new project for the logged user.
	 * @param name name of the project.
	 * @return the id of the created project.
	 * @throws CreateProjectException if the name is already existing or blank.
	 */
	public Long createProject(String name);

	/**
	 * Get all the project of the logged user.
	 * @return a collection of projects.
	 */
	public Collection<Project> getProjects();
	
	/**
	 * Update the given project by saving it on the base.
	 * @param project The project to save.
	 */
	public void updateProject(Project project);
	
	/**
	 * Delete the given Project
	 * @param projectToDelete the project to delete. 
	 */
	public void deleteProject(Project projectToDelete);
	
	/**
	 * Add a directory to the given project
	 * @param project the project 
	 * @param directoryName the name of the directory to add;
	 */
	public void addDirectory(Project project, String directoryName);
	
}
