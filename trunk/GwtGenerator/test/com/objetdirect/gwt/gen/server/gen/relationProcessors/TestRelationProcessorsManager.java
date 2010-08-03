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

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.objetdirect.gwt.gen.server.gen.SeamGenerator;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestRelationProcessorsManager {

	@Mock
	SeamGenerator seamGenerator;
	
	@Mock(answer=Answers.RETURNS_DEEP_STUBS)
	ObjectRelation objectRelation;
	
	@Mock
	UMLObject umlObjectOwner;
	
	@Mock
	UMLObject umlObjectTarget;
	
	RelationProcessorsManager manager;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		when(objectRelation.getLeftObject()).thenReturn(umlObjectOwner);
		when(objectRelation.getRightObject()).thenReturn(umlObjectTarget);
		manager = new RelationProcessorsManager(seamGenerator);
	}
	
	private void setRelationClassNames(String fromClassName, String targetClassName) {
		when(umlObjectOwner.getClassName()).thenReturn(fromClassName);
		when(umlObjectTarget.getClassName()).thenReturn(targetClassName);
	}
	
	private void assertInstanceOf(RelationProcessor<?,?> rp, Class<? extends RelationProcessor<?,?>> clazz) {
		assertNotNull("The relation processors manager returned no processor.", rp);
		assertTrue(
			"The relation processor returned is not the one expected, returned : " + rp.getClass().getSimpleName() + " expected : " + clazz.getSimpleName(),
			rp.getClass().equals(clazz));
	}
	
	@Test
	public void getNoProcessor() {
		setRelationClassNames("Fake", "PrintForm");
		
		RelationProcessor<?,?> rp = manager.getRelationProcessor(objectRelation);
		assertNull("Was expected to found no processor and returned null", rp);
	}
	

	@Test
	public void getPrintDescriptorToPrintEntity() {
		setRelationClassNames("PrintDescriptor", "PrintEntity");
		
		RelationProcessor<?,?> rp = manager.getRelationProcessor(objectRelation);
		assertInstanceOf(rp, PrintDescriptorToDocumentFeature.class);
	}
	
	@Test
	public void getPrintEntityToPrintForm() {
		setRelationClassNames("PrintEntity", "PrintForm");
		
		RelationProcessor<?,?> rp = manager.getRelationProcessor(objectRelation);
		assertInstanceOf(rp, PrintEntityToPrintElement.class);
	}
	
	@Test
	public void getPrintFormToStringField() {
		setRelationClassNames("PrintForm", "StringField");
		
		RelationProcessor<?,?> rp = manager.getRelationProcessor(objectRelation);
		assertInstanceOf(rp, PrintFormToStringField.class);
	}
	
}
