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

public class ClassDescriptor extends BasicClassDescriptor {
	
	ImportSet imports;
	
	static final String[] defaultPattern = {
		"package packageName;",
		"",
		"/// imports here",
		"",
		"/// annotations here",
		"public class ClassName extends SuperClass implements Interface {",
		"",
		"	/// attributes here",
		"",
		"	/// constructors here",
		"",
		"	/// methods here",
		"",
		"	/// types here",
		"}"
	};
	
	public ClassDescriptor(String packageName, NameMaker nameMaker, String ... pattern) {
		super(packageName, null, nameMaker.makePropertyName(), pattern.length==0 ? defaultPattern : pattern);
		imports = new ImportSet(packageName);
		setPattern(Rewrite.replace(getPattern(), "packageName", this.packageName));
	}
	
	public ClassDescriptor(String packageName, String name, String ... pattern) {
		this(packageName, new StandardNameMaker(name), pattern);
	}
	
	public ImportSet getImportSet() {
		return imports;
	}
	
	public String[] getText() {
		setPattern(Rewrite.insertLines(getPattern(), "/// imports here", imports.getText()));
		setPattern(super.getText());
		setPattern(Rewrite.removeLine(getPattern(), "/// imports here"));
		return getPattern();
	}
	
	public ClassDescriptor setSuperClass(TypeDescriptor superType) {
		super.setSuperClass(superType);
		return this;
	}

	public ClassDescriptor addInterface(TypeDescriptor interfaceType) {
		super.addInterface(interfaceType);
		return this;
	}

	public ClassDescriptor addAnnotation(TypeDescriptor type, String ... params) {
		super.addAnnotation(type, params);
		return this;
	}
	
	public ClassDescriptor addAnnotationParam(TypeDescriptor type, String param) {
		super.addAnnotationParam(type, param);
		return this;
	}
	
	public ClassDescriptor replace(String replaceThis, TypeDescriptor byThis) {
		super.replace(replaceThis, byThis);
		return this;
	}

	public ClassDescriptor replace(String ... replacements) {
		super.replace(replacements);
		return this;
	}
	

	public static String makeInternalTypeName(ClassDescriptor owner, NameMaker nameMaker) {
		String result;
		do {
			result = nameMaker.makePropertyName();
		} while (!owner.acceptTypeName(result));
		return result;
	}
	
}
