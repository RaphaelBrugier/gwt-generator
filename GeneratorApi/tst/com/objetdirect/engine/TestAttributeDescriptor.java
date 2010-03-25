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

public class TestAttributeDescriptor extends TestCase {

	public void testSimpleAttribute() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.engine", "Processor");
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(owner, TypeDescriptor.List(TypeDescriptor.String), "names");
		TestUtil.assertText(attr.getText(), "List<String> names;");
		attr = new AttributeDescriptor();
		attr.init(owner, TypeDescriptor.List(TypeDescriptor.String), "names").initToNull();
		TestUtil.assertText(attr.getText(), "List<String> names = null;");
		attr = new AttributeDescriptor();
		attr.init(owner, TypeDescriptor.List(TypeDescriptor.String), "names").
			initWithNew(TypeDescriptor.ArrayList(TypeDescriptor.String));
		TestUtil.assertText(attr.getText(), "List<String> names = new ArrayList<String>();");
		attr = new AttributeDescriptor();
		attr.init(owner, TypeDescriptor.rInt, "count").initWithPattern("0");
		TestUtil.assertText(attr.getText(), "int count = 0;");
		attr = new AttributeDescriptor();
		attr.init(owner, TypeDescriptor.array(TypeDescriptor.rInt), "counts").
			initWithPattern("{1, 2, 3}");
		TestUtil.assertText(attr.getText(), "int[] counts = {1, 2, 3};");
	}
	
	public void testInitializationWithNew() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.engine", "Processor");
		TypeDescriptor client = TypeDescriptor.type("com.objetdirect.domain", "Client");
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(owner, client, "current").
			initWithNew(client, "\"first name\"", "\"last name\"");
		TestUtil.assertText(attr.getText(), "Client current = new Client(\"first name\", \"last name\");");
	}
	
	public void testAnnotations() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.engine", "Processor");
		TypeDescriptor em = TypeDescriptor.type("javax.persistence", "EntityManager");
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(owner, em, "entityManager").
			addAnnotation(TypeDescriptor.type("org.jboss.seam.annotations", "In"));
		TestUtil.assertText(attr.getText(), 
			"@In", 
			"EntityManager entityManager;");
		TypeDescriptor client = TypeDescriptor.type("com.objetdirect.domain", "Client");
		attr = new AttributeDescriptor();
		attr.init(owner, client, "client").
			addAnnotation(TypeDescriptor.type("javax.persistence", "ManyToOne")).
			addAnnotation(TypeDescriptor.type("javax.persistence", "JoinColumn"), "name=\"client_id\"");
		TestUtil.assertText(attr.getText(), 
			"@ManyToOne", 
			"@JoinColumn(name=\"client_id\")", 
			"Client client;");
	}
	
	public void testReplacements() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.engine", "Processor");
		TypeDescriptor app = TypeDescriptor.type("javax.faces", "Application");
		TypeDescriptor fc = TypeDescriptor.type("javax.faces", "FacesContext");
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(owner, app, "application").
			initWithPattern("Singleton.getInstance().method()").
			replace("Singleton", fc).
			replace("method", "getApplication");
		TestUtil.assertText(attr.getText(), 
			"Application application = FacesContext.getInstance().getApplication();");
		TestUtil.assertText(owner.getImportSet().getText(), 
			"import javax.faces.Application;",
			"import javax.faces.FacesContext;");
		ClassDescriptor init = new ClassDescriptor("com.objetdirect.engine", "Initializator");
		AttributeDescriptor attr1 = new AttributeDescriptor();
		attr1.init(owner, init, "initSet");
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(init, TypeDescriptor.rInt, "getFittedValue");
		AttributeDescriptor attr2 = new AttributeDescriptor();
		attr2.init(owner, TypeDescriptor.rInt, "value")
			.initWithPattern("obj.getValue()")
			.replace("obj", attr1)
			.replace("getValue", meth);
		TestUtil.assertText(attr2.getText(), 
			"int value = initSet.getFittedValue();");
	}
	
	public void testModifier() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.engine", "Processor");
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(owner, TypeDescriptor.rInt, "SIZE")
			.initWithPattern("20");
		attr.addModifier("public")
			.addModifier("static final");
		TestUtil.assertText(attr.getText(), 
			"public static final int SIZE = 20;");
	}
	
}
