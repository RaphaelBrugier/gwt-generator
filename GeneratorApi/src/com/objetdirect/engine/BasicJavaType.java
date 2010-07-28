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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BasicJavaType extends TypeDescriptor {

	private String[] pattern;
	private MethodSet methods;
	private AttributeSet attributes;
	
	public BasicJavaType(String packageName, String classPath, String name, String ... pattern) {
		super(packageName, classPath, name);
		attributes = new AttributeSet(this);
		methods = new MethodSet(this);
	}
		
	List<AnnotationDescriptor> annotations = new ArrayList<AnnotationDescriptor>();
	Map<TypeDescriptor, AnnotationDescriptor> annotationMap = new HashMap<TypeDescriptor, AnnotationDescriptor>();
	
	protected abstract ImportSet getImportSet();
	
	public BasicJavaType addAnnotation(TypeDescriptor type, String ... params) {
		AnnotationDescriptor annotation = new AnnotationDescriptor(type);
		for (String param : params) {
			annotation.addParam(param);
		}
		annotation.setOwner(this);
		annotations.add(annotation);
		annotationMap.put(annotation.getType(), annotation);
		return this;
	}

	public BasicJavaType addAnnotationParam(TypeDescriptor type, String param) {
		annotationMap.get(type).addParam(param);
		return this;
	}
	
	public BasicJavaType addAnnotationParam(TypeDescriptor type, String param, TypeDescriptor paramType, String paramTypePattern) {
		getImportSet().addType(paramType);
		param = Rewrite.replace(param, paramTypePattern, paramType.getUsageName(this));
		addAnnotationParam(type, param);
		return this;
	}

	public BasicJavaType addInterface(TypeDescriptor interfaceType) {
		getImportSet().addType(interfaceType);
		pattern = Rewrite.replace(pattern, "Interface", interfaceType.getUsageName(this)+", Interface");
		return this;
	}
	
	protected String[] getPattern() {
		return pattern;
	}
	
	protected void setPattern(String[] pattern) {
		this.pattern = pattern;
	}
	
	public BasicJavaType replace(String replaceThis, TypeDescriptor byThis) {
		getImportSet().addType(byThis);
		this.pattern = Rewrite.replace(pattern, replaceThis, byThis.getUsageName(this));
		return this;
	}

	public BasicJavaType replace(String ... replacements) {
		this.pattern = Rewrite.replace(pattern, replacements);
		return this;
	}

	public boolean acceptAttributeName(String name) {
		return attributes.accept(name);
	}

	public AttributeSet getAttributes() {
		return attributes;
	}
	
	public void addAttribute(AttributeDescriptor attribute) {
		attributes.addAttribute(attribute);
	}
	
	public boolean acceptMethodName(String name) {
		return methods.accept(name);
	}
	
	public MethodSet getMethods() {
		return methods;
	}
	
	public void addMethod(MethodDescriptor method) {
		methods.addMethod(method);
	}
	
	public MethodDescriptor getMethod(String methodName) {
		return methods.getMethod(methodName);
	}

	Map<String, SemanticDescriptor> semantics = new HashMap<String, SemanticDescriptor>();
	
	public BasicJavaType addSemantic(SemanticDescriptor semantic) {
		this.semantics.put(semantic.getRequest(), semantic);
		return this;
	}
	
	public SemanticDescriptor getSemantic(String request) {
		return semantics.get(request);
	}
	
	public boolean recognizes(SemanticDescriptor semantic) {
		if (semantic==null)
			return false;
		SemanticDescriptor sem = this.semantics.get(semantic.getRequest());
		return semantic.equals(sem);
	}
	
}
