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

public class TestConstructorDescriptor extends TestCase {

	public void testSimpleConstructor() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "PageAnimator");
		ConstructorDescriptor constructor = new ConstructorDescriptor();
		constructor.init(owner).
			setContent("list = new ImplList();").
			replace("ImplList", TypeDescriptor.ArrayList(TypeDescriptor.String));
		TestUtil.assertText(constructor.getText(), 
			"PageAnimator() {",
			"	list = new ArrayList<String>();",
			"}"
		);
		TestUtil.assertText(owner.getImportSet().getText(), 
			"import java.util.ArrayList;"
		);
	}
	
	public void testModifiers() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "PageAnimator");
		ConstructorDescriptor constructor = new ConstructorDescriptor();
		constructor.init(owner).
			addModifier("public").
			setContent("list = new ImplList();").
			replace("ImplList", TypeDescriptor.ArrayList(TypeDescriptor.String));
		TestUtil.assertText(constructor.getText(), 
			"public PageAnimator() {",
			"	list = new ArrayList<String>();",
			"}"
		);
		TestUtil.assertText(owner.getImportSet().getText(), 
			"import java.util.ArrayList;"
		);
	}

	public void testAnnotations() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "PageAnimator");
		ConstructorDescriptor constructor = new ConstructorDescriptor();
		constructor.init(owner).
			addAnnotation(TypeDescriptor.type("com.objetdirect.frmk", "PersistentBuilder")).
			addAnnotation(TypeDescriptor.type("org.jboss.seam.annotation", "Role"), "\"guest\"").
			setContent("list = new ImplList();").
			replace("ImplList", TypeDescriptor.ArrayList(TypeDescriptor.String));
		TestUtil.assertText(constructor.getText(),
			"@PersistentBuilder",
			"@Role(\"guest\")",
			"PageAnimator() {",
			"	list = new ArrayList<String>();",
			"}"
		);
		TestUtil.assertText(owner.getImportSet().getText(), 
			"import com.objetdirect.frmk.PersistentBuilder;",
			"import java.util.ArrayList;",
			"import org.jboss.seam.annotation.Role;"
		);
	}
	
	public void testParameter() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "PageAnimator");
		ConstructorDescriptor constructor = new ConstructorDescriptor();
		constructor.init(owner).
			addParameter(TypeDescriptor.List(TypeDescriptor.String), "list").
			setContent("this.list = list;");
		TestUtil.assertText(constructor.getText(),
			"PageAnimator(List<String> list) {",
			"	this.list = list;",
			"}"
		);
		TestUtil.assertText(owner.getImportSet().getText(), 
			"import java.util.List;"
		);
		constructor = new ConstructorDescriptor();
		constructor.init(owner).
			addParameter(TypeDescriptor.String, "label").
			addParameter(TypeDescriptor.String, "value").
			setContent(
				"this.label = label;",
				"this.value = value;"
			);
		TestUtil.assertText(constructor.getText(),
			"PageAnimator(String label, String value) {",
			"	this.label = label;",
			"	this.value = value;",
			"}"
		);
		constructor = new ConstructorDescriptor();
		constructor.init(owner).
			addVarargsParameter(TypeDescriptor.String, "lines").
			setContent(
				"this.text = lines;"
			);
		TestUtil.assertText(constructor.getText(),
			"PageAnimator(String ... lines) {",
			"	this.text = lines;",
			"}"
		);
	}
	
	public void testReplacements() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "ClientPageAnimator");
		ConstructorDescriptor constructor = new ConstructorDescriptor();
		constructor.init(owner).
			addParameter(TypeDescriptor.type("com.objetdirect.domain", "Client"), "client").
			setContent(
				"currentClient = client;",
				"if (client!=null) {",
				"	/// insert init here",
				"}"
			).insertLines("/// insert init here",
				"paginator.setList(currentClient.getProducts());"
			).removeLine("/// insert init here");
		TestUtil.assertText(constructor.getText(),
			"ClientPageAnimator(Client client) {",
			"	currentClient = client;",
			"	if (client!=null) {",
			"		paginator.setList(currentClient.getProducts());",
			"	}",
			"}"
		);
		AttributeDescriptor paginator = new AttributeDescriptor();
		paginator.init(owner, TypeDescriptor.type("com.objetdirect.frmk", "SimplePaginator"), "clientPaginator");
		ClassDescriptor clientClass = new ClassDescriptor("com.objetdirect.actions", "Client");
		MethodDescriptor getObjects = new MethodDescriptor();
		getObjects.init(clientClass, TypeDescriptor.List(clientClass), "getProducts");
		AttributeDescriptor current = new AttributeDescriptor();
		current.init(owner, clientClass, "currentClient");
		constructor = new ConstructorDescriptor();
		constructor.init(owner).
			addParameter(TypeDescriptor.type("com.objetdirect.domain", "Client"), "client").
			setContent(
				"currentObject = client;",
				"if (client!=null) {",
				"	paginator.setList(currentObject.getObjects());",
				"}").
			replace("currentObject", current).
			replace("paginator", paginator).
			replace("getObjects", getObjects);
		TestUtil.assertText(constructor.getText(),
			"ClientPageAnimator(Client client) {",
			"	currentClient = client;",
			"	if (client!=null) {",
			"		clientPaginator.setList(currentClient.getProducts());",
			"	}",
			"}"
		);
	}
	
	public void testClear() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "ClientPageAnimator");
		ConstructorDescriptor constructor = new ConstructorDescriptor();
		constructor.init(owner).
			addParameter(TypeDescriptor.type("com.objetdirect.domain", "Client"), "client").
			setContent(
				"currentClient = client;",
				"if (client!=null) {",
				"	// insert init here",
				"}",
				"access++;"
			).
			removeLines(
				"if (client!=null) {",
				"	// insert init here",
				"}"
			);
		TestUtil.assertText(constructor.getText(),
			"ClientPageAnimator(Client client) {",
			"	currentClient = client;",
			"	access++;",
			"}");
	}
}
