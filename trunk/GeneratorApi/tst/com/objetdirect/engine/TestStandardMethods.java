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

public class TestStandardMethods extends TestCase {

	public void testSimpleGetterAndSetter() {
		ClassDescriptor client = new ClassDescriptor("com.objetdirect.domain", "Client");
		AttributeDescriptor id = new AttributeDescriptor();
		id.init(client, TypeDescriptor.rInt, "id");
		MethodDescriptor getId = StandardMethods.getter(id, "public");
		TestUtil.assertText(getId.getText(),
			"public int getId() {",
			"	return id;",
			"}"
		);
		MethodDescriptor setId = StandardMethods.setter(id, "public");
		TestUtil.assertText(setId.getText(),
			"public void setId(int id) {",
			"	this.id = id;",
			"}"
		);
	}
	
	public void testBooleanGetter() {
		ClassDescriptor client = new ClassDescriptor("com.objetdirect.domain", "Client");
		AttributeDescriptor visible = new AttributeDescriptor();
		visible.init(client, TypeDescriptor.rBoolean, "visible");
		MethodDescriptor isVisible = StandardMethods.getter(visible, "public");
		TestUtil.assertText(isVisible.getText(),
			"public boolean isVisible() {",
			"	return visible;",
			"}"
		);
		AttributeDescriptor opened = new AttributeDescriptor();
		opened.init(client, TypeDescriptor.Boolean, "opened");
		MethodDescriptor isOpened = StandardMethods.getter(opened, "public");
		TestUtil.assertText(isOpened.getText(),
			"public Boolean isOpened() {",
			"	return opened;",
			"}"
		);
	}
	
}
