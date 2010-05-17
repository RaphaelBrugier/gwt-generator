/*
 * This file is part of the Gwt-Generator project and was written by Rapha�l Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright � 2010 Objet Direct
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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.ProjectService;
import com.objetdirect.gwt.gen.server.dao.ProjectDao;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.entities.Directory;
import com.objetdirect.gwt.gen.shared.entities.Project;
import com.objetdirect.gwt.gen.shared.entities.Directory.DirType;
import com.objetdirect.gwt.gen.shared.exceptions.CreateProjectException;

/**
 * Real implementation of ProjectService
 * @see com.objetdirect.gwt.gen.client.services.ProjectService
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@SuppressWarnings("serial")
public class ProjectServiceImpl extends RemoteServiceServlet implements ProjectService {

	private final ProjectDao projectDao = new ProjectDao();
	
	private final DiagramService diagramService = new DiagramServiceImpl();
	
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

		Project persistedProject = projectDao.createProject(name);
		
		for (DirType dirType : DirType.values()) {
			Directory newDirectory = new Directory(dirType.toString().toLowerCase(), getCurrentUser().getEmail(), dirType);
			persistedProject.addDirectory(newDirectory);
		}
		projectDao.updateProject(persistedProject);
		
		return persistedProject.getKey();
	}

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#getProjects()
	 */
	@Override
	public List<Project> getProjects() {
		checkLoggedIn();
		
		List<Project> projects = projectDao.getProjects(); 
		
		for (Project project : projects) {
			for (Directory directory : project.getDirectories()) {
				ArrayList<DiagramDto> diagrams = diagramService.getDiagrams(directory.getKey());
				directory.setDiagrams(diagrams);
			}
		}
		
		return projects;
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

}
