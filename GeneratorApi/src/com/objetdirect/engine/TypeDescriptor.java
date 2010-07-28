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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TypeDescriptor {

	public static final String JAVA_LANG = "java.lang";
	public static final String JAVA_UTIL = "java.util";
	public static final String JAVA_IO = "java.io";
	
	String packageName;
	String classPath = null;
	String typeName;
	TypeDescriptor[] parameters;
	int array;
	
	public TypeDescriptor(String packageName, String typeName, TypeDescriptor ... parameters) {
		this(packageName, null, typeName, 0, parameters);
	}

	public TypeDescriptor(String packageName, String classPath, String typeName, TypeDescriptor ... parameters) {
		this(packageName, classPath, typeName, 0, parameters);
	}
	
	public TypeDescriptor(String packageName, String classPath, String typeName, int array, TypeDescriptor ... parameters) {
		this.packageName = packageName;
		this.typeName = typeName;
		this.parameters = parameters;
		this.array = array;
		this.classPath = classPath;
	}

	public String getPackageName() {
		return packageName;
	}
	
	public String getClassPath() {
		return classPath;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	public int getArray() {
		return array;
	}
	
	public TypeDescriptor[] getParameters() {
		return parameters;
	}
	
	public Collection<TypeDescriptor> getTypes() {
		List<TypeDescriptor> types = new ArrayList<TypeDescriptor>();
		types.add(this);
		if (parameters!=null) {
			for (TypeDescriptor type : parameters)
				types.addAll(type.getTypes());
		}
		return types;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + array;
		result = prime * result
				+ ((classPath == null) ? 0 : classPath.hashCode());
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result + Arrays.hashCode(parameters);
		result = prime * result
				+ ((typeName == null) ? 0 : typeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeDescriptor other = (TypeDescriptor) obj;
		if (array != other.array)
			return false;
		if (classPath == null) {
			if (other.classPath != null)
				return false;
		} else if (!classPath.equals(other.classPath))
			return false;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		if (!Arrays.equals(parameters, other.parameters))
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}

	public String getString() {
		String result = typeName;
		if (parameters!=null && parameters.length>0) {
			result+="<";
			boolean first = true;
			for(TypeDescriptor type : parameters) {
				result+=type.getString();
				if (first) first=false;
				else result+=", ";
			}
			result+=">";
		}
		for (int i=0; i<array; i++)
			result+="[]";
		return result;
	}
	
	public static TypeDescriptor type(String packageName, String typeName, TypeDescriptor ... types) {
		return new TypeDescriptor(packageName, typeName, types);
	}
	
	public static final TypeDescriptor rVoid = new TypeDescriptor(null, "void");
	public static final TypeDescriptor rInt = new TypeDescriptor(null, "int");
	public static final TypeDescriptor rShort = new TypeDescriptor(null, "short");
	public static final TypeDescriptor rLong = new TypeDescriptor(null, "long");
	public static final TypeDescriptor rByte = new TypeDescriptor(null, "byte");
	public static final TypeDescriptor rChar = new TypeDescriptor(null, "char");
	public static final TypeDescriptor rBoolean = new TypeDescriptor(null, "boolean");
	public static final TypeDescriptor rFloat = new TypeDescriptor(null, "float");
	public static final TypeDescriptor rDouble = new TypeDescriptor(null, "double");
	
	public static final TypeDescriptor Int = new TypeDescriptor(JAVA_LANG, "Integer");
	public static final TypeDescriptor Short = new TypeDescriptor(JAVA_LANG, "Short");
	public static final TypeDescriptor Long = new TypeDescriptor(JAVA_LANG, "Long");
	public static final TypeDescriptor Byte = new TypeDescriptor(JAVA_LANG, "Byte");
	public static final TypeDescriptor Char = new TypeDescriptor(JAVA_LANG, "Character");
	public static final TypeDescriptor Boolean = new TypeDescriptor(JAVA_LANG, "Boolean");
	public static final TypeDescriptor Float = new TypeDescriptor(JAVA_LANG, "Float");
	public static final TypeDescriptor Double = new TypeDescriptor(JAVA_LANG, "Double");
	public static final TypeDescriptor String = new TypeDescriptor(JAVA_LANG, "String");
	public static final TypeDescriptor NullPointerException = new TypeDescriptor(JAVA_LANG,  "NullPointerException"); 
	public static final TypeDescriptor SuppressWarnings = new TypeDescriptor(JAVA_LANG,  "SuppressWarnings"); 
	
	public static final TypeDescriptor Date = new TypeDescriptor(JAVA_UTIL, "Date");
	public static TypeDescriptor ArrayList(TypeDescriptor type) {
		return type==null ? type(JAVA_UTIL, "ArrayList") : type(JAVA_UTIL, "ArrayList", type);
	}
	public static TypeDescriptor List(TypeDescriptor type) {
		return type==null ? type(JAVA_UTIL, "List") : type(JAVA_UTIL, "List", type);
	}
	public static TypeDescriptor Set(TypeDescriptor type) {
		return type==null ? type(JAVA_UTIL, "Set") : type(JAVA_UTIL, "Set", type);
	}
	public static TypeDescriptor HashSet(TypeDescriptor type) {
		return type==null ? type(JAVA_UTIL, "HashSet") : type(JAVA_UTIL, "HashSet", type);
	}
	public static TypeDescriptor Map(TypeDescriptor keyType, TypeDescriptor valueType) {
		return keyType==null ? type(JAVA_UTIL, "Map") : type(JAVA_UTIL, "Map", keyType, valueType);
	}
	public static TypeDescriptor HashMap(TypeDescriptor keyType, TypeDescriptor valueType) {
		return keyType==null ? type(JAVA_UTIL, "HashMap") : type(JAVA_UTIL, "HashMap", keyType, valueType);
	}
	public static final TypeDescriptor Collections = new TypeDescriptor(JAVA_UTIL, "Collections");
	public static final TypeDescriptor Serializable = new TypeDescriptor(JAVA_IO,  "Serializable");
	
	
	public static final TypeDescriptor array(TypeDescriptor type) {
		return new TypeDescriptor(type.getPackageName(), type.getClassPath(), type.getTypeName(), type.getArray()+1, type.getParameters());
	}

	public String getUsageName(BasicJavaType owner) {
		String usageName;
		if (packageName!=null && !owner.getImportSet().accept(this)) {
			if (classPath==null)
				usageName = packageName+"."+typeName;
			else
				usageName = packageName+"."+classPath+"."+typeName;
		}
		else {
			if (classPath!=null && !classPath.equals(owner.getTypeName()))
				usageName = classPath+"."+typeName;
			else
				usageName = typeName;
		}
		if (parameters.length>0) {
			usageName+="<";
			boolean first = true;
			for (TypeDescriptor type : parameters) {
				if (first) first=false;
				else usageName+=", ";
				usageName+=type.getUsageName(owner);
			}
			usageName+=">";
		}
		for (int i=0; i<getArray(); i++)
			usageName+="[]";
		return usageName;
	}
	
	
}
