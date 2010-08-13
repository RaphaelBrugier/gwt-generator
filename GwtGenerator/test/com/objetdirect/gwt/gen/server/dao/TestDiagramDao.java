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


import static com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType.CLASS;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.objetdirect.gwt.gen.server.entities.Diagram;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestDiagramDao {

	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig());
	
	DiagramDao diagramDao;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		helper.setUp();
		helper.setEnvIsLoggedIn(true)
		  .setEnvEmail("MyEmail@gmail.com")
	      .setEnvAuthDomain("google.com");
		
		diagramDao = new DiagramDao();
	}
	
	@Test
	public void getAllSeamDiagrams() throws Exception {
		// given
		Diagram diagram = new Diagram(null, CLASS, "diagram", null);
		diagram.setSeamDiagram(false);
		diagramDao.createDiagram(diagram);
		
		Diagram seamDiagram1 = new Diagram(null, CLASS, "diagram1", null);
		seamDiagram1.setSeamDiagram(true);
		diagramDao.createDiagram(seamDiagram1);
		
		Diagram seamDiagram2 = new Diagram(null, CLASS, "diagram2", null);
		seamDiagram2.setSeamDiagram(true);
		diagramDao.createDiagram(seamDiagram2);
		
		// when
		Collection<Diagram> seamDiagrams = diagramDao.getSeamDiagrams();

		// then
		assertEquals(2, seamDiagrams.size());
	}

}
