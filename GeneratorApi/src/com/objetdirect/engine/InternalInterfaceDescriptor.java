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

public class InternalInterfaceDescriptor extends BasicInterfaceDescriptor implements InternalTypeDescriptor {

	static final String[] defaultPattern = {
		"/// annotations here",
		"modifier interface InterfaceName extends Interface {",
		"	/// constants here",
		"",
		"	/// methods here",
		"}"
	};
	
	ClassDescriptor owner;
	
	public InternalInterfaceDescriptor(
			ClassDescriptor owner, 
			NameMaker nameMaker,
			String ... pattern) 
	{
		super(owner.getPackageName(), owner.getTypeName(), 
			ClassDescriptor.makeInternalTypeName(owner, nameMaker), pattern.length==0 ? defaultPattern : pattern);
		this.owner = owner;
	}

	public InternalInterfaceDescriptor(ClassDescriptor owner, String name, String ... pattern) {
		this(owner, new StandardNameMaker(name), pattern);
	}
	
	@Override
	protected ImportSet getImportSet() {
		return owner.getImportSet();
	}

	public ClassDescriptor getOwner() {
		return owner;
	}

	public InternalInterfaceDescriptor addModifier(String modifier) {
		setPattern(Rewrite.replace(getPattern(), "modifier", modifier+" modifier"));
		return this;
	}
	
	public String[] getText() {
		setPattern(Rewrite.remove(getPattern(), "modifier "));
		return super.getText();
	}
		
	public InternalInterfaceDescriptor addInterface(TypeDescriptor interfaceType) {
		super.addInterface(interfaceType);
		return this;
	}

	public InternalInterfaceDescriptor addAnnotation(TypeDescriptor type, String ... params) {
		super.addAnnotation(type, params);
		return this;
	}

	public InternalInterfaceDescriptor addAnnotationParam(TypeDescriptor type, String param) {
		super.addAnnotationParam(type, param);
		return this;
	}
	
	public InternalInterfaceDescriptor addAnnotationParam(TypeDescriptor type, String param, TypeDescriptor paramType, String paramTypePattern) {
		super.addAnnotationParam(type, param,  paramType, paramTypePattern);
		return this;
	}

	public InternalInterfaceDescriptor replace(String replaceThis, TypeDescriptor byThis) {
		super.replace(replaceThis, byThis);
		return this;
	}

	public InternalInterfaceDescriptor replace(String ... replacements) {
		super.replace(replacements);
		return this;
	}

	public InternalInterfaceDescriptor addSemantic(SemanticDescriptor semantic) {
		super.addSemantic(semantic);
		return this;
	}
	
	
}
