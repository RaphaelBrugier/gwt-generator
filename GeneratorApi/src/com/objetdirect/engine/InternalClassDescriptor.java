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

public class InternalClassDescriptor extends BasicClassDescriptor implements InternalTypeDescriptor {

	static final String[] defaultPattern = {
		"/// annotations here",
		"static class ClassName extends SuperClass implements Interface {",
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
	
	ClassDescriptor owner;
	
	public InternalClassDescriptor(
			ClassDescriptor owner, 
			NameMaker nameMaker,
			String ... pattern) 
	{
		super(owner.getPackageName(), owner.getTypeName(), 
			ClassDescriptor.makeInternalTypeName(owner, nameMaker), pattern.length==0 ? defaultPattern : pattern);
		this.owner = owner;
	}

	public InternalClassDescriptor(ClassDescriptor owner, String name, String ... pattern) {
		this(owner, new StandardNameMaker(name), pattern);
	}
	
	@Override
	protected ImportSet getImportSet() {
		return owner.getImportSet();
	}

	public ClassDescriptor getOwner() {
		return owner;
	}

	public InternalClassDescriptor addInterface(TypeDescriptor interfaceType) {
		super.addInterface(interfaceType);
		return this;
	}

	public InternalClassDescriptor addAnnotation(TypeDescriptor type, String ... params) {
		super.addAnnotation(type, params);
		return this;
	}

	public InternalClassDescriptor addAnnotationParam(TypeDescriptor type, String param) {
		super.addAnnotationParam(type, param);
		return this;
	}
	
	public InternalClassDescriptor addAnnotationParam(TypeDescriptor type, String param, TypeDescriptor paramType, String paramTypePattern) {
		super.addAnnotationParam(type, param,  paramType, paramTypePattern);
		return this;
	}

	public InternalClassDescriptor replace(String replaceThis, TypeDescriptor byThis) {
		super.replace(replaceThis, byThis);
		return this;
	}

	public InternalClassDescriptor replace(String ... replacements) {
		super.replace(replacements);
		return this;
	}

	public InternalClassDescriptor addSemantic(SemanticDescriptor semantic) {
		super.addSemantic(semantic);
		return this;
	}
}
