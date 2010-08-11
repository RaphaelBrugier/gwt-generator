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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.objetdirect.gwt.gen.client.services.ProjectService;
import com.objetdirect.gwt.gen.server.ServerHelper;
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

	public static final String SEAM_CLASSES_SUPPORTED = "Seam Classes supported";

	private final ProjectDao projectDao = new ProjectDao();
	
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

		String email = ServerHelper.getCurrentUser().getEmail();
		
		Project persistedProject = projectDao.createProject(name, email);

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

		List<Project> projects = projectDao.getProjects(getCurrentUser().getEmail());

		for (Project project : projects) {
			List<Directory> directories = project.getDirectories();
			addDiagramsToDirectories(directories);
		}

		projects.add(getSeamProject());

		return projects;
	}

	/**
	 * @param directories
	 */
	private void addDiagramsToDirectories(List<Directory> directories) {
		for (Directory directory : directories) {
			ArrayList<DiagramDto> diagrams = diagramDao.getDiagrams(directory.getKey());
			directory.setDiagrams(diagrams);
		}
	}

	private Project getSeamProject() {
		Project seamProject = projectDao.getSeamProject();
		
		if (seamProject == null) {
			createSeamProject();
			seamProject = projectDao.getSeamProject();
		}
		
		addDiagramsToDirectories(seamProject.getDirectories());
		return seamProject;
	}
	
	private void createSeamProject() {
		Project seamProject = new Project(SEAM_CLASSES_SUPPORTED, null);
		seamProject.setSeamProject(true);
		seamProject = projectDao.createProject(seamProject);
		
		Directory seamDirectory = new Directory("Seam objects", null, DOMAIN);
		seamProject.addDirectory(seamDirectory);
		projectDao.updateProject(seamProject);
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
