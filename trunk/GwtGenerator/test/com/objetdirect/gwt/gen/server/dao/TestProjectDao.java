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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.objetdirect.gwt.gen.server.ServerHelper;
import com.objetdirect.gwt.gen.shared.entities.Directory;
import com.objetdirect.gwt.gen.shared.entities.Project;

/**
 * Tests for the project dao.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class TestProjectDao extends TestCase {

	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(
        		new LocalDatastoreServiceTestConfig(), 
        		new LocalUserServiceTestConfig());

	private final ProjectDao dao = new ProjectDao();
	
	private final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	
	protected void setUp() throws Exception {
		super.setUp();
		helper.setUp();
	}
	
	public void testUpdateProject() {
		helper	.setEnvIsLoggedIn(true)
				.setEnvEmail("MyEmail@gmail.com")
				.setEnvAuthDomain("google.com");
		
		Long id  = dao.createProject("name");
		
		List<Project> projects = new ArrayList<Project>(dao.getProjects());
		
		Project p = projects.get(0);
		
		assertEquals(id, p.getKey());
		
		p.setName("newName");
		p = dao.updateProject(p);
	
		assertEquals(id, p.getKey());
	}
	
	public void testDeleteProject() {
		helper	.setEnvIsLoggedIn(true)
		.setEnvEmail("MyEmail@gmail.com")
		.setEnvAuthDomain("google.com");
		
		Long id  = dao.createProject("name");
		
		List<Project> projects = new ArrayList<Project>(dao.getProjects());
		
		Project p = projects.get(0);
		
		assertEquals(id, p.getKey());
		
		dao.deleteProject(p);
		
		assertEquals(0, ds.prepare(new Query("Project")).countEntities());
	}
	
	public void testCreateDirectoryInProject() {
		helper	.setEnvIsLoggedIn(true)
		.setEnvEmail("MyEmail@gmail.com")
		.setEnvAuthDomain("google.com");
	
		//Given a new project
		Long id  = dao.createProject("name");
		List<Project> projects = new ArrayList<Project>(dao.getProjects());
		Project newProject = projects.get(0);
	
		//Given a new directory
		Directory newDirectory = new Directory("Dname", ServerHelper.getCurrentUser().getEmail());
		
		// Add the directory to the project and update the project.
		newProject.addDirectory(newDirectory);
		dao.updateProject(newProject);
		
		// Expect the directory to be saved
		assertEquals(1, ds.prepare(new Query("Directory")).countEntities());
		
		DirectoryDao directoryDao = new DirectoryDao();
		List<Directory> directories = new ArrayList<Directory>(directoryDao.getDirectories());
		Directory directoryFound = directories.get(0);
						
		projects = new ArrayList<Project>(dao.getProjects());
		newProject = projects.get(0);
		Directory directorySaveInProject = newProject.getDirectories().get(0);
		
		// Expect directory in the project and the one from the base to have the same key.
		assertEquals(directoryFound.getKey(), directorySaveInProject.getKey());
	}
}
