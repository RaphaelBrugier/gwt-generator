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

import junit.framework.TestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto.Type;

/**
 * test for the diagram dao.
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com >
 */
public class TestDiagramDao extends TestCase {

	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig());

	private final DiagramDao dao = new DiagramDao();
	
	private final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	
	protected void setUp() throws Exception {
		super.setUp();
		helper.setUp();
	}

	
	public void testCreateDiagram() {
		Long id = dao.createDiagram(Type.CLASS, "name");
		assertNotNull(id);
		id = dao.createDiagram(Type.HYBRYD, "name 2");

		assertEquals(2, ds.prepare(new Query("Diagram")).countEntities());
	}
	
	
	public void testGetDiagramFromNameAndType() {
		Long id = dao.createDiagram(Type.CLASS, "name");

		DiagramDto dto = dao.getDiagram(Type.CLASS, "name");
		assertEquals(Type.CLASS, dto.getType() );
		assertEquals("name", dto.getName());
	}
}
