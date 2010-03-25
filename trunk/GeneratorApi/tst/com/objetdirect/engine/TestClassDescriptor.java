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

public class TestClassDescriptor extends TestCase {

	public void testEmptyClass() {
		ClassDescriptor cd = new ClassDescriptor("com.objetdirect.engine", "Engine");
		TestUtil.assertText(cd.getText(),
			"package com.objetdirect.engine;",
			"",
			"",
			"public class Engine {",
			"",
			"",
			"",
			"",
			"}"
		);
	}
	
	public void testSuperClassAndInterfaces() {
		ClassDescriptor cd = new ClassDescriptor("com.objetdirect.engine", "Engine").
			setSuperClass(TypeDescriptor.type("com.objetdirect.frmk", "Registrable")).
			addInterface(TypeDescriptor.Serializable);
		TestUtil.assertText(cd.getText(),
			"package com.objetdirect.engine;",
			"",
			"import com.objetdirect.frmk.Registrable;",
			"import java.io.Serializable;",
			"",
			"public class Engine extends Registrable implements Serializable {",
			"",
			"",
			"",
			"",
			"}"
		);
	}
	
	public void testAnnotations() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.domain", "Client").
			addAnnotation(TypeDescriptor.type("javax.persistence", "Entity")).
			addAnnotation(TypeDescriptor.type("javax.persistence", "Table"), "name=\"TClient\"");
		cd.addAnnotation(TypeDescriptor.type("com.objetdirect.frmk", "Reference"));
		cd.addAnnotationParam(TypeDescriptor.type("com.objetdirect.frmk", "Reference"),"true");
		TestUtil.assertText(cd.getText(),
			"package com.myapp.domain;",
			"",
			"import com.objetdirect.frmk.Reference;",
			"import javax.persistence.Entity;",
			"import javax.persistence.Table;",
			"",
			"@Entity",
			"@Table(name=\"TClient\")",
			"@Reference(true)",
			"public class Client {",
			"",
			"",
			"",
			"",
			"}"
		);
		cd = new ClassDescriptor("com.myapp.views", "ClientAnimator").
			addAnnotation(TypeDescriptor.type("org.jboss.seam.annotations", "Name"), "\"clientAnimator\"").
			addAnnotation(TypeDescriptor.type("org.jboss.seam.annotations", "Scope"));
		cd.addAnnotationParam(TypeDescriptor.type("org.jboss.seam.annotations", "Scope"), "ScopeTypeClass.CONVERSATION", 
				TypeDescriptor.type("org.jboss.seam", "ScopeType"), "ScopeTypeClass");
		TestUtil.assertText(cd.getText(),
			"package com.myapp.views;",
			"",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"clientAnimator\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class ClientAnimator {",
			"",
			"",
			"",
			"",
			"}"
		);
	}

	public void testSimpleAttributes() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.domain", "Client");
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(cd, TypeDescriptor.rInt, "id"); 
		cd.addAttribute(attr);
		attr = new AttributeDescriptor();
		attr.init(cd, TypeDescriptor.rInt, "version");
		cd.addAttribute(attr);
		TestUtil.assertText(cd.getText(),
			"package com.myapp.domain;",
			"",
			"",
			"public class Client {",
			"",
			"	int id;",
			"	int version;",
			"",
			"",
			"",
			"}"
		);
	}
	
	public void testSimpleConstructor() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.domain", "Client");
		ConstructorDescriptor constr = new ConstructorDescriptor();
		constr.init(cd).addModifier("public");
		cd.addConstructor(constr);
		TestUtil.assertText(cd.getText(),
			"package com.myapp.domain;",
			"",
			"",
			"public class Client {",
			"",
			"",
			"	public Client() {",
			"	}",
			"",
			"",
			"}"
		);
	}

	public void testSimpleMethod() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.domain", "Client");
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(cd, TypeDescriptor.rVoid, "doIt").addModifier("public");
		cd.addMethod(meth);
		TestUtil.assertText(cd.getText(),
			"package com.myapp.domain;",
			"",
			"",
			"public class Client {",
			"",
			"",
			"",
			"	public void doIt() {",
			"	}",
			"",
			"}"
		);
	}
	
	public void testSimpleInternalInterface() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.action", "ClientAnimator");
		InternalInterfaceDescriptor iid = new InternalInterfaceDescriptor(cd, "Selector").addModifier("public");
		MethodDescriptor meth = new SignatureDescriptor();
		meth.init(iid, TypeDescriptor.Date, "getDate");
		iid.addMethod(meth);
		cd.addType(iid);
		TestUtil.assertText(cd.getText(),
			"package com.myapp.action;",
			"",
			"import java.util.Date;",
			"",
			"public class ClientAnimator {",
			"",
			"",
			"",
			"",
			"	public interface Selector {",
			"	",
			"		Date getDate();",
			"	}",
			"}"
		);
	}

	public void testSimpleInternalClass() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.action", "ClientAnimator");
		ClassDescriptor agency = new ClassDescriptor("com.objetdirect.domain", "Agency");
		InternalClassDescriptor selector = 
			new InternalClassDescriptor(cd, "AgencySelector");
		MethodDescriptor caller = new MethodDescriptor();
		caller.init(selector, agency, "selectAgency");
		caller.setContent("return null;");
		selector.addMethod(caller);
		cd.addType(selector);
		TestUtil.assertText(cd.getText(),
			"package com.myapp.action;",
			"",
			"import com.objetdirect.domain.Agency;",
			"",
			"public class ClientAnimator {",
			"",
			"",
			"",
			"",
			"	static class AgencySelector {",
			"	",
			"	",
			"	",
			"		Agency selectAgency() {",
			"			return null;",
			"		}",
			"	",
			"	}",
			"}"
		);
	}
	
	public void testSimpleClass() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.domain", "Client");
		AttributeDescriptor nameAttr = new AttributeDescriptor();
		nameAttr.init(cd, TypeDescriptor.String, "name"); 
		cd.addAttribute(nameAttr);
		ConstructorDescriptor constr = new ConstructorDescriptor();
		cd.addConstructor(constr.init(cd));
		constr = new ConstructorDescriptor();
		constr.init(cd).
			addModifier("public").
			addParameter(TypeDescriptor.String, "name").
			setContent("this.name = name;");
		cd.addConstructor(constr);
		cd.addMethod(StandardMethods.getter(nameAttr, "public"));
		cd.addMethod(StandardMethods.setter(nameAttr, "public"));
		cd.addType(new InternalInterfaceDescriptor(cd, "Selector").addModifier("public"));
		cd.addType(new InternalClassDescriptor(cd, "AgencySelector"));
		TestUtil.assertText(cd.getText(),
			"package com.myapp.domain;",
			"",
			"",
			"public class Client {",
			"",
			"	String name;",
			"",
			"	Client() {",
			"	}",
			"	",
			"	public Client(String name) {",
			"		this.name = name;",
			"	}",
			"",
			"	public String getName() {",
			"		return name;",
			"	}",
			"	",
			"	public void setName(String name) {",
			"		this.name = name;",
			"	}",
			"",
			"	public interface Selector {",
			"	",
			"	}",
			"	",
			"	static class AgencySelector {",
			"	",
			"	",
			"	",
			"	",
			"	}",
			"}"
		);
	}
	
}
