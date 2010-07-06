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

import static com.objetdirect.seam.TestPrintEntity.testSimpleEntityFaceletText;
import static com.objetdirect.seam.TestPrintEntity.testSimpleEntityJavaText;

import org.junit.Test;

import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 *
 */
public class TestPrintEntity extends TestSeamGenerator {

	@Test
	public void testSimpleEntity() {
		UMLObject printDescriptorInstance =  new UMLObject("", "PrintDescriptor").
			addAttributeValuePair("classPackageName", "com.objetdirect.actions").
			addAttributeValuePair("className", "PrintAgencies").
			addAttributeValuePair("viewPackageName", "views").
			addAttributeValuePair("viewName", "print-agencies");
		objects.add(printDescriptorInstance);
		
		UMLObject printEntityInstance = new UMLObject("", "PrintEntity");
		objects.add(printEntityInstance);
		
		ObjectRelation featureRelation = new ObjectRelation(printDescriptorInstance, printEntityInstance).setRightRole("feature");
		objectRelations.add(featureRelation);
		
		UMLObject entityInstance = new UMLObject("", "Agency");
		ObjectRelation entityRelation = new ObjectRelation(printEntityInstance, entityInstance).setRightRole("entity");
		objectRelations.add(entityRelation);
		
		UMLObject printFormInstance = new UMLObject("", "PrintForm");
		
		ObjectRelation elementRelation = new ObjectRelation(printEntityInstance, printFormInstance).setRightRole("element");
		objectRelations.add(elementRelation);
		
		createStringFieldInstance(printFormInstance, "name", "Name", "20");
		
		createStringFieldInstance(printFormInstance, "phone", "Phone", "10");
		
		createStringFieldInstance(printFormInstance, "email", "Email", "20");
		
		assertGenerated(testSimpleEntityJavaText, testSimpleEntityFaceletText);
	}
}
