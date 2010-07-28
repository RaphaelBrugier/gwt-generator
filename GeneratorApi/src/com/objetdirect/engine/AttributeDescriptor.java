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

public class AttributeDescriptor extends CodeDescriptor {
	
	TypeDescriptor type;
	String name;
	
	static final String[] defaultPattern = new String[] {
		"/// annotations here",
		"modifier typeName attrName = initialization;"
	};
	
	public AttributeDescriptor() {
	}
	
	public void define() {
	}
	
	public AttributeDescriptor init(BasicJavaType owner, TypeDescriptor type, NameMaker nameMaker, String ... pattern) {
		super.init(owner, (pattern==null || pattern.length==0) ? defaultPattern : pattern);
		setContent(getPattern());
		this.type = type;
		this.name = makeName(nameMaker);
		this.owner.getImportSet().addType(type);
		return this;
	}

	public AttributeDescriptor init(BasicJavaType owner, TypeDescriptor type, String name, String ... pattern) {
		this.init(owner, type, new StandardNameMaker(name), pattern);
		return this;
	}

	public AttributeDescriptor define(BasicJavaType owner, TypeDescriptor type, String name, String ... pattern) {
		this.init(owner, type, name, pattern);
		owner.addAttribute(this);
		return this;
	}

	String makeName(NameMaker nameMaker) {
		String result;
		do {
			result = nameMaker.makeMemberName();
		} while (!owner.acceptAttributeName(result));
		return result;
	}
	
	public TypeDescriptor getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setPattern(String[] pattern) {
		super.setPattern(pattern);
		super.setContent(pattern);
	}
	
	public AttributeDescriptor setContent(String ... content) {
		setPattern(content);
		return this;
	}

	public String[] getText() {
		setPattern(Rewrite.replace(getPattern(), "typeName", type.getUsageName(owner)));
		setPattern(Rewrite.replace(getPattern(), "attrName", name));
		setPattern(Rewrite.remove(getPattern(), " = initialization"));
		setPattern(Rewrite.remove(getPattern(), ", param"));
		setPattern(Rewrite.remove(getPattern(), "param"));
		setPattern(Rewrite.remove(getPattern(), "modifier "));
		return super.getText();
	}

	public AttributeDescriptor addModifier(String modifier) {
		super.addModifier(modifier);
		return this;
	}
	
	public AttributeDescriptor initToNull() {
		setPattern(Rewrite.replace(getPattern(), "initialization", "null"));
		return this;
	}
	
	public AttributeDescriptor initWithNew(TypeDescriptor type, String ... params) {
		this.owner.getImportSet().addType(type);
		setPattern(Rewrite.replace(getPattern(), "initialization", "new Type(param)"));
		setPattern(Rewrite.replace(getPattern(), "Type", type.getUsageName(owner)));
		for (String param : params) {
			setPattern(Rewrite.replace(getPattern(), "param", param+", param"));
		}
		return this;
	}
	
	public AttributeDescriptor initWithPattern(String initPattern) {
		setPattern(Rewrite.replace(getPattern(), "initialization", initPattern));
		return this;
	}
	
}
