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

public class TestConstructorSet extends TestCase {

	public void testGetText() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.domain", "Client");
		ConstructorSet cs = cd.getConstructors();
		ConstructorDescriptor constructor1 = new ConstructorDescriptor();
		constructor1.init(cd).addModifier("public");
		cs.addConstructor(constructor1);
		ConstructorDescriptor constructor2 = new ConstructorDescriptor();
		constructor2.init(cd).
			setContent("this.name = name;").
			addModifier("public").
			addParameter(TypeDescriptor.String, "name");
		cs.addConstructor(constructor2);
		TestUtil.assertText(cs.getText(), 
			"public Client() {",
			"}",
			"",
			"public Client(String name) {",
			"	this.name = name;",
			"}"
		);
	}
	
}
