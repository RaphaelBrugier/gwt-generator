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

public abstract class BasicInterfaceDescriptor extends BasicJavaType {
	
	public BasicInterfaceDescriptor(String packageName, String classPath, String name, String ... pattern) {
		super(packageName, classPath, name);
		setPattern(Rewrite.replace(pattern, "InterfaceName", getTypeName()));
	}
	
	public String[] getText() {
		for (AnnotationDescriptor annotation : annotations) {
			setPattern(Rewrite.insertLines(getPattern(), "/// annotations here", annotation.getText()));
		}
		setPattern(Rewrite.insertLines(getPattern(), "/// constants here", getAttributes().getText()));
		setPattern(Rewrite.insertLines(getPattern(), "/// methods here", getMethods().getText()));
		setPattern(Rewrite.remove(getPattern(), "extends Interface "));
		setPattern(Rewrite.remove(getPattern(), ", Interface"));
		setPattern(Rewrite.removeLine(getPattern(), "/// annotations here"));
		setPattern(Rewrite.removeLine(getPattern(), "/// constants here"));
		setPattern(Rewrite.removeLine(getPattern(), "/// methods here"));
		return getPattern();
	}
	
	public BasicInterfaceDescriptor addInterface(TypeDescriptor interfaceType) {
		super.addInterface(interfaceType);
		return this;
	}

	public BasicInterfaceDescriptor addAnnotation(TypeDescriptor type, String ... params) {
		super.addAnnotation(type, params);
		return this;
	}

	public BasicInterfaceDescriptor addAnnotationParam(TypeDescriptor type, String param) {
		super.addAnnotationParam(type, param);
		return this;
	}
	
	public BasicInterfaceDescriptor addAnnotationParam(TypeDescriptor type, String param, TypeDescriptor paramType, String paramTypePattern) {
		super.addAnnotationParam(type, param,  paramType, paramTypePattern);
		return this;
	}

	public BasicInterfaceDescriptor replace(String replaceThis, TypeDescriptor byThis) {
		super.replace(replaceThis, byThis);
		return this;
	}

	public BasicInterfaceDescriptor replace(String ... replacements) {
		super.replace(replacements);
		return this;
	}

	public void addMethod(MethodDescriptor method) {
		if (method instanceof SignatureDescriptor)
			super.addMethod(method);
		else
			throw new GeneratorException("Interfaces can contain signature only");
	}
	
	public BasicInterfaceDescriptor addSemantic(SemanticDescriptor semantic) {
		super.addSemantic(semantic);
		return this;
	}
}
