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

public abstract class BasicClassDescriptor extends BasicJavaType  {
	ConstructorSet constructors;
	TypeSet types;
	
	public BasicClassDescriptor(String packageName, String classPath, String name, String ... pattern) {
		super(packageName, classPath, name);
		constructors = new ConstructorSet(this);
		types = new TypeSet(this);
		setPattern(Rewrite.replace(pattern, "ClassName", getTypeName()));
	}
	
	public String[] getText() {
		for (AnnotationDescriptor annotation : annotations) {
			setPattern(Rewrite.insertLines(getPattern(), "/// annotations here", annotation.getText()));
		}
		setPattern(Rewrite.insertLines(getPattern(), "/// attributes here", getAttributes().getText()));
		setPattern(Rewrite.insertLines(getPattern(), "/// constructors here", constructors.getText()));
		setPattern(Rewrite.insertLines(getPattern(), "/// methods here", getMethods().getText()));
		setPattern(Rewrite.insertLines(getPattern(), "/// types here", types.getText()));
		setPattern(Rewrite.remove(getPattern(), "extends SuperClass "));
		setPattern(Rewrite.remove(getPattern(), "implements Interface "));
		setPattern(Rewrite.remove(getPattern(), ", Interface"));
		setPattern(Rewrite.removeLine(getPattern(), "/// annotations here"));
		setPattern(Rewrite.removeLine(getPattern(), "/// attributes here"));
		setPattern(Rewrite.removeLine(getPattern(), "/// constructors here"));
		setPattern(Rewrite.removeLine(getPattern(), "/// methods here"));
		setPattern(Rewrite.removeLine(getPattern(), "/// types here"));
		return getPattern();
	}
	
	public BasicClassDescriptor setSuperClass(TypeDescriptor superType) {
		getImportSet().addType(superType);
		setPattern(Rewrite.replace(getPattern(), "SuperClass", superType.getUsageName(this)));
		return this;
	}

	public BasicClassDescriptor addInterface(TypeDescriptor interfaceType) {
		super.addInterface(interfaceType);
		return this;
	}

	public BasicClassDescriptor addAnnotation(TypeDescriptor type, String ... params) {
		super.addAnnotation(type, params);
		return this;
	}

	public BasicClassDescriptor addAnnotationParam(TypeDescriptor type, String param) {
		super.addAnnotationParam(type, param);
		return this;
	}
	
	public BasicClassDescriptor addAnnotationParam(TypeDescriptor type, String param, TypeDescriptor paramType, String paramTypePattern) {
		super.addAnnotationParam(type, param,  paramType, paramTypePattern);
		return this;
	}

	public BasicClassDescriptor replace(String replaceThis, TypeDescriptor byThis) {
		super.replace(replaceThis, byThis);
		return this;
	}

	public BasicClassDescriptor replace(String ... replacements) {
		super.replace(replacements);
		return this;
	}

	public boolean acceptTypeName(String name) {
		return types.accept(name);
	}

	public TypeSet getInternalTypes() {
		return types;
	}
	
	public ConstructorSet getConstructors() {
		return constructors;
	}

	public void addType(InternalTypeDescriptor type) {
		types.addType(type);
	}

	public void addConstructor(ConstructorDescriptor constructor) {
		constructors.addConstructor(constructor);
	}

	public BasicClassDescriptor addSemantic(SemanticDescriptor semantic) { 
		super.addSemantic(semantic);
		return this;
	}
	
}
