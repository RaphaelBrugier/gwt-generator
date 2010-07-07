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
package com.objetdirect.gwt.gen.server.gen.relationProcessors;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.objetdirect.gwt.gen.server.gen.SeamGenerator;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 *
 */
public abstract class TestRelationProcessor {

	@Mock
	SeamGenerator seamGenerator;

	@Mock(answer=Answers.RETURNS_DEEP_STUBS)
	ObjectRelation objectRelation;
	
	@Mock
	UMLObject umlObjectOwner;
	
	@Mock
	UMLObject umlObjectTarget;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		when(objectRelation.getLeftObject()).thenReturn(umlObjectOwner);
		when(objectRelation.getRightObject()).thenReturn(umlObjectTarget);
	}
	
	/**
	 * Convenient method the set :
	 *  - the counterPart gen object of the owner of the relation
	 *  - the counterPart gen object of the target of the relation
	 *  - the target role of the relation (the right role by convention)
	 * 
	 * @param owner
	 * @param target
	 * @param targetRole
	 */
	public void setReturnedGenObject(Object owner, Object target, String targetRole) {
		when(seamGenerator.getObjectGenCounterPartOf(umlObjectOwner)).thenReturn(owner);
		when(seamGenerator.getObjectGenCounterPartOf(umlObjectTarget)).thenReturn(target);
		when(objectRelation.getRightRole()).thenReturn(targetRole);
	}
	
	@After
	public void after() {
		verifyNoMoreInteractions(seamGenerator);
	}
}
