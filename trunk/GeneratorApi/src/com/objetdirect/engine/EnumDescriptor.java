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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class EnumDescriptor extends TypeDescriptor {

	public EnumDescriptor(String packageName, String typeName) {
		super(packageName, typeName);
		pattern = defaultEnumPattern;
	}

	List<String[]> constants = new ArrayList<String[]>();
	Map<String, String> constantMap = new HashMap<String, String>();
	String[] pattern;
	
	public EnumDescriptor addConstant(String name, String label) {
		constants.add(new String[] {name, label});
		constantMap.put(name, label);
		return this;
	}
	
	public String getLabel(String name) {
		return constantMap.get(name);
	}
	
	public String[] getText() {
		for (String[] constant : constants) {
			pattern = Rewrite.replace(pattern, "LAST_CONSTANT", "A_CONSTANT(\"a_label\"), LAST_CONSTANT");
			pattern = Rewrite.replace(pattern, "A_CONSTANT", constant[0]);
			pattern = Rewrite.replace(pattern, "a_label", constant[1]);
		}
		pattern = Rewrite.replace(pattern, "packageName", this.getPackageName());
		pattern = Rewrite.replace(pattern, "EnumClass", this.getTypeName());
		pattern = Rewrite.remove(pattern, ", LAST_CONSTANT");
		return pattern;
	}

	String[] defaultEnumPattern = {
		"package packageName;",
		"",
		"public enum EnumClass {",
		"",
		"	LAST_CONSTANT;",
		"",
		"	EnumClass(String label) {",
		"		this.label = label;",
		"	}",
		"",
		"	String label;",
		"",
		"	public String getLabel() {",
		"		return label;",
		"	}",
		"}"
	};
}
