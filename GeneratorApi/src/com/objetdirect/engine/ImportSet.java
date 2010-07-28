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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ImportSet {
	
	String packageName;
	
	public ImportSet(String packageName) {
		this.packageName = packageName;
	}
	
	Map<String, TypeDescriptor> types = new TreeMap<String, TypeDescriptor>();
	Set<String> accepted = new HashSet<String>();
	
	public void addType(TypeDescriptor type) {
		if (type.getPackageName()==null)
			return;
		if (!accepted.contains(type.getTypeName())) {
			types.put(type.getPackageName()+":"+type.getTypeName(), type);
			accepted.add(type.getTypeName());
		}
		for (TypeDescriptor param : type.getParameters()) {
			addType(param);
		}
	}

	boolean isSpecialPackage(String packageName) {
			return packageName==null || 
				packageName.equals(TypeDescriptor.JAVA_LANG) || 
				packageName.equals(this.packageName);
	}
	
	public boolean accept(TypeDescriptor type) {
		if (isSpecialPackage(type.getPackageName()))
			return true;
		else
			return types.containsKey(type.getPackageName()+":"+type.getTypeName());
	}
	
	public List<TypeDescriptor> getTypes() {
		List<TypeDescriptor> result = new ArrayList<TypeDescriptor>();
		for (Map.Entry<String, TypeDescriptor> e : types.entrySet()) {
			result.add(e.getValue());
		}
		return result;
	}
	
	public String[] getText() {
		List<String> result = new ArrayList<String>();
		for (TypeDescriptor type : getTypes()) {
			if (!type.getPackageName().equals(packageName) && !type.getPackageName().equals(TypeDescriptor.JAVA_LANG))
				result.add(Rewrite.replace("import package.class;", "package", type.packageName, "class", type.getTypeName()));
		}
		return Rewrite.toArray(result);
	}
}
