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
package com.objetdirect.gwt.gen.server.services;

import static com.objetdirect.gwt.gen.server.ServerHelper.checkLoggedIn;
import static com.objetdirect.gwt.gen.server.ServerHelper.getCurrentUser;
import static com.objetdirect.gwt.umlapi.client.helpers.GWTUMLDrawerHelper.isBlank;

import java.util.Collection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.objetdirect.gwt.gen.client.services.ProjectService;
import com.objetdirect.gwt.gen.server.dao.DirectoryDao;
import com.objetdirect.gwt.gen.server.dao.ProjectDao;
import com.objetdirect.gwt.gen.shared.entities.Directory;
import com.objetdirect.gwt.gen.shared.entities.Project;
import com.objetdirect.gwt.gen.shared.exceptions.CreateProjectException;

/**
 * Real implementation of ProjectService
 * @see com.objetdirect.gwt.gen.client.services.ProjectService
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@SuppressWarnings("serial")
public class ProjectServiceImpl extends RemoteServiceServlet implements ProjectService {

	private final ProjectDao projectDao = new ProjectDao();
	private final DirectoryDao directoryDao = new DirectoryDao();
	
	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#createProject(java.lang.String)
	 */
	@Override
	public Long createProject(String name) {
		checkLoggedIn();
		
		if (isBlank(name)) {
			throw new CreateProjectException("You must specify a name to your project");
		}
		//TODO check if a project with the same name already exist

		return projectDao.createProject(name);
	}

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#getProjects()
	 */
	@Override
	public Collection<Project> getProjects() {
		checkLoggedIn();
		return projectDao.getProjects();
	}

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#updateProject(com.objetdirect.gwt.gen.shared.entities.Project)
	 */
	@Override
	public void updateProject(Project project) {
		checkLoggedIn();
		projectDao.updateProject(project);
	}

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#deleteProject(com.objetdirect.gwt.gen.shared.entities.Project)
	 */
	@Override
	public void deleteProject(Project projectToDelete) {
		checkLoggedIn();
		projectDao.deleteProject(projectToDelete);
	}

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#addDirectory(com.objetdirect.gwt.gen.shared.entities.Project, java.lang.String)
	 */
	@Override
	public void addDirectory(Project project, String directoryName) {
		checkLoggedIn();
		
		if (isBlank(directoryName)) {
			throw new CreateProjectException("You must specify a name to your directory");
		}
		//TODO check if a directory with the same name already exist

		Directory newDirectory = new Directory(directoryName, getCurrentUser().getEmail());
		project.addDirectory(newDirectory);
		projectDao.updateProject(project);
	}


	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#deleteDirectory(com.objetdirect.gwt.gen.shared.entities.Project, com.objetdirect.gwt.gen.shared.entities.Directory)
	 */
	@Override
	public void deleteDirectory(Project project, Directory directory) {
		checkLoggedIn();
		
		System.out.println("deleteDirectory  " + directory + "  on project " + project);
		System.out.println("dir key = " + directory.getKey());
		Project projectOwner = projectDao.getProjectById(project.getKey());
		projectOwner.removeDirectory(directory);
		projectDao.updateProject(projectOwner);
		directoryDao.deleteDirectory(directory);
	}

}
