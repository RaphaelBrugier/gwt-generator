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

import static com.objetdirect.seam.TestPageDescriptor.testSimplePageFaceletText;
import static com.objetdirect.seam.TestPageDescriptor.testSimplePageJavaText;

import org.junit.Test;

import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestPageDescriptor extends TestSeamGenerator {

	public static final String PAGE_DESCRIPTOR_CLASSNAME = "PageDescriptor";

	@Test
	public void testGenerateSimplePageCode() throws Exception {
		UMLClass clazz = new UMLClass(PAGE_DESCRIPTOR_CLASSNAME);
		
		UMLObject pageDescriptorObject =  new UMLObject("", clazz).
			addAttributeValuePair("classPackageName", "com.objetdirect.actions").
			addAttributeValuePair("className", "EditAgencies").
			addAttributeValuePair("viewPackageName", "views").
			addAttributeValuePair("viewName", "edit-agencies");
		
		objects.add(pageDescriptorObject);
		
		assertGenerated(testSimplePageJavaText, testSimplePageFaceletText);
	}
}
