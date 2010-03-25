/*
 * This file is part of the Gwt-Generator project and was written by Henri Darmet for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2009 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package com.objetdirect.engine;

import junit.framework.TestCase;

public class TestTypeSet extends TestCase {

	public void testMethodAdd() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.action", "ClientAnimator");
		TypeSet ts = cd.getInternalTypes();
		InternalTypeDescriptor agencySelector = 
			new InternalInterfaceDescriptor(cd, "AgencySelector");
		ts.addType(agencySelector);
		assertFalse(ts.accept("AgencySelector"));
		assertTrue(ts.accept("SkillSelector"));
		try {
			ts.addType(agencySelector);
			fail();
		}
		catch (GeneratorException e) {
			assertEquals("Name already used on class com.myapp.action.ClientAnimator : AgencySelector", e.getMessage());
		}
		InternalTypeDescriptor version1Selector = new InternalInterfaceDescriptor(cd, "Selector");
		InternalTypeDescriptor version2Selector = new InternalInterfaceDescriptor(cd, "Selector");
		ts.addType(version1Selector);
		try {
			ts.addType(version2Selector);
			fail();
		}
		catch (GeneratorException e) {
			assertEquals("Name already used on class com.myapp.action.ClientAnimator : Selector", e.getMessage());
		}
	}
	
	public void testGetText() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.action", "ClientAnimator");
		TypeSet ts = cd.getInternalTypes();
		InternalTypeDescriptor agencySelector = new InternalInterfaceDescriptor(cd, "AgencySelector");
		ts.addType(agencySelector);
		InternalTypeDescriptor skillSelector = new InternalInterfaceDescriptor(cd, "SkillSelector");
		ts.addType(skillSelector);
		TestUtil.assertText(ts.getText(), 
			"interface AgencySelector {",
			"",
			"}",
			"",
			"interface SkillSelector {",
			"",
			"}"
		);
	}
	
	public void testKeywordRecognition() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.action", "ClientAnimator");
		TypeSet ts = cd.getInternalTypes();
		assertFalse(ts.accept("class"));
		InternalTypeDescriptor selector = new InternalInterfaceDescriptor(cd, "instanceof");
		ts.addType(selector);
		assertEquals("Instanceof", selector.getTypeName());
	}
	
}
