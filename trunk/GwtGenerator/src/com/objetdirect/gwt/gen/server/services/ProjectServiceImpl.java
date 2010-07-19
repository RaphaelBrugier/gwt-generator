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
import static com.objetdirect.gwt.gen.shared.entities.Directory.DirectoryType.DOMAIN;
import static com.objetdirect.gwt.gen.shared.entities.Directory.DirectoryType.SEAM;
import static com.objetdirect.gwt.umlapi.client.helpers.GWTUMLDrawerHelper.isBlank;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType.CLASS;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.ProjectService;
import com.objetdirect.gwt.gen.server.dao.DiagramDao;
import com.objetdirect.gwt.gen.server.dao.ProjectDao;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
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

	/**
	 * Reserved name for the admin project
	 */
	private static final String ADMIN_PROJECT_NAME = "adminProject";
	
	private static final String SEAM_DIAGRAM_NAME = "seamClasses";

	private final ProjectDao projectDao = new ProjectDao();

	private final DiagramService diagramService = new DiagramServiceImpl();
	
	private final DiagramDao diagramDao = new DiagramDao();

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#createProject(java.lang.String)
	 */
	@Override
	public Long createProject(String name) {
		checkLoggedIn();

		if (isBlank(name)) {
			throw new CreateProjectException("You must specify a name to your project");
		}

		Project persistedProject = projectDao.createProject(name);

		createDomainDirectory(persistedProject);
		createInterfaceDirectory(persistedProject);

		projectDao.updateProject(persistedProject);
		
		return persistedProject.getKey();
	}

	/**
	 * Create a directory in the given project
	 * @param project
	 */
	private void createDomainDirectory(Project project) {
		Directory newDirectory = new Directory("Domain", getCurrentUser().getEmail(), DOMAIN);
		project.addDirectory(newDirectory);
	}
	
	/**
	 * Create a directory in the given project
	 * @param project
	 */
	private void createInterfaceDirectory(Project project) {
		Directory seamDirectory = new Directory("Seam", getCurrentUser().getEmail(), SEAM);
		project.addDirectory(seamDirectory);
	}


	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#getProjects()
	 */
	@Override
	public List<Project> getProjects() {
		checkLoggedIn();

		List<Project> projects = projectDao.getProjects();

		if (UserServiceFactory.getUserService().isUserAdmin()) {
			projects.add(getOrCreateAdminProject());
		}

		for (Project project : projects) {
			for (Directory directory : project.getDirectories()) {
				ArrayList<DiagramDto> diagrams = diagramService.getDiagrams(directory.getKey());
				
				// For the seam directory, we add the class diagram of the classes supported by the code generator.
				if (directory.getDirType() == SEAM) {
					DiagramDto seamDiagram = getSeamDiagram();
					if (seamDiagram != null)
						diagrams.add(seamDiagram);
				}
				directory.setDiagrams(diagrams);
			}
		}

		return projects;
	}

	
	/**
	 * @return the seam class diagram of the classes supported by the code generator.
	 */
	private DiagramDto getSeamDiagram() {
		Project adminProject = projectDao.getProjectByName(ADMIN_PROJECT_NAME);
		// If the seam class diagram has not been created or if it's owned by the logged user we do not need to add it again.
		if (adminProject == null || UserServiceFactory.getUserService().getCurrentUser().getEmail() == adminProject.getEmail())
			return null;
		
		Directory seamDir = adminProject.getDirectory(SEAM);
		List<DiagramDto> diagrams = diagramDao.getDiagrams(seamDir.getKey());
		
		DiagramDto seamDiagram = diagrams.get(0);
		seamDiagram.editable = false;
		return seamDiagram;
	}

	/**
	 *	Return the special project where an user logged as an administrator can configure the seam class diagram.
	 *	If the project does not already exist, this method will create it and also create the diagram.
	 *
	 * @return The special admin project
	 */
	private Project getOrCreateAdminProject() {
		Project adminProject = projectDao.getProjectByName(ADMIN_PROJECT_NAME);
		if (adminProject == null) {
			adminProject = projectDao.createProject(ADMIN_PROJECT_NAME);
			
			Directory seamDirectory = new Directory("Seam", getCurrentUser().getEmail(), SEAM);
			adminProject.addDirectory(seamDirectory);
			projectDao.updateProject(adminProject);
			diagramDao.createDiagram(adminProject.getDirectory(SEAM).getKey(), CLASS, SEAM_DIAGRAM_NAME, null, null);
		} 
		// If the admin project was already existing, we check if is owned by the current logged admin.
		// In this case, we do not need to return it again because it was already added with the getProjects method.
		if (UserServiceFactory.getUserService().getCurrentUser().getEmail() == adminProject.getEmail()){
			return null;
			
		}
		
		return adminProject;
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
