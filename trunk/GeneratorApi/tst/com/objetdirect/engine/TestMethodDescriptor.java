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

public class TestMethodDescriptor extends TestCase {

	public void testSimpleMethod() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "PageAnimator");
		MethodDescriptor method = new MethodDescriptor();
		method.init(owner, TypeDescriptor.List(TypeDescriptor.String), "getNames").
			setContent("return new ImplList();").
			replace("ImplList", TypeDescriptor.ArrayList(TypeDescriptor.String));
		TestUtil.assertText(method.getText(), 
			"List<String> getNames() {",
			"	return new ArrayList<String>();",
			"}"
		);
		TestUtil.assertText(owner.getImportSet().getText(), 
			"import java.util.ArrayList;",
			"import java.util.List;"
		);
	}
	
	public void testModifiers() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "PageAnimator");
		MethodDescriptor method = new MethodDescriptor();
		method.init(owner, TypeDescriptor.List(TypeDescriptor.String), "getNames").
			addModifier("public").
			addModifier("static").
			setContent("return new ImplList();").
			replace("ImplList", TypeDescriptor.ArrayList(TypeDescriptor.String));
		TestUtil.assertText(method.getText(), 
			"public static List<String> getNames() {",
			"	return new ArrayList<String>();",
			"}"
		);
		TestUtil.assertText(owner.getImportSet().getText(), 
			"import java.util.ArrayList;",
			"import java.util.List;"
		);
	}

	public void testAnnotations() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "PageAnimator");
		MethodDescriptor method = new MethodDescriptor();
		method.init(owner, TypeDescriptor.List(TypeDescriptor.String), "getNames").
			addAnnotation(TypeDescriptor.type("org.jboss.seam.annotation", "Transactional")).
			addAnnotation(TypeDescriptor.type("org.jboss.seam.annotation", "Role"), "\"guest\"").
			setContent("return new ImplList();").
			replace("ImplList", TypeDescriptor.ArrayList(TypeDescriptor.String));
		TestUtil.assertText(method.getText(),
			"@Transactional",
			"@Role(\"guest\")",
			"List<String> getNames() {",
			"	return new ArrayList<String>();",
			"}"
		);
		TestUtil.assertText(owner.getImportSet().getText(), 
			"import java.util.ArrayList;",
			"import java.util.List;",
			"import org.jboss.seam.annotation.Role;",
			"import org.jboss.seam.annotation.Transactional;"
		);
	}
	
	public void testParameter() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "PageAnimator");
		MethodDescriptor method = new MethodDescriptor();
		method.init(owner, TypeDescriptor.rVoid, "setNames").
			addParameter(TypeDescriptor.List(TypeDescriptor.String), "list").
			setContent("this.list = list;");
		TestUtil.assertText(method.getText(),
			"void setNames(List<String> list) {",
			"	this.list = list;",
			"}"
		);
		TestUtil.assertText(owner.getImportSet().getText(), 
			"import java.util.List;"
		);
		method = new MethodDescriptor();
		method.init(owner, TypeDescriptor.rVoid, "writeOnConsole").
			addParameter(TypeDescriptor.String, "label").
			addParameter(TypeDescriptor.String, "value").
			setContent(
				"System.out.println(label);",
				"System.out.println(value);"
			);
		TestUtil.assertText(method.getText(),
			"void writeOnConsole(String label, String value) {",
			"	System.out.println(label);",
			"	System.out.println(value);",
			"}"
		);
		method = new MethodDescriptor();
		method.init(owner, TypeDescriptor.rVoid, "writeOnConsole").
			addVarargsParameter(TypeDescriptor.String, "lines").
			setContent(
				"for (String line : lines) {",
				"	System.out.println(line);",
				"}"
			);
		TestUtil.assertText(method.getText(),
			"void writeOnConsole(String ... lines) {",
			"	for (String line : lines) {",
			"		System.out.println(line);",
			"	}",
			"}"
		);
	}
	
	public void testReplacements() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "ClientPageAnimator");
		MethodDescriptor method = new MethodDescriptor();
		method.init(owner, TypeDescriptor.rVoid, "setCurrentClient").
			addParameter(TypeDescriptor.type("com.objetdirect.domain", "Client"), "client").
			setContent(
				"currentClient = client;",
				"if (client!=null) {",
				"	// insert init here",
				"}"
			).insertLines("// insert init here",
				"paginator.setList(currentClient.getProducts());"
			).removeLine("// insert init here");
		TestUtil.assertText(method.getText(),
			"void setCurrentClient(Client client) {",
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
		method = new MethodDescriptor();
		method.init(owner, TypeDescriptor.rVoid, "setCurrentClient").
			addParameter(TypeDescriptor.type("com.objetdirect.domain", "Client"), "client").
			setContent(
				"currentObject = client;",
				"if (client!=null) {",
				"	paginator.setList(currentObject.getObjects());",
				"}").
			replace("currentObject", current).
			replace("paginator", paginator).
			replace("getObjects", getObjects);
		TestUtil.assertText(method.getText(),
			"void setCurrentClient(Client client) {",
			"	currentClient = client;",
			"	if (client!=null) {",
			"		clientPaginator.setList(currentClient.getProducts());",
			"	}",
			"}"
		);
	}
	
	public void testClear() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "ClientPageAnimator");
		MethodDescriptor method = new MethodDescriptor();
		method.init(owner, TypeDescriptor.rVoid, "setCurrentClient").
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
		TestUtil.assertText(method.getText(),
			"void setCurrentClient(Client client) {",
			"	currentClient = client;",
			"	access++;",
			"}");
	}
	
	public void testCreateMethodsWithSameName() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "ClientPageAnimator");
		MethodDescriptor method1 = new MethodDescriptor();
		method1.init(owner, TypeDescriptor.rVoid, "setCurrentClient");
		owner.addMethod(method1);
		MethodDescriptor method2 = new MethodDescriptor();
		method2.init(owner, TypeDescriptor.rVoid, "setCurrentClient");		
		assertEquals("setCurrentClient1", method2.getName());
	}
	
}
