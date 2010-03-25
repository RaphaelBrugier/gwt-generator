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

public class TestInternalTypesDescriptor extends TestCase {

	public void testEmptyInterface() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "SelectAnimator");
		InternalInterfaceDescriptor interf = 
			new InternalInterfaceDescriptor(owner, "Selector");
		TestUtil.assertText(interf.getText(), 
			"interface Selector {",
			"",
			"}"
		);
		interf = new InternalInterfaceDescriptor(owner, "AgencySelector")
			.addModifier("public")
			.addAnnotation(TypeDescriptor.type("com.objetdirect.frmk", "SelectInterface"));
		TestUtil.assertText(interf.getText(),
			"@SelectInterface",
			"public interface AgencySelector {",
			"",
			"}"
		);
		TestUtil.assertText(owner.getImportSet().getText(), 
			"import com.objetdirect.frmk.SelectInterface;"
		);
		interf = new InternalInterfaceDescriptor(owner, "EmployeeSelector")
			.addModifier("public")
			.addAnnotation(TypeDescriptor.type("com.objetdirect.frmk", "SelectInterface"), "asynchronous=true");
		TestUtil.assertText(interf.getText(),
			"@SelectInterface(asynchronous=true)",
			"public interface EmployeeSelector {",
			"",
			"}"
		);
	}
	
	public void testSimpleInterface() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "SelectAnimator");
		ClassDescriptor agency = new ClassDescriptor("com.objetdirect.domain", "Agency");
		InternalInterfaceDescriptor selector = 
			new InternalInterfaceDescriptor(owner, "Selector")
				.addModifier("public");
		SignatureDescriptor meth = new SignatureDescriptor();
		meth.init(selector, TypeDescriptor.rVoid, "select").addParameter(agency, "agency");
		selector.addMethod(meth);
		TestUtil.assertText(selector.getText(),
			"public interface Selector {",
			"",
			"	void select(Agency agency);",
			"}"
		);
	}

	public void testStandardInterface() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "SelectAnimator");
		ClassDescriptor agency = new ClassDescriptor("com.objetdirect.domain", "Agency");
		ClassDescriptor asynchronous = new ClassDescriptor("com.objetdirect.frmk", "Asynchronous");
		InternalInterfaceDescriptor selector = 
			new InternalInterfaceDescriptor(owner, "Selector")
				.addModifier("public");
		SignatureDescriptor methSelectOne = new SignatureDescriptor();
		methSelectOne.init(selector, TypeDescriptor.rVoid, "selectOne")
			.addParameter(agency, "agency");
		selector.addMethod(methSelectOne);
		SignatureDescriptor methSelectMany = new SignatureDescriptor();
		methSelectMany.init(selector, TypeDescriptor.rVoid, "selectMany")
			.addParameter(TypeDescriptor.rInt, "size")
			.addVarargsParameter(agency, "agencies")
			.addAnnotation(asynchronous, "timeout=3");
		selector.addMethod(methSelectMany);
		TestUtil.assertText(selector.getText(),
			"public interface Selector {",
			"",
			"	void selectOne(Agency agency);",
			"	",
			"	@Asynchronous(timeout=3)",
			"	void selectMany(int size, Agency ... agencies);",
			"}"
		);
	}
	
	public void testInternalClass() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.actions", "EmployeeAnimator");
		ClassDescriptor agency = new ClassDescriptor("com.objetdirect.domain", "Agency");
		InternalClassDescriptor selector = 
			new InternalClassDescriptor(owner, "AgencySelector");
		MethodDescriptor caller = new MethodDescriptor();
		caller.init(selector, agency, "selectAgency");
		caller.setContent("return null;");
		selector.addMethod(caller);
		TestUtil.assertText(selector.getText(),
			"static class AgencySelector {",
			"",
			"",
			"",
			"	Agency selectAgency() {",
			"		return null;",
			"	}",
			"",
			"}"
		);
	}
	
}
