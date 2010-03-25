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

public class TestInterfaceDescriptor extends TestCase {

	public void testEmptyClass() {
		InterfaceDescriptor id = new InterfaceDescriptor("com.objetdirect.engine", "IEngine");
		TestUtil.assertText(id.getText(),
			"package com.objetdirect.engine;",
			"",
			"",
			"public interface IEngine {",
			"",
			"}"
		);
	}
	
	public void testSuperInterfaces() {
		InterfaceDescriptor id = new InterfaceDescriptor("com.objetdirect.engine", "IEngine").
			addInterface(TypeDescriptor.Serializable).
			addInterface(TypeDescriptor.type("java.lang", "Cloneable"));
		TestUtil.assertText(id.getText(),
			"package com.objetdirect.engine;",
			"",
			"import java.io.Serializable;",
			"",
			"public interface IEngine extends Serializable, Cloneable {",
			"",
			"}"
		);
	}
	
	public void testAnnotations() {
		InterfaceDescriptor id = new InterfaceDescriptor("com.myapp.domain", "IService").
			addAnnotation(TypeDescriptor.type("javax.ejb", "Remote"));
		id.addAnnotation(TypeDescriptor.type("com.objetdirect.frmk", "Secured"));
		id.addAnnotationParam(TypeDescriptor.type("com.objetdirect.frmk", "Secured"),"true");
		TestUtil.assertText(id.getText(),
			"package com.myapp.domain;",
			"",
			"import com.objetdirect.frmk.Secured;",
			"import javax.ejb.Remote;",
			"",
			"@Remote",
			"@Secured(true)",
			"public interface IService {",
			"",
			"}"
		);
	}
	
	public void testSimpleConstants() {
		InterfaceDescriptor id = new InterfaceDescriptor("com.myapp.domain", "IService");
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(id, TypeDescriptor.String, "KEY").initWithPattern("\"Service\""); 
		id.addAttribute(attr);
		attr = new AttributeDescriptor();
		attr.init(id, TypeDescriptor.rInt, "CODE_NUMBER").initWithPattern("78"); ;
		id.addAttribute(attr);
		TestUtil.assertText(id.getText(),
			"package com.myapp.domain;",
			"",
			"",
			"public interface IService {",
			"	String KEY = \"Service\";",
			"	int CODE_NUMBER = 78;",
			"",
			"}"
		);
	}
	
	public void testSimpleMethod() {
		InterfaceDescriptor id = new InterfaceDescriptor("com.myapp.domain", "IService");
		SignatureDescriptor meth = new SignatureDescriptor();
		meth.init(id, TypeDescriptor.rVoid, "doIt").addModifier("public");
		id.addMethod(meth);
		TestUtil.assertText(id.getText(),
			"package com.myapp.domain;",
			"",
			"",
			"public interface IService {",
			"",
			"	void doIt();",
			"}"
		);
	}
	

	public void testSimpleInterface() {
		InterfaceDescriptor id = new InterfaceDescriptor("com.myapp.domain", "IService");
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(id, TypeDescriptor.String, "KEY").initWithPattern("\"Service\""); 
		id.addAttribute(attr);
		attr = new AttributeDescriptor();
		attr.init(id, TypeDescriptor.rInt, "CODE_NUMBER").initWithPattern("78"); ;
		id.addAttribute(attr);

		SignatureDescriptor doThis = new SignatureDescriptor();
		doThis.init(id, TypeDescriptor.rVoid, "doThis");
		id.addMethod(doThis);
		SignatureDescriptor doThat = new SignatureDescriptor();
		doThat.init(id, TypeDescriptor.String, "doThat");
		doThat.addParameter(TypeDescriptor.rBoolean, "confirm");
		id.addMethod(doThat);
		TestUtil.assertText(id.getText(),
			"package com.myapp.domain;",
			"",
			"",
			"public interface IService {",
			"	String KEY = \"Service\";",
			"	int CODE_NUMBER = 78;",
			"",
			"	void doThis();",
			"	",
			"	String doThat(boolean confirm);",
			"}"
		);
	}

}
