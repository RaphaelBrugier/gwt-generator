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

public class TestScriptDescriptor extends TestCase {

	public void testSimpleScript() {
		ClassDescriptor cd = new ClassDescriptor("com.objetdirect.domain","Agency");
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.action","Processor");
		ScriptDescriptor script = new ScriptDescriptor(
			"return new Entity(entityName);"
			)
			.setOwner(owner)
			.replace("entityName", "\"Nom de l'agence\"")
			.replace("Entity", cd);
		TestUtil.assertText(script.getText(),
			"return new Agency(\"Nom de l'agence\");"
		);
		TestUtil.assertExists(owner.getImportSet().getText(),
			"import com.objetdirect.domain.Agency;"
		);
	}
	
	public void testScript() {
		ClassDescriptor cd = new ClassDescriptor("com.objetdirect.domain","Agency");
		ClassDescriptor ce = new ClassDescriptor("com.acme.domain","Agency");
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.action","Processor");
		owner.getImportSet().addType(ce);
		ScriptDescriptor script = new ScriptDescriptor(
			"if (param==null) ",
			"	return new Entity(defaultName);",
			"else",
			"	return new Entity(param);"
			)
			.setOwner(owner)
			.replace("defaultName", "\"Nom de l'agence\"", "param", "name")
			.replace("Entity", cd);
		TestUtil.assertText(script.getText(),
			"if (name==null) ",
			"	return new com.objetdirect.domain.Agency(\"Nom de l'agence\");",
			"else",
			"	return new com.objetdirect.domain.Agency(name);"
		);
	}
}
