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

public class TestEnum extends TestCase {

	public void testSimpleEnum() {
		EnumDescriptor ed = new EnumDescriptor("com.objetdirect.domain", "EmployeeType")
			.addConstant("ENGINEER", "engineer")
			.addConstant("COMMERCIAL", "commercial")
			.addConstant("MANAGER", "manager");
		TestUtil.assertText(ed.getText(),
			"package com.objetdirect.domain;",
			"",
			"public enum EmployeeType {",
			"",
			"	ENGINEER(\"engineer\"), COMMERCIAL(\"commercial\"), MANAGER(\"manager\");",
			"",
			"	EmployeeType(String label) {",
			"		this.label = label;",
			"	}",
			"",
			"	String label;",
			"",
			"	public String getLabel() {",
			"		return label;",
			"	}",
			"}"
		);
	}
}