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

package com.objetdirect.seam;

import com.objetdirect.engine.ClassDescriptor;

import junit.framework.TestCase;

public class TestSeam extends TestCase {
	
	public void testSeamTypes() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "PageAnimator");
		assertEquals("org.jboss.seam.core.Conversation", Seam.Conversation.getUsageName(owner));
		assertEquals("org.jboss.seam.annotations.Scope", Seam.Scope.getUsageName(owner));
		assertEquals("org.jboss.seam.ScopeType", Seam.ScopeType.getUsageName(owner));
	}

	public void testSeamNameMaker() {
		assertEquals(Seam.getName(null, "EditAgency"), "editAgency");
		assertEquals(Seam.getName(null, "EditAgency"), "editAgency1");
		assertEquals(Seam.getName("view", "EditAgency"), "viewEditAgency");
		assertEquals(Seam.getName("view", "EditAgency"), "viewEditAgency1");
		assertEquals(Seam.getName("portal/view", "EditAgency"), "portalViewEditAgency");
		assertEquals(Seam.getName("portal/view", "EditAgency"), "portalViewEditAgency1");

		assertEquals(Seam.getName("portal/view", "EditProduct"), "editProduct");
		assertEquals(Seam.getName("view", "EditProduct"), "viewEditProduct");
		assertEquals(Seam.getName("", "EditProduct"), "editProduct1");
		assertEquals(Seam.getName(null, "EditProduct"), "editProduct2");
	}
	
}
