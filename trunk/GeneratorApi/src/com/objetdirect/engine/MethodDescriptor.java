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

public class MethodDescriptor extends CodeDescriptor {
	TypeDescriptor returnType;
	NameMaker nameMaker;
	String name;
	
	static final String[] defaultPattern = new String[] {
		"/// annotations here",
		"modifier returnType methodName(paramType paramName) {",
		"	/// content here",
		"}"
	};
	String[] content = new String[0];
	
	public MethodDescriptor() {
	}
			
	public MethodDescriptor init(BasicJavaType owner, TypeDescriptor returnType, NameMaker nameMaker, String ... pattern) {
		super.init(owner, (pattern==null || pattern.length==0) ? defaultPattern : pattern);
		this.returnType = returnType;
		this.nameMaker = nameMaker;
		this.owner.getImportSet().addType(returnType);
		return this;
	}
	
	public MethodDescriptor init(BasicJavaType owner, TypeDescriptor returnType, String name, String ... pattern) {
		init(owner, returnType, new StandardNameMaker(name), pattern);
		return this;
	}

	public String getName() {
		if (name==null) {
			do {
				name = nameMaker.makeMemberName();
			} while (!owner.acceptMethodName(name));
		}
		return name;
	}
	
	public TypeDescriptor getReturnType() {
		return returnType;
	}

	public String[] getText() {
		setPattern(Rewrite.replace(getPattern(), "returnType", returnType.getUsageName(owner), "methodName", getName()));
		return super.getText();
	}

}
