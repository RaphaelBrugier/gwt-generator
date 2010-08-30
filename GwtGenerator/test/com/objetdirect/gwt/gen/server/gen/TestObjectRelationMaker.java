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
package com.objetdirect.gwt.gen.server.gen;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;
import com.objetdirect.seam.print.PrintEntityDescriptor;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestObjectRelationMaker {

	ObjectRelationMaker relationMaker;
	
	@Mock
	SeamGenerator seamGenerator;
	
	@Mock
	UMLObject umlObjectOwner;
	
	@Mock
	UMLObject umlObjectTarget;
	
	PrintEntityDescriptor ownerObject;
	
	EntityDescriptor targetObject;
	
	ObjectRelation objectRelation;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		relationMaker = new ObjectRelationMaker(seamGenerator);
		objectRelation = new ObjectRelation(umlObjectOwner, umlObjectTarget);
		when(umlObjectOwner.getClassName()).thenReturn("seam.print.PrintEntityDescriptor");
		when(umlObjectTarget.getClassName()).thenReturn("entities.EntityDescriptor");
	}
	
	@Test
	public void makeSetMethodFromAttributeName() throws Exception {
		// given 
		objectRelation.setRightRole("entity");
		
		// when
		String setMethodName = relationMaker.makeSetMethodFromRelation(objectRelation);
		
		// then
		assertEquals("setEntity", setMethodName);
	}
	
	@Test
	public void makeSetMethodFromClassName() throws Exception {
		// given 
		objectRelation.getRightObject().setInstantiatedClass(new UMLClass("entities.EntityDescriptor"));
		objectRelation.setRightRole("");
		
		// when
		String setMethodName = relationMaker.makeSetMethodFromRelation(objectRelation);
		
		// then
		assertEquals("setEntityDescriptor", setMethodName);
	}

	@Test
	public void setRelation() throws Exception {
		// given
		ownerObject = mock(PrintEntityDescriptor.class);
		targetObject = mock(EntityDescriptor.class);
		when(seamGenerator.getGenObjectCounterPartOf(umlObjectTarget)).thenReturn(targetObject);
		when(seamGenerator.getGenObjectCounterPartOf(umlObjectOwner)).thenReturn(ownerObject);
		
		objectRelation.setRightRole("entity");
		
		// when
		relationMaker.createRelationFromUml(objectRelation);

		// then
		verify(ownerObject).setEntity(targetObject);
	}
}
