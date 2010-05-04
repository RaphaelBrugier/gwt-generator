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
package com.objetdirect.gwt.gen.server.dao;

import static com.objetdirect.gwt.gen.server.ServerHelper.getCurrentUser;

import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.objetdirect.gwt.gen.server.ServerHelper;
import com.objetdirect.gwt.gen.shared.entities.Project;
import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;

/**
 * Data access object for the project entities.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class ProjectDao {
	
	/**
	 * Create a new project for the logged user.
	 * @param name
	 * @return
	 */
	public Long createProject(String name) {
		PersistenceManager pm = ServerHelper.getPM();
		String email = ServerHelper.getCurrentUser().getEmail();
		
		Project persistedProject = new Project(name, email);

		try {
			persistedProject = pm.makePersistent(persistedProject);
		} finally  {
			pm.close();
		}
		return persistedProject.getKey();
	}
	
	/**
	 * Get all the projects of the logged user
	 * @return a list of project.
	 */
	@SuppressWarnings("unchecked")
	public Collection<Project> getProjects() {
		PersistenceManager pm = ServerHelper.getPM();
		List<Project> queryResult;
		Collection<Project> projectsFound = null;
		try {
			Query q = pm.newQuery(Project.class, "email == e");
		    q.declareParameters("String e");
		    queryResult = (List<Project>) q.execute(getCurrentUser().getEmail());
		    projectsFound = pm.detachCopyAll(queryResult);
		    
		} finally {
			pm.close();
		}

		return projectsFound;
	}
	
	/**
	 * Update a project in the base;
	 * @param project
	 */
	public Project updateProject(Project project) {
		PersistenceManager pm = ServerHelper.getPM();
		Project persistedProject = null;
		try {
			persistedProject = pm.makePersistent(project);
		} finally {
			pm.close();
		}
		
		return persistedProject;
	}
	
	/**
	 * Delete the given project.
	 * The project to delete must be owned by the logged user.
 	 * @param project The project to delete
	 */
	public void deleteProject(Project project) {
		
		PersistenceManager pm = ServerHelper.getPM();
		try {
			Project projectFound = pm.getObjectById(Project.class, project.getKey());
			if (! projectFound.getEmail().equals(getCurrentUser().getEmail())) {
				throw new GWTGeneratorException("Trying to delete a Project not owned by the user logged.");
			}
			if (projectFound == null) 
				throw new GWTGeneratorException("The Project to delete was not found.");
			
			pm.deletePersistent(projectFound);
		} finally {
			pm.close();
		}
		
	}
}
