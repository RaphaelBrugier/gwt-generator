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

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.objetdirect.gwt.gen.server.ServerHelper;
import com.objetdirect.gwt.gen.server.entities.SeamDiagram;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class SeamDiagramDaoTest {

	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(
		new LocalDatastoreServiceTestConfig(), 
		new LocalUserServiceTestConfig());
	
	SeamDiagramDao dao;
	
	@Before
	public void setup(){
		helper.setUp();
		dao = new SeamDiagramDao();
	}
	
	@Test
	public void createSeamDiagram() {
		String id = dao.createSeamDiagram().getKey();
		
		SeamDiagram seamDiagram = ServerHelper.getPM().getObjectById(SeamDiagram.class, id);
		assertNotNull(seamDiagram);
	}
	
	@Test
	public void getSeamDiagram_returnNull_IfNotAlreadyCreated() {
		SeamDiagram seamDiagram = dao.getSeamDiagram();
		assertNull(seamDiagram);
	}
	
	@Test
	public void getSeamDiagram_returnTheSeamDiagram_WhenCreated() {
		dao.createSeamDiagram();
		SeamDiagram seamDiagram = dao.getSeamDiagram();
		
		assertNotNull(seamDiagram);
	}
	
	@Test
	public void deleteSeamDiagram() {
		dao.deleteSeamDiagram();
		SeamDiagram seamDiagram = dao.getSeamDiagram();
		assertNull(seamDiagram);
	}
	
	@Test
	public void deleteSeamDiagram_WhenDiagramWasCreated() {
		dao.createSeamDiagram();
		SeamDiagram seamDiagram = dao.getSeamDiagram();
		assertNotNull(seamDiagram);
		dao.deleteSeamDiagram();
		seamDiagram = dao.getSeamDiagram();
		assertNull(seamDiagram);
	}
	
	@Test
	public void updateCanvasInSeamDiagram() {
		dao.createSeamDiagram();

		UMLCanvas canvas = mock(UMLCanvas.class);
		
		dao.updateCanvasInSeamDiagram(canvas);
		SeamDiagram seamDiagram = dao.getSeamDiagram();
		
		assertNotNull(seamDiagram.getCanvas());
	}
}
