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
import static com.objetdirect.gwt.umlapi.client.helpers.GWTUMLDrawerHelper.isBlank;

import java.util.Collection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.objetdirect.gwt.gen.client.services.ProjectService;
import com.objetdirect.gwt.gen.server.dao.ProjectDao;
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
	
	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#createProject(java.lang.String)
	 */
	@Override
	public Long createProject(String name) {
		checkLoggedIn();
		
		if (isBlank(name)) {
			throw new CreateProjectException("You must specify a name to your project");
		}
		
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
		projectDao.updateProject(project);
	}
}
