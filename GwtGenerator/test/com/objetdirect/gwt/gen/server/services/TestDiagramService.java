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

import static com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType.CLASS;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType.OBJECT;
import static org.mockito.Mockito.mock;
import junit.framework.TestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.objetdirect.gwt.gen.client.services.DiagramService;
import com.objetdirect.gwt.gen.client.services.ProjectService;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.entities.Directory;
import com.objetdirect.gwt.gen.shared.entities.Project;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvasClassDiagram;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvasObjectDiagram;
import com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType;

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
		Project p = projectService.getProjects().get(0);
		directory = p.getDirectories().get(0);
	}
	
	
	/**
	 * Helper method to check if all the fields of the given diagramDto are equals to the others parameters.
	 */
	private void assertEquals(String key, String name, DiagramType type, String directoryKey, DiagramDto diagramDto) {
		assertEquals(key, diagramDto.getKey());
		assertEquals(name, diagramDto.getName());
		assertEquals(type, diagramDto.getType());
		assertEquals(directoryKey, diagramDto.getDirectoryKey());
	}
	
	public void testCreateDiagram() {
		DiagramDto diagramDto = new DiagramDto(directory.getKey(),"name", CLASS);
		
		String id = diagramService.createDiagram(diagramDto);
		assertNotNull(id);
		
		diagramDto = new DiagramDto(directory.getKey(),"name2", OBJECT);
		id = diagramService.createDiagram(diagramDto);

		assertEquals(2, ds.prepare(new Query("Diagram")).countEntities());
	}
	
	
	public void testGetDiagramFromId() {
		DiagramDto diagramDto = new DiagramDto(directory.getKey(),"name", CLASS);
		String id = diagramService.createDiagram(diagramDto);

		DiagramDto dto = diagramService.getDiagram(id);
		assertEquals(CLASS, dto.getType() );
		assertEquals("name", dto.getName());
		assertEquals(directory.getKey(), dto.getDirectoryKey());
	}
	
	public void testDelete() throws Exception {
		String id = diagramService.createDiagram(new DiagramDto(directory.getKey(),"name", CLASS));
		
		diagramService.deleteDiagram(id);
		assertEquals(0, ds.prepare(new Query("Diagram")).countEntities());
	}
	
	public void testSaveDiagram() throws Exception {
		String id = diagramService.createDiagram(new DiagramDto(directory.getKey(),"name", CLASS));
		DiagramDto dto = diagramService.getDiagram(id);
		dto.setName("newName");
		
		diagramService.saveDiagram(dto);
		dto = diagramService.getDiagram(id);
		
		assertEquals(id, "newName", CLASS, directory.getKey(), dto);
	}
	
	public void test_getObjectDiagramAlsoSetupClassesFromClassDiagram() {
		UMLCanvasClassDiagram classDiagram = mock(UMLCanvasClassDiagram.class);
		DiagramDto classDiagramDto = new DiagramDto(directory.getKey(),"name", CLASS);
		classDiagramDto.setCanvas(classDiagram);

		String classDiagramKey = diagramService.createDiagram(classDiagramDto);
		
		DiagramDto objectDiagramDto = new DiagramDto(directory.getKey(),"objectDiagram", OBJECT);
		objectDiagramDto.classDiagramKey = classDiagramKey;
		UMLCanvasObjectDiagram objectDiagram = mock(UMLCanvasObjectDiagram.class);
		objectDiagramDto.setCanvas(objectDiagram);
		
		String objectDiagramKey = diagramService.createDiagram(objectDiagramDto);
	}
}
