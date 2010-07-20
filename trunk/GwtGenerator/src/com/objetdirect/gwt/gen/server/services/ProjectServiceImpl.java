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

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.ProjectService;
import com.objetdirect.gwt.gen.server.dao.ProjectDao;
import com.objetdirect.gwt.gen.server.dao.SeamDiagramDao;
import com.objetdirect.gwt.gen.server.entities.SeamDiagram;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.entities.Directory;
import com.objetdirect.gwt.gen.shared.entities.Project;
import com.objetdirect.gwt.gen.shared.exceptions.CreateProjectException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType;

/**
 * Real implementation of ProjectService
 * @see com.objetdirect.gwt.gen.client.services.ProjectService
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@SuppressWarnings("serial")
public class ProjectServiceImpl extends RemoteServiceServlet implements ProjectService {

	public static final String SEAM_CLASSES_SUPPORTED = "Seam Classes supported";

	private final ProjectDao projectDao = new ProjectDao();

	private final DiagramService diagramService = new DiagramServiceImpl();
	
	private final SeamDiagramDao seamDiagramDao = new SeamDiagramDao();

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

		for (Project project : projects) {
			for (Directory directory : project.getDirectories()) {
				ArrayList<DiagramDto> diagrams = diagramService.getDiagrams(directory.getKey());
				
				// For the seam directory, we add the class diagram of the classes supported by the code generator.
				if (directory.getDirType() == SEAM) {
					DiagramDto seamDiagram = getSeamDiagram();
					diagrams.add(seamDiagram);
				}
				directory.setDiagrams(diagrams);
			}
		}

		if (UserServiceFactory.getUserService().isUserAdmin()) {
			projects.add(getAdminProject());
		}

		return projects;
	}

	
	/**
	 * @return the seam class diagram of the classes supported by the code generator.
	 */
	private DiagramDto getSeamDiagram() {
		DiagramDto seamDiagramDto = new DiagramDto();
		seamDiagramDto.setName(SEAM_CLASSES_SUPPORTED);
		seamDiagramDto.seamSpecialDiagram = true;
		seamDiagramDto.setKey(null);
		seamDiagramDto.setType(DiagramType.CLASS);
		return seamDiagramDto;
	}

	/**
	 *	Return the special project where an user logged as an administrator can configure the seam class diagram.
	 *	If the project does not already exist, this method will create it and also create the diagram.
	 *
	 * @return The special admin project
	 */
	private Project getAdminProject() {
		SeamDiagram	seamDiagram = seamDiagramDao.getSeamDiagram();
		if (seamDiagram == null) {
			seamDiagramDao.createSeamDiagram();
			seamDiagram = seamDiagramDao.getSeamDiagram();
		}
		
		DiagramDto seamDiagramDto = new DiagramDto();
		seamDiagramDto.setName(SEAM_CLASSES_SUPPORTED);
		seamDiagramDto.setCanvas(seamDiagram.getCanvas());
		seamDiagramDto.seamSpecialDiagram = false;
		seamDiagramDto.setKey(null);
		
		List<DiagramDto> diagrams = new ArrayList<DiagramDto>();
		diagrams.add(seamDiagramDto);
		
		Directory seamDirectory = new Directory("seam diagram", "", SEAM);
		seamDirectory.setDiagrams(diagrams);

		Project adminProject = new Project("adminProject", "");
		adminProject.setKey(null);
		adminProject.addDirectory(seamDirectory);
		
		return adminProject;
	}

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.ProjectService#deleteProject(com.objetdirect.gwt.gen.shared.entities.Project)
	 */
	@Override
	public void deleteProject(Project projectToDelete) {
		checkLoggedIn();
		if (projectToDelete.getKey() != null) {
			projectDao.deleteProject(projectToDelete);
		}
	}
}
