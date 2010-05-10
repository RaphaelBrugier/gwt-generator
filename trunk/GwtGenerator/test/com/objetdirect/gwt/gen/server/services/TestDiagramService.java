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

import java.util.ArrayList;

import junit.framework.TestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.ProjectService;
import com.objetdirect.gwt.gen.server.services.DiagramServiceImpl;
import com.objetdirect.gwt.gen.server.services.ProjectServiceImpl;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto.Type;
import com.objetdirect.gwt.gen.shared.entities.Directory;
import com.objetdirect.gwt.gen.shared.entities.Project;

/**
 * Tests for the diagram dao.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class TestDiagramService extends TestCase {

	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig());

	private final DiagramService diagramService = new DiagramServiceImpl();
	
	private final ProjectService projectService = new ProjectServiceImpl(); 
	
	private final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	
	private Directory directory;
	
	protected void setUp() throws Exception {
		super.setUp();
		helper.setUp();
		helper.setEnvIsLoggedIn(true)
		  .setEnvEmail("MyEmail@gmail.com")
	      .setEnvAuthDomain("google.com");
		
		projectService.createProject("projectTest");
		Project p = projectService.getProjects().iterator().next();
		projectService.addDirectory(p, "dirTest");
		directory = projectService.getProjects().iterator().next().getDirectories().get(0);
		
	}

	
	public void testCreateDiagram() {
		Long id = diagramService.createDiagram(directory.getKey(),Type.CLASS, "name");
		assertNotNull(id);
		id = diagramService.createDiagram(directory.getKey(), Type.HYBRYD, "name 2");

		assertEquals(2, ds.prepare(new Query("Diagram")).countEntities());
	}
	
	
	public void testGetDiagramFromId() {
		Long id = diagramService.createDiagram(directory.getKey(), Type.CLASS, "name");

		DiagramDto dto = diagramService.getDiagram(id);
		assertEquals(Type.CLASS, dto.getType() );
		assertEquals("name", dto.getName());
		assertEquals(directory.getKey(), dto.getDirectoryKey());
	}
	
	public void testGetDiagrams() {
		Long id = diagramService.createDiagram(directory.getKey(),Type.CLASS, "name");
		diagramService.createDiagram(directory.getKey(), Type.HYBRYD, "name 2");

		ArrayList<DiagramDto> diagrams =  diagramService.getDiagrams();
		assertEquals(2, diagrams.size());

		DiagramDto dto = diagrams.get(0);
		assertEquals(Type.CLASS, dto.getType());
		assertEquals("name", dto.getName());
		assertEquals(id, dto.getKey());
		assertEquals(directory.getKey(), dto.getDirectoryKey());
	}
	
	public void testDelete() throws Exception {
		Long id = diagramService.createDiagram(directory.getKey(),Type.CLASS, "name");
		
		diagramService.deleteDiagram(id);
		assertEquals(0, ds.prepare(new Query("Diagram")).countEntities());
	}
	
	public void testSaveDiagram() throws Exception {
		Long id = diagramService.createDiagram(directory.getKey(),Type.CLASS, "name");
		DiagramDto dto = diagramService.getDiagram(id);
		dto.setName("newName");
		
		diagramService.saveDiagram(dto);
		dto = diagramService.getDiagram(id);
		
		assertEquals(Type.CLASS, dto.getType());
		assertEquals("newName", dto.getName());
		assertEquals(id, dto.getKey());
		assertEquals(directory.getKey(), dto.getDirectoryKey());
	}

}
