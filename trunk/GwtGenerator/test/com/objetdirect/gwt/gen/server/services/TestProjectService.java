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


import static org.junit.Assert.*;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.entities.Directory;
import com.objetdirect.gwt.gen.shared.entities.Project;
import com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType;

/**
 * Tests for the project services.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class TestProjectService {

	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(
		new LocalDatastoreServiceTestConfig(), 
		new LocalUserServiceTestConfig());

	private final ProjectServiceImpl projectService = new ProjectServiceImpl();
	
	private final DiagramServiceImpl diagramService = new DiagramServiceImpl();
	
	
	private final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	
	private final static String EMAIL = "MyEmail@gmail.com";
	
	private final static int NumberOfDefaultDirectories = 2;
	
	@Before
	public void setUp() throws Exception {
		helper.setUp();
		helper.setEnvIsLoggedIn(true)
			  .setEnvEmail(EMAIL)
			  .setEnvIsAdmin(false)
		      .setEnvAuthDomain("google.com");
	}
	
	/**
	 * Helper method to get a property of an object based on its id.
	 * @param id
	 * @param property
	 * @return
	 */
	@SuppressWarnings("unused")
	private Object getProjectProperty(Long id, String property) {
		Key key = new KeyFactory.Builder("Project", id).getKey();
		Entity entity = null;
		try {
			entity = ds.get(key);
		} catch (EntityNotFoundException e) {
			TestCase.fail();
		}
		
		return entity.getProperty(property);
	}
	
	@Test
	public void testCreateProject() throws Exception {
		final Long id  = projectService.createProject("name");
		
		// Check if the returned id gets an entity from the datastore
		Key key = new KeyFactory.Builder("Project", id).getKey();
		ds.get(key);
	}
	
	@Test
	public void createAProject_AlsoCreateDefaultDirectories() {
		projectService.createProject("name");
		
		assertEquals(NumberOfDefaultDirectories, ds.prepare(new Query("Directory")).countEntities());
	}
	
	@Test
	public void getProject_return_projects_and_SeamProject() {
		Long id  = projectService.createProject("name");
		projectService.createProject("name2");
		
		List<Project> projects = projectService.getProjects();
		
		assertEquals(3, projects.size());
		
		Project p = projects.get(0);
		assertEquals(id, p.getKey());
		assertEquals("name", p.getName());
		assertEquals(EMAIL, p.getEmail());
		assertEquals(NumberOfDefaultDirectories, p.getDirectories().size());
	}
	
	@Test
	public void testGetProjectsWithADiagram() throws Exception {
		// Create a new project and create a new diagram in the first directory
		projectService.createProject("name");
		Project p = projectService.getProjects().get(0);
		Directory directory = p.getDirectories().get(0);
		String directoryKey = directory.getKey();
		String diagramKey = diagramService.createDiagram(new DiagramDto(directoryKey,"diagram", DiagramType.CLASS));
		
		// Assert that getting the project also gets the diagram in the directory.
		p = projectService.getProjects().get(0);
		directory = p.getDirectories().get(0);
		assertEquals(1, directory.getDiagrams().size());
		
		DiagramDto diagramFound = directory.getDiagrams().get(0);
		
		assertEquals(diagramKey, diagramFound.getKey());
	}

	@Test
	public void testDeleteProject() {
		Long id  = projectService.createProject("name");
		assertEquals(1, ds.prepare(new Query("Project")).countEntities());
		Project p = projectService.getProjects().get(0);
		
		assertEquals(id, p.getKey());
		
		assertEquals(2, ds.prepare(new Query("Project")).countEntities());
		
		projectService.deleteProject(p);
		
		assertEquals(1, ds.prepare(new Query("Project")).countEntities());
	}
	
	@Test
	public void testDeleteProjectAlsoDeleteDirectories() {
		Long id  = projectService.createProject("name");
		Project p = projectService.getProjects().get(0);
		
		assertEquals(id, p.getKey());
		
		projectService.deleteProject(p);
		
		assertEquals(0, ds.prepare(new Query("Directory")).countEntities());
	}
	
	@Test
	public void testDeleteProjectAlsoDeleteDiagrams() {
		projectService.createProject("name");
		Project p = projectService.getProjects().get(0);
		String directoryKey = p.getDirectories().get(0).getKey();
		diagramService.createDiagram(new DiagramDto(directoryKey,"diagramName", DiagramType.CLASS));
		
		projectService.deleteProject(p);
		
		assertEquals(0, ds.prepare(new Query("Diagram")).countEntities());
	}
}
